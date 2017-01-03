package com.tt.findyou.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.pojo.UserInfo;

/**
 * Created by TT on 2016/12/13.
 */
public class Utils {
    public static UserInfo loginUser;

    public static void toast(final Context context, final String msg){
        if(context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }
    }

    public static final File DIR_ROOT = new File(Environment.getExternalStorageDirectory(),"一定找到你");
//    public static final File DIR_TEMP = new File(DIR_ROOT,"一定找到你");
    static{
        if(!DIR_ROOT.exists())
            DIR_ROOT.mkdirs();
    }


    public static SpannableStringBuilder highlight(String txt , String key, int color, int start, int end) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(txt);
        CharacterStyle span = null;
        Pattern p = Pattern.compile(key,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(txt.subSequence(start,end));
        while (m.find()) {
            span = new ForegroundColorSpan(color);
            spannable.setSpan(span, start+m.start(), start+m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }
    public static SpannableStringBuilder highlight(String txt ,String key,int color) {
        return highlight(txt,key,color,0,txt.length());
    }
    public static SpannableStringBuilder highlight(String txt ,String key,int color,int start) {
        return highlight(txt,key,color,start,txt.length());
    }
}
