package com.demo.nspl.restaurantlite.Global;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    private static Retrofit retrofitTest;


//  272500

//    private static final String TEST_URL = "http://192.168.1.200:89/api/";
//    private static final String BASE_URL = "http://192.168.1.199:89/api/";

//    private static final String BASE_URL = "http://136.233.136.42:89/api/";

    //     Live Api
    private static final String BASE_URL = "https://www.ftouch.app:444/api/";

//    private static final String BASE_URL = "http://www.shap.in:89/api/";

//    private static final String BASE_URL = "http://123.201.110.178:89/api/";
//    private static final String TEST_URL = "http://123.201.110.178:89/api/";


//    ********************************** LIVE USER ID PASS ****************************************

    //       9825079823
    //       823823
    //       yasin.nathani823@gmail.com

//    *********************************************************************************************

//    ********************************** LIVE USER ID PASS TOSIFA ****************************************

    //       6354293562
    //       1234
    //       hr@nathanisoftware.com

            /*
                1234567
                r@yopmail.com

               7698555593
               123456
               jigpatel792@gmail.com*/

//    *********************************************************************************************


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getRequestHeader())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getDemoInstance() {
        if (retrofitTest == null) {
            retrofitTest = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .baseUrl(TEST_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getRequestHeader())
                    .build();
        }
        return retrofitTest;
    }


    private static OkHttpClient getRequestHeader() {

        return new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new MyInterceptor())
                .retryOnConnectionFailure(true)
//                .connectionPool(new ConnectionPool(8, 120,
//                        TimeUnit.SECONDS))
                .build();
    }


}
