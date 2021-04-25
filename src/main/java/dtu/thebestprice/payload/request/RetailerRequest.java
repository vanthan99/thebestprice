package dtu.thebestprice.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RetailerRequest {
    private String id;

    @Size(message = "tên nhà cung cấp không quá 50 ký tự",max = 50)
    @NotBlank(message = "Không được để trống tên")
    private String name;

    @NotBlank(message = "Không được để trống mô tả")
    private String description;

    @Size(min = 10,max = 255,message = "URL logo quy định có độ dài từ 10 - 255 ký tự")
    @NotBlank(message = "Không được để trống logo")
    private String logo;

    @Size(min = 10,max = 255,message = "URL trang chủ quy định có độ dài từ 10 - 255 ký tự")
    @NotBlank(message = "Không được để trống home page")
    private String homePage;
}
