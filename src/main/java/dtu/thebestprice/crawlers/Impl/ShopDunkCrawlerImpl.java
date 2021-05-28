package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.ShopDunkCrawler;
import dtu.thebestprice.crawlers.model.CrawlerModel;
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
import java.util.stream.Collectors;

@Component
public class ShopDunkCrawlerImpl implements ShopDunkCrawler {
    Document document;

    @Override
    public Long getPriceByUrl(String url) {
        try {
            document = Jsoup.connect(url).get();
            Element priceElement = document.selectFirst(".tfs-new-price p span");
            return Long.parseLong(priceElement.attr("content"));
        } catch (Exception e) {
            return null;
        }
    }

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

    private List<CrawlerModel> listProduct(String url) {
        List<CrawlerModel> result = new ArrayList<>();

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return result;
        }
        Elements itemElements = document.select(".item-product");
        itemElements.forEach(element -> {
            try {
                CrawlerModel crawlerModel = getProductByUrl(element.attr("data-link"));
                if (crawlerModel != null)
                    result.add(crawlerModel);

            } catch (Exception ignored) {
            }
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
            title = document.selectFirst("h1.text-normal").text();
        } catch (Exception e) {
            return null;
        }

        long price;
        try {
            Element priceElement = document.selectFirst(".tfs-new-price p span");
            price = Long.parseLong(priceElement.attr("content"));
        } catch (Exception e) {
            return null;
        }

        List<String> images = new ArrayList<>();

        Elements imageElements = document.select(".img-small a img");
        if (imageElements.size() > 0) {
            imageElements.forEach(element -> {

                try {
                    if (!isRedirectHomePage(element.attr("src")))
                        images.add(element.attr("src"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        StringBuffer longDesc = new StringBuffer("");
        StringBuffer shortDesc = new StringBuffer("");
        Elements longDescElements = document.select(".sdo-info-system li");

        for (int i = 0; i <= 5; i++) {
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

        CrawlerModel crawlerModel = new CrawlerModel();

        crawlerModel.setTitle(title);
        crawlerModel.setPrice(price);
        crawlerModel.setLongDesc(longDesc.toString());
        crawlerModel.setShortDesc(shortDesc.toString());
        crawlerModel.setImages(images);
        crawlerModel.setUrl(url);

        return crawlerModel;
    }

    @Override
    public List<CrawlerModel> listIphone() {
        String url = "https://shopdunk.com/iphone/";

        return listProduct(url)
                .stream()
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase().
                            replaceAll(" ", "").trim();

                    String code = title.substring(0, title.indexOf("ch")).trim();
                    String memory = title.substring(title.indexOf("gb") - 3, title.indexOf("gb") + 2)
                            .replaceAll("\\(", "");
                    code = code + memory;

                    crawlerModel.setCode(code);
                }).collect(Collectors.toList());
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

                try {
                    if (!isRedirectHomePage(element.attr("src")))
                        images.add(element.attr("src"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        StringBuffer longDesc = new StringBuffer("");
        StringBuffer shortDesc = new StringBuffer("");
        Elements longDescElements = document.select(".sdo-info-system li");

        for (int i = 0; i <= 5; i++) {
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

    private boolean isRedirectHomePage(String url) throws IOException {
        Document document = Jsoup.connect(url).ignoreContentType(true).get();
        return document.baseUri().equalsIgnoreCase("https://shopdunk.com/");
    }
}
