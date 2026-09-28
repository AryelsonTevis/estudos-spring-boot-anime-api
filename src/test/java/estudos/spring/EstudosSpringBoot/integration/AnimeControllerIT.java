package estudos.spring.EstudosSpringBoot.integration;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.repository.AnimeRepository;
import estudos.spring.EstudosSpringBoot.repository.ProducerRepository;
import estudos.spring.EstudosSpringBoot.utill.AnimeCreator;
import estudos.spring.EstudosSpringBoot.utill.AnimePostRequestCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerCreator;
import estudos.spring.EstudosSpringBoot.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureTestRestTemplate
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private ProducerRepository producerRepository;
    private Producer producer;

    @BeforeEach
    public void setUp(){
        producer = producerRepository.save(ProducerCreator.createProducer());

    }
    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();
        setProducer(animeToBeSave);
        Anime savedAnime = animeRepository.save(animeToBeSave);

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
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
        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();
        setProducer(animeToBeSave);
        Anime savedAnime = animeRepository.save(animeToBeSave);

        String expectedName = savedAnime.getName();

       List<Anime> animeanimeList = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        Assertions.assertThat(animeanimeList)
                .isNotNull()
                .hasSize(1);


        Assertions.assertThat(animeanimeList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_Anime_WhenSuccessful() {
        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();
        setProducer(animeToBeSave);
        Anime savedAnime = animeRepository.save(animeToBeSave);


        Long expectedId = savedAnime.getId();

        Anime animeFound = testRestTemplate.getForObject("animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(animeFound).isNotNull();

        Assertions.assertThat(animeFound.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();
        setProducer(animeToBeSave);
        Anime savedAnime = animeRepository.save(animeToBeSave);

        String expectedName = savedAnime.getName();

        String url = String.format("/animes/find?name=%s",expectedName);

        List<Anime> animeList = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
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





        List<Anime> animeList = testRestTemplate.exchange("/animes/find?name=Black", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        AnimePostRequest animePostRequest = AnimePostRequestCreator.createAnimePostRequest();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequest, Anime.class);


        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();

        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful() {
        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();
        setProducer(animeToBeSave);
        Anime savedAnime = animeRepository.save(animeToBeSave);
        savedAnime.setName("New name");
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes",HttpMethod.PUT,new HttpEntity<>(savedAnime) ,Void.class);


        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete remove anime when successful")
    void delete_RemoveAnime_WhenSuccessful() {
        Anime animeToBeSave = AnimeCreator.createAnimeToBeSave();
        setProducer(animeToBeSave);
        Anime savedAnime = animeRepository.save(animeToBeSave);

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",HttpMethod.DELETE,null ,Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    void setProducer(Anime anime) {
        anime.setProducer(producer);
    }
}
