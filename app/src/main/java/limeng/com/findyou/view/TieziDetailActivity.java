package limeng.com.findyou.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.tt.findyou.CommentActivity;
import com.tt.findyou.R;

import java.io.IOException;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.Experience;
import model.pojo.Topic;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;

public class TieziDetailActivity extends AppCompatActivity {
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0: tip.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.GONE);
                    break;
                case 1:
                        tip.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        t = (Topic) msg.obj;
                    if(t.getType()==1){
                        Log.e("1","1111111");
                        linename.setVisibility(View.GONE);
                        linesex.setVisibility(View.GONE);
                        aimExp.setVisibility(View.GONE);
                    }
                if(t.getType()==0){
                        linename.setVisibility(View.VISIBLE);
                        linesex.setVisibility(View.VISIBLE);
                        name.setText(t.getTargetTruename());
                        sex.setText(t.getTargetSex()==0?"男":"女");
                    if(t.getExpList()!=null&&t.getExpList().size()>0){
                        aimExp.setVisibility(View.VISIBLE);
                        List<Experience> tList = t.getExpList();
                        for(int i = 0 ;i<tList.size();i++){
                             View view = LayoutInflater.from(TieziDetailActivity.this).inflate(R.layout.tiezidetail_view,null);
                            TextView txt = (TextView) view.findViewById(R.id.name);
                            TextView start = (TextView) view.findViewById(R.id.place_type);
                            TextView end= (TextView) view.findViewById(R.id.end_date);
                            txt.setText(tList.get(i).getName());
                            start.setText(tList.get(i).getPlaceType().getName());
                            end.setText(tList.get(i).getEndDate());
                            aimExp.addView(view);
                        }
                    }else{
                        aimExp.setVisibility(View.GONE);
                        }
                    }
                    newsTitle.setText(t.getTitle());
                    newsContent.setText(t.getContent());
                    pubIns.setText(t.getUserInfo().getTruename());
                    pubTime.setText(t.getPubTime());
                    Glide.with(TieziDetailActivity.this).load(t.getUserInfo().getHeadImage()).into(userimg);
                    if(t.getImageList().size()==1){
                        newsImgDesc.setVisibility(View.VISIBLE);
                        newsImg.setVisibility(View.VISIBLE);
                        Glide.with(TieziDetailActivity.this).load(t.getImageList().get(0).getUrl()).into(newsImg);
                        newsImgDesc.setText(t.getImageList().get(0).getDescription());
                    }else if(t.getImageList().size()==2){
                        newsImgDesc.setVisibility(View.VISIBLE);
                        newsImgDesc2.setVisibility(View.VISIBLE);
                        newsImg.setVisibility(View.VISIBLE);
                        newsImg2.setVisibility(View.VISIBLE);
                        Glide.with(TieziDetailActivity.this).load(t.getImageList().get(0).getUrl()).into(newsImg);
                        newsImgDesc.setText(t.getImageList().get(0).getDescription());
                        Glide.with(TieziDetailActivity.this).load(t.getImageList().get(1).getUrl()).into(newsImg2);
                        newsImgDesc2.setText(t.getImageList().get(1).getDescription());
                    }else if(t.getImageList().size()==3){
                        newsImgDesc.setVisibility(View.VISIBLE);
                        newsImgDesc2.setVisibility(View.VISIBLE);
                        newsImgDesc3.setVisibility(View.VISIBLE);
                        newsImg.setVisibility(View.VISIBLE);
                        newsImg2.setVisibility(View.VISIBLE);
                        newsImg3.setVisibility(View.VISIBLE);
                        Glide.with(TieziDetailActivity.this).load(t.getImageList().get(0).getUrl()).into(newsImg);
                        newsImgDesc.setText(t.getImageList().get(0).getDescription());
                        Glide.with(TieziDetailActivity.this).load(t.getImageList().get(1).getUrl()).into(newsImg2);
                        newsImgDesc2.setText(t.getImageList().get(1).getDescription());
                        Glide.with(TieziDetailActivity.this).load(t.getImageList().get(2).getUrl()).into(newsImg3);
                        newsImgDesc3.setText(t.getImageList().get(2).getDescription());
                    }else{
                        newsImgDesc.setVisibility(View.GONE);
                        newsImgDesc2.setVisibility(View.GONE);
                        newsImgDesc3.setVisibility(View.GONE);
                        newsImg.setVisibility(View.GONE);
                        newsImg2.setVisibility(View.GONE);
                        newsImg3.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    //标题 姓名 时间 描述123
    //用户头像，照片1 照片2 照片3
    private TextView newsTitle,pubIns,pubTime,newsImgDesc,newsImgDesc2,newsImgDesc3,newsContent,name,sex,back;
    private ImageView userimg,newsImg,newsImg2,newsImg3;
    private int id;
    private Topic t;
    int uid;
    private ScrollView relativeLayout;
    private LinearLayout linename,linesex,aimExp;
    private TextView tip;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiezi_detail);
        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = getIntent().getIntExtra("tid",0);
        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginUtils.userInfo.getId()!=uid){
                    Intent intent = new Intent(TieziDetailActivity.this,OtherMainActivity.class);
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(TieziDetailActivity.this,MyselfinformationActivity.class);
                    startActivity(intent);
                }

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TieziDetailActivity.this,CommentActivity.class);
                intent.putExtra("topic",t);
                startActivity(intent);
            }
        });
        HttpUtils.request("topic/get?&id=" + id, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShortMessage(TieziDetailActivity.this,"当前网络不给力");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        DataRoot<Topic> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<Topic>>(){});
                        if(root.getData()!=null){
                            Topic t = root.getData();
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.obj = t;
                            uid = t.getUserInfo().getId();
                            mhandler.sendMessage(msg);
                        }else{
                            mhandler.sendEmptyMessage(0);
                        }

                    }
            }
        });
    }
    void init(){
        aimExp = (LinearLayout) findViewById(R.id.aimExp);
        newsTitle = (TextView) findViewById(R.id.newsTitle);
        pubIns = (TextView) findViewById(R.id.pubIns);
        pubTime = (TextView) findViewById(R.id.pubTime);
        newsImgDesc = (TextView) findViewById(R.id.newsImgDesc);
        newsImgDesc2 = (TextView) findViewById(R.id.newsImgDesc2);
        newsImgDesc3 = (TextView) findViewById(R.id.newsImgDesc3);
        newsContent = (TextView) findViewById(R.id.newsContent);
        userimg = (ImageView) findViewById(R.id.userimg);
        newsImg = (ImageView) findViewById(R.id.newsImg);
        newsImg2 = (ImageView) findViewById(R.id.newsImg2);
        newsImg3 = (ImageView) findViewById(R.id.newsImg3);
        tip = (TextView) findViewById(R.id.tip);
        relativeLayout = (ScrollView) findViewById(R.id.truecontent);
        linename = (LinearLayout) findViewById(R.id.linename);
        linesex = (LinearLayout) findViewById(R.id.lineage);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        btn = (Button)findViewById(R.id.comment);
        back = (TextView) findViewById(R.id.back);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }
}
