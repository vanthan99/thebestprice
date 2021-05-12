package dtu.thebestprice.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BannerResponse {
    private String title;
    private String description;
    private String imageUrl;
    private String redirectUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
