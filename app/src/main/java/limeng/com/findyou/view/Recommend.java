package limeng.com.findyou.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import model.pojo.TuiJian;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ui.RecyclerAdapeter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ToastUtils;
import widget.PullToLoaderRecylerView;

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class Recommend extends Fragment {
    private View view;
    private PullToLoaderRecylerView mRecyclerView;
    private List<TuiJian> tList = new ArrayList<>();
    private RecyclerAdapeter recyclerAdapeter;
    private TextView tx;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                mRecyclerView.setVisibility(View.VISIBLE);
                tx.setVisibility(View.GONE);
                tList = (List<TuiJian>) msg.obj;
                recyclerAdapeter = new RecyclerAdapeter(getActivity(),tList);
                mRecyclerView.setAdapter(recyclerAdapeter);
            }
            if(msg.what==1){
                mRecyclerView.setVisibility(View.GONE);
                tx.setVisibility(View.VISIBLE);
            }
            if(msg.what==2){
                mRecyclerView.setVisibility(View.VISIBLE);
                tx.setVisibility(View.GONE);
                tList = (List<TuiJian>) msg.obj;
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerAdapeter = new RecyclerAdapeter(getActivity(),tList);
                        mRecyclerView.setAdapter(recyclerAdapeter);
                        mRecyclerView.setPullLoadMoreCompleted();
                    }
                },1000);
            }
            if(msg.what==3){
                mRecyclerView.setPullLoadMoreCompleted();
                mRecyclerView.setVisibility(View.GONE);
                tx.setVisibility(View.VISIBLE);
            }

        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recommand_fragment,null);
        mRecyclerView = (PullToLoaderRecylerView)view.findViewById(R.id.recycler);
        tx = (TextView) view.findViewById(R.id.tip);
        HttpUtils.request("mix/getTuiJian?uid="+ LoginUtils.userInfo.getId(), null, new Callback() {
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
                    DataRoot<List<TuiJian>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<TuiJian>>>(){});
                    if(root.getData()!=null&&root.getData().size()>0){
                        List<TuiJian> list = root.getData();
                        Message msg = Message.obtain();
                        msg.obj = list;
                        msg.what = 0;
                        mHandler.sendMessage(msg);
                    }else{
                        mHandler.sendEmptyMessage(1);
                    }
                }
            }
        });
        //LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setPushRefreshEnable(false);
        mRecyclerView.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.empty_view, null));
        mRecyclerView.setLinearLayout();
        mRecyclerView.setFooterViewText("正在加载更多数据");
        mRecyclerView.setOnPullLoadMoreListener(new PullToLoaderRecylerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                HttpUtils.request("mix/getTuiJian?uid="+ LoginUtils.userInfo.getId(), null, new Callback() {
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
                            DataRoot<List<TuiJian>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<TuiJian>>>(){});
                            if(root.getData()!=null&&root.getData().size()>0){
                                List<TuiJian> list = root.getData();
                                Message msg = Message.obtain();
                                msg.obj = list;
                                msg.what = 2;
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
                //加载更多
            }
        });
        return view;
    }
}
