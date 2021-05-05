package dtu.thebestprice.payload.response.query;

import dtu.thebestprice.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewCountModel {
    private Long viewCount;
    private Product product;
}
