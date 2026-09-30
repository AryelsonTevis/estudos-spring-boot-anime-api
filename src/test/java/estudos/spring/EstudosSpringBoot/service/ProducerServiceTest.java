package estudos.spring.EstudosSpringBoot.service;

import estudos.spring.EstudosSpringBoot.domain.Producer;
import estudos.spring.EstudosSpringBoot.exception.BadRequestException;
import estudos.spring.EstudosSpringBoot.repository.AnimeRepository;
import estudos.spring.EstudosSpringBoot.repository.ProducerRepository;
import estudos.spring.EstudosSpringBoot.utill.ProducerCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerPostRequestCreator;
import estudos.spring.EstudosSpringBoot.utill.ProducerPutRequestCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService producerService;
    @Mock
    private ProducerRepository producerRepositoryMock;
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Producer> producers = new PageImpl<>(List.of(ProducerCreator.createValidProducer()));
        BDDMockito.when(producerRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(producers);

        BDDMockito.when(producerRepositoryMock.findAll())
                .thenReturn(List.of(ProducerCreator.createValidProducer()));

        BDDMockito.when(producerRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(ProducerCreator.createValidProducer()));

        BDDMockito.when(producerRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(ProducerCreator.createValidProducer()));

        BDDMockito.when(producerRepositoryMock.save(ArgumentMatchers.any(Producer.class)))
                .thenReturn(ProducerCreator.createValidProducer());

        BDDMockito.doNothing().when(producerRepositoryMock).delete(ArgumentMatchers.any(Producer.class));

    }

    @Test
    @DisplayName("List returns list of producer inside page object when successful")
    void list_ReturnsListOfProducerInsidePageObject_WhenSuccessful() {
        String expectedName = ProducerCreator.createProducer().getName();
        Page<Producer> producerPage = producerService.listAll(PageRequest.of(0, 1));

        Assertions.assertThat(producerPage).isNotNull().hasSize(1);
        Assertions.assertThat(producerPage.toList().getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("List returns list of producer when successful")
    void list_ReturnsListOfProducer_WhenSuccessful() {
        String expectedName = ProducerCreator.createProducer().getName();
        List<Producer> producerPage = producerService.listAllNonPageable();

        Assertions.assertThat(producerPage).isNotNull().hasSize(1);
        Assertions.assertThat(producerPage.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns a list of producer when successful")
    void findByName_ReturnsListOfProducer_WhenSuccessful() {
        String expectedName = ProducerCreator.createProducer().getName();


        List<Producer> producerList = producerService.findByName("Producer");

        Assertions.assertThat(producerList)
                .isNotNull()
                .isNotEmpty();

        Assertions.assertThat(producerList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of producer when is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenProducerIsNotFound() {
        BDDMockito.when(producerRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());


        List<Producer> producerList = producerService.findByName("Producer");

        Assertions.assertThat(producerList)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("FindById return producer when successful")
    void findByIdOrThrowBadRequestException_ReturnProducer_WhenSuccessful() {
        Long expectedId = ProducerCreator.createValidProducer().getId();

        Producer producerFound = producerService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(producerFound)
                .isNotNull();

        Assertions.assertThat(producerFound.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }


    @Test
    @DisplayName("FindById return BadRequestException when producer not found")
    void findByIdOrThrowBadRequestException_ThrowBadRequestException_WhenProducerNotFound() {

        BDDMockito.when(producerRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> producerService.findByIdOrThrowBadRequestException(1L))
                .isInstanceOf(BadRequestException.class);
    }


    @Test
    @DisplayName("save returns producer when successful")
    void save_ReturnsProducer_WhenSuccessful() {
        Producer savedProducer = producerService.save(ProducerPostRequestCreator.createProducerPostRequest());

        Assertions.assertThat(savedProducer).isNotNull().isEqualTo(ProducerCreator.createValidProducer());
    }

    @Test
    @DisplayName("deleted remove producer when successful")
    void deleted_RemoveProducer_WhenSuccessful() {
        producerService.save(ProducerPostRequestCreator.createProducerPostRequest());
        Assertions.assertThatCode(() -> producerService.delete(1L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("replace update producer when successful")
    void replace_UpdateProducer_WhenSuccessful() {
        Assertions.assertThatCode(() -> producerService.replace(ProducerPutRequestCreator.createProducerPostRequest())).doesNotThrowAnyException();
    }

}