package com.github.lzenczuk.osm.awsosm.lambda.download;

import com.amazonaws.services.lambda.runtime.Context;
import com.github.lzenczuk.osm.awsosm.model.DownloadUrlRequest;
import com.github.lzenczuk.osm.awsosm.service.DownloadProgressService;
import com.github.lzenczuk.osm.awsosm.service.DownloadService;

import java.io.IOException;
import java.util.List;

/**
 * Created by lzenczuk on 29/03/17.
 */
public class DownloadLambdaHandler {

    public DownloadUrlToS3Response downloadUrlToS3Lambda(DownloadUrlToS3Request request, Context context){

        DownloadProgressService downloadProgressService = new DownloadProgressService();
        DownloadService downloadService = new DownloadService(downloadProgressService);

        return new DownloadUrlToS3Response(downloadService.downloadFromTo(request.getUrl(), request.getBucketName(), request.getFileName()));

    }

    public DownloadUrlRequest downloadUrlStatusLambda(String requestId, Context context) throws IOException {

        DownloadProgressService downloadProgressService = new DownloadProgressService();

        return downloadProgressService.getRequestById(requestId);

    }

    public List<String> getAllDownloadUrlRequestsLambda(Context context) throws IOException {

        DownloadProgressService downloadProgressService = new DownloadProgressService();

        return downloadProgressService.getAllRequests();

    }
}
