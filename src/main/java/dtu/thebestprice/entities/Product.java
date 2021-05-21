package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Indexed
public class Product extends BaseEntity {
    @Column
    @Field(store = Store.YES)
    private String title;

    @Column(columnDefinition = "TEXT")
    @Field(store = Store.YES)
    private String shortDescription;

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

    @Column(columnDefinition = "TINYINT(1) default 0")
    @Field(index = Index.YES, store = Store.YES)
    private boolean approve = false;

    @Column(columnDefinition = "TINYINT(1) default 1")
    @Field(index = Index.YES, store = Store.YES)
    private boolean enable = true;
}
