package dtu.thebestprice.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface DashBoardService {
    ResponseEntity<Object> overView();

    ResponseEntity<Object> dashBoard(String type);

    // tổng quan lượt truy cập
    ResponseEntity<Object> statisticAccess();

    // tổng quan lượt tìm kiếm
    ResponseEntity<Object> statisticSearch();

    // tỷ lệ người dùng truy cập có hay không có tài khoản
    ResponseEntity<Object> rateUser();

    // thống kê từ khóa được tìm kiếm
    ResponseEntity<Object> statisticKeyword(Pageable pageable);

    ResponseEntity<Object> statisticAccessByQuarter();

    ResponseEntity<Object> exportToExcel() throws IOException;
}
