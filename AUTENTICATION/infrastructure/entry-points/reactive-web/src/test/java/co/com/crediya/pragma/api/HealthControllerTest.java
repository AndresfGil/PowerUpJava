package co.com.crediya.pragma.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Tests para el controlador de health check
 */
@WebFluxTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void health_ShouldReturnOkStatus() {
        webTestClient
                .get()
                .uri("/api/v1/health")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP")
                .jsonPath("$.service").isEqualTo("PowerUp Authentication Service")
                .jsonPath("$.version").isEqualTo("1.0.0")
                .jsonPath("$.timestamp").exists();
    }

    @Test
    void ping_ShouldReturnPongMessage() {
        webTestClient
                .get()
                .uri("/api/v1/ping")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("pong")
                .jsonPath("$.timestamp").exists();
    }
}
