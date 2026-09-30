package estudos.spring.EstudosSpringBoot.service;

import estudos.spring.EstudosSpringBoot.domain.UserJava;
import estudos.spring.EstudosSpringBoot.repository.UserJavaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Log4j2
@Service
@RequiredArgsConstructor
public class UserJavaDetailsService implements UserDetailsService {
    private final UserJavaRepository userJavaRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        List<UserJava> javaUserFound = Optional.ofNullable(userJavaRepository.findByUserName(username))
                .orElseThrow(() -> new UsernameNotFoundException("Java user not found"));

        UserJava user = javaUserFound.getFirst();


        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }
}
