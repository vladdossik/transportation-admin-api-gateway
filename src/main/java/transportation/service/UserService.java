package transportation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import users.UserResponseDto;

import javax.cache.CacheManager;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient userServiceClient;
    private final CacheManager ehCacheManager;
    @Value("${cache.users_cache}")
    private String userCache;

    public Mono<UserResponseDto> getUser(UUID externalId) {
        var user = ehCacheManager.getCache(userCache).get(externalId);
        System.out.println(user);
        if (user == null) {
            return userServiceClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("users/{externalId}")
                            .build(externalId))
                    .retrieve()
                    .bodyToMono(UserResponseDto.class)
                    .flatMap(userResponseDto ->
                            saveUser(Mono.just(userResponseDto)));
        }
        return Mono.just((UserResponseDto) user);
    }


    public Mono<UserResponseDto> saveUser(Mono<UserResponseDto> userResponseDto) {
        return userResponseDto.doOnSuccess(dto ->
                ehCacheManager.getCache(userCache).put(dto.getExternalId(), dto));
    }
}
