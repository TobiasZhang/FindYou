package limeng.com.findyou.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tt.findyou.R;

import utils.ScreenUtils;

public class Noviceguidance extends AppCompatActivity {
    private TextView tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noviceguidance);
        tx = (TextView) findViewById(R.id.back);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }

}
