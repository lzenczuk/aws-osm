package com.github.lzenczuk.osm.awsosm.lambda.download;

/**
 * Created by lzenczuk on 29/03/17.
 */
public class DownloadUrlToS3Response {

    private boolean success;
    private String errorMessage;

    public DownloadUrlToS3Response(String errorMessage) {
        if(errorMessage==null){
            success = true;
        }else {
            success = false;
            this.errorMessage = errorMessage;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
