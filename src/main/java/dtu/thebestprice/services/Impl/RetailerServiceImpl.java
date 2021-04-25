package dtu.thebestprice.services.Impl;

import dtu.thebestprice.converters.RetailerConverter;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.payload.request.RetailerRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.RetailerResponse;
import dtu.thebestprice.repositories.RetailerRepository;
import dtu.thebestprice.services.RetailerService;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Set<RetailerResponse> findAll() {
        return retailerRepository.findByDeleteFlgFalse().stream().map(retailer -> retailerConverter.toRetailerResponse(retailer)).collect(Collectors.toSet());
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Object> save(RetailerRequest retailerRequest) {
        // truong hop them moi
        if (retailerRequest.getId() == null) {

            // kiểm tra xem có bị trùng tên với các nhà bán lẽ khác hay không
            if (retailerRepository.existsByName(retailerRequest.getName().trim()))
                throw new RuntimeException("Tên nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập tên khác");

            // kiểm tra xem có bị trùng logo với các nhà bán lẽ khác hay không?
            if (retailerRepository.existsByLogoImage(retailerRequest.getLogo().trim()))
                throw new RuntimeException("logo của nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập logo khác");

            // kiểm tra xem có bị trung homepage với nhà bán lẽ khác hay không?
            if (retailerRepository.existsByHomePage(retailerRequest.getHomePage().trim()))
                throw new RuntimeException("Homepage nhà bán lẽ này bị trùng với một nhà bán lẽ khác.Vui lòng nhập Homepage khác");

            retailerRepository.save(retailerConverter.toEntity(retailerRequest));
            return ResponseEntity.ok(new ApiResponse(true, "Thêm mới thành công"));
        }

        // truong hop cap nhat
        else {

            /*
             * Kiểm tra định dạng của id
             * */
            Long retailerId = null;
            try {
                retailerId = Long.parseLong(retailerRequest.getId());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("id của nhà bán lẽ không đúng định dạng");
            }

            Retailer retailer = retailerRepository.findById(retailerId).orElse(null);
            if (retailer == null)
                return ResponseEntity.status(404).body(new ApiResponse(false, "id của nhà bán lẽ không tồn tại"));

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
}
