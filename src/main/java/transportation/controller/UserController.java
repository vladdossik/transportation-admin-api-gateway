package transportation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pagination.SortOrder;
import reactor.core.publisher.Mono;
import users.UserPageResponse;
import users.UserPostDto;
import users.UserPutDto;
import users.UserResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.UUID;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final WebClient userServiceClient;

    @Operation(summary = "Добавить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @PostMapping("/add")
    public Mono<UserResponseDto> add(@Valid @RequestBody UserPostDto userPostDto) {
        return userServiceClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("users/add")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userPostDto))
                .retrieve()
                .bodyToMono(UserResponseDto.class);
    }

    @Operation(summary = "Получить список пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = UserPageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @GetMapping("/all")
    public Mono<UserPageResponse> getAll(
            @RequestParam(defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam String direction,
            @RequestParam(required = false) String firstNameFilter,
            @RequestParam(required = false) String lastNameFilter
    ) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("users/all")
                        .queryParam("pageNumber", pageNumber)
                        .queryParam("pageSize", pageSize)
                        .queryParam("sortBy", sortBy)
                        .queryParam("direction", direction)
                        .queryParam("firstNameFilter", firstNameFilter)
                        .queryParam("lastNameFilter", lastNameFilter)
                        .build())
                .retrieve()
                .bodyToMono(UserPageResponse.class);
    }

    @Operation(summary = "Получить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @GetMapping("/{externalId}")
    public Mono<UserResponseDto> getById(@PathVariable UUID externalId) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("users/{externalId}")
                        .build(externalId))
                .retrieve()
                .bodyToMono(UserResponseDto.class);
    }

    @Operation(summary = "Удалить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @DeleteMapping("/{externalId}/delete")
    public Mono<String> deleteById(@PathVariable UUID externalId) {
        return userServiceClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("users/{externalId}/delete")
                        .build(externalId))
                .retrieve()
                .bodyToMono(String.class);
    }

    @Operation(summary = "Удалить всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @DeleteMapping("/delete")
    public Mono<String> deleteAll() {
        return userServiceClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("users/delete")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    @Operation(summary = "Обновить данные пользователея")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @PutMapping("/{externalId}")
    public Mono<UserResponseDto> update(@PathVariable UUID externalId, @Valid @RequestBody UserPutDto userDto) {
        return userServiceClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("users/{externalId}")
                        .build(externalId))
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userDto))
                .retrieve()
                .bodyToMono(UserResponseDto.class);
    }

    @Operation(summary = "Востановить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @PostMapping("/{externalId}")
    public Mono<UserResponseDto> reestablish(@PathVariable UUID externalId) {
        return userServiceClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("users/{externalId}")
                        .build(externalId))
                .retrieve()
                .bodyToMono(UserResponseDto.class);
    }
}
