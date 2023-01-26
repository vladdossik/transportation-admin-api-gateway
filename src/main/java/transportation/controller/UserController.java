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

    @Operation(summary = "Удалить пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @DeleteMapping("/{id}/delete")
    public Mono<Void> deleteById(@PathVariable Long id) {
        return userServiceClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("users/{id}/delete")
                        .build(id))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Operation(summary = "Обновить данные пользователея")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Ошибочный запрос"),
            @ApiResponse(responseCode = "409", description = "Запись уже существует"),
            @ApiResponse(responseCode = "503", description = "Сервис временно недоступен")
    })
    @PutMapping("/{id}")
    public Mono<UserPutDto> update(@PathVariable Long id, @Valid @RequestBody UserPutDto userDto) {
        return userServiceClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("users/{id}")
                        .build(id))
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userDto))
                .retrieve()
                .bodyToMono(UserPutDto.class);
    }
}
