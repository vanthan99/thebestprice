package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
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
@Indexed
@AnalyzerDef(name = "vietnameseAnalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
        @TokenFilterDef(factory = StandardFilterFactory.class),
        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
})
public class Product extends BaseEntity {
    @Column
    @Field(index = Index.YES, analyze = Analyze.YES)
    @Analyzer(definition = "vietnameseAnalyzer")
    private String title;

    @Column(columnDefinition = "TEXT")
    @Field(index = Index.YES, analyze = Analyze.YES)
    @Analyzer(definition = "vietnameseAnalyzer")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    @Field(index = Index.YES, analyze = Analyze.YES)
    @Analyzer(definition = "vietnameseAnalyzer")
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
