package dtu.thebestprice.entities.base;

import dtu.thebestprice.securities.MyUserDetails;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.NumericField;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
public abstract class BaseEntity {
//    @Id
//    @GeneratedValue(generator = "uuid4")
//    @GenericGenerator(name = "UUID", strategy = "uuid4")
//    @Type(type = "org.hibernate.type.UUIDCharType")
//    @Column(columnDefinition = "CHAR(36)")
//    private UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TINYINT(1) default 0")
    private boolean deleteFlg = false;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @PrePersist
    public void prePersist(){
        this.createdBy = auditorProvider();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedBy = auditorProvider();
    }

    private String auditorProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "unknown";
        }
        return authentication.getName();
    }
}
