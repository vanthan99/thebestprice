package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.exports.StatisticExcelExporter;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.payload.response.SearchResponse;
import dtu.thebestprice.payload.response.dashboard.DashBoard;
import dtu.thebestprice.payload.response.dashboard.OverView;
import dtu.thebestprice.payload.response.dashboard.User;
import dtu.thebestprice.repositories.*;
import dtu.thebestprice.services.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    ViewCountStatisticRepository viewCountStatisticRepository;

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

        Long auth = statisticAccessRepository.countByYearAndAuth(year, true);
        Long anonymous = statisticAccessRepository.countByYearAndAuth(year, false);

        User userRate = new User();


        userRate.setAuth((double) Math.round(((1.0) * auth / (auth + anonymous)) * 100 * 100) / 100);
        userRate.setAnonymous(100 - userRate.getAuth());


        DashBoard dashBoard = new DashBoard();
        // set User rate
        dashBoard.setRateUser(userRate);

        List<Long> statisticAccess = new ArrayList<>();
        List<Long> statisticSearch = new ArrayList<>();
        List<Long> statisticViewCount = new ArrayList<>();

        if (type.equalsIgnoreCase("month")) {

            // set statistic access // set statistic search
            for (int i = 1; i <= month; i++) {
                statisticAccess.add(statisticAccessRepository.countByMonth(year, i));
                statisticSearch.add(searchStatisticRepository.countByMonth(year, i));
                statisticViewCount.add(viewCountStatisticRepository.countByMonth(year, i));
            }

            dashBoard.setStatisticAccess(statisticAccess.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList()));
            dashBoard.setStatisticSearch(statisticSearch.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList()));
            dashBoard.setStatisticViewCount(statisticViewCount.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList()));

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
                statisticViewCount.add(viewCountStatisticRepository.countByQuarter(year, months));
            }

            dashBoard.setStatisticAccess(statisticAccess.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList()));
            dashBoard.setStatisticSearch(statisticSearch.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList()));
            dashBoard.setStatisticViewCount(statisticViewCount.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList()));

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
        Long auth = statisticAccessRepository.countByMonthAndAuth(nowDay.getYear(), nowDay.getMonthValue(), true);
        Long anonymous = statisticAccessRepository.countByMonthAndAuth(nowDay.getYear(), nowDay.getMonthValue(), false);

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

    @Override
    public ResponseEntity<Object> exportToExcel() throws IOException {

        LocalDate nowDay = LocalDate.now();

        int month = nowDay.getMonthValue();
        int year = nowDay.getYear();

        OverView overView = new OverView();
        overView.setVisitor(statisticAccessRepository.countByMonth(year, month));
        overView.setSearch(searchStatisticRepository.countByMonth(year, month));
        overView.setProduct(productRepository.count());
        overView.setUser(userRepository.countByRoleAndEnableTrueAndApproveTrueAndDeleteFlgFalse(ERole.ROLE_GUEST));
        overView.setRetailer(userRepository.countByRoleAndEnableTrueAndApproveTrueAndDeleteFlgFalse(ERole.ROLE_RETAILER));

        Long auth = statisticAccessRepository.countByYearAndAuth(year, true);
        Long anonymous = statisticAccessRepository.countByYearAndAuth(year, false);

        User userRate = new User();

        userRate.setAuth((double) Math.round(((1.0) * auth / (auth + anonymous)) * 100 * 100) / 100);
        userRate.setAnonymous(100 - userRate.getAuth());

        DashBoard dashBoard = new DashBoard();
        // set User rate
        dashBoard.setRateUser(userRate);

        List<Long> statisticAccess = new ArrayList<>();
        List<Long> statisticSearch = new ArrayList<>();
        List<Long> statisticViewCount = new ArrayList<>();

        // set statistic access // set statistic search
        for (int i = 1; i <= month; i++) {
            statisticAccess.add(statisticAccessRepository.countByMonth(year, i));
            statisticSearch.add(searchStatisticRepository.countByMonth(year, i));
            statisticViewCount.add(viewCountStatisticRepository.countByMonth(year, i));
        }

        dashBoard.setStatisticAccess(statisticAccess.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList()));
        dashBoard.setStatisticSearch(statisticSearch.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList()));
        dashBoard.setStatisticViewCount(statisticViewCount.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList()));


        ByteArrayInputStream in = StatisticExcelExporter.customersToExcel(overView, dashBoard,searchRepository.findTop20ByOrderByNumberOfSearchDesc());
        // return IOUtils.toByteArray(in);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    private SearchResponse toSearchResponse(Search search) {
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setId(search.getId());
        searchResponse.setKeyword(search.getKeyword());
        searchResponse.setNumberOfSearch(search.getNumberOfSearch());

        return searchResponse;
    }
}
