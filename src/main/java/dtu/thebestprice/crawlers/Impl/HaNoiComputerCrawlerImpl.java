package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.HaNoiComputerCrawler;
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
public class HaNoiComputerCrawlerImpl implements HaNoiComputerCrawler {
    Document document;

    @Override
    public Long getPriceByUrl(String url) {
        try {
            document = Jsoup.connect(url).get();
            Element priceElement = document.selectFirst(".giakm");
            return Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private List<CrawlerModel> listProduct(String url) {
        int page = 1;
        List<CrawlerModel> result = new ArrayList<>();
        while (true) {
            try {
                document = Jsoup.connect(url + "/" + page + "/").get();
            } catch (IOException e) {
                return result;
            }

            Elements productItem = document.select("div.p-info h3.p-name a");
            if (productItem == null || productItem.size() == 0)
                break;
            productItem.forEach(item -> {
                CrawlerModel crawlerModel = this.getProductByUrl("https://www.hanoicomputer.vn" + item.attr("href"));
                if (crawlerModel != null)
                    result.add(crawlerModel);
            });

            page++;
        }

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
            Element titleElement = document.selectFirst("div.product_detail-header > div > h1");
            title = titleElement.text();
        } catch (Exception e) {
            title = null;
        }

        long price;
        try {
            Element priceElement = document.selectFirst(".giakm");
            price = Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }

        List<String> images = new ArrayList<>();
        try {
            Elements imageElements = document.select(".img_thumb");
            for (Element imageElement : imageElements) {
                images.add(imageElement.attr("data-href"));
            }
        } catch (Exception e) {
            images.add("https://posxanh.com/wp-content/uploads/2019/01/dang-cap-nhat.png");
        }

        StringBuilder shortDesc = new StringBuilder();
        try {
            Elements shotDescElements = document.select("#js-tskt-item li");
            for (Element shortDescElement : shotDescElements) {
                shortDesc.append(shortDescElement.text());
                shortDesc.append("\n");
            }
        } catch (Exception ignored) {
        }

        StringBuilder longDesc = new StringBuilder();
        try {
            Elements longDescElements = document.select(".bang-tskt tr");
            for (Element longDescElement : longDescElements) {
                longDesc.append(longDescElement.select("td:nth-child(1)").text());
                longDesc.append(" : ");
                longDesc.append(longDescElement.select("td:nth-child(2)").text());
                longDesc.append("\n");
            }
        } catch (Exception ignored) {
        }

        CrawlerModel crawlerModel = new CrawlerModel();
        crawlerModel.setTitle(title);
        crawlerModel.setPrice(price);
        crawlerModel.setImages(images);
        crawlerModel.setShortDesc(shortDesc.toString());
        crawlerModel.setLongDesc(longDesc.toString());
        crawlerModel.setUrl(url);

        return crawlerModel;


    }

    @Override
    public List<CrawlerModel> listLaptopAcer() {
        String url = "https://www.hanoicomputer.vn/laptop-acer";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopacer"))

                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code;
                    if (title.contains("(")) {
                        code = title.substring(0, title.indexOf("("));
                        List<String> keyList = Arrays.asList(code.split(" "));
                        Collections.reverse(keyList);
                        code = keyList.get(0).replaceAll("-", "")
                                .replaceAll("\\.", "");
                    } else {

                        List<String> keyList = Arrays.asList(title.split(" "));
                        code = keyList.get(3).replaceAll("-", "")
                                .replaceAll("\\.", "");
                    }
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopApple() {
        String url = "https://www.hanoicomputer.vn/laptop-apple";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title.substring(title.indexOf("(") + 1, title.indexOf(")"));

                    crawlerModel.setCode(code);
                }).collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopAsus() {
        String url = "https://www.hanoicomputer.vn/laptop-asus";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopasus"))

                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code;
                    if (title.contains("(")) {
                        code = title.substring(0, title.indexOf("("));
                        List<String> keyList = Arrays.asList(code.split(" "));
                        Collections.reverse(keyList);
                        code = keyList.get(0).replaceAll("-", "")
                                .replaceAll("\\.", "");
                    } else {

                        List<String> keyList = Arrays.asList(title.split(" "));
                        code = keyList.get(3).replaceAll("-", "")
                                .replaceAll("\\.", "");
                    }

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopAvita() {
        String url = "https://www.hanoicomputer.vn/laptop-avita";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopavita"))

                .filter(crawlerModel -> crawlerModel.getTitle().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title.substring(title.indexOf("(") + 1, title.indexOf(")"))
                            .replaceAll("-", "");

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopDell() {
        String url = "https://www.hanoicomputer.vn/laptop-dell";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopdell"))
                .filter(crawlerModel -> crawlerModel.getTitle().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title.substring(title.indexOf("(") + 1, title.indexOf(")"))
                            .replaceAll("\\(", "");

                    if (code.contains(" ")) {
                        title = title.substring(0, title.indexOf("("))
                                .replaceAll("\\+", "")
                                .trim();
                        List<String> keyList = Arrays.asList(title.split(" "));
                        Collections.reverse(keyList);
                        code = keyList.get(0);
                    }
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopHp() {
        String url = "https://www.hanoicomputer.vn/laptop-hp";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptophp"))
                .filter(crawlerModel -> crawlerModel.getTitle().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title.substring(title.indexOf("(") + 1, title.indexOf(")"))
                            .replaceAll("\\(", "");

                    if (code.contains(" ")) {
                        title = title.substring(0, title.indexOf("("))
                                .replaceAll("\\+", "")
                                .trim();
                        List<String> keyList = Arrays.asList(title.split(" "));
                        Collections.reverse(keyList);
                        code = keyList.get(0);
                    }
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopLg() {
        String url = "https://www.hanoicomputer.vn/laptop-lg";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptoplg"))

                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();

                    String code = title.substring(0, title.indexOf("("));
                    code = code.replaceAll(" ", "")
                            .replaceAll("laptoplggram", "")
                            .replaceAll("-", "")
                            .replaceAll("\\.", "");

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopMsi() {
        String url = "https://www.hanoicomputer.vn/laptop-msi";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopmsi"))
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
    public List<CrawlerModel> listLaptopLenovo() {
        String url = "https://www.hanoicomputer.vn/laptop-lenovo";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptoplenovo"))
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title.substring(title.indexOf("(") + 1, title.indexOf(")"));

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listCpuIntel() {
        String url = "https://www.hanoicomputer.vn/cpu-intel";

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
        String url = "https://www.hanoicomputer.vn/cpu-amd";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("amd"))
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    String code = title
                            .substring(0, title.indexOf("("))
                            .replaceAll("cpu", "")
                            .replaceAll("amd", "")
                            .replaceAll(" ", "")
                            .replaceAll("-", "")
                            .trim();
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }
}
