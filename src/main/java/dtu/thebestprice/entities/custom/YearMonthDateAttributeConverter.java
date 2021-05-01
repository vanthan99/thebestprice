package dtu.thebestprice.entities.custom;

import javax.persistence.AttributeConverter;
import javax.swing.plaf.BorderUIResource;
import java.sql.Date;
import java.time.Instant;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;

public class YearMonthDateAttributeConverter implements AttributeConverter<YearMonth, Date> {
    @Override
    public Date convertToDatabaseColumn(YearMonth attribute) {
        return Date.valueOf(attribute.atDay(1));
    }

    @Override
    public YearMonth convertToEntityAttribute(Date dbData) {
        return YearMonth
                .from(Instant.ofEpochMilli(dbData.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
    }
}
