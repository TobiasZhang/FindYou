package utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.utils.HttpContacts;

import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
}
