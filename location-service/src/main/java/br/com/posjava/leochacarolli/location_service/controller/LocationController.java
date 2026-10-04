package br.com.posjava.leochacarolli.location_service.controller;

import br.com.posjava.leochacarolli.location_service.dto.LocationRequestDTO;
import br.com.posjava.leochacarolli.location_service.dto.LocationResponseDTO;
import br.com.posjava.leochacarolli.location_service.model.Location;
import br.com.posjava.leochacarolli.location_service.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(
        name = "Localizações",
        description = "Endpoints responsáveis pelo gerenciamento das localizações"
)
@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(
            summary = "Listar localizações",
            description = "Retorna todas as localizações cadastradas"
    )
    @GetMapping
    public List<LocationResponseDTO> getAllLocations() {
        return locationService.getAll()
                .stream()
                .map(LocationResponseDTO::new)
                .toList();
    }


    @Operation(
            summary = "Buscar localização por ID",
            description = "Retorna uma localização a partir do seu identificador"
    )
    @GetMapping("/{id}")
    public LocationResponseDTO getLocationById(
            @Parameter(description = "ID da localização", example = "1")
            @PathVariable Long id) {

                Location location = locationService.getById(id);

                return new LocationResponseDTO(location);
    }

    @Operation(
            summary = "Criar localização",
            description = "Cadastra uma nova localização"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponseDTO createLocation(
            @Valid @RequestBody LocationRequestDTO request) {

        Location location = locationService.create(request);

        return new LocationResponseDTO(location);
    }
}