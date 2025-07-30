package ukathon.mayo.domain.reference;

import org.springframework.data.jpa.repository.JpaRepository;
import ukathon.mayo.domain.reference.entity.Channel;
import ukathon.mayo.domain.reference.entity.Reference;

import java.util.List;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
    List<Reference> findByChannel(Channel channel);
}
