package estudos.spring.EstudosSpringBoot.repository;

import estudos.spring.EstudosSpringBoot.domain.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
}
