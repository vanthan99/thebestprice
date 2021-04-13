package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import dtu.thebestprice.entities.enums.ERole;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {
    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private ERole name;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "roles")
    private Set<User> users;
}
