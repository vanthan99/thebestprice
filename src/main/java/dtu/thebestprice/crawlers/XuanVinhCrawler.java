package dtu.thebestprice.crawlers;

import dtu.thebestprice.crawlers.model.CrawlerModel;

import java.io.IOException;
import java.util.List;

public interface XuanVinhCrawler {
    Long getPriceByUrl(String url);

    List<CrawlerModel> listLaptopDell();

    List<CrawlerModel> listLaptopLg();

    List<CrawlerModel> listLaptopAvita();

    List<CrawlerModel> listLaptopLenovo();

    List<CrawlerModel> listLaptopHp();

    List<CrawlerModel> listLaptopMsi();

    List<CrawlerModel> listLaptopAsus();

    List<CrawlerModel> listLaptopAcer();


    List<CrawlerModel> listCpuIntel();

    List<CrawlerModel> listCpuAmd();

}
