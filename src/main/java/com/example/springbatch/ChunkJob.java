package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EnableBatchProcessing
@Configuration
public class ChunkJob {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job chunkBasedJob() {
    return this.jobBuilderFactory.get("chunkBasedJob")
      .start(chunkStep())
      .build();
  }

  @Bean
  public Step chunkStep() {
    return this.stepBuilderFactory.get("chunkStep500")
      .<String, String>chunk(100)
      .reader(itemReader())
      .writer(itemWriter())
      .build();
  }

  @Bean
  public ListItemReader<String> itemReader() {
    List<String> items = new ArrayList<>(500);
    for (int i = 0; i < 500; i++) {
      items.add(UUID.randomUUID().toString());
    }
    return new ListItemReader<>(items);
  }

  @Bean
  public ItemWriter<String> itemWriter() {
    return items -> {
      for (String item : items) {
        System.out.println(">> current item = " + item);
      }
    };
  }


}
