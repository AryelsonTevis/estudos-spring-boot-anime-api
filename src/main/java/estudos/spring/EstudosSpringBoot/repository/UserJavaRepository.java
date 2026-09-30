package estudos.spring.EstudosSpringBoot.repository;


import estudos.spring.EstudosSpringBoot.domain.UserJava;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserJavaRepository extends JpaRepository<UserJava, Long> {

    List<UserJava> findByUserName(String username);

}
