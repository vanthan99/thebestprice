package dtu.thebestprice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "tên đầy đủ không được để trống")
    private String fullName;

    @NotBlank(message = "địa chỉ không đươc để trống")
    private String address;

    @Size(min = 9, max = 11, message = "Số điện thoại từ 9-11 ký tự")
    private String phoneNumber;
}
