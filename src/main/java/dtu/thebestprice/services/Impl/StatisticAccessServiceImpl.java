package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.StatisticAccess;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.StatisticAccessRepository;
import dtu.thebestprice.services.StatisticAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticAccessServiceImpl implements StatisticAccessService {

    @Autowired
    StatisticAccessRepository statisticAccessRepository;

    @Override
    @Transactional
    public ResponseEntity<Object> save(boolean auth) {
        LocalDate nowDay = LocalDate.now();
        if (statisticAccessRepository.existsByAuthAndStatisticDay(auth, nowDay)) {
            // cập nhật counter lên 1
            StatisticAccess statisticAccess = statisticAccessRepository.findByAuthAndStatisticDay(auth, nowDay);

            statisticAccess.setCounter(statisticAccess.getCounter() + 1);

            statisticAccessRepository.save(statisticAccess);

        } else {
            // tạo mới 1 statistic access
            StatisticAccess statisticAccess = new StatisticAccess();
            statisticAccess.setAuth(auth);
            statisticAccess.setCounter(1L);
            statisticAccess.setStatisticDay(nowDay);

            statisticAccessRepository.save(statisticAccess);
        }

        return ResponseEntity.ok(new ApiResponse(true, "Tracking thành công"));
    }

    @Override
    public ResponseEntity<Object> staticBetweenDay(Date startDay, Date endDay) {
        long getDiff = endDay.getTime() - startDay.getTime();

        long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);

        LocalDate date = startDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (endDay.after(new Date()))
            throw new RuntimeException("Ngày kết thúc chỉ được thống kê tới hôm nay " + LocalDate.now().toString());

        List<Long> result = new ArrayList<>();

        for (int i = 0; i <= getDaysDiff; i++) {
            result.add(statisticAccessRepository.countByDay(date.plusDays(i)));
        }

        result = result.stream().map(item -> item == null ? 0 : item).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
