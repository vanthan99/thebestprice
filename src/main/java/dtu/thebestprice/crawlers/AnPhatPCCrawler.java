package dtu.thebestprice.crawlers;

import dtu.thebestprice.crawlers.model.CrawlerModel;

import java.util.List;

public interface AnPhatPCCrawler {
    Long getPriceByUrl(String url);

    List<CrawlerModel> listLaptopAcer();

//    List<CrawlerModel> listLaptopApple();

    List<CrawlerModel> listLaptopAsus();

    List<CrawlerModel> listLaptopAvita();

    List<CrawlerModel> listLaptopDell();

    List<CrawlerModel> listLaptopHp();

    List<CrawlerModel> listLaptopLg();

    List<CrawlerModel> listLaptopMsi();

    List<CrawlerModel> listLaptopLenovo();
}
