package softuni.exam.repository;

import com.sun.jdi.connect.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Offer;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Integer> {

    Optional<Offer> findByAddedOn(LocalDateTime addedOn);
}
