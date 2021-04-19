package dtu.thebestprice.crawlers;

import dtu.thebestprice.crawlers.model.ProductCrawler;

import java.io.IOException;
import java.util.Set;

public interface PhiLongCrawler {
    Set<ProductCrawler> getProductByURL(String url) throws IOException;
}
