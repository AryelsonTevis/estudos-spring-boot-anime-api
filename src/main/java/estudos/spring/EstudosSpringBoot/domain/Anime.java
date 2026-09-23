package estudos.spring.EstudosSpringBoot.domain;

import jakarta.persistence.*;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//database generate the anime id
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "The anime name cannot be empty")
    private String name;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    @NotNull(message = "The producer id cannot be empty")
    private Producer producer;
}
