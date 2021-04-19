package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.ShopDunkCrawler;
import dtu.thebestprice.crawlers.model.ProductCrawler;
import dtu.thebestprice.entities.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ShopDunkCrawlerImpl implements ShopDunkCrawler {
    Document document;

    @Override
    public Set<ProductCrawler> getListIphoneByUrl(String url) throws IOException {
        document = Jsoup.connect(url).get();
        Set<ProductCrawler> listResult = new HashSet<>();
        Elements itemElements = document.select(".item-product");
        itemElements.forEach(element -> {
            try {
                listResult.add(getProduct(element.attr("data-link")));
            } catch (Exception ignored) {
            }
        });
        return listResult;
    }

    private ProductCrawler getProduct(String url) throws Exception {
        document = Jsoup.connect(url).get();
        String title = document.selectFirst("h1.text-normal").text();

        Long price = null;
        Element priceElement = document.selectFirst(".tfs-new-price p span");
        if (priceElement != null) {
            price = Long.parseLong(priceElement.attr("content"));
        }

        List<String> images = new ArrayList<>();

        Elements imageElements = document.select(".img-small a img");
        if (imageElements.size() > 0) {
            imageElements.forEach(element -> {
                images.add(element.attr("src"));
            });

        }
        StringBuffer longDesc = new StringBuffer("");
        StringBuffer shortDesc = new StringBuffer("");
        Elements longDescElements = document.select(".sdo-info-system li");

        for (int i = 0; i <=5 ; i++) {
            shortDesc.append(longDescElements.get(i).select(".detail").text());
            shortDesc.append(" : ");
            shortDesc.append(longDescElements.get(i).select(".parameter").text());
            shortDesc.append("\n");
        }

        longDescElements.forEach(element -> {
            longDesc.append(element.select(".detail").text());
            longDesc.append(" : ");
            longDesc.append(element.select(".parameter").text());
            longDesc.append("\n");
        });

        Product product = new Product();
        product.setTitle(title);
        product.setShortDescription(shortDesc.toString());
        product.setLongDescription(longDesc.toString());

        ProductCrawler productCrawler = new ProductCrawler();
        productCrawler.setProduct(product);
        productCrawler.setImages(images);
        productCrawler.setPrice(price);
        productCrawler.setUrl(url);

        return productCrawler;
    }
}
