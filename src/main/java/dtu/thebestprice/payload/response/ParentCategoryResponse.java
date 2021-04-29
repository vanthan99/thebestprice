package dtu.thebestprice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentCategoryResponse {
    private Long id;
    private String title;
    private String description;
    private List<ChildCategoryResponse> categories;
}
