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
public class AnimePutRequest {
    @NotNull(message = "The anime id cannot be null")
    @Schema(description = "This is the Anime id",example = "1")
    private Long id;
    @NotEmpty(message = "The anime name cannot be empty")
    @Schema(description = "This is the new name for Anime", example = "NHK")
    private String name;


}
