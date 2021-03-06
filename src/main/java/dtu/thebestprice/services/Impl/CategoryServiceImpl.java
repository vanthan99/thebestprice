package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.CategoryConverter;
import dtu.thebestprice.entities.Category;
import dtu.thebestprice.payload.request.CategoryChildRequest;
import dtu.thebestprice.payload.request.CategoryParentRequest;
import dtu.thebestprice.payload.request.CategoryRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.ChildCategoryResponse;
import dtu.thebestprice.payload.response.ParentCategoryResponse;
import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
            if (categoryRepository.existsByDeleteFlgFalseAndTitleAndCategory(category.getTitle(), category.getCategory()))
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
        if (oldCategory.getTitle().equalsIgnoreCase(newCategory.getTitle()) || !categoryRepository.existsByDeleteFlgFalseAndTitleAndCategory(newCategory.getTitle(), newCategory.getCategory())) {
            oldCategory.setTitle(newCategory.getTitle());
            oldCategory.setDescription(newCategory.getDescription());
            oldCategory.setCategory(newCategory.getCategory());
            categoryRepository.save(oldCategory);
            return new ApiResponse(true, "Updated Successfully");
        }
        return new ApiResponse(false, "Already Exists Name In Same Level");
    }

    @Override
    public ResponseEntity<Object> createChildCategory(CategoryChildRequest request) {
        if (!categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNull(request.getParentId()))
            throw new RuntimeException("Id parent kh??ng ph???i l?? danh m???c cha");

        Category parentCategory = categoryRepository.getOne(request.getParentId());
        if (categoryRepository.existsByDeleteFlgFalseAndTitleAndCategory(request.getTitle(), parentCategory))
            throw new RuntimeException("???? t???n t???i ti??u ????? danh m???c c??ng c???p.");

        Category category = categoryConverter.toCategoryChild(request);
        categoryRepository.save(category);

        return ResponseEntity.ok(new ApiResponse(true, "Th??m m???i danh m???c con th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> updateChildCategory(CategoryChildRequest request, Long childCategoryId) {
        if (categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNull(childCategoryId))
            throw new RuntimeException("Id Kh??ng ph???i l?? danh m???c con.");

        if (categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNotNull(request.getParentId()))
            throw new RuntimeException("Id parent kh??ng ph???i l?? danh m???c cha");

        Category currentCategory = categoryRepository
                .findById(childCategoryId)
                .orElseThrow(() -> new RuntimeException("id kh??ng t???n t???i"));


        // truong hop update category parent
        if (currentCategory.getCategory().getId() != request.getParentId()) {

            // ki???m tra title c?? b??? tr??ng khi c?? c??ng danh m???c cha
            if (categoryRepository.existsByDeleteFlgFalseAndTitleAndCategory(
                    request.getTitle(),
                    categoryRepository.getOne(request.getParentId())
            ))
                throw new RuntimeException("Ti??u ????? ???? b??? tr??ng khi chuy???n sang danh m???c cha kh??c.");
        }

        Category parentCategory = categoryRepository.getOne(request.getParentId());
        // ki???m tra ti??u ????? danh m???c c?? b??? trung n???u tr?????ng h???p c???p nh???t ti??u ?????
        if (categoryRepository.existsByDeleteFlgFalseAndTitleAndCategory(request.getTitle(), parentCategory)
                && !currentCategory.getTitle().equalsIgnoreCase(request.getTitle())
        )
            throw new RuntimeException("Ti??u ????? ???? b??? tr??ng.");

        Category newCategory = categoryConverter.toCategoryChild(request, currentCategory);

        categoryRepository.save(newCategory);

        return ResponseEntity.ok(new ApiResponse(true, "C???p nh???t danh m???c con th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> createParentCategory(CategoryParentRequest request) {
        // ki???m tra xem t??n c???a danh m???c c?? b??? tr??ng v???i nh???ng danh m???c c??ng c???p kh??c?
        if (categoryRepository.existsByDeleteFlgFalseAndTitleAndCategoryIsNull(request.getTitle().trim()))
            throw new RuntimeException("???? b??? tr??ng t??n v???i m???t t??n danh m???c c??ng c???p kh??c");

        Category category = categoryConverter.toCategoryParent(request);
        categoryRepository.save(category);

        return ResponseEntity.ok(new ApiResponse(true, "Th??m m???i danh m???c cha th??nh c??ng"));
    }


    @Override
    public ResponseEntity<Object> updateParentCategory(CategoryParentRequest request, Long parentCategoryId) {
        // tr?????ng h???p c???p nh???t category cha

        // ki???m tra n???u id truy???n v??o thu???c danh m???c con?
        if (categoryRepository.existsByDeleteFlgFalseAndIdAndCategoryIsNotNull(parentCategoryId))
            throw new RuntimeException("Id danh m???c c???n c???p nh???t thu???c v??o danh m???c con.");

        Category currentCategory = categoryRepository
                .findById(parentCategoryId)
                .orElseThrow(() -> new RuntimeException("Kh??ng t???n t???i id danh m???c"));

        // ki???m tra xem t??n sau khi thay ?????i c?? b??? tr??ng v???i danh m???c c??ng c???p kh??ng?
        if (categoryRepository.existsByDeleteFlgFalseAndTitleAndCategoryIsNull(request.getTitle().trim())
                && !currentCategory.getTitle().equalsIgnoreCase(request.getTitle()))
            throw new RuntimeException("T??u ????? danh m???c m???i ???? b??? tr??ng v???i nh???ng danh m???c kh??c.");

        Category newCategory = categoryConverter.toCategoryParent(request, currentCategory);
        categoryRepository.save(newCategory);
        return ResponseEntity.ok(new ApiResponse(true, "C???p nh???t danh m???c cha th??nh c??ng"));
    }

    @Override
    public List<ParentCategoryResponse> listCategoryIsActive() {
        List<ParentCategoryResponse> list = new ArrayList<>();

        List<Category> parentCategories = categoryRepository.findByCategoryIsNullAndDeleteFlgFalseOrderByTitle();
        parentCategories.forEach(parentCategory -> {
            list.add(categoryConverter.toParentCategoryResponse(
                    parentCategory,
                    categoryRepository.findByCategoryAndDeleteFlgFalseOrderByTitle(parentCategory)
            ));
        });


        return list;
    }

    @Override
    public ResponseEntity<Object> deleteById(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Id danh m???c kh??ng t???n t???i"));

        category.setDeleteFlg(true);
        categoryRepository.save(category);

        // n???u x??a category cha th?? x??a luoon category con
        if (category.getCategory() == null) {
            categoryRepository.deleteChildCategoryByParentId(category.getId());
        }

        return ResponseEntity.ok(new ApiResponse(true, "X??a danh m???c th??nh c??ng"));
    }

    @Override
    public ResponseEntity<Object> listChildCategory() {
        List<Category> categories = categoryRepository.findByCategoryIsNotNullAndDeleteFlgFalse();
        List<ChildCategoryResponse> result = categories
                .stream()
                .map(category -> categoryConverter.toChildCategoryResponse(category))
                .sorted(Comparator.comparing(ChildCategoryResponse::getTitle))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
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
