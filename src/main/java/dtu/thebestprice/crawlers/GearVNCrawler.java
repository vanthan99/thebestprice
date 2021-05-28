package dtu.thebestprice.crawlers;

import dtu.thebestprice.crawlers.model.CrawlerModel;

import java.util.List;

public interface GearVNCrawler {
    Long getPriceByUrl(String url);

    List<CrawlerModel> listLaptopAcer();

//    List<CrawlerModel> listLaptopApple();

    List<CrawlerModel> listLaptopAsus();

    List<CrawlerModel> listLaptopDell();

    List<CrawlerModel> listLaptopHp();

    List<CrawlerModel> listLaptopLg();

    List<CrawlerModel> listLaptopMsi();

    List<CrawlerModel> listLaptopLenovo();
}
