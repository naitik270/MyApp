package com.demo.nspl.restaurantlite.Interface;

import com.demo.nspl.restaurantlite.classes.UploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InterfaceFileUpload {


    @Multipart
//    @POST("Service/saveFile")
    @POST("FileUpload/BackupTypePostFile")
    Call<UploadResponse> UploadFile(@Part MultipartBody.Part file,
                                    @Part("CustomerID") RequestBody CustomerID,
                                    @Part("ProductName") RequestBody ProductName,
                                    @Part("AppVersion") RequestBody AppVersion,
                                    @Part("AppType") RequestBody AppType,
                                    @Part("IMEINumber") RequestBody IMEINumber,
                                    @Part("DeviceInfo") RequestBody DeviceInfo,
                                    @Part("BackupType") RequestBody BackupType,
                                    @Part("Remark") RequestBody Remark,
                                    @Part("Extentsion") RequestBody Extentsion,
                                    @Part("FileName") RequestBody FileName);
}
