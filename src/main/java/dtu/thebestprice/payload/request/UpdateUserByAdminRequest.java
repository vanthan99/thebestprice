package dtu.thebestprice.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateUserByAdminRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 8,max = 16,message = "Tên đăng nhập từ 8-16 ký tự")
    private String username;

//    @NotBlank(message = "mật khẩu không được để trống")
//    @Size(min = 8,max = 16,message = "Mật khẩu từ 8-16 ký tự")
//    private String password;

    @NotBlank(message = "Tên đầy đủ không được để trống")
    @Size(min = 5,max = 50,message = "Tên đầy đủ phải từ 5-50 ký tự")
    private String fullName;

    @Email(message = "Email không đúng định dạng")
    @Size(min = 5,max = 50,message = "email từ 5-50 ký tự")
    private String email;

    @NotBlank(message = "Địa chỉ không đươc để trống")
    @Size(min = 5,max = 50,message = "Địa chỉ phải từ 5-50 ký tự")
    private String address;

    @NotBlank(message = "Không được để trống số điện thoại")
    @Size(min = 9, max = 11, message = "Số điện thoại từ 9-11 ký tự")
    private String phoneNumber;
}
