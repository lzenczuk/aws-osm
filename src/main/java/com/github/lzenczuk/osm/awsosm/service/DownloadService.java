package com.github.lzenczuk.osm.awsosm.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lzenczuk on 29/03/17.
 */
public class DownloadService {

    private static final Logger log = Logger.getLogger(DownloadService.class);

    public String downloadFromTo(String url, String bucketName, String fileName){

        if(url==null){
            return "Null url";
        }

        if(bucketName==null){
            return "BucketName url";
        }

        if(fileName==null){
            return "FileName url";
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)){
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {

                long toTransfer = response.getEntity().getContentLength();
                String contentType = response.getEntity().getContentType().getValue();

                log.info("Content type: "+contentType+"; length: "+ toTransfer);
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(toTransfer);
                objectMetadata.setContentType(contentType);

                InputStream contentInputStream = response.getEntity().getContent();

                TransferManager transferManager = TransferManagerBuilder.defaultTransferManager();
                Upload upload = transferManager.upload(bucketName, fileName, contentInputStream, objectMetadata);

                AtomicLong transferred = new AtomicLong();
                AtomicInteger percentage = new AtomicInteger();
                AtomicLong time = new AtomicLong(System.currentTimeMillis());

                upload.addProgressListener((ProgressListener) progressEvent -> {
                    long t = transferred.addAndGet(progressEvent.getBytesTransferred());
                    long p = t * 100 / toTransfer;

                    if(percentage.get()<p){
                        long currentTimeInMillis = System.currentTimeMillis();
                        long lastStepTime = time.getAndSet(currentTimeInMillis);

                        long deltaTime = (currentTimeInMillis - lastStepTime)/1000;

                        log.info("Progress: "+((int) p)+"% "+deltaTime+"s");
                        percentage.set((int) p);
                    }
                });
                upload.waitForCompletion();

                log.info("Uploaded.");
                return null;

            }else {
                log.info("Receive failure response: "+response.getStatusLine().toString());
                return "Receive failure response: "+response.getStatusLine().toString();
            }
        } catch (IOException e) {
            log.error("Error executing http request.", e);
            return "Error executing http request.";
        } catch (AmazonServiceException e){
            log.error("Amazon service exception.", e);
            return "Amazon service exception.";
        } catch (AmazonClientException e){
            log.error("Amazon client exception.", e);
            return "Amazon client exception";
        } catch (InterruptedException e) {
            log.error("Error waiting for end of upload.", e);
            return "Error waiting for end of upload.";
        }
    }
}
