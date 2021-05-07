package dtu.thebestprice.payload.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverView {
    private Long visitor;// lượt truy cập
    private Long search; // lượt tìm kiếm
    private Long user; // số người dùng
    private Long retailer; // retailer
    private Long product; // số sản phẩm
}
