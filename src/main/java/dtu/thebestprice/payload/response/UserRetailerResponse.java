package dtu.thebestprice.payload.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRetailerResponse {
    private Long id;
    private String username;
    private String fullName;
    private String address;
    private String email;
    private String phoneNumber;
    private LocalDate registerDay;
}
