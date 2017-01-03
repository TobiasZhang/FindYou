package limeng.com.findyou.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.parse.MyParseManager;
import com.hyphenate.easeui.domain.EaseUser;
import com.tt.findyou.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.Experience;
import model.pojo.Hobby;
import model.pojo.Topic;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import ui.GridViewAdapter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.ImageUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;
import widget.FlowLayout;
import widget.MyGrideView;
import widget.MyLinearLayout;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar bar;
    UserInfo me = LoginUtils.userInfo;
    String [] titles = {"幼儿园", "学前班", "小学", "初中", "大学/专科", "大学/本科", "大学/研究生", "大学/博士生", "军队", "公司/机构/单位"};
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    String[] sports = {"游泳", "跑步", "瑜伽", "单车", "篮球", "足球", "滑板", "滑雪", "兵乓球", "羽毛球"};
    List<String> list = new ArrayList<>();
    List<String> trueList = new ArrayList<>();
    LinearLayout layout;
    MyGrideView exp;
    private ImageView userimg, usersex;
    private TextView username, userage, back;
    private FlowLayout sport, music, eat, tele, book, tral;
    private TextView tx;
    private Button btn;
    List<Experience> eList = new ArrayList<>();
    private MyGrideView addContent;
    AlertDialog alertDialog = null;
    String item = "";
    private View view;
    public static int ImgFlag = 0;
    private Uri outputFileUri;
    private EditText et;
    String title;
    private TextView start, end;
    private Bitmap bm;
    final int CUT_OK = 1;
    private int start_year = 1998;
    private int start_month = 8;
    private int start_day = 20;
    private int end_year;
     LinearLayout lps;
    private int end_month;
    private GridViewAdapter adapter;
    File file;
    private int end_day;
    String name = "";
    String starttime = "";
    String endtime = "";
    int id = 0;
    private Button btn_commit;
    View contentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        parseArraytoList(sports, trueList);
        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent it = new Intent(Intent.ACTION_PICK);
//                it.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
            }
        });
    }
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case PHOTO_PICKED_WITH_DATA:
                    Uri uri = data.getData();
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        String path = ImageUtils.saveImage(bm);
                        file = new File(path);
                        outputFileUri = Uri.fromFile(file);
                        Intent intent2 = new Intent("com.android.camera.action.CROP");
                        intent2.setDataAndType(outputFileUri, "image/*");
                        intent2.putExtra("crop", "true");
                        // aspectX aspectY 是宽高的比例
                        intent2.putExtra("aspectX", 9998);
                        intent2.putExtra("aspectY", 9999);
                        // outputX outputY 是裁剪图片宽高
                        intent2.putExtra("outputX", 120);
                        intent2.putExtra("outputY", 120);
                        intent2.putExtra("noFaceDetection", true);
                        startActivityForResult(intent2, CUT_OK);
                        break;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case CUT_OK:
                    File files = new File(outputFileUri.getPath());
                    RequestBody uploadFile = RequestBody.create(MediaType.parse("application/octet-stream"),files);

                    MultipartBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("uid",LoginUtils.userInfo.getId()+"")
                            .addFormDataPart("file","123",uploadFile)
                            .build();

                    HttpUtils.request("/user/uploadHeadImage", body, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.code()==200){
                                DataRoot<UserInfo> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<UserInfo>>(){});
                                UserInfo user = root.getData();
                                Message msg = Message.obtain();
                                msg.obj = user;
                                msg.what=1;
                                mHandler.sendMessage(msg);

