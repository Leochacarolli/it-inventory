package br.com.posjava.leochacarolli.location_service.config;

import br.com.posjava.leochacarolli.location_service.model.Location;
import br.com.posjava.leochacarolli.location_service.repository.LocationRepository;
import br.com.posjava.leochacarolli.location_service.service.LocationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Loader implements CommandLineRunner {

    private final LocationRepository locationRepository;
    private final LocationService locationService;

    public Loader(LocationRepository locationRepository, LocationService locationService) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!locationService.getAll().isEmpty()) {
            System.out.println("Location Service já possui dados cadastrados. Carga inicial ignorada.");
            return;
        }

        Location humanResources = new Location(
                null,
                true,
                "Human Resources",
                13,
                ""
        );

        Location noc = new Location(
                null,
                true,
                "NOC",
                1,
                ""
        );

        Location comercial = new Location(
                null,
                true,
                "Comercial",
                13,
                ""
        );

        locationRepository.save(humanResources);
        locationRepository.save(noc);
        locationRepository.save(comercial);

        System.out.println("Localizações cadastradas no location-service:");

        locationRepository.findAll()
                .forEach(System.out::println);
    }
}