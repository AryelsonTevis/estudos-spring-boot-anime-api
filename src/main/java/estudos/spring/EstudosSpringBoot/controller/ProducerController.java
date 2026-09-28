package estudos.spring.EstudosSpringBoot.controller;

import estudos.spring.EstudosSpringBoot.DTO.ProducerPostRequest;
import estudos.spring.EstudosSpringBoot.DTO.ProducerPutRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("producers")
@Log4j2
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @GetMapping
    public ResponseEntity<Page<Producer>> list(Pageable pageable) {
        return ResponseEntity.ok(producerService.listAll(pageable));
    }
    @GetMapping(path = "/all")
    public ResponseEntity<List<Producer>> list(){
        return ResponseEntity.ok(producerService.listAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Producer> findById(@PathVariable long id){
        return ResponseEntity.ok(producerService.findByIdOrThrowBadRequestException(id));
    }
    @GetMapping(path = "/find")
    public ResponseEntity<List<Producer>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(producerService.findByName(name));
    }


    @PostMapping
    public ResponseEntity<Producer> save(@RequestBody ProducerPostRequest producerRequest){
        return new ResponseEntity<>(producerService.save(producerRequest), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        producerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody ProducerPutRequest producerRequest){
        producerService.replace(producerRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
