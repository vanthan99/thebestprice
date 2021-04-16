package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String longDescription;

    // map to Image table
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "product")
    private List<Image> images;

    // map to Categories table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    // map to Brand table;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "product")
    private Set<ProductRetailer> productRetailers;

    @Column
    private Long viewCount = 0L;
}
