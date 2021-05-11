package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Price extends BaseEntity {
    @Column
    private Long price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_retailer_id")
    private ProductRetailer productRetailer;

    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean active = false;
}
