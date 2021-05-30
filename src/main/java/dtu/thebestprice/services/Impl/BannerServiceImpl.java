package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.BannerConverter;
import dtu.thebestprice.entities.Banner;
import dtu.thebestprice.payload.request.BannerRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.BannerResponse;
import dtu.thebestprice.repositories.BannerRepository;
import dtu.thebestprice.services.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    BannerRepository bannerRepository;

    @Autowired
    BannerConverter bannerConverter;

    @Override
    public ResponseEntity<Object> createNew(BannerRequest request) {
        // kiểm tra xem tiêu đề của banner mới có bị trùng hay không?
        //// LÀM SAU
        Banner banner = bannerConverter.toEntity(request);
        banner.setEnable(true);
        bannerRepository.save(banner);
        return ResponseEntity.ok(new ApiResponse(true, "Thêm mới banner thành công"));
    }

    @Override
    public ResponseEntity<Object> findByEnable(boolean enable) {
        return ResponseEntity.ok(
                bannerRepository
                        .findByDeleteFlgFalseAndEnableOrderByCreatedAtDesc(enable)
                        .stream().map(banner -> bannerConverter.toBannerResponse(banner))
        );
    }

    @Override
    public ResponseEntity<Object> update(long bannerId, BannerRequest request) {
        Banner banner = bannerRepository
                .findById(bannerId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại banner"));

        Banner newBanner = bannerConverter.toEntity(request, banner);
        bannerRepository.save(newBanner);

        return ResponseEntity.ok(new ApiResponse(true, "Chỉnh sửa banner thành công"));
    }

    @Override
    public ResponseEntity<Object> deleteById(long bannerId) {
        Banner banner = bannerRepository
                .findById(bannerId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại banner"));

        banner.setDeleteFlg(true);

        bannerRepository.save(banner);
        return ResponseEntity.ok(new ApiResponse(true, "Xóa banner thành công"));
    }

    @Override
    public ResponseEntity<Object> switchEnable(long bannerId) {
        Banner banner = bannerRepository
                .findById(bannerId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại banner"));

        if (banner.isEnable()) {
            banner.setEnable(false);
            bannerRepository.save(banner);
            return ResponseEntity.ok(new ApiResponse(true, "Tắt trạng thái hoạt động của banner thành công"));
        }

        banner.setEnable(true);
        bannerRepository.save(banner);
        return ResponseEntity.ok(new ApiResponse(true, "bật trạng thái hoạt động của banner thành công"));
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable) {
        Page<BannerResponse> page
                = bannerRepository
                .findByDeleteFlgFalse(pageable)
                .map(banner -> bannerConverter.toBannerResponse(banner));
        return ResponseEntity.ok(page);
    }
}