//                                EaseUser me = DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo();
//                                me.setAvatar(user.getHeadName());
                                //DemoHelper.getInstance().saveContact(me);
                                DemoHelper.getInstance().getUserProfileManager().uploadUserAvatar(user.getHeadName());

                            }

                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.commit:
                HttpUtils.request("user/get?uid=1&id=" + LoginUtils.userInfo.getId(), null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(EditActivity.this,"当前网络不给力");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                            if(response.code()==200){
                                DataRoot<UserInfo> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<UserInfo>>(){});
                                UserInfo u = root.getData();
                                Message msg = Message.obtain();
                                msg.obj = u;
                                msg.what = 2;
                                mHandler.sendMessage(msg);
                            }
                    }
                });
                break;
            case R.id.add_btn:
                AlertDialog exp_items = new AlertDialog.Builder(EditActivity.this)
                        .setTitle("添加经历").setItems(new String[]{"幼儿园", "学前班", "小学", "初中","高中", "大学/专科", "大学/本科", "大学/研究生", "大学/博士生", "军队", "公司/机构/单位"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                id = which + 2;
                                title = titles[which];
                                view = LayoutInflater.from(EditActivity.this).inflate(R.layout.myresume_view, null);
                                et = (EditText) view.findViewById(R.id.kindname);
                                start = (TextView) view.findViewById(R.id.starttime);
                                end = (TextView) view.findViewById(R.id.endtimetime);
                                start.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                start_year = year;
                                                start_month = monthOfYear;
                                                start_day = dayOfMonth;
                                                String y = year + "";
                                                String month = monthOfYear + 1 >= 10 ? (monthOfYear + 1) + "" : "0" + (monthOfYear + 1);
                                                String day = dayOfMonth >= 10 ? dayOfMonth + "" : "0" + dayOfMonth;
                                                start.setText(y + "-" + month + "-" + day);
                                                start.setTextColor(Color.BLACK);

                                            }
                                        }, 2000, 1, 2).show();
                                    }
                                });
                                end.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                end_year = year;
                                                end_month = monthOfYear + 1;
                                                end_day = dayOfMonth;
                                                String y = year + "";
                                                String month = monthOfYear + 1 >= 10 ? (monthOfYear + 1) + "" : "0" + (monthOfYear + 1);
                                                String day = dayOfMonth >= 10 ? dayOfMonth + "" : "0" + dayOfMonth;
                                                end.setText(y + "-" + month + "-" + day);
                                                end.setTextColor(Color.BLACK);
                                            }
                                        }, start_year, start_month, start_day).show();
                                        // datePickerDialog.getDatePicker().setMinDate(new Date(start_year,start_month,start_day).getTime());
                                        //datePickerDialog.show();

                                    }
                                });
                                new AlertDialog.Builder(EditActivity.this).setView(view)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                name = et.getText().toString().trim();
                                                starttime = start.getText().toString().trim();
                                                endtime = end.getText().toString().trim();
                                                if(name.isEmpty()){
                                                    ToastUtils.showShortMessage(EditActivity.this,"标题不能为空");
                                                    return;
                                                }
                                                if(starttime.isEmpty()){
                                                    ToastUtils.showShortMessage(EditActivity.this,"起始时间不能为空");
                                                    return;
                                                }
                                                if(endtime.isEmpty()){
                                                    ToastUtils.showShortMessage(EditActivity.this,"终止时间不能为空");
                                                    return;
                                                }
                                                if(compare(starttime,endtime)){
                                                    ToastUtils.showShortMessage(EditActivity.this,"起始时间不能大于终止时间");
                                                    return;
                                                }
//                                                final View view = LayoutInflater.from(EditActivity.this).inflate(R.layout.exp_items, null);
//                                                TextView t = (TextView) view.findViewById(R.id.title);
//                                                TextView txt = (TextView) view.findViewById(R.id.name);
//                                                TextView start = (TextView) view.findViewById(R.id.start_date);
//                                                TextView end = (TextView) view.findViewById(R.id.end_date);
//                                                TextView delete = (TextView) view.findViewById(R.id.delete);
//                                                t.setText(title);
//                                                txt.setText(name);
//                                                start.setText(starttime);
//                                                end.setText(endtime);
                                                RequestBody body = new FormBody.Builder().add("name",name)
                                                        .add("placeType.id",id+"")
                                                        .add("beginDate",starttime)
                                                        .add("endDate",endtime)
                                                        .add("userInfo.id", LoginUtils.userInfo.getId()+"")
                                                        .build();
                                                HttpUtils.request("exp/add", body, new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    ToastUtils.showShortMessage(EditActivity.this,"当前网络不给力");
                                                                }
                                                            });
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        if(response.code()==200){
                                                            DataRoot<Experience> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<Experience>>(){});
                                                            Experience e = root.getData();
                                                            Message msg = Message.obtain();
                                                            msg.obj = e;
                                                            msg.what=0;
                                                            mHandler.sendMessage(msg);
                                                        }
                                                    }
                                                });
