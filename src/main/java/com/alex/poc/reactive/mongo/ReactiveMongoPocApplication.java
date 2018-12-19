package com.alex.poc.reactive.mongo;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.alex.poc.reactive.mongo.customer.Customer;
import com.alex.poc.reactive.mongo.customer.CustomerRepository;
import com.alex.poc.reactive.mongo.customer.untyped.RawCustomer;
import com.alex.poc.reactive.mongo.customer.untyped.UntypedCustomerRepository;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import reactor.core.publisher.Mono;

/**
 * Hello world!
 *
 */
public class ReactiveMongoPocApplication 
{
    
    public static void main( String[] args ) throws Throwable
    {
        
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        
        UntypedCustomerRepository untypedRepo = new UntypedCustomerRepository(mongoClient);
        untypedRepo.clear();
        untypedRepo.save(new RawCustomer("test@test.com", "alex", 32));
        Mono<RawCustomer> found = untypedRepo.getByLogin("test@test.com");
        System.out.println("found : " + found.block().getLogin());
        
        
        // to play with pojo, need a custom registry
        CodecRegistry pojoCodecRegistry = 
                CodecRegistries.fromRegistries(MongoClients.getDefaultCodecRegistry(),
                        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        
        MongoClientSettings settings = 
                MongoClientSettings.builder()
                    .codecRegistry(pojoCodecRegistry)
                    .applyConnectionString(new ConnectionString("mongodb://localhost:27017"))
                    .build();
        mongoClient = MongoClients.create(settings);
        
        CustomerRepository repo = new CustomerRepository(mongoClient);
        repo.save(new Customer("test2@test.com", "doe", 78));
        Mono<Customer> found2 = repo.getByLogin("test2@test.com");
        System.out.println("found : " + found2.block().getId());
    }
}
