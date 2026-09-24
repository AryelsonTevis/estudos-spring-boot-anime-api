package estudos.spring.EstudosSpringBoot.DTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePostRequest {

    @NotEmpty(message = "The anime name cannot be empty")
    private String name;
    @NotNull(message = "The producer_id cannot be empty")
    private Long producer_id;

}
