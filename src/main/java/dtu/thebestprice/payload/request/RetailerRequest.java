package dtu.thebestprice.payload.request;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RetailerRequest {
    @Size(message = "tên nhà cung cấp không quá 50 ký tự",max = 50,min = 5)
    @NotBlank(message = "Không được để trống tên")
    private String name;

    @NotBlank(message = "Không được để trống mô tả")
    @Size(min = 5,message = "Mô tả tối thiểu 5 ký tự")
    private String description;

    @NotBlank(message = "Không được để trống logo")
    @Size(min = 10,max = 255,message = "URL logo quy định có độ dài từ 10 - 255 ký tự")
    @URL(message = "Không đúng định dạng URL logo")
    private String logo;

    @Size(min = 10,max = 255,message = "URL trang chủ quy định có độ dài từ 10 - 255 ký tự")
    @NotBlank(message = "Không được để trống home page")
    @URL(message = "Không đúng định dạng URL homepage")
    private String homePage;

    private String userId;
}
