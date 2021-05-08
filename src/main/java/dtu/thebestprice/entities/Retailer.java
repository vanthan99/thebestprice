package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Indexed
public class Retailer extends BaseEntity {
    @Column(length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String homePage;

    @Column
    private String logoImage;

    @Column
    private boolean approve = false;
}
