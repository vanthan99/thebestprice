package dtu.thebestprice.payload.response.dashboard;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DashBoard {
    private List<Long> statisticAccess = new ArrayList<>();
    private List<Long> statisticSearch = new ArrayList<>();
    private List<Long> statisticViewCount = new ArrayList<>();
    private User rateUser;
}
