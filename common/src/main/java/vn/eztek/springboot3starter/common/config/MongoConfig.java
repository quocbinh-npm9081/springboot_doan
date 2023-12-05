package vn.eztek.springboot3starter.common.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import vn.eztek.springboot3starter.common.converter.StringToZonedDateTimeMongoConverter;
import vn.eztek.springboot3starter.common.converter.ZonedDateTimeToStringMongoConverter;

import java.util.Arrays;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Value("${mongodbName}")
    private String mongodbName;

    @Value("${mongodbHost}")
    private String mongodbHost;

    @Value("${mongodbPort}")
    private Integer mongodbPort;

    @Value("${mongodbUsername}")
    private String mongodbUsername;

    @Value("${mongodbPassword}")
    private String mongodbPassword;

    @Value("${mongodbAuthenticationDatabase}")
    private String mongodbAuthenticationDatabase;

    @Bean
//    public MongoDatabaseFactory mongoDatabaseFactory() {
//        var connectionString =
//                new ConnectionString("mongodb://" + mongodbHost + ":" + mongodbPort + "/");
//        var mongoClientSettings =
//        MongoClientSettings.builder().applyConnectionString(connectionString)
//            .credential(MongoCredential.createCredential(mongodbUsername,
//                mongodbAuthenticationDatabase, mongodbPassword.toCharArray()))
//            .build();
//
//        var mongoClient = MongoClients.create(mongoClientSettings);
//
//        return new SimpleMongoClientDatabaseFactory(mongoClient, mongodbName);
//    }
    public MongoDatabaseFactory mongoDatabaseFactory() {
        var connectionString =
                new ConnectionString("mongodb://" + mongodbHost + ":" + mongodbPort + "/");
        var mongoClientSettings =
                MongoClientSettings.builder().applyConnectionString(connectionString)
                        // Omitting credential here
                        .build();
        var mongoClient = MongoClients.create(mongoClientSettings);

        return new SimpleMongoClientDatabaseFactory(mongoClient, mongodbName);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        var converters = Arrays.asList(new ZonedDateTimeToStringMongoConverter(),
                new StringToZonedDateTimeMongoConverter());

        return new MongoCustomConversions(converters);
    }

}
