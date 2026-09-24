package estudos.spring.EstudosSpringBoot.utill;

import estudos.spring.EstudosSpringBoot.domain.Producer;

public class ProducerCreator {

    public static Producer createProducer(){

        return Producer.builder().name("Baki").build();
    }
    public static Producer createValidProducer() {

        return Producer.builder().id(1L).name("Baki").build();
    }
    public static Producer createValidUpdatedProducer() {

        return Producer.builder().id(1L).name("Baki").build();
    }
}
