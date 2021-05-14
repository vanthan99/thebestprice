package dtu.thebestprice.payload.response.retailer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetailerForAdminResponse {
    private Long id;
    private String name;
    private String description;
    private String homePage;
    private String logoImage;
    private boolean enable;
    private Long userId;
}
