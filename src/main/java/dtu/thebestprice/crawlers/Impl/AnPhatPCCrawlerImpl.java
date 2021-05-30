package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.AnPhatPCCrawler;
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
public class AnPhatPCCrawlerImpl implements AnPhatPCCrawler {
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

            Elements productItem = document.select(".p-list-container .p-item .p-name");
            if (productItem == null || productItem.size() == 0)
                break;

            productItem.forEach(item -> {
                CrawlerModel crawlerModel = this.getProductByUrl("https://www.anphatpc.com.vn" + item.attr("href"));
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
            Element titleElement = document.selectFirst("div.pro-info-center > h2");
            title = titleElement.text();
        } catch (Exception e) {
            return null;
        }

        long price;
        try {
            Element priceElement = document.selectFirst("b.js-pro-total-price");
            price = Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }


        StringBuilder shortDesc = new StringBuilder();
        try {
            Elements shortDescElements = document.select(".pro-info-summary .item");
            shortDescElements.forEach(element -> {
                shortDesc.append(element.text());
                shortDesc.append("\n");
            });
        } catch (Exception ignored) {
        }

        CrawlerModel crawlerModel = new CrawlerModel();
        crawlerModel.setTitle(title);
        crawlerModel.setPrice(price);
        crawlerModel.setImages(Collections.singletonList("https://posxanh.com/wp-content/uploads/2019/01/dang-cap-nhat.png"));
        crawlerModel.setShortDesc(shortDesc.toString());
        crawlerModel.setLongDesc(shortDesc.toString());
        crawlerModel.setUrl(url);
        return crawlerModel;
    }

    @Override
    public Long getPriceByUrl(String url) {
        try {
            document = Jsoup.connect(url).get();

            Element priceElement = document.selectFirst("b.js-pro-total-price");
            return Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<CrawlerModel> listLaptopAcer() {
        String url = "https://www.anphatpc.com.vn/laptop-acer_dm1060.html";
        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopacer"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title.substring(0, title.indexOf(".") - 3);

                    List<String> keyList = Arrays.asList(code.split(" "));
                    Collections.reverse(keyList);
                    code = keyList.get(0).replaceAll("-", "").trim();
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

//    @Override
//    public List<CrawlerModel> listLaptopApple() {
//        String url = "https://www.anphatpc.com.vn/laptop-apple_dm1064.html";
//        return this.listProduct(url)
//                .stream()
//                .peek(crawlerModel -> {
//                    String title = crawlerModel.getTitle().toLowerCase();
//
//                }).collect(Collectors.toList());
//    }

    @Override
    public List<CrawlerModel> listLaptopAsus() {
        String url = "https://www.anphatpc.com.vn/laptop-asus_dm1058.html";
        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopasus"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase()
                            .replaceAll("- bạc", "")
                            .replaceAll("- đen", "")
                            .replaceAll("- grey", "")
                            .replaceAll("- xám", "")
                            .replaceAll("- bạc", "")
                            .replaceAll("- 144hz", "")
                            .replaceAll("- trắng", "")
                            .replaceAll("- red", "").trim();
                    if (title.contains("("))
                        title = title.substring(0, title.indexOf("(")).trim();

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(0).replaceAll("-", "");
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopAvita() {
        String url = "https://www.anphatpc.com.vn/laptop-avita_dm2214.html";
        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopavita"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    List<String> keyList = Arrays.asList(title.split(" "));
                    String code = keyList.get(3).replaceAll("-", "").trim();

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopDell() {
        String url = "https://www.anphatpc.com.vn/laptop-dell_dm1012.html";
        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopdell"))
                .filter(crawlerModel -> !crawlerModel.getTitle().toLowerCase().contains("cto"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase()
                            .replaceAll("titan grey", "")
                            .replaceAll("black", "")
                            .replaceAll("- xám", "")
                            .replaceAll("- đen", "")
                            .replaceAll("đen", "")
                            .replaceAll("bạc", "")
                            .replaceAll("ugray", "")
                            .replaceAll("gray", "")
                            .replaceAll("- alu", "")
                            .trim();
                    if (title.contains("("))
                        title = title.substring(0, title.indexOf("(")).trim();

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
        String url = "https://www.anphatpc.com.vn/laptop-hp_dm1013.html";
        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptophp"))
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("pa"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase()
                            .replaceAll("pavilion", "").trim();

                    String code = title.substring(title.indexOf("pa") - 5, title.indexOf("pa") + 2);

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopLg() {
        String url = "https://www.anphatpc.com.vn/laptop-may-tinh-xach-tay-lg_dm1480.html";
        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptoplg"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    List<String> keyList = Arrays.asList(title.split(" "));
                    String code = keyList.get(4).replaceAll("-", "")
                            .replaceAll("\\.", "")
                            .trim();

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopMsi() {
        String url = "https://www.anphatpc.com.vn/Laptop-msi_dm1065.html";
        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopmsi"))
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("vn"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title.substring(0, title.indexOf("vn") + 2);
                    code = code.replaceAll("-", " ");
                    List<String> keyList = Arrays.asList(code.split(" "));
                    Collections.reverse(keyList);
                    code = keyList.get(1) + keyList.get(0);

                    if (code.trim().equalsIgnoreCase("evo089vn"))
                        code = "a11m089vn";
                    crawlerModel.setCode(code);


                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopLenovo() {
        String url = "https://www.anphatpc.com.vn/laptop-lenovo_dm1059.html";
        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptoplenovo"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase()
                            .replaceAll("gaming", "")
                            .replaceAll("\\(đen\\)", "")
                            .replaceAll("- đen", "")
                            .replaceAll("- xám", "")
                            .trim();
                    if (title.contains("("))
                        title = title.substring(0, title.indexOf("(")).trim();
                    else if (title.contains("/"))
                        title = title.substring(0, title.indexOf("/")).trim();

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(0);

                    crawlerModel.setCode(code);

                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listCpuIntel() {
        String url = "https://www.anphatpc.com.vn/cpu-intel.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("intel"))
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title
                            .substring(0, title.indexOf("("))
                            .replaceAll("cpu", "")
                            .replaceAll("intel", "")
                            .replaceAll(" ", "")
                            .replaceAll("-", "")
                            .trim();
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listCpuAmd() {
        String url = "https://www.anphatpc.com.vn/cpu-amd.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("amd"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();

                    if (title.contains(",") && title.contains("/")) {
                        if (title.indexOf(",") < title.indexOf("/"))
                            title = title.substring(0, title.indexOf(",")).trim();
                        else title = title.substring(0, title.indexOf("/")).trim();
                    } else if (title.contains("/"))
                        title = title.substring(0, title.indexOf("/")).trim();
                    else if (title.contains(","))
                        title = title.substring(0, title.indexOf(",")).trim();


                    String code = title
                            .replaceAll("cpu", "")
                            .replaceAll("amd", "")
                            .replaceAll(" ", "")
                            .replaceAll("-", "")
                            .replaceAll("3\\.6ghz\\(4.1ghzwithboost\\)", "")
                            .trim();
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }
}
