package limeng.com.findyou.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.exceptions.HyphenateException;
import com.tt.findyou.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import butterknife.internal.Utils;
import limeng.com.findyou.Index;
import model.dto.DataRoot;
import model.pojo.UserInfo;
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

public class Myself_Activity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private int number ;
    private String pwd;
    private String birth;
    private String path;
    private String sex;
    private String name;
    private TextView txback, txt_time,txt1,txt2,txt3;
    private LinearLayout photo_layout;
    public static int flag = 0;
    public static int ImgFlag = 0;
    private EditText txtName,txtPwd;
    private ImageView userImg;
    private RadioButton man_rb,female_rb;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private File file;
    private Uri outputFileUri;
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    private static final int CAMERA_WITH_DATA = 3023;
    private AlertDialog dialog;
    private int crop = 120;
    private Bitmap bm;
    private Button btn;
    private final int  ALBUM_OK = 1, CAMERA_OK = 2,CUT_OK = 3;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                LoginUtils.userInfo = (UserInfo) msg.obj;
                editor.putInt("uid",LoginUtils.userInfo.getId());
                editor.commit();
                Intent intent = new Intent(Myself_Activity.this, Index.class);
                startActivity(intent);

                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_);
        number  = getIntent().getIntExtra("phoneNum",0);
        Log.e("phoneNum",number+"");
        init();

        txback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Myself_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String y = year+"";
                        String month = monthOfYear+1>=10?(monthOfYear+1)+"":"0"+(monthOfYear+1);
                        String day = dayOfMonth>=10?dayOfMonth+"":"0"+dayOfMonth;
                        txt_time.setText(y+"-"+month+"-"+day);
                        txt_time.setTextColor(Color.BLACK);
                        flag = 1;
                    }
                },2000,1,2).show();
            }
        });
        photo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
