package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Year;
import java.time.YearMonth;

@Entity
@Table
@Getter
@Setter
public class SearchStatisticYear extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "searchId")
    private Search search;

    @Column
    private int statisticYear;

    @Column
    private Long totalOfSearch = 1L;
}
