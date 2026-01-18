package com.fullstack.taskservice.client;

import com.fullstack.shared.utils.dto.UserDto;
import com.fullstack.shared.security.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public UserServiceClient(RestTemplateBuilder builder, @Value("${user.service.url:http://user-service:8081}") String baseUrl) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .additionalInterceptors((request, body, execution) -> {
                    String token = CurrentUserProvider.getToken();
                    if (token != null && !token.trim().isEmpty()) {
                        request.getHeaders().set("Authorization", token);
                    }
                    return execution.execute(request, body);
                })
                .build();
        this.baseUrl = baseUrl;
    }

    public List<UserDto> getByCategory(String category) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path("/internal/users/by-category/")
                    .pathSegment(category)
                    .toUriString();
            ResponseEntity<UserDto[]> resp = restTemplate.getForEntity(url, UserDto[].class);
            UserDto[] body = resp.getBody();
            if (body == null) return Collections.emptyList();
            return Arrays.asList(body);
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
