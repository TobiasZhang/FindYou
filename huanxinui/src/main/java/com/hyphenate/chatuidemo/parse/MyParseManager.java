package com.hyphenate.chatuidemo.parse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by TT on 2016/12/18.
 */
public class MyParseManager extends ParseManager {
    private static final String TAG = MyParseManager.class.getSimpleName();

    private static MyParseManager instance = new MyParseManager();
    private MyParseManager() {}
    public static MyParseManager getInstance() {
        return instance;
    }

    public boolean updateParseNickName(final String nickname) {
        System.out.println("updateParseNickName----------"+nickname);


        String username = EMClient.getInstance().getCurrentUser();
        /*ParseQuery<ParseObject> pQuery = ParseQuery.getQuery(CONFIG_TABLE_NAME);
        pQuery.whereEqualTo(CONFIG_USERNAME, username);
        ParseObject pUser = null;
        try {
            pUser = pQuery.getFirst();
            if (pUser == null) {
                return false;
            }
            pUser.put(CONFIG_NICK, nickname);
            pUser.save();
            return true;
        } catch (ParseException e) {
            if(e.getCode()==ParseException.OBJECT_NOT_FOUND){
                pUser = new ParseObject(CONFIG_TABLE_NAME);
                pUser.put(CONFIG_USERNAME, username);
                pUser.put(CONFIG_NICK, nickname);
                try {
                    pUser.save();
                    return true;
                } catch (ParseException e1) {
                    e1.printStackTrace();
                    EMLog.e(TAG, "parse error " + e1.getMessage());
                }

            }
            e.printStackTrace();
            EMLog.e(TAG, "parse error " + e.getMessage());
        } catch(Exception e) {
            EMLog.e(TAG, "updateParseNickName error");
            e.printStackTrace();
        }*/
        return true;
    }

