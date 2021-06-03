package dtu.thebestprice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CrawlTest {
    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("https://anhducdigital.vn/iphone-xs-max-64gb-2sim-gold.html").get();
        Element priceElement = document.selectFirst("#info-normal_product div.pull-right.text-right > span");

        System.out.println("price: "+Long.parseLong(priceElement.text().replaceAll("[^0-9]", "")));
    }
}
