package dtu.thebestprice.crawlers;

import dtu.thebestprice.crawlers.model.CrawlerModel;

import java.util.List;

public interface DiDongVietCrawler {
    Long getPriceByUrl(String url);

    List<CrawlerModel> listIphone();

    List<CrawlerModel> listSamsung();

    List<CrawlerModel> listVsmart();

    List<CrawlerModel> listOppo();

    List<CrawlerModel> listXiaomi();

    List<CrawlerModel> listRealme();

}
