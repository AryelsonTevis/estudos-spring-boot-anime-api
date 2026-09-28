package estudos.spring.EstudosSpringBoot.repository;


import estudos.spring.EstudosSpringBoot.domain.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AnimeRepository extends JpaRepository<Anime, Long> {

    Page<Anime> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM anime WHERE name LIKE %:name%", nativeQuery = true)
    List<Anime> findByName(@Param("name") String name);


    @Query(value = "SELECT * FROM anime WHERE producer_id = :producer_id",nativeQuery = true)
    List<Anime> findAllByProducerId(@Param("producer_id") long producer_id);
}