//                                                delete.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        AlertDialog dialogs = new AlertDialog.Builder(EditActivity.this).setTitle("删除经历").setMessage("确定删除么？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                //请求服务器删除数据
//                                                                HttpUtils.request("exp/delete?id="+id, null, new Callback() {
//                                                                    @Override
//                                                                    public void onFailure(Call call, IOException e) {
//                                                                        runOnUiThread(new Runnable() {
//                                                                            @Override
//                                                                            public void run() {
//                                                                                ToastUtils.showShortMessage(EditActivity.this,"当前网络不给力");
//                                                                            }
//                                                                        });
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onResponse(Call call, Response response) throws IOException {
//                                                                            if(response.code()==200){
//
//                                                                            }
//                                                                    }
//                                                                });
//                                                                addContent.removeView(view);
//                                                            }
//                                                        }).show();
//                                                    }
//                                                });
                                                //addContent.addView(view);
                                            }
                                        }).setNegativeButton("取消", null).show();
                            }
                        }).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.user_name:
            case R.id.user_sex:
            case R.id.user_age:Intent intent = new Intent(EditActivity.this,UpdateActivity.class);
                                    startActivity(intent);
                //跳转到个人资料的更改页面
                break;
            case R.id.sport:
                //ti 5
                            contentView = LayoutInflater.from(EditActivity.this).inflate(R.layout.update_hobby_layout,null);
                            TextView tx  = (TextView) contentView.findViewById(R.id.t);
                            tx.setText("体育");
                            lps = (LinearLayout) contentView.findViewById(R.id.contentView);
                            HttpUtils.request("hobby/getall?hobbyParentId=5&uid="+LoginUtils.userInfo.getId(), null, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if(response.code()==200){
                                      DataRoot<List<Hobby>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<Hobby>>>(){});
                                      if(root.getData()!=null&&root.getData().size()>0){
                                          List<Hobby> list = root.getData();
                                          Message msg = Message.obtain();
                                          msg.what=3;
                                          msg.obj = list;
                                          mHandler.sendMessage(msg);
                                      }
                                    }
                                }
                            });
                            contentView.findViewById(R.id.addmyhobby).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   final EditText et = new EditText(EditActivity.this);
                                    new AlertDialog.Builder(EditActivity.this).setTitle("添加自定义标签").setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String hobby  = et.getText().toString();
                                            CheckBox ch = new CheckBox(EditActivity.this);
                                            ch.setText(hobby);
                                            ch.setChecked(true);
                                            ch.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            lps.addView(ch);
                                        }
                                    }).show();
                                }
                            });
                AlertDialog hobby_alert = new AlertDialog.Builder(EditActivity.this)
                                                    .setTitle("选择标签").setView(contentView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i = 0;i<lps.getChildCount();i++){
                                    if(((CheckBox)lps.getChildAt(i)).isSelected()){
                                        
                                    }
                                }
                            }
                        }).show();
            case R.id.music:
            case R.id.eat:
            case R.id.book:
            case R.id.travl:
