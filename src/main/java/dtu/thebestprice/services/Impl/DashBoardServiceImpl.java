package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.entities.StatisticAccess;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.response.SearchResponse;
import dtu.thebestprice.payload.response.dashboard.OverView;
import dtu.thebestprice.repositories.*;
import dtu.thebestprice.services.DashBoardService;
import dtu.thebestprice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    public ResponseEntity<Object> statisticKeyword(Pageable pageable) {
        Page<SearchResponse> result =
                searchRepository
                        .findByOrderByNumberOfSearchDesc(pageable)
                        .map(this::toSearchResponse);
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
