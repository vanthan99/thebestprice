package dtu.thebestprice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberRequest {
    @Email(message = "Email không đúng định dạng")
    @Size(min = 5,max = 50,message = "Email từ 5-50 ký tự")
    private String email;
}
