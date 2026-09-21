package estudos.spring.EstudosSpringBoot.service;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;
import estudos.spring.EstudosSpringBoot.DTO.AnimePutRequest;
import estudos.spring.EstudosSpringBoot.DTO.AnimeResponse;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.exception.BadRequestException;
import estudos.spring.EstudosSpringBoot.repository.AnimeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final ProducerService producerService;



    public List<AnimeResponse> listAll() {
        return animeRepository.findAll().stream().map(this::response).toList();
    }
    public List<AnimeResponse> findByName(String name) {
        return animeRepository.findByName(name).stream().map(this::response).toList();
    }

    public AnimeResponse response(Anime anime){
        return AnimeResponse.builder().id(anime.getId()).name(anime.getName()).producer_name(anime.getProducer().getName()).build();
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }

    @Transactional
    public AnimeResponse save(AnimePostRequest animeRequest) {

        Producer producer = producerService.findByIdOrThrowBadRequestException(animeRequest.getProducer_id());
        Anime anime = Anime.builder().name(animeRequest.getName()).producer(producer).build();

        return AnimeResponse.builder().id(anime.getId()).name(anime.getName()).producer_name(anime.getProducer().getName()).build();

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
