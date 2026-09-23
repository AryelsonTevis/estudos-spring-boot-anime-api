package estudos.spring.EstudosSpringBoot.client;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/6", Anime.class);
        log.info("Get for Entity '{}'", entity);

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 6);
        log.info("Get For Object'{}'", object);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info("Get For Object array'{}'", Arrays.toString(animes));

        ResponseEntity<List<Anime>> animesList = new RestTemplate()
                .exchange("http://localhost:8080/animes/all", HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("Get exchange'{}'", animesList.getBody());


        AnimePostRequest animePostRequest = new AnimePostRequest();
        animePostRequest.setName("Kingdom");
        animePostRequest.setProducer_id(6L);
        Anime animePost = new RestTemplate().postForObject("http://localhost:8080/animes", animePostRequest, Anime.class);

        log.info("Post For object '{}'", animePost);

        AnimePostRequest animePostRequestSecond = new AnimePostRequest();
        animePostRequestSecond.setName("Vagabond");
        animePostRequestSecond.setProducer_id(11L);
        ResponseEntity<Anime> animeSaved = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.POST,
                new HttpEntity<>(animePostRequestSecond),
                Anime.class);

        log.info("Post exchange '{}'", animeSaved);

        Anime animeToBeUpdated = animeSaved.getBody();
        animeToBeUpdated.setName("Vagabond 2");

        ResponseEntity<Void> animeReplace = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.PUT,
                new HttpEntity<>(animeToBeUpdated),
                Void.class);

        log.info("Put exchange '{}'", animeReplace);


        ResponseEntity<Void> animeDeleted = new RestTemplate().exchange("http://localhost:8080/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class, animeToBeUpdated.getId());

        log.info("Delete exchange '{}'", animeDeleted);
    }


}
