package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import dtu.thebestprice.entities.enums.ERole;
import dtu.thebestprice.entities.enums.EUserStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true, length = 50)
    private String username;

    @Column(length = 60)
    private String password;

    @Column(length = 50)
    private String fullName;

    @Column(length = 50)
    private String address;

    @Column(length = 50)
    private String email;

    @Column(length = 11)
    private String phoneNumber;

    @Column
    private boolean enable = false;

    @Column
    private boolean approve = false;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ERole role;
}
