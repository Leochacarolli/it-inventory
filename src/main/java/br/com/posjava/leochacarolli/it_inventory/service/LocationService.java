package br.com.posjava.leochacarolli.it_inventory.service;

import br.com.posjava.leochacarolli.it_inventory.exception.DuplicateLocationException;
import br.com.posjava.leochacarolli.it_inventory.exception.InvalidLocationDataException;
import br.com.posjava.leochacarolli.it_inventory.exception.LocationNotFoundException;
import br.com.posjava.leochacarolli.it_inventory.model.Location;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocationService {

    private final Map<Long, Location> locations = new HashMap<>();

    public Location getLocationById(Long id){
        if (locations.containsKey(id)) {
            return locations.get(id);
        } else {
            throw new LocationNotFoundException("Localização não encontrada para o ID: " + id);
        }
    }

    public void addLocation(Location location) {
        if (location == null) {
            throw new InvalidLocationDataException("A localização não pode ser nulo");
        }

        if (location.getId() == null) {
            throw new InvalidLocationDataException("O ID da localização não pode ser nulo");
        }

        if (location.getName() == null || location.getName().isBlank()) {
            throw new InvalidLocationDataException("A localização não pode ser nula, vazia ou conter apenas espaços");
        }

        if (locations.containsKey(location.getId())) {
            throw new DuplicateLocationException("Já existe uma localização com ID: " + location.getId());
        }

        locations.put(location.getId(), location);
    }
}
