package dtu.thebestprice.converters;

import dtu.thebestprice.entities.Banner;
import dtu.thebestprice.payload.request.BannerRequest;
import dtu.thebestprice.payload.response.BannerResponse;
import org.springframework.stereotype.Component;

@Component
public class BannerConverter {
    public BannerResponse toBannerResponse(Banner banner) {
        BannerResponse response = new BannerResponse();

        response.setTitle(banner.getTitle());
        response.setDescription(banner.getDescription());
        response.setImageUrl(banner.getImageUrl());
        response.setRedirectUrl(banner.getRedirectUrl());

        response.setCreatedAt(banner.getCreatedAt());
        response.setUpdatedAt(banner.getUpdatedAt());

        return response;
    }

    public Banner toEntity(BannerRequest request){
        Banner banner = new Banner();

        banner.setTitle(request.getTitle());
        banner.setDescription(request.getDescription());
        banner.setImageUrl(request.getImageUrl());
        banner.setRedirectUrl(request.getRedirectUrl());

        return banner;
    }

    public Banner toEntity(BannerRequest request,Banner banner){

        banner.setTitle(request.getTitle());
        banner.setDescription(request.getDescription());
        banner.setImageUrl(request.getImageUrl());
        banner.setRedirectUrl(request.getRedirectUrl());

        return banner;
    }
}
