package dtu.thebestprice;

import dtu.thebestprice.crawlers.*;
import dtu.thebestprice.crawlers.model.CrawlerModel;
import dtu.thebestprice.crawlers.model.ProductCrawler;
import dtu.thebestprice.entities.*;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.payload.request.BannerRequest;
import dtu.thebestprice.payload.request.CategoryRequest;
import dtu.thebestprice.repositories.*;
import dtu.thebestprice.services.AuthService;
import dtu.thebestprice.services.BannerService;
import dtu.thebestprice.services.CategoryService;
import dtu.thebestprice.services.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class initData {
    @Autowired
    AuthService authService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    BrandRepository brandRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Autowired
    PriceRepository priceRepository;

    @Autowired
    PhiLongCrawler phiLongCrawler;

    @Autowired
    ShopDunkCrawler shopDunkCrawler;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    SearchRepository searchRepository;

    @Autowired
    SearchStatisticRepository searchStatisticRepository;

    @Autowired
    ViewCountStatisticRepository viewCountStatisticRepository;

    @Autowired
    StatisticAccessRepository statisticAccessRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RateService rateService;

    @Autowired
    BannerService bannerService;

    private final String PHILONG_LAPTOP_ACER = "https://philong.com.vn/laptop-acer.html";
    private final String PHILONG_LAPTOP_ASUS = "https://philong.com.vn/laptop-asus.html";
    private final String PHILONG_LAPTOP_DELL = "https://philong.com.vn/laptop-dell.html";
    private final String PHILONG_LAPTOP_LENOVO = "https://philong.com.vn/laptop-lenovo.html";
    private final String PHILONG_LAPTOP_HP = "https://philong.com.vn/laptop-hp.html";
    private final String PHILONG_LAPTOP_MSI = "https://philong.com.vn/laptop-msi.html";
    private final String PHILONG_LAPTOP_LG = "https://philong.com.vn/laptop-lg.html";
    private final String PHILONG_LAPTOP_MICROSOFT = "https://philong.com.vn/laptop-microsoft-surface.html";
    private final String PHILONG_LAPTOP_AVITA = "https://philong.com.vn/laptop-avita.html";

    private final String PHILONG_MANHINH_VIEWSONIC = "https://philong.com.vn/man-hinh-may-tinh-viewsonic";
    private final String PHILONG_MANHINH_ACER = "https://philong.com.vn/man-hinh-may-tinh-acer";
    private final String PHILONG_MANHINH_AOC = "https://philong.com.vn/man-hinh-may-tinh-aoc";
    private final String PHILONG_MANHINH_ASUS = "https://philong.com.vn/man-hinh-may-tinh-asus";
    private final String PHILONG_MANHINH_DELL = "https://philong.com.vn/man-hinh-may-tinh-dell";
    private final String PHILONG_MANHINH_GIGABYTE = "https://philong.com.vn/man-hinh-may-tinh-gigabyte";
    private final String PHILONG_MANHINH_HKC = "https://philong.com.vn/man-hinh-may-tinh-hkc";
    private final String PHILONG_MANHINH_HP = "https://philong.com.vn/man-hinh-may-tinh-hp";
    private final String PHILONG_MANHINH_INFINITY = "https://philong.com.vn/man-hinh-may-tinh-infinity";
    private final String PHILONG_MANHINH_LENOVO = "https://philong.com.vn/man-hinh-may-tinh-lenovo";
    private final String PHILONG_MANHINH_LG = "https://philong.com.vn/man-hinh-may-tinh-lg";
    private final String PHILONG_MANHINH_MSI = "https://philong.com.vn/man-hinh-may-tinh-msi";
    private final String PHILONG_MANHINH_PHILIPS = "https://philong.com.vn/man-hinh-may-tinh-philips";
    private final String PHILONG_MANHINH_SAMSUNG = "https://philong.com.vn/man-hinh-may-tinh-samsung";

    private final String PHILONG_CPU_AMD = "https://philong.com.vn/cpu-vi-xu-ly-phi-long-da-nang-amd";
    private final String PHILONG_CPU_INTEL = "https://philong.com.vn/cpu-vi-xu-ly-phi-long-da-nang-intel";

    private final String PHILONG_MAINBOARD_ASUS = "https://philong.com.vn/mainboard-bo-mach-chu-asus";
    private final String PHILONG_MAINBOARD_GIGABYTE = "https://philong.com.vn/mainboard-bo-mach-chu-gigabyte";
    private final String PHILONG_MAINBOARD_INTEL = "https://philong.com.vn/mainboard-bo-mach-chu-intel";
    private final String PHILONG_MAINBOARD_MSI = "https://philong.com.vn/mainboard-bo-mach-chu-msi";

    private final String PHILONG_RAM_CORSAIR = "https://philong.com.vn/ram-may-tinh-build-pc-tai-da-nang-corsair";
    private final String PHILONG_RAM_GALAX = "https://philong.com.vn/ram-may-tinh-build-pc-tai-da-nang-galax";
    private final String PHILONG_RAM_GEIL = "https://philong.com.vn/ram-may-tinh-build-pc-tai-da-nang-geil";
    private final String PHILONG_RAM_GSKILL = "https://philong.com.vn/ram-may-tinh-build-pc-tai-da-nang-gskill";
    private final String PHILONG_RAM_KINGMAX = "https://philong.com.vn/ram-may-tinh-build-pc-tai-da-nang-kingmax";
    private final String PHILONG_RAM_KINGSTON = "https://philong.com.vn/ram-may-tinh-build-pc-tai-da-nang-kingston";
    private final String PHILONG_RAM_PATRIOT = "https://philong.com.vn/ram-may-tinh-build-pc-tai-da-nang-patriot";
    private final String PHILONG_RAM_SAMSUNG = "https://philong.com.vn/ram-may-tinh-build-pc-tai-da-nang-samsung";

    private final String PHILONG_HDD_ASUS = "https://philong.com.vn/o-cung-ssd-hdd-desktop-tai-phi-long-asus";
    private final String PHILONG_HDD_SEAGATE = "https://philong.com.vn/o-cung-ssd-hdd-desktop-tai-phi-long-seagate";
    private final String PHILONG_HDD_TOSHIBA = "https://philong.com.vn/o-cung-ssd-hdd-desktop-tai-phi-long-toshiba";
    private final String PHILONG_HDD_WD = "https://philong.com.vn/o-cung-ssd-hdd-desktop-tai-phi-long-western-digital";

    private final String PHILONG_SSD_CORSAIR = "https://philong.com.vn/o-cung-ssd-tai-da-nang-corsair";
    private final String PHILONG_SSD_GIGABYTE = "https://philong.com.vn/o-cung-ssd-tai-da-nang-gigabyte";
    private final String PHILONG_SSD_INTEL = "https://philong.com.vn/o-cung-ssd-tai-da-nang-intel";
    private final String PHILONG_SSD_KINGMAX = "https://philong.com.vn/o-cung-ssd-tai-da-nang-kingmax";
    private final String PHILONG_SSD_KINGSTON = "https://philong.com.vn/o-cung-ssd-tai-da-nang-kingston";
    private final String PHILONG_SSD_PLEXTOR = "https://philong.com.vn/o-cung-ssd-tai-da-nang-plextor";
    private final String PHILONG_SSD_SAMSUNG = "https://philong.com.vn/o-cung-ssd-tai-da-nang-samsung";
    private final String PHILONG_SSD_WD = "https://philong.com.vn/o-cung-ssd-tai-da-nang-western-digital";

    private final String PHILONG_VGA_ASUS = "https://philong.com.vn/card-do-hoa-asus";
    private final String PHILONG_VGA_GIGABYTE = "https://philong.com.vn/card-do-hoa-gigabyte";
    private final String PHILONG_VGA_MSI = "https://philong.com.vn/card-do-hoa-msi";

    private final String PHILONG_PSU_ASUS = "https://philong.com.vn/nguon-may-tinh-pc-psu-asus";
    private final String PHILONG_PSU_CORSAIR = "https://philong.com.vn/nguon-may-tinh-pc-psu-corsair";
    private final String PHILONG_PSU_GIGABYTE = "https://philong.com.vn/nguon-may-tinh-pc-psu-gigabyte";
    private final String PHILONG_PSU_MSI = "https://philong.com.vn/nguon-may-tinh-pc-psu-msi";

    private final String PHILONG_CASE_GIGABYTE = "https://philong.com.vn/case-vo-may-tinh-PC-gigabyte";

    private final String PHILONG_TAINGHE_APPLE = "https://philong.com.vn/thiet-bi-am-thanh-apple";
    private final String PHILONG_TAINGHE_CORSAIR = "https://philong.com.vn/thiet-bi-am-thanh-corsair";
    private final String PHILONG_TAINGHE_DARUE = "https://philong.com.vn/thiet-bi-am-thanh-dare-u";
    private final String PHILONG_TAINGHE_JBL = "https://philong.com.vn/am-thanh-chieu-sang-jbl";
    private final String PHILONG_TAINGHE_LG = "https://philong.com.vn/loa-tai-nghe-webcam-micro-lg";
    private final String PHILONG_TAINGHE_LOGITECH = "https://philong.com.vn/am-thanh-chieu-sang-logitech";

    private final String SHUPDUNK_IPHONE12 = "https://shopdunk.com/iphone-12-12-pro-12-pro-max/";
    private final String SHUPDUNK_IPHONE11 = "https://shopdunk.com/iphone-11/";
    private final String SHUPDUNK_IPHONEXr = "https://shopdunk.com/iphone-xr/";
    private final String SHUPDUNK_IPHONESE = "https://shopdunk.com/iphone-se-2020/";


    @Autowired
    XuanVinhCrawler xuanVinhCrawler;

    @Autowired
    AppleTAndTCrawler appleTAndTCrawler;

    @Autowired
    AnhDucDigitalCrawler anhDucDigitalCrawler;

    @Autowired
    HaNoiComputerCrawler haNoiComputerCrawler;

    @Autowired
    AnPhatPCCrawler anPhatPCCrawler;

    @Autowired
    GearVNCrawler gearVNCrawler;

    @Autowired
    DiDongVietCrawler diDongVietCrawler;

//    @PostConstruct
    public void init() {

//        System.out.println("Bắt đầu lưu user");
//        initUser();
//
//        System.out.println("Bắt đầu lưu category");
//        initCategory();
//
//        System.out.println("Bắt đầu lưu retailer");
//        initRetailer();
//
//        System.out.println("Bắt đầu lưu brand");
//        initBrand();
//
//        Retailer phiLongretailer = retailerRepository.findByHomePage("https://philong.com.vn/");
//        Retailer xuanVinhRetailer = retailerRepository.findByHomePage("http://xuanvinh.vn/");
//        Retailer shupDunkRetailer = retailerRepository.findByHomePage("https://shopdunk.com/");
//        Retailer anhDucDigitalRetailer = retailerRepository.findByHomePage("https://anhducdigital.vn/");
//        Retailer anPhatPCRetailer = retailerRepository.findByHomePage("https://www.anphatpc.com.vn/");
//        Retailer iphoneDaNangRetailer = retailerRepository.findByHomePage("https://iphonedanang.com.vn/");
//        Retailer diDongVietRetailer = retailerRepository.findByHomePage("https://didongviet.vn/");
//        Retailer gearVNRetailer = retailerRepository.findByHomePage("https://gearvn.com/");
//        Retailer haNoiComputerRetailer = retailerRepository.findByHomePage("https://www.hanoicomputer.vn/");
//
//
//        Brand asusBrand = brandRepository.findByName("ASUS");
//        Brand acerBrand = brandRepository.findByName("ACER");
//        Brand dellBrand = brandRepository.findByName("DELL");
//        Brand lenovoBrand = brandRepository.findByName("LENOVO");
//        Brand hpBrand = brandRepository.findByName("HP");
//        Brand msiBrand = brandRepository.findByName("MSI");
//        Brand lgBrand = brandRepository.findByName("LG");
//        Brand avitaBrand = brandRepository.findByName("AVITA");
//        Brand appleBrand = brandRepository.findByName("APPLE");
//        Brand amdBrand = brandRepository.findByName("AMD");
//        Brand intelBrand = brandRepository.findByName("INTEL");
//
//
//        Category laptopAsus = categoryRepository.findByTitle("Laptop ASUS");
//        Category laptopAcer = categoryRepository.findByTitle("Laptop ACER");
//        Category laptopDell = categoryRepository.findByTitle("Laptop DELL");
//        Category laptopLenovo = categoryRepository.findByTitle("Laptop LENOVO");
//        Category laptopHp = categoryRepository.findByTitle("Laptop HP");
//        Category laptopMsi = categoryRepository.findByTitle("Laptop MSI");
//        Category laptopLg = categoryRepository.findByTitle("Laptop LG");
//        Category laptopAvita = categoryRepository.findByTitle("Laptop AVITA");
//
//        Category dienThoaiIphone = categoryRepository.findByTitle("Điện thoại Iphone");
//
//        Category cpuCategory = categoryRepository.findByTitle("Bộ vi xử lý - cpu".toUpperCase());
//
//        // crawl philong
//        System.out.println("start philong");
//        phiLongCrawler.listLaptopAsus().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, asusBrand, laptopAsus, phiLongretailer);
//        });
//        System.out.println("Done laptop asus");
//
//        phiLongCrawler.listLaptopAcer().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, acerBrand, laptopAcer, phiLongretailer);
//        });
//        System.out.println("Done laptop acer");
//
//
//        phiLongCrawler.listLaptopAvita().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, avitaBrand, laptopAvita, phiLongretailer);
//        });
//        System.out.println("Done laptop avita");
//
//
//        phiLongCrawler.listLaptopLg().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lgBrand, laptopLg, phiLongretailer);
//        });
//        System.out.println("Done laptop lg");
//
//        phiLongCrawler.listLaptopHp().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, hpBrand, laptopHp, phiLongretailer);
//        });
//        System.out.println("Done laptop hp");
//
//        phiLongCrawler.listLaptopMsi().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, msiBrand, laptopMsi, phiLongretailer);
//        });
//        System.out.println("Done laptop msi");
//
//        phiLongCrawler.listLaptopLenovo().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lenovoBrand, laptopLenovo, phiLongretailer);
//        });
//        System.out.println("Done laptop lenovo");
//
//        phiLongCrawler.listLaptopDell().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, dellBrand, laptopDell, phiLongretailer);
//        });
//        System.out.println("Done laptop asus");
//
//        phiLongCrawler.listCpuIntel().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, intelBrand, cpuCategory, phiLongretailer);
//        });
//        System.out.println("Done cpu intel");
//
//        phiLongCrawler.listCpuAmd().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, amdBrand, cpuCategory, phiLongretailer);
//        });
//        System.out.println("Done cpu amd");
//
//        System.out.println("end philong");
//
//
//        // crawl xuan vinh
//        System.out.println("start xuanvinh");
//        xuanVinhCrawler.listLaptopAsus().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, asusBrand, laptopAsus, xuanVinhRetailer);
//        });
//        System.out.println("done laptop asus");
//
//        xuanVinhCrawler.listLaptopAcer().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, acerBrand, laptopAcer, xuanVinhRetailer);
//        });
//        System.out.println("done laptop acer");
//
//        xuanVinhCrawler.listLaptopAvita().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, avitaBrand, laptopAvita, xuanVinhRetailer);
//        });
//        System.out.println("done laptop avita");
//
//        xuanVinhCrawler.listLaptopLg().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lgBrand, laptopLg, xuanVinhRetailer);
//        });
//        System.out.println("done laptop lg");
//
//        xuanVinhCrawler.listLaptopHp().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, hpBrand, laptopHp, xuanVinhRetailer);
//        });
//        System.out.println("done laptop hp");
//
//        xuanVinhCrawler.listLaptopMsi().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, msiBrand, laptopMsi, xuanVinhRetailer);
//        });
//        System.out.println("done laptop msi");
//
//        xuanVinhCrawler.listLaptopLenovo().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lenovoBrand, laptopLenovo, xuanVinhRetailer);
//        });
//        System.out.println("done laptop lenovo");
//
//        xuanVinhCrawler.listLaptopDell().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, dellBrand, laptopDell, xuanVinhRetailer);
//        });
//        System.out.println("done laptop dell");
//
//        xuanVinhCrawler.listCpuIntel().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, intelBrand, cpuCategory, xuanVinhRetailer);
//        });
//        System.out.println("done cpu intel");
//
//        xuanVinhCrawler.listCpuAmd().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, amdBrand, cpuCategory, xuanVinhRetailer);
//        });
//        System.out.println("done cpu amd");
//
//        System.out.println("end xuanvinh");
//
//        // crawl shopdunk
//        System.out.println("start shopdunk");
//        shopDunkCrawler.listIphone().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, appleBrand, dienThoaiIphone, shupDunkRetailer);
//
//        });
//        System.out.println("done iphone");
//        System.out.println("end shopdunk");
//
//
//        // crawl hanoicomputer
//        System.out.println("start hanoi computer");
//        haNoiComputerCrawler.listLaptopAsus().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, asusBrand, laptopAsus, haNoiComputerRetailer);
//        });
//        System.out.println("done laptop asus");
//
//        haNoiComputerCrawler.listLaptopAcer().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, acerBrand, laptopAcer, haNoiComputerRetailer);
//        });
//        System.out.println("done laptop acer");
//
//
//        haNoiComputerCrawler.listLaptopAvita().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, avitaBrand, laptopAvita, haNoiComputerRetailer);
//        });
//        System.out.println("done laptop avita");
//
//        haNoiComputerCrawler.listLaptopLg().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lgBrand, laptopLg, haNoiComputerRetailer);
//        });
//        System.out.println("done laptop lg");
//
//        haNoiComputerCrawler.listLaptopHp().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, hpBrand, laptopHp, haNoiComputerRetailer);
//        });
//        System.out.println("done laptop hp");
//
//        haNoiComputerCrawler.listLaptopMsi().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, msiBrand, laptopMsi, haNoiComputerRetailer);
//        });
//        System.out.println("done laptop msi");
//
//        haNoiComputerCrawler.listLaptopLenovo().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lenovoBrand, laptopLenovo, haNoiComputerRetailer);
//        });
//        System.out.println("done laptop lenovo");
//
//        haNoiComputerCrawler.listLaptopDell().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, dellBrand, laptopDell, haNoiComputerRetailer);
//        });
//        System.out.println("done laptop dell");
//        System.out.println("end hanoi computer");
//
//
//        // crawl gearvn
//        System.out.println("start gearvn");
//        gearVNCrawler.listLaptopAsus().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, asusBrand, laptopAsus, gearVNRetailer);
//        });
//        System.out.println("done laptop asus");
//
//        gearVNCrawler.listLaptopAcer().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, acerBrand, laptopAcer, gearVNRetailer);
//        });
//        System.out.println("done laptop acer");
//
//        gearVNCrawler.listLaptopLg().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lgBrand, laptopLg, gearVNRetailer);
//        });
//        System.out.println("done laptop lg");
//
//        gearVNCrawler.listLaptopHp().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, hpBrand, laptopHp, gearVNRetailer);
//        });
//        System.out.println("done laptop hp");
//
//        gearVNCrawler.listLaptopMsi().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, msiBrand, laptopMsi, gearVNRetailer);
//        });
//        System.out.println("done laptop msi");
//
//        gearVNCrawler.listLaptopLenovo().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lenovoBrand, laptopLenovo, gearVNRetailer);
//        });
//        System.out.println("done laptop lenovo");
//
//        gearVNCrawler.listLaptopDell().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, dellBrand, laptopDell, gearVNRetailer);
//        });
//        System.out.println("done laptop dell");
//
//        System.out.println("end gearvn");
//
//
//        // crawl anphatPC
//        System.out.println("start anphat pc");
//        anPhatPCCrawler.listLaptopAsus().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, asusBrand, laptopAsus, anPhatPCRetailer);
//        });
//        System.out.println("done laptop asus");
//
//        anPhatPCCrawler.listLaptopAcer().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, acerBrand, laptopAcer, anPhatPCRetailer);
//        });
//        System.out.println("done laptop acer");
//
//        anPhatPCCrawler.listLaptopAvita().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, avitaBrand, laptopAvita, anPhatPCRetailer);
//        });
//        System.out.println("done laptop avita");
//
//        anPhatPCCrawler.listLaptopLg().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lgBrand, laptopLg, anPhatPCRetailer);
//        });
//        System.out.println("done laptop lg");
//
//        anPhatPCCrawler.listLaptopHp().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, hpBrand, laptopHp, anPhatPCRetailer);
//        });
//        System.out.println("done laptop hp");
//
//        anPhatPCCrawler.listLaptopMsi().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, msiBrand, laptopMsi, anPhatPCRetailer);
//        });
//        System.out.println("done laptop msi");
//
//        anPhatPCCrawler.listLaptopLenovo().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, lenovoBrand, laptopLenovo, anPhatPCRetailer);
//        });
//        System.out.println("done laptop lenovo");
//
//        anPhatPCCrawler.listLaptopDell().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, dellBrand, laptopDell, anPhatPCRetailer);
//        });
//        System.out.println("done laptop dell");
//        System.out.println("end anphat pc");
//
//        // crawl didongviet
//        System.out.println("start didongviet");
//        diDongVietCrawler.listIphone().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, appleBrand, dienThoaiIphone, diDongVietRetailer);
//        });
//        System.out.println("done iphone");
//        System.out.println("end didongviet");
//
//
//        // crawl iphonedanang
//        System.out.println("start iphonedanang");
//        appleTAndTCrawler.listIphone().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, appleBrand, dienThoaiIphone, iphoneDaNangRetailer);
//        });
//        System.out.println("done iphone");
//        System.out.println("end iphonedanang");
//
//        // crawl anhducdigital
//        System.out.println("start anhducdigital");
//        anhDucDigitalCrawler.listIphone().forEach(crawlerModel -> {
//            initProductV3(crawlerModel, appleBrand, dienThoaiIphone, anhDucDigitalRetailer);
//        });
//        System.out.println("done iphone");
//        System.out.println("end anhducdigital");


//        System.out.println("Bắt đầu lưu sản phẩm");
//        initProductV2();

//        initProduct();
//        initManyProductRetailer();
//
//        System.out.println("start rating");
//        initRating();
//        System.out.println("end rating");
//
//        System.out.println("start access");
//        initSoluottruycap();
//        System.out.println("end access");

//        System.out.println("start viewcount");
//        initViewCount();
//        System.out.println("end viewcount");
//
//        System.out.println("start search");
//        initSoluotTimKiem();
//        System.out.println("end search");
//
//        System.out.println("start banner");
//        initBanner();
//        System.out.println("end banner");
    }

    private void initProductV3(CrawlerModel crawlerModel, Brand brand, Category category, Retailer retailer) {
        Product product = productRepository.findByCode(crawlerModel.getCode().toLowerCase().trim());

        if (product == null) {
            // thêm mới product
            product = new Product();
            product.setTitle(crawlerModel.getTitle());
            product.setLongDescription(crawlerModel.getLongDesc());
            product.setShortDescription(crawlerModel.getShortDesc());
            product.setCode(crawlerModel.getCode().toLowerCase().trim());
            product.setCategory(category);
            product.setBrand(brand);

            productRepository.save(product);

            // save image
            if (crawlerModel.getImages().size() > 0) {
                for (String image : crawlerModel.getImages()) {
                    imageRepository.save(new Image(image, product));
                }

            }

            // save product_retailer
            ProductRetailer productRetailer = new ProductRetailer(crawlerModel.getUrl(), retailer, product, true, true);
            productRetailerRepository.save(productRetailer);

            // save price
            Price price = new Price(crawlerModel.getPrice(), productRetailer);
            priceRepository.save(price);
        } else {
            // tạo mới giá
            ProductRetailer productRetailer = new ProductRetailer(crawlerModel.getUrl(), retailer, product, true, true);
            productRetailerRepository.save(productRetailer);
            priceRepository.save(new Price(crawlerModel.getPrice(), productRetailer));

            // kiểm tra xem sản phẩm hiện tại có bao nhiêu hình ảnh
            // nếu <=3 sản phẩm lưu thêm hình ảnh cho sản phẩm

            if (imageRepository.findByProductAndDeleteFlgFalse(product).size() <= 3 && crawlerModel.getImages().size() > 0) {

                for (String image : crawlerModel.getImages()) {
                    imageRepository.save(new Image(image, product));
                }

            }
        }
    }


    private void initBanner() {
        bannerService.createNew(
                new BannerRequest(
                        "So sánh ngay",
                        "So sánh sản phẩm với đánh giá tốt nhất từ các gian hàng",
                        "https://demo-tbp.herokuapp.com/img/slider2.5a899920.png",
                        "https://demo-tbp.herokuapp.com/"
                )
        );

        bannerService.createNew(
                new BannerRequest(
                        "Nhãn hàng apple",
                        "Mẫu điện thoại apple được yêu thích nhất",
                        "https://shopdunk.com/wp-content/uploads/2021/05/iphone-12-pro-max.jpg",
                        "https://shopdunk.com/"
                )
        );

        bannerService.createNew(
                new BannerRequest(
                        "Nhãn hàng asus",
                        "Chương trình khuyến mãi asus",
                        "https://philong.com.vn/media/product/24408-04_scar_15_l.jpg",
                        "https://philong.com.vn/"
                )
        );

        bannerService.createNew(
                new BannerRequest(
                        "Màn hình LG siêu mỏng",
                        "Trải nghiệm công nghệ màn hình tràn viền",
                        "https://philong.com.vn/media/product/22428-34wk95c-new.png",
                        "https://philong.com.vn/"
                )
        );

        bannerService.createNew(
                new BannerRequest(
                        "Hiệu suất vượt trội với i7",
                        "Hoạt động vượt trội với CPU core i7",
                        "https://philong.com.vn/media/product/20637-9700-1.jpg",
                        "https://philong.com.vn/"
                )
        );
    }

    //    @PostConstruct
    private void initRating() {
        for (int i = 1; i <= productRepository.count(); i++) {
            for (int j = 1; j <= random(2, 6); j++)
                rateService.rating((long) random(3, 13), (long) random(1, 5), (long) i);
        }
    }

    //    @PostConstruct
    @Transactional
    public void initSoluotTimKiem() {
        Search search1 = new Search("Máy tính asus s430", 600L);
        Search search2 = new Search("iphone x", 650L);
        Search search3 = new Search("iphone xr", 550L);
        Search search4 = new Search("iphone 8 plus", 640L);
        Search search5 = new Search("laptop envy", 400L);
        Search search6 = new Search("tai nghe hay", 450L);
        Search search7 = new Search("laptop gaming i9", 470L);
        Search search8 = new Search("loa jbl", 465L);
        Search search9 = new Search("laptop gram", 511L);
        Search search10 = new Search("diện thoại iphone", 520L);

        searchRepository.save(search1);
        searchRepository.save(search2);
        searchRepository.save(search3);
        searchRepository.save(search4);
        searchRepository.save(search5);
        searchRepository.save(search6);
        searchRepository.save(search7);
        searchRepository.save(search8);
        searchRepository.save(search9);
        searchRepository.save(search10);


        LocalDate startDay = LocalDate.of(2021, 1, 1);
        LocalDate endDay = LocalDate.of(2021, 7, 1);

        Date date = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());

        long getDiff = date2.getTime() - date.getTime();

        long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);
        int min = 10;
        int max = 12;

        for (int i = 0; i <= getDaysDiff; i++) {
            searchStatisticRepository.save(new SearchStatistic(null, search1, (long) random(min, max), startDay.plusDays(i)));
            searchStatisticRepository.save(new SearchStatistic(null, search2, (long) random(min, max), startDay.plusDays(i)));
            searchStatisticRepository.save(new SearchStatistic(null, search3, (long) random(min, max), startDay.plusDays(i)));
            searchStatisticRepository.save(new SearchStatistic(null, search4, (long) random(min, max), startDay.plusDays(i)));
            searchStatisticRepository.save(new SearchStatistic(null, search5, (long) random(min, max), startDay.plusDays(i)));
            searchStatisticRepository.save(new SearchStatistic(null, search6, (long) random(min, max), startDay.plusDays(i)));
            searchStatisticRepository.save(new SearchStatistic(null, search7, (long) random(min, max), startDay.plusDays(i)));
            searchStatisticRepository.save(new SearchStatistic(null, search8, (long) random(min, max), startDay.plusDays(i)));
            searchStatisticRepository.save(new SearchStatistic(null, search9, (long) random(min, max), startDay.plusDays(i)));
            searchStatisticRepository.save(new SearchStatistic(null, search10, (long) random(min, max), startDay.plusDays(i)));
        }

        search1.setNumberOfSearch(searchStatisticRepository.countBySearch(search1));
        search2.setNumberOfSearch(searchStatisticRepository.countBySearch(search2));
        search3.setNumberOfSearch(searchStatisticRepository.countBySearch(search3));
        search4.setNumberOfSearch(searchStatisticRepository.countBySearch(search4));
        search5.setNumberOfSearch(searchStatisticRepository.countBySearch(search5));
        search6.setNumberOfSearch(searchStatisticRepository.countBySearch(search6));
        search7.setNumberOfSearch(searchStatisticRepository.countBySearch(search7));
        search8.setNumberOfSearch(searchStatisticRepository.countBySearch(search8));
        search9.setNumberOfSearch(searchStatisticRepository.countBySearch(search9));
        search10.setNumberOfSearch(searchStatisticRepository.countBySearch(search10));


        searchRepository.save(search1);
        searchRepository.save(search2);
        searchRepository.save(search3);
        searchRepository.save(search4);
        searchRepository.save(search5);
        searchRepository.save(search6);
        searchRepository.save(search7);
        searchRepository.save(search8);
        searchRepository.save(search9);
        searchRepository.save(search10);

    }


    private int random(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    //    @PostConstruct
    @Transactional
    public void initViewCount() {
        LocalDate startDay = LocalDate.of(2021, 1, 1);
        LocalDate endDay = LocalDate.of(2021, 7, 1);
        Date date = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());

        long getDiff = date2.getTime() - date.getTime();

        long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);

        int min = 50;
        int max = 60;

        for (int i = 0; i <= getDaysDiff; i++) {
            // random viewcount dien thoai
            viewCountStatisticRepository.save(new ViewCountStatistic(
                    productRepository.getOne((long) random(1, (int) productRepository.count())),
                    startDay.plusDays(i),
                    (long) random(min, max)
            ));
        }

        // set viewcount for product

        for (int i = 1; i <= productRepository.count(); i++) {
            Long viewCount = viewCountStatisticRepository.countByProduct((long) i);
            if (viewCount != null) {
                Product product = productRepository.getOne((long) i);
                product.setViewCount(viewCount);
                productRepository.save(product);
            }
        }


    }

    //    @PostConstruct
    public void initSoluottruycap() {
        LocalDate startDay = LocalDate.of(2021, 1, 1);
        LocalDate endDay = LocalDate.of(2021, 7, 1);
        Date date = Date.from(startDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(endDay.atStartOfDay(ZoneId.systemDefault()).toInstant());

        long getDiff = date2.getTime() - date.getTime();

        long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);
        int min = 45;
        int max = 60;

        for (int i = 0; i <= getDaysDiff; i++) {
            statisticAccessRepository.save(new StatisticAccess(
                    null,
                    startDay.plusDays(i),
                    true,
                    (long) random(min, max)
            ));

            statisticAccessRepository.save(new StatisticAccess(
                    null,
                    startDay.plusDays(i),
                    false,
                    (long) random(min, max)
            ));
        }
    }

    private void saveProductV2(Set<ProductCrawler> productCrawlers, Category category, Brand brand, Retailer retailer) {
        // danh muc laptop
        productCrawlers.forEach(productCrawler -> {
            System.out.println("Đang lưu " + productCrawler.getUrl());
            Product product = productCrawler.getProduct();
            product.setCategory(category);
            product.setBrand(brand);
            product.setApprove(true);


            // save product retailer
            ProductRetailer productRetailer = new ProductRetailer();
            productRetailer.setUrl(productCrawler.getUrl());
            productRetailer.setRetailer(retailer);
            productRetailer.setProduct(product);
            productRetailerRepository.save(productRetailer);

            // save price
            priceRepository.save(new Price(productCrawler.getPrice(), productRetailer));

            // save Images
            productCrawler.getImages().forEach(s -> imageRepository.save(new Image(s, product)));
        });
    }

    private Long randomRate() {
        int random = new Random().nextInt(6);
        return (long) random;
    }

    private void initProductV2() {
        Brand asusBrand = brandRepository.findByName("ASUS");
        Brand acerBrand = brandRepository.findByName("ACER");
        Brand dellBrand = brandRepository.findByName("DELL");
        Brand lenovoBrand = brandRepository.findByName("LENOVO");
        Brand hpBrand = brandRepository.findByName("HP");
        Brand msiBrand = brandRepository.findByName("MSI");
        Brand lgBrand = brandRepository.findByName("LG");
        Brand microsoftBrand = brandRepository.findByName("MICROSOFT");
        Brand avitaBrand = brandRepository.findByName("AVITA");

        Brand aocBrand = brandRepository.findByName("AOC");
        Brand benqBrand = brandRepository.findByName("BENQ");
        Brand gigabyteBrand = brandRepository.findByName("GIGABYTE");
        Brand hkcBrand = brandRepository.findByName("HKC");
        Brand infinityBrand = brandRepository.findByName("INFINITY");
        Brand philipsBrand = brandRepository.findByName("PHILIPS");
        Brand samsungBrand = brandRepository.findByName("SAMSUNG");
        Brand viewsonicBrand = brandRepository.findByName("VIEWSONIC");
        Brand corsairBrand = brandRepository.findByName("CORSAIR");

        Brand galaxBrand = brandRepository.findByName("GALAX");
        Brand geilBrand = brandRepository.findByName("GEIL");
        Brand gskillBrand = brandRepository.findByName("GSKILL");
        Brand kingmaxBrand = brandRepository.findByName("KINGMAX");
        Brand kingstonBrand = brandRepository.findByName("KINGSTON");
        Brand patriotBrand = brandRepository.findByName("PATRIOT");

        Brand seagateBrand = brandRepository.findByName("SEAGATE");
        Brand toshibaBrand = brandRepository.findByName("TOSHIBA");
        Brand wdBrand = brandRepository.findByName("WD");
        Brand plextorBrand = brandRepository.findByName("PLEXTOR");
        Brand jblBrand = brandRepository.findByName("JBL");
        Brand darueBrand = brandRepository.findByName("DAREU");
        Brand logitechBrand = brandRepository.findByName("LOGITECH");
        Brand appleBrand = brandRepository.findByName("APPLE");


        Brand amdBrand = brandRepository.findByName("AMD");
        Brand intelBrand = brandRepository.findByName("INTEL");

        Category laptopAsus = categoryRepository.findByTitle("Laptop ASUS");
        Category laptopAcer = categoryRepository.findByTitle("Laptop ACER");
        Category laptopDell = categoryRepository.findByTitle("Laptop DELL");
        Category laptopLenovo = categoryRepository.findByTitle("Laptop LENOVO");
        Category laptopHp = categoryRepository.findByTitle("Laptop HP");
        Category laptopMsi = categoryRepository.findByTitle("Laptop MSI");
        Category laptopLg = categoryRepository.findByTitle("Laptop LG");
        Category laptopMicrosoft = categoryRepository.findByTitle("Laptop MICROSOFT");
        Category laptopAvita = categoryRepository.findByTitle("Laptop AVITA");


        Category manHinhAcer = categoryRepository.findByTitle("Màn hình ACER");
        Category manHinhAoc = categoryRepository.findByTitle("Màn hình AOC");
        Category manHinhAsus = categoryRepository.findByTitle("Màn hình ASUS");
        Category manHinhDell = categoryRepository.findByTitle("Màn hình DELL");
        Category manHinhGigabyte = categoryRepository.findByTitle("Màn hình GIGABYTE");
        Category manHinhHKC = categoryRepository.findByTitle("Màn hình HKC");
        Category manHinhHP = categoryRepository.findByTitle("Màn hình HP");
        Category manHinhInfinity = categoryRepository.findByTitle("Màn hình INFINITY");
        Category manHinhLenovo = categoryRepository.findByTitle("Màn hình LENOVO");
        Category manHinhLg = categoryRepository.findByTitle("Màn hình LG");
        Category manHinhMsi = categoryRepository.findByTitle("Màn hình MSI");
        Category manHinhPhilips = categoryRepository.findByTitle("Màn hình PHILIPS");
        Category manHinhSamsung = categoryRepository.findByTitle("Màn hình SAMSUNG");
        Category manHinhViewSonic = categoryRepository.findByTitle("Màn hình VIEWSONIC");

        Category cpuCategory = categoryRepository.findByTitle("Bộ vi xử lý - cpu".toUpperCase());
        Category mainboardCategory = categoryRepository.findByTitle("Bo mạch chủ - mainboard".toUpperCase());
        Category ramCategory = categoryRepository.findByTitle("Bộ nhớ trong - ram".toUpperCase());
        Category hddCategory = categoryRepository.findByTitle("ổ đĩa hdd-dvd".toUpperCase());
        Category ssdCategory = categoryRepository.findByTitle("ổ cứng ssd".toUpperCase());
        Category vgaCategory = categoryRepository.findByTitle("card đồ họa-vga".toUpperCase());
        Category psuCategory = categoryRepository.findByTitle("nguồn máy tính psu".toUpperCase());
        Category caseCategory = categoryRepository.findByTitle("vỏ máy tính - case".toUpperCase());

        Category taiNgheApple = categoryRepository.findByTitle("Tai nghe APPLE");
        Category taiNgheCorsair = categoryRepository.findByTitle("Tai nghe CORSAIR");
        Category taiNgheDarue = categoryRepository.findByTitle("Tai nghe DAREU");
        Category taiNgheJbl = categoryRepository.findByTitle("Tai nghe JBL");
        Category taiNgheLg = categoryRepository.findByTitle("Tai nghe LG");
        Category taiNgheLogitech = categoryRepository.findByTitle("Tai nghe LOGITECH");

        Category appleCategory = categoryRepository.findByTitle("Điện thoại Iphone");


        Retailer phiLongretailer = retailerRepository.findByHomePage("https://philong.com.vn/");
        Retailer shopDunkRetailer = retailerRepository.findByHomePage("https://shopdunk.com/");

// list dien thoai
        try {
            Set<ProductCrawler> listDienThoai11 = shopDunkCrawler.getListIphoneByUrl(SHUPDUNK_IPHONE11);
            saveProductV2(listDienThoai11, appleCategory, appleBrand, shopDunkRetailer);

            Set<ProductCrawler> listDienThoai12 = shopDunkCrawler.getListIphoneByUrl(SHUPDUNK_IPHONE12);
            saveProductV2(listDienThoai12, appleCategory, appleBrand, shopDunkRetailer);

            Set<ProductCrawler> listDienThoaiSE = shopDunkCrawler.getListIphoneByUrl(SHUPDUNK_IPHONESE);
            saveProductV2(listDienThoaiSE, appleCategory, appleBrand, shopDunkRetailer);

            Set<ProductCrawler> listDienThoaiXr = shopDunkCrawler.getListIphoneByUrl(SHUPDUNK_IPHONEXr);
            saveProductV2(listDienThoaiXr, appleCategory, appleBrand, shopDunkRetailer);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
        // tai nghe
        try {
            Set<ProductCrawler> listTaiNgheApple = phiLongCrawler.getProductByURL(PHILONG_TAINGHE_APPLE);
            saveProductV2(listTaiNgheApple, taiNgheApple, appleBrand, phiLongretailer);
            Set<ProductCrawler> listTaiNgheCorsair = phiLongCrawler.getProductByURL(PHILONG_TAINGHE_CORSAIR);
            saveProductV2(listTaiNgheCorsair, taiNgheCorsair, corsairBrand, phiLongretailer);

            Set<ProductCrawler> listTaiNgheDarue = phiLongCrawler.getProductByURL(PHILONG_TAINGHE_DARUE);
            saveProductV2(listTaiNgheDarue, taiNgheDarue, darueBrand, phiLongretailer);

            Set<ProductCrawler> listTaiNgheJbl = phiLongCrawler.getProductByURL(PHILONG_TAINGHE_JBL);
            saveProductV2(listTaiNgheJbl, taiNgheJbl, jblBrand, phiLongretailer);

            Set<ProductCrawler> listTaiNgheLg = phiLongCrawler.getProductByURL(PHILONG_TAINGHE_LG);
            saveProductV2(listTaiNgheLg, taiNgheLg, lgBrand, phiLongretailer);

            Set<ProductCrawler> listTaiNgheLogitech = phiLongCrawler.getProductByURL(PHILONG_TAINGHE_LOGITECH);
            saveProductV2(listTaiNgheLogitech, taiNgheLogitech, logitechBrand, phiLongretailer);
        } catch (IOException e) {
            e.printStackTrace();
        }

//
        // linh kien may tinh - vga
        try {
            Set<ProductCrawler> listVgaAsus = phiLongCrawler.getProductByURL(PHILONG_VGA_ASUS);
            saveProductV2(listVgaAsus, vgaCategory, asusBrand, phiLongretailer);

            Set<ProductCrawler> listVgaGigabyte = phiLongCrawler.getProductByURL(PHILONG_VGA_GIGABYTE);
            saveProductV2(listVgaGigabyte, vgaCategory, gigabyteBrand, phiLongretailer);

            Set<ProductCrawler> listVgaMsi = phiLongCrawler.getProductByURL(PHILONG_VGA_MSI);
            saveProductV2(listVgaMsi, vgaCategory, msiBrand, phiLongretailer);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // linh kien may tinh - psu
        try {
            Set<ProductCrawler> listPsuAsus = phiLongCrawler.getProductByURL(PHILONG_PSU_ASUS);
            saveProductV2(listPsuAsus, psuCategory, asusBrand, phiLongretailer);
            Set<ProductCrawler> listPsuCorsair = phiLongCrawler.getProductByURL(PHILONG_PSU_CORSAIR);
            saveProductV2(listPsuCorsair, psuCategory, corsairBrand, phiLongretailer);

            Set<ProductCrawler> listPsuGigabyte = phiLongCrawler.getProductByURL(PHILONG_PSU_GIGABYTE);
            saveProductV2(listPsuGigabyte, psuCategory, gigabyteBrand, phiLongretailer);

            Set<ProductCrawler> listPsuMsi = phiLongCrawler.getProductByURL(PHILONG_PSU_MSI);
            saveProductV2(listPsuMsi, psuCategory, msiBrand, phiLongretailer);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        // linh kien may tinh - case

        try {
            Set<ProductCrawler> listCaseGigabyte = phiLongCrawler.getProductByURL(PHILONG_CASE_GIGABYTE);
            saveProductV2(listCaseGigabyte, caseCategory, gigabyteBrand, phiLongretailer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // linh kien may tinh - ssd
        try {
            Set<ProductCrawler> listSSDCorsair = phiLongCrawler.getProductByURL(PHILONG_SSD_CORSAIR);
            saveProductV2(listSSDCorsair, ssdCategory, corsairBrand, phiLongretailer);

            Set<ProductCrawler> listSSDGigabyte = phiLongCrawler.getProductByURL(PHILONG_SSD_GIGABYTE);
            saveProductV2(listSSDGigabyte, ssdCategory, gigabyteBrand, phiLongretailer);

            Set<ProductCrawler> listSSDIntel = phiLongCrawler.getProductByURL(PHILONG_SSD_INTEL);
            saveProductV2(listSSDIntel, ssdCategory, intelBrand, phiLongretailer);

            Set<ProductCrawler> listSSDKingmax = phiLongCrawler.getProductByURL(PHILONG_SSD_KINGMAX);
            saveProductV2(listSSDKingmax, ssdCategory, kingmaxBrand, phiLongretailer);

            Set<ProductCrawler> listSSDKingston = phiLongCrawler.getProductByURL(PHILONG_SSD_KINGSTON);
            saveProductV2(listSSDKingston, ssdCategory, kingstonBrand, phiLongretailer);

            Set<ProductCrawler> listSSDPlextor = phiLongCrawler.getProductByURL(PHILONG_SSD_PLEXTOR);
            saveProductV2(listSSDPlextor, ssdCategory, plextorBrand, phiLongretailer);

            Set<ProductCrawler> listSSDSamsung = phiLongCrawler.getProductByURL(PHILONG_SSD_SAMSUNG);
            saveProductV2(listSSDSamsung, ssdCategory, samsungBrand, phiLongretailer);

            Set<ProductCrawler> listSSDWd = phiLongCrawler.getProductByURL(PHILONG_SSD_WD);
            saveProductV2(listSSDWd, ssdCategory, wdBrand, phiLongretailer);


        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        // linh kien may tinh - hdd, dvd
        try {
            Set<ProductCrawler> listHddAsus = phiLongCrawler.getProductByURL(PHILONG_HDD_ASUS);
            saveProductV2(listHddAsus, hddCategory, asusBrand, phiLongretailer);
            Set<ProductCrawler> listHddSeagate = phiLongCrawler.getProductByURL(PHILONG_HDD_SEAGATE);
            saveProductV2(listHddSeagate, hddCategory, seagateBrand, phiLongretailer);

            Set<ProductCrawler> listHddToshiba = phiLongCrawler.getProductByURL(PHILONG_HDD_TOSHIBA);
            saveProductV2(listHddToshiba, hddCategory, toshibaBrand, phiLongretailer);

            Set<ProductCrawler> listHddWd = phiLongCrawler.getProductByURL(PHILONG_HDD_WD);
            saveProductV2(listHddWd, hddCategory, wdBrand, phiLongretailer);

        } catch (IOException e) {
            e.printStackTrace();
        }
//
//
//        // linh kien may tinh - ram
        try {
            Set<ProductCrawler> listRamCorsair = phiLongCrawler.getProductByURL(PHILONG_RAM_CORSAIR);
            saveProductV2(listRamCorsair, ramCategory, corsairBrand, phiLongretailer);

            Set<ProductCrawler> listRamGalax = phiLongCrawler.getProductByURL(PHILONG_RAM_GALAX);
            saveProductV2(listRamGalax, ramCategory, galaxBrand, phiLongretailer);

            Set<ProductCrawler> listRamGeil = phiLongCrawler.getProductByURL(PHILONG_RAM_GEIL);
            saveProductV2(listRamGeil, ramCategory, geilBrand, phiLongretailer);

            Set<ProductCrawler> listRamGSkill = phiLongCrawler.getProductByURL(PHILONG_RAM_GSKILL);
            saveProductV2(listRamGSkill, ramCategory, gskillBrand, phiLongretailer);

            Set<ProductCrawler> listRamKingmax = phiLongCrawler.getProductByURL(PHILONG_RAM_KINGMAX);
            saveProductV2(listRamKingmax, ramCategory, kingmaxBrand, phiLongretailer);

            Set<ProductCrawler> listRamKingston = phiLongCrawler.getProductByURL(PHILONG_RAM_KINGSTON);
            saveProductV2(listRamKingston, ramCategory, kingstonBrand, phiLongretailer);

            Set<ProductCrawler> listRamPatriot = phiLongCrawler.getProductByURL(PHILONG_RAM_PATRIOT);
            saveProductV2(listRamPatriot, ramCategory, patriotBrand, phiLongretailer);

            Set<ProductCrawler> listRamSamsung = phiLongCrawler.getProductByURL(PHILONG_RAM_SAMSUNG);
            saveProductV2(listRamSamsung, ramCategory, samsungBrand, phiLongretailer);

        } catch (IOException e) {
            e.printStackTrace();
        }
//
//
//        // linh kien may tinh - mainboard
        try {
            Set<ProductCrawler> mainboardAsus = phiLongCrawler.getProductByURL(PHILONG_MAINBOARD_ASUS);
            saveProductV2(mainboardAsus, mainboardCategory, asusBrand, phiLongretailer);

            Set<ProductCrawler> mainboardGigabyte = phiLongCrawler.getProductByURL(PHILONG_MAINBOARD_GIGABYTE);
            saveProductV2(mainboardGigabyte, mainboardCategory, gigabyteBrand, phiLongretailer);

            Set<ProductCrawler> mainboardIntel = phiLongCrawler.getProductByURL(PHILONG_MAINBOARD_INTEL);
            saveProductV2(mainboardIntel, mainboardCategory, intelBrand, phiLongretailer);

            Set<ProductCrawler> mainboardMsi = phiLongCrawler.getProductByURL(PHILONG_MAINBOARD_MSI);
            saveProductV2(mainboardMsi, mainboardCategory, msiBrand, phiLongretailer);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        // linh kien may tinh - cpu
        try {
            Set<ProductCrawler> listCPUIntel = phiLongCrawler.getProductByURL(PHILONG_CPU_INTEL);
            Set<ProductCrawler> listCPUAmd = phiLongCrawler.getProductByURL(PHILONG_CPU_AMD);

            saveProductV2(listCPUIntel, cpuCategory, intelBrand, phiLongretailer);
            saveProductV2(listCPUAmd, cpuCategory, amdBrand, phiLongretailer);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        // Man hinh
        try {
            Set<ProductCrawler> listManHinhAcer = phiLongCrawler.getProductByURL(PHILONG_MANHINH_ACER);
            saveProductV2(listManHinhAcer, manHinhAcer, acerBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhAoc = phiLongCrawler.getProductByURL(PHILONG_MANHINH_AOC);
            saveProductV2(listManHinhAoc, manHinhAoc, aocBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhAsus = phiLongCrawler.getProductByURL(PHILONG_MANHINH_ASUS);
            saveProductV2(listManHinhAsus, manHinhAsus, asusBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhDell = phiLongCrawler.getProductByURL(PHILONG_MANHINH_DELL);
            saveProductV2(listManHinhDell, manHinhDell, dellBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhGigabyte = phiLongCrawler.getProductByURL(PHILONG_MANHINH_GIGABYTE);
            saveProductV2(listManHinhGigabyte, manHinhGigabyte, gigabyteBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhHkc = phiLongCrawler.getProductByURL(PHILONG_MANHINH_HKC);
            saveProductV2(listManHinhHkc, manHinhHKC, hkcBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhHp = phiLongCrawler.getProductByURL(PHILONG_MANHINH_HP);
            saveProductV2(listManHinhHp, manHinhHP, hpBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhInfinity = phiLongCrawler.getProductByURL(PHILONG_MANHINH_INFINITY);
            saveProductV2(listManHinhInfinity, manHinhInfinity, infinityBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhLenovo = phiLongCrawler.getProductByURL(PHILONG_MANHINH_LENOVO);
            saveProductV2(listManHinhLenovo, manHinhLenovo, lenovoBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhLg = phiLongCrawler.getProductByURL(PHILONG_MANHINH_LG);
            saveProductV2(listManHinhLg, manHinhLg, lgBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhMsi = phiLongCrawler.getProductByURL(PHILONG_MANHINH_MSI);
            saveProductV2(listManHinhMsi, manHinhMsi, msiBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhPhilips = phiLongCrawler.getProductByURL(PHILONG_MANHINH_PHILIPS);
            saveProductV2(listManHinhPhilips, manHinhPhilips, philipsBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhSamsung = phiLongCrawler.getProductByURL(PHILONG_MANHINH_SAMSUNG);
            saveProductV2(listManHinhSamsung, manHinhSamsung, samsungBrand, phiLongretailer);

            Set<ProductCrawler> listManHinhViewSonic = phiLongCrawler.getProductByURL(PHILONG_MANHINH_VIEWSONIC);
            saveProductV2(listManHinhViewSonic, manHinhViewSonic, viewsonicBrand, phiLongretailer);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // laptop
        try {
            Set<ProductCrawler> listAcer = phiLongCrawler.getProductByURL(PHILONG_LAPTOP_ACER);
            listAcer = listAcer.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            saveProductV2(listAcer, laptopAcer, acerBrand, phiLongretailer);
        } catch (Exception ignored) {
            System.out.println("Xảy ra lỗi khi crawl laptop acer");
        }

        try {
            Set<ProductCrawler> listAsus = phiLongCrawler.getProductByURL(PHILONG_LAPTOP_ASUS);
            listAsus = listAsus.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            saveProductV2(listAsus, laptopAsus, asusBrand, phiLongretailer);
        } catch (Exception ignored) {
            System.out.println("Xảy ra lỗi khi crawl laptop asus");
        }
        try {
            Set<ProductCrawler> listDell = phiLongCrawler.getProductByURL(PHILONG_LAPTOP_DELL);
            listDell = listDell.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            saveProductV2(listDell, laptopDell, dellBrand, phiLongretailer);
        } catch (Exception ignored) {
            System.out.println("Xảy ra lỗi khi crawl laptop dell");
        }

        try {
            Set<ProductCrawler> listLenovo = phiLongCrawler.getProductByURL(PHILONG_LAPTOP_LENOVO);
            listLenovo = listLenovo.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            saveProductV2(listLenovo, laptopLenovo, lenovoBrand, phiLongretailer);
        } catch (Exception ignored) {
            System.out.println("Xảy ra lỗi khi crawl laptop lenovo");
        }

        try {
            Set<ProductCrawler> listHp = phiLongCrawler.getProductByURL(PHILONG_LAPTOP_HP);
            listHp = listHp.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            saveProductV2(listHp, laptopHp, hpBrand, phiLongretailer);
        } catch (Exception ignored) {
            System.out.println("Xảy ra lỗi khi crawl laptop HP");
        }
        try {
            Set<ProductCrawler> listMsi = phiLongCrawler.getProductByURL(PHILONG_LAPTOP_MSI);
            listMsi = listMsi.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            saveProductV2(listMsi, laptopMsi, msiBrand, phiLongretailer);
        } catch (Exception ignored) {
            System.out.println("Xảy ra lỗi khi crawl laptop msi");
        }
        try {
            Set<ProductCrawler> listLg = phiLongCrawler.getProductByURL(PHILONG_LAPTOP_LG);
            listLg = listLg.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            saveProductV2(listLg, laptopLg, lgBrand, phiLongretailer);
        } catch (Exception ignored) {
            System.out.println("Xảy ra lỗi khi crawl laptop lg");
        }
        try {
            Set<ProductCrawler> listMicrosoft = phiLongCrawler.getProductByURL(PHILONG_LAPTOP_MICROSOFT);
            listMicrosoft = listMicrosoft.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            saveProductV2(listMicrosoft, laptopMicrosoft, microsoftBrand, phiLongretailer);
        } catch (Exception ignored) {
            System.out.println("Xảy ra lỗi khi crawl laptop Microsoft");
        }
        try {
            Set<ProductCrawler> listAvita = phiLongCrawler.getProductByURL(PHILONG_LAPTOP_AVITA);
            listAvita = listAvita.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            saveProductV2(listAvita, laptopAvita, avitaBrand, phiLongretailer);
        } catch (Exception ignored) {
            System.out.println("Xảy ra lỗi khi crawl laptop avita");
        }
    }

    private void initProduct() {
        Category categoryLaptopAcer = categoryRepository.findByTitle("Laptop ACER");
        Brand brandAcer = brandRepository.findByName("ACER");
        Retailer phiLongRetailer = retailerRepository.findByHomePage("https://philong.com.vn/");
        saveProduct(
                "LAPTOP ACER SWIFT 3 SF314-42-R0TR (Ryzen 5-4500U, Ram 16GB, SSD 1TB, Màn hình 14\" Full HD, Win 10)",
                "CPU:&nbsp;AMD Ryzen 5-4500U \n" +
                        "RAM:&nbsp;&nbsp;16GB LPDDR4 3733MHz Onboard>\n" +
                        "Lưu trữ:&nbsp;1TB SSD M.2 PCIE Gen3x4>\n" +
                        "Đồ hoạ:&nbsp;AMD Radeon Graphics>\n" +
                        "Màn hình:&nbsp;14\" FHD (1920 x 1080) Acer ComfyView™ IPS LED LCD>\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Swift 3 SF314-42-R0TR NX.HSESV.002\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý AMD Ryzen™ 5-4500U\n" +
                        "Tốc độ 2.30GHz upto 4.00GHz, 6 cores 6 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 16GB (4GBx4) LPDDR4X\n" +
                        "Số khe cắm không nâng cấp được Ram\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 1TB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng Có (đã cắm sẵn 1TB SSD PCIe NVMe)\n" +
                        "Ổ đĩa quang (ODD)\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920 x 1080) 60Hz IPS Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình AMD Radeon™ Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX200\n" +
                        "LAN\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type-C port with DC-in 1 x USB 3.2 Gen 1 port with power-off charging 1 x USB 2.0 port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x Headset / Speaker jack\n" +
                        "Camera 720p HD audio\n" +
                        "Card mở rộng Không\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home + Office\n" +
                        "Kích thước (Dài x Rộng x Cao) 323.4 (W) x 218.9 (D) x 15.95 (H) mm\n" +
                        "Trọng Lượng 1.19kg\n" +
                        "Màu sắc Bạc\n" +
                        "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Swift 3 SF314-42-R0TR NX.HSESV.002\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý AMD Ryzen™ 5-4500U\n" +
                        "Tốc độ 2.30GHz upto 4.00GHz, 6 cores 6 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 16GB (4GBx4) LPDDR4X\n" +
                        "Số khe cắm không nâng cấp được Ram\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 1TB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng Có (đã cắm sẵn 1TB SSD PCIe NVMe)\n" +
                        "Ổ đĩa quang (ODD)\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920 x 1080) 60Hz IPS Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình AMD Radeon™ Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX200\n" +
                        "LAN\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type-C port with DC-in 1 x USB 3.2 Gen 1 port with power-off charging 1 x USB 2.0 port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x Headset / Speaker jack\n" +
                        "Camera 720p HD audio\n" +
                        "Card mở rộng Không\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home + Office\n" +
                        "Kích thước (Dài x Rộng x Cao) 323.4 (W) x 218.9 (D) x 15.95 (H) mm\n" +
                        "Trọng Lượng 1.19kg\n" +
                        "Màu sắc Bạc",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/120-24222-3.jpg",
                        "https://philong.com.vn/media/product/120-24222-2.jpg",
                        "https://philong.com.vn/media/product/120-24222-1.jpg",
                        "https://philong.com.vn/media/product/120-24222-4.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-swift-3-sf314-42-r0tr-ryzen-5-4500u-ram-16gb-ssd-1tb-man-hinh-14quot-full-hd-win-10.html",
                21990000L
        );

        saveProduct(
                "LAPTOP ACER NITRO 5 AN515-55-5923 (Core i5-10300H, Ram 8G, SSD 512GB, Màn hình 15.6 FHD 144Hz, VGA GTX1650Ti, WIN 0 Home 64)\n",
                "CPU: Intel Core i5-10300H\n" +
                        "RAM: 8GB DDR4 2933MHz\n" +
                        "Ổ cứng: 512GB PCIe NVMe SSD\n" +
                        "VGA: NVIDIA GeForce GTX 1650Ti 4GB GDDR6\n" +
                        "Màn hình: 15.6” FHD(1920 x 1080) IPS 144Hz\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel Core i5-10300H\n" +
                        "Tốc độ 2.50 GHz upto 4.50 GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2933MHz\n" +
                        "Số khe cắm 2 khe, tối đa 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa thêm HDD: 2TB, SSD: 2 khe M.2, mỗi khe 1TB SSD NVMe PCIe - RAI0\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz slim bezel LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 10/100/1000 Mbps\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 Gen 2 2 x USB 3.2 Gen 1 1 x USB Type C\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 57.5 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 23.9 (H) mm\n" +
                        "Trọng Lượng 2.3 kg\n" +
                        "Màu sắc Đen\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel Core i5-10300H\n" +
                        "Tốc độ 2.50 GHz upto 4.50 GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2933MHz\n" +
                        "Số khe cắm 2 khe, tối đa 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa thêm HDD: 2TB, SSD: 2 khe M.2, mỗi khe 1TB SSD NVMe PCIe - RAI0\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz slim bezel LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 10/100/1000 Mbps\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 Gen 2 2 x USB 3.2 Gen 1 1 x USB Type C\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 57.5 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 23.9 (H) mm\n" +
                        "Trọng Lượng 2.3 kg\n" +
                        "Màu sắc Đen",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/23178-an515-55-2.jpg",
                        "https://philong.com.vn/media/product/23178-acer-nitro-an515-55-5923-i5-nhq7nsv004-141520-111501-600x600.jpg",
                        "https://philong.com.vn/media/product/23178-an515-55-8.jpg",
                        "https://philong.com.vn/media/product/23178-an515-55-1.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-nitro-an515-55-5923-core-i5-10300hram-8gssd-512gman-hinh-15.6-fhdvga-4gb1650tiwin-10.html",
                20090000L
        );

        saveProduct(
                "Laptop Acer Aspire 3 A315 56 502X (i5-1035G1, Ram 4GB, SSD 256GB, Màn hình 15.6\" FHD, Win 10",
                "CPU:&nbsp;Intel Core i5-1035G1>\n" +
                        "RAM:&nbsp;&nbsp;4GB DDR4 2400Mhz onboard>\n" +
                        "Lưu trữ:&nbsp;256GB PCIe NVMe SSD>\n" +
                        "Đồ hoạ: Intel UHD Graphics>\n" +
                        "Màn hình:&nbsp;15.6\" FHD (1920 x 1080) IPS\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Aspire 3 A315-56-502X NX.HS5SV.00F\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1035G1\n" +
                        "Tốc độ 1.00GHz upto 3.60GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 6MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 4GB DDR4 2400Mhz onboard\n" +
                        "Số khe cắm 1 khe, tối đa 12GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng Có (nâng cấp tối đa 1TB PCIe NVMe Gen3 8 Gb/s)\n" +
                        "Ổ đĩa quang (ODD) Không\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080), IPS, 60Hz, Acer ComfyView LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® UHD Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless-AC 9461/9462\n" +
                        "LAN Gigabit Ethernet, Wake-on-LAN ready\n" +
                        "Bluetooth Bluetooth® 4.2 1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn - Có bàn phím số\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.1 Gen1 port 2 x USB 2.0 ports\n" +
                        "Kết nối HDMI/VGA 1 x HDMI®port with HDCP support\n" +
                        "Tai nghe 1 x 3.5 mm headphone/speaker jack\n" +
                        "Camera Acer webcam with: 640 x 480 resolution\n" +
                        "Card mở rộng\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 2-cell, 36.7Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 247.5 (D) x 19.9 (H) mm\n" +
                        "Trọng Lượng 1.7kg\n" +
                        "Màu sắc Đen\n" +
                        "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Aspire 3 A315-56-502X NX.HS5SV.00F\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1035G1\n" +
                        "Tốc độ 1.00GHz upto 3.60GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 6MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 4GB DDR4 2400Mhz onboard\n" +
                        "Số khe cắm 1 khe, tối đa 12GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng Có (nâng cấp tối đa 1TB PCIe NVMe Gen3 8 Gb/s)\n" +
                        "Ổ đĩa quang (ODD) Không\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080), IPS, 60Hz, Acer ComfyView LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® UHD Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless-AC 9461/9462\n" +
                        "LAN Gigabit Ethernet, Wake-on-LAN ready\n" +
                        "Bluetooth Bluetooth® 4.2 1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn - Có bàn phím số\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.1 Gen1 port 2 x USB 2.0 ports\n" +
                        "Kết nối HDMI/VGA 1 x HDMI®port with HDCP support\n" +
                        "Tai nghe 1 x 3.5 mm headphone/speaker jack\n" +
                        "Camera Acer webcam with: 640 x 480 resolution\n" +
                        "Card mở rộng\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 2-cell, 36.7Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 247.5 (D) x 19.9 (H) mm\n" +
                        "Trọng Lượng 1.7kg\n" +
                        "Màu sắc Đen",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/24207-2.jpg",
                        "https://philong.com.vn/media/product/24207-1.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-3-a315-56-502x-i5-1035g1-ram-4gb-ssd-256gb-man-hinh-15.6quot-fhd-win-10.html",
                12990000L
        );

        saveProduct(
                "Laptop Acer Aspire 7 A715 42G R4ST (Ryzen 5-5500U, Ram 8GB, SSD 256GB, VGA GTX1650 4GB, Màn hình 15.6\" FHD, Win 10)\n" +
                        "elementsShort = CPU: AMD Ryzen 5-5500U",
                "8GB DDR4 3200Mhz &nbsp;\n" +
                        "Ổ cứng: 256GB PCIe NVMe SSD VGA\n" +
                        "Card đồ họa: NVIDIA GTX 1650 4GB\n" +
                        "Màn hình: 15.6 inch FHD (1920 x 1080), 60Hz\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "CPU AMD Ryzen 5 – 5500U\n" +
                        "RAM 8GB DDR4 (2x SO-DIMM socket, up to 32GB SDRAM)\n" +
                        "Ổ cứng 256GB PCIe® NVMe™ M.2 SSD\n" +
                        "Card đồ họa NVIDIA GeForce GTX 1650 4GB GDDR6\n" +
                        "Màn hình 15.6\" FHD (1920 x 1080) IPS, Anti-Glare, 60Hz\n" +
                        "Cổng giao tiếp 1x USB 3.0 1x USB Type C 2x USB 2.0 1x HDMI 1x RJ45\n" +
                        "Ổ quang None\n" +
                        "Audio True Harmony; Dolby® Audio Premium\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN 10/100/1000/Gigabits Base T\n" +
                        "Chuẩn WIFI Wi-Fi 6(Gig+)(802.11ax) (2x2)\n" +
                        "Bluetooth v5.0\n" +
                        "Webcam HD Webcam\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 4 Cell 48Whr\n" +
                        "Trọng lượng 2.1 kg\n" +
                        "Màu sắc Đen, Có đèn bàn phím\n" +
                        "Kích thước 363.4 x 254.5 x 23.25 (mm)\n" +
                        "CPU AMD Ryzen 5 – 5500U\n" +
                        "RAM 8GB DDR4 (2x SO-DIMM socket, up to 32GB SDRAM)\n" +
                        "Ổ cứng 256GB PCIe® NVMe™ M.2 SSD\n" +
                        "Card đồ họa NVIDIA GeForce GTX 1650 4GB GDDR6\n" +
                        "Màn hình 15.6\" FHD (1920 x 1080) IPS, Anti-Glare, 60Hz\n" +
                        "Cổng giao tiếp 1x USB 3.0 1x USB Type C 2x USB 2.0 1x HDMI 1x RJ45\n" +
                        "Ổ quang None\n" +
                        "Audio True Harmony; Dolby® Audio Premium\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN 10/100/1000/Gigabits Base T\n" +
                        "Chuẩn WIFI Wi-Fi 6(Gig+)(802.11ax) (2x2)\n" +
                        "Bluetooth v5.0\n" +
                        "Webcam HD Webcam\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 4 Cell 48Whr\n" +
                        "Trọng lượng 2.1 kg\n" +
                        "Màu sắc Đen, Có đèn bàn phím\n" +
                        "Kích thước 363.4 x 254.5 x 23.25 (mm)",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/24205-57769_aspire_7_13_.jpg",
                        "https://philong.com.vn/media/product/24205-57769_aspire_7_12_.jpg",
                        "https://philong.com.vn/media/product/24205-57769_aspire_7_11_.jpg",
                        "https://philong.com.vn/media/product/24205-57769_aspire_7_10_.jpg",
                        "https://philong.com.vn/media/product/24205-57769_aspire_7_9_.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-7-a715-42g-r4st-ryzen-5-5500u-ram-8gb-ssd-256gb-vga-gtx1650-4gb-man-hinh-15.6quot-fhd-win-10.html",
                18490000L
        );

        saveProduct(
                "LAPTOP ACER Aspire A514 54 540F (Core i5-1135G7/Ram 8GB/SSD 512GB/Màn hình 14\" FHD/WIN 10/SILVER)\n",
                "CPU: Intel® Core™ i5-1135G7\n" +
                        "RAM: 8GB DDR4 2666Mhz\n" +
                        "Ổ cứng: 512GB PCIe NVMe SSD\n" +
                        "Card đồ hoạ: Intel Iris Xe Graphics\n" +
                        "Màn hình: 14\" FHD(1920*1080)&nbsp;\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Acer Aspire 5 A514-54-540F\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ tối đa 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2666Mhz (4GB onboard + 4GB So-dim)\n" +
                        "Số khe cắm 2\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Ổ đĩa quang (ODD) Không\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920*1080) Acer ComfyView™ IPS LED LCD, 60Hz\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel Iris Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 1 x RJ-45\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type C, 2 x USB 3.1, 1 x USB 2.0,\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x 3.5 mm headphone/speaker jack\n" +
                        "Camera HD\n" +
                        "Card mở rộng Không\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 3 cell, 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328.8 (W) x 236 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất xứ Trung Quốc\n" +
                        "Hãng sản xuất Laptop Acer\n" +
                        "Tên sản phẩm Acer Aspire 5 A514-54-540F\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ tối đa 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2666Mhz (4GB onboard + 4GB So-dim)\n" +
                        "Số khe cắm 2\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Ổ đĩa quang (ODD) Không\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920*1080) Acer ComfyView™ IPS LED LCD, 60Hz\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel Iris Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 1 x RJ-45\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type C, 2 x USB 3.1, 1 x USB 2.0,\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x 3.5 mm headphone/speaker jack\n" +
                        "Camera HD\n" +
                        "Card mở rộng Không\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 3 cell, 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328.8 (W) x 236 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/24202-637498460857867432_acer-aspire-a514-54-bac-2.jpg",
                        "https://philong.com.vn/media/product/24202-637498460857711080_acer-aspire-a514-54-bac-5.jpg",
                        "https://philong.com.vn/media/product/24202-191084.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-a514-54-540f-core-i5-1135g7ram-8gbssd-512gbman-hinh-14quot-fhdwin-10silver.html",
                17690000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE A515-56G-51YL (Core i5-1135G7, Ram 8GB, SSD 512GB, màn hình 15.6inch Full HD, VGA MX350 2GB, Win 10)",
                "CPU: Intel Core i5-1135G7\n" +
                        "RAM:&nbsp;8GB (4GBx2) DDR4 2666MHz>\n" +
                        "Lưu trữ: 512GB PCIe\n" +
                        "Đồ hoạ:&nbsp;NVIDIA GeForce MX350 2GB GDDR5>\n" +
                        "Màn hình:&nbsp;15.6\" FHD (1920 x 1080) IPS>\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "CPU Intel Core i5-1135G7 2.4GHz up to 4.2GHz 8MB\n" +
                        "RAM 8GB (4GBx2) DDR4 2666MHz (1x SO-DIMM socket, up to 20GB SDRAM)\n" +
                        "Ổ cứng 512GB SSD M.2 PCIE, 1x slot SATA3 2.5\"\n" +
                        "Card đồ họa NVIDIA GeForce MX350 2GB GDDR5 + Intel Iris Xe Graphics\n" +
                        "Màn hình 15.6\" FHD (1920 x 1080) IPS, Anti-Glare, Acer ComfyView LCD\n" +
                        "Cổng giao tiếp 1x USB 3.2 port with power-off charging 2x USB 3.2 port 1x USB Type-C port 1x RJ-45 port 1x HDMI 2.0\n" +
                        "Ổ quang None\n" +
                        "Bàn phím Backlit keyboard\n" +
                        "Audio Acer TrueHarmony\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN 10/100/1000 Mbps\n" +
                        "Chuẩn WIFI Wi-Fi 6(Gig+)(802.11ax)\n" +
                        "Bluetooth v5.1\n" +
                        "Webcam HD Webcam\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 3 Cell 48WHrs\n" +
                        "Trọng lượng 1.7 kg\n" +
                        "Màu sắc Pure Silver\n" +
                        "Đèn bàn phím Có\n" +
                        "Kích thước 363.4 x 238.5 x 17.9 (mm)\n" +
                        "CPU Intel Core i5-1135G7 2.4GHz up to 4.2GHz 8MB\n" +
                        "RAM 8GB (4GBx2) DDR4 2666MHz (1x SO-DIMM socket, up to 20GB SDRAM)\n" +
                        "Ổ cứng 512GB SSD M.2 PCIE, 1x slot SATA3 2.5\"\n" +
                        "Card đồ họa NVIDIA GeForce MX350 2GB GDDR5 + Intel Iris Xe Graphics\n" +
                        "Màn hình 15.6\" FHD (1920 x 1080) IPS, Anti-Glare, Acer ComfyView LCD\n" +
                        "Cổng giao tiếp 1x USB 3.2 port with power-off charging 2x USB 3.2 port 1x USB Type-C port 1x RJ-45 port 1x HDMI 2.0\n" +
                        "Ổ quang None\n" +
                        "Bàn phím Backlit keyboard\n" +
                        "Audio Acer TrueHarmony\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN 10/100/1000 Mbps\n" +
                        "Chuẩn WIFI Wi-Fi 6(Gig+)(802.11ax)\n" +
                        "Bluetooth v5.1\n" +
                        "Webcam HD Webcam\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 3 Cell 48WHrs\n" +
                        "Trọng lượng 1.7 kg\n" +
                        "Màu sắc Pure Silver\n" +
                        "Đèn bàn phím Có\n" +
                        "Kích thước 363.4 x 238.5 x 17.9 (mm)",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/23826-14.jpg",
                        "https://philong.com.vn/media/product/23826-13.png",
                        "https://philong.com.vn/media/product/23826-12.jpg",
                        "https://philong.com.vn/media/product/23826-11.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-a515-56g-51yl-core-i5-1135g7-ram-8gb-ssd-512gb-man-hinh-15.6inch-full-hd-vga-mx350-2gb-win-10.html",
                18690000L
        );

        saveProduct(
                "LAPTOP ACER PREDATOR PH315 53 770L (Core i7-10750H, Ram 8GB, SSD 512GB, Màn hình 15.6\" Full HD,VGA 6GB 1660Ti, Win 10)",
                "CPU: Intel Core i7-10750H &nbsp;\n" +
                        "RAM: 8GB DDR4 2933MHz\n" +
                        "Ổ cứng: 512GB PCIe NVMe\n" +
                        "Card đồ hoạ: NVIDIA GeForce GTX 1660Ti 6GB\n" +
                        "Màn hình: 15.6” FHD(1920 x 1080) IPS 144Hz\n" +
                        "Hệ điều hành: Windows 10 Home SL 64-bit",
                "Tên sản phẩm Predator Helios 300 PH315-53-770L\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i7-10750H\n" +
                        "Tốc độ 2.60GHz upto 5.00GHz, 12MB cache, 6 cores 12 threads\n" +
                        "Bộ nhớ đệm 12MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2933MHz\n" +
                        "Số khe cắm 2 khe ram, nâng cấp tối 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa thêm 2TB HDD 7200rpm, 2 khe M.2 chạy được RAID0 mỗi khe nâng cấp được 1TBSSD PCIe NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz 3ms slim bezel LCD (300nit)\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1660Ti 6GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Killer™ Wi-Fi 6 AX1650i\n" +
                        "LAN 1 x Ethernet (RJ-45) port\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn bàn phím RGB 4 zone\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type C (đọc ghi 50GBps) 1 x USB 3.2 Gen 2 (đọc ghi 10GBps) 2 x USB 3.2 Gen 1 (đọc ghi 5GBps)\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® 2.0 port with HDCP support 1 x Mini DisplayPortTM 1.4\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 59 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 22.9 (H) mm\n" +
                        "Trọng Lượng 2.2 kg\n" +
                        "Màu sắc Abyssal Black\n" +
                        "Xuất Xứ Trung Quốc\n" +
                        "Tên sản phẩm Predator Helios 300 PH315-53-770L\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i7-10750H\n" +
                        "Tốc độ 2.60GHz upto 5.00GHz, 12MB cache, 6 cores 12 threads\n" +
                        "Bộ nhớ đệm 12MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2933MHz\n" +
                        "Số khe cắm 2 khe ram, nâng cấp tối 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa thêm 2TB HDD 7200rpm, 2 khe M.2 chạy được RAID0 mỗi khe nâng cấp được 1TBSSD PCIe NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz 3ms slim bezel LCD (300nit)\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1660Ti 6GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Killer™ Wi-Fi 6 AX1650i\n" +
                        "LAN 1 x Ethernet (RJ-45) port\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn bàn phím RGB 4 zone\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB Type C (đọc ghi 50GBps) 1 x USB 3.2 Gen 2 (đọc ghi 10GBps) 2 x USB 3.2 Gen 1 (đọc ghi 5GBps)\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® 2.0 port with HDCP support 1 x Mini DisplayPortTM 1.4\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 59 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 22.9 (H) mm\n" +
                        "Trọng Lượng 2.2 kg\n" +
                        "Màu sắc Abyssal Black\n" +
                        "Xuất Xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/23787-ph315-52-4-1.jpg",
                        "https://philong.com.vn/media/product/23787-ph315-52-2-1.jpg",
                        "https://philong.com.vn/media/product/23787-4b969e2e31009785163807b410b54024.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-predator-ph315-53-770l-core-i7-10750h-ram-8gb-ssd-512gb-man-hinh-15.6quot-full-hdvga-6gb-1660ti-win-10.html",
                35790000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE A514 54 39KU (core i3-1115G4/Ram 4GB/SSD 256GB/Màn hình 14\" FHD/Win 10/SILVER)",
                "CPU: Intel Core™ i3-1115G4\n" +
                        "RAM: 4GB DDR4 2666Mhz\n" +
                        "Ổ cứng: 256GB PCIe NVMe SSD\n" +
                        "Card đồ họa: Intel® UHD Graphics\n" +
                        "Màn hình: 14” FHD(1920 x 1080)\n" +
                        "Hệ điều hành: Win 10 bản quyền",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i3-1115G4\n" +
                        "Tốc độ 4.10GHz, 6 MB cache, 2 cores 4 threads\n" +
                        "Bộ nhớ đệm 6MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 4GB DDR4 2666Mhz\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng Có (đã cắm sẵn SSD), nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920 x 1080) Acer ComfyView™ IPS LED LCD, 60Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® UHD Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.45 kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất Xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i3-1115G4\n" +
                        "Tốc độ 4.10GHz, 6 MB cache, 2 cores 4 threads\n" +
                        "Bộ nhớ đệm 6MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 4GB DDR4 2666Mhz\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng Có (đã cắm sẵn SSD), nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD(1920 x 1080) Acer ComfyView™ IPS LED LCD, 60Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® UHD Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI® port\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.45 kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất Xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/23723-2.png",
                        "https://philong.com.vn/media/product/23723-1.png",
                        "https://philong.com.vn/media/product/23723-55834_a514_54__1_.png",
                        "https://philong.com.vn/media/product/23723-40907_aspire_5_a514_54_white_ha3.jpg",
                        "https://philong.com.vn/media/product/23723-40907_aspire_5_a514_54_white_ha4.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-a514-54-39ku-core-i3-1115g4ram-4gbssd-256gbman-hinh-14quot-fhdwin-10silver.html",
                12990000L
        );

        saveProduct(
                "Laptop Acer Swift 5 SF514-55T-51NZ (i5-1135G7, Ram 8GB, SSD 512GB, Intel Iris Xe Graphics, Màn hình 14'' FHD Touch, Win 10)",
                "CPU: Intel Core i5-1135G7\n" +
                        "RAM:&nbsp;8GB Onboard LPDDR4X 2666MHz Không nâng cấp được )>\n" +
                        "Ổ cứng:&nbsp;512GB SSD M.2 NVMe>\n" +
                        "Card đồ hoạ: Intel Iris Xe Graphics\n" +
                        "Màn hình:&nbsp;14\" ( 1920 x 1080 ) Full HD IPS cảm ứng>\n" +
                        "Hệ điều hành: Windows 10 Home SL 64bit",
                "CPU Intel Core i5-1135G7 2.4GHz up to 4.2GHz 8MB\n" +
                        "RAM 8GB LPDDR4x 4267MHz\n" +
                        "Ổ cứng 512GB PCIe NVMe SSD\n" +
                        "Card đồ họa Intel Iris Xe Graphics\n" +
                        "Màn hình 14\" FHD (1920 x 1080) IPS NarrowBoarder Touch LCD, Acer CineCrystalTM LED-Backlit TFT LCD, Multi-Touch, 400 Nits, 100% sRGB, 75% Adobe RGB\n" +
                        "Cổng giao tiếp 1x USB 3.2 Gen 1 port with power-off charging 1x USB Type-C / Thunderbolt TM 4 port 1x USB 3.2 Gen 1 port 1x Power jack (DC-in) 1x HDMI® port 1x Headset / Speaker jack Finger Print\n" +
                        "Ổ quang None\n" +
                        "Audio DTS® Audio Acer TrueHarmony technology\n" +
                        "Bàn phím Led\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN None\n" +
                        "Chuẩn WIFI Intel® Wireless Wi-Fi 6 AX201 (2x2)\n" +
                        "Bluetooth v5.1\n" +
                        "Webcam (1280 x 720)\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 4 Cell 40WHrs\n" +
                        "Trọng lượng 1050 gram\n" +
                        "Màu sắc Safari Gold\n" +
                        "Kích thước 318.9 x 206.98 x 14.95 (mm)\n" +
                        "CPU Intel Core i5-1135G7 2.4GHz up to 4.2GHz 8MB\n" +
                        "RAM 8GB LPDDR4x 4267MHz\n" +
                        "Ổ cứng 512GB PCIe NVMe SSD\n" +
                        "Card đồ họa Intel Iris Xe Graphics\n" +
                        "Màn hình 14\" FHD (1920 x 1080) IPS NarrowBoarder Touch LCD, Acer CineCrystalTM LED-Backlit TFT LCD, Multi-Touch, 400 Nits, 100% sRGB, 75% Adobe RGB\n" +
                        "Cổng giao tiếp 1x USB 3.2 Gen 1 port with power-off charging 1x USB Type-C / Thunderbolt TM 4 port 1x USB 3.2 Gen 1 port 1x Power jack (DC-in) 1x HDMI® port 1x Headset / Speaker jack Finger Print\n" +
                        "Ổ quang None\n" +
                        "Audio DTS® Audio Acer TrueHarmony technology\n" +
                        "Bàn phím Led\n" +
                        "Đọc thẻ nhớ None\n" +
                        "Chuẩn LAN None\n" +
                        "Chuẩn WIFI Intel® Wireless Wi-Fi 6 AX201 (2x2)\n" +
                        "Bluetooth v5.1\n" +
                        "Webcam (1280 x 720)\n" +
                        "Hệ điều hành Windows 10 Home\n" +
                        "Pin 4 Cell 40WHrs\n" +
                        "Trọng lượng 1050 gram\n" +
                        "Màu sắc Safari Gold\n" +
                        "Kích thước 318.9 x 206.98 x 14.95 (mm)",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/23664-7.png",
                        "https://philong.com.vn/media/product/23664-6.jpg",
                        "https://philong.com.vn/media/product/23664-5.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-swift-5-sf514-55t-51nz-i5-1135g7-ram-8gb-ssd-512gb-intel-iris-xe-graphics-man-hinh-14-fhd-touch-win-10.html",
                23990000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE A514-54-51VT (Core i5-1135G7/Ram 8GB/SSD 512GB/Màn hình 14\" FHD/Win 10/SIVER)",
                "CPU: Intel Core™ i5-1135G7\n" +
                        "RAM: 8GB 2666Mhz &nbsp;\n" +
                        "Ổ cứng: 512GB PCIe NVMe\n" +
                        "Card đồ hoạ: Intel Iris Xe Graphics\n" +
                        "Màn hình: 14” FHD (1920 x 1080)\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ upto 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (4GB onboard + 4GB So-dim) DDR4 2666Mhz\n" +
                        "Số khe cắm 1 khe\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD (1920 x 1080) 60Hz Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® Iris® Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46 kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ upto 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (4GB onboard + 4GB So-dim) DDR4 2666Mhz\n" +
                        "Số khe cắm 1 khe\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD (1920 x 1080) 60Hz Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® Iris® Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46 kg\n" +
                        "Màu sắc Bạc\n" +
                        "Xuất xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/23659-55834_a514_54__2_.png",
                        "https://philong.com.vn/media/product/23659-acer-aspire-5-a514-54-51vt.png",
                        "https://philong.com.vn/media/product/23659-thumb650_acer_aspire_5_a514-54_silver_1.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-a514-54-51vt-core-i5-1135g7ram-8gbssd-512gbman-hinh-14quot-fhdwin-10siver.html",
                15990000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE A514-54-51RB (Core i5-1135G7/ram 8GB/SSD 256GB/Màn hình 14\" FHD/Win 10/GOLD)",
                "CPU: Intel Core &nbsp;i5-1135G7\n" +
                        "RAM: 8GB 2666Mhz\n" +
                        "Ổ cứng: 256GB PCIe NVMe SSD\n" +
                        "Card đồ hoạ: Intel Iris Xe Graphics\n" +
                        "Màn hình: 14.0 inch FHD (1920 x 1080)\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ upto 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (4GB onboard + 4GB So-dim)\n" +
                        "Số khe cắm 1 khe Nâng cấp tối đa 20GB (4GB onboard + 16GB sodim)\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD (1920 x 1080) Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® Iris® Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46 kg\n" +
                        "Màu sắc Vàng\n" +
                        "Xuất xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i5-1135G7\n" +
                        "Tốc độ upto 4.20GHz, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm 8MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (4GB onboard + 4GB So-dim)\n" +
                        "Số khe cắm 1 khe Nâng cấp tối đa 20GB (4GB onboard + 16GB sodim)\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 256GB PCIe NVMe SSD\n" +
                        "Tốc độ vòng quay\n" +
                        "Khe cắm SSD mở rộng nâng cấp tối đa 2 TB HDD và 1 TB SSD PCIe Gen3 8 Gb/s up to 4 lanes, NVMe\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 14.0 inch FHD (1920 x 1080) Acer ComfyView™ IPS LED LCD\n" +
                        "Độ phân giải 1920*1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình Intel® Iris® Xe Graphics\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 5.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 port with power-off charging 2 x USB 3.2 port 1 x USB Type-C port\n" +
                        "Kết nối HDMI/VGA 1 x HDMI\n" +
                        "Tai nghe 1 x Headset/speaker jack\n" +
                        "Camera HD webcam with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 328 (W) x 223 (D) x 17.95 (H) mm\n" +
                        "Trọng Lượng 1.46 kg\n" +
                        "Màu sắc Vàng\n" +
                        "Xuất xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/23658-56291_a514_54__3_.png",
                        "https://philong.com.vn/media/product/23658-5925_35205_2.jpg",
                        "https://philong.com.vn/media/product/23658-5925_35205_1.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-a514-54-51rb-core-i5-1135g7ram-8gbssd-256gbman-hinh-14quot-fhdwin-10gold.html",
                15490000L
        );

        saveProduct(
                "LAPTOP ACER ASPIRE 7 A715-41G-R150 (R7-3750H/Ram 8GB/SSD 512GB/Màn hình 15.6\" FHD/VGA 1650Ti 4G/Win 10)",
                "CPU: AMD Ryzen 7-3750H\n" +
                        "RAM: 8GB DDR4 2400Mhz &nbsp;\n" +
                        "Ổ cứng: 512GB PCIe NVMe SSD VGA\n" +
                        "Card đồ họa: NVIDIA GTX 1650Ti 4GB\n" +
                        "Màn hình: 15.6 inch FHD(1920 x 1080\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý AMD Ryzen 7-3750H\n" +
                        "Tốc độ 2.10GHz upto 3.70GHz, 4MB cache, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm Total L2 Cache 2MB Total L3 Cache 4MB\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2400Mhz\n" +
                        "Số khe cắm 2 khe, tối đa 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD (nâng cấp được tối đa 1TB SSD)\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) Acer ComfyView IPS LED LCD 60Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless-Wi-Fi 6 AX200\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 4.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 2 x USB 3.2 1 x USB Type C 1 x USB 2.0\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera HD camera with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "Card mở rộng\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh Li-ion battery\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 SL\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 254.5 (D) x 23.25 (H) mm\n" +
                        "Trọng Lượng 2.1kg\n" +
                        "Màu sắc Đen\n" +
                        "Xuất Xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý AMD Ryzen 7-3750H\n" +
                        "Tốc độ 2.10GHz upto 3.70GHz, 4MB cache, 4 cores 8 threads\n" +
                        "Bộ nhớ đệm Total L2 Cache 2MB Total L3 Cache 4MB\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB DDR4 2400Mhz\n" +
                        "Số khe cắm 2 khe, tối đa 32GB\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD (nâng cấp được tối đa 1TB SSD)\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) Acer ComfyView IPS LED LCD 60Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless-Wi-Fi 6 AX200\n" +
                        "LAN Gigabit Ethernet\n" +
                        "Bluetooth Bluetooth® 4.1\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 2 x USB 3.2 1 x USB Type C 1 x USB 2.0\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera HD camera with 1280 x 720 resolution, 720p HD audio/video recording\n" +
                        "Card mở rộng\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 48Wh Li-ion battery\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 SL\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 254.5 (D) x 23.25 (H) mm\n" +
                        "Trọng Lượng 2.1kg\n" +
                        "Màu sắc Đen\n" +
                        "Xuất Xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/23259-5774_aspire7_a715_75g_41g_black_01.png",
                        "https://philong.com.vn/media/product/23259-5774_aspire7_a715_75g_41g_black_03.png",
                        "https://philong.com.vn/media/product/23259-5774_aspire7_a715_75g_41g_black_06.png"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-aspire-7-a715-41g-r150-r7-3750hram-8gbssd-512gbman-hinh-15.6quot-fhdvga-1650ti-4gwin-10.html",
                18090000L
        );

        saveProduct(
                "LAPTOP ACER NITRO AN515 55 77P9 (Core i7 10750H/Ram 8GB/SSD 512GB/Màn hình 15.6\" FHD/VGA 4G 1650Ti/Win 10)",
                "CPU: Intel Core i7 10750H &nbsp;\n" +
                        "RAM: 8GB (8GBx1) DDR4\n" +
                        "Ổ cứng: 512GB PCIe NVMe SSD VGA\n" +
                        "Card đồ họa: NVIDIA GTX 1650Ti 4GB GDDR6\n" +
                        "Màn hình: 15.6” FHD(1920 x 1080) IPS 144Hz\n" +
                        "Hệ điều hành: Windows 10 Home",
                "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i7-10750H\n" +
                        "Tốc độ 2.60GHz upto 5.00GHz, 12MB cache, 6 cores 12 threads\n" +
                        "Bộ nhớ đệm 12MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (8GBx1) DDR4\n" +
                        "Số khe cắm 2\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 10/100/1000 Mbps\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 Gen 2 2 x USB 3.2 Gen 1 1 x USB Type C\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 57.5 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 23.9 (H) mm\n" +
                        "Trọng Lượng 2.3 kg\n" +
                        "Màu sắc Đen\n" +
                        "Xuất Xứ Trung Quốc\n" +
                        "Bộ vi xử lý\n" +
                        "Bộ vi xử lý Intel® Core™ i7-10750H\n" +
                        "Tốc độ 2.60GHz upto 5.00GHz, 12MB cache, 6 cores 12 threads\n" +
                        "Bộ nhớ đệm 12MB Cache\n" +
                        "Bộ nhớ trong (RAM)\n" +
                        "Dung lượng 8GB (8GBx1) DDR4\n" +
                        "Số khe cắm 2\n" +
                        "Ổ cứng\n" +
                        "Dung lượng 512GB PCIe NVMe SSD\n" +
                        "Ổ đĩa quang (ODD) Không có\n" +
                        "Hiển thị\n" +
                        "Màn hình 15.6 inch FHD(1920 x 1080) IPS 144Hz\n" +
                        "Độ phân giải 1920 x 1080\n" +
                        "Đồ Họa (VGA)\n" +
                        "Card màn hình NVIDIA® GeForce® GTX 1650Ti 4GB GDDR6\n" +
                        "Kết nối (Network)\n" +
                        "Wireless Intel® Wireless Wi-Fi 6 AX201\n" +
                        "LAN 10/100/1000 Mbps\n" +
                        "Bluetooth Bluetooth® 5.0\n" +
                        "Bàn phím , Chuột\n" +
                        "Kiểu bàn phím Bàn phím tiêu chuẩn, Có bàn phím số - Đèn nền bàn phím\n" +
                        "Chuột Cảm ứng đa điểm\n" +
                        "Giao tiếp mở rộng\n" +
                        "Kết nối USB 1 x USB 3.2 Gen 2 2 x USB 3.2 Gen 1 1 x USB Type C\n" +
                        "Kết nối HDMI/VGA 1xHDMI\n" +
                        "Tai nghe 1x jack 3.5mm\n" +
                        "Camera Có\n" +
                        "LOA 2 Loa\n" +
                        "Kiểu Pin 4-cell, 57.5 Wh\n" +
                        "Sạc pin Đi kèm\n" +
                        "Hệ điều hành (bản quyền) đi kèm Windows 10 Home\n" +
                        "Kích thước (Dài x Rộng x Cao) 363.4 (W) x 255 (D) x 23.9 (H) mm\n" +
                        "Trọng Lượng 2.3 kg\n" +
                        "Màu sắc Đen\n" +
                        "Xuất Xứ Trung Quốc",
                categoryLaptopAcer,
                brandAcer,
                Arrays.asList(
                        "https://philong.com.vn/media/product/23258-6731_acer_nitro_5_an515_55_55e3_nh_q7qsv_002_1.jpg",
                        "https://philong.com.vn/media/product/23258-an515-55-7.jpg"
                ),
                phiLongRetailer,
                "https://philong.com.vn/laptop-acer-nitro-an515-55-77p9-core-i7-10750hram-8gbssd-512gbman-hinh-15.6quot-fhdvga-4g-1650tiwin-10.html",
                24090000L
        );
    }


    private void saveProduct(
            String title,
            String shortDesc,
            String longDesc,
            Category category,
            Brand brand,
            List<String> images,
            Retailer retailer,
            String url,
            Long price
    ) {

        Product product = new Product();
        product.setId(null);
        product.setTitle(title);
        product.setShortDescription(shortDesc);
        product.setLongDescription(longDesc);
        product.setCategory(category);
        product.setBrand(brand);
        productRepository.save(product);
        images.forEach(s -> imageRepository.save(new Image(s, product)));

        initProductRetailer(product, retailer, url, price);
    }

    private void initProductRetailer(Product product, Retailer retailer, String url, Long price) {
        ProductRetailer productRetailer = new ProductRetailer();
        productRetailer.setProduct(product);
        productRetailer.setRetailer(retailer);
        productRetailer.setUrl(url);
        productRetailerRepository.save(productRetailer);

        initPrice(productRetailer, price);
    }

    private void initPrice(ProductRetailer productRetailer, Long p) {
        Price price = new Price();
        price.setPrice(p);
        price.setProductRetailer(productRetailer);
        priceRepository.save(price);
    }


    private void initBrand() {
        List<String> listBrand = Arrays.asList(
                "ACBEL",
                "ACER",
                "ADATA",
                "AEROCOOL",
                "AKINO",
                "AMD",
                "ANTEC",
                "AOC",
                "APACER",
                "APPLE",
                "ARROW",
                "ASANZO",
                "ASROCK",
                "ASUS",
                "AUDIO-TECHNICA",
                "AVEXIR",
                "AVITA",
                "BENQ",
                "BLACKBERRY",
                "CASPER",
                "COOLER MASTER",
                "CORLORFULL",
                "CORSAIR",
                "CREATIVE",
                "CRUCIAL",
                "DAREU",
                "DEEPCOOL",
                "DELL",
                "DELUXE",
                "ELGATO",
                "EROSI",
                "FSP",
                "GSKILL",
                "GALAX",
                "GEIL",
                "GOLDEN FIELD",
                "GIGABYTE",
                "HKC",
                "HP",
                "HUAWEI",
                "HUNTKEY",
                "INFINITY",
                "INTEL",
                "JABRA",
                "JBL",
                "JETEK",
                "KINGMAX",
                "KINGSTON",
                "KLEVV",
                "LACIE",
                "LEAKTEK",
                "LENOVO",
                "LEXAR",
                "LG",
                "LOGITECH",
                "MEIZU",
                "MICROSOFT",
                "MSI",
                "NOKIA",
                "NVIDIA",
                "NZXT",
                "ONEPLUS",
                "OPPO (REALME)",
                "ORICO",
                "PANASONIC",
                "PATRIOT",
                "PLEXTOR",
                "PHANTEKS",
                "PHILIPS",
                "RAZER",
                "SAHARA",
                "SAMA",
                "SAMSUNG",
                "SANCO",
                "SANDISK",
                "SEAGATE",
                "SEASONIC",
                "SHARP",
                "SKULLCANDY",
                "SONY",
                "SOUNDMAX",
                "SSK",
                "SUPER FLOWER",
                "SUPERMICRO",
                "TARGUS",
                "TCL",
                "TOSHIBA",
                "THERMALTAKE",
                "TRANSCEND",
                "VIEWSONIC",
                "VIVO",
                "WD",
                "XIAOMI",
                "XIGMATEK",
                "ZIDLI",
                "ZTE"
        );
        listBrand.forEach(s -> {
            brandRepository.save(
                    new Brand(
                            s,
                            "Nhà sản xuất " + s,
                            true
                    )
            );
        });
    }

    private void initRetailer() {
//        retailerRepository.save(new Retailer(
//                "didongdanang.com",
//                "Siêu thị điện thoại - phụ kiện - linh kiện chính hãng",
//                "https://didongdanang.com/",
//                "https://didongdanang.com/wp-content/uploads/2020/02/bi-logo1.jpg",
//                true,
//                true,
//                userRepository.findByUsername("supersuper").orElse(null)
//        ));

        retailerRepository.save(new Retailer(
                "Shopdunk",
                "ShopDuck Đại lý ủy quyền Apple",
                "https://shopdunk.com/",
                "https://firebasestorage.googleapis.com/v0/b/shop1-eeb2c.appspot.com/o/images%2Fthebestprice%2Fshopdunk.PNG?alt=media&token=dfa85480-b42f-4fad-9f36-88f4fbc87a5e",
                true, true,
                userRepository.findByUsername("supersuper").orElse(null)
        ));

        retailerRepository.save(new Retailer(
                "PhiLong Technology",
                "Trang web công ty Phi Long",
                "https://philong.com.vn/",
                "https://firebasestorage.googleapis.com/v0/b/shop1-eeb2c.appspot.com/o/images%2Fthebestprice%2Fphilong.PNG?alt=media&token=fcf98e99-3955-4208-bfd1-e3195b1d4a3e",
                true, true,
                userRepository.findByUsername("supersuper").orElse(null)
        ));

        retailerRepository.save(new Retailer(
                "Xuân Vinh",
                "Trang web công ty Xuân Vinh",
                "http://xuanvinh.vn/",
                "https://firebasestorage.googleapis.com/v0/b/shop1-eeb2c.appspot.com/o/images%2Fthebestprice%2Fxuanvinh.PNG?alt=media&token=071dd4ad-3202-4852-9d23-4cdcd4179019",
                true, true,
                userRepository.findByUsername("supersuper").orElse(null)
        ));

        retailerRepository.save(new Retailer(
                "AnhDucDigital",
                "Trang web AnhDucDigital",
                "https://anhducdigital.vn/",
                "https://firebasestorage.googleapis.com/v0/b/shop1-eeb2c.appspot.com/o/images%2Fthebestprice%2Fanhducdigital.PNG?alt=media&token=4e7f9c53-722d-47f9-a70d-da5e4d143134",
                true, true,
                userRepository.findByUsername("supersuper").orElse(null)
        ));

        retailerRepository.save(new Retailer(
                "An Phát PC",
                "Trang web công ty An Phát PC",
                "https://www.anphatpc.com.vn/",
                "https://firebasestorage.googleapis.com/v0/b/shop1-eeb2c.appspot.com/o/images%2Fthebestprice%2Fanphatoc.PNG?alt=media&token=e4ff6a88-cbc1-4a69-805c-8d74111cb03d",
                true, true,
                userRepository.findByUsername("supersuper").orElse(null)
        ));

        retailerRepository.save(new Retailer(
                "IphoneDaNang",
                "Trang web iphone đà nẵng",
                "https://iphonedanang.com.vn/",
                "https://firebasestorage.googleapis.com/v0/b/shop1-eeb2c.appspot.com/o/images%2Fthebestprice%2Fapplet%26t.PNG?alt=media&token=35fe7de2-9402-4a73-a6de-5fc0df18f9fe",
                true, true,
                userRepository.findByUsername("supersuper").orElse(null)
        ));

        retailerRepository.save(new Retailer(
                "Di Động Việt",
                "Trang web công ty Di Động Việt",
                "https://didongviet.vn/",
                "https://firebasestorage.googleapis.com/v0/b/shop1-eeb2c.appspot.com/o/images%2Fthebestprice%2Fdidongviet.PNG?alt=media&token=07c1f579-419b-4003-b533-ef24138649bc",
                true, true,
                userRepository.findByUsername("supersuper").orElse(null)
        ));

        retailerRepository.save(new Retailer(
                "GearVN",
                "Trang web công ty GearVN",
                "https://gearvn.com/",
                "https://firebasestorage.googleapis.com/v0/b/shop1-eeb2c.appspot.com/o/images%2Fthebestprice%2Fgearvn.PNG?alt=media&token=0bf2d014-13c5-45ed-91c2-96f7ef5c6357",
                true, true,
                userRepository.findByUsername("supersuper").orElse(null)
        ));

        retailerRepository.save(new Retailer(
                "Hà Nội Computer",
                "Trang web công ty Hà Nội Computer",
                "https://www.hanoicomputer.vn/",
                "https://firebasestorage.googleapis.com/v0/b/shop1-eeb2c.appspot.com/o/images%2Fthebestprice%2Fhanoicomputer.PNG?alt=media&token=61f89364-7bc7-4ba5-ae8a-3e46c845de34",
                true, true,
                userRepository.findByUsername("supersuper").orElse(null)
        ));
    }

    private void initCategory() {
        categoryService.create(new CategoryRequest(
                "Điện thoại",
                "Danh mục điện thoại",
                null
        ));

        List<String> listSmartPhoneName = Arrays.asList(
                "Điện thoại Samsung",
                "Điện thoại OPPO (Realme)",
                "Điện thoại Asus",
                "Điện thoại Google",
                "Điện thoại Sony",
                "Điện thoại BKAV",
                "Điện thoại Meiigoo",
                "Điện thoại Iphone",
                "Điện thoại OnePlus",
                "Điện thoại Vivo",
                "Điện thoại Nokia",
                "Điện thoại Vsmart",
                "Điện thoại BlackBerry",
                "Điện thoại Huawei",
                "Điện thoại Xiaomi",
                "Điện thoại ZTE",
                "Điện thoại LG",
                "Điện thoại Meizu",
                "Điện thoại Lenovo"
        );

        listSmartPhoneName.forEach(s -> {
            categoryService.create(new CategoryRequest(
                    s,
                    "Danh mục điện thoại " + s,
                    categoryRepository.findByTitle("Điện thoại").getId()
            ));
        });

        categoryService.create(new CategoryRequest(
                "Laptop",
                "Danh mục laptop",
                null
        ));

        List<String> laptopNameList = Arrays.asList(
                "Laptop ASUS",
                "Laptop HP",
                "Laptop ACER",
                "Laptop DELL",
                "Laptop LENOVO",
                "Laptop MSI",
                "Laptop LG",
                "Laptop AVITA",
                "Laptop MICROSOFT",
                "Laptop Huawei"
        );

        laptopNameList.forEach(s -> {
            categoryService.create(new CategoryRequest(
                    s,
                    "Danh muc laptop " + s,
                    categoryRepository.findByTitle("Laptop").getId()
            ));
        });

        categoryService.create(new CategoryRequest(
                "Tivi",
                "Danh mục Tivi",
                null
        ));

        List<String> tiviNameList = Arrays.asList(
                "LG",
                "SAMSUNG",
                "SONY",
                "TCL",
                "PANASONIC",
                "SHARP",
                "SANCO",
                "TOSHIBA"
        );
        tiviNameList.forEach(s -> {
            categoryService.create(
                    new CategoryRequest(
                            "Tivi " + s,
                            "Danh mục tivi " + s,
                            categoryRepository.findByTitle("Tivi").getId()
                    )
            );
        });

        categoryService.create(new CategoryRequest(
                "Tai nghe",
                "Danh mục Tai nghe",
                null
        ));

        List<String> headphoneNameList = Arrays.asList(
                "APPLE",
                "CORSAIR",
                "DAREU",
                "JBL",
                "LG",
                "LOGITECH"
        );

        headphoneNameList.forEach(s -> {
            categoryService.create(
                    new CategoryRequest(
                            "Tai nghe " + s,
                            "Danh mục tai nghe " + s,
                            categoryRepository.findByTitle("Tai nghe").getId()
                    )
            );
        });

        categoryService.create(new CategoryRequest(
                "Màn hình",
                "Danh mục màn hình máy tính",
                null
        ));

        List<String> monitorNameList = Arrays.asList(
                "ACER",
                "AOC",
                "ASUS",
                "BENQ",
                "DELL",
                "GIGABYTE",
                "HKC",
                "HP",
                "INFINITY",
                "LENOVO",
                "LG",
                "MSI",
                "PHILIPS",
                "SAMSUNG",
                "VIEWSONIC"

        );

        monitorNameList.forEach(s -> {
            categoryService.create(new CategoryRequest(
                    "Màn hình " + s,
                    "Danh mục màn hình máy tính " + s,
                    categoryRepository.findByTitle("Màn hình").getId()
            ));
        });

        categoryService.create(
                new CategoryRequest(
                        "Linh kiện máy tính",
                        "Danh mục linh kiện máu tính",
                        null
                )
        );

        List<String> linhKienName = Arrays.asList(
                "Bộ vi xử lý - cpu",
                "Bo mạch chủ - mainboard",
                "Bộ nhớ trong - ram",
                "ổ đĩa hdd-dvd",
                "ổ cứng ssd",
                "card đồ họa-vga",
                "nguồn máy tính psu",
                "vỏ máy tính - case"
        );

        linhKienName.forEach(s -> {
            categoryService.create(
                    new CategoryRequest(
                            s.toUpperCase(),
                            "Danh mục linh kiện máy tính - " + s,
                            categoryRepository.findByTitle("Linh kiện máy tính").getId()
                    )
            );
        });

        categoryService.create(
                new CategoryRequest(
                        "Phím chuột, Gaming Gear",
                        "Danh mục phím chuột, Gaming Gear",
                        null
                )
        );

        List<String> phimchuotNameList = Arrays.asList(
                "Bàn phím",
                "Chuột máy tính",
                "Bàn di chuột",
                "Tay cầm chơi game",
                "Ghế chơi game",
                "Bàn chơi game",
                "Máy chơi game"
        );

        phimchuotNameList.forEach(s -> {
            categoryService.create(new CategoryRequest(
                    s,
                    "Danh mục phím chuột, Gaming Gear - " + s,
                    categoryRepository.findByTitle("Phím chuột, Gaming Gear").getId()
            ));
        });


        categoryService.create(
                new CategoryRequest(
                        "Phụ kiện công nghê",
                        "Danh mục phụ kiện công nghệ",
                        null
                )
        );

        List<String> phuKienCongNgheNameList = Arrays.asList(
                "Thiết bị lưu trữ",
                "Pin dự phòng, cáp sạc, củ sạc",
                "Dây cáp các loại",
                "Thiết bị chuyển đổi"
        );

        phuKienCongNgheNameList.forEach(s -> {
            categoryService.create(
                    new CategoryRequest(
                            s,
                            "Danh mục phụ kiện công nghệ - " + s,
                            categoryRepository.findByTitle("Phụ kiện công nghê").getId()
                    )
            );
        });

        categoryService.create(
                new CategoryRequest(
                        "Thiết bị mạng",
                        "Danh mục thiết bị mạng",
                        null
                )
        );

        List<String> thietbimangNameList = Arrays.asList(
                "Bộ phát sóng wifi",
                "Bộ nhận sóng wifi",
                "bộ kích mở rộng sóng wifi",
                "bộ phát sóng wifi 4G",
                "bộ chia mạng hub/switch",
                "tủ mạng - tủ rack",
                "cáp mạng",
                "phụ kiện thi công mạng"
        );

        thietbimangNameList.forEach(s ->
                categoryService.create(
                        new CategoryRequest(
                                s,
                                "Danh mục thiết bị mạng " + s,
                                categoryRepository.findByTitle("Thiết bị mạng").getId()
                        )
                )
        );

        categoryService.create(
                new CategoryRequest(
                        "Máy in, Scan, Vật tự máy in",
                        "Danh mục máy in, Scan, Vật tự máy in",
                        null
                )
        );

        List<String> mayinNameList = Arrays.asList(
                "Máy in laser",
                "Máy in phun",
                "Máy in hóa đơn",
                "Máy in tem nhãn, đầu đọc mã vạch",
                "Máy in kim",
                "Máy scan",
                "Máy photocopy"
        );

        mayinNameList.forEach(s -> {
            categoryService.create(
                    new CategoryRequest(
                            s,
                            "Danh mục máy in, Scan, Vật tự máy in - " + s,
                            categoryRepository.findByTitle("Máy in, Scan, Vật tự máy in").getId()
                    )
            );
        });

    }


    public void initUser() {

        // tài khoản supper
        userRepository.save(
                new User(
                        "supersuper",
                        passwordEncoder.encode("adminadmin"),
                        "Tài khoản root",
                        "Đại học duy tân",
                        "supper@gmail.com",
                        "0365843463",
                        true,
                        true,
                        ERole.ROLE_SUPER
                )
        );

        userRepository.save(
                new User(
                        "truongvanthan",
                        passwordEncoder.encode("thanthan"),
                        "Trương Văn Thân",
                        "Triệu Phong - Quảng Trị",
                        "vanthan.ad.it@gmail.com",
                        "0365843463",
                        true,
                        true,
                        ERole.ROLE_ADMIN
                )
        );

        userRepository.save(
                new User(
                        "nguyenthithao",
                        passwordEncoder.encode("nguyenthithao123"),
                        "Nguyễn Thị Thảo",
                        "Quảng Bình",
                        "thithaonguyen@gmail.com",
                        "0982312370",
                        true,
                        true,
                        ERole.ROLE_RETAILER
                )
        );

        userRepository.save(
                new User(
                        "nguyenhuutho",
                        passwordEncoder.encode("thothotho"),
                        "Nguyễn Hữu Thọ",
                        "Hải Lăng - Quảng Trị",
                        "huutho.nguyen@gmail.com",
                        "0973612732",
                        true,
                        true,
                        ERole.ROLE_RETAILER
                )
        );


        userRepository.save(
                new User(
                        "nguyenthinhung",
                        passwordEncoder.encode("nhungnhung"),
                        "Nguyễn Thị Nhung",
                        "Triệu An - Quảng Trị",
                        "nhungnhung@gmail.com",
                        "0934273263",
                        true,
                        true,
                        ERole.ROLE_GUEST
                )
        );

        userRepository.save(
                new User(
                        "nguyenquanghai",
                        passwordEncoder.encode("nguyenquanghai"),
                        "Nguyễn Quang Hải",
                        "Hà Nội",
                        "quanghai.bongda@gmail.com",
                        "0937246284",
                        true,
                        true,
                        ERole.ROLE_GUEST
                )
        );

        userRepository.save(
                new User(
                        "phuongnc",
                        passwordEncoder.encode("nguyencongphuong"),
                        "Nguyễn Công Phượng",
                        "TP.Hồ Chí Minh",
                        "nguyencongphuong.bongda@gmail.com",
                        "0987436829",
                        true,
                        true,
                        ERole.ROLE_GUEST
                )
        );

        userRepository.save(
                new User(
                        "mytam_singer",
                        passwordEncoder.encode("casimytam"),
                        "Ca sĩ mỹ tâm",
                        "Đà Nẵng",
                        "mytam.danang@gmail.com",
                        "0937378924",
                        true,
                        true,
                        ERole.ROLE_GUEST
                )
        );

        userRepository.save(
                new User(
                        "kylamdtu",
                        passwordEncoder.encode("kytamdtu"),
                        "Lê Nguyễn kỳ Lâm",
                        "Đà Nẵng",
                        "kylamdtu@gmail.com",
                        "0938274629",
                        true,
                        true,
                        ERole.ROLE_RETAILER
                )
        );

        userRepository.save(
                new User(
                        "khacvietsinger",
                        passwordEncoder.encode("khacviet"),
                        "Nguyễn Khắc Việt",
                        "Hà Nội",
                        "khacviet.casi@gmail.com",
                        "0983748345",
                        true,
                        true,
                        ERole.ROLE_GUEST
                )
        );

        userRepository.save(
                new User(
                        "daobinhan123",
                        passwordEncoder.encode("daobinhan"),
                        "Đào Bình An",
                        "Hải Lăng - Quảng Trị",
                        "binhandao.hailang@gmail.com",
                        "0987438943",
                        true,
                        true,
                        ERole.ROLE_GUEST
                )
        );

        userRepository.save(
                new User(
                        "vaocaoky1999",
                        passwordEncoder.encode("vocaoky99"),
                        "Võ Cao Kỳ",
                        "Ái Tử - Quảng Trị",
                        "kyvo1999@gmail.com",
                        "093743478",
                        true,
                        true,
                        ERole.ROLE_GUEST
                )
        );

        userRepository.save(
                new User(
                        "nguyenthanhnamtb",
                        passwordEncoder.encode("nguyenthanhnam"),
                        "Nguyễn Thành Nam",
                        "Tiền Hải - Thái Bình",
                        "namntn@gmail.com",
                        "0987367834",
                        true,
                        true,
                        ERole.ROLE_GUEST
                )
        );

    }
}
