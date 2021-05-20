package dtu.thebestprice.payload.response.retailer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetailerForUserRetailerResponse {
    private Long id;
    private String name;
    private String description;
    private String homePage;
    private String logoImage;
    private boolean enable;
    private boolean approve;
}
