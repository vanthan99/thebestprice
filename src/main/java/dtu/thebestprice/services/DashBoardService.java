package dtu.thebestprice.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DashBoardService {
    ResponseEntity<Object> overView();

    // tổng quan lượt truy cập
    ResponseEntity<Object> statisticAccess();

    // tổng quan lượt tìm kiếm
    ResponseEntity<Object> statisticSearch();

    // tỷ lệ người dùng truy cập có hay không có tài khoản


    // thống kê từ khóa được tìm kiếm
    ResponseEntity<Object> statisticKeyword(Pageable pageable);

}
