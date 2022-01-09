package com.example.social.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    public MongoConfig() {
        logger.info("MongoConfiguration applied  ...");
    }

    @Override
    protected String getDatabaseName() {
        return "MongoDB";
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/MongoDB");
        //ConnectionString connectionString = new ConnectionString("mongodb://10.0.30.51:27017/MongoDB");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        //return MongoClients.create("mongodb://localhost:27017");
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoDatabase mongoDatabase() {
        return mongoClient().getDatabase("MongoDB");
    }


    @Bean
    public MongoOperations mongoOperations() throws Exception {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory()),
                new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        //converter.setMapKeyDotReplacement("#");
        //converter.afterPropertiesSet();
        //converter.setCustomConversions(new CustomConversions());

        // provide a WriteConcernResolver, which is called for _every_ MongoAction
        // which might degrade performance slightly (not tested)
        // and is very flexible to determine the value
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), converter);
        mongoTemplate.setWriteConcernResolver(action -> {
            logger.debug("Action {} called on collection {} for document {} with WriteConcern.MAJORITY. Default WriteConcern was {}", action.getMongoActionOperation(), action.getCollectionName(), action.getDocument(), action.getDefaultWriteConcern());
            return WriteConcern.ACKNOWLEDGED;
        });
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);

        return mongoTemplate;
    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("com.example.social.domain");
    }

    /*@Bean
    public LoggingEventListener<MongoMappingEvent> mappingEventsListener() {
        return new LoggingEventListener<MongoMappingEvent>();
    }*/

    /*@Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb://10.0.30.51:27017");

        //CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        //        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                //.codecRegistry(pojoCodecRegistry)
                //.addCommandListener(new CommandCounter())
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoDatabase mongoDatabase() {
        //CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        //CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));


        return mongoClient().getDatabase("MongoDB"); //.withCodecRegistry(pojoCodecRegistry);
    }*/

}

/*
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    public MongoConfig() {
        logger.info("MongoConfiguration applied  ...");
    }

    @Override
    protected String getDatabaseName() {
        return "MongoDB";
    }

    @Bean
    MongoTransactionManager transactionManager() {
        return new MongoTransactionManager(mongoDbFactory());
    }

    @Override
    public MongoClient mongoClient() {
        //ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/MongoDB");
        ConnectionString connectionString = new ConnectionString("mongodb://10.0.30.51:27017/MongoDB");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        //return MongoClients.create("mongodb://localhost:27017");
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoOperations mongoOperations() throws Exception {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory()),
                new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        // provide a WriteConcernResolver, which is called for _every_ MongoAction
        // which might degrade performance slightly (not tested)
        // and is very flexible to determine the value
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), converter);
        mongoTemplate.setWriteConcernResolver(action -> {
            logger.debug("Action {} called on collection {} for document {} with WriteConcern.MAJORITY. Default WriteConcern was {}", action.getMongoActionOperation(), action.getCollectionName(), action.getDocument(), action.getDefaultWriteConcern());
            return WriteConcern.ACKNOWLEDGED;
        });
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);

        return mongoTemplate;
    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton("kr.co.voiij.mongo");
    }

    /*@Bean
    public MongoTemplate mongoTemplate() throws Exception {

        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory()),
                new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        // Version 2: provide a WriteConcernResolver, which is called for _every_ MongoAction
        // which might degrade performance slightly (not tested)
        // and is very flexible to determine the value
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), converter);
        mongoTemplate.setWriteConcernResolver(action -> {
            logger.debug("Action {} called on collection {} for document {} with WriteConcern.MAJORITY. Default WriteConcern was {}", action.getMongoActionOperation(), action.getCollectionName(), action.getDocument(), action.getDefaultWriteConcern());
            return WriteConcern.ACKNOWLEDGED;
        });
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);

        //MongoOperations mongoOps = new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create("mongodb://localhost:27017"), "MongoDB"));
        //MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.create("mongodb://localhost:27017"), "MongoDB"));

        //return new MongoTemplate(mongoClient(), "mydatabase");
        return mongoTemplate;
    }
}*/