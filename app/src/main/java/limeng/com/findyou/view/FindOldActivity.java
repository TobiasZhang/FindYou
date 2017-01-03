package limeng.com.findyou.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tt.findyou.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import limeng.com.findyou.Index;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.HttpUtils;
import utils.ImageUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;

public class FindOldActivity extends AppCompatActivity implements View.OnClickListener{
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                ToastUtils.showShortMessage(FindOldActivity.this,"发表成功");
                Intent intent = new Intent(FindOldActivity.this, Index.class);
                startActivity(intent);
            }
        }
    };
    private EditText title,name,content;
    TextView sex;
    private List<String> desList = new ArrayList<>();
    private List<String> expName_param = new ArrayList<>();
    private List<Integer> type_param = new ArrayList<>();
    private List<String> date_param = new ArrayList<>();
    LinearLayout line;
    private int start_year = 1998;
    private int start_month = 8;
    private int start_day = 20;
    private int end_year;
    private int end_month;
    private int end_day;
    String [] str ={"幼儿园", "学前班", "小学", "初中","高中", "大学/专科", "大学/本科", "大学/研究生", "大学/博士生", "军队", "公司/机构/单位"};
    int id = 0;
    Button add,commit;
    EditText view_name;
    TextView view_place;
    TextView view_end_date;
    //param
    String theme;
    String user_name;
    String user_sex;
    String contents;
    int type = 1;
    String end_date;
    String placeType ="";
    String placeName;
//    MultipartBody.Builder body;
    String y,m,d;
    private EditText des1,des2,des3;
    private ImageView img1,img2,img3;
    private TextView tip1,tip2,tip3,back;
    private Bitmap bm;
    private Bitmap bm2;
    private Bitmap bm3;
    String tName,tSex,tContent,tTitle;
    private List<RequestBody> rlist = new ArrayList<>();
    private Uri outputFileUri;
    //上传图片用到的body
    private final int CUT_OK = 1,CUT_OK2 = 2,CUT_OK3=3;
    private LinearLayout l1,l2,l3;
    private static final int PHOTO_PICKED_WITH_DATA = 3031;
    private static final int PHOTO_PICKED_WITH_DATA2 = 3032;
    private static final int PHOTO_PICKED_WITH_DATA3 = 3033;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_old);
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
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(title.getText().toString().trim())){
                    ToastUtils.showShortMessage(FindOldActivity.this,"标题不能为空");
                    return;
                }
                if("".equals(content.getText().toString().trim())){
                    ToastUtils.showShortMessage(FindOldActivity.this,"内容不能为空");
                    return;
                }
                if(sex.getText().toString().trim().isEmpty()){
                    ToastUtils.showShortMessage(FindOldActivity.this,"性别不能为空");
                    return;
                }
                if(name.getText().toString().trim().isEmpty()){
                    ToastUtils.showShortMessage(FindOldActivity.this,"姓名不能为空");
                    return;
                }

                theme = title.getText().toString().trim();
                contents = content.getText().toString().trim();
                MultipartBody.Builder body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("userInfo.id",LoginUtils.userInfo.getId()+"");
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
                if(expName_param.size()>0){
                    for(String str:expName_param){
                        body.addFormDataPart("expName",str);
                    }
                }
                if(type_param.size()>0){
                    for(Integer str:type_param){
                        body.addFormDataPart("placeType",str+"");
                    }
                }
                if(date_param.size()>0){
                    for(String str:date_param){
                        body.addFormDataPart("endDate",str);
                    }
                }
                tTitle = title.getText().toString().trim();
                tContent = content.getText().toString().trim();
                //tSex =sex.getText().toString().trim();
                int usersex = sex.getText().toString().trim().equals("男")?0:1;
                tName =name.getText().toString().trim();
                body.addFormDataPart("title",tTitle).addFormDataPart("content",tContent)
                        .addFormDataPart("targetSex",usersex+"")
                        .addFormDataPart("targetTruename",tName)
                        .addFormDataPart("type",0+"");
                MultipartBody mb =  body.build();

                HttpUtils.request("topic/add", mb, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                });

            }
        });
    }

    void init(){
        back = (TextView) findViewById(R.id.back);
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name = (EditText) findViewById(R.id.name);
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
        commit = (Button) findViewById(R.id.login_commit_btn);
        add = (Button) findViewById(R.id.add_btn);
        add.setOnClickListener(this);
        sex = (TextView) findViewById(R.id.sex);
        sex.setOnClickListener(this);
        line = (LinearLayout) findViewById(R.id.addExp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_btn:
                AlertDialog exp_items = new AlertDialog.Builder(FindOldActivity.this)
                    .setTitle("添加经历").setItems(str,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            id = which+2;
                            placeType =str[which];
                            view = LayoutInflater.from(FindOldActivity.this).inflate(R.layout.find_old_items_layout,null);
                            view_name = (EditText) view.findViewById(R.id.kindname);
                            view_place = (TextView) view.findViewById(R.id.placetype);
                            view_place.setText(placeType);
                            view_end_date = (TextView) view.findViewById(R.id.endtime);
//                            view_sex.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    final String [] user_sex1 = {"男","女"};
//                                    AlertDialog alert = new AlertDialog.Builder(FindOldActivity.this)
//                                            .setSingleChoiceItems(user_sex1, 0, new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    user_sex =user_sex1[which];
//                                                }
//                                            }).setTitle("选择性别").setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    sex.setText(user_sex);
//                                                }
//                                            }).show();
//                                }
//                            });

                            view_end_date.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new DatePickerDialog(FindOldActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            end_year = year;
                                            end_month = monthOfYear+1;
                                            end_day = dayOfMonth;
                                            y = year+"";
                                            m = monthOfYear+1>=10?(monthOfYear+1)+"":"0"+(monthOfYear+1);
                                            d = dayOfMonth>=10?dayOfMonth+"":"0"+dayOfMonth;
                                            view_end_date.setText(y+"/"+m+"/"+d);
                                            view_end_date.setTextColor(Color.BLACK);
                                            end_date = y+"/"+m+"/"+d;
                                        }
                                    },start_year,start_month,start_day).show();
                                    // datePickerDialog.getDatePicker().setMinDate(new Date(start_year,start_month,start_day).getTime());
                                    //datePickerDialog.show();

                                }
                            });
                            new AlertDialog.Builder(FindOldActivity.this).setView(view)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            placeName = view_name.getText().toString().trim();
                                            if(placeName.equals("")){
                                                ToastUtils.showShortMessage(FindOldActivity.this,"地点名称不能为空");
                                                return;
                                            }
                                            placeType = view_place.getText().toString().trim();
                                            type_param.add(id);
                                            end_date = view_end_date.getText().toString().trim();
                                            if(end_date.equals("")){
                                                ToastUtils.showShortMessage(FindOldActivity.this,"结束时间不能为空");
                                                return;
                                            }
                                            expName_param.add(placeName);
                                            date_param.add(end_date);
                                            final View view = LayoutInflater.from(FindOldActivity.this).inflate(R.layout.find_old_exp,null);
                                            TextView txt = (TextView) view.findViewById(R.id.name);
                                            TextView start = (TextView) view.findViewById(R.id.place_type);
                                            TextView end= (TextView) view.findViewById(R.id.end_date);
                                            TextView delete = (TextView) view.findViewById(R.id.delete);
                                            txt.setText(view_name.getText().toString().trim());
                                            start.setText(placeType);
                                            end.setText(y+""+m+""+d);
                                            line.addView(view);
                                            delete.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    AlertDialog dialogs =
                                                            new AlertDialog.Builder(FindOldActivity.this)
                                                                    .setTitle("删除经历")
                                                                    .setMessage("确定删除么？")
                                                                    .setNegativeButton("取消",null)
                                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //请求服务器删除数据
                                                           int index =  line.indexOfChild(view);
                                                            line.removeView(view);
                                                            expName_param.remove(index);
                                                            type_param.remove(index);
                                                            date_param.remove(index);
                                                        }
                                                    }).show();
                                                }
                                            });

                                            /**
                                             *  String theme;
                                             String user_name;
                                             String user_sex;
                                             String contents;
                                             int type = 1;
                                             String end_date;1
                                             String placeType ="";1
                                             String expName;1
                                             */
                                        }
                                    }).setNegativeButton("取消",null).show();
                        }
                    }).setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            view_place.setText(placeType);
                        }
                    }).show();break;
            case R.id.sex:
               final String [] str = {"男","女"};
                AlertDialog dialog = new AlertDialog.Builder(FindOldActivity.this)
                        .setTitle("选择性别")
                        .setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sex.setText(str[which].toString());
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("取消",null).show();
                break;
        }
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
                    break;
                case CUT_OK2:
                    bm = BitmapFactory.decodeFile(outputFileUri.getPath());
                    tip2.setVisibility(View.GONE);
                    img2.setVisibility(View.VISIBLE);
                    des2.setVisibility(View.VISIBLE);
                    String url2 =ImageUtils.saveImage(bm);
                    img2.setImageBitmap(bm);
                    break;
                case CUT_OK3:
                    bm = BitmapFactory.decodeFile(outputFileUri.getPath());
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

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }
}
