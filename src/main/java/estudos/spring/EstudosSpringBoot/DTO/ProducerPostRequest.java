package estudos.spring.EstudosSpringBoot.DTO;


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
    private String name;

}
