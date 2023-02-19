package com.flyingdata.core.storage;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import com.flyingdata.core.config.FlyingDataSyncProperties;
import com.flyingdata.core.config.StorageProperties;
import com.flyingdata.core.exception.FlyingDataException;
import com.flyingdata.core.result.SyncResult;
import com.flyingdata.core.utils.FastjsonUtil;
import com.flyingdata.core.utils.ListUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public class Es7xDataSyncResultStorage implements DataSyncResultStorage {
    protected final static Logger logger = LoggerFactory.getLogger(Es7xDataSyncResultStorage.class);


    private ElasticsearchClient client;

    private String indexName;

    @Override
    public void init(FlyingDataSyncProperties properties) {
        if (client != null) {
            return;
        }
        StorageProperties storageProperties = properties.getStorageProperties();
        // Create the low-level client
        RestClientBuilder restClientBuilder = RestClient.builder(
                new HttpHost(storageProperties.getHost(), storageProperties.getPort()));
        if (StringUtils.isNotEmpty(storageProperties.getUsername()) && StringUtils.isNotEmpty(storageProperties.getPassword())) { // 设置账号密码
            restClientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(storageProperties.getUsername(), storageProperties.getPassword()));
                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }
        RestClient restClient = restClientBuilder.build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
        indexName = Optional.of(storageProperties.getIndexName()).get();
    }

    @Override
    public void store(List<SyncResult> results) {

        ListUtil.splitList(results,500).forEach(rs -> {
            BulkRequest.Builder br = new BulkRequest.Builder();

            for (SyncResult result : rs) {
                br.operations(op -> {
                    ObjectBuilder<BulkOperation> builder;
                    if ("INSERT".equalsIgnoreCase(result.getType())) {
                        builder = op.index(idx -> idx
                                .index(indexName)
                                .id(result.get_id())
                                .document(FastjsonUtil.toEsJsonData(result.getData()))
                        );
                    } else if ("UPDATE".equalsIgnoreCase(result.getType())) {
                        builder = op.update(up -> up
                                .index(indexName)
                                .id(result.get_id())
                                .action(a -> a
                                        .doc(FastjsonUtil.toEsJsonData(result.getData()))
                                        .docAsUpsert(true)
                                )
                                .retryOnConflict(3)
                        );
                    } else { // DELETE
                        builder = op.delete(de -> de
                                .index(indexName)
                                .id(result.get_id())
                        );
                    }
                    return builder;
                });
            }

            BulkResponse result;
            try {
                result = client.bulk(br.build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (result.errors()) {
                for (BulkResponseItem item: result.items()) {
                    if (item.error() != null) {
                        logger.error(item.error().reason());
                    }
                }
                throw new FlyingDataException("Bulk had errors");
            }

        });



    }

}
