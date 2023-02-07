package transportation.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import users.UserResponseDto;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.UUID;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager ehCacheManager(
            @Value("${cache.heap_in_mb}") long cacheHeapSize,
            @Value("${cache.ttl_in_minutes}") long timeToLive,
            @Value("${cache.users_cache}") String userCache) {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();

        cacheManager.createCache(
                userCache,
                Eh107Configuration.fromEhcacheCacheConfiguration(
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                        UUID.class,
                                        UserResponseDto.class,
                                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                                .heap(cacheHeapSize, MemoryUnit.MB)
                                )
                                .withExpiry(
                                        ExpiryPolicyBuilder.timeToIdleExpiration(
                                                Duration.ofMinutes(timeToLive)
                                        )
                                )
                )
        );
        return cacheManager;
    }
}
