package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.PhiLongCrawler;
import dtu.thebestprice.crawlers.model.ProductCrawler;
import dtu.thebestprice.entities.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PhiLongCrawlerImpl implements PhiLongCrawler {

    Document document;

    @Override
    public Set<ProductCrawler> getProductByURL(String url) throws IOException {
        Set<ProductCrawler> listResult = new HashSet<>();

        document = Jsoup.connect(url).get();
        int i = 1;
        while (true) {
            document = Jsoup.connect(url + "?page=" + i).get();
            Elements itemsElements = document.select(".p-item .p-name a");
            if (itemsElements.size() == 0) break;
            itemsElements.forEach(element -> {
                ProductCrawler productCrawler = null;
                try {
                    productCrawler = getProductDetail("https://philong.com.vn" + element.attr("href"));
                    if (productCrawler != null) {
                        listResult.add(productCrawler);
                    }
                } catch (Exception ignored) {

                }


            });
            i++;
        }

        return listResult;
    }

    // chỉ cào cho laptop của philong
    public ProductCrawler getProductDetail(String s) throws Exception {
        System.out.println("Đã vào " + s);
        try {
            document = Jsoup.connect(s).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String title;
        try {
            title = document.selectFirst(".entry-top .entry-header h1").text();
        } catch (NullPointerException e) {
            return null;
        }

        Elements listImageElement = document.select("#sync2 a");

        List<String> images = new ArrayList<>();
        if (listImageElement.size() > 0) {
            listImageElement.forEach(element -> {
                images.add("https://philong.com.vn" + element.attr("href"));
            });
        } else {
            listImageElement = document.select("#Zoomer");
            if (listImageElement.size() > 0)
                images.add("https://philong.com.vn" + listImageElement.first().attr("href"));
            else images.add("https://dummyimage.com/380x380/fff/333333.png&text=%C4%90ang+c%E1%BA%ADp+nh%E1%BA%ADt");
        }

        Elements shortDescElements = document.select(".pro-info p");

        StringBuffer shortDesc = new StringBuffer("");

        if (shortDescElements.size() > 0) {
            shortDescElements.forEach(element -> {
                shortDesc.append(element.text());
                shortDesc.append("\n");
            });
        }
        Elements longDescEs = document.select("#tb-product-spec .tbl-technical tbody tr");
        StringBuffer longDesc = new StringBuffer("");

        if (longDescEs.size() > 0) {
            longDescEs.forEach(element -> {
                longDesc.append(element.text());
                longDesc.append("\n");
            });
        } else {
            longDescEs = document.select("#tb-product-spec .tbl-technical p");
            if (longDescEs != null || longDescEs.size() > 0) {
                longDescEs.forEach(element -> {
                    longDesc.append(element.text().replaceAll("&nbsp", " "));
                    longDesc.append("\\n");
                });
            } else {
                longDescEs = document.select("#tb-product-spec .tbl-technical li");
                if (longDescEs != null || longDescEs.size() > 0) {
                    longDescEs.forEach(element -> {
                        longDesc.append(element.text().replaceAll("&nbsp;", " "));
                        longDesc.append("\n");
                    });
                }

            }
        }


        Long price = null;
        Element priceElement = document.selectFirst(".p-price span");
        if (priceElement != null) {
            try {
                price = Long.parseLong(priceElement.text().replaceAll("\\.", "").replaceAll("đ", ""));
            } catch (NumberFormatException e) {
            }
        }


        Product product = new Product();
        product.setTitle(title);
        product.setShortDescription(shortDesc.toString().replaceAll("&nbsp;", ""));
        product.setLongDescription(longDesc.toString());



        return new ProductCrawler(product, price, images, s);

    }

}

