package com.flyingdata.core.storage;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.config.StorageProperties;
import com.flyingdata.core.result.SyncResult;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public class Es7xDataSyncResultStorage implements DataSyncResultStorage {

    private ElasticsearchClient client;

    @Override
    public void init(FlyingDataSyncProperties properties) {
        if (client != null) {
            return;
        }
        StorageProperties storageProperties = properties.getStorageProperties();
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                        new HttpHost(storageProperties.getHost(), storageProperties.getPort()))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(storageProperties.getUsername(), storageProperties.getPassword()));
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                })
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
    }

    @Override
    public void store(List<SyncResult> results) {

    }

}
