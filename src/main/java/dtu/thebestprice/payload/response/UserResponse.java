package dtu.thebestprice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String username;
    private String fullName;
    private String address;
    private String email;
    private String phoneNumber;
}
