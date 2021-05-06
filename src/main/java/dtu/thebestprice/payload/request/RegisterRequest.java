package dtu.thebestprice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLInsert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "tên đăng nhập không được để trống")
    private String username;

    @NotBlank(message = "mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "tên đầy đủ không được để trống")
    private String fullName;

    @Email(message = "Email không đúng định dạng")
    @Size(min = 5,max = 50,message = "email từ 5-50 ký tự")
    private String email;

    @NotBlank(message = "địa chỉ không đươc để trống")
    private String address;
    @Size(min = 9, max = 11, message = "Số điện thoại từ 9-11 ký tự")
    private String phoneNumber;
}
