package com.demo.nspl.restaurantlite.Global;

public class ClsPrintPdf {

    String PdfString ="";
    String PdfFilePath = "";
    String Status = "";

    public String getPdfString() {
        return PdfString;
    }

    public void setPdfString(String pdfString) {
        PdfString = pdfString;
    }

    public String getPdfFilePath() {
        return PdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        PdfFilePath = pdfFilePath;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}