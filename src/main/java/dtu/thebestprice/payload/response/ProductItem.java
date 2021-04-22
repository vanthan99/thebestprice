package dtu.thebestprice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* Item product trong gợi ý tìm kiếm
* Bao gồm: tiêu đề, giá (giá tốt nhất), số nơi bán
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItem {
    private Long id;
    private String title;
    private Long price;
    private Long totalStore;
}
