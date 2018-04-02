package com.guyue.proj1.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guyue.proj1.R;
import com.guyue.proj1.bean.LotteryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LotteryInfoActivity extends AppCompatActivity {

    private TextView tv_back, tv_main_title;
    private ListView lv_list;
    private List<LotteryInfo> lotteryInfos;
    private LotteryAdapter lotteryAdapter;
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_info);


        initView();
    }

    private void initView() {
        tv_back = findViewById(R.id.tv_back);
        tv_main_title = findViewById(R.id.tv_main_title);
        setTVMainTitle();
        lv_list = findViewById(R.id.lv_list);
        lotteryAdapter = new LotteryAdapter(this);
        getLotteryInfo();

        myHandler = new MyHandler();

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LotteryInfoActivity.this.finish();
            }
        });
    }

    class LotteryAdapter extends BaseAdapter {

        private Context mContext;
        private List<LotteryInfo> lotteryInfos;

        public LotteryAdapter(Context context) {
            this.mContext = context;

        }

        public void setData(List<LotteryInfo> lotteryInfos) {
            this.lotteryInfos = lotteryInfos;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return lotteryInfos == null ? 0 : lotteryInfos.size();
        }

        @Override
        public LotteryInfo getItem(int i) {
            return lotteryInfos == null ? null : lotteryInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final ViewHolder vh;
            if (view == null) {
                vh = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.lotteryinfo_item, null);
                vh.tv_qishu = view.findViewById(R.id.tv_qishu);
                vh.tv_content = view.findViewById(R.id.tv_content);
                vh.tv_time = view.findViewById(R.id.tv_time);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) view.getTag();
            }


            LotteryInfo info = getItem(i);
            if (info != null) {
                vh.tv_qishu.setText(info.getQishu());
                vh.tv_content.setText(info.getContent());
                vh.tv_time.setText(info.getTime());
            }


            return view;
        }
    }

    class ViewHolder {
        public TextView tv_qishu, tv_content, tv_time;
    }


    public void getLotteryInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "";
                int flag = getIntent().getIntExtra("flag", -1);
                if (flag == 0) {
                    url = "http://f.apiplus.net/ssq.json?rows=20";
                } else if (flag == 1) {
                    url = "http://f.apiplus.net/fc3d.json?rows=20";
                } else if (flag == 2) {
                    url = "http://f.apiplus.net/dlt.json?rows=20";
                } else if (flag == 3) {
                    url = "http://f.apiplus.net/pl3.json?rows=20";
                } else if (flag == 4) {
                    url = "http://f.apiplus.net/pl5.json?rows=20";
                } else if (flag == 5) {
                    url = "http://f.apiplus.net/qxc.json?rows=20";
                } else if (flag == 6) {
                    url = "http://f.apiplus.net/qlc.json?rows=20";
                }
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = responseData;
                    myHandler.sendMessage(msg);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJsontoLotteryInfo(String jsonString) {
        lotteryInfos = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                LotteryInfo lotteryInfo = new LotteryInfo();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                lotteryInfo.setQishu("第" + jsonObject1.getString("expect") + "期");
                String content=jsonObject1.getString("opencode").replaceAll(",","  ");
                lotteryInfo.setContent(content);
                lotteryInfo.setTime("开奖时间：" + jsonObject1.getString("opentime"));
                lotteryInfos.add(lotteryInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String jsonString = (String) msg.obj;
                    Log.d("guyue",jsonString);
                    parseJsontoLotteryInfo(jsonString);
                    lotteryAdapter.setData(lotteryInfos);
                    lv_list.setAdapter(lotteryAdapter);
                    break;
            }
        }
    }

    private void setTVMainTitle() {
        int flag = getIntent().getIntExtra("flag", -1);
        if (flag == 0) {
            tv_main_title.setText("双色球开奖信息");
        } else if (flag == 1) {
            tv_main_title.setText("福彩3D开奖信息");

        } else if (flag == 2) {
            tv_main_title.setText("大乐透开奖信息");

        } else if (flag == 3) {
            tv_main_title.setText("排列3开奖信息");

        } else if (flag == 4) {
            tv_main_title.setText("排列5开奖信息");

        } else if (flag == 5) {
            tv_main_title.setText("七星彩开奖信息");

        } else if (flag == 6) {
            tv_main_title.setText("七乐彩开奖信息");

        }
    }
}
