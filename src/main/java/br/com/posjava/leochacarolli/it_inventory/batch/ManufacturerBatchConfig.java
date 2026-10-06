package br.com.posjava.leochacarolli.it_inventory.batch;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ManufacturerBatchConfig {

    @Bean
    public FlatFileItemReader<ManufacturerCsvRow> manufacturerReader() {

        return new FlatFileItemReaderBuilder<ManufacturerCsvRow>()
                .name("manufacturerReader")
                .resource(new ClassPathResource("batch/manufacturers.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("name", "country", "active")
                .fieldSetMapper(fieldSet ->
                        new ManufacturerCsvRow(
                                fieldSet.readString("name"),
                                fieldSet.readString("country"),
                                fieldSet.readBoolean("active")
                        )
                )
                .build();
    }
}