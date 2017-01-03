package limeng.com.findyou.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tt.findyou.R;

import utils.ScreenUtils;
import widget.MyLinearLayout;

public class FindActivity extends AppCompatActivity {
    private ImageView edit,userimg,usersex;
    private TextView username,userage,back;
    private MyLinearLayout sport,music,eat,tele,book,tral;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        init();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent =new Intent(FindActivity.this,EditActivity.class);
//                intent.putExtra("uid",${uid});
//                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    void init(){
        edit = (ImageView) findViewById(R.id.edit);
        userimg = (ImageView) findViewById(R.id.user_img);
        username = (TextView) findViewById(R.id.user_name);
        usersex = (ImageView) findViewById(R.id.user_sex);
        userage = (TextView) findViewById(R.id.user_age);
        sport = (MyLinearLayout) findViewById(R.id.sport);
        music = (MyLinearLayout) findViewById(R.id.music);
        eat = (MyLinearLayout) findViewById(R.id.eat);
        tele = (MyLinearLayout) findViewById(R.id.televsion);
        book = (MyLinearLayout) findViewById(R.id.book);
        tral = (MyLinearLayout) findViewById(R.id.travl);
        back = (TextView) findViewById(R.id.back);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }
}
