package estudos.spring.EstudosSpringBoot.repository;


import estudos.spring.EstudosSpringBoot.domain.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AnimeRepository extends JpaRepository<Anime, Long> {

    Page<Anime> findAll(Pageable pageable);


    List<Anime> findByName(String name);

}
