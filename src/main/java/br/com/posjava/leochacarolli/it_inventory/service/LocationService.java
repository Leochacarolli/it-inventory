package br.com.posjava.leochacarolli.it_inventory.service;

import br.com.posjava.leochacarolli.it_inventory.exception.DuplicateLocationException;
import br.com.posjava.leochacarolli.it_inventory.exception.InvalidLocationDataException;
import br.com.posjava.leochacarolli.it_inventory.exception.LocationNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.model.Location;
import br.com.posjava.leochacarolli.it_inventory.repository.LocationRepository;
import org.springframework.stereotype.Service;


@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException("Localização não encontrada para o ID: " + id));
    }

    public Location addLocation(Location location) {
        if (location == null) {
            throw new InvalidLocationDataException("A localização não pode ser nulo");
        }

        if (location.getName() == null || location.getName().isBlank()) {
            throw new InvalidLocationDataException("A localização não pode ser nula, vazia ou conter apenas espaços");
        }

        return locationRepository.save(location);
    }
}
