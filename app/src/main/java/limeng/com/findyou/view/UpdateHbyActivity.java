package limeng.com.findyou.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.Hobby;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import ui.CkAdapter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;

public class UpdateHbyActivity extends AppCompatActivity {
    private TextView t,add;
    private Button btn;
    private ListView lp;
    private CkAdapter adapter;
    int i;
    private List<Hobby> list = new ArrayList();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what ==3){
                Hobby h = (Hobby) msg.obj;
                list.add(h);
                LoginUtils.userInfo.getHobbyList().get(i-1).getSubList().add(h);
                adapter = new CkAdapter(list,UpdateHbyActivity.this,LoginUtils.userInfo.getHobbyList().get(i-1).getSubList());
                lp.setAdapter(adapter);
            }
            if(msg.what==0){
                list = (List<Hobby>) msg.obj;//所有list
                List<Hobby> hList = LoginUtils.userInfo.getHobbyList().get(i-1).getSubList();
                Log.e("list.size",list.size()+"");
                adapter = new CkAdapter(list,UpdateHbyActivity.this,hList);
                lp.setAdapter(adapter);
            }
            if(msg.what==2){
                List<Hobby> hobbyList= (List<Hobby>) msg.obj;
                LoginUtils.userInfo.getHobbyList().get(i-1).setSubList(list);
                Intent intent = new Intent(UpdateHbyActivity.this,EditActivity.class);
                startActivity(intent);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hby);
        i = getIntent().getIntExtra("hid",0);
        lp = (ListView) findViewById(R.id.contentView);
        btn = (Button) findViewById(R.id.commit);
        t = (TextView) findViewById(R.id.t);
        add = (TextView) findViewById(R.id.addmyhobby);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(UpdateHbyActivity.this);
                new AlertDialog.Builder(UpdateHbyActivity.this).setTitle("添加自定义标签").setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //绑定用户并且返回绑定的hobby对象
                        RequestBody body = new FormBody.Builder().add("name",et.getText().toString())
                                .add("hobby.id",i+"").add("userId",LoginUtils.userInfo.getId()+"").build();
                        HttpUtils.request("hobby/addAndBind", body, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                DataRoot<Hobby> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<Hobby>>(){});
                                Message msg = Message.obtain();
                                msg.obj = root.getData();
                                msg.what = 3;
                                handler.sendMessage(msg);
                            }
                        });
                    }
                }).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              final List<Hobby> list = adapter.getHobby();

                FormBody.Builder builder = new FormBody.Builder();
                for(Hobby b:list){
                    builder.add("hobbyId",b.getId()+"");
                }
                builder.add("userId",LoginUtils.userInfo.getId()+"");
              RequestBody body =   builder.build();
                HttpUtils.request("hobby/bind", body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            Message msg = Message.obtain();
                            msg.obj = list;
                            msg.what =2;
                            handler.sendMessage(msg);
                        }
                    }
                });

            }
        });


        list = LoginUtils.userInfo.getHobbyList().get(i).getSubList();
        HttpUtils.request("/hobby/getall?hobbyParentId=" +i + "&uid=" + LoginUtils.userInfo.getId(), null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DataRoot<List<Hobby>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<Hobby>>>(){});
                Message msg = Message.obtain();
                msg.obj = root.getData();
                msg.what=0;
                handler.sendMessage(msg);
            }
        });
    }
}
