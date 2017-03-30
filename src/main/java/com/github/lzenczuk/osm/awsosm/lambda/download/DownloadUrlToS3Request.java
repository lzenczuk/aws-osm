package com.github.lzenczuk.osm.awsosm.lambda.download;

/**
 * Created by lzenczuk on 29/03/17.
 */
public class DownloadUrlToS3Request {
    private String url;
    private String bucketName;
    private String fileName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DownloadUrlToS3Request{" +
                "bucketName='" + bucketName + '\'' +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
