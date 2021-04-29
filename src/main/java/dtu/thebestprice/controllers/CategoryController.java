package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.CategoryChildRequest;
import dtu.thebestprice.payload.request.CategoryParentRequest;
import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.services.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/category")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @ApiOperation(value = "Danh sách tất cả danh mục")
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(categoryService.listCategoryIsActive());
    }

    @PostMapping("/parent")
    @ApiOperation(value = "Thêm mới hoặc chỉnh sửa danh mục cha")
    public ResponseEntity<Object> saveParentCategory(
            @RequestBody @Valid CategoryParentRequest request
    ) {
        return categoryService.saveParentCategory(request);
    }

    @PostMapping("/child")
    @ApiOperation(value = "Thêm mới hoặc chỉnh sửa danh mục con")
    public ResponseEntity<Object> saveChildCategory(
            @RequestBody @Valid CategoryChildRequest request
    ) {
        return categoryService.saveChildCategory(request);
    }

    @DeleteMapping
    @ApiOperation(value = "Xóa danh mục")
    public ResponseEntity<Object> deleteById(@RequestParam("id") Long categoryId) {
        return categoryService.deleteById(categoryId);
    }

}
