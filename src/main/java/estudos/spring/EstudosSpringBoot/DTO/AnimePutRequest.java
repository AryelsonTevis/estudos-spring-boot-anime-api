package estudos.spring.EstudosSpringBoot.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePutRequest {
    private Long id;
    @NotEmpty(message = "The anime name cannot be empty")
    private String name;


}
