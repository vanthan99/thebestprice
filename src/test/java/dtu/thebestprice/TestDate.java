package dtu.thebestprice;

import java.time.LocalDate;
import java.util.Date;

public class TestDate {
    public static void main(String[] args) throws InterruptedException {
        Date nowDate = new Date();
        System.out.println("nowDate. = " + nowDate.getTime());
        Thread.sleep(3000);
        System.out.println("new Date().getTime() = " + new Date().getTime());
    }
}
