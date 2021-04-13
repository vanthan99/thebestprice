package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Price extends BaseEntity {
    @Column
    private Long price;

    @Column
    private Long discountPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_retailer_id")
    private ProductRetailer productRetailer;
}
