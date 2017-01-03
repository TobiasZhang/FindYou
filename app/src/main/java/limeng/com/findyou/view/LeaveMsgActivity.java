package limeng.com.findyou.view;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.LeaveMsg;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ui.LMDetailAdapter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.ScreenUtils;
import utils.ToastUtils;
import widget.PullToLoaderRecylerView;

public class LeaveMsgActivity extends AppCompatActivity {
    private PullToLoaderRecylerView view;
    private static int i = 1;
    private TextView tx;
    private LMDetailAdapter adapter;
    private List<LeaveMsg> lList = new ArrayList<>();
    private int id;
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new LMDetailAdapter(LeaveMsgActivity.this,lList);
                        view.setAdapter(adapter);
                        view.setPullLoadMoreCompleted();
                    }
                },1000);
            }
            if(msg.what==1){
                lList.addAll((List<LeaveMsg>)msg.obj);
                //加载
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new LMDetailAdapter(LeaveMsgActivity.this,lList);
                        view.setAdapter(adapter);
                        view.setPullLoadMoreCompleted();
                    }
                },1000);

            }
            if(msg.what==2){
                adapter = new LMDetailAdapter(LeaveMsgActivity.this,lList);
                view.setAdapter(adapter);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_msg);
        tx = (TextView) findViewById(R.id.back);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = getIntent().getIntExtra("uid",0);
        view = (PullToLoaderRecylerView) findViewById(R.id.pull_view);
        view.setFooterViewText("下拉查看更多");
        view.setLinearLayout();
        HttpUtils.request("leaveMsg/getall?pid=" + i + "&pageSize=10&uid=" + id, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShortMessage(LeaveMsgActivity.this,"当前网络不可用");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    DataRoot<List<LeaveMsg>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<LeaveMsg>>>(){});
                    if(root.getData()!=null&&root.getData().size()!=0){
                        lList = root.getData();
                        mhandler.sendEmptyMessage(2);
                    }
                }
            }
        });
        view.setOnPullLoadMoreListener(new PullToLoaderRecylerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                i = 1;
                HttpUtils.request("leaveMsg/getall?pid="+i+"&pageSize=10&uid="+id, null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(LeaveMsgActivity.this,"当前网络不可用");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<List<LeaveMsg>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<LeaveMsg>>>(){});
                            if(root.getData()!=null&&root.getData().size()!=0){
                                lList = root.getData();
                                mhandler.sendEmptyMessage(0);
                            }
                        }
                    }
                });
            }

            @Override
            public void onLoadMore() {
                i++;
                HttpUtils.request("leaveMsg/getall?pid="+i+"&pageSize=10&uid="+id, null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(LeaveMsgActivity.this,"当前网络不可用");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<List<LeaveMsg>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<LeaveMsg>>>(){});
                            if(root.getData()!=null&&root.getData().size()!=0){
                                List<LeaveMsg> list = root.getData();
                                Message msg = Message.obtain();
                                msg.obj = list;
                                msg.what= 1;
                                mhandler.sendMessage(msg);
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.setFooterViewText("当前无更多数据");
                                        ToastUtils.showShortMessage(LeaveMsgActivity.this,"当前无更多数据");
                                        view.setPullLoadMoreCompleted();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }
}
