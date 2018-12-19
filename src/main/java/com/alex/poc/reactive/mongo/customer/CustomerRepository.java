package com.alex.poc.reactive.mongo.customer;

import com.alex.poc.reactive.mongo.Parameters;
import com.alex.poc.reactive.mongo.subscriber.SubscriberHelpers.ObservableSubscriber;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;

import reactor.core.publisher.Mono;

public class CustomerRepository {

    private final MongoClient mongoClient;

    public CustomerRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    
    public Mono<Customer> getByLogin(String login) {
        return Mono.from(getMongoCollection().find(Filters.eq("_id", login)).first());
    }
    
    public void save(final Customer customer) throws Throwable {
        var subscriber = new ObservableSubscriber<Success>();
        getMongoCollection().insertOne(customer).subscribe(subscriber);
        subscriber.await();
    }
    
    private MongoCollection<Customer> getMongoCollection() {
        return mongoClient
                .getDatabase(Parameters.MONGO_DB)
                .getCollection(Parameters.MONGO_COLLECTION, Customer.class);
    }
    
}
