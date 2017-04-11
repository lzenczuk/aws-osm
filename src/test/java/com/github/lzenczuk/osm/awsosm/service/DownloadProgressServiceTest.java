package com.github.lzenczuk.osm.awsosm.service;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by lzenczuk on 31/03/17.
 */
public class DownloadProgressServiceTest {

    @Test
    public void updateDownloadProgress(){
        DownloadProgressService downloadProgressService = new DownloadProgressService();

        String requestId = UUID.randomUUID().toString();
        downloadProgressService.requested(requestId, "url", "bn", "fn");
        downloadProgressService.startDownloading(requestId, 100L, LocalDateTime.now());
        downloadProgressService.updateProgress(requestId, 10, 2, 10, 2);
        downloadProgressService.updateProgress(requestId, 20, 3, 30, 5);
        downloadProgressService.updateProgress(requestId, 10, 3, 40, 8);
    }
}

