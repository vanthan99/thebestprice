package dtu.thebestprice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TheBestPriceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TheBestPriceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TheBestPriceApplication.class);
    }
}
