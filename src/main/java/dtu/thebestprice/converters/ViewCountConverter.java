package dtu.thebestprice.converters;

import dtu.thebestprice.entities.ViewCountStatistic;
import dtu.thebestprice.payload.response.StatisticViewCountResponse;
import dtu.thebestprice.payload.response.query.ViewCountModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ViewCountConverter {
    public StatisticViewCountResponse toStatisticViewCountResponse(ViewCountStatistic statistic) {
        StatisticViewCountResponse viewCountResponse = new StatisticViewCountResponse();
        viewCountResponse.setProductId(statistic.getProduct().getId());
        viewCountResponse.setViewCount(statistic.getViewCount());
        viewCountResponse.setProductTitle(statistic.getProduct().getTitle());
        List<String> images = new ArrayList<>();
        statistic.getProduct().getImages().forEach(image -> images.add(image.getUrl()));
        viewCountResponse.setProductImages(images);
        return viewCountResponse;
    }

    public StatisticViewCountResponse toStatisticViewCountResponse(ViewCountModel statistic) {
        StatisticViewCountResponse viewCountResponse = new StatisticViewCountResponse();
        viewCountResponse.setProductId(statistic.getProduct().getId());
        viewCountResponse.setViewCount(statistic.getViewCount());
        viewCountResponse.setProductTitle(statistic.getProduct().getTitle());
        List<String> images = new ArrayList<>();
        statistic.getProduct().getImages().forEach(image -> images.add(image.getUrl()));
        viewCountResponse.setProductImages(images);
        return viewCountResponse;
    }


}
