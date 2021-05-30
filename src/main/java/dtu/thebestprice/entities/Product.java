package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import dtu.thebestprice.entities.interceptor.ProductIndexingInterceptor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Indexed(interceptor = ProductIndexingInterceptor.class)
public class Product extends BaseEntity {
    @Column
    @Field(store = Store.YES)
    private String title;

    @Column(columnDefinition = "TEXT")
    @Field(store = Store.YES)
    private String shortDescription;

    @Column
    private String code;

    @Column(columnDefinition = "TEXT")
    @Field(store = Store.YES)
    private String longDescription;

    // map to Image table
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Image> images;

    // map to Categories table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private Category category;

    // map to Brand table;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private Brand brand;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    @IndexedEmbedded(includeEmbeddedObjectId = true)
    private Set<ProductRetailer> productRetailers;

    @Column
    @Field(index = Index.NO, store = Store.YES)
    private Long viewCount = 0L;

    @Column(columnDefinition = "TINYINT(1) default 1")
    private boolean approve = true;

    @Column(columnDefinition = "TINYINT(1) default 1")
    private boolean enable = true;
}
