package dtu.thebestprice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dtu.thebestprice.entities.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.search.annotations.Field;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@JsonIgnoreProperties({
        "category",
            "createdAt",
        "updatedAt",
        "createdBy",
        "updatedBy",
        "deleteFlg"
})
@Indexed
public class Category extends BaseEntity {
    @Column(length = 50)
    @Field
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category category;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "category")
    private Set<Category> categories;
}
