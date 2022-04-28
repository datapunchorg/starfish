package org.datapunch.starfish.core;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClientBuilder;

import java.util.concurrent.ConcurrentHashMap;

public class EmrHelper {
    private static ConcurrentHashMap<String, AmazonElasticMapReduce> cachedEmrClients = new ConcurrentHashMap<>();

    public static AmazonElasticMapReduce getEmr(String region) {
        AmazonElasticMapReduce emr = cachedEmrClients.get(region);
        if (emr != null) {
            return emr;
        }
        return cachedEmrClients.computeIfAbsent(region, t -> {
            DefaultAWSCredentialsProviderChain defaultAWSCredentialsProviderChain = new DefaultAWSCredentialsProviderChain();
            return AmazonElasticMapReduceClientBuilder.standard()
                    .withCredentials(defaultAWSCredentialsProviderChain)
                    .withRegion(region)
                    .build();
        });
    }
}
