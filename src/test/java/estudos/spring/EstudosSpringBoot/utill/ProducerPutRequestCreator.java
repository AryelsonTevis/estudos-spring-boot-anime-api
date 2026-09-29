package estudos.spring.EstudosSpringBoot.utill;

import estudos.spring.EstudosSpringBoot.DTO.ProducerPutRequest;
import estudos.spring.EstudosSpringBoot.domain.Producer;

public class ProducerPutRequestCreator {
    public static ProducerPutRequest createProducerPostRequest() {

        Producer validProducer = ProducerCreator.createValidProducer();

        return  ProducerPutRequest.builder().name(validProducer.getName()).id(validProducer.getId()).build();
    }
}
