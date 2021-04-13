package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Category;
import dtu.thebestprice.payload.request.CategoryRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.CategoryResponse;
import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

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
