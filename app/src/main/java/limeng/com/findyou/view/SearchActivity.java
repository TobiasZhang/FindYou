package limeng.com.findyou.view;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import ui.SearchListAdapter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.ScreenUtils;
import utils.ToastUtils;
import widget.PullToLoaderRecylerView;

public class SearchActivity extends AppCompatActivity {
    private EditText et;
    private TextView dispose;
    private PullToLoaderRecylerView list;
    private TextView tip;
    private UserInfo user;
    private SearchListAdapter adapter;
    static int i = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1: tip.setVisibility(View.VISIBLE);
                        list.setVisibility(View.GONE);
                    Log.e("aaa","tip");break;

                case 2: adapter = new SearchListAdapter(SearchActivity.this,userList);
                    tip.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                        list.setAdapter(adapter);
                    Log.e("bbb","tip");
                    break;
                case 3: list.setFooterViewText("当前无更多数据");
                    tip.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private List<UserInfo> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        list.setLinearLayout();
        list.setFooterViewText("下拉加载更多数据");
        list.setOnPullLoadMoreListener(new PullToLoaderRecylerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                i =1;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.request("user/getall?uid=1&pid="+i+"&pageSize=10", null, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                ToastUtils.showShortMessage(SearchActivity.this,"当前网络不给力，请检查设置");
                                list.setPullLoadMoreCompleted();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                DataRoot<List<UserInfo>> dataRoot = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<UserInfo>>>(){});
                                userList = dataRoot.getData();
                                Message message = Message.obtain();
                                if(userList.size()==0){
                                    mHandler.sendEmptyMessage(1);
                                }else{
                                    mHandler.sendEmptyMessage(2);
                                }
                                list.setPullLoadMoreCompleted();
                            }
                        });
                    }
                },1000);

            }

            @Override
            public void onLoadMore() {
                i++;
                HttpUtils.request("user/getall?uid=1&pid="+i+"&pageSize=10", null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.showShortMessage(SearchActivity.this,"当前网络不给力，请检查设置");
                        list.setPullLoadMoreCompleted();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        DataRoot<List<UserInfo>> dataRoot = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<UserInfo>>>(){});
                       List<UserInfo> uList = dataRoot.getData();
                        Message message = Message.obtain();
                        if(uList.size()==0){
                            message.what=3;
                            message.obj = userList.size();
                            mHandler.sendMessage(message);
                        }else{
                            userList.addAll(uList);
                            mHandler.sendEmptyMessage(2);
                        }
                        list.setPullLoadMoreCompleted();
                    }
                });
            }
        });
        dispose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty())
                    return;
                RequestBody body = new FormBody.Builder().add("str",s.toString().trim()).build();
                HttpUtils.request("user/getall",body,new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtils.showShortMessage(SearchActivity.this,"当前网络不给力，请检查设置");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            Log.e("aa","bb");
                            DataRoot<List<UserInfo>> dataRoot = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<UserInfo>>>(){});
                            userList = dataRoot.getData();
                            Message message = Message.obtain();
                            if(userList.size()==0){
                                mHandler.sendEmptyMessage(1);
                            }else{
                                mHandler.sendEmptyMessage(2);
                            }
                        }else{
                            Log.e("aaa",response.code()+"");
                        }

                    }
                });
            }
        });

    }
    void init(){
        et = (EditText) findViewById(R.id.content);
        dispose = (TextView) findViewById(R.id.dispose);
        list = (PullToLoaderRecylerView) findViewById(R.id.listview);
        tip = (TextView) findViewById(R.id.tip);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.search_title);
    }
}
