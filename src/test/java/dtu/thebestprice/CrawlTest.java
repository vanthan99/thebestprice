package dtu.thebestprice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CrawlTest {
    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("https://shopdunk.com/iphone/").get();
        Elements codeElement = document.select(".sdo-wrap-item");

        System.out.println("code: "+codeElement.outerHtml());
    }
}
