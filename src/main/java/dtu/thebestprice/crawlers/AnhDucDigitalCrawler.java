package dtu.thebestprice.crawlers;

import dtu.thebestprice.crawlers.model.CrawlerModel;

import java.util.List;

public interface AnhDucDigitalCrawler {
    Long getPriceByUrl(String url);
    List<CrawlerModel> listIphone();
    List<CrawlerModel> listSamsung();
}
