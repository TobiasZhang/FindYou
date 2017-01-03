package utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.view.View;

/**
 * Created by Administrator on 2016/12/7 0007.
 */
public class ScreenUtils {
    public static void doStatueImmersive(Context con,int color_Navigat,int color_Status){
        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = ((Activity)con).getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            ((Activity)con).getWindow().setNavigationBarColor(color_Navigat);
//            ((Activity)con).getWindow().setStatusBarColor(color_Status);
            if (Build.VERSION.SDK_INT >= 21) {
                View decorView = ((Activity)con).getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                ((Activity)con).getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }
}