//                if (dialog == null) {
//                    dialog = new AlertDialog.Builder(Myself_Activity.this).setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if(which==0){
//                                File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//                                outputFileUri = Uri.fromFile(file);
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//                                startActivityForResult(intent,CAMERA_WITH_DATA );
//                            }else{
//                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                intent.setType("image/*");
//                                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
//                            }
//                        }
//                    }).create();
//                }
//                if (!dialog.isShowing()) {
//                    dialog.show();
//                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = txtName.getText().toString();
                pwd = txtPwd.getText().toString();
                birth = txt_time.getText().toString();
                sex = man_rb.isChecked()?"男":"女";
                if(name.equals("")||name==null){
                    ToastUtils.showShortMessage(Myself_Activity.this,"姓名不能为空");
                    return;
                }
                if(pwd.equals("")||pwd==null){
                    ToastUtils.showShortMessage(Myself_Activity.this,"密码不能为空");
                    return;
                }
                if(flag==0){
                    ToastUtils.showShortMessage(Myself_Activity.this,"请选择出生日期");
                    return;
                }if(ImgFlag==0){
                    ToastUtils.showShortMessage(Myself_Activity.this,"赶快上传帅帅的您的头像呦！");
                    return;
                }
                path =outputFileUri.getPath();
                RequestBody uploadFile = RequestBody.create(MediaType.parse("application/octet-stream"),file);
                MultipartBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("tel",number+"")
                        .addFormDataPart("sex",sex.equals("男")?0+"":1+"")
                        .addFormDataPart("birth",birth)
                        .addFormDataPart("file","123",uploadFile)
                        .addFormDataPart("password",pwd)
                        .build();
                HttpUtils.request("user/register", body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(Myself_Activity.this,"当前网络不给力请，检查设置");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<UserInfo> user = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<UserInfo>>(){});
                            final UserInfo u = user.getData();

                            final ProgressDialog pd = new ProgressDialog(Myself_Activity.this);
                            pd.setMessage(getResources().getString(com.hyphenate.chatuidemo.R.string.Is_the_registered));
                            pd.show();
                            try {
                                // call method in SDK
                                EMClient.getInstance().createAccount(u.getId()+"", u.getPassword());
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (!Myself_Activity.this.isFinishing())
                                            pd.dismiss();
                                        // save current user
                                        DemoHelper.getInstance().setCurrentUserName(u.getId()+"");
                                        DemoHelper.getInstance().getUserProfileManager().uploadUserAvatar(u.getHeadName());
                                        DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(u.getTruename());
                                        Toast.makeText(getApplicationContext(), getResources().getString(com.hyphenate.chatuidemo.R.string.Registered_successfully), Toast.LENGTH_SHORT).show();


                                        Message message = Message.obtain();
                                        message.what=1;
                                        message.obj = u;
                                        mHandler.sendMessage(message);
                                    }
                                });
                            } catch (final HyphenateException e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (!Myself_Activity.this.isFinishing())
                                            pd.dismiss();
                                        int errorCode=e.getErrorCode();
                                        if(errorCode== EMError.NETWORK_ERROR){
                                            Toast.makeText(getApplicationContext(), getResources().getString(com.hyphenate.chatuidemo.R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                        }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                            Toast.makeText(getApplicationContext(), getResources().getString(com.hyphenate.chatuidemo.R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                        }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                            Toast.makeText(getApplicationContext(), getResources().getString(com.hyphenate.chatuidemo.R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                        }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                            Toast.makeText(getApplicationContext(), getResources().getString(com.hyphenate.chatuidemo.R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(), getResources().getString(com.hyphenate.chatuidemo.R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }


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
//                case CAMERA_WITH_DATA:
//                    Intent intent = new Intent("com.android.camera.action.CROP");
//                    intent.setDataAndType(outputFileUri,"image/*");
//                    intent.putExtra("crop", "true");
//                    // aspectX aspectY 是宽高的比例
//                    intent.putExtra("aspectX", 1);
//                    intent.putExtra("aspectY", 1);
//                    // outputX outputY 是裁剪图片宽高
//                    intent.putExtra("outputX",120);
//                    intent.putExtra("outputY",120);
//                    intent.putExtra("noFaceDetection", true);
//                    startActivityForResult(intent, CUT_OK);
//                    break;
                case PHOTO_PICKED_WITH_DATA:
                    Uri uri =  data.getData();
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        String path = ImageUtils.saveImage(bm);
                        file = new File(path);
                        outputFileUri = Uri.fromFile(file);
                        Intent intent2 = new Intent("com.android.camera.action.CROP");
                        intent2.setDataAndType(outputFileUri,"image/*");
                        intent2.putExtra("crop", "true");
                        // aspectX aspectY 是宽高的比例
                        intent2.putExtra("aspectX", 1);
                        intent2.putExtra("aspectY", 1);
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
                case CUT_OK:
                    bm = BitmapFactory.decodeFile(outputFileUri.getPath());
//                    try {
//                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(),outputFileUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    txt1.setVisibility(View.GONE);
                    txt2.setVisibility(View.GONE);
                    txt3.setVisibility(View.GONE);
                    userImg.setVisibility(View.VISIBLE);
                    String url1 =ImageUtils.saveImage(bm);
                    userImg.setImageBitmap(bm);
                    ImgFlag = 1;
                    break;
                default:
                    break;
        }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }
    private void init(){
        sp = getSharedPreferences("loginsp",MODE_PRIVATE);
        editor = sp.edit();
        txback = (TextView) findViewById(R.id.back);
        txt_time = (TextView) findViewById(R.id.datapicker);
        txtName = (EditText) findViewById(R.id.name);
        txtPwd = (EditText) findViewById(R.id.txt_pwd);
        man_rb = (RadioButton) findViewById(R.id.male);
        female_rb = (RadioButton) findViewById(R.id.female);
        photo_layout = (LinearLayout) findViewById(R.id.photopicker);
        userImg = (ImageView) findViewById(R.id.img);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        btn = (Button) findViewById(R.id.login_commit_btn);
    }
    private void showImg(Intent picdata){
        txt1.setVisibility(View.GONE);
        txt2.setVisibility(View.GONE);
        txt3.setVisibility(View.GONE);
        userImg.setVisibility(View.VISIBLE);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picdata.getData()));
            userImg.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
