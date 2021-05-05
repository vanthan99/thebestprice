package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Search;
import dtu.thebestprice.entities.SearchStatistic;
import dtu.thebestprice.payload.request.SearchTrackingRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.SearchRepository;
import dtu.thebestprice.repositories.SearchStatisticRepository;
import dtu.thebestprice.services.SearchTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class SearchTrackingServiceImpl implements SearchTrackingService {
    @Autowired
    SearchRepository searchRepository;

    @Autowired
    SearchStatisticRepository searchStatisticRepository;

    @Override
    public ResponseEntity<Object> searchTracking(SearchTrackingRequest searchRequest) {
        if (searchRequest.getKeyword() != null && !searchRequest.getKeyword().trim().isEmpty()) {

            LocalDate nowDay = LocalDate.now();

            if (searchRepository.existsByKeyword(searchRequest.getKeyword().trim().toLowerCase())) {
                // tăng total of search lên 1
                Search search = searchRepository.findByKeyword(searchRequest.getKeyword().trim().toLowerCase());
                searchRepository.updateNumberOfSearch(search.getId());


                // kiểm tra lưu vào statistic
                if (searchStatisticRepository.existsByStatisticDayAndSearch(nowDay, search)) {
                    // update total of seach len 1
                    searchStatisticRepository.updateNumberOfSearch(search.getId(), nowDay);
                } else {
                    // nếu không tồn tại ngày thống kê thì tạo mới
                    SearchStatistic searchStatistic = new SearchStatistic();
                    searchStatistic.setSearch(search);
                    searchStatistic.setStatisticDay(nowDay);
                    searchStatistic.setNumberOfSearch(1L);

                    searchStatisticRepository.save(searchStatistic);
                }

            } else {
                Search search = new Search();
                search.setKeyword(searchRequest.getKeyword().trim().toLowerCase());
                search.setNumberOfSearch(1L);
                searchRepository.save(search);


                // save statistic
                SearchStatistic searchStatistic = new SearchStatistic();
                searchStatistic.setSearch(search);
                searchStatistic.setNumberOfSearch(1L);
                searchStatistic.setStatisticDay(nowDay);

                searchStatisticRepository.save(searchStatistic);
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
