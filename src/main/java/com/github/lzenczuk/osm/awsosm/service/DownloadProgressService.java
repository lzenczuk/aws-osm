package com.github.lzenczuk.osm.awsosm.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.lzenczuk.osm.awsosm.model.DownloadUrlRequest;
import com.github.lzenczuk.osm.awsosm.model.DownloadUrlStatus;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by lzenczuk on 31/03/17.
 */
public class DownloadProgressService {

    private static final Logger log = Logger.getLogger(DownloadProgressService.class);

    private static final DynamoDB dynamoDB;
    private static final ObjectMapper mapper;

    static {
        dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
        mapper =  new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
    }

    public void requested(String requestId, String url, String bucketName, String fileName){
        log.info("Creating new entry. id: "+requestId+"; url: "+url+"; bucket: "+bucketName+"; file: "+fileName);
        Table table = dynamoDB.getTable("downloadUrlProgressTable");

        Item item = new Item()
                .withPrimaryKey("requestId", requestId)
                .withString("url", url)
                .withString("bucketName", bucketName)
                .withString("fileName", fileName)
                .withString("status", DownloadUrlStatus.REQUESTED.name())
                .withList("downloadProgress", Collections.<Map<String, Long>>emptyList());

        log.info("Putting item: "+item);
        PutItemOutcome putItemOutcome = table.putItem(item);
        log.info("Putting item result: "+putItemOutcome);
    }

    public void startDownloading(String requestId, Long size, LocalDateTime time){
        log.info("Updating entry. id: "+requestId+"; size: "+size+"; time: "+time);
        Table table = dynamoDB.getTable("downloadUrlProgressTable");

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("requestId", requestId)
                .withAttributeUpdate(
                        new AttributeUpdate("size").put(size),
                        new AttributeUpdate("startTime").put(time.toString()),
                        new AttributeUpdate("status").put(DownloadUrlStatus.DOWNLOADING.name())
                        );

        log.info("Updating item: "+updateItemSpec);
        UpdateItemOutcome updateItemOutcome = table.updateItem(updateItemSpec);
        log.info("Updating item result: "+updateItemOutcome);
    }

    public void updateProgress(String requestId, long stepSize, long stepTime, long totalSize, long totalTime){
        log.info("Updating entry. id: "+requestId+"; stepSize: "+stepSize+"; stepTime: "+stepTime);
        Table table = dynamoDB.getTable("downloadUrlProgressTable");

        NameMap nameMap = new NameMap()
                .with("#dp", "downloadProgress");

        HashMap<String, Long> step = new HashMap<>();
        step.put("downloaded", stepSize);
        step.put("time", stepTime);
        step.put("totalDownload", totalSize);
        step.put("totalTime", totalTime);

        LinkedList<Map<String, Long>> newStep = new LinkedList<>();
        newStep.add(step);

        ValueMap valueMap = new ValueMap()
                .withList(":dp", newStep);

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("requestId", requestId)
                .withUpdateExpression("SET #dp = list_append(#dp, :dp)")
                .withNameMap(nameMap)
                .withValueMap(valueMap);

        log.info("Updating item: "+updateItemSpec);
        UpdateItemOutcome updateItemOutcome = table.updateItem(updateItemSpec);
        log.info("Updating item result: "+updateItemOutcome);
    }

    public void endDownloading(String requestId, LocalDateTime time){
        log.info("Updating entry. id: "+requestId+";time: "+time);
        Table table = dynamoDB.getTable("downloadUrlProgressTable");

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("requestId", requestId)
                .withAttributeUpdate(
                        new AttributeUpdate("endTime").put(time.toString()),
                        new AttributeUpdate("status").put(DownloadUrlStatus.DOWNLOADED.name())
                );

        log.info("Updating item: "+updateItemSpec);
        UpdateItemOutcome updateItemOutcome = table.updateItem(updateItemSpec);
        log.info("Updating item result: "+updateItemOutcome);
    }

    public void endDownloading(String requestId, LocalDateTime time, String error_message){
        log.info("Updating entry. id: "+requestId+";time: "+time);
        Table table = dynamoDB.getTable("downloadUrlProgressTable");

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("requestId", requestId)
                .withAttributeUpdate(
                        new AttributeUpdate("endTime").put(time.toString()),
                        new AttributeUpdate("error").put(error_message),
                        new AttributeUpdate("status").put(DownloadUrlStatus.FAILURE.name())
                );

        log.info("Updating item: "+updateItemSpec);
        UpdateItemOutcome updateItemOutcome = table.updateItem(updateItemSpec);
        log.info("Updating item result: "+updateItemOutcome);
    }

    public DownloadUrlRequest getRequestById(String requestId) throws IOException {
        log.info("Getting request by id: "+requestId);
        Table table = dynamoDB.getTable("downloadUrlProgressTable");

        Item item = table.getItem("requestId", requestId);

        return mapper.readValue(item.toJSON(), DownloadUrlRequest.class);
    }

    public List<String> getAllRequests() {
        log.info("Getting all requests.");
        Table table = dynamoDB.getTable("downloadUrlProgressTable");

        ScanSpec params = new ScanSpec().withAttributesToGet("requestId");
        ItemCollection<ScanOutcome> collection = table.scan(params);

        LinkedList<String> requests = new LinkedList<>();

        collection.forEach(item -> requests.add(item.getString("requestId")));
        log.info("Requests: "+requests);
        return requests;
    }
}
