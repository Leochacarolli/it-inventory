package br.com.posjava.leochacarolli.it_inventory.location.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "location-service",
        url = "${services.location.url}"
)
public interface LocationClient {

    @GetMapping("/locations/{id}")
    LocationClientResponseDTO getLocationById(
            @PathVariable Long id
    );
}