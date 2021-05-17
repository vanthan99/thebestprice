package dtu.thebestprice.services;

import dtu.thebestprice.payload.request.brand.BrandRequest;
import org.springframework.http.ResponseEntity;

public interface BrandService {

    // danh sách tất cả brand đang hoạt động
    ResponseEntity<Object> findAllBrandByEnable(boolean enable);

    // admin thêm mới brand
    ResponseEntity<Object> adminCreateBrand(BrandRequest brandRequest);

    // admin update brand id
    ResponseEntity<Object> adminUpdateBrand(String strBrandId, BrandRequest brandRequest);

    // admin bật tăt trạng thái hoạt động của brand
    ResponseEntity<Object> adminToggleBrand(String strBrandId);

    // admin xóa nhà sản xuất
    ResponseEntity<Object> adminDeleteBrand(String strBrandId);
}
