package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.DiDongVietCrawler;
import dtu.thebestprice.crawlers.model.CrawlerModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiDongVietCrawlerImpl implements DiDongVietCrawler {

    Document document;

    @Override
    public Long getPriceByUrl(String url) {
        try {
            document = Jsoup.connect(url).get();
            Element priceElement = document.selectFirst(".price-wrapper");
            return Long.parseLong(priceElement.attr("data-price-amount"));
        } catch (Exception e) {
            return null;
        }
    }

    private List<CrawlerModel> listProduct(String url) {

        List<CrawlerModel> resultList = new ArrayList<>();
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return resultList;
        }

        Elements productItemElements = document.select("div.product-item-info a");

        productItemElements.forEach(element -> {

            CrawlerModel crawlerModel = getProductByUrl(element.attr("href"));
            if (crawlerModel != null)
                resultList.add(crawlerModel);
        });


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
            Element titleElement = document.selectFirst(".page-title .base");
            title = titleElement.text();
        } catch (Exception e) {
            return null;
        }


        long price;
        try {
            Element priceElement = document.selectFirst(".price-wrapper");
            price = Long.parseLong(priceElement.attr("data-price-amount"));
        } catch (Exception e) {
            return null;
        }

        List<String> images = new ArrayList<>();
        try {
            Elements imageElements = document.select("div.product.media > div.nav-gallery-left .thumb-color");
            for (Element itemImage : imageElements) {
                images.add(itemImage.attr("href"));
            }
        } catch (Exception e) {
            images = new ArrayList<>();
        }

        // mô tả sản phẩm
        Elements descElements = document.select("#product-attribute-specs-table > .iattribute");
        StringBuilder desc = new StringBuilder("");
        try {
            for (Element item : descElements) {
                desc.append(item.select(".col-label").text());
                desc.append(" : ");
                desc.append(item.select(".col-data").text());
                desc.append("\n");
            }
        } catch (Exception ignored) {
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
        String url = "https://didongviet.vn/apple";

        return this.listProduct(url)
                .stream()
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().replaceAll(" ", "");
                    String code = title.substring(0, title.indexOf("gb") + 2);
                    crawlerModel.setCode(code);
                }).collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listSamsung() {
        String url = "https://didongviet.vn/samsung";

        return this.listProduct(url)
                .stream()
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title
                            .replaceAll(" ", "")
                            .substring(0, title.indexOf("gb"));
                    crawlerModel.setCode(code);
                }).collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listVsmart() {
        String url = "https://didongviet.vn/vsmart";
        return this.listProduct(url)
                .stream()
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title
                            .replaceAll(" ", "")
                            .replaceAll("\\(", "")
                            .replaceAll("\\|", "")
                            .replaceAll("\\)", "");
                    crawlerModel.setCode(code);
                }).collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listOppo() {
        String url = "https://didongviet.vn/oppo";
        return this.listProduct(url)
                .stream()
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title
                            .replaceAll("\\(", "")
                            .replaceAll("\\|", "")
                            .replaceAll("\\)", "");
                    crawlerModel.setCode(code);
                }).collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listXiaomi() {
        String url = "https://didongviet.vn/xiaomi";
        return this.listProduct(url)
                .stream()
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title
                            .replaceAll("\\(", "")
                            .replaceAll("\\|", "")
                            .replaceAll("\\)", "");
                    crawlerModel.setCode(code);
                }).collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listRealme() {
        String url = "https://didongviet.vn/realme";
        return this.listProduct(url)
                .stream()
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title
                            .replaceAll("\\(", "")
                            .replaceAll("\\|", "")
                            .replaceAll("\\)", "");
                    crawlerModel.setCode(code);
                }).collect(Collectors.toList());
    }
}
