package estudos.spring.EstudosSpringBoot.integration;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;
import estudos.spring.EstudosSpringBoot.DTO.ProducerPostRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.exception.BadRequestException;
import estudos.spring.EstudosSpringBoot.repository.AnimeRepository;
import estudos.spring.EstudosSpringBoot.repository.ProducerRepository;
import estudos.spring.EstudosSpringBoot.utill.AnimeCreator;
import estudos.spring.EstudosSpringBoot.utill.AnimePostRequestCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerPostRequestCreator;
import estudos.spring.EstudosSpringBoot.wrapper.PageableResponse;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureTestRestTemplate
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProducerControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ProducerRepository producerRepository;
    @Autowired
    private AnimeRepository animeRepository;


    @Test
    @DisplayName("List returns list of producer inside page object when successful")
    void list_ReturnsListOfProducersInsidePageObject_WhenSuccessful() {

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        PageableResponse<Producer> producerPage = testRestTemplate.exchange("/producers", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Producer>>() {
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

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        String expectedName = savedProducer.getName();

        List<Producer> producerList = testRestTemplate.exchange("/producers/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Producer>>() {
        }).getBody();

        Assertions.assertThat(producerList)
                .isNotNull()
                .hasSize(1);


        Assertions.assertThat(producerList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns producer when successful")
    void findById_Producer_WhenSuccessful() {

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());


        Long expectedId = savedProducer.getId();

        Producer producerFound = testRestTemplate.getForObject("producers/{id}", Producer.class, expectedId);

        Assertions.assertThat(producerFound).isNotNull();

        Assertions.assertThat(producerFound.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of producer when successful")
    void findByName_ReturnsListOfProducer_WhenSuccessful() {

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        String expectedName = savedProducer.getName();

        String url = String.format("/producers/find?name=%s", expectedName);

        List<Producer> producerList = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Producer>>() {
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

        List<Producer> producerList = testRestTemplate.exchange("/producers/find?name=Black", HttpMethod.GET, null, new ParameterizedTypeReference<List<Producer>>() {
        }).getBody();

        Assertions.assertThat(producerList)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns producer when successful")
    void save_ReturnsProducer_WhenSuccessful() {
        ProducerPostRequest producerPostRequest = ProducerPostRequestCreator.createProducerPostRequest();
        ResponseEntity<Producer> producerResponseEntity = testRestTemplate.postForEntity("/producers", producerPostRequest, Producer.class);


        Assertions.assertThat(producerResponseEntity).isNotNull();

        Assertions.assertThat(producerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(producerResponseEntity.getBody()).isNotNull();

        Assertions.assertThat(producerResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace update producer when successful")
    void replace_UpdateProducer_WhenSuccessful() {
        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        savedProducer.setName("New name");
        ResponseEntity<Void> producerResponseEntity = testRestTemplate.exchange("/producers", HttpMethod.PUT, new HttpEntity<>(savedProducer), Void.class);


        Assertions.assertThat(producerResponseEntity).isNotNull();

        Assertions.assertThat(producerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete remove anime when successful")
    void delete_RemoveAnime_WhenSuccessful() {

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        ResponseEntity<Void> producerResponseEntity = testRestTemplate.exchange("/producers/{id}", HttpMethod.DELETE, null, Void.class, savedProducer.getId());

        Assertions.assertThat(producerResponseEntity).isNotNull();

        Assertions.assertThat(producerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Do not exclude the producer when there is a connection via foreign key")
    void doNot_DeleteProducer_WhenThereIsConnectionViaForeignKey() {

        Producer savedProducer = producerRepository.save(ProducerCreator.createProducer());

        AnimePostRequest animePostRequest = AnimePostRequestCreator.createAnimePostRequest();

       testRestTemplate.postForEntity("/animes", animePostRequest, Anime.class);


        ResponseEntity<Void> producerResponseEntity = testRestTemplate.exchange("/producers/{id}", HttpMethod.DELETE, null, Void.class, savedProducer.getId());

        Assertions.assertThat(producerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}
