package db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/12/17 0017.
 */
public class SPUtils {
    public static int id;
    public static int getId(Context con){
       SharedPreferences sp = con.getSharedPreferences("loginsp",Context.MODE_PRIVATE);
        return sp.getInt("uid",1);
    }
}
