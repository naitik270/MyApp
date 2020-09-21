package com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClsGetBackupDetailsList {

    @SerializedName("BackupDate")
    @Expose
    private String backupDate;
    @SerializedName("FileName")
    @Expose
    private String fileName;
    @SerializedName("Extentsion")
    @Expose
    private String extentsion;
    @SerializedName("FileSize")
    @Expose
    private String fileSize;
    @SerializedName("FullFileName")
    @Expose
    private String fullFileName;
    @SerializedName("FileUrl")
    @Expose
    private String fileUrl;
    @SerializedName("Remark")
    @Expose
    private String remark;

    public String getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(String backupDate) {
        this.backupDate = backupDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtentsion() {
        return extentsion;
    }

    public void setExtentsion(String extentsion) {
        this.extentsion = extentsion;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
