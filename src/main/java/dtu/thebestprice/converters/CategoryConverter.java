package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Category;
import dtu.thebestprice.payload.request.CategoryChildRequest;
import dtu.thebestprice.payload.request.CategoryParentRequest;
import dtu.thebestprice.payload.request.CategoryRequest;
import dtu.thebestprice.payload.response.ChildCategoryResponse;
import dtu.thebestprice.payload.response.ParentCategoryResponse;
import dtu.thebestprice.payload.response.ShortCategoryResponse;
import dtu.thebestprice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryConverter {
    @Autowired
    CategoryRepository categoryRepository;

    public ShortCategoryResponse toShortCategoryResponse(Category category) {
        if (category == null)
            return null;
        return new ShortCategoryResponse(category.getId(), category.getTitle());
    }

    public Category toCategoryParent(CategoryParentRequest categoryParentRequest) {
        Category category = new Category();

        category.setTitle(categoryParentRequest.getTitle());
        category.setDescription(categoryParentRequest.getDescription());

        return category;
    }

    public Category toCategoryParent(CategoryParentRequest categoryParentRequest, Category category) {

        category.setTitle(categoryParentRequest.getTitle());
        category.setDescription(categoryParentRequest.getDescription());

        return category;
    }

    public Category toCategoryChild(CategoryChildRequest categoryChildRequest) {
        Category category = new Category();

        category.setTitle(categoryChildRequest.getTitle());
        category.setDescription(categoryChildRequest.getDescription());

        Category parentCategory =
                categoryRepository
                        .findByIdAndCategoryIsNull(categoryChildRequest.getParentId());


        category.setCategory(parentCategory);


        return category;
    }

    public Category toCategoryChild(CategoryChildRequest categoryChildRequest, Category category) {

        category.setTitle(categoryChildRequest.getTitle());
        category.setDescription(categoryChildRequest.getDescription());

        // trường hợp cập nhật danh mục cha
        if (categoryChildRequest.getParentId() != category.getCategory().getId()) {
            Category parentCategory =
                    categoryRepository
                            .findByIdAndCategoryIsNull(categoryChildRequest.getParentId());

            category.setCategory(parentCategory);
        }

        return category;
    }

    public ChildCategoryResponse toChildCategoryResponse(Category category) {
        return new ChildCategoryResponse(
                category.getId(),
                category.getTitle(),
                category.getDescription()
        );
    }

    public ParentCategoryResponse toParentCategoryResponse(Category category, List<Category> childCategories) {
        ParentCategoryResponse parentCategoryResponse = new ParentCategoryResponse();
        parentCategoryResponse.setId(category.getId());
        parentCategoryResponse.setTitle(category.getTitle());
        parentCategoryResponse.setDescription(category.getDescription());

        List<ChildCategoryResponse> childCategoryResponseList = new ArrayList<>();
        if (childCategories != null && childCategories.size() > 0) {
            childCategories.forEach(categoryChild -> {
                childCategoryResponseList.add(this.toChildCategoryResponse(categoryChild));
            });
        }

        parentCategoryResponse.setCategories(childCategoryResponseList);

        return parentCategoryResponse;
    }


//    public Category toEntity(CategoryRequest categoryRequest) {
//        Category category = new Category();
//
//        category.setId(categoryRequest.getId());
//        category.setTitle(categoryRequest.getTitle());
//        category.setDescription(categoryRequest.getDescription());
//
//        // nếu id parent truyền vào là null. tức đó là category cha
//        if (categoryRequest.getParentId() == null) {
//            category.setCategory(null);
//        } else {
//            // ngược lại là category con
//            Category categoryParent = categoryRepository.findByIdAndCategoryIsNull(categoryRequest.getParentId());
//            if (categoryParent == null)
//                throw new RuntimeException("Id danh mục cha hiện tại không hợp lệ!");
//            category.setCategory(categoryParent);
//        }
//
//        return category;
//    }
}
