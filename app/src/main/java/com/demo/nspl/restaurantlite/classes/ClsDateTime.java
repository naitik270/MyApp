package com.demo.nspl.restaurantlite.classes;

/**
 * Created by Desktop on 4/21/2018.
 */

public class ClsDateTime {
    String MonthName = "";//Apr
    int Year=0;//2016,2017,2018,...
    String LastDate="";//30/04/2018,28/02/2018,31/12/2018
    String FirstDate = "";//01/04/2018,01/12/2018
    int shortYear=0;

    public int getShortYear() {
        return shortYear;
    }

    public void setShortYear(int shortYear) {
        this.shortYear = shortYear;
    }

    public String getMonthName() {
        return MonthName;
    }

    public void setMonthName(String monthName) {
        MonthName = monthName;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getFirstDate() {
        return FirstDate;
    }

    public void setFirstDate(String firstDate) {
        FirstDate = firstDate;
    }

    public String getLastDate() {
        return LastDate;
    }

    public void setLastDate(String lastDate) {
        LastDate = lastDate;
    }


}
