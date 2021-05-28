package dtu.thebestprice.jobs;

import dtu.thebestprice.crawlers.*;
import dtu.thebestprice.entities.Price;
import dtu.thebestprice.entities.ProductRetailer;
import dtu.thebestprice.entities.Retailer;
import dtu.thebestprice.repositories.PriceRepository;
import dtu.thebestprice.repositories.ProductRetailerRepository;
import dtu.thebestprice.repositories.RetailerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class UpdatePriceJob {
    @Autowired
    ProductRetailerRepository productRetailerRepository;

    @Autowired
    PriceRepository priceRepository;

    @Autowired
    AnhDucDigitalCrawler anhDucDigitalCrawler;

    @Autowired
    AnPhatPCCrawler anPhatPCCrawler;

    @Autowired
    AppleTAndTCrawler appleTAndTCrawler;

    @Autowired
    DiDongVietCrawler diDongVietCrawler;

    @Autowired
    GearVNCrawler gearVNCrawler;

    @Autowired
    HaNoiComputerCrawler haNoiComputerCrawler;

    @Autowired
    PhiLongCrawler phiLongCrawler;

    @Autowired
    ShopDunkCrawler shopDunkCrawler;

    @Autowired
    XuanVinhCrawler xuanVinhCrawler;

    @Autowired
    RetailerRepository retailerRepository;

    //     cập nhật giá cho sản phẩm của Phi Long
    @Scheduled(fixedDelay = 18000000)
    @Transactional
    public void updatePriceForPhiLong() {
        try {
            Retailer retailer = retailerRepository.findByDeleteFlgFalseAndHomePage("https://philong.com.vn/");
            List<ProductRetailer> productRetailers = productRetailerRepository.findByDeleteFlgFalseAndApproveTrueAndRetailer(retailer);
            productRetailers.forEach(productRetailer -> {
                Long priceCrawl = phiLongCrawler.getPriceByUrl(productRetailer.getUrl());
                if (priceCrawl != null) {
                    // kiểm tra xem giá đã cào hiện tại và giá trong DB có thay đổi hay không
                    Price priceInDB = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
                    if (!priceCrawl.equals(priceInDB.getPrice())) {
                        productRetailer.setEnable(true);
                        productRetailerRepository.save(productRetailer);
                        priceRepository.save(new Price(priceCrawl, productRetailer));
                    }
                } else {
                    // đặt trạng thái thông tin nơi bán về false
                    productRetailer.setEnable(false);
                    productRetailerRepository.save(productRetailer);
                }
            });
        } catch (Exception ignored) {
            System.out.println("Đã xảy ra vấn đề khi cập nhật giá cho sản phẩm web philong");
        }
    }

    // cập nhật giá cho sản phẩm của Xuan Vinh
    // sau 5 tiếng thì cập nhật lại 1 lần
    @Scheduled(fixedDelay = 18000000)
    @Transactional
    public void updatePriceForXuanVinh() {
        try {
            Retailer retailer = retailerRepository.findByDeleteFlgFalseAndHomePage("http://xuanvinh.vn/");
            List<ProductRetailer> productRetailers = productRetailerRepository.findByDeleteFlgFalseAndApproveTrueAndRetailer(retailer);
            productRetailers.forEach(productRetailer -> {
                Long priceCrawl = xuanVinhCrawler.getPriceByUrl(productRetailer.getUrl());
                if (priceCrawl != null) {
                    // kiểm tra xem giá đã cào hiện tại và giá trong DB có thay đổi hay không
                    Price priceInDB = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
                    if (!priceCrawl.equals(priceInDB.getPrice())) {
                        productRetailer.setEnable(true);
                        productRetailerRepository.save(productRetailer);
                        priceRepository.save(new Price(priceCrawl, productRetailer));
                    }
                } else {
                    // đặt trạng thái thông tin nơi bán về false
                    productRetailer.setEnable(false);
                    productRetailerRepository.save(productRetailer);
                }
            });
        } catch (Exception ignored) {
            System.out.println("Đã xảy ra vấn đề khi cập nhật giá cho sản phẩm web xuanvinh");
        }
    }

    // cập nhật giá cho sản phẩm của ShopDunk
    // sau 5 tiếng thì cập nhật lại 1 lần
    @Scheduled(fixedDelay = 18000000)
    @Transactional
    public void updatePriceForShopDunk() {
        try {
            Retailer retailer = retailerRepository.findByDeleteFlgFalseAndHomePage("https://shopdunk.com/");
            List<ProductRetailer> productRetailers = productRetailerRepository.findByDeleteFlgFalseAndApproveTrueAndRetailer(retailer);
            productRetailers.forEach(productRetailer -> {
                Long priceCrawl = shopDunkCrawler.getPriceByUrl(productRetailer.getUrl());
                if (priceCrawl != null) {
                    // kiểm tra xem giá đã cào hiện tại và giá trong DB có thay đổi hay không
                    Price priceInDB = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
                    if (!priceCrawl.equals(priceInDB.getPrice())) {
                        productRetailer.setEnable(true);
                        productRetailerRepository.save(productRetailer);
                        priceRepository.save(new Price(priceCrawl, productRetailer));
                    }
                } else {
                    // đặt trạng thái thông tin nơi bán về false
                    productRetailer.setEnable(false);
                    productRetailerRepository.save(productRetailer);
                }
            });
        } catch (Exception ignored) {
            System.out.println("Đã xảy ra vấn đề khi cập nhật giá cho sản phẩm web shopdunk");
        }
    }

    // cập nhật giá cho sản phẩm của Anh Duc Digital
    // sau 5 tiếng thì cập nhật lại 1 lần
    @Scheduled(fixedDelay = 18000000)
    @Transactional
    public void updatePriceForAnhDucDigital() {
        try {
            Retailer retailer = retailerRepository.findByDeleteFlgFalseAndHomePage("https://anhducdigital.vn/");
            List<ProductRetailer> productRetailers = productRetailerRepository.findByDeleteFlgFalseAndApproveTrueAndRetailer(retailer);
            productRetailers.forEach(productRetailer -> {
                Long priceCrawl = anhDucDigitalCrawler.getPriceByUrl(productRetailer.getUrl());
                if (priceCrawl != null) {
                    // kiểm tra xem giá đã cào hiện tại và giá trong DB có thay đổi hay không
                    Price priceInDB = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
                    if (!priceCrawl.equals(priceInDB.getPrice())) {
                        productRetailer.setEnable(true);
                        productRetailerRepository.save(productRetailer);
                        priceRepository.save(new Price(priceCrawl, productRetailer));
                    }
                } else {
                    // đặt trạng thái thông tin nơi bán về false
                    productRetailer.setEnable(false);
                    productRetailerRepository.save(productRetailer);
                }
            });
        } catch (Exception ignored) {
            System.out.println("Đã xảy ra vấn đề khi cập nhật giá cho sản phẩm web anhduc digital");

        }
    }

    // cập nhật giá cho sản phẩm của An Phat Pc
    // sau 5 tiếng thì cập nhật lại 1 lần
    @Scheduled(fixedDelay = 18000000)
    @Transactional
    public void updatePriceForAnPhatPC() {
        try {
            Retailer retailer = retailerRepository.findByDeleteFlgFalseAndHomePage("https://www.anphatpc.com.vn/");
            List<ProductRetailer> productRetailers = productRetailerRepository.findByDeleteFlgFalseAndApproveTrueAndRetailer(retailer);
            productRetailers.forEach(productRetailer -> {
                Long priceCrawl = anPhatPCCrawler.getPriceByUrl(productRetailer.getUrl());
                if (priceCrawl != null) {
                    // kiểm tra xem giá đã cào hiện tại và giá trong DB có thay đổi hay không
                    Price priceInDB = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
                    if (!priceCrawl.equals(priceInDB.getPrice())) {
                        productRetailer.setEnable(true);
                        productRetailerRepository.save(productRetailer);
                        priceRepository.save(new Price(priceCrawl, productRetailer));
                    }
                } else {
                    // đặt trạng thái thông tin nơi bán về false
                    productRetailer.setEnable(false);
                    productRetailerRepository.save(productRetailer);
                }
            });
        } catch (Exception ignored) {
            System.out.println("Đã xảy ra vấn đề khi cập nhật giá cho sản phẩm web an phat pc");

        }
    }

    // cập nhật giá cho sản phẩm của Apple T & T
    // sau 5 tiếng thì cập nhật lại 1 lần
    @Scheduled(fixedDelay = 18000000)
    @Transactional
    public void updatePriceForAppleTAndT() {
        try {
            Retailer retailer = retailerRepository.findByDeleteFlgFalseAndHomePage("https://iphonedanang.com.vn/");
            List<ProductRetailer> productRetailers = productRetailerRepository.findByDeleteFlgFalseAndApproveTrueAndRetailer(retailer);
            productRetailers.forEach(productRetailer -> {
                Long priceCrawl = appleTAndTCrawler.getPriceByUrl(productRetailer.getUrl());
                if (priceCrawl != null) {
                    // kiểm tra xem giá đã cào hiện tại và giá trong DB có thay đổi hay không
                    Price priceInDB = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
                    if (!priceCrawl.equals(priceInDB.getPrice())) {
                        productRetailer.setEnable(true);
                        productRetailerRepository.save(productRetailer);
                        priceRepository.save(new Price(priceCrawl, productRetailer));
                    }
                } else {
                    // đặt trạng thái thông tin nơi bán về false
                    productRetailer.setEnable(false);
                    productRetailerRepository.save(productRetailer);
                }
            });
        } catch (Exception ignored) {
            System.out.println("Đã xảy ra vấn đề khi cập nhật giá cho sản phẩm web iphonedanang");

        }
    }

    // cập nhật giá cho sản phẩm của Di Dong Viet
    // sau 5 tiếng thì cập nhật lại 1 lần
    @Scheduled(fixedDelay = 18000000)
    @Transactional
    public void updatePriceForDiDongViet() {
        try {
            Retailer retailer = retailerRepository.findByDeleteFlgFalseAndHomePage("https://didongviet.vn/");
            List<ProductRetailer> productRetailers = productRetailerRepository.findByDeleteFlgFalseAndApproveTrueAndRetailer(retailer);
            productRetailers.forEach(productRetailer -> {
                Long priceCrawl = diDongVietCrawler.getPriceByUrl(productRetailer.getUrl());
                if (priceCrawl != null) {
                    // kiểm tra xem giá đã cào hiện tại và giá trong DB có thay đổi hay không
                    Price priceInDB = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
                    if (!priceCrawl.equals(priceInDB.getPrice())) {
                        productRetailer.setEnable(true);
                        productRetailerRepository.save(productRetailer);
                        priceRepository.save(new Price(priceCrawl, productRetailer));
                    }
                } else {
                    // đặt trạng thái thông tin nơi bán về false
                    productRetailer.setEnable(false);
                    productRetailerRepository.save(productRetailer);
                }
            });
        } catch (Exception ignored) {
            System.out.println("Đã xảy ra vấn đề khi cập nhật giá cho sản phẩm web didongviet");
        }
    }

    // cập nhật giá cho sản phẩm của Gearn Vn
    // sau 5 tiếng thì cập nhật lại 1 lần
    @Scheduled(fixedDelay = 18000000)
    @Transactional
    public void updatePriceForGearVN() {
        try {
            Retailer retailer = retailerRepository.findByDeleteFlgFalseAndHomePage("https://gearvn.com/");
            List<ProductRetailer> productRetailers = productRetailerRepository.findByDeleteFlgFalseAndApproveTrueAndRetailer(retailer);
            productRetailers.forEach(productRetailer -> {
                Long priceCrawl = gearVNCrawler.getPriceByUrl(productRetailer.getUrl());
                if (priceCrawl != null) {
                    // kiểm tra xem giá đã cào hiện tại và giá trong DB có thay đổi hay không
                    Price priceInDB = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
                    if (!priceCrawl.equals(priceInDB.getPrice())) {
                        productRetailer.setEnable(true);
                        productRetailerRepository.save(productRetailer);
                        priceRepository.save(new Price(priceCrawl, productRetailer));
                    }
                } else {
                    // đặt trạng thái thông tin nơi bán về false
                    productRetailer.setEnable(false);
                    productRetailerRepository.save(productRetailer);
                }
            });
        } catch (Exception ignored) {
            System.out.println("Đã xảy ra vấn đề khi cập nhật giá cho sản phẩm web gearvn");
        }
    }

    // cập nhật giá cho sản phẩm của Ha Noi Computer
    // sau 5 tiếng thì cập nhật lại 1 lần
    @Scheduled(fixedDelay = 18000000)
    @Transactional
    public void updatePriceForHaNoiComputer() {
        try {
            Retailer retailer = retailerRepository.findByDeleteFlgFalseAndHomePage("https://www.hanoicomputer.vn/");
            List<ProductRetailer> productRetailers = productRetailerRepository.findByDeleteFlgFalseAndApproveTrueAndRetailer(retailer);
            productRetailers.forEach(productRetailer -> {
                Long priceCrawl = haNoiComputerCrawler.getPriceByUrl(productRetailer.getUrl());
                if (priceCrawl != null) {
                    // kiểm tra xem giá đã cào hiện tại và giá trong DB có thay đổi hay không
                    Price priceInDB = priceRepository.findFirstByProductRetailerOrderByUpdatedAtDesc(productRetailer);
                    if (!priceCrawl.equals(priceInDB.getPrice())) {
                        productRetailer.setEnable(true);
                        productRetailerRepository.save(productRetailer);
                        priceRepository.save(new Price(priceCrawl, productRetailer));
                    }
                } else {
                    // đặt trạng thái thông tin nơi bán về false
                    productRetailer.setEnable(false);
                    productRetailerRepository.save(productRetailer);
                }
            });
        } catch (Exception ignored) {
            System.out.println("Đã xảy ra vấn đề khi cập nhật giá cho sản phẩm web hanoi computer");
        }
    }
}
