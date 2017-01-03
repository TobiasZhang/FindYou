package com.tt.findyou.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.utils.HttpContacts;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by TT on 2016/12/13.
 */
public class HttpUtils {
    public static String BASE_URL = HttpContacts.BASE_URL;
    public static String BASE_URL_IMAGE_USER_HEAD = HttpContacts.BASE_URL_IMAGE_USER_HEAD;
    public static String BASE_URL_IMAGE_USER = HttpContacts.BASE_URL_IMAGE_USER;
    public static String BASE_URL_IMAGE_TOPIC = HttpContacts.BASE_URL_IMAGE_TOPIC;

    private static OkHttpClient client;
    static{
        client = new OkHttpClient();
    }
    public static void request(String url, RequestBody requestBody, Callback callback){
        Request.Builder requestBuilder = new Request.Builder();
        if(requestBody != null)
            requestBuilder.post(requestBody);
        Request request = requestBuilder.url(BASE_URL+url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    private static Gson gson = new Gson();
    public static<T> T parseJson(String json,TypeToken<T> token){
        /*DataRoot<T> dataRoot = gson.fromJson(json,token.getType());
        T t = dataRoot.getData();*/
        T t = gson.fromJson(json,token.getType());
        return t;
    }
}
