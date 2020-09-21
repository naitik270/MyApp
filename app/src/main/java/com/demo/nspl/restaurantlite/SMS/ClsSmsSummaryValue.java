package com.demo.nspl.restaurantlite.SMS;

import java.io.Serializable;

public class ClsSmsSummaryValue implements Serializable {

    double TransactionSMSTotalCreditASPerPackage = 0.0;
    double PromotionalSMSTotalCreditPerPackage = 0.0;
    double TotalSMSCreditPerPackage = 0.0;

    double TransactionSMSTotalCreditASPerOffer = 0.0;
    double PromotionalSMSTotalCreditPerOffer = 0.0;
    double TotalSMSCreditPerOffer = 0.0;

    double TransactionSMSTotalCredit = 0.0;
    double PromotionalSMSTotalCredit = 0.0;
    double TotalSMSCredit = 0.0;
    double discountPer = 0.0;
    double discountAmount = 0.0;
    double TaxableAmount = 0.0;
    double IGSTValue = 0.0, CGSTValue = 0.0, SGSTValue = 0.0;
    double IGSTTaxAmount = 0.0, CGSTTaxAmount = 0.0, SGSTTaxAmount = 0.0,
            TotalTaxAmount = 0.0, TotalPackageAmount = 0.0;

    public double getTaxableAmount() {
        return TaxableAmount;
    }

    public void setTaxableAmount(double taxableAmount) {
        TaxableAmount = taxableAmount;
    }

    public double getIGSTValue() {
        return IGSTValue;
    }

    public void setIGSTValue(double IGSTValue) {
        this.IGSTValue = IGSTValue;
    }

    public double getCGSTValue() {
        return CGSTValue;
    }

    public void setCGSTValue(double CGSTValue) {
        this.CGSTValue = CGSTValue;
    }

    public double getSGSTValue() {
        return SGSTValue;
    }

    public void setSGSTValue(double SGSTValue) {
        this.SGSTValue = SGSTValue;
    }

    public double getIGSTTaxAmount() {
        return IGSTTaxAmount;
    }

    public void setIGSTTaxAmount(double IGSTTaxAmount) {
        this.IGSTTaxAmount = IGSTTaxAmount;
    }

    public double getCGSTTaxAmount() {
        return CGSTTaxAmount;
    }

    public void setCGSTTaxAmount(double CGSTTaxAmount) {
        this.CGSTTaxAmount = CGSTTaxAmount;
    }

    public double getSGSTTaxAmount() {
        return SGSTTaxAmount;
    }

    public void setSGSTTaxAmount(double SGSTTaxAmount) {
        this.SGSTTaxAmount = SGSTTaxAmount;
    }

    public double getTotalTaxAmount() {
        return TotalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        TotalTaxAmount = totalTaxAmount;
    }

    public double getTotalPackageAmount() {
        return TotalPackageAmount;
    }

    public void setTotalPackageAmount(double totalPackageAmount) {
        TotalPackageAmount = totalPackageAmount;
    }

    public double getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(double discountPer) {
        this.discountPer = discountPer;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTransactionSMSTotalCreditASPerPackage() {
        return TransactionSMSTotalCreditASPerPackage;
    }

    public void setTransactionSMSTotalCreditASPerPackage(double transactionSMSTotalCreditASPerPackage) {
        TransactionSMSTotalCreditASPerPackage = transactionSMSTotalCreditASPerPackage;
    }

    public double getPromotionalSMSTotalCreditPerPackage() {
        return PromotionalSMSTotalCreditPerPackage;
    }

    public void setPromotionalSMSTotalCreditPerPackage(double promotionalSMSTotalCreditPerPackage) {
        PromotionalSMSTotalCreditPerPackage = promotionalSMSTotalCreditPerPackage;
    }

    public double getTotalSMSCreditPerPackage() {
        return TotalSMSCreditPerPackage;
    }

    public void setTotalSMSCreditPerPackage(double totalSMSCreditPerPackage) {
        TotalSMSCreditPerPackage = totalSMSCreditPerPackage;
    }

    public double getTransactionSMSTotalCreditASPerOffer() {
        return TransactionSMSTotalCreditASPerOffer;
    }

    public void setTransactionSMSTotalCreditASPerOffer(double transactionSMSTotalCreditASPerOffer) {
        TransactionSMSTotalCreditASPerOffer = transactionSMSTotalCreditASPerOffer;
    }

    public double getPromotionalSMSTotalCreditPerOffer() {
        return PromotionalSMSTotalCreditPerOffer;
    }

    public void setPromotionalSMSTotalCreditPerOffer(double promotionalSMSTotalCreditPerOffer) {
        PromotionalSMSTotalCreditPerOffer = promotionalSMSTotalCreditPerOffer;
    }

    public double getTotalSMSCreditPerOffer() {
        return TotalSMSCreditPerOffer;
    }

    public void setTotalSMSCreditPerOffer(double totalSMSCreditPerOffer) {
        TotalSMSCreditPerOffer = totalSMSCreditPerOffer;
    }

    public double getTransactionSMSTotalCredit() {
        return TransactionSMSTotalCredit;
    }

    public void setTransactionSMSTotalCredit(double transactionSMSTotalCredit) {
        TransactionSMSTotalCredit = transactionSMSTotalCredit;
    }

    public double getPromotionalSMSTotalCredit() {
        return PromotionalSMSTotalCredit;
    }

    public void setPromotionalSMSTotalCredit(double promotionalSMSTotalCredit) {
        PromotionalSMSTotalCredit = promotionalSMSTotalCredit;
    }

    public double getTotalSMSCredit() {
        return TotalSMSCredit;
    }

    public void setTotalSMSCredit(double totalSMSCredit) {
        TotalSMSCredit = totalSMSCredit;
    }
}
