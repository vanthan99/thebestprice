package dtu.thebestprice.crawlers.model;

import lombok.Data;

import java.util.List;

@Data
public class CrawlerModel {
    private String title;
    private String shortDesc;
    private String longDesc;
    private List<String> images;
    private String code;
    private String url;
    private Long price;
}
