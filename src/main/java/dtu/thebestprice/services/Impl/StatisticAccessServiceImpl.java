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
}
