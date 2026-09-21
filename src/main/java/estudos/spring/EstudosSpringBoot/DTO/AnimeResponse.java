package estudos.spring.EstudosSpringBoot.DTO;


import lombok.Builder;

@Builder
public record AnimeResponse(Long id,
                            String name,
                            String producer_name) {

}
