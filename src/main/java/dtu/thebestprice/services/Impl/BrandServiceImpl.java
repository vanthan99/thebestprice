package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.BrandConverter;
import dtu.thebestprice.entities.Brand;
import dtu.thebestprice.payload.request.brand.BrandRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.BrandResponse;
import dtu.thebestprice.repositories.BrandRepository;
import dtu.thebestprice.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    BrandConverter brandConverter;

    @Override
    public ResponseEntity<Object> findAllBrandByEnable(boolean enable) {
        List<Brand> brandListEnableTrue = brandRepository.findByDeleteFlgFalseAndEnable(enable);
        List<BrandResponse> brandResponses = brandListEnableTrue
                .stream()
                .map(brand -> brandConverter.toBrandResponse(brand))
                .collect(Collectors.toList());

        return ResponseEntity.ok(brandResponses);
    }

    @Override
    public ResponseEntity<Object> adminCreateBrand(BrandRequest brandRequest) {
        // kiểm tra xem có cùng tên brand trươc đó không
        if (brandRepository.existsByName(brandRequest.getName().toUpperCase()))
            throw new RuntimeException("Đã tồn tại tên nhà sản xuất này");

        Brand brand = new Brand(brandRequest.getName(), brandRequest.getDescription(), true);

        brandRepository.save(brand);
        return ResponseEntity.ok(new ApiResponse(true, "Thêm mới nhà sản xuất thành công"));
    }

    @Override
    public ResponseEntity<Object> adminUpdateBrand(String strBrandId, BrandRequest brandRequest) {
        long brandId;
        try {
            brandId = Long.parseLong(strBrandId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id của nhà sản xuất phải là số nguyên");
        }

        Brand brand = brandRepository
                .findById(brandId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại nhà sản xuất"));

        // kiểm tra xem trước và sau khi cập nhật có khác nhau không
        if (brand.getName().equalsIgnoreCase(brandRequest.getName()) && brand.getDescription().equalsIgnoreCase(brandRequest.getDescription()))
            throw new RuntimeException("Trước và sau khi cập nhật không có thay đổi");

        // kiểm tra xem tên mới có bị trùng với tên nhà sản xuất khác hay không
        if (brandRepository.existsByName(brandRequest.getName().toUpperCase()) && !brand.getName().equalsIgnoreCase(brandRequest.getName()))
            throw new RuntimeException("Tên nhà sản xuất đã cập nhật với một nhà sản xuất khác");


        Brand newBrand = brandConverter.toEntity(brandRequest, brand);

        brandRepository.save(newBrand);
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật nhà sản xuất thành công"));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminToggleBrand(String strBrandId) {
        long brandId;
        try {
            brandId = Long.parseLong(strBrandId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id của nhà sản xuất phải là số nguyên");
        }

        Brand brand = brandRepository
                .findById(brandId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại nhà sản xuất"));

        String message = "";
        if (brand.isEnable())
            message = "Tắt trạng thái hoạt động của nhà sản xuất thành công";
        else message = "Bật trạng thái hoạt động của nhà sản xuất thành công";

        brand.setEnable(!brand.isEnable());

        brandRepository.save(brand);

        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @Override
    @Transactional
    public ResponseEntity<Object> adminDeleteBrand(String strBrandId) {
        long brandId;
        try {
            brandId = Long.parseLong(strBrandId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Id của nhà sản xuất phải là số nguyên");
        }

        Brand brand = brandRepository
                .findById(brandId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại nhà sản xuất"));

        if (brand.isDeleteFlg())
            throw new RuntimeException("Nhà sản xuất đã bị xóa trước đó");
        brand.setDeleteFlg(true);
        brandRepository.save(brand);

        return ResponseEntity.ok(new ApiResponse(true, "Xóa nhà sản xuất thành công"));
    }
}
