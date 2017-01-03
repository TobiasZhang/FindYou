package com.tt.findyou;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.tt.findyou.utils.EmptyUtil;
import com.tt.findyou.utils.HttpUtils;
import com.tt.findyou.utils.Utils;
import com.tt.findyou.widget.AutoLoadRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.Comment;
import model.pojo.Topic;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import utils.LoginUtils;

public class CommentActivity extends AppCompatActivity {
    AutoLoadRecyclerView commentList;
    List<Comment> list;
    CommentAdapter commentAdapter;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    commentAdapter.loadMore(list);
                    break;
                case 2:
                    commentAdapter.refresh(list);
                    commentList.smoothScrollToPosition(0);
                    break;
            }
        }
    };

    EditText commentTxt;
    Button commentBtn;

    Topic topic;

    View replyInfo;
    TextView commentTarget, commentTargetContent;

    int pid = 1;
    int pageSize = 5;

    View back;
    TextView topicTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        topic = (Topic) getIntent().getSerializableExtra("topic");


        commentList = (AutoLoadRecyclerView) findViewById(R.id.commentList);
        commentAdapter = new CommentAdapter();
        commentList.setLayoutManager(new LinearLayoutManager(this));
        commentList.setAdapter(commentAdapter);

        commentTxt = (EditText) findViewById(R.id.commentTxt);
        commentBtn = (Button) findViewById(R.id.commentBtn);
        replyInfo = findViewById(R.id.replyInfo);
        replyInfo.setVisibility(View.GONE);
        commentTarget = (TextView) findViewById(R.id.replyTarget);
        commentTargetContent = (TextView) findViewById(R.id.replyTargetContent);
        commentTarget.setTag(0);

        commentList.setOnLoadListener(new AutoLoadRecyclerView.OnLoadListener() {
            @Override
            public void onLoad() {
                loadMore(pid,pageSize);
            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = commentTxt.getText().toString().trim();
                if(EmptyUtil.isEmpty(commentContent)){
                    Utils.toast(CommentActivity.this,"内容不能为空！");
                    return;
                }

                HttpUtils.request(
                        "comment/add",
                        new FormBody.Builder()
                                .add("content", commentContent)
                                .add("userInfo.id", LoginUtils.userInfo.getId() + "")
                                .add("topic.id", topic.getId() + "")
                                .add("comment.id", String.valueOf(commentTarget.getTag())).build(),
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Utils.toast(CommentActivity.this,"网络异常，回复失败");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                DataRoot<List<Comment>> dataRoot = HttpUtils.parseJson(response.body().string(),new TypeToken<DataRoot<List<Comment>>>(){});
                                if(dataRoot.getResult().equals("ok")){
                                    List<Comment> newCommentList = dataRoot.getData();
                                    list = newCommentList;
                                    handler.sendEmptyMessage(2);
                                    pid = 2;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            commentTxt.setText("");
                                            commentTxt.setTag(0);
                                            replyInfo.setVisibility(View.GONE);
                                            Utils.toast(CommentActivity.this,"回复成功");
                                        }
                                    });
                                }
                            }
                        });

            }
        });

        loadMore(pid,pageSize);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        topicTitle = (TextView) findViewById(R.id.topicTitle);
        topicTitle.setText(topic.getTitle());
    }
    private void loadMore(final int pid, int pageSize){
        HttpUtils.request("comment/getall",
                new FormBody.Builder()
                        .add("tid", topic.getId() + "")
                        .add("pid",pid+"")
                        .add("pageSize",pageSize+"")
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Utils.toast(CommentActivity.this,"请求评论失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<List<Comment>> dataRoot = HttpUtils.parseJson(response.body().string(),new TypeToken<DataRoot<List<Comment>>>(){});
                            if(dataRoot.getResult().equals("ok")){
                                //请求成功
                                list = dataRoot.getData();
                                handler.sendEmptyMessage(1);
                                System.out.println(list.size()+"----startWith---"+pid);
                                CommentActivity.this.pid++;
                            }else{
                                Utils.toast(CommentActivity.this,dataRoot.getResult());
                                handler.sendEmptyMessage(10);
                            }
                        }else{
                            Utils.toast(CommentActivity.this,"请求评论失败---"+response.code());
                        }
                    }
                });
    }


    class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{
        private List<Comment> list = new ArrayList<>();

        public void refresh(List<Comment> newList){
            list.clear();
            list.addAll(newList);
            notifyDataSetChanged();
        }
        public CommentAdapter() {
        }
        public void loadMore(List<Comment> list){
            this.list.addAll(list);
            this.notifyDataSetChanged();
        }
        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(CommentActivity.this).inflate(R.layout.item_comment,parent,false);
            return new CommentViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder,final int position) {
            final Comment comment = list.get(position);
            holder.nickname.setText(comment.getUserInfo().getTruename());
            holder.pubTime.setText(comment.getFormatPubTime());
            holder.content.setText(comment.getContent());
            String headImageUrl = comment.getUserInfo().getHeadImage();
            if(headImageUrl==null||"".equals(headImageUrl)){
                Glide.with(CommentActivity.this).load(R.drawable.app_icon).into(holder.headImage);
            }else{
                Glide.with(CommentActivity.this).load(headImageUrl).into(holder.headImage);
            }
            Comment replyComment = comment.getComment();
            if(replyComment==null){
                holder.replyArea.setVisibility(View.GONE);
            }else{
                holder.replyArea.setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder();
                sb.append("回复：");
                sb.append(replyComment.getUserInfo().getTruename());
                sb.append("\n");
                sb.append(replyComment.getPubTime());
                sb.append("\n");
                sb.append(replyComment.getContent());
                holder.replyComment.setText(sb.toString());
            }
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    replyInfo.setVisibility(View.VISIBLE);
                    commentTarget.setText(Utils.highlight("回复："+comment.getUserInfo().getTruename(),comment.getUserInfo().getTruename(), getResources().getColor(R.color.hightlight_nickname)));
                    commentTargetContent.setText(comment.getContent());

                    System.out.println(comment.getId()+"------");
                    // TODO: 2016/12/22
                    commentTarget.setTag(comment.getId());
                    commentTxt.requestFocus();
                    if(!(getWindow().getAttributes().softInputMode== WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)){
                        InputMethodManager imm = (InputMethodManager) CommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    }

                    return true;
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        public CommentViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            replyArea = itemView.findViewById(R.id.replyArea);
            headImage = (ImageView) itemView.findViewById(R.id.headImage);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            pubTime = (TextView) itemView.findViewById(R.id.pubTime);
            content = (TextView) itemView.findViewById(R.id.content);
            replyComment = (TextView) itemView.findViewById(R.id.replyComment);
        }
        View replyArea;
        ImageView headImage;
        TextView nickname,pubTime,content,replyComment;
    }

    @Override
    public void onBackPressed() {
        if(replyInfo.getVisibility() == View.VISIBLE){
            replyInfo.setVisibility(View.GONE);
            commentTarget.setTag(0);
        }else{
            super.onBackPressed();
        }
    }


}
