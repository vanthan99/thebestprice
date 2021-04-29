package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.CategoryChildRequest;
import dtu.thebestprice.payload.request.CategoryParentRequest;
import dtu.thebestprice.payload.request.CategoryRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.ParentCategoryResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ApiResponse create(CategoryRequest categoryRequest);

    ApiResponse update(CategoryRequest categoryRequest, Long categoryId);

    ResponseEntity<Object> saveParentCategory(CategoryParentRequest request);

    ResponseEntity<Object> saveChildCategory(CategoryChildRequest request);

    List<ParentCategoryResponse> listCategoryIsActive();
}
