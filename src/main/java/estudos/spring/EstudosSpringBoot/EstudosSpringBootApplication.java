package estudos.spring.EstudosSpringBoot;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.micrometer.metrics.autoconfigure.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EstudosSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstudosSpringBootApplication.class, args);
    }
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCustomizer(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);

    }

}
