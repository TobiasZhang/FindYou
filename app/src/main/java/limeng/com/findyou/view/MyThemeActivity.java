package limeng.com.findyou.view;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.Topic;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ui.MyThemeAdapter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;
import widget.PullToLoaderRecylerView;

public class MyThemeActivity extends AppCompatActivity {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Log.e("aaaaaaaa","==========");
                    tlist = (List<Topic>) msg.obj;
                    adapter = new MyThemeAdapter(MyThemeActivity.this,tlist);
                    view.setAdapter(adapter);break;
                case 1:
                    tlist = (List<Topic>) msg.obj;
                    adapter = new MyThemeAdapter(MyThemeActivity.this,tlist);
                    view.setAdapter(adapter);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setPullLoadMoreCompleted();
                        }
                    },1000);
                    break;
                case 2:
                    tlist.addAll((List<Topic>) msg.obj);
                    adapter = new MyThemeAdapter(MyThemeActivity.this,tlist);
                    view.setAdapter(adapter);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setPullLoadMoreCompleted();
                        }
                    },1000);
                    break;
                case 3:
                    ToastUtils.showShortMessage(MyThemeActivity.this,"当前无更多数据");
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setPullLoadMoreCompleted();
                        }
                    },1000);
                    break;
            }
        }
    };
    private PullToLoaderRecylerView view;
    private MyThemeAdapter adapter;
    private TextView tx;
    private List<Topic> tlist = new ArrayList<>();
    private static int i = 1;
    int uid = LoginUtils.userInfo.getId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_theme);
        tx = (TextView) findViewById(R.id.back);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        view = (PullToLoaderRecylerView) findViewById(R.id.myview);
        view.setFooterViewText("下拉加载更多");
        view.setLinearLayout();
        view.setEmptyView(getLayoutInflater().inflate(R.layout.empty_layout,null));

        adapter = new MyThemeAdapter(MyThemeActivity.this,new ArrayList<Topic>());
        view.setAdapter(adapter);
        HttpUtils.request("topic/getall?uid="+LoginUtils.userInfo.getId()+"&pid=1&pageSize=5", null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShortMessage(MyThemeActivity.this,"当前网络不可用");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    Log.e("onResume","ok");
                    DataRoot<List<Topic>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<Topic>>>(){});
                    Log.e("onResume====",""+root.getData().size());
                    if(root.getData()!=null&&root.getData().size()>0){
                        List<Topic> tlist = root.getData();
                        Message msg = Message.obtain();
                        msg.what=0;
                        msg.obj = tlist;
                        mHandler.sendMessage(msg);
                    }else{
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                view.setVisibility(View.GONE);
//                            }
//                        });
                    }
                }
            }
        });
        view.setOnPullLoadMoreListener(new PullToLoaderRecylerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                i = 1;
                HttpUtils.request("topic/getall?uid="+LoginUtils.userInfo.getId()+"&pid=1&pageSize=5", null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(MyThemeActivity.this,"当前网络不可用");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        DataRoot<List<Topic>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<Topic>>>(){});
                        Message msg = Message.obtain();
                        msg.what=1;
                        msg.obj = root.getData();
                        mHandler.sendMessage(msg);
                    }
                });
            }

            @Override
            public void onLoadMore() {
                i++;
                HttpUtils.request("topic/getall?uid="+LoginUtils.userInfo.getId()+"&pid="+i+"&pageSize=5", null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(MyThemeActivity.this,"当前网络不可用");
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        DataRoot<List<Topic>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<Topic>>>(){});
                        if(root.getData()!=null&&root.getData().size()>0){
                            Message msg = Message.obtain();
                            msg.what=2;
                            msg.obj = root.getData();
                            mHandler.sendMessage(msg);
                        }else{
                            mHandler.sendEmptyMessage(3);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.setVisibility(View.VISIBLE);
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);

    }
}
