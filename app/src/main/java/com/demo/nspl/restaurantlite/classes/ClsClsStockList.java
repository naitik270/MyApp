package com.demo.nspl.restaurantlite.classes;

import com.demo.nspl.restaurantlite.Stock.ClsStock;

import java.util.List;

public class ClsClsStockList {

    private List<ClsStock> lstClsStocks;
    private List<ClsStock> lstClsStocksOut;


    public List<ClsStock> getLstClsStocks() {
        return lstClsStocks;
    }

    public void setLstClsStocks(List<ClsStock> lstClsStocks) {
        this.lstClsStocks = lstClsStocks;
    }

    public List<ClsStock> getLstClsStocksOut() {
        return lstClsStocksOut;
    }

    public void setLstClsStocksOut(List<ClsStock> lstClsStocksOut) {
        this.lstClsStocksOut = lstClsStocksOut;
    }
}
