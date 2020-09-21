package com.demo.nspl.restaurantlite.A_Test;

public class ClsGetValue {


    String mName="";
    String mMobile="";
    int DetailId=0,lstGroupId = 0;


    public int getDetailId() {
        return DetailId;
    }

    public void setDetailId(int detailId) {
        DetailId = detailId;
    }

    public int getLstGroupId() {
        return lstGroupId;
    }

    public void setLstGroupId(int lstGroupId) {
        this.lstGroupId = lstGroupId;
    }

    public ClsGetValue(){

    }


    public ClsGetValue(String mName) {
        this.mName = mName;


    }

    public ClsGetValue(String mName, String mMobile) {
        this.mName = mName;
        this.mMobile = mMobile;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmMobile() {
        return mMobile;
    }

    public void setmMobile(String mMobile) {
        this.mMobile = mMobile;
    }





//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ClsGetValue that = (ClsGetValue) o;
//        return Objects.equals(mName, that.mName) &&
//                Objects.equals(mMobile, that.mMobile);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(mName, mMobile);
//    }
}
