package dtu.thebestprice.entities;

import dtu.thebestprice.entities.base.BaseEntity;
import dtu.thebestprice.entities.enums.EUserStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class User extends BaseEntity {

    @Column(unique = true,length = 50)
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id",nullable = false,updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role_id",nullable = false,updatable = false)}
    )
    private Set<Role> roles;
}
