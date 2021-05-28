package dtu.thebestprice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CrawlTest {
    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("http://xuanvinh.vn/lenovo-ideapad-s340-13iml-81um004rvn-i3-10110u-8gb-ssd-512gb-win10-psd").get();
        Element codeElement = document.selectFirst("#main-content > div > div.content-box > div.product-right > div > div.product-right-content > p:nth-child(2)");
        String code = codeElement.text()
                .substring(0, codeElement.text().indexOf("- B"))
                .replace("(2020)", "")
                .replaceAll("- Mã hàng:", "").trim();
        System.out.println("code: "+code);
    }
}
