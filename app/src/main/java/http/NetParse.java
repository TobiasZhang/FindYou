package http;

import android.net.Network;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class NetParse {
    private static final OkHttpClient client = new OkHttpClient();
    private NetParse(){}
    public static OkHttpClient getInstance(){
        return client;
    }
    /**
     * @param url 请求路径
     * @param map 参数拼接
     * @param callback 回调接口
     */

    public static  void doPost(String url,Map<String,String> map,Callback callback){
        Request request;
        FormBody.Builder builder = new FormBody.Builder();
        if(map!=null){
            Set<String> set = map.keySet();
            Iterator<String> it = set.iterator();
            while(it.hasNext()){
                builder.add(it.next(),map.get(it.next()));
            }
            RequestBody body = builder.build();
            request = new Request.Builder().url(url).post(body).build();
        }else{
            request = new Request.Builder().url(url).build();
        }
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    /**
     * @param url 请求路径
     * @param callback 回调接口
     */
    public static void doGet(String url,Callback callback){
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
