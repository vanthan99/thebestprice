package dtu.thebestprice.services.Impl;

import com.sun.javafx.iio.gif.GIFImageLoaderFactory;
import dtu.thebestprice.converters.RetailerConverter;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.RetailerRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.RetailerResponse;
import dtu.thebestprice.repositories.RetailerRepository;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.services.RetailerService;
import dtu.thebestprice.services.UserService;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RetailerServiceImpl implements RetailerService {
    @Autowired
    RetailerConverter retailerConverter;

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Override
    public Set<RetailerResponse> findAll() {
        return retailerRepository.findByDeleteFlgFalse().stream().map(retailer -> retailerConverter.toRetailerResponse(retailer)).collect(Collectors.toSet());
    }

    @Override
    public ResponseEntity<Object> deleteById(String id) {
        Long retailerId = null;
        try {
            retailerId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("id của nhà cung cấp không đúng định dạng");
        }

        /*
         * Kiểm tra id có tồn tại?
         * */
        if (!retailerRepository.existsById(retailerId))
            return ResponseEntity.status(404).body(new ApiResponse(false, "id của nhà cung cấp không tồn tại"));

        Retailer retailer = retailerRepository.getOne(retailerId);
        retailer.setDeleteFlg(true);
        retailerRepository.save(retailer);

        return ResponseEntity.ok().body(new ApiResponse(true, "Xóa nhà cung cấp: " + retailer.getName() + " thành công"));
    }

    @Override
    public ResponseEntity<Object> create(RetailerRequest retailerRequest, Long userId) {
        // kiểm tra xem có bị trùng tên với các nhà bán lẽ khác hay không
        if (retailerRepository.existsByName(retailerRequest.getName().trim()))
            throw new RuntimeException("Tên nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập tên khác");

        // kiểm tra xem có bị trùng logo với các nhà bán lẽ khác hay không?
        if (retailerRepository.existsByLogoImage(retailerRequest.getLogo().trim()))
            throw new RuntimeException("logo của nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập logo khác");

        // kiểm tra xem có bị trung homepage với nhà bán lẽ khác hay không?
        if (retailerRepository.existsByHomePage(retailerRequest.getHomePage().trim()))
            throw new RuntimeException("Homepage nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập Homepage khác");

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại user"));

        Retailer retailer = retailerConverter.toEntity(retailerRequest);

        retailer.setUser(user);

        retailerRepository.save(retailer);
        return ResponseEntity.ok(new ApiResponse(true, "Thêm mới thành công"));
    }

    @Override
    public ResponseEntity<Object> create(RetailerRequest retailerRequest, User user, boolean approve, boolean isCheckValidate) {
        if (isCheckValidate) {
            // kiểm tra xem có bị trùng tên với các nhà bán lẽ khác hay không
            if (retailerRepository.existsByName(retailerRequest.getName().trim()))
                throw new RuntimeException("Tên nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập tên khác");

            // kiểm tra xem có bị trùng logo với các nhà bán lẽ khác hay không?
            if (retailerRepository.existsByLogoImage(retailerRequest.getLogo().trim()))
                throw new RuntimeException("logo của nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập logo khác");

            // kiểm tra xem có bị trung homepage với nhà bán lẽ khác hay không?
            if (retailerRepository.existsByHomePage(retailerRequest.getHomePage().trim()))
                throw new RuntimeException("Homepage nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập Homepage khác");
        }

        Retailer retailer = retailerRepository.save(retailerConverter.toEntity(retailerRequest));
        retailer.setApprove(approve);
        retailer.setUser(user);
        retailerRepository.save(retailer);

        return ResponseEntity.ok(new ApiResponse(true, "Thêm mới nhà bán lẽ thành công"));
    }

    @Override
    public boolean validateWhileCreateRetailer(RetailerRequest request) {
        // kiểm tra xem có bị trùng tên với các nhà bán lẽ khác hay không
        if (retailerRepository.existsByName(request.getName().trim()))
            throw new RuntimeException("Tên nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập tên khác");

        // kiểm tra xem có bị trùng logo với các nhà bán lẽ khác hay không?
        if (retailerRepository.existsByLogoImage(request.getLogo().trim()))
            throw new RuntimeException("logo của nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập logo khác");

        // kiểm tra xem có bị trung homepage với nhà bán lẽ khác hay không?
        if (retailerRepository.existsByHomePage(request.getHomePage().trim()))
            throw new RuntimeException("Homepage nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập Homepage khác");
        return true;
    }

    @Override
    public ResponseEntity<Object> update(RetailerRequest retailerRequest, Long retailerId) {
        Retailer retailer = retailerRepository
                .findById(retailerId)
                .orElseThrow(() -> new RuntimeException("id của nhà bán lẽ không tồn tại"));

        long userId;
        try {
            userId = Long.parseLong(retailerRequest.getUserId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("id user không tồn tại");
        }

        User user = userRepository.getOne(userId);

        // kiểm tra xem thông tin trước và sau khi update có giống nhau hay không?
        if (retailerRepository.existsByIdAndNameAndDescriptionAndLogoImageAndHomePageAndUser(
                retailerId,
                retailerRequest.getName(),
                retailerRequest.getDescription(),
                retailerRequest.getLogo(),
                retailerRequest.getHomePage(),
                user
        ))
            throw new RuntimeException("Thông tin trước và sau khi thay đổi là giống nhau");

        // kiểm tra tên mới có trùng với tên của một nhà bán lẽ khác hay không?
        if (
                retailerRepository.existsByName(retailerRequest.getName())
                        && !retailer.getName().equalsIgnoreCase(retailerRequest.getName())
        )
            throw new RuntimeException("Tên nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập tên khác");

        // kiểm tra hình ảnh có trung với tên của một nhà bán lẽ khác hay không?
        if (
                retailerRepository.existsByLogoImage(retailerRequest.getLogo())
                        && !retailer.getLogoImage().equalsIgnoreCase(retailerRequest.getLogo())
        )
            throw new RuntimeException("logo của nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập logo khác");

        // kiểm tra homepage có trùng với homepage của nhà bán lẽ khác hay không?
        if (
                retailerRepository.existsByHomePage(retailerRequest.getHomePage())
                        && !retailer.getHomePage().equalsIgnoreCase(retailerRequest.getHomePage())
        )
            throw new RuntimeException("Homepage nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập Homepage khác");


        retailerRepository.save(retailerConverter.toEntity(retailerRequest, retailer));
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật nhà bán lẽ thành công"));
    }

    @Override
    public ResponseEntity<Object> pageRetailerByApprove(boolean approve, Pageable pageable) {
        return ResponseEntity.ok(
                retailerRepository
                        .findByDeleteFlgFalseAndApproveOrderByCreatedAt(approve, pageable)
                        .map(retailer -> retailerConverter.toRetailerResponse(retailer))
        );
    }

    @Override
    @Transactional
    public ResponseEntity<Object> approveRetailer(Long retailerId) {
        Retailer retailer = retailerRepository
                .findById(retailerId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại nhà bán lẽ"));
        if (retailer.isApprove())
            throw new RuntimeException("Nhà bán lẽ được được phê duyệt trước đó");

        retailer.setApprove(true);
        retailerRepository.save(retailer);

        // nếu mà user là guest thì update lên role retailer
        User user = retailer.getUser();
        if (user.getRole().equals(ERole.ROLE_GUEST)) {
            user.setRole(ERole.ROLE_RETAILER);
            userRepository.save(user);
        }

        return ResponseEntity.ok(new ApiResponse(true, "Xác nhận nhà bán lẽ thành công"));
    }
}
