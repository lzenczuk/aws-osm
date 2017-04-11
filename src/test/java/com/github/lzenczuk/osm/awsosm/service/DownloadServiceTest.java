package com.github.lzenczuk.osm.awsosm.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Created by lzenczuk on 29/03/17.
 */
public class DownloadServiceTest {

    private static final Logger log = Logger.getLogger(DownloadServiceTest.class);

    @Test
    public void shouldTransferFile(){
        DownloadProgressService downloadProgressServiceMock = mock(DownloadProgressService.class);
        DownloadService downloadService = new DownloadService(downloadProgressServiceMock);

        String result = downloadService.downloadFromTo("https://download.geofabrik.de/europe/faroe-islands-latest.osm.bz2", "expapp-lambda-bucket", "faroe.osm.bz2");
        assertNull(result);
    }

    @Test
    public void nullUrl(){
        DownloadProgressService downloadProgressServiceMock = mock(DownloadProgressService.class);
        DownloadService downloadService = new DownloadService(downloadProgressServiceMock);

        String result = downloadService.downloadFromTo(null, "expapp-lambda-bucket", "faroe.osm.bz2");
        assertEquals("Null url", result);
    }
}
