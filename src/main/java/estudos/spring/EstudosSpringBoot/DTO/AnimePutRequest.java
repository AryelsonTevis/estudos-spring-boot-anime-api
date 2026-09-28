package estudos.spring.EstudosSpringBoot.DTO;

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
    private Long id;
    @NotEmpty(message = "The anime name cannot be empty")
    private String name;


}
