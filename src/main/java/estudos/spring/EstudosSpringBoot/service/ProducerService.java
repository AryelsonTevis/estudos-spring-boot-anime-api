package estudos.spring.EstudosSpringBoot.service;

import estudos.spring.EstudosSpringBoot.DTO.ProducerPostRequest;
import estudos.spring.EstudosSpringBoot.DTO.ProducerPutRequest;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.exception.BadRequestException;
import estudos.spring.EstudosSpringBoot.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository producerRepository;

    public List<Producer> listAll(){
        return producerRepository.findAll();
    }
    public Producer findByIdOrThrowBadRequestException(Long id){
        return producerRepository.findById(id).orElseThrow(() -> new BadRequestException("Producer not found"));

    }

    public Producer save(ProducerPostRequest producerRequest){
        Producer producer = Producer.builder().name(producerRequest.name()).build();
        return producerRepository.save(producer);
    }
    public void delete(Long id){
        producerRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(ProducerPutRequest producerRequest){
        Producer saveProducer = findByIdOrThrowBadRequestException(producerRequest.id());
        Producer producer = Producer.builder().id(saveProducer.getId()).name(producerRequest.name()).build();

        producerRepository.save(producer);
    }
}
