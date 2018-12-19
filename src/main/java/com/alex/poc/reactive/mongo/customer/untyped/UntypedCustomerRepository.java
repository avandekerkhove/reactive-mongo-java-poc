package com.alex.poc.reactive.mongo.customer.untyped;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.alex.poc.reactive.mongo.Parameters;
import com.alex.poc.reactive.mongo.subscriber.SubscriberHelpers.ObservableSubscriber;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.Success;

import reactor.core.publisher.Mono;

public class UntypedCustomerRepository {

    private final MongoClient mongoClient;

    public UntypedCustomerRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    
    public void clear() throws Throwable {
        var subscriber = new ObservableSubscriber<Success>();
        getMongoCollection().drop().subscribe(subscriber);
        subscriber.await();
    }
    
    public Mono<RawCustomer> getByLogin(String login) {
        return Mono.from(getMongoCollection().find(Filters.eq("_id", login)).first()).map(document -> {
            return new RawCustomer((String)document.get("login"), (String)document.get("name"), (int)document.get("age"));
        });
    }
    
    public void save(final RawCustomer customer) throws Throwable {
        
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("_id", customer.getLogin());
        fields.put("login", customer.getLogin());
        fields.put("name", customer.getName());
        fields.put("age", customer.getAge());
        Document document = new Document(fields);
        
        var subscriber = new ObservableSubscriber<Success>();
        getMongoCollection().insertOne(document).subscribe(subscriber);
        subscriber.await();
    }
    
    private MongoCollection<Document> getMongoCollection() {
        return mongoClient
                .getDatabase(Parameters.MONGO_DB)
                .getCollection(Parameters.MONGO_COLLECTION);
    }
    
}
