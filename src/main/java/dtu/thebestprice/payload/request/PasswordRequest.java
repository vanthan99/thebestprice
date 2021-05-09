package dtu.thebestprice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordRequest {
    @NotBlank(message = "mật khẩu hiện tại không được để trống")
    @Size(min = 8, max = 16, message = "Mật khẩu từ 8-16 ký tự")
    private String currentPassword;

    @NotBlank(message = "mật khẩu mới không được để trống")
    @Size(min = 8, max = 16, message = "Mật khẩu từ 8-16 ký tự")
    private String newPassword;
}
