package estudos.spring.EstudosSpringBoot.controller;

import estudos.spring.EstudosSpringBoot.DTO.AnimePostRequest;
import estudos.spring.EstudosSpringBoot.DTO.AnimePutRequest;
import estudos.spring.EstudosSpringBoot.domain.Anime;
import estudos.spring.EstudosSpringBoot.service.AnimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor //constructor with finals
public class AnimeController {

    private final AnimeService animeService;


    @GetMapping
    @Operation(summary = "List all animes paginated", description = "The default size is 20, use the parameter size to change the default value")
    public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(animeService.listAll(pageable));
    }
    @GetMapping(path = "/all")
    @Operation(summary = "List all animes in the list")
    public ResponseEntity<List<Anime>> listAll() {
        return ResponseEntity.ok(animeService.listAllNonPageable());
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }
    @GetMapping(path = "by-id/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Return Anime")
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
      log.info("User Details: '{}'", userDetails);
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }
    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequest animeRequest) {
        return new ResponseEntity <> (animeService.save(animeRequest),HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/admin/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Successful Operation"),
            @ApiResponse(responseCode = "400",description = "When Anime Not Exist in the Database")
    })
    public ResponseEntity<Void> delete(@PathVariable long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Successful Operation"),
            @ApiResponse(responseCode = "400",description = "When Anime Not Exist in the Database")
    })
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequest animeRequest) {
        animeService.replace(animeRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
