package br.com.posjava.leochacarolli.it_inventory.service;

import br.com.posjava.leochacarolli.it_inventory.exception.DuplicateLocationException;
import br.com.posjava.leochacarolli.it_inventory.exception.InvalidLocationDataException;
import br.com.posjava.leochacarolli.it_inventory.exception.LocationNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.model.Location;
import br.com.posjava.leochacarolli.it_inventory.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocationService {

    private final Map<Long, Location> locations = new HashMap<>();
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationById(Long id){
        if (locations.containsKey(id)) {
            return locations.get(id);
        } else {
            throw new LocationNotFoundException("Localização não encontrada para o ID: " + id);
        }
    }

    public Location addLocation(Location location) {
        if (location == null) {
            throw new InvalidLocationDataException("A localização não pode ser nulo");
        }

        if (location.getName() == null || location.getName().isBlank()) {
            throw new InvalidLocationDataException("A localização não pode ser nula, vazia ou conter apenas espaços");
        }

        if (locations.containsKey(location.getId())) {
            throw new DuplicateLocationException("Já existe uma localização com ID: " + location.getId());
        }

        return locationRepository.save(location);
    }
}
