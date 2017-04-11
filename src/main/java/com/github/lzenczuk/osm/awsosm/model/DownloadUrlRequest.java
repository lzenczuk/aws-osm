package com.github.lzenczuk.osm.awsosm.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lzenczuk on 04/04/17.
 */
public class DownloadUrlRequest {
    private String requestId;
    private String url;
    private String bucketName;
    private String fileName;
    private DownloadUrlStatus status;
    private Long size;
    private String startTime;
    private String endTime;
    private String error;
    private List<DownloadUrlProgress> downloadProgress;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public DownloadUrlStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadUrlStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<DownloadUrlProgress> getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(List<DownloadUrlProgress> downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "DownloadUrlRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", requestId='" + requestId + '\'' +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", status=" + status +
                ", size=" + size +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", error='" + error + '\'' +
                ", downloadProgress=" + downloadProgress +
                '}';
    }
}
