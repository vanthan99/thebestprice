package dtu.thebestprice.controllers;

import dtu.thebestprice.payload.request.CategoryChildRequest;
import dtu.thebestprice.payload.request.CategoryParentRequest;
import dtu.thebestprice.repositories.CategoryRepository;
import dtu.thebestprice.services.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Thêm mới danh mục cha")
    public ResponseEntity<Object> createParentCategory(
            @RequestBody @Valid CategoryParentRequest request
    ) {
        return categoryService.createParentCategory(request);
    }

    @PutMapping("/parent/{parentCatId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Chỉnh sửa danh mục cha")
    public ResponseEntity<Object> updateParentCategory(
            @RequestBody @Valid CategoryParentRequest request,
            @PathVariable("parentCatId") Long parentId
    ) {
        return categoryService.updateParentCategory(request, parentId);
    }

    @PostMapping("/child")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Thêm mới sửa danh mục con")
    public ResponseEntity<Object> createChildCategory(
            @RequestBody @Valid CategoryChildRequest request
    ) {
        return categoryService.createChildCategory(request);
    }

    @PutMapping("/child/{childCatId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Chỉnh sửa danh mục con")
    public ResponseEntity<Object> updateChildCategory(
            @RequestBody @Valid CategoryChildRequest request,
            @PathVariable("childCatId") Long childId
    ) {
        return categoryService.updateChildCategory(request, childId);
    }

    @DeleteMapping
    @ApiOperation(value = "Xóa danh mục")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteById(@RequestParam("id") Long categoryId) {
        return categoryService.deleteById(categoryId);
    }

}
