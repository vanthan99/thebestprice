package dtu.thebestprice.crawlers;

import dtu.thebestprice.crawlers.model.CrawlerModel;
import dtu.thebestprice.crawlers.model.ProductCrawler;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface PhiLongCrawler {
    Long getPriceByUrl(String url);

    Set<ProductCrawler> getProductByURL(String url) throws IOException;

    // start laptop
    List<CrawlerModel> listLaptopAsus();

    List<CrawlerModel> listLaptopAcer();

    List<CrawlerModel> listLaptopDell();

    List<CrawlerModel> listLaptopLenovo();

    List<CrawlerModel> listLaptopHp();

    List<CrawlerModel> listLaptopMsi();

    List<CrawlerModel> listLaptopLg();

//    List<CrawlerModel> listLaptopMicrosoft() throws IOException;

    List<CrawlerModel> listLaptopAvita();
    // end laptop

    // start monitor
    List<CrawlerModel> listMonitorHp();

    List<CrawlerModel> listMonitorSamsung();

    List<CrawlerModel> listMonitorDell();

    List<CrawlerModel> listMonitorLg();
    // end monitor


    // start cpu intell
    List<CrawlerModel> listCpuIntel();
    // end cpu intell

    //start cpu amd
    List<CrawlerModel> listCpuAmd();
    // end cpu amd

    //start main board
    List<CrawlerModel> listMainBoardAsus();


    // end mainboard
}
