package estudos.spring.EstudosSpringBoot.service;

import estudos.spring.EstudosSpringBoot.DTO.ProducerPostRequest;
import estudos.spring.EstudosSpringBoot.DTO.ProducerPutRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.exception.BadRequestException;
import estudos.spring.EstudosSpringBoot.repository.AnimeRepository;
import estudos.spring.EstudosSpringBoot.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository producerRepository;
    private final AnimeRepository animeRepository;

    public Page<Producer> listAll(Pageable pageable) {
        return producerRepository.findAll(pageable);
    }

    public List<Producer> listAllNonPageable() {
        return producerRepository.findAll();
    }

    public List<Producer> findByName(String name) {
        return producerRepository.findByName(name);
    }

    public Producer findByIdOrThrowBadRequestException(Long id) {
        return producerRepository.findById(id).orElseThrow(() -> new BadRequestException("Producer not found"));

    }

    public Producer verifyIfProducerCanBeExcluded(Long id) {
        Producer producer = verifyIdIsValid(id);
        if (!animeRepository.findAllByProducerId(id).isEmpty())
            throw new BadRequestException("Producer cannot be excluded");
        return producer;
    }

    public Producer verifyIdIsValid(Long id) {
        if (id != null) return findByIdOrThrowBadRequestException(id);
        throw new BadRequestException("Producer id cannot be null");
    }

    public Producer save(ProducerPostRequest producerRequest) {
        Producer producer = Producer.builder().name(producerRequest.getName()).build();
        return producerRepository.save(producer);
    }

    public void delete(Long id) {
        animeRepository.findAllByProducerId(id);
        Producer producer = verifyIfProducerCanBeExcluded(id);

        producerRepository.delete(producer);
    }

    public void replace(ProducerPutRequest producerRequest) {
        Producer saveProducer = verifyIdIsValid(producerRequest.getId());
        Producer producer = Producer.builder().id(saveProducer.getId()).name(producerRequest.getName()).build();

        producerRepository.save(producer);
    }
}
