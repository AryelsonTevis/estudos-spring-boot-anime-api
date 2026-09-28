package estudos.spring.EstudosSpringBoot.utill;

import estudos.spring.EstudosSpringBoot.DTO.AnimePutRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;

public class AnimePutRequestCreator {
    public static AnimePutRequest createAnimePutRequest() {
        Anime validAnime = AnimeCreator.createValidAnime();
        return  AnimePutRequest.builder().name(validAnime.getName()).id(validAnime.getId()).build();
    }
}
