package transportation.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

import javax.annotation.PostConstruct;


@Configuration
@RequiredArgsConstructor
@EnableWebFlux
public class WebClientConfiguration implements WebFluxConfigurer {
    @Value("${services.user-service-url}")
    private String userServiceUrl;

    private final ObjectMapper mapper;
    private ExchangeStrategies exchangeStrategies;

    @PostConstruct
    public void init() {
        mapper.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        mapper.registerModule(new JavaTimeModule());
        exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .jackson2JsonDecoder(new Jackson2JsonDecoder(mapper)))
                .build();
    }

    @Bean
    public UriBuilderFactory uriBuilder() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return factory;
    }

    @Bean
    public WebClient userServiceClient() {
        return WebClient.builder()
                .baseUrl(userServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }
}
