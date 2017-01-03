package limeng.com.findyou;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import model.Test;
import model.dto.DataRoot;
import model.pojo.Tip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import ui.MyTipsAdapter;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ScreenUtils;
import utils.ToastUtils;

public class MyTipsActivity extends AppCompatActivity {
    private RecyclerView reView;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    reView.setVisibility(View.VISIBLE);txt.setVisibility(View.GONE);
                    Tip t = (Tip) msg.obj;
                    tlist.add(0,t);
                    pagerAdapter = new MyTipsAdapter(MyTipsActivity.this,tlist);
                    reView.setAdapter(pagerAdapter);
                    break;
                case 1:
                    tlist = (List<Tip>) msg.obj;
                    pagerAdapter = new MyTipsAdapter(MyTipsActivity.this,tlist);
                    reView.setAdapter(pagerAdapter);
                    txt.setVisibility(View.GONE);
                    break;
                case 2:
                    int position = (int) msg.obj;
                    tlist.remove(position);
                    pagerAdapter.notifyItemRemoved(position);
                    break;
                case 4:
                case 5:reView.setVisibility(View.GONE);txt.setVisibility(View.VISIBLE);break;
            }
        }
    };
    private TextView tx;
    private ImageView img;
    String content;
    private List<Tip> tlist = new ArrayList<>();
    private boolean flag = false;
    private MyTipsAdapter pagerAdapter = null;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tips);
        tx = (TextView) findViewById(R.id.back);
        txt = (TextView) findViewById(R.id.tishi);
        img = (ImageView) findViewById(R.id.write);
        reView = (RecyclerView) findViewById(R.id.myview);
        LinearLayoutManager manager = new LinearLayoutManager(MyTipsActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        reView.setLayoutManager(manager);
//        for(int i = 0;i<10;i++){
//            Test t = new Test(new Date()+"","===="+i+"个选项");
//            tlist.add(t);
//        }
        HttpUtils.request("tip/getall?pid=1&pageSize=9999&uid="+LoginUtils.userInfo.getId(), null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShortMessage(MyTipsActivity.this,"当前网络不给力");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    DataRoot<List<Tip>> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<List<Tip>>>(){});
                    List<Tip> list = root.getData();
                    if(list!=null&&list.size()>0){
                        Message msg = Message.obtain();
                        msg.what=1;
                        msg.obj = list;
                        mHandler.sendMessage(msg);
                    }else if(list!=null&&list.size()==0){
                        mHandler.sendEmptyMessage(4);
                    }else{
                        mHandler.sendEmptyMessage(5);
                    }
                }
            }
        });

        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final EditText et = new EditText(MyTipsActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(MyTipsActivity.this)
                        .setTitle("写便签")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(et.getText().toString().equals("")){
                                   return;
                                }
                                content = et.getText().toString();
                                //增加
                                RequestBody body = new FormBody.Builder().add("userInfo.id", LoginUtils.userInfo.getId()+"")
                                        .add("content",content).build();
                                HttpUtils.request("tip/add", body, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtils.showShortMessage(MyTipsActivity.this,"当前网络不给力");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if(response.code()==200){
                                            DataRoot<Tip> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<Tip>>(){});
                                            if(root.getData()!=null){
                                                Message msg = Message.obtain();
                                                msg.what=0;
                                                msg.obj = root.getData();
                                                mHandler.sendMessage(msg);
                                            }
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消",null).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            private RecyclerView.ViewHolder vh;
            //获取移动标志
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 拖拽的标记，这里允许上下左右四个方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
                        ItemTouchHelper.RIGHT;
                // 滑动的标记，这里允许左右滑动
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END ;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            //当一个Item被另外的Item替代时回调，也就是数据集的内容顺序改变
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 移动时更改列表中对应的位置并返回true
                Collections.swap(tlist, viewHolder.getAdapterPosition(), target
                        .getAdapterPosition());
                return true;
            }

            //某个Item被长按选中会被回调，当某个被长按移动的Item被释放时也调用
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    vh = viewHolder;
                    pickUpAnimation(viewHolder.itemView);
                } else {
                    if (vh != null) {
                        pickUpAnimation(vh.itemView);
                    }
                }
                flag = !flag;
            }

            //当onMove返回true的时候回调
            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
                // 移动完成后刷新列表
                pagerAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target
                        .getAdapterPosition());
            }

            //当某个Item被滑动离开屏幕之后回调
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int p  =viewHolder.getAdapterPosition();
                Log.e("position",tlist.get(viewHolder.getAdapterPosition()).getId()+"");
                // 将数据集中的数据移除
                //删除数据
                HttpUtils.request("tip/delete?id="+tlist.get(viewHolder.getAdapterPosition()).getId(), null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShortMessage(MyTipsActivity.this,"当前网络不给力");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            Message msg = Message.obtain();
                            msg.obj =p;
                            msg.what =2;
                            mHandler.sendMessage(msg);
                        }
                    }
                });
                // 刷新列表

            }

            //Item是否可以滑动
            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            //Item是否可以长按
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

        }).attachToRecyclerView(reView);

    }
    @Override
    protected void onResume() {
        super.onResume();
        ScreenUtils.doStatueImmersive(this, Color.BLACK,R.color.colorStatusBar);
    }
    private void pickUpAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 1f, 10f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
    }


}
