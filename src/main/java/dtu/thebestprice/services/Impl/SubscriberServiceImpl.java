package dtu.thebestprice.services.Impl;

import dtu.thebestprice.entities.Subscriber;
import dtu.thebestprice.payload.request.SubscriberRequest;
import dtu.thebestprice.payload.response.ApiResponse;
import dtu.thebestprice.repositories.SubscriberRepository;
import dtu.thebestprice.services.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    @Autowired
    SubscriberRepository subscriberRepository;


    @Override
    public ResponseEntity<Object> create(SubscriberRequest request) {
        if (request.getEmail() == null)
            throw new RuntimeException("Không được để trống email");
        if (subscriberRepository.existsByEmailAndDeleteFlgFalse(request.getEmail()))
            throw new RuntimeException("Email này đã tồn tại");

        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(request.getEmail());

        subscriberRepository.save(subscriber);

        return ResponseEntity.ok(new ApiResponse(true,"Gửi email thành công"));
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable) {
        return ResponseEntity.ok(subscriberRepository.findByDeleteFlgFalse());
    }
}
