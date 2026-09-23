package estudos.spring.EstudosSpringBoot.repository;


import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

@Log4j2
@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private ProducerRepository producerRepository;

    @Test
    @DisplayName("Save persists anime when Successful")
    void save_PersistAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();

        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        Assertions.assertThat(animeSaved).isNotNull();

        Assertions.assertThat(animeSaved.getId()).isNotNull();

        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Save Updates anime when Successful")
    void save_UpdatesAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();

        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        animeSaved.setName("Overlord");

        Anime animeUpdated = this.animeRepository.save(animeSaved);

        Assertions.assertThat(animeUpdated).isNotNull();

        Assertions.assertThat(animeUpdated.getId()).isNotNull();

        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());

    }

    @Test
    @DisplayName("Deleted remove anime when Successful")
    void deleted_RemovesAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();

        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        this.animeRepository.delete(animeSaved);

        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

        Assertions.assertThat(animeOptional).isEmpty();

    }

    @Test
    @DisplayName("Find By Name returns list of anime when Successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();

        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        String name = animeSaved.getName();

        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty().contains(animeSaved);

    }

    @Test
    @DisplayName("Find By Name returns empty list when no anime is found")
    void findByName_ReturnsEmptyList_WhenAnimeNotFound() {

        String name = "Teste";

        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes).isEmpty();

    }

    @Test
    @DisplayName("Find By id returns object Anime when Successful")
    void findById_ReturnsObjectAnime_WhenSuccessful() {
        Anime animeToBeSaved = createAnime();

        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        Long id = animeSaved.getId();

        Optional<Anime> animeOptional = this.animeRepository.findById(id);

        Assertions.assertThat(animeOptional).isNotEmpty();

    }

    @Test
    @DisplayName("Find By id returns empty when no id is found")
    void findById_ReturnsEmpty_WhenIdNotFound() {

        Long id = 1L;

        Optional<Anime> animeOptional = this.animeRepository.findById(id);

        Assertions.assertThat(animeOptional).isEmpty();

    }


    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowConstraintViolationException_WhenNameIsEmpty() {
        Anime animeToBeSaved = new Anime();
        Assertions.assertThatThrownBy(() ->this.animeRepository.save(animeToBeSaved))
                .isInstanceOf(ConstraintViolationException.class);

    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when producer_id is empty")
    void save_ThrowConstraintViolationException_WhenProducer_idIsEmpty() {
        Anime animeToBeSaved = Anime.builder().name("test").build();

        Assertions.assertThatThrownBy(() ->this.animeRepository.save(animeToBeSaved))
                .isInstanceOf(ConstraintViolationException.class);


    }

    private Anime createAnime() {
        Producer producer = createProducer();
        return Anime.builder().name("Hajime no Ippo").producer(producer).build();
    }
    private Producer createProducer(){

        return this.producerRepository.save(Producer.builder().name("Baki").build());
    }

}