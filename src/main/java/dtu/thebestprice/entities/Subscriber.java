package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Subscriber extends BaseEntity {
    @Column(length = 50)
    private String email;
}
