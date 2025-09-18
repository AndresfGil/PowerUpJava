package co.com.crediya.pragma.api;

import co.com.crediya.pragma.api.docs.HealthControllerDocs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador personalizado para el endpoint de health check
 * Proporciona información adicional sobre el estado del servicio
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class HealthController implements HealthControllerDocs {

    /**
     * Endpoint personalizado de health check
     *
     * @return
     */
    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, Object>>> health() {
        log.info("Health endpoint accessed");
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("service", "PowerUp Authentication Service");
        healthInfo.put("timestamp", LocalDateTime.now());
        healthInfo.put("version", "1.0.0");
        healthInfo.put("description", "Servicio de autenticación PowerUp funcionando correctamente");
        
        return Mono.just(ResponseEntity.ok(healthInfo));
    }

    /**
     * Endpoint simple de ping para verificar conectividad
     *
     * @return
     */
    @GetMapping("/ping")
    public Mono<ResponseEntity<Map<String, String>>> ping() {
        log.info("Ping endpoint accessed");
        Map<String, String> response = new HashMap<>();
        response.put("message", "pong");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return Mono.just(ResponseEntity.ok(response));
    }

    /**
     * Endpoint de prueba para verificar la configuración de seguridad
     * @return ResponseEntity con mensaje de prueba
     */
    @GetMapping("/test")
    public Mono<ResponseEntity<Map<String, String>>> test() {
        log.info("Test endpoint accessed - Security configuration working");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test endpoint working - Security configuration is correct");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return Mono.just(ResponseEntity.ok(response));
    }
}
