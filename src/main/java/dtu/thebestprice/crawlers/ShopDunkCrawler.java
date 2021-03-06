package dtu.thebestprice.crawlers;


import dtu.thebestprice.crawlers.model.CrawlerModel;
import dtu.thebestprice.crawlers.model.ProductCrawler;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ShopDunkCrawler {
    Long getPriceByUrl(String url);

    Set<ProductCrawler> getListIphoneByUrl(String url) throws IOException;

    List<CrawlerModel> listIphone();
}
