package com.github.lzenczuk.osm.awsosm.lambda.download;

import com.amazonaws.services.lambda.runtime.Context;
import com.github.lzenczuk.osm.awsosm.service.DownloadService;

/**
 * Created by lzenczuk on 29/03/17.
 */
public class DownloadLambdaHandler {

    public DownloadUrlToS3Response downloadUrlToS3Lambda(DownloadUrlToS3Request request, Context context){

        DownloadService downloadService = new DownloadService();

        return new DownloadUrlToS3Response(downloadService.downloadFromTo(request.getUrl(), request.getBucketName(), request.getFileName()));

    }
}
