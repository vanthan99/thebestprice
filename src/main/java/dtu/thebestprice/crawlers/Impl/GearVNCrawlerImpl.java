package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.GearVNCrawler;
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
public class GearVNCrawlerImpl implements GearVNCrawler {
    Document document;

    @Override
    public Long getPriceByUrl(String url) {
        try {
            document = Jsoup.connect(url).get();
        } catch (Exception e) {
            System.out.println("Lỗi khi truy cập vào url: " + url);
        }

        try {
            Element priceElement = document.selectFirst(".product_sale_price");
            return Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private List<CrawlerModel> listProduct(String url) {
        List<CrawlerModel> result = new ArrayList<>();
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return result;
        }

        Elements productItem = document.select("div.product-row > a:nth-child(1)");
        if (productItem == null || productItem.size() == 0)
            return result;

        productItem.forEach(item -> {
            CrawlerModel crawlerModel = this.getProductByUrl("https://gearvn.com" + item.attr("href"));
            if (crawlerModel != null)
                result.add(crawlerModel);
        });


        return result;
    }

    private CrawlerModel getProductByUrl(String url) {
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }

        String title;
        try {
            Element titleElement = document.selectFirst(".product_name");
            title = titleElement.text();
        } catch (Exception e) {
            return null;
        }

        long price;
        try {
            Element priceElement = document.selectFirst(".product_sale_price");
            price = Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }


        StringBuffer shortDesc = new StringBuffer();
        try {
            Elements shortDescElements = document.select("#tblGeneralAttribute tr");

            for (Element shortDescElement : shortDescElements) {
                shortDesc.append(shortDescElement.select("td:nth-child(1)").text());
                shortDesc.append(" : ");
                shortDesc.append(shortDescElement.select("td:nth-child(2)").text());
                shortDesc.append("\n");
            }
        } catch (Exception ignored) {
        }

        List<String> images = new ArrayList<>();
        try {
            Elements imageElements = document.select(".product_thumbnail .fotorama img");
            for (Element imageElement : imageElements) {
                images.add(imageElement.attr("src"));
            }
        } catch (Exception e) {
            images.add("https://posxanh.com/wp-content/uploads/2019/01/dang-cap-nhat.png");
        }


        CrawlerModel crawlerModel = new CrawlerModel();
        crawlerModel.setTitle(title);
        crawlerModel.setPrice(price);
        crawlerModel.setImages(images);
        crawlerModel.setShortDesc(shortDesc.toString());
        crawlerModel.setLongDesc(shortDesc.toString());
        crawlerModel.setUrl(url);
        return crawlerModel;
    }

    @Override
    public List<CrawlerModel> listLaptopAcer() {
        String url = "https://gearvn.com/collections/laptop-acer";

        return this.listProduct(url)
                .stream()
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().trim();

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(2) + keyList.get(1) + keyList.get(0);
                    crawlerModel.setCode(code);
                }).collect(Collectors.toList());
    }

//    @Override
//    public List<CrawlerModel> listLaptopApple() {
//        return null;
//    }

    @Override
    public List<CrawlerModel> listLaptopAsus() {
        String url = "https://gearvn.com/collections/laptop-asus";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopasus"))

                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().trim();

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(1) + keyList.get(0);
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopDell() {
        String url = "https://gearvn.com/collections/laptop-dell";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopdell"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().trim();

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(0);
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopHp() {
        String url = "https://gearvn.com/collections/laptop-hp";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptophp"))

                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().trim();

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(0);
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopLg() {
        String url = "https://gearvn.com/collections/laptop-lg-cao-cap";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptoplg"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().trim();

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(1) + keyList.get(0);
                    crawlerModel.setCode(code.replaceAll("\\.", ""));
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopMsi() {
        String url = "https://gearvn.com/collections/laptop-msi";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopmsi"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().trim();

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(1) + keyList.get(0);


                    if (code.equalsIgnoreCase("evo206vn"))
                        code = "a11m206vn";

                    if (code.equalsIgnoreCase("evo089vn"))
                        code = "a11m089vn";

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopLenovo() {
        String url = "https://gearvn.com/collections/laptop-lenovo-thinkpad-chinh-hang";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptoplenovo"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().trim();

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(0);
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }
}
