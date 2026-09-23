package estudos.spring.EstudosSpringBoot.service;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;
import estudos.spring.EstudosSpringBoot.DTO.AnimePutRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.exception.BadRequestException;
import estudos.spring.EstudosSpringBoot.repository.AnimeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final ProducerService producerService;


    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }
    public List<Anime> listAllNonPageable() {
        return animeRepository.findAll();
    }


    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }

    @Transactional
    public Anime save(AnimePostRequest animeRequest) {

        Producer producer = producerService.findByIdOrThrowBadRequestException(animeRequest.getProducer_id());
        Anime anime = Anime.builder().name(animeRequest.getName()).producer(producer).build();

        return animeRepository.save(anime);

    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequest animeRequest) {
        Anime savedAnime = findByIdOrThrowBadRequestException(animeRequest.id());

        Anime anime = Anime.builder().id(savedAnime.getId()).name(animeRequest.name()).producer(savedAnime.getProducer()).build();


        animeRepository.save(anime);
    }

}
