package dtu.thebestprice.crawlers.model;

import dtu.thebestprice.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
 * Class lấy dữ liệu thô trực tiếp từ trang web
 *
 * */
public class ProductCrawler {
    private Product product;
    private Long price;
    private List<String> images;
    private String url;
}
