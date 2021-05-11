package dtu.thebestprice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordByAdminRequest {
    @NotBlank(message = "mật khẩu mới không được để trống")
    @Size(min = 8, max = 16, message = "Mật khẩu từ 8-16 ký tự")
    private String newPassword;
}
