package br.com.posjava.leochacarolli.it_inventory.batch;

import br.com.posjava.leochacarolli.it_inventory.catalog.model.Manufacturer;
import br.com.posjava.leochacarolli.it_inventory.catalog.repository.ManufacturerRepository;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManufacturerWriterConfig {

    @Bean
    public RepositoryItemWriter<Manufacturer> manufacturerWriter(
            ManufacturerRepository manufacturerRepository) {

        return new RepositoryItemWriterBuilder<Manufacturer>()
                .repository(manufacturerRepository)
                .build();
    }
}