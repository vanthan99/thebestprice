package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Banner extends BaseEntity {

    @Column(length = 50)
    private String title;

    @Column(length = 100)
    private String description;

    @Column
    private String imageUrl;

    @Column
    private String redirectUrl;

    @Column(columnDefinition = "TINYINT(1) default 1")
    private boolean enable = true;
}
