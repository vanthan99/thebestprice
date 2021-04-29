package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.CategoryConverter;
import dtu.thebestprice.entities.Category;
import dtu.thebestprice.payload.request.CategoryChildRequest;
import dtu.thebestprice.payload.request.CategoryParentRequest;
import dtu.thebestprice.payload.request.CategoryRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.ParentCategoryResponse;
import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryConverter categoryConverter;

    @Override
    public ApiResponse create(CategoryRequest categoryRequest) {
        try {
            Category category = toEntity(categoryRequest);

            // check equal name
            if (categoryRepository.existsByTitleAndCategory(category.getTitle(), category.getCategory()))
                return new ApiResponse(false, "Already Exists Name In Same Level");

            categoryRepository.save(category);
            return new ApiResponse(true, "Created Successfully");

        } catch (IllegalArgumentException e) {
            return new ApiResponse(false, "parentId invalid");
        }
    }

    @Override
    public ApiResponse update(CategoryRequest categoryRequest, Long categoryId) {
        Category oldCategory;
        try {
            oldCategory = categoryRepository.findById(categoryId).orElse(null);
            if (oldCategory == null)
                return new ApiResponse(false, "Does not exist category");
        } catch (IllegalArgumentException exception) {
            return new ApiResponse(false, "Category Id Invalid");
        }

        Category newCategory;
        try {
            newCategory = toEntity(categoryRequest);
        } catch (IllegalArgumentException exception) {
            return new ApiResponse(false, "Category Parent Id Invalid");
        }

        // check current category name
        if (oldCategory.getTitle().equalsIgnoreCase(newCategory.getTitle()) || !categoryRepository.existsByTitleAndCategory(newCategory.getTitle(), newCategory.getCategory())) {
            oldCategory.setTitle(newCategory.getTitle());
            oldCategory.setDescription(newCategory.getDescription());
            oldCategory.setCategory(newCategory.getCategory());
            categoryRepository.save(oldCategory);
            return new ApiResponse(true, "Updated Successfully");
        }
        return new ApiResponse(false, "Already Exists Name In Same Level");
    }

    @Override
    public ResponseEntity<Object> saveParentCategory(CategoryParentRequest request) {
        // Trường hợp thêm mới category cha
        if (request.getId() == null) {

            // kiểm tra xem tên của danh mục có bị trùng với những danh mục cùng cấp khác?
            if (categoryRepository.existsByTitleAndCategoryIsNull(request.getTitle().trim()))
                throw new RuntimeException("Đã bị trùng tên với một tên danh mục cùng cấp khác");

            Category category = categoryConverter.toCategoryParent(request);
            categoryRepository.save(category);

            return ResponseEntity.ok(new ApiResponse(true, "Thêm mới danh mục cha thành công"));
        } else {
            // trường hợp cập nhật category cha

            // kiểm tra nếu id truyền vào thuộc danh mục con?
            if (categoryRepository.existsByIdAndCategoryIsNotNull(request.getId()))
                throw new RuntimeException("Id danh mục cần cập nhật thuộc vào danh mục con.");

            Category currentCategory = categoryRepository
                    .findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Không tồn tại id danh mục"));

            // kiểm tra xem tên sau khi thay đổi có bị trùng với danh mục cùng cấp không?
            if (categoryRepository.existsByTitleAndCategoryIsNull(request.getTitle().trim())
                    && !currentCategory.getTitle().equalsIgnoreCase(request.getTitle()))
                throw new RuntimeException("Têu đề danh mục mới đã bị trùng với những danh mục khác.");

            Category newCategory = categoryConverter.toCategoryParent(request, currentCategory);
            categoryRepository.save(newCategory);
            return ResponseEntity.ok(new ApiResponse(true, "Cập nhật danh mục cha thành công"));
        }
    }

    @Override
    public ResponseEntity<Object> saveChildCategory(CategoryChildRequest request) {
        if (request.getId() == null) {
            // trường hợp thêm mới

            if (!categoryRepository.existsByIdAndCategoryIsNull(request.getParentId()))
                throw new RuntimeException("Id parent không phải là danh mục cha");

            Category parentCategory = categoryRepository.getOne(request.getParentId());
            if (categoryRepository.existsByTitleAndCategory(request.getTitle(), parentCategory))
                throw new RuntimeException("Đã tồn tại tiêu đề danh mục cùng cấp.");

            Category category = categoryConverter.toCategoryChild(request);
            categoryRepository.save(category);

            return ResponseEntity.ok(new ApiResponse(true, "Thêm mới danh mục con thành công"));
        }

        // Trường hợp cập nhật
        else {
            if (categoryRepository.existsByIdAndCategoryIsNull(request.getId()))
                throw new RuntimeException("Id Không phải là danh mục con.");

            if (categoryRepository.existsByIdAndCategoryIsNotNull(request.getParentId()))
                throw new RuntimeException("Id parent không phải là danh mục cha");

            Category currentCategory = categoryRepository
                    .findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("id không tồn tại"));


            // truong hop update category parent
            if (currentCategory.getCategory().getId() != request.getParentId()) {

                // kiểm tra title có bị trùng khi có cùng danh mục cha
                if (categoryRepository.existsByTitleAndCategory(
                        request.getTitle(),
                        categoryRepository.getOne(request.getParentId())
                ))
                    throw new RuntimeException("Tiêu đề đã bị trùng khi chuyển sang danh mục cha khác.");
            }

            Category parentCategory = categoryRepository.getOne(request.getParentId());
            // kiểm tra tiêu đề danh mục có bị trung nếu trường hợp cập nhật tiêu đề
            if (categoryRepository.existsByTitleAndCategory(request.getTitle(), parentCategory)
                    && !currentCategory.getTitle().equalsIgnoreCase(request.getTitle())
            )
                throw new RuntimeException("Tiêu đề đã bị trùng.");

            Category newCategory = categoryConverter.toCategoryChild(request, currentCategory);

            categoryRepository.save(newCategory);

            return ResponseEntity.ok(new ApiResponse(true, "Cập nhật danh mục con thành công"));
        }
    }

    @Override
    public List<ParentCategoryResponse> listCategoryIsActive() {
        List<ParentCategoryResponse> list = new ArrayList<>();

        List<Category> parentCategories = categoryRepository.findByCategoryIsNullAndDeleteFlgFalse();
        parentCategories.forEach(parentCategory -> {
            list.add(categoryConverter.toParentCategoryResponse(
                    parentCategory,
                    categoryRepository.findByCategoryAndDeleteFlgFalse(parentCategory)
            ));
        });

        return list;
    }

    public Category toEntity(CategoryRequest categoryRequest) throws IllegalArgumentException {
        Category category = new Category();
        category.setTitle(categoryRequest.getTitle());
        category.setDescription(categoryRequest.getDescription());

        if (categoryRequest.getParentId() == null) {
            category.setCategory(null);
        } else {
            category.setCategory(categoryRepository.findById(categoryRequest.getParentId()).orElse(null));
        }
        return category;
    }
}
