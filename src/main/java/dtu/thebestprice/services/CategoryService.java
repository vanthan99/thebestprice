package dtu.thebestprice.services;

import dtu.thebestprice.entities.Category;
import dtu.thebestprice.payload.request.CategoryRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    ApiResponse create(CategoryRequest categoryRequest);

    ApiResponse update(CategoryRequest categoryRequest, Long categoryId);
}
