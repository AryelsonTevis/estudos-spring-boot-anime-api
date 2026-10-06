package estudos.spring.EstudosSpringBoot.controller;

import estudos.spring.EstudosSpringBoot.DTO.ProducerPostRequest;
import estudos.spring.EstudosSpringBoot.DTO.ProducerPutRequest;
import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.domain.UserJava;
import estudos.spring.EstudosSpringBoot.service.ProducerService;
import estudos.spring.EstudosSpringBoot.utill.ProducerCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerPostRequestCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerPutRequestCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProducerControllerTest {
    @InjectMocks
    private ProducerController producerController;
    @Mock
    private ProducerService producerService;

    @BeforeEach
    void setUp() {
        PageImpl<Producer> producerPage = new PageImpl<>(List.of(ProducerCreator.createValidProducer()));
        BDDMockito.when(producerService.listAll(ArgumentMatchers.any())).thenReturn(producerPage);
        BDDMockito.when(producerService.listAllNonPageable())
                .thenReturn(List.of(ProducerCreator.createValidProducer()));

        BDDMockito.when(producerService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(ProducerCreator.createValidProducer());

        BDDMockito.when(producerService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(ProducerCreator.createValidProducer()));

        BDDMockito.when(producerService.save(ArgumentMatchers.any(ProducerPostRequest.class)))
                .thenReturn(ProducerCreator.createValidProducer());

        BDDMockito.doNothing().when(producerService).delete(ArgumentMatchers.anyLong());

        BDDMockito.doNothing().when(producerService).replace(ArgumentMatchers.any(ProducerPutRequest.class));

    }

    @Test
    @DisplayName("List returns list of producer inside page object when successful")
    void list_ReturnsOfProducerInsidePageObjectWhenSuccessful() {
        String expectName = ProducerCreator.createValidProducer().getName();

        Page<Producer> producerPage = producerController.list(null).getBody();

        Assertions.assertThat(producerPage)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(producerPage.toList().getFirst().getName()).isEqualTo(expectName);
    }

    @Test
    @DisplayName("ListAll returns list of producer when successful")
    void listAll_ReturnListOfProducers_WhenSuccessful() {
        String expectedName = ProducerCreator.createValidProducer().getName();

        List<Producer> producerList = producerController.listAll().getBody();

        Assertions.assertThat(producerList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(producerList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById return producer when successful")
    void findById_Producer_WhenSuccessful() {
        Long expectedId = ProducerCreator.createValidProducer().getId();

        Producer producerFound = producerController.findById(1).getBody();

        Assertions.assertThat(producerFound)
                .isNotNull();

        Assertions.assertThat(producerFound.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of producer when successful")
    void findByName_ReturnsListOfProducers_WhenSuccessful() {
        String expectedName = ProducerCreator.createProducer().getName();

        List<Producer> producerList = producerController.findByName(ProducerCreator.createValidProducer().getName()).getBody();

        Assertions.assertThat(producerList)
                .isNotEmpty()
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(producerList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("save returns producer when successful")
    void save_ReturnsProducer_WhenSuccessful() {
        Producer producer = producerController.save(ProducerPostRequestCreator.createProducerPostRequest()).getBody();

        Assertions.assertThat(producer).isNotNull().isEqualTo(ProducerCreator.createValidProducer());
    }

    @Test
    @DisplayName("delete remove producer when successful")
    void delete_RemoveProducer_WhenSuccessful() {
       Assertions.assertThatCode(()-> producerController.delete(1)).doesNotThrowAnyException();

        ResponseEntity<Void> entity = producerController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace update producer when successful")
        void replace_UpdateProducer_WhenSuccessful() {
        Assertions.assertThatCode(()-> producerController.replace(ProducerPutRequestCreator.createProducerPostRequest())).doesNotThrowAnyException();

        ResponseEntity<Void> entity = producerController.replace(ProducerPutRequestCreator.createProducerPostRequest());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}