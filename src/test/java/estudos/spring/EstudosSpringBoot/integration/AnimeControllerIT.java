package estudos.spring.EstudosSpringBoot.integration;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.domain.UserJava;
import estudos.spring.EstudosSpringBoot.repository.AnimeRepository;
import estudos.spring.EstudosSpringBoot.repository.ProducerRepository;
import estudos.spring.EstudosSpringBoot.repository.UserJavaRepository;
import estudos.spring.EstudosSpringBoot.utill.AnimeCreator;
import estudos.spring.EstudosSpringBoot.utill.AnimePostRequestCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerCreator;
import estudos.spring.EstudosSpringBoot.wrapper.PageableResponse;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureTestRestTemplate
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUserCreator")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdminCreator")
    private TestRestTemplate testRestTemplateRoleAdmin;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private UserJavaRepository userJavaRepository;

    private Producer producer;

    private UserJava user;

    private UserJava admin;

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUserCreator")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().baseUri("http://localhost:" + port).basicAuthentication("Teste", "RainSun");

            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdminCreator")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().baseUri("http://localhost:" + port).basicAuthentication("Dev", "RainSun");

            return new TestRestTemplate(restTemplateBuilder);
        }
    }


    @BeforeEach
    public void setUp() {
        producer = producerRepository.save(ProducerCreator.createProducer());
        userJavaRepository.deleteAll();
        user = UserJava.builder().name("User").userName("Teste").password("{bcrypt}$2a$12$nJxqyRzv7w9R4vXbfGBnu.JC2/1UuYpD2rSeWltC66/BjqXljRf9C").authorities("USER").build();
        admin = UserJava.builder().name("Owner").userName("Dev").password("{bcrypt}$2a$12$nJxqyRzv7w9R4vXbfGBnu.JC2/1UuYpD2rSeWltC66/BjqXljRf9C").authorities("ADMIN,USER").build();
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        userJavaRepository.save(user);

        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();

        setProducer(animeToBeSave);


        Anime savedAnime = animeRepository.save(animeToBeSave);

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange("/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
        }).getBody();

        Assertions.assertThat(animePage)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {
        userJavaRepository.save(user);



        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();

        setProducer(animeToBeSave);

        Anime savedAnime = animeRepository.save(animeToBeSave);

        String expectedName = savedAnime.getName();

        List<Anime> animeanimeList = testRestTemplateRoleUser.exchange("/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        Assertions.assertThat(animeanimeList)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(animeanimeList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_Anime_WhenSuccessful() {
        userJavaRepository.save(user);

        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();


        setProducer(animeToBeSave);

        Anime savedAnime = animeRepository.save(animeToBeSave);

        Long expectedId = savedAnime.getId();

        Anime animeFound = testRestTemplateRoleUser.getForObject("animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(animeFound).isNotNull();

        Assertions.assertThat(animeFound.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        userJavaRepository.save(user);


        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();

        setProducer(animeToBeSave);

        Anime savedAnime = animeRepository.save(animeToBeSave);

        String expectedName = savedAnime.getName();

        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animeList = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of anime when is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        userJavaRepository.save(user);


        List<Anime> animeList = testRestTemplateRoleUser.exchange("/animes/find?name=Black", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        userJavaRepository.save(user);


        AnimePostRequest animePostRequest = AnimePostRequestCreator.createAnimePostRequest();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.postForEntity("/animes", animePostRequest, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();

        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful() {
        userJavaRepository.save(user);


        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();

        setProducer(animeToBeSave);

        Anime savedAnime = animeRepository.save(animeToBeSave);

        savedAnime.setName("New name");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes", HttpMethod.PUT, new HttpEntity<>(savedAnime), Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete remove anime when successful")
    void delete_RemoveAnime_WhenSuccessful() {
        userJavaRepository.save(admin);


        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();

        setProducer(animeToBeSave);

        Anime savedAnime = animeRepository.save(animeToBeSave);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange("/animes/admin/{id}", HttpMethod.DELETE, null, Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 when user is not admin")
    void delete_Returns403_WhenUserIsNotAdmin() {
        userJavaRepository.save(admin);
        userJavaRepository.save(user);


        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();

        setProducer(animeToBeSave);

        Anime savedAnime = animeRepository.save(animeToBeSave);

        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/admin/{id}", HttpMethod.DELETE, null, Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    void setProducer(Anime anime) {
        anime.setProducer(producer);
    }
}