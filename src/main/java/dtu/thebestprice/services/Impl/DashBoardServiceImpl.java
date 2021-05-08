package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.SearchResponse;
import dtu.thebestprice.payload.response.dashboard.DashBoard;
import dtu.thebestprice.payload.response.dashboard.OverView;
import dtu.thebestprice.payload.response.dashboard.User;
import dtu.thebestprice.repositories.*;
import dtu.thebestprice.services.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    StatisticAccessRepository statisticAccessRepository;

    @Autowired
    SearchStatisticRepository searchStatisticRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SearchRepository searchRepository;

    @Override
    public ResponseEntity<Object> overView() {
        LocalDate nowDay = LocalDate.now();

        int month = nowDay.getMonthValue();
        int year = nowDay.getYear();

        OverView overView = new OverView();
        overView.setVisitor(statisticAccessRepository.countByMonth(year, month));
        overView.setSearch(searchStatisticRepository.countByMonth(year, month));
        overView.setProduct(productRepository.count());
        overView.setUser(userRepository.countByRoleAndEnableTrueAndApproveTrueAndDeleteFlgFalse(ERole.ROLE_GUEST));
        overView.setRetailer(userRepository.countByRoleAndEnableTrueAndApproveTrueAndDeleteFlgFalse(ERole.ROLE_RETAILER));

        return ResponseEntity.ok(overView);
    }

    @Override
    public ResponseEntity<Object> dashBoard(String type) {
        LocalDate nowDate = LocalDate.now();
        int month = nowDate.getMonthValue();
        int year = nowDate.getYear();

        Long auth = statisticAccessRepository.countByYearAndAuthTrue(year);
        Long anonymous = statisticAccessRepository.countByYearAndAuthFalse(year);

        User userRate = new User();
        userRate.setAuth(((1.0) * auth / (auth + anonymous)) * 100);
        userRate.setAnonymous(100 - userRate.getAuth());


        DashBoard dashBoard = new DashBoard();
        // set User rate
        dashBoard.setRateUser(userRate);

        List<Long> statisticAccess = new ArrayList<>();
        List<Long> statisticSearch = new ArrayList<>();

        if (type.equalsIgnoreCase("month")) {


            // set statistic access // set statistic search
            for (int i = 1; i <= month; i++) {
                statisticAccess.add(statisticAccessRepository.countByMonth(year, i));
                statisticSearch.add(searchStatisticRepository.countByMonth(year, i));
            }
            dashBoard.setStatisticAccess(statisticAccess);
            dashBoard.setStatisticSearch(statisticSearch);

            return ResponseEntity.ok(dashBoard);

        } else if (type.equalsIgnoreCase("quarter")) {
            // tinh quý hiện tại
            int quarter = nowDate.get(IsoFields.QUARTER_OF_YEAR);

            int[] months;

            // set statistic search and statistic access
            for (int i = 1; i <= quarter; i++) {

                if (i == 1) months = new int[]{1, 2, 3};
                else if (i == 2) months = new int[]{4, 5, 6};
                else if (i == 3) months = new int[]{7, 8, 9};
                else months = new int[]{10, 11, 12};

                statisticSearch.add(searchStatisticRepository.countByQuarter(year, months));
                statisticAccess.add(statisticAccessRepository.countByQuarter(year, months));
            }

            dashBoard.setStatisticAccess(statisticAccess);
            dashBoard.setStatisticSearch(statisticSearch);

            return ResponseEntity.ok(dashBoard);
        } else return ResponseEntity.status(400).body(new ApiResponse(false, "Tham số không đúng"));
    }

    @Override
    public ResponseEntity<Object> statisticAccess() {
        List<Long> result = new ArrayList<>();
        LocalDate nowDate = LocalDate.now();
        int month = nowDate.getMonthValue();
        int year = nowDate.getYear();

        for (int i = 1; i <= month; i++) {
            result.add(statisticAccessRepository.countByMonth(year, i));
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Object> statisticSearch() {
        List<Long> result = new ArrayList<>();
        LocalDate nowDate = LocalDate.now();
        int month = nowDate.getMonthValue();
        int year = nowDate.getYear();

        for (int i = 1; i <= month; i++) {
            result.add(searchStatisticRepository.countByMonth(year, i));
        }
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Object> rateUser() {
        LocalDate nowDay = LocalDate.now();
        Long auth = statisticAccessRepository.countByMonthAndAuthTrue(nowDay.getYear(), nowDay.getMonthValue());
        Long anonymous = statisticAccessRepository.countByMonthAndAuthFalse(nowDay.getYear(), nowDay.getMonthValue());

        User user = new User();
        user.setAuth(((1.0) * auth / (auth + anonymous)) * 100);
        user.setAnonymous(100 - user.getAuth());

        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<Object> statisticKeyword(Pageable pageable) {
        Page<SearchResponse> result =
                searchRepository
                        .findByOrderByNumberOfSearchDesc(pageable)
                        .map(this::toSearchResponse);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Object> statisticAccessByQuarter() {
        List<Long> result = new ArrayList<>();
        LocalDate nowDate = LocalDate.now();


        int month = nowDate.getMonthValue();
        int year = nowDate.getYear();

        for (int i = 1; i <= month; i++) {
            result.add(statisticAccessRepository.countByMonth(year, i));
        }

        return ResponseEntity.ok(result);
    }

    private SearchResponse toSearchResponse(Search search) {
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setId(search.getId());
        searchResponse.setKeyword(search.getKeyword());
        searchResponse.setNumberOfSearch(search.getNumberOfSearch());

        return searchResponse;
    }
}
