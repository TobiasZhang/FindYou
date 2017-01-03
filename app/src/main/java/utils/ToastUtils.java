package utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class ToastUtils {
    public static void showLongMessage(Context con,String msg){
        Toast.makeText(con,msg,Toast.LENGTH_LONG).show();
    }
    public static void showShortMessage(Context con,String msg){
        Toast.makeText(con,msg,Toast.LENGTH_SHORT).show();
    }
}
