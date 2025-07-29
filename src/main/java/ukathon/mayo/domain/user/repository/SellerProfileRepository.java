package ukathon.mayo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ukathon.mayo.domain.user.entity.SellerProfile;

public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {
    boolean existsByUserId(Long userId);
}