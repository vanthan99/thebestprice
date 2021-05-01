package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import dtu.thebestprice.entities.custom.YearMonthDateAttributeConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table
@Getter
@Setter
public class SearchStatisticYearMonth extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "searchId")
    private Search search;

    @Column
    private int statisticMonth;

    @Column
    private int statisticYear;

    @Column
    private Long totalOfSearch = 1L;
}
