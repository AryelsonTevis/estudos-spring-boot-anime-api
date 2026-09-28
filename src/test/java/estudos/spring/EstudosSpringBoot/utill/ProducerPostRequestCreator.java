package estudos.spring.EstudosSpringBoot.utill;

import estudos.spring.EstudosSpringBoot.DTO.ProducerPostRequest;

public class ProducerPostRequestCreator {
    public static ProducerPostRequest createProducerPostRequest() {


        return  ProducerPostRequest.builder().name("Baki").build();
    }
}
