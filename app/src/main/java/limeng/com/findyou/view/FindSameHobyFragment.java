package limeng.com.findyou.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.Topic;
import model.pojo.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ui.FindOldFragmentAdapter;
import ui.FindSameFragmentAdapter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ToastUtils;
import widget.PullToLoaderRecylerView;

/**
 * Created by Administrator on 2016/12/16 0016.
 */
public class FindSameHobyFragment extends Fragment {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    myview.setVisibility(View.GONE);
                    tx.setVisibility(View.VISIBLE);
                    break;

                case 1:
                    myview.setVisibility(View.VISIBLE);
                    tx.setVisibility(View.GONE);
                    tlist = (List<Topic>) msg.obj;
                    adapter = new FindSameFragmentAdapter(tlist,getActivity());
                    myview.setAdapter(adapter);
                    break;
                case 2:
                    myview.setVisibility(View.VISIBLE);
                    tx.setVisibility(View.GONE);
                    tlist = (List<Topic>) msg.obj;
                    adapter = new FindSameFragmentAdapter(tlist,getActivity());
                    myview.setAdapter(adapter);
                    myview.setPullLoadMoreCompleted();
                    break;

                case 3:
                    myview.setPullLoadMoreCompleted();
                    break;
                case 4:
                    myview.setVisibility(View.VISIBLE);
                    tx.setVisibility(View.GONE);
                    tlist.addAll((List<Topic>) msg.obj);
                    adapter = new FindSameFragmentAdapter(tlist,getActivity());
                    myview.setAdapter(adapter);
                    myview.setPullLoadMoreCompleted();
                    break;
                case 5:
                    ToastUtils.showShortMessage(getActivity(),"已无更多数据");
                    myview.setPullLoadMoreCompleted();
                    break;
            }
        }
    };
    private PullToLoaderRecylerView myview;
    private FindSameFragmentAdapter adapter;
    private List<Topic> tlist = new ArrayList<>();
    private static int i = 1;
    private TextView tx;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.findnewfragment_layout,null);
        tx = (TextView) view.findViewById(R.id.tip);
        myview = (PullToLoaderRecylerView) view.findViewById(R.id.myview);
        myview.setLinearLayout();
        myview.setFooterViewText("正在加载更多数据");
        myview.setOnPullLoadMoreListener(new PullToLoaderRecylerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                i= 1;
                HttpUtils.request("topic/getall?pid="+i+"&pageSize=5&type=1", null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(getActivity(),"当前网络不给力");
                                myview.setPullLoadMoreCompleted();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<List<Topic>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<Topic>>>(){});
                            if(root.getData()!=null&&root.getData().size()>0){
                                Message msg = Message.obtain();
                                msg.what = 2;
                                msg.obj = root.getData();
                                mHandler.sendMessage(msg);
                            }else{
                                mHandler.sendEmptyMessage(3);
                            }
                        }

                    }
                });
            }

            @Override
            public void onLoadMore() {
                i++;
                HttpUtils.request("topic/getall?pid="+i+"&pageSize=5&type=1", null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(getActivity(),"当前网络不给力");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<List<Topic>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<Topic>>>(){});
                            if(root.getData()!=null&&root.getData().size()>0){
                                Message msg = Message.obtain();
                                msg.what = 4;
                                msg.obj = root.getData();
                                mHandler.sendMessage(msg);
                            }else{
                                mHandler.sendEmptyMessage(5);
                                ;
                            }
                        }

                    }
                });
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        HttpUtils.request("topic/getall?pid=1&pageSize=5&type=1", null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShortMessage(getActivity(),"当前网络不给力");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    DataRoot<List<Topic>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<Topic>>>(){});
                    if(root.getData()!=null&&root.getData().size()>0){
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = root.getData();
                        mHandler.sendMessage(msg);
                    }else{
                        mHandler.sendEmptyMessage(0);
                    }
                }

            }
        });
    }
}
