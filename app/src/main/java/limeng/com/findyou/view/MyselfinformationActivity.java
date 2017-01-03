package limeng.com.findyou.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;
import com.tt.findyou.utils.TimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.Hobby;
import model.pojo.LeaveMsg;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ui.GridViewAdapter;
import ui.LeaveMessageAdapter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;
import widget.FlowLayout;
import widget.MyGrideView;

public class MyselfinformationActivity extends AppCompatActivity {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==7){
                leaveMessageAdapter = new LeaveMessageAdapter(MyselfinformationActivity.this,lList);
                comment.setAdapter(leaveMessageAdapter);
                comment.setVisibility(View.VISIBLE);
                tips.setVisibility(View.GONE);
                findmore.setVisibility(View.GONE);
            }
            else if(msg.what==5){
                lList = lList.subList(0,5);
                tips.setVisibility(View.GONE);
                findmore.setVisibility(View.VISIBLE);
                findmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyselfinformationActivity.this,LeaveMsgActivity.class);
                        intent.putExtra("uid",LoginUtils.userInfo.getId());
                        startActivity(intent);
                    }
                });
                leaveMessageAdapter = new LeaveMessageAdapter(MyselfinformationActivity.this,lList);
                comment.setAdapter(leaveMessageAdapter);
                comment.setVisibility(View.VISIBLE);

            }else if(msg.what==6){
                comment.setVisibility(View.GONE);
                tips.setVisibility(View.VISIBLE);
                findmore.setVisibility(View.GONE);
            }
        }
    };
    private ImageView edit,userimg,usersex;
    private List<LeaveMsg> lList = new ArrayList<>();
    private int uid = LoginUtils.userInfo.getId();
    UserInfo me = LoginUtils.userInfo;
    private TextView username,userage,back;
    private MyGrideView grideView,comment;
    private TextView tips;
    private Button findmore;
    private GridViewAdapter adapter;
    private LeaveMessageAdapter leaveMessageAdapter;
    private FlowLayout sport,music,eat,tele,book,tral;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myselfinformation);
        init();
        HttpUtils.request("leaveMsg/getall?pid=1&pageSize=10&uid="+LoginUtils.userInfo.getId(), null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShortMessage(MyselfinformationActivity.this,"当前网络不给力");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    DataRoot<List<LeaveMsg>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<LeaveMsg>>>(){});
                    if(root.getData()!=null&&root.getData().size()!=0){
                        lList = root.getData();
                        if(lList.size()<=5){
                            mHandler.sendEmptyMessage(7);
                        }else{
                            mHandler.sendEmptyMessage(5);
                        }

                    }else{
                        mHandler.sendEmptyMessage(6);
                    }
                }
            }
        });




        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MyselfinformationActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //赋值
        if(me.getHeadImage()!=null&&(!me.getHeadImage().equals(""))){
            Glide.with(MyselfinformationActivity.this).load(me.getHeadImage()).into(userimg);
        }
        if(me.getTruename()!=null&&(!me.getTruename().equals("")))
        username.setText(me.getTruename());
        if(me.getSex()!=null){
            if(me.getSex()==0){
                usersex.setImageDrawable(getResources().getDrawable(R.drawable.male_icon));
            }else{
                usersex.setImageDrawable(getResources().getDrawable(R.drawable.female_icon));
            }
        }
        userage.setText(TimeUtils.parseAge(me.getBirth())+"");
        if(me.getExpList()!=null&&me.getExpList().size()>0){
            adapter = new GridViewAdapter(MyselfinformationActivity.this,me.getExpList());
            grideView.setAdapter(adapter);
        }
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
    }
    void init(){
        edit = (ImageView) findViewById(R.id.edit);
        userimg = (ImageView) findViewById(R.id.user_img);
        username = (TextView) findViewById(R.id.user_name);
        usersex = (ImageView) findViewById(R.id.user_sex);
        userage = (TextView) findViewById(R.id.user_age);
        grideView = (MyGrideView) findViewById(R.id.myExp);
        sport = (FlowLayout) findViewById(R.id.sport);
        music = (FlowLayout) findViewById(R.id.music);
        eat = (FlowLayout) findViewById(R.id.eat);
        tele = (FlowLayout) findViewById(R.id.televsion);
        book = (FlowLayout) findViewById(R.id.book);
        tral = (FlowLayout) findViewById(R.id.travl);
        back = (TextView) findViewById(R.id.back);
        comment = (MyGrideView) findViewById(R.id.leave_message);
        findmore = (Button) findViewById(R.id.lookmore);
        tips = (TextView) findViewById(R.id.tips);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }

    public void displayHobby(Hobby h, FlowLayout layout, int textColor, Drawable background){
        for(int j = 0;j<h.getSubList().size();j++){
            Hobby childHobby = h.getSubList().get(j);
            TextView tx = new TextView(MyselfinformationActivity.this);
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
}
