package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.AppleTAndTCrawler;
import dtu.thebestprice.crawlers.model.CrawlerModel;
import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AppleTAndTCrawlerImpl implements AppleTAndTCrawler {
    Document document;

    @Override
    public Long getPriceByUrl(String url) {

        try {
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println("Lỗi khi truy cập vào url: " + url);
        }

        try {
            Element priceElement = document.selectFirst("div.summary.entry-summary > p.price > span > bdi");
            String strPrice = priceElement.text().replaceAll("[^0-9]", "");
            return Long.parseLong(strPrice);
        } catch (Exception e) {
            return null;
        }
    }

    private List<CrawlerModel> listProduct(String url) {

        List<CrawlerModel> resultList = new ArrayList<>();
        int page = 1;
        while (true) {
            try {
                document = Jsoup.connect(url + "/page/" + page).get();
            } catch (IOException e) {
                return resultList;
            }

            Elements productItemElements = document.select(".pr-loop-footer > a:nth-child(1)");
            if (productItemElements == null || productItemElements.size() == 0)
                break;

            productItemElements.forEach(element -> {

                CrawlerModel crawlerModel = getProductByUrl(element.attr("href"));
                if (crawlerModel != null)
                    resultList.add(crawlerModel);
            });
            page++;
        }

        return resultList;
    }

    private CrawlerModel getProductByUrl(String url) {
//        System.out.println("url = " + url);
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }

        String title;
        try {
            Element titleElement = document.selectFirst("div.summary.entry-summary > h1");
            title = titleElement.text();
        } catch (Exception e) {
            return null;
        }


        Long price;
        try {
            Element priceElement = document.selectFirst("div.summary.entry-summary > p.price > span > bdi");
            String strPrice = priceElement.text().replaceAll("[^0-9]", "");
            price = Long.parseLong(strPrice);
        } catch (Exception e) {
            return null;
        }

        List<String> images = new ArrayList<>();
        try {
            Elements imageElements = document.select(".woocommerce-product-gallery a");
            for (Element itemImage : imageElements) {
                images.add(itemImage.attr("href"));
            }
        } catch (Exception e) {
            images = new ArrayList<>();
        }

        // mô tả sản phẩm
        Elements descElements = document.select("div.table-responsive > table > tbody tr");
        StringBuilder desc = new StringBuilder("");
        for (Element item : descElements) {
            desc.append(item.text());
            desc.append("\n");
        }

        CrawlerModel crawlerModel = new CrawlerModel();
        crawlerModel.setTitle(title);
        crawlerModel.setPrice(price);
        crawlerModel.setImages(images);
        crawlerModel.setLongDesc(desc.toString());
        crawlerModel.setShortDesc(desc.toString());
        crawlerModel.setUrl(url);

        return crawlerModel;
    }

    @Override
    public List<CrawlerModel> listIphone() {
        List<String> listUrl = Arrays.asList(
                "https://iphonedanang.com.vn/danh-muc/iphone-7-plus",
                "https://iphonedanang.com.vn/danh-muc/iphone-12-mini-pro-max",
                "https://iphonedanang.com.vn/danh-muc/iphone-11-pro-max",
                "https://iphonedanang.com.vn/danh-muc/iphone-x-xs-max",
                "https://iphonedanang.com.vn/danh-muc/iphone-xr-se",
                "https://iphonedanang.com.vn/danh-muc/iphone-8-plus"

        );
        List<CrawlerModel> results = new ArrayList<>();
        for (String url : listUrl) {
            results.addAll(this.listProduct(url).stream()
                    .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("gb"))
                    .peek(crawlerModel -> {
                        String title = crawlerModel.getTitle().toLowerCase();
                        title = title.substring(0, title.indexOf("gb") + 2).trim();
                        title = WordUtils.capitalizeFully(title);

                        crawlerModel.setTitle(title);

                        String code = title.toLowerCase().replaceAll(" ", "").trim();
                        crawlerModel.setCode(code);
                    }).collect(Collectors.toList()));
        }
        return results;
    }
}
