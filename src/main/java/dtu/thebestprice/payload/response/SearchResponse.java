package dtu.thebestprice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private Long id;
    private String keyword;
    private Long numberOfSearch;
}
