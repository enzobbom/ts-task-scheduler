package com.javanauta.ts.taskscheduler.adapters.out.persistence.config;

import com.javanauta.ts.taskscheduler.adapters.out.persistence.converter.ZoneIdReadConverter;
import com.javanauta.ts.taskscheduler.adapters.out.persistence.converter.ZoneIdWriteConverter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Slf4j
@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient(
            @Value("${spring.data.mongodb.uri}") String uri,
            @Value("${spring.data.mongodb.uuid-representation}") UuidRepresentation uuidRepresentation) {

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .uuidRepresentation(uuidRepresentation)
                .applyConnectionString(new ConnectionString(uri))
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(
            MongoClient mongoClient,
            @Value("${spring.data.mongodb.database}") String databaseName) {

        return new SimpleMongoClientDatabaseFactory(
                mongoClient,
                databaseName
        );
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory factory) {
        return new MongoTemplate(factory);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(
                List.of(
                        new ZoneIdReadConverter(),
                        new ZoneIdWriteConverter()
                )
        );
    }
}
