package com.demo.nspl.restaurantlite.Interface;

import com.demo.nspl.restaurantlite.RetrofitApi.ApiClasses.PdfFileUploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InterfacePdfFileUpload {

    @Multipart
    @POST("FileUpload/CustomerInvoicePostFile")
    Call<PdfFileUploadResponse> PdfUploadFile(@Part MultipartBody.Part file,
                                           @Part("CustomerCode") RequestBody CustomerCode,
                                           @Part("ProductName") RequestBody ProductName,
                                           @Part("FileName") RequestBody FileName,
                                           @Part("Extentsion") RequestBody Extentsion,
                                           @Part("InvoiceNo") RequestBody InvoiceNo,
                                           @Part("Type") RequestBody Type); // Quotation / Invoice

}
