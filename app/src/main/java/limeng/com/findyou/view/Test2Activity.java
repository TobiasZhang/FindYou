package limeng.com.findyou.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.tt.findyou.R;

import utils.LocationUtils;
import widget.FlowLayout;
import widget.MyLinearLayout;

public class Test2Activity extends AppCompatActivity {
    FlowLayout layout;
    private TextView tx;
    String [] str = {"aa","bbbbbbb"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        tx = (TextView) findViewById(R.id.aa);
        LocationUtils.doPosition(Test2Activity.this,new AMapLocationClient(Test2Activity.this),tx);
        layout = (FlowLayout) findViewById(R.id.sport);
        for(String s:str){
            TextView tx = new TextView(Test2Activity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tx.setText(s);
            tx.setTextColor(Color.BLUE);
            tx.setBackgroundDrawable(getResources().getDrawable(R.drawable.sport_textshape));
            params.setMargins(0,5,5,5);
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);lp.leftMargin = 5;
//        lp.rightMargin =10;
//        lp.topMargin = 5;
//        lp.bottomMargin = 5;
            layout.addView(tx,params);
        }
    }
}
