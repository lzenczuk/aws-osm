package com.github.lzenczuk.osm.awsosm.model;

/**
 * Created by lzenczuk on 04/04/17.
 */
public class DownloadUrlProgress {
    private Long downloaded;
    private Long time;
    private Long totalDownload;
    private Long totalTime;

    public Long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Long downloaded) {
        this.downloaded = downloaded;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTotalDownload() {
        return totalDownload;
    }

    public void setTotalDownload(Long totalDownload) {
        this.totalDownload = totalDownload;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return "DownloadUrlProgress{" +
                "downloaded=" + downloaded +
                ", time=" + time +
                ", totalDownload=" + totalDownload +
                ", totalTime=" + totalTime +
                '}';
    }
}
