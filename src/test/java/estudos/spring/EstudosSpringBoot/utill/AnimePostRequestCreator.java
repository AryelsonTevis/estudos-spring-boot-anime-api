package estudos.spring.EstudosSpringBoot.utill;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;

public class AnimePostRequestCreator {
    public static AnimePostRequest createAnimPostRequest() {

        return AnimePostRequest.builder().name(AnimeCreator.createAnimeToBeSave().getName())
                .producer_id(1L).build();
    }
}
