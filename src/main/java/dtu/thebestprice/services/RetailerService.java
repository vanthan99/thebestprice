package dtu.thebestprice.services;

import dtu.thebestprice.entities.User;
import dtu.thebestprice.payload.request.RetailerForUserRequest;
import dtu.thebestprice.payload.request.RetailerRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.RetailerResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Set;

public interface RetailerService {
    Set<RetailerResponse> findAll();

    ResponseEntity<Object> deleteById(String id);

    ResponseEntity<Object> create(RetailerRequest retailerRequest, Long userId);

    ResponseEntity<Object> create(RetailerRequest retailerRequest, User user, boolean approve, boolean isCheckValidate);

    ResponseEntity<Object> create(RetailerRequest retailerRequest, User user, boolean deleteFlg, boolean enable, boolean approve);

    ResponseEntity<Object> create(RetailerForUserRequest retailerForUserRequest, User user, boolean deleteFlg, boolean enable, boolean approve);

    boolean validateWhileCreateRetailer(RetailerRequest request);

    ResponseEntity<Object> update(RetailerRequest retailerRequest, Long retailerId);

    // page retailer đã xác nhận hoặc chưa xác nhận
    ResponseEntity<Object> pageRetailerByApprove(boolean approve, String keyword, Pageable pageable);

    // phê duyệt retailer
    ResponseEntity<Object> approveRetailer(Long retailerId) throws MessagingException;

    ResponseEntity<Object> findById(long retailerId);

    ResponseEntity<Object> toggleEnable(long retailerId);

    // lấy danh sách retailer do user quản lý
    ResponseEntity<Object> getRetailerByUser(User user);

    // role retailer update
    ResponseEntity<Object> roleRetailerUpdate(RetailerForUserRequest retailerForUserRequest, long retailerId);
}
