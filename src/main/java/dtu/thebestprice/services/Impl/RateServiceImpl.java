package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Product;
import dtu.thebestprice.entities.Rating;
import dtu.thebestprice.entities.User;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.ProductRepository;
import dtu.thebestprice.repositories.RatingRepository;
import dtu.thebestprice.repositories.UserRepository;
import dtu.thebestprice.services.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RateServiceImpl implements RateService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Override
    @Transactional
    public ResponseEntity<Object> rating(Long userId, Long rate, Long productId) {
        User user = userRepository.getOne(userId);

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // nếu người dùng đã đánh giá rồi thì cập nhật rate
        if (ratingRepository.existsByProductAndUser(product,user)){
            Rating rating = ratingRepository.findByProductAndUser(product,user);
            rating.setRate(rate);
            ratingRepository.save(rating);

            return ResponseEntity.ok(new ApiResponse(true,"Bạn đã cập nhật đánh giá của mình"));
        }
        // nếu chưa đánh giá thì tạo mới
        else {
            Rating rating = new Rating();
            rating.setRate(rate);
            rating.setProduct(product);
            rating.setUser(user);
            ratingRepository.save(rating);
            return ResponseEntity.ok(new ApiResponse(true,"Đánh giá thành công"));
        }
    }
}
