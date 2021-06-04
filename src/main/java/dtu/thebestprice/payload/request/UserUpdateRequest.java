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
    @NotBlank(message = "Tên đầy đủ không được để trống")
    @Size(min = 5,max = 30,message = "Tên có đồ dài từ 5-50 ký tự")
    private String fullName;

    @NotBlank(message = "Địa chỉ không đươc để trống")
    @Size(min = 5,max = 50,message = "địa chỉ có đồ dài từ 5-50 ký tự")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(min = 9, max = 11, message = "Số điện thoại từ 9-11 ký tự")
    private String phoneNumber;
}
