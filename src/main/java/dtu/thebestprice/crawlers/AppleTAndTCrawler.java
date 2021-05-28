package dtu.thebestprice.crawlers;

import dtu.thebestprice.crawlers.model.CrawlerModel;
import dtu.thebestprice.crawlers.model.ProductCrawler;

import java.util.List;

public interface AppleTAndTCrawler {
    Long getPriceByUrl(String url);

    List<CrawlerModel> listIphone();
}
