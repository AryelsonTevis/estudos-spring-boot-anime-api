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
public class ProducerPutRequest {
    @NotNull(message = "The producer id cannot be null")
    @Schema(description = "This is the Producer id",example = "1")
    private long id;
    @NotNull(message = "The producer name cannot be empty")
    @Schema(description = "This is the new name for Producer", example = "NHK")
    private String name;
}