//            case R.id.televsion: AlertDialog hobby_alert = new AlertDialog.Builder(EditActivity.this)
//                                                    .setTitle("选择标签").setView(contentView).
                break;
            case R.id.updateuser:
                Intent data = new Intent(EditActivity.this, UpdateActivity.class);
                //intent.putExtra("uid",${uid});
                startActivity(data);break;
            case R.id.back :finish();break;
        }
        alertDialog = null;

    }

    void init() {
        userimg = (ImageView) findViewById(R.id.user_img);
        if(me.getHeadImage()!=null&&(!me.getHeadImage().equals(""))){
            Glide.with(EditActivity.this).load(me.getHeadImage()).into(userimg);
        }
        username = (TextView) findViewById(R.id.user_name);
        if(me.getTruename()!=null&&(!me.getTruename().equals("")))
        username.setText(me.getTruename());
        usersex = (ImageView) findViewById(R.id.user_sex);
        if(me.getSex()!=null){
            if(me.getSex()==0){
                usersex.setImageDrawable(getResources().getDrawable(R.drawable.male_icon));
            }else{
                usersex.setImageDrawable(getResources().getDrawable(R.drawable.female_icon));
            }
        }
        int age = 0;
        userage = (TextView) findViewById(R.id.user_age);
        if(me.getBirth()!=null&&(!me.getBirth().equals(""))){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birth =  sdf.parse(me.getBirth());
                Date d = new Date();
                age = d.getYear()-birth.getYear();
                userage.setText(age+"");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        sport = (FlowLayout) findViewById(R.id.sport);
        sport.setOnClickListener(this);
        music = (FlowLayout) findViewById(R.id.music);
        eat = (FlowLayout) findViewById(R.id.eat);
        tele = (FlowLayout) findViewById(R.id.televsion);
        btn_commit = (Button) findViewById(R.id.commit);
        btn_commit.setOnClickListener(this);
        book = (FlowLayout) findViewById(R.id.book);
        tral = (FlowLayout) findViewById(R.id.travl);
        if(me.getHobbyList()!=null&&me.getHobbyList().size()>0){
            List<Hobby> hList= me.getHobbyList();
            for(int i = 0;i<hList.size();i++){
                Hobby h = hList.get(i);
                //音乐 电影 户外运动 书籍 电子游戏
                if(h.getSubList()!=null&&h.getSubList().size()>0){
                    switch (h.getName()){
                        case "吃货":
                            displayHobby(h,eat,getResources().getColor(R.color.colorEating_TX),getResources().getDrawable(R.drawable.eat_shape));
                            break;
                        case "电影":
                            displayHobby(h,tele,getResources().getColor(R.color.colorMovie_TX),getResources().getDrawable(R.drawable.televation_shape));
                            break;
                        case "户外运动":
                            displayHobby(h,sport,getResources().getColor(R.color.colorSport_TX),getResources().getDrawable(R.drawable.sport_textshape));
                            break;
                        case "书籍":
                            displayHobby(h,book,getResources().getColor(R.color.colorReading_TX),getResources().getDrawable(R.drawable.book_shape));
                            break;
                        case "电子游戏":
                            displayHobby(h,tral,getResources().getColor(R.color.colorTravaling_TX),getResources().getDrawable(R.drawable.travl_shape));
                            break;
                        case "音乐":
                            displayHobby(h,music,getResources().getColor(R.color.colorMusic_TX),getResources().getDrawable(R.drawable.music_shape));
                            break;
                    }
                }
            }
        }
        addContent = (MyGrideView) findViewById(R.id.addExp);
        btn = (Button) findViewById(R.id.add_btn);
        btn.setOnClickListener(this);
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(this);
        //exp = (MyGrideView) findViewById(R.id.myExp);
        layout = (LinearLayout) findViewById(R.id.updateuser);
        layout.setOnClickListener(this);
        eList = LoginUtils.userInfo.getExpList();
        if(eList!=null&&eList.size()>0){
            adapter = new GridViewAdapter(EditActivity.this,eList);
            addContent.setAdapter(adapter);
        }
    }

    public List<String> addList(List<String> list, String str) {
        list.add(str);
        return list;
    }

    public List<String> parseArraytoList(String[] arr, List<String> list) {
        for (String str : arr) {
            list.add(str);
        }
        return list;
    }

    public String[] parseListToArr(List<String> list, String[] str) {
        return list.toArray(str);
    }

    public boolean[] adapter(List<String> list) {
        boolean[] b = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            b[i] = false;
        }
        return b;

    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                  Experience e = (Experience) msg.obj;
                  if(LoginUtils.userInfo.getExpList()!=null){
                      LoginUtils.userInfo.getExpList().add(e);
                      eList.add(0,e);
                      adapter = new GridViewAdapter(EditActivity.this,eList);
                      addContent.setAdapter(adapter);
                      ToastUtils.showShortMessage(EditActivity.this,"新增成功");
                  }else{
                      List<Experience> list = new ArrayList<>();
                      list.add(e);
                      LoginUtils.userInfo.setExpList(list);
                      eList = list;
                      adapter = new GridViewAdapter(EditActivity.this,eList);
                      addContent.setAdapter(adapter);
                  }
                    break;
                case 1: UserInfo user = (UserInfo) msg.obj;
                        LoginUtils.userInfo = user;
                        bm = BitmapFactory.decodeFile(outputFileUri.getPath());
                        Log.e("aaa",outputFileUri.getPath());
                        String url1 = ImageUtils.saveImage(bm);
                   // Glide.with(EditActivity.this).load(LoginUtils.userInfo.getHeadImage()).error(R.mipmap.ic_launcher).into(userimg);
                        userimg.setImageBitmap(bm);
                    break;
                case 2:UserInfo u = (UserInfo) msg.obj;
                        LoginUtils.userInfo = u;
                    ToastUtils.showShortMessage(EditActivity.this,"当前操作保存成功");
                    Intent intent = new Intent(EditActivity.this,MyselfinformationActivity.class);
                    startActivity(intent);
                    break;
                case 3:List<Hobby> tList = (List<Hobby>) msg.obj;
                    List<Hobby> hList = LoginUtils.userInfo.getHobbyList();
                       for(int i = 0;i<tList.size();i++){
                            CheckBox ch = new CheckBox(EditActivity.this);
                           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                           params.setMargins(5,10,0,5);
                           ch.setLayoutParams(params);
                           ch.setText(hList.get(i).getName());
                           for(Hobby h:hList){
                               if(h.getName().equals(tList.get(i).getName())){
                                   ch.setChecked(true);
                               }
                           }
                        lps.addView(ch);
                       }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }
    public void displayHobby(Hobby h, FlowLayout layout, int textColor, Drawable background){
        for(int j = 0;j<h.getSubList().size();j++){
            Hobby childHobby = h.getSubList().get(j);
            TextView tx = new TextView(EditActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tx.setText(childHobby.getName());
            tx.setTextColor(textColor);
            tx.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_btn_shape));
            params.setMargins(10,5,10,5);
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);lp.leftMargin = 5;
//        lp.rightMargin =10;
//        lp.topMargin = 5;
//        lp.bottomMargin = 5;
            layout.addView(tx,params);
        }
    }
    public boolean compare(String str,String str1){
        String [] start =str.split("-");
        String [] end = str1.split("-");
        String st = "";
        String ed = "";
        for(String s :start){
            st+=s;
        }
        for(String s1:end){
            ed+=s1;
        }
        if(Integer.parseInt(st)-Integer.parseInt(ed)>0){
            return false;
        }else{
            return true;
        }

    }
}
