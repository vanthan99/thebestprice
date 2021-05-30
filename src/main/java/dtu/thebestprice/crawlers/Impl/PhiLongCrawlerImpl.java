package dtu.thebestprice.crawlers.Impl;

import dtu.thebestprice.crawlers.PhiLongCrawler;
import dtu.thebestprice.crawlers.filters.MyFilter;
import dtu.thebestprice.crawlers.model.CrawlerModel;
import dtu.thebestprice.crawlers.model.ProductCrawler;
import dtu.thebestprice.entities.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PhiLongCrawlerImpl implements PhiLongCrawler {

    Document document;

    @Override
    public Long getPriceByUrl(String url) {
        try {
            document = Jsoup.connect(url).get();
            Element priceElement = document.selectFirst(".p-price span");
            return Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private List<CrawlerModel> listProduct(String url) {
        List<CrawlerModel> listResult = new ArrayList<>();
        int i = 1;
        while (true) {
            try {
                document = Jsoup.connect(url + "?page=" + i).get();
            } catch (IOException e) {
                System.out.println("Đã có vấn đề với url: " + url + "?page=" + i);
                return listResult;
            }
            Elements itemsElements = document.select(".p-item .p-name a");
            if (itemsElements.size() == 0) break;
            itemsElements.forEach(element -> {
                CrawlerModel crawlerModel;
                try {
                    crawlerModel = getProductByUrl("https://philong.com.vn" + element.attr("href"));
                    if (crawlerModel != null) {
                        listResult.add(crawlerModel);
                    }
                } catch (Exception ignored) {
                }
            });
            i++;
        }

        return listResult;
    }

    // lấy thông tin sản phẩm dựa vào url
    private CrawlerModel getProductByUrl(String url) {
//        System.out.println("url = " + url);
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
        String title;
        try {
            title = document.selectFirst(".entry-top .entry-header h1").text();
        } catch (Exception e) {
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


//        Elements longDescEs = document.select("#tb-product-spec .tbl-technical tbody tr");
//        StringBuffer longDesc = new StringBuffer("");
//        if (document.select("table#tblGeneralAttribute tr") != null) {
//            Elements longElements = document.select("#tblGeneralAttribute tr");
//
//            for (Element longElement : longElements) {
//                shortDesc.append(longElement.select("td:nth-child(1)").text());
//                shortDesc.append(" : ");
//                shortDesc.append(longElement.select("td:nth-child(2)").text());
//                shortDesc.append("\n");
//            }
//        } else if (document.select("#tb-product-spec .tbl-technical tbody tr") != null) {
//            Elements longDescEs = document.select("#tb-product-spec .tbl-technical tbody tr");
//            longDescEs.forEach(element -> {
//                longDesc.append(element.text().replaceAll("&nbsp", " "));
//                longDesc.append("\n");
//            });
//        } else if (document.select("#tb-product-spec .tbl-technical p") != null) {
//            Elements longDescEs = document.select("#tb-product-spec .tbl-technical p");
//            longDescEs.forEach(element -> {
//                longDesc.append(element.text().replaceAll("&nbsp", " "));
//                longDesc.append("\n");
//            });
//        } else if (document.select("#tb-product-spec .tbl-technical li") != null) {
//            Elements longDescEs = document.select("#tb-product-spec .tbl-technical li");
//
//            longDescEs.forEach(element -> {
//                longDesc.append(element.text().replaceAll("&nbsp;", " "));
//                longDesc.append("\n");
//            });
//
//        } else longDesc.append("");


        long price;
        try {
            Element priceElement = document.selectFirst(".p-price span");
            price = Long.parseLong(priceElement.text().replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }


        CrawlerModel crawlerModel = new CrawlerModel();
        crawlerModel.setTitle(title);
        crawlerModel.setShortDesc(shortDesc.toString().replaceAll("&nbsp;", ""));
        crawlerModel.setLongDesc(shortDesc.toString());
        crawlerModel.setImages(images);
        crawlerModel.setPrice(price);
        crawlerModel.setCode(null);
        crawlerModel.setUrl(url);

        return crawlerModel;
    }


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

    @Override
    public List<CrawlerModel> listLaptopAsus() {

        String url = "https://philong.com.vn/laptop-asus.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopasus"))
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    title = title.substring(0, title.indexOf("(")).trim();
                    title = title.replaceAll(" - ", " ")
                            .replaceAll("- ", " ")
                            .replaceAll(" -", " ")
                            .replaceAll("-", " ")
                            .trim()
                    ;


                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(1) + keyList.get(0);
                    code = code.trim().toLowerCase();

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopAcer() {
        String url = "https://philong.com.vn/laptop-acer.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopacer"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().toLowerCase();
                    title = title.substring(0, title.indexOf("(")).trim();
                    title = title.replaceAll(" - ", " ")
                            .replaceAll("- ", " ")
                            .replaceAll(" -", " ")
                            .replaceAll("-", " ")
                            .trim()
                    ;


                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(2) + keyList.get(1) + keyList.get(0);
                    code = code.trim().toLowerCase();

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopDell() {
        String url = "https://philong.com.vn/laptop-dell.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopdell"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().replaceAll("-", " ");
                    String code;

                    // kiểm tra xem có bao nhiêu dấu ngoặc
                    int numOfChar = title.split("\\(", -1).length - 1;

                    if (numOfChar == 2) {
                        // set code ở () thứ nhất
                        code = title.substring(title.indexOf("(") + 1, title.indexOf(")"));
                    } else {
                        title = title.substring(0, title.indexOf("("));

                        List<String> keyList = Arrays.asList(title.split(" "));
                        Collections.reverse(keyList);
                        code = keyList.get(0).trim();
                    }
                    crawlerModel.setCode(code.replaceAll("-", ""));
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopLenovo() {
        String url = "https://philong.com.vn/laptop-lenovo.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptoplenovo"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    String code = null;
                    List<String> listContain = Arrays.asList(
                            "81W",
                            "81L",
                            "81Y",
                            "81U",

                            "82H",
                            "82A",
                            "82E",
                            "82B",
                            "82F",
                            "82L",

                            "20V",
                            "20T",
                            "20R"
                    );
                    for (String key : listContain) {
                        if (title.contains(key)) {
                            code = title.substring(title.indexOf(key), title.indexOf(key) + 10).toLowerCase();
                            break;
                        }
                    }

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopHp() {
        String url = "https://philong.com.vn/laptop-hp.html";

        List<String> listContains = Arrays.asList(
                "pa",
                "tu"
        );

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptophp"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    String code = title.toLowerCase().replaceAll("pavilion", "");


                    for (String key : listContains) {
                        if (code.contains(key) && key.equals("pa")) {
                            code = code.substring(code.indexOf(key) - 5, code.indexOf(key) + 2);
                            break;
                        }

                        if (code.contains(key)) {
                            code = code.substring(code.indexOf(key) - 6, code.indexOf(key) + 1);
                        }
                    }

                    crawlerModel.setCode(code.trim());
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listLaptopMsi() {
        String url = "https://philong.com.vn/laptop-msi.html";

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
    public List<CrawlerModel> listLaptopLg() {
        String url = "https://philong.com.vn/laptop-lg.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptoplg"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    if (title.contains("("))
                        title = title.substring(0, title.indexOf("("))
                                .replaceAll(" - ", "");

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);
                    String code = keyList.get(0)
                            .replaceAll("-", "")
                            .replaceAll("\\.", "");
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

//    @Override
//    public List<CrawlerModel> listLaptopMicrosoft() throws IOException {
//        String url = "https://philong.com.vn/laptop-microsoft-surface.html";
//
//        return this.listProduct(url)
//                .stream()
//                .filter(crawlerModel -> crawlerModel.getPrice() >= 10000000)
//                .peek(crawlerModel -> {
//                    String title = crawlerModel.getTitle();
//                    String code = title.substring(0, title.indexOf("("))
//                            .toLowerCase()
//                            .replaceAll(" ", "")
//                            .replaceAll("-", "")
//                            .replaceAll("microsoft", "");
//                    crawlerModel.setCode(code);
//                }).collect(Collectors.toList());
//    }

    @Override
    public List<CrawlerModel> listLaptopAvita() {
        String url = "https://philong.com.vn/laptop-avita.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptopavita"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle().substring(0, crawlerModel.getTitle().indexOf("("));

                    List<String> keyList = Arrays.asList(title.split(" "));
                    Collections.reverse(keyList);


                    String code = keyList.get(0).replaceAll("-", "");
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listMonitorHp() {
        String url = "https://philong.com.vn/thuong-hieu-man-hinh-hp.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().replaceAll(" ", "").trim().contains("laptophp"))
                .filter(crawlerModel -> crawlerModel.getTitle().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    String code = title.substring(title.indexOf("(") + 1, title.indexOf(")"))
                            .toLowerCase();
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listMonitorSamsung() {
        String url = "https://philong.com.vn/thuong-hieu-man-hinh-samsung.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("samsung"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    String code = title.substring(title.toLowerCase().indexOf("samsung"))
                            .toLowerCase()
                            .replaceAll("samsung", "")
                            .trim();

                    code = code.toLowerCase()
                            .replaceAll("samsung", "")
                            .substring(0, code.indexOf(" "))
                            .replaceAll(",", "");


                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listMonitorDell() {
        String url = "https://philong.com.vn/thuong-hieu-man-hinh-dell.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("dell"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    String code;
                    code = title.substring(title.toLowerCase().indexOf("dell"))
                            .toLowerCase()
                            .replaceAll("dell", "")
                            .trim();
                    if (title.contains("(")) {


                        code = code.toLowerCase()
                                .replaceAll("dell", "")
                                .substring(0, code.indexOf("("))
                                .replaceAll(" ", "")
                                .replaceAll(",", "")
                                .replaceAll("professional", "")
                                .replaceAll("ultrasharp", "")
                                .replaceAll("27\"", "")
                                .replaceAll("2kqhd", "")
                                .replaceAll("23.8\"", "")
                                .replaceAll("25\"", "")
                                .replaceAll("17\"", "")
                                .replaceAll("ULTRAHD4KMULTICLIENT".toLowerCase(), "");
                    } else {
                        code = code.toLowerCase()
                                .replaceAll("dell", "")
                                .substring(0, code.indexOf(","))
                                .replaceAll(" ", "")
                                .replaceAll(",", "")
                                .replaceAll("professional", "")
                                .replaceAll("ultrasharp", "")
                                .replaceAll("27\"", "")
                                .replaceAll("2kqhd", "")
                                .replaceAll("23.8\"", "")
                                .replaceAll("25\"", "")
                                .replaceAll("17\"", "")
                                .replaceAll("FHD".toLowerCase(), "")
                                .replaceAll("2K-QHD".toLowerCase(), "")
                                .replaceAll("GAMING".toLowerCase(), "")
                                .replaceAll("IPS".toLowerCase(), "")
                                .replaceAll("ULTRAHD4KMULTICLIENT".toLowerCase(), "");
                    }


                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listMonitorLg() {
        String url = "https://philong.com.vn/thuong-hieu-man-hinh-lg.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("lg"))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    String code = title.toLowerCase()
                            .replaceFirst("QHD".toLowerCase(), "")
                            .replaceAll("UltraGear Curved 38\"".toLowerCase(), "")
                            .replaceAll("34-", "")
                            .replaceAll("- Model:".toLowerCase(), "")
                            .replaceAll(".ATV".toLowerCase(), "")
                            .replaceAll("-", "")
                            .replaceAll("ULTRAWIDE 29''".toLowerCase(), "")
                            .replaceAll("ULTRAWIDE".toLowerCase(), "")
                            .replaceAll("23.8\"", "")
                            .replaceAll("38\"", "")
                            .replaceAll("27\"", "")
                            .replaceAll("24\"", "")
                            .replaceAll("21.5\"", "")
                            .replaceAll("32\"", "")
                            .replaceAll("UltraFine UHD 31.5''".toLowerCase(), "")
                            .replaceAll("UltraWide™ 34'' IPS Full HD AMD FreeSync™ VESA DisplayHDR™ 400 sRGB 99% USB Type-C™".toLowerCase(), "")
                            .replaceAll("ULTRAGEAR".toLowerCase(), "")
                            .replaceAll("38'' UltraWide™ QHD+ Nano IPS VESA DisplayHDR™ 600 Thunderbolt ".toLowerCase(), "")
                            .replaceAll("UltraGear™".toLowerCase(), "")
                            .replaceAll("27'' UltraGear™ UHD 4K Nano IPS 1ms VESA DisplayHDR 600".toLowerCase(), "")
                            .replaceAll("38'' UltraWide™ QHD+ Nano IPS VESA DisplayHDR™ 600 Thunderbolt".toLowerCase(), "");

                    try {
                        code = code.substring(code.indexOf("lg"));
                        code = code.substring(0, code.indexOf(" ", 4));
                        code = code.replaceAll(",", "")
                                .replaceAll("-", "")
                                .replaceFirst("lg".toLowerCase(), "").trim();
                    } catch (Exception e) {
                        code = null;
                    }

                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listCpuIntel() {
        String url = "https://philong.com.vn/cpu-vi-xu-ly-intel.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    String code = title
                            .substring(title.toLowerCase().indexOf("intel"), title.indexOf("("))
                            .replaceAll(" ", "")
                            .replaceAll("-", "")
                            .toLowerCase()
                            .replaceAll("intel", "").trim();
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listCpuAmd() {
        String url = "https://philong.com.vn/cpu-vi-xu-ly-amd.html";

        return this.listProduct(url)
                .stream()
                .filter(crawlerModel -> crawlerModel.getTitle().contains("("))
                .peek(crawlerModel -> {
                    String title = crawlerModel.getTitle();
                    String code = title
                            .substring(title.toLowerCase().indexOf("amd"), title.indexOf("("))
                            .replaceAll(" ", "")
                            .replaceAll("-", "")
                            .toLowerCase()
                            .replaceAll("amd", "").trim();
                    crawlerModel.setCode(code);
                })
                .filter(MyFilter.distinctByKey(CrawlerModel::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public List<CrawlerModel> listMainBoardAsus() {
        return null;
    }

//    @Override
//    public List<CrawlerModel> listMainBoardAsus() throws IOException {
//        String url = "https://philong.com.vn/mainboard-asus.html";
//
//        return this.listProduct(url)
//                .stream()
//                .filter(crawlerModel -> crawlerModel.getTitle().toLowerCase().contains("asus"))
//                .peek(crawlerModel -> {
//                    String title = crawlerModel.getTitle();
//                    String code;
//
//                    if (title.contains("("))
//                        code = title.substring(title.toLowerCase().indexOf("asus"),title.toLowerCase().indexOf("("))
//                                .toLowerCase()
//                                .replaceAll("asus","")
//                                .replaceAll("-","")
//                                .replaceAll("\\.","")
//                                .replaceAll(" ","");
//                    if (title == null && title.contains(","))
//                        code = title.substring(title.toLowerCase().indexOf("asus"),title.toLowerCase().indexOf(","))
//                                .toLowerCase()
//                                .replaceAll("asus","")
//                                .replaceAll("-","")
//                                .replaceAll("\\.","")
//                                .replaceAll(" ","");
//
//
//
//                    String code = title
//                            .substring(title.toLowerCase().indexOf("intel"), title.indexOf("("))
//                            .replaceAll(" ", "")
//                            .replaceAll("-", "")
//                            .toLowerCase()
//                            .replaceAll("intel", "");
//                    crawlerModel.setCode(code);
//                }).collect(Collectors.toList());
//    }

    private CrawlerModel getLaptopAsus(String url) {
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String title;
        try {
            title = document.selectFirst(".entry-top .entry-header h1").text();
        } catch (NullPointerException e) {
            return null;
        }

        String code = title.substring(0, title.indexOf("("))
                .replaceAll("-", "")
                .replaceAll(" ", "")
                .toLowerCase()
                .replaceAll("laptopasus", "");

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
            if (longDescEs != null && longDescEs.size() > 0) {
                longDescEs.forEach(element -> {
                    longDesc.append(element.text().replaceAll("&nbsp", " "));
                    longDesc.append("\\n");
                });
            } else {
                longDescEs = document.select("#tb-product-spec .tbl-technical li");
                if (longDescEs != null && longDescEs.size() > 0) {
                    longDescEs.forEach(element -> {
                        longDesc.append(element.text().replaceAll("&nbsp;", " "));
                        longDesc.append("\n");
                    });
                }

            }
        }


        long price;
        Element priceElement = document.selectFirst(".p-price span");
        if (priceElement != null) {
            try {
                price = Long.parseLong(priceElement.text().replaceAll("\\.", "").replaceAll("đ", ""));
            } catch (NumberFormatException e) {
                return null;
            }
        } else return null;


        CrawlerModel crawlerModel = new CrawlerModel();
        crawlerModel.setTitle(title);
        crawlerModel.setShortDesc(shortDesc.toString().replaceAll("&nbsp;", ""));
        crawlerModel.setLongDesc(longDesc.toString());
        crawlerModel.setImages(images);
        crawlerModel.setPrice(price);
        crawlerModel.setCode(code);
        crawlerModel.setUrl(url);

        return crawlerModel;
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
                return null;
            }
        } else return null;


        Product product = new Product();
        product.setTitle(title);
        product.setShortDescription(shortDesc.toString().replaceAll("&nbsp;", ""));
        product.setLongDescription(longDesc.toString());


        return new ProductCrawler(product, price, images, s);

    }

}

