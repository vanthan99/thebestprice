package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
public class ViewCountStatistic extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product product;

    @Column
    private LocalDate statisticDay;

    @Column
    private Long viewCount = 0L;
}
