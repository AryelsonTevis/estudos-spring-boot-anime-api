package estudos.spring.EstudosSpringBoot.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostRequest {

    @NotEmpty(message = "The anime name cannot be empty")
    @Schema(description = "This is the Anime name", example = "Dragon Ball Z")
    private String name;
    @NotNull(message = "The producer_id cannot be empty")
    @Schema(description = "This is the Producer id for the foreign key", example = "1")
    private Long producer_id;

}
