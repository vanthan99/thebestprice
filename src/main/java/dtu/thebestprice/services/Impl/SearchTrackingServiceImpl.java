package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.entities.SearchStatisticDay;
import dtu.thebestprice.entities.SearchStatisticYear;
import dtu.thebestprice.entities.SearchStatisticYearMonth;
import dtu.thebestprice.payload.request.SearchTrackingRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.SearchRepository;
import dtu.thebestprice.repositories.SearchStatisticDayRepository;
import dtu.thebestprice.repositories.SearchStatisticYearMonthRepository;
import dtu.thebestprice.repositories.SearchStatisticYearRepository;
import dtu.thebestprice.services.SearchTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Set;

@Service
public class SearchTrackingServiceImpl implements SearchTrackingService {
    @Autowired
    SearchRepository searchRepository;

    @Autowired
    SearchStatisticDayRepository searchStatisticDayRepository;

    @Autowired
    SearchStatisticYearMonthRepository searchStatisticYearMonthRepository;

    @Autowired
    SearchStatisticYearRepository searchStatisticYearRepository;

    @Override
    public ResponseEntity<Object> searchTracking(SearchTrackingRequest searchRequest) {
        if (searchRequest.getKeyword() != null && !searchRequest.getKeyword().trim().isEmpty()) {
            LocalDate nowDay = LocalDate.now();

            if (searchRepository.existsByKeyword(searchRequest.getKeyword().trim().toLowerCase())) {
                // tăng total of search lên 1
                Search search = searchRepository.findByKeyword(searchRequest.getKeyword().trim().toLowerCase());
                searchRepository.updateTotalOfSearch(search.getId());


                // kiểm tra lưu vào statistic day
                if (searchStatisticDayRepository.existsBySearchAndStatisticDay(search, nowDay)) {
                    // update total of seach len 1
                    searchStatisticDayRepository.updateTotalOfSearch(search.getId(), nowDay);
                } else {
                    // nếu không tồn tại ngày thống kê thì tạo mới
                    SearchStatisticDay searchStatistic = new SearchStatisticDay();
                    searchStatistic.setSearch(search);
                    searchStatistic.setStatisticDay(nowDay);
                    searchStatistic.setTotalOfSearch(1L);

                    searchStatisticDayRepository.save(searchStatistic);
                }


                // kiểm tra lưu vào statistic year month
                if (searchStatisticYearMonthRepository.existsBySearchAndStatisticMonthAndStatisticYear(search, nowDay.getMonthValue(), nowDay.getYear())) {
                    // update total of seach len 1
                    searchStatisticYearMonthRepository.updateTotalOfSearch(search.getId(), nowDay.getMonthValue(), nowDay.getYear());
                } else {
                    // nếu không tồn tại ngày thống kê thì tạo mới
                    SearchStatisticYearMonth searchStatisticYearMonth = new SearchStatisticYearMonth();
                    searchStatisticYearMonth.setSearch(search);
                    searchStatisticYearMonth.setStatisticMonth(nowDay.getMonthValue());
                    searchStatisticYearMonth.setStatisticYear(nowDay.getYear());
                    searchStatisticYearMonth.setTotalOfSearch(1L);

                    searchStatisticYearMonthRepository.save(searchStatisticYearMonth);
                }

                // kiểm tra lưu vào statistic year
                if (searchStatisticYearRepository.existsBySearchAndStatisticYear(search, nowDay.getYear())) {
                    // update total of seach len 1
                    searchStatisticYearRepository.updateTotalOfSearch(search.getId(), nowDay.getYear());
                } else {
                    // nếu không tồn tại ngày thống kê thì tạo mới
                    SearchStatisticYear searchStatisticYear = new SearchStatisticYear();
                    searchStatisticYear.setSearch(search);
                    searchStatisticYear.setStatisticYear(nowDay.getYear());
                    searchStatisticYear.setTotalOfSearch(1L);

                    searchStatisticYearRepository.save(searchStatisticYear);
                }


            } else {
                Search search = new Search();
                search.setKeyword(searchRequest.getKeyword().trim().toLowerCase());
                search.setTotalOfSearch(1L);
                searchRepository.save(search);


                // save statistic Day
                SearchStatisticDay searchStatistic = new SearchStatisticDay();
                searchStatistic.setSearch(search);
                searchStatistic.setTotalOfSearch(1L);
                searchStatistic.setStatisticDay(nowDay);

                searchStatisticDayRepository.save(searchStatistic);

                // save statistic year month
                SearchStatisticYearMonth searchStatisticYearMonth = new SearchStatisticYearMonth();
                searchStatisticYearMonth.setSearch(search);
                searchStatisticYearMonth.setTotalOfSearch(1L);
                searchStatisticYearMonth.setStatisticMonth(nowDay.getMonthValue());
                searchStatisticYearMonth.setStatisticYear(nowDay.getYear());

                searchStatisticYearMonthRepository.save(searchStatisticYearMonth);

                // save statistic year
                SearchStatisticYear searchStatisticYear = new SearchStatisticYear();
                searchStatisticYear.setSearch(search);
                searchStatisticYear.setTotalOfSearch(1L);
                searchStatisticYear.setStatisticYear(nowDay.getYear());

                searchStatisticYearRepository.save(searchStatisticYear);
            }

            return ResponseEntity.ok(new ApiResponse(true, "Tracking thành công"));

        }
        return ResponseEntity.ok(null);
    }

    // truyền vào 1 keyword
    // Kiểm tra xem trong danh sách có tồn tại keyword đó không?

    private Search getSearchInSetSearch(String keyword, Set<String> keywordList) {
        return null;
    }
}
