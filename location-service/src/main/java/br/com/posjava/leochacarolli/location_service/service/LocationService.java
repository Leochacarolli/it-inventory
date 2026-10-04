package br.com.posjava.leochacarolli.location_service.service;

import br.com.posjava.leochacarolli.location_service.dto.LocationRequestDTO;
import br.com.posjava.leochacarolli.location_service.model.Location;
import br.com.posjava.leochacarolli.location_service.repository.LocationRepository;
import br.com.posjava.leochacarolli.location_service.exception.LocationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location create(LocationRequestDTO request) {

        Location location = new Location(
                null,
                request.isActive(),
                request.getName(),
                request.getFloor(),
                request.getDescription()
        );

        return locationRepository.save(location);
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location getById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->
                        new LocationNotFoundException(
                                "Localização não encontrada para o ID: " + id
                        )
                );
    }
}