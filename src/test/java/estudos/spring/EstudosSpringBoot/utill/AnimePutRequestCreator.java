package estudos.spring.EstudosSpringBoot.utill;

import estudos.spring.EstudosSpringBoot.DTO.AnimePutRequest;

public class AnimePutRequestCreator {
    public static AnimePutRequest createAnimPutRequest() {

        return new AnimePutRequest(AnimeCreator
                .createValidAnime().getId(),
                AnimeCreator.createValidAnime().getName());
    }
}
