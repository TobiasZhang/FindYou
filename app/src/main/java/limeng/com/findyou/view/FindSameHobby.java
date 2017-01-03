package limeng.com.findyou.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import limeng.com.findyou.Index;
import model.dto.DataRoot;
import model.pojo.Topic;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.GsonManager;
import utils.HttpUtils;
import utils.ImageUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;

public class FindSameHobby extends AppCompatActivity {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                ToastUtils.showShortMessage(FindSameHobby.this,"发表成功");
                Topic t = (Topic) msg.obj;
                Intent intent = new Intent(FindSameHobby.this, Index.class);
                startActivity(intent);
            }
        }
    };
    private TextView back;
    private EditText title,content,des1,des2,des3;
    private ImageView img1,img2,img3;
    private TextView tip1,tip2,tip3;
    private Button btn;
    private Bitmap bm;
    private Bitmap bm2;
    private Bitmap bm3;
    boolean flag1 = false;
    private List<RequestBody> rlist = new ArrayList<>();
    private List<String> desList = new ArrayList<>();
    private Uri outputFileUri;
    //上传图片用到的body

    private final int CUT_OK = 1,CUT_OK2 = 2,CUT_OK3=3;
    private LinearLayout l1,l2,l3;
    private static final int PHOTO_PICKED_WITH_DATA = 3031;
    private static final int PHOTO_PICKED_WITH_DATA2 = 3032;
    private static final int PHOTO_PICKED_WITH_DATA3 = 3033;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_same_hobby);
        init();
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA2);
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA3);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(title.getText().toString().trim())){
                    ToastUtils.showShortMessage(FindSameHobby.this,"标题不能为空");
                    return;
                }
                if("".equals(content.getText().toString().trim())){
                    ToastUtils.showShortMessage(FindSameHobby.this,"内容不能为空");
                    return;
                }
                String titles = title.getText().toString().trim();
                String contents = content.getText().toString().trim();
                int type = 1;
                int uid = LoginUtils.userInfo.getId();
                MultipartBody.Builder body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("userInfo.id",uid+"");
                if(rlist.size()!=0){
                    for(RequestBody b:rlist){
                        body.addFormDataPart("file","123",b);
                    }
                    if(rlist.size()==1){
                        if(des1.getText().toString()==""||des1.getText().toString().equals("")){
                            desList.add("描述1");
                        }else{
                            desList.add(des1.getText().toString());
                        }
                    }if(rlist.size()==2){
                        if(des1.getText().toString()==""||des1.getText().toString().equals("")){
                            desList.add("描述1");
                        }else{
                            desList.add(des1.getText().toString());
                        }
                        if(des2.getText().toString()==""||des2.getText().toString().equals("")){
                            desList.add("描述2");
                        }else{
                            desList.add(des2.getText().toString());
                        }

                    }if(rlist.size()==3){
                        if(des1.getText().toString()==""||des1.getText().toString().equals("")){
                            desList.add("描述1");
                        }else{
                            desList.add(des1.getText().toString());
                        }
                        if(des2.getText().toString()==""||des2.getText().toString().equals("")){
                            desList.add("描述2");
                        }else{
                            desList.add(des2.getText().toString());
                        }
                        if(des3.getText().toString()==""||des3.getText().toString().equals("")){
                            desList.add("描述3");
                        }else{
                            desList.add(des3.getText().toString());
                        }
                    }
                }
                if(desList.size()!=0){
                    for(String s:desList){
                        body.addFormDataPart("description",s);
                    }
                }
                body.addFormDataPart("title",titles).addFormDataPart("content",contents)
                .addFormDataPart("type",1+"");
               MultipartBody mb =  body.build();
                HttpUtils.request("topic/add", mb, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(FindSameHobby.this,"当前网络不给力");
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<Topic> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<Topic>>(){});
                            Message msg= Message.obtain();
                            msg.what=0;
                            msg.obj = root.getData();
                            mHandler.sendMessage(msg);
                        }

                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && data != null){
            switch(requestCode){
                case PHOTO_PICKED_WITH_DATA:
                    Uri uri =  data.getData();
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        String path = ImageUtils.saveImage(bm);
                        File file = new File(path);
                        RequestBody uploadFile = RequestBody.create(MediaType.parse("application/octet-stream"),file);
                        rlist.add(uploadFile);
                        outputFileUri = Uri.fromFile(file);
                        Intent intent2 = new Intent("com.android.camera.action.CROP");
                        intent2.setDataAndType(outputFileUri,"image/*");
                        intent2.putExtra("crop", "true");
                        // aspectX aspectY 是宽高的比例
                        intent2.putExtra("aspectX", 9998);
                        intent2.putExtra("aspectY", 9999);
                        // outputX outputY 是裁剪图片宽高
                        intent2.putExtra("outputX",120);
                        intent2.putExtra("outputY",120);
                        intent2.putExtra("noFaceDetection", true);
                        startActivityForResult(intent2, CUT_OK);
                        break;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case PHOTO_PICKED_WITH_DATA2:
                    Uri uri2 =  data.getData();
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri2);
                        String path = ImageUtils.saveImage(bm);
                        File file = new File(path);
                        RequestBody uploadFile = RequestBody.create(MediaType.parse("application/octet-stream"),file);
                        rlist.add(uploadFile);
                        outputFileUri = Uri.fromFile(file);
                        Intent intent2 = new Intent("com.android.camera.action.CROP");
                        intent2.setDataAndType(outputFileUri,"image/*");
                        intent2.putExtra("crop", "true");
                        // aspectX aspectY 是宽高的比例
                        intent2.putExtra("aspectX", 9998);
                        intent2.putExtra("aspectY", 9999);
                        // outputX outputY 是裁剪图片宽高
                        intent2.putExtra("outputX",120);
                        intent2.putExtra("outputY",120);
                        intent2.putExtra("noFaceDetection", true);
                        startActivityForResult(intent2, CUT_OK2);
                        break;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case PHOTO_PICKED_WITH_DATA3:
                    Uri uri3 =  data.getData();
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri3);
                        String path = ImageUtils.saveImage(bm);
                        File file = new File(path);
                        RequestBody uploadFile = RequestBody.create(MediaType.parse("application/octet-stream"),file);
                        rlist.add(uploadFile);
                        outputFileUri = Uri.fromFile(file);
                        Intent intent2 = new Intent("com.android.camera.action.CROP");
                        intent2.setDataAndType(outputFileUri,"image/*");
                        intent2.putExtra("crop", "true");
                        // aspectX aspectY 是宽高的比例
                        intent2.putExtra("aspectX", 9998);
                        intent2.putExtra("aspectY", 9999);
                        // outputX outputY 是裁剪图片宽高
                        intent2.putExtra("outputX",120);
                        intent2.putExtra("outputY",120);
                        intent2.putExtra("noFaceDetection", true);
                        startActivityForResult(intent2, CUT_OK3);
                        break;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case CUT_OK:
                    bm = BitmapFactory.decodeFile(outputFileUri.getPath());
