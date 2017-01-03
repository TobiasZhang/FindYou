package limeng.com.findyou.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tt.findyou.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Test;
import model.pojo.Experience;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ui.GridViewAdapter;
import utils.HttpUtils;
import utils.ImageUtils;
import utils.ScreenUtils;
import utils.ToastUtils;
import widget.MyGrideView;
import widget.MyLinearLayout;

public class TestActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar bar;
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    String[] sports = {"游泳", "跑步", "瑜伽", "单车", "篮球", "足球", "滑板", "滑雪", "兵乓球", "羽毛球"};
    List<String> list = new ArrayList<>();
    List<String> trueList = new ArrayList<>();
    LinearLayout layout;
    private ImageView userimg, usersex;
    private TextView username, userage, back;
    private MyLinearLayout sport, music, eat, tele, book, tral;
    private TextView tx;
    private Button btn;
    private LinearLayout addContent;
    AlertDialog alertDialog = null;
    String item = "";
    private View view;
    public static int ImgFlag = 0;
    private Uri outputFileUri;
    private EditText et;
    private TextView start, end;
    private Bitmap bm;
    final int CUT_OK = 1;
    private int start_year = 1998;
    private int start_month = 8;
    private int start_day = 20;
    private int end_year;
    private int end_month;
    private int end_day;
    String name = "";
    String starttime = "";
    String endtime = "";
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
        parseArraytoList(sports, trueList);
        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        File file = new File(path);
                        outputFileUri = Uri.fromFile(file);
                        Intent intent2 = new Intent("com.android.camera.action.CROP");
                        intent2.setDataAndType(outputFileUri, "image/*");
                        intent2.putExtra("crop", "true");
                        // aspectX aspectY 是宽高的比例
                        intent2.putExtra("aspectX", 1);
                        intent2.putExtra("aspectY", 1);
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
                    bm = BitmapFactory.decodeFile(outputFileUri.getPath());
//                    try {
//                        bm = MediaStore.Images.Media.getBitmap(getContentResolver(),outputFileUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    String url1 = ImageUtils.saveImage(bm);
                    userimg.setImageBitmap(bm);
                    ImgFlag = 1;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                AlertDialog exp_items = new AlertDialog.Builder(TestActivity.this)
                        .setTitle("添加经历").setItems(new String[]{"幼儿园", "学前班", "小学", "初中", "大学/专科", "大学/本科", "大学/研究生", "大学/博士生", "军队", "公司/机构/单位"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                id = which + 2;
                                view = LayoutInflater.from(TestActivity.this).inflate(R.layout.myresume_view, null);
                                et = (EditText) view.findViewById(R.id.kindname);
                                start = (TextView) view.findViewById(R.id.starttime);
                                end = (TextView) view.findViewById(R.id.endtimetime);
                                start.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new DatePickerDialog(TestActivity.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                start_year = year;
                                                start_month = monthOfYear;
                                                start_day = dayOfMonth;
                                                String y = year + "";
                                                String month = monthOfYear + 1 >= 10 ? (monthOfYear + 1) + "" : "0" + (monthOfYear + 1);
                                                String day = dayOfMonth >= 10 ? dayOfMonth + "" : "0" + dayOfMonth;
                                                start.setText(y + "/" + month + "/" + day);
                                                start.setTextColor(Color.BLACK);

                                            }
                                        }, 2000, 1, 2).show();
                                    }
                                });
                                end.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new DatePickerDialog(TestActivity.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                end_year = year;
                                                end_month = monthOfYear + 1;
                                                end_day = dayOfMonth;
                                                String y = year + "";
                                                String month = monthOfYear + 1 >= 10 ? (monthOfYear + 1) + "" : "0" + (monthOfYear + 1);
                                                String day = dayOfMonth >= 10 ? dayOfMonth + "" : "0" + dayOfMonth;
                                                end.setText(y + "/" + month + "/" + day);
                                                end.setTextColor(Color.BLACK);
                                            }
                                        }, start_year, start_month, start_day).show();
                                        // datePickerDialog.getDatePicker().setMinDate(new Date(start_year,start_month,start_day).getTime());
                                        //datePickerDialog.show();

                                    }
                                });
                                new AlertDialog.Builder(TestActivity.this).setView(view)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                name = et.getText().toString().trim();
                                                starttime = start.getText().toString().trim();
                                                endtime = end.getText().toString().trim();
                                                final View view = LayoutInflater.from(TestActivity.this).inflate(R.layout.exp_items, null);
                                                TextView txt = (TextView) view.findViewById(R.id.name);
                                                TextView start = (TextView) view.findViewById(R.id.start_date);
                                                TextView end = (TextView) view.findViewById(R.id.end_date);
                                                TextView delete = (TextView) view.findViewById(R.id.delete);
                                                txt.setText("1");
                                                start.setText(name);
                                                end.setText(starttime);
                                                delete.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        AlertDialog dialogs = new AlertDialog.Builder(TestActivity.this).setTitle("删除经历").setMessage("确定删除么？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //请求服务器删除数据
                                                                addContent.removeView(view);
                                                            }
                                                        }).show();
                                                    }
                                                });
                                                addContent.addView(view);
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
            case R.id.user_age:
                //跳转到个人资料的更改页面
                break;
            case R.id.sport:
                AlertDialog alertDialog = new AlertDialog.Builder(TestActivity.this)
                        .setTitle("运动").setItems(new String[]{"创建自己的标签"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final EditText et1 = new EditText(TestActivity.this);
                                new AlertDialog.Builder(TestActivity.this).setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        item = et1.getText().toString();
                                        trueList.add(item);
                                    }
                                }).setNegativeButton("取消", null).show();
                            }
                        }).setMultiChoiceItems(parseListToArr(trueList, new String[]{}), adapter(trueList), new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    list.add(sports[which]);
                                }
                            }
                        }).setPositiveButton("确定", null).show();
            case R.id.music:
            case R.id.eat:
            case R.id.book:
            case R.id.travl:
            case R.id.televsion:
                break;
            case R.id.updateuser:
                Intent intent = new Intent(TestActivity.this, UpdateActivity.class);
                //intent.putExtra("uid",${uid});
                startActivity(intent);
        }
        alertDialog = null;
    }

    void init() {
        userimg = (ImageView) findViewById(R.id.user_img);
        username = (TextView) findViewById(R.id.user_name);
        usersex = (ImageView) findViewById(R.id.user_sex);
        userage = (TextView) findViewById(R.id.user_age);
        sport = (MyLinearLayout) findViewById(R.id.sport);
        sport.setOnClickListener(this);
        music = (MyLinearLayout) findViewById(R.id.music);
        eat = (MyLinearLayout) findViewById(R.id.eat);
        tele = (MyLinearLayout) findViewById(R.id.televsion);
        book = (MyLinearLayout) findViewById(R.id.book);
        addContent = (LinearLayout) findViewById(R.id.addExp);
        btn = (Button) findViewById(R.id.add_btn);
        btn.setOnClickListener(this);
        tral = (MyLinearLayout) findViewById(R.id.travl);
        back = (TextView) findViewById(R.id.back);

        layout = (LinearLayout) findViewById(R.id.updateuser);
        layout.setOnClickListener(this);
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

}