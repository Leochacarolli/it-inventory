package br.com.posjava.leochacarolli.location_service.config;

import br.com.posjava.leochacarolli.location_service.model.Location;
import br.com.posjava.leochacarolli.location_service.repository.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Loader implements CommandLineRunner {

    private final LocationRepository locationRepository;

    public Loader(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) {

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