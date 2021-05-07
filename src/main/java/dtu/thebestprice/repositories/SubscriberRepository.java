package dtu.thebestprice.repositories;

import dtu.thebestprice.entities.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    List<Subscriber> findByDeleteFlgFalse();

    boolean existsByEmail(String email);
}
