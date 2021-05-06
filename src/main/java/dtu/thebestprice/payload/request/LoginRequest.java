package dtu.thebestprice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Không được để trống tên đăng nhập")
    @Size(min = 8,max = 16,message = "Tên đăng nhập từ 8-16 ký tự")
    private String username;

    @NotBlank(message = "Không được để trống mật khẩu")
    @Size(min = 8,max = 16,message = "Mật khẩu từ 8-16 ký tự")
    private String password;
}
