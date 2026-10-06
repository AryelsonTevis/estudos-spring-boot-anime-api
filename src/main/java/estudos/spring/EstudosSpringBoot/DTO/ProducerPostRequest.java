package estudos.spring.EstudosSpringBoot.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProducerPostRequest {

    @NotNull(message = "The producer name cannot be empty")
    @Schema(description = "This is the Producer name",example = "Toei")
    private String name;

}
