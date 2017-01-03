package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.dto.DataRoot;
import model.pojo.UserInfo;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class GsonManager<T> {
    static Gson gson = new Gson();
    public static<T> T parseJson(String json,TypeToken<T> token){
        T t = gson.fromJson(json,token.getType());
        return t;
    }

}
