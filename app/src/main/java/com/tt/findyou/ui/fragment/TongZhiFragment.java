package com.tt.findyou.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.CommentActivity;
import com.tt.findyou.R;
import com.tt.findyou.utils.HttpUtils;
import com.tt.findyou.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import limeng.com.findyou.view.MyselfinformationActivity;
import limeng.com.findyou.view.OtherMainActivity;
import model.dto.DataRoot;
import model.pojo.TongZhi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import utils.LoginUtils;

/**
 * Created by TT on 2016/12/14.
 */
public class TongZhiFragment extends Fragment {

    RecyclerView tongZhiList;
    TongZhiAdapter tongZhiAdapter;
    List<TongZhi> dataList;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    tongZhiAdapter.addData(dataList);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tongzhi,null);

        tongZhiList = (RecyclerView) view.findViewById(R.id.tongZhiList);
        tongZhiAdapter = new TongZhiAdapter();
        tongZhiList.setAdapter(tongZhiAdapter);
        tongZhiList.setLayoutManager(new LinearLayoutManager(getActivity()));

        HttpUtils.request("mix/getTongZhi",
                new FormBody.Builder()
                        .add("uid", LoginUtils.userInfo.getId()+"")
                        .build(),
                new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Utils.toast(getActivity(),"--获取通知错误--");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    DataRoot<List<TongZhi>> dataRoot = HttpUtils.parseJson(response.body().string(),new TypeToken<DataRoot<List<TongZhi>>>(){});
                    if("ok".equals(dataRoot.getResult())){
                        dataList = dataRoot.getData();
                        handler.sendEmptyMessage(1);
                    }else{
                        Utils.toast(getActivity(),dataRoot.getResult());
                    }
                }else{
                    Utils.toast(getActivity(),"--获取通知错误--"+response.code());
                }
            }
        });

        return view;
    }

    class TongZhiAdapter extends RecyclerView.Adapter<TongZhiViewHolder>{
        private List<TongZhi> list = new ArrayList<>();

        public TongZhiAdapter() {
        }
        public void addData(List<TongZhi> list){
            this.list.addAll(list);
            this.notifyDataSetChanged();
        }
        @Override
        public TongZhiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //1回复主题帖 2回复回复帖 3留言
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_tongzhi_reply_topic,parent,false);
            return new TongZhiViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TongZhiViewHolder holder, int position) {
            final TongZhi tongZhi = list.get(position);
            holder.content.setText(tongZhi.getContent());
            SpannableStringBuilder ssb = null;
            switch (getItemViewType(position)){
                case 1:
                    ssb = Utils.highlight(tongZhi.getUserTruename()+" 回复了你的帖 "+tongZhi.getTopicTitle(),tongZhi.getUserTruename(), getResources().getColor(R.color.hightlight_nickname));
                    Pattern p = Pattern.compile(tongZhi.getTopicTitle(),Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(tongZhi.getUserTruename()+" 回复了你的帖 "+tongZhi.getTopicTitle());
                    while (m.find()) {
                        CharacterStyle span = new ForegroundColorSpan(getResources().getColor(R.color.hightlight_topicname));
                        ssb.setSpan(span, m.start(), m.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), CommentActivity.class);
                            model.pojo.Topic topic = new model.pojo.Topic();
                            topic.setId(tongZhi.getTopicId());
                            topic.setTitle(tongZhi.getTopicTitle());
                            intent.putExtra("topic",topic);
                            getActivity().startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    ssb = Utils.highlight(tongZhi.getUserTruename()+" 在 "+tongZhi.getTopicTitle()+" 中回复了你",tongZhi.getUserTruename(), getResources().getColor(R.color.hightlight_nickname));
                    Pattern p2 = Pattern.compile(tongZhi.getTopicTitle(),Pattern.CASE_INSENSITIVE);
                    Matcher m2 = p2.matcher(tongZhi.getUserTruename()+" 在 "+tongZhi.getTopicTitle()+" 中回复了你");
                    while (m2.find()) {
                        CharacterStyle span = new ForegroundColorSpan(getResources().getColor(R.color.hightlight_comment_topicname));
                        ssb.setSpan(span, m2.start(), m2.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), CommentActivity.class);
                            model.pojo.Topic topic = new model.pojo.Topic();
                            topic.setId(tongZhi.getTopicId());
                            topic.setTitle(tongZhi.getTopicTitle());
                            intent.putExtra("topic",topic);
                            getActivity().startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    ssb = Utils.highlight(tongZhi.getUserTruename()+" 给你留了言",tongZhi.getUserTruename(), getResources().getColor(R.color.hightlight_nickname));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = null;
                            if(tongZhi.getId()==LoginUtils.userInfo.getId()){
                                intent = new Intent(getActivity(),MyselfinformationActivity.class);
                            }else{
                                intent = new Intent(getActivity(),OtherMainActivity.class);
                                intent.putExtra("uid", tongZhi.getId());
                            }
                            getActivity().startActivity(intent);
                        }
                    });
                    break;
            }
            holder.info.setText(ssb);
            holder.pubTime.setText(tongZhi.getFormatPubTime());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).getType();
        }
    }

    class TongZhiViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        public TongZhiViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            info = (TextView) itemView.findViewById(R.id.info);
            pubTime = (TextView) itemView.findViewById(R.id.pubTime);
            content = (TextView) itemView.findViewById(R.id.content);
        }
        TextView info,pubTime,content;
    }
}
