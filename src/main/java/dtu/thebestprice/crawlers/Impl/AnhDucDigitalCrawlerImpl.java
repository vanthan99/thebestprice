package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.AnhDucDigitalCrawler;
import dtu.thebestprice.crawlers.model.CrawlerModel;
import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnhDucDigitalCrawlerImpl implements AnhDucDigitalCrawler {
    Document document;

    private List<CrawlerModel> listProduct(String url) {
        int page = 1;
        List<CrawlerModel> result = new ArrayList<>();
        while (true) {
            try {
                document = Jsoup.connect(url + "?page=" + page).get();
            } catch (IOException e) {
                return result;
            }

            Elements productItem = document.select(".p_container a.p-name");
            if (productItem == null || productItem.size() == 0)
                break;

            productItem.forEach(item -> {
                CrawlerModel crawlerModel = this.getProductByUrl("https://anhducdigital.vn" + item.attr("href"));
                if (crawlerModel != null)
                    result.add(crawlerModel);
            });

            page++;
        }

        return result;
    }

    private CrawlerModel getProductByUrl(String url) {
//        System.out.println("đã vào: " + url);
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }

        String title;
        try {
            Element titleElement = document.selectFirst(".productName");
            title = titleElement.text();
        } catch (Exception e) {
            return null;
        }

        long price;
        try {
            Element priceElement = document.selectFirst("#info-normal_product div.pull-right.text-right > span");
            price = Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }

        String image;
        try {
            Element imageElement = document.selectFirst("#detailimagesrc");
            image = "https://anhducdigital.vn" + imageElement.attr("src");
        } catch (Exception e) {
            image = "https://posxanh.com/wp-content/uploads/2019/01/dang-cap-nhat.png";
        }

        String desc;
        try {
            Element descElement = document.selectFirst("div.summary-detail");
            desc = descElement
                    .html()
                    .replaceAll(" - ", "")
                    .replaceAll(" -", "")
                    .replaceAll("- ", "")
                    .replaceAll("-", "")
                    .replaceAll("<br>", "\n")
                    .replaceAll("<p>", "")
                    .replaceAll("</p>", "")
                    .replaceAll("&nbsp;", "")
                    .trim();
        } catch (Exception e) {
            desc = "";
        }

        CrawlerModel crawlerModel = new CrawlerModel();
        crawlerModel.setTitle(title);
        crawlerModel.setPrice(price);
        crawlerModel.setImages(Collections.singletonList(image));
        crawlerModel.setShortDesc(desc);
        crawlerModel.setLongDesc(desc);
        crawlerModel.setUrl(url);

        return crawlerModel;


    }

    @Override
    public Long getPriceByUrl(String url) {
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println("Lỗi khi truy cập vào url: " + url);
        }

        try {
            Element priceElement = document.selectFirst("#info-normal_product div.pull-right.text-right > span");
            return Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<CrawlerModel> listIphone() {
        String url = "https://anhducdigital.vn/iphone.html";
        return this.listProduct(url).stream().peek(crawlerModel -> {
            String title = crawlerModel.getTitle().toLowerCase()
                    .replaceAll("\\(2020\\)", "");
            title = title.substring(0, title.indexOf("gb") + 2).trim();
            title = WordUtils.capitalizeFully(title);

            crawlerModel.setTitle(title);

            String code = title.toLowerCase().replaceAll(" ", "").trim();
            crawlerModel.setCode(code);
        }).collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listSamsung() {

        String url = "https://anhducdigital.vn/dien-thoai-samsung.html";

        return this.listProduct(url).stream().peek(crawlerModel -> {
            String title = crawlerModel.getTitle().toLowerCase();
            String code = title
                    .replaceAll(" ", "")
                    .replaceAll("\\(chínhhãng\\)", "");
            crawlerModel.setCode(code);
        }).collect(Collectors.toList());
    }
}