    public void getContactInfos(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {


        FormBody.Builder builder = new FormBody.Builder();
        for(String username:usernames){
            builder.add("ids",username);
            System.out.println(username+"--ids------usernames------before----------");
        }
        HttpUtils.request("user/getByIds",
                builder.build(),
                new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onError(0, e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            String resultData = response.body().string();
                            DataRoot<List<UserInfo>> dataRoot = HttpUtils.parseJson(resultData,new TypeToken<DataRoot<List<UserInfo>>>(){});
                            if(dataRoot.getResult().equals("ok")){
                                List<UserInfo> friends = dataRoot.getData();
                                System.out.println(resultData);

                                List<EaseUser> easeUserList = new ArrayList<EaseUser>();
                                for (UserInfo user : friends) {
                                    EaseUser easeUser = new EaseUser(user.getId()+"");
                                    easeUser.setAvatar(user.getHeadImage());
                                    easeUser.setNickname(user.getTruename());
                                    EaseCommonUtils.setUserInitialLetter(easeUser);
                                    easeUserList.add(easeUser);

                                    System.out.println(easeUser.getUsername()+"---"+easeUser.getNickname()+"---"+easeUser.getAvatar()+"--------usernames------after----MyParseManager----getContactInfos--");
                                }
                                callback.onSuccess(easeUserList);
                            }else{
                                callback.onError(0, dataRoot.getResult());
                            }
                        }else{
                            callback.onError(0, "获取好友列表失败---"+response.code());
                        }
                    }
                });
    }


    public void asyncGetCurrentUserInfo(final EMValueCallBack<EaseUser> callback){
        final String username = EMClient.getInstance().getCurrentUser();
        asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {

            @Override
            public void onSuccess(EaseUser value) {
                callback.onSuccess(value);
            }

            @Override
            public void onError(int error, String errorMsg) {
                callback.onError(error, errorMsg);
                if (error == ParseException.OBJECT_NOT_FOUND) {

                    /*ParseObject pUser = new ParseObject(CONFIG_TABLE_NAME);
                    pUser.put(CONFIG_USERNAME, username);
                    pUser.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException arg0) {
                            if(arg0==null){
                                callback.onSuccess(new EaseUser(username));
                            }
                        }
                    });*/
                }else{
                    //callback.onError(error, errorMsg);
                }
            }
        });
    }

    public void asyncGetUserInfo(final String username,final EMValueCallBack<EaseUser> callback){
        HttpUtils.request("user/getByIds",
                new FormBody.Builder().add("ids",username).build(),
                new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onError(0, e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()==200){
                            DataRoot<List<UserInfo>> dataRoot = HttpUtils.parseJson(response.body().string(),new TypeToken<DataRoot<List<UserInfo>>>(){});
                            if(dataRoot.getResult().equals("ok")){
                                UserInfo user = dataRoot.getData().get(0);

                                if(callback!=null){
                                    EaseUser easeUser = DemoHelper.getInstance().getContactList().get(username);
                                    if(easeUser==null){
                                        easeUser = new EaseUser(user.getId()+"");
                                    }
                                    easeUser.setAvatar(user.getHeadImage());
                                    easeUser.setNick(user.getTruename());
                                    callback.onSuccess(easeUser);
                                }else{
                                    callback.onError(0, dataRoot.getResult());
                                }
                            }else{
                                callback.onError(0, username+"--username---MyParseManager--asyncGetUserInfo---"+response.code());
                            }
                        }
                    }
                });
    }

    public String uploadParseAvatar(byte[] data) {
        String username = EMClient.getInstance().getCurrentUser();
        /*ParseQuery<ParseObject> pQuery = ParseQuery.getQuery(CONFIG_TABLE_NAME);
        pQuery.whereEqualTo(CONFIG_USERNAME, username);
        ParseObject pUser = null;
        try {
            pUser = pQuery.getFirst();
            if (pUser == null) {
                pUser = new ParseObject(CONFIG_TABLE_NAME);
                pUser.put(CONFIG_USERNAME, username);
            }
            ParseFile pFile = new ParseFile(data);
            pUser.put(CONFIG_AVATAR, pFile);
            pUser.save();
            return pFile.getUrl();
        } catch (ParseException e) {
            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                try {
                    pUser = new ParseObject(CONFIG_TABLE_NAME);
                    pUser.put(CONFIG_USERNAME, username);
                    ParseFile pFile = new ParseFile(data);
                    pUser.put(CONFIG_AVATAR, pFile);
                    pUser.save();
                    return pFile.getUrl();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                    EMLog.e(TAG, "parse error " + e1.getMessage());
                }
            } else {
                e.printStackTrace();
                EMLog.e(TAG, "parse error " + e.getMessage());
            }
        } catch(Exception e) {
            EMLog.e(TAG, "uploadParseAvatar error");
            e.printStackTrace();
        }*/
        return null;
    }

    public static class HttpUtils {


        private static OkHttpClient client;
        static{
            client = new OkHttpClient();
        }
        public static void request(String url, RequestBody requestBody, Callback callback){
            Request.Builder requestBuilder = new Request.Builder();
            if(requestBody != null)
                requestBuilder.post(requestBody);
            Request request = requestBuilder.url(com.hyphenate.easeui.utils.HttpContacts.BASE_URL+url).build();
            Call call = client.newCall(request);
            call.enqueue(callback);
        }
        private static Gson gson = new Gson();
        public static<T> T parseJson(String json,TypeToken<T> token){
            T t = gson.fromJson(json,token.getType());
            return t;
        }
    }
    public static class UserInfo implements Serializable {
        private Integer id;
        private String nickname;
        private String headImage;
        private String truename;

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }
    }
    public static class Condition {
        private int pid;
        private int pageSize;
        private int startWith;
        private int maxPid;
        private int max;
        private Integer uid;
        private Integer tid;



        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getMaxPid() {
            return maxPid;
        }

        public void setMaxPid(int maxPid) {
            this.maxPid = maxPid;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getStartWith() {
            return (pid-1)*pageSize;
        }

        public void setStartWith(int startWith) {
            this.startWith = startWith;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
        public Integer getTid() {
            return tid;
        }

        public void setTid(Integer tid) {
            this.tid = tid;
        }
    }
    public static class DataRoot<T> implements Serializable {
        private String result;
        private T data;
        private Condition condition;

        public DataRoot(String result, T data,Condition condition) {
            super();
            this.result = result;
            this.data = data;
            this.condition = condition;
        }

        public String getResult() {
            return result;
        }
        public void setResult(String result) {
            this.result = result;
        }
        public T getData() {
            return data;
        }
        public void setData(T data) {
            this.data = data;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }
}


