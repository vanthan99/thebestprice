package dtu.thebestprice.entities;

import io.swagger.annotations.ApiKeyAuthDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class VerificationToken {
    // 5 phút
    private static final long EXPIRATION = 300000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 36)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column
    private Date expiryDate;

    public Date calculateExpiryDate() {
        Date now = new Date();
        return new Date(now.getTime() + EXPIRATION);
    }

}
