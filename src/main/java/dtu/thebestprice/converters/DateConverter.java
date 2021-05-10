package dtu.thebestprice.converters;

import dtu.thebestprice.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Component
public class DateConverter {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public LocalDate toStartDay(String date) {
        LocalDate startDay;
        try {
            startDay = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Ngày bắt đầu không hợp lệ. nhập đúng định dạng dd/MM/yyyy");
        }

        if (startDay.getYear() <= 2020 && startDay.getYear() > 2050)
            throw new RuntimeException("năm bắt đầu phải chỉ được phép nằm trong khoảng 2021 - 2050");

        return startDay;
    }

    public LocalDate toEndDay(String date) {
        LocalDate endDay;
        try {
            endDay = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Ngày kết thúc không hợp lệ. nhập đúng định dạng dd/MM/yyyy");
        }

        if (endDay.getYear() <= 2020 && endDay.getYear() > 2050)
            throw new RuntimeException("năm kết thúc phải chỉ được phép nằm trong khoảng 2021 - 2050");

        return endDay;
    }

    public int toStartYear(String strYear) {
        int year;
        try {
            year = Integer.parseInt(strYear);
            if (year <= 2020 || year > 2050)
                throw new RuntimeException("Năm bắt đầu chỉ được nằm trong khoảng 2021 - 2050");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("năm bắt đầu phải là số nguyên");
        }
        return year;
    }

    public int toEndYear(String strYear) {
        int year;
        try {
            year = Integer.parseInt(strYear);
            if (year <= 2020 || year > 2050)
                throw new RuntimeException("Năm kết thúc chỉ được nằm trong khoảng 2021 - 2050");

        } catch (NumberFormatException e) {
            throw new NumberFormatException("năm kết thúc phải là số nguyên");
        }
        return year;
    }

    public int toQuarter(String strQuarter) {
        int quarter;

        try {
            quarter = Integer.parseInt(strQuarter);

            if (quarter < 1 || quarter > 4)
                throw new RuntimeException("Quý chỉ được nằm trong khoảng từ 1 tới 4");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("quý phải là số nguyên");
        }
        return quarter;
    }

    public int toYear(String strYear) {
        int year;

        try {
            year = Integer.parseInt(strYear);

            if (year <= 2020 || year > 2050)
                throw new RuntimeException("Năm phải lớn hơn 2020 và bé hơn 2050");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Năm là số nguyên");
        }
        return year;
    }
}