//                    try {
//                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(),outputFileUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    tip1.setVisibility(View.GONE);
                    img1.setVisibility(View.VISIBLE);
                    des1.setVisibility(View.VISIBLE);
                    String url1 =ImageUtils.saveImage(bm);
                    img1.setImageBitmap(bm);
                    flag1 = true;
                    break;
                case CUT_OK2:
                    bm = BitmapFactory.decodeFile(outputFileUri.getPath());
//                    try {
//                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(),outputFileUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    tip2.setVisibility(View.GONE);
                    img2.setVisibility(View.VISIBLE);
                    des2.setVisibility(View.VISIBLE);

                    String url2 =ImageUtils.saveImage(bm);
                    img2.setImageBitmap(bm);
                    break;
                case CUT_OK3:
                    bm = BitmapFactory.decodeFile(outputFileUri.getPath());
//                    try {
//                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(),outputFileUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    tip3.setVisibility(View.GONE);
                    img3.setVisibility(View.VISIBLE);
                    des3.setVisibility(View.VISIBLE);

                    String url3 =ImageUtils.saveImage(bm);
                    img3.setImageBitmap(bm);
                    break;
                default:
                    break;
            }
        }
    }
    void init(){
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        des1 = (EditText) findViewById(R.id.img1_des);
        des2 = (EditText) findViewById(R.id.img2_des);
        des3 = (EditText) findViewById(R.id.img3_des);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        tip1 = (TextView) findViewById(R.id.tip1);
        tip2 = (TextView) findViewById(R.id.tip2);
        tip3 = (TextView) findViewById(R.id.tip3);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        l3 = (LinearLayout) findViewById(R.id.l3);
        btn = (Button) findViewById(R.id.login_commit_btn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }
}
