package dtu.thebestprice.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BannerResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String redirectUrl;
    private boolean enable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
