package dtu.thebestprice.controllers;

import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.services.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/category")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @ApiOperation(value = "Danh sách tất cả danh mục")
    @GetMapping
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok(categoryRepository.findByCategoryIsNull());
    }
}
