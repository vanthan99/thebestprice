package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.XuanVinhCrawler;
import dtu.thebestprice.crawlers.filters.MyFilter;
import dtu.thebestprice.crawlers.model.CrawlerModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class XuanVinhCrawlerImpl implements XuanVinhCrawler {

    Document document;

    @Override
    public Long getPriceByUrl(String url) {
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println("Lỗi khi truy cập vào url: " + url);
        }
        try {
            Element priceElement = document.selectFirst("#main-content > div > div.content-box > div.product-right > div > div.single-product-price > div:nth-child(1)");
            String strPrice = priceElement.text().replaceAll("[^0-9]", "");
            return Long.parseLong(strPrice);
        } catch (Exception e) {
            return null;
        }
    }

    private List<CrawlerModel> getListProduct(String url) {
        List<CrawlerModel> resultList = new ArrayList<>();
        int page = 1;

        while (true) {
            try {
                document = Jsoup.connect(url + "?page=" + page).get();
            } catch (IOException e) {
                return resultList;
            }
            Elements productItems = document.select(".product-item");
            if (productItems.size() == 0) break;

            productItems.forEach(element -> {
                CrawlerModel crawlerModel = this.getProductByUrl(element.select(".product-info .product-title a").first().attr("href"));
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
            Element titleElement = document.selectFirst("#main-content > div > div.content-box > div.product-right > h1");
            title = titleElement.text().trim();
        } catch (Exception e) {
            return null;
        }


        long price;
        try {
            Element priceElement = document.selectFirst("#main-content > div > div.content-box > div.product-right > div > div.single-product-price > div:nth-child(1)");
            String strPrice = priceElement.text().replaceAll("[^0-9]", "");
            price = Long.parseLong(strPrice);
        } catch (Exception e) {
            return null;
        }


        String code;
        try {
            Element codeElement = document.selectFirst("#main-content > div > div.content-box > div.product-right > div > div.product-right-content > p:nth-child(2)");
            code = codeElement.text()
                    .substring(0, codeElement.text().indexOf("- B"))
                    .replace("(2020)", "")
                    .replaceAll("- Mã hàng:", "").trim();
            if (code.isEmpty())
                return null;
        } catch (Exception e) {
            return null;
        }


//        Element descElement = document.selectFirst("#tab1");
        String desc;
        try {
            Element descElement = document.selectFirst(".mo-ta-san-pham");
            desc = descElement.html()
                    .replaceAll("<br>", "")
                    .trim();
        } catch (Exception e) {
            desc = "";
        }


        Elements imageElements = document.select(".item img");
        List<String> images = new ArrayList<>();
        if (imageElements != null && imageElements.size() > 0) {
            images = imageElements.stream()
                    .map(element -> element.attr("src"))
                    .collect(Collectors.toList());
        } else {
            images.add("https://posxanh.com/wp-content/uploads/2019/01/dang-cap-nhat.png");
        }

        CrawlerModel crawlerModel = new CrawlerModel();

        crawlerModel.setTitle(title);
        crawlerModel.setShortDesc(desc);
        crawlerModel.setLongDesc(desc);
        crawlerModel.setCode(code);
        crawlerModel.setImages(images);
        crawlerModel.setPrice(price);
        crawlerModel.setUrl(url);


        return crawlerModel;
    }

    @Override
    public List<CrawlerModel> listLaptopDell() {
        String url = "http://xuanvinh.vn/DELL";

        return this.getListProduct(url).stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("dell"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    if (title.contains("70")) {
                        String code = title.substring(title.indexOf("70"), title.indexOf("70") + 8);
                        crawlerModel.setCode(code);
                    }

                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopLg() {
        String url = "http://xuanvinh.vn/LG";
        return getListProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("lg"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    List<String> keyList = Arrays.asList(title.split(" "));
                    String code = keyList.get(2).toLowerCase()
                            .replaceAll("-", "")
                            .replaceAll("\\.", "");
                    crawlerModel.setCode(code);

                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopAvita() {
        String url = "http://xuanvinh.vn/avita-1";
        return this.getListProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("avita"))
                .peek(crawlerModel -> {
                    String code = crawlerModel.getCode();
                    code = code.replaceAll("-", "");

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopLenovo() {
        String url = "http://xuanvinh.vn/lenovo";
        return this.getListProduct(url).stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("lenovo"))
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopHp() {
        String url = "http://xuanvinh.vn/HP";
        return this.getListProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("hp"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase()
                            .replaceAll("pavilion", "")
                            .trim();
                    if (title.contains("pa")) {
                        String code = title.substring(title.indexOf("pa") - 5, title.indexOf("pa") + 2)
                                .trim();

                        crawlerModel.setCode(code);
                    }

                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopMsi() {
        String url = "http://xuanvinh.vn/msi";
        return this.getListProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("msi"))
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("vn"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase()
                            .replaceAll("\\(", "")
                            .replaceAll("\\)", "")
                            .replaceAll("-", " ");
                    String code = title.substring(0, title.indexOf("vn") + 2);

                    List<String> keyList = Arrays.asList(code.split(" "));

                    Collections.reverse(keyList);

                    code = keyList.get(1) + keyList.get(0);

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopAsus() {
        String url = "http://xuanvinh.vn/ASUS";
        return this.getListProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("asus"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    if (title.contains("-")) {
                        String code = title.substring(0, title.indexOf(" ", title.indexOf("-"))).trim();
                        List<String> keyList = Arrays.asList(code.split(" "));
                        Collections.reverse(keyList);

                        code = keyList.get(0)
                                .replaceAll("-", "")
                                .replaceAll(" ", "")
                                .trim();

                        crawlerModel.setCode(code);
                    } else {
                        crawlerModel.setCode(crawlerModel.getCode().toLowerCase().replaceAll("-", "").replaceAll(" ", "").trim());
                    }

                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopAcer() {
        String url = "http://xuanvinh.vn/Acer";
        return this.getListProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("acer"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase()
                            .trim()
                            .replaceAll("acer", "")
                            .replaceAll("predator helios 300", "")
                            .replaceAll("aspire 3", "")
                            .replaceAll("aspire 5", "")
                            .replaceAll("aspire 7", "")
                            .replaceAll("aspire", "")
                            .replaceAll("swift 3x", "")
                            .replaceAll("swift 3", "")
                            .replaceAll("swift 5", "")
                            .replaceAll("swift 7", "")
                            .replaceAll("swift", "")
                            .replaceAll("nitro 7", "")
                            .replaceAll("nitro 5", "")
                            .replaceAll("nitro", "")
                            .replaceAll("2020", "")
                            .trim();

                    String code = title.substring(0, title.indexOf(" "))
                            .replaceAll("-", "")
                            .trim();

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listCpuIntel() {
        String url = "http://xuanvinh.vn/cpu-bo-vi-xu-ly";

        return this.getListProduct(url).stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("intel"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().replaceAll("cpu", "").trim();
                    List<String> keyList = Arrays.asList(title.split(" "));
                    String code = keyList.get(1) + keyList.get(2).replaceAll("-", "");
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());

    }

    @Override
    public List<CrawlerModel> listCpuAmd() {
        String url = "http://xuanvinh.vn/cpu-bo-vi-xu-ly";

        return this.getListProduct(url).stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("amd"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    List<String> keyList = Arrays.asList(title.split(" "));
                    String code = keyList.get(1) + keyList.get(2) + keyList.get(3);
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());

    }
}
