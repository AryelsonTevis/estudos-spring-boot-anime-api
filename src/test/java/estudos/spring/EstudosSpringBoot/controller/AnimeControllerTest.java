package estudos.spring.EstudosSpringBoot.controller;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;
import estudos.spring.EstudosSpringBoot.DTO.AnimePutRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.service.AnimeService;
import estudos.spring.EstudosSpringBoot.utill.AnimeCreator;
import estudos.spring.EstudosSpringBoot.utill.AnimePostRequestCreator;
import estudos.spring.EstudosSpringBoot.utill.AnimePutRequestCreator;
import lombok.extern.log4j.Log4j2;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@Log4j2
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setUp() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequest.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequest.class));
        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage)
                .isNotNull()
                .hasSize(1);


        Assertions.assertThat(animePage.toList().getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeController.listAll().getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_Anime_WhenSuccessful() {
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime animeFound = animeController.findById(1).getBody();

        Assertions.assertThat(animeFound).isNotNull();

        Assertions.assertThat(animeFound.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animeList = animeController.findByName("Anime").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animeList.getFirst().getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of anime when is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());


        List<Anime> animeList = animeController.findByName("Anime").getBody();

        Assertions.assertThat(animeList)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {

        Anime anime = animeController.save(AnimePostRequestCreator.createAnimPostRequest()).getBody();


        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }
    @Test
    @DisplayName("replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful() {
        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestCreator.createAnimPutRequest())).doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestCreator.createAnimPutRequest());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }

    @Test
    @DisplayName("delete remove anime when successful")
    void delete_RemoveAnime_WhenSuccessful() {
        Assertions.assertThatCode(() -> animeController.delete(1)).doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }
}