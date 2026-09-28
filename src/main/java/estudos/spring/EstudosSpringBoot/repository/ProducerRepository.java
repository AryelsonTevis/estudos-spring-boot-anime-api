package estudos.spring.EstudosSpringBoot.repository;

import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    Page<Producer> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM producer WHERE name LIKE %:name%", nativeQuery = true)
    List<Producer> findByName(@Param("name") String name);

}
