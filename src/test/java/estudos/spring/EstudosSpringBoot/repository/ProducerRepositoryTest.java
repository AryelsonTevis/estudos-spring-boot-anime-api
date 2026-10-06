package estudos.spring.EstudosSpringBoot.repository;

import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.utill.ProducerCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class ProducerRepositoryTest {
    @Autowired
    private ProducerRepository producerRepository;

    @Test
    @DisplayName("Save persists producer when Successful")
    void save_PersistProducer_WhenSuccessful() {

        Producer producerToBeSaved = ProducerCreator.createProducer();


        Producer savedProducer = this.producerRepository.save(ProducerCreator.createProducer());

        Assertions.assertThat(savedProducer).isNotNull();

        Assertions.assertThat(savedProducer.getId()).isNotNull();

        Assertions.assertThat(savedProducer.getName()).isEqualTo(producerToBeSaved.getName());
    }
    @Test
    @DisplayName("Save Updates producer when Successful")
    void save_UpdatesProducer_WhenSuccessful() {
        Producer producerToBeSaved = ProducerCreator.createProducer();

        Producer producerSaved = this.producerRepository.save(producerToBeSaved);

        producerSaved.setName("Overlord");

        Producer producerUpdated = this.producerRepository.save(producerSaved);

        Assertions.assertThat(producerUpdated).isNotNull();

        Assertions.assertThat(producerUpdated.getId()).isNotNull();

        Assertions.assertThat(producerUpdated.getName()).isEqualTo(producerSaved.getName());

    }

    @Test
    @DisplayName("Deleted remove producer when Successful")
    void deleted_RemovesProducer_WhenSuccessful() {
        Producer producerToBeSaved = ProducerCreator.createProducer();

        Producer producerSaved = this.producerRepository.save(producerToBeSaved);

        this.producerRepository.delete(producerSaved);

        Optional<Producer> producerOptional = this.producerRepository.findById(producerSaved.getId());

        Assertions.assertThat(producerOptional).isEmpty();

    }
}