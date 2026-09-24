package estudos.spring.EstudosSpringBoot.utill;

import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.repository.ProducerRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AnimeCreator {
    private static ProducerRepository producerRepository;

    public static Anime createAnimeToBeSave() {

        return Anime.builder().name("Hajime no Ippo").build();
    }
    public static Anime createValidAnime() {

        return Anime.builder().id(1L).name("Hajime no Ippo").build();
    }
    public static Anime createValidAnimeUpdated() {

        return Anime.builder().id(1L).name("Hajime no Ippo").build();
    }

    
}
