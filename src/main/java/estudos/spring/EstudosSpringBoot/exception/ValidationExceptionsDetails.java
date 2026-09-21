package estudos.spring.EstudosSpringBoot.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionsDetails extends ExceptionDetails{
    private String fields;
    private String fieldMessage;
}
