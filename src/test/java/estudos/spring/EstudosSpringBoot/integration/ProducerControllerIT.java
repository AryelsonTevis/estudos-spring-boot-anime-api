package estudos.spring.EstudosSpringBoot.integration;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;
import estudos.spring.EstudosSpringBoot.DTO.ProducerPostRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.domain.UserJava;
import estudos.spring.EstudosSpringBoot.exception.BadRequestException;
import estudos.spring.EstudosSpringBoot.repository.AnimeRepository;
import estudos.spring.EstudosSpringBoot.repository.ProducerRepository;
import estudos.spring.EstudosSpringBoot.repository.UserJavaRepository;
import estudos.spring.EstudosSpringBoot.utill.AnimeCreator;
import estudos.spring.EstudosSpringBoot.utill.AnimePostRequestCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerPostRequestCreator;
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
public class ProducerControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUserCreator")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdminCreator")
    private TestRestTemplate testRestTemplateRoleAdmin;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private UserJavaRepository userJavaRepository;

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
    public void setup() {
        userJavaRepository.deleteAll();
        user = UserJava.builder().name("User").userName("Teste").password("{bcrypt}$2a$12$nJxqyRzv7w9R4vXbfGBnu.JC2/1UuYpD2rSeWltC66/BjqXljRf9C").authorities("USER").build();
        admin = UserJava.builder().name("Owner").userName("Dev").password("{bcrypt}$2a$12$nJxqyRzv7w9R4vXbfGBnu.JC2/1UuYpD2rSeWltC66/BjqXljRf9C").authorities("ADMIN,USER").build();
    }
    @Test
    @DisplayName("List returns list of producer inside page object when successful")
    void list_ReturnsListOfProducersInsidePageObject_WhenSuccessful() {
        userJavaRepository.save(user);

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        PageableResponse<Producer> producerPage = testRestTemplateRoleUser.exchange("/producers", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Producer>>() {
        }).getBody();

        Assertions.assertThat(producerPage)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(producerPage.stream().toList().getFirst().getName()).isEqualTo(savedProducer.getName());
    }

    @Test
    @DisplayName("ListAll returns list of producer when successful")
    void listAll_ReturnsListOfProducers_WhenSuccessful() {
        userJavaRepository.save(user);

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        String expectedName = savedProducer.getName();

        List<Producer> producerList = testRestTemplateRoleUser.exchange("/producers/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Producer>>() {
        }).getBody();

        Assertions.assertThat(producerList)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(producerList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns producer when successful")
    void findById_Producer_WhenSuccessful() {
        userJavaRepository.save(admin);

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        Long expectedId = savedProducer.getId();

        Producer producerFound = testRestTemplateRoleAdmin.getForObject("producers/{id}", Producer.class, expectedId);

        Assertions.assertThat(producerFound).isNotNull();

        Assertions.assertThat(producerFound.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of producer when successful")
    void findByName_ReturnsListOfProducer_WhenSuccessful() {
        userJavaRepository.save(user);

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        String expectedName = savedProducer.getName();

        String url = String.format("/producers/find?name=%s", expectedName);

        List<Producer> producerList = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Producer>>() {
        }).getBody();

        Assertions.assertThat(producerList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(producerList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of producer when is not found")
    void findByName_ReturnsEmptyListOfProducer_WhenProducerIsNotFound() {
        userJavaRepository.save(user);

        List<Producer> producerList = testRestTemplateRoleUser.exchange("/producers/find?name=Black", HttpMethod.GET, null, new ParameterizedTypeReference<List<Producer>>() {
        }).getBody();

        Assertions.assertThat(producerList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns producer when successful")
    void save_ReturnsProducer_WhenSuccessful() {
        userJavaRepository.save(user);

        ProducerPostRequest producerPostRequest = ProducerPostRequestCreator.createProducerPostRequest();

        ResponseEntity<Producer> producerResponseEntity = testRestTemplateRoleUser.postForEntity("/producers", producerPostRequest, Producer.class);

        Assertions.assertThat(producerResponseEntity).isNotNull();

        Assertions.assertThat(producerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(producerResponseEntity.getBody()).isNotNull();

        Assertions.assertThat(producerResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace update producer when successful")
    void replace_UpdateProducer_WhenSuccessful() {
        userJavaRepository.save(user);

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        savedProducer.setName("New name");

        ResponseEntity<Void> producerResponseEntity = testRestTemplateRoleUser.exchange("/producers", HttpMethod.PUT, new HttpEntity<>(savedProducer), Void.class);

        Assertions.assertThat(producerResponseEntity).isNotNull();

        Assertions.assertThat(producerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete remove producer when successful")
    void delete_RemoveProduce_WhenSuccessful() {
        userJavaRepository.save(admin);

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        ResponseEntity<Void> producerResponseEntity = testRestTemplateRoleAdmin.exchange("/producers/{id}", HttpMethod.DELETE, null, Void.class, savedProducer.getId());

        Assertions.assertThat(producerResponseEntity).isNotNull();

        Assertions.assertThat(producerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Do not exclude the producer when there is a connection via foreign key")
    void doNot_DeleteProducer_WhenThereIsConnectionViaForeignKey() {
        userJavaRepository.save(admin);

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        AnimePostRequest animePostRequest = AnimePostRequestCreator.createAnimePostRequest();

       testRestTemplateRoleAdmin.postForEntity("/animes", animePostRequest, Anime.class);

        ResponseEntity<Void> producerResponseEntity = testRestTemplateRoleAdmin.exchange("/producers/{id}", HttpMethod.DELETE, null, Void.class, savedProducer.getId());

        Assertions.assertThat(producerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
