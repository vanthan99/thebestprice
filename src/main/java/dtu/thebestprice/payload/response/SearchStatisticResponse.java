package dtu.thebestprice.payload.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchStatisticResponse {
    private String title = "";
    private List<SearchStatisticItemResponse> content = new ArrayList<>();
    private int totalOfElements = 0;
}
