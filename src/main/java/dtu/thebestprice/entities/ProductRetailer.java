package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table
@AllArgsConstructor
@NoArgsConstructor
@Indexed
public class ProductRetailer extends BaseEntity {
    @Column
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "retailer_id")
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private Retailer retailer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(columnDefinition = "TINYINT(1) default 1")
    private boolean enable = true;

    @Column(columnDefinition = "TINYINT(1) default 1")
    private boolean approve = true;
}
