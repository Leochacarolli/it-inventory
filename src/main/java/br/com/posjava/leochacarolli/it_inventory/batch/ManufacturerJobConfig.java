package br.com.posjava.leochacarolli.it_inventory.batch;

import br.com.posjava.leochacarolli.it_inventory.catalog.model.Manufacturer;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManufacturerJobConfig {

    @Bean
    public Step manufacturerImportStep(
            JobRepository jobRepository,
            FlatFileItemReader<ManufacturerCsvRow> manufacturerReader,
            ManufacturerProcessor manufacturerProcessor,
            RepositoryItemWriter<Manufacturer> manufacturerWriter) {

        return new StepBuilder(
                "manufacturerImportStep",
                jobRepository
        )
                .<ManufacturerCsvRow, Manufacturer>chunk(2)
                .reader(manufacturerReader)
                .processor(manufacturerProcessor)
                .writer(manufacturerWriter)
                .build();
    }

    @Bean
    public Job manufacturerImportJob(
            JobRepository jobRepository,
            Step manufacturerImportStep) {

        return new JobBuilder(
                "manufacturerImportJob",
                jobRepository
        )
                .start(manufacturerImportStep)
                .build();
    }
}