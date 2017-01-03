package utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class ImageUtils {
   public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static File imgDir;
    static{
        imgDir = new File(Environment.getExternalStorageDirectory(),"个人图片");
        if(!imgDir.exists())
            imgDir.mkdir();
    }

    public static String saveImage(Bitmap bitmap){
        String fileName = sdf.format(new Date())+(int)(Math.random()*10000);
        File imgFile = new File(imgDir,fileName+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return imgFile.getPath();
    }

}
