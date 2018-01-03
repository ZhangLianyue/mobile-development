package cn.edu.pku.zhanglianyue.miniweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.pku.zhanglianyue.bean.TodayWeather;
import cn.edu.pku.zhanglianyue.util.NetUtil;

/**
 * Created by zhanglianyue on 2017/9/27.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private static final int UPDATE_TODAY_WEATHER = 1;
    private ImageView mUpdateBtn;

    private ImageView mCitySelect;

    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;

    private ViewPagesAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = {R.id.iv1,R.id.iv2};
    private Button btn;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }

    };


    void initDots(){
        dots = new ImageView[views.size()];
        for (int i = 0;i < views.size();i++){
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    private  void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(layoutInflater.inflate(R.layout.page1,null));
        views.add(layoutInflater.inflate(R.layout.page2,null));
//        views.add(layoutInflater.inflate(R.layout.page3,null));
        vpAdapter = new ViewPagesAdapter(views,this);
        vp = (ViewPager)findViewById(R.id.viewPage);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);
//        btn = (Button)views.get(2).findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Guide.this,MainActivity.class);
//                startActivity(i);
//                finish();
//            }
//        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int i) {
        for (int j = 0;j <ids.length;j++){
            if (j == i){
                dots[j].setImageResource(R.drawable.page_indicator_focused);
            }else{
                dots[j].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }





    void initView(){

        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);


        LayoutInflater layout=this.getLayoutInflater();
        View view=layout.inflate(R.layout.page1, null);

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }

    private TodayWeather parseXML(String xmldata){
        System.out.println("today weather："+xmldata);
        TodayWeather todayWeather = null;
        int fengxiangCount=0;
        int fengliCount =0;
        int dateCount=0;
        int highCount =0;
        int lowCount=0;
        int typeCount =0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp")){
                            todayWeather= new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }

                        break;


                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    /**
     *
     * @param cityCode
     */
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather = null;
                try{
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr=response.toString();
                    Log.d("myWeather", responseStr);
                   //在获取网络数据后，调用解析函数
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());

                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }

            }
        }).start();
    }

    @Override
    /*通过SharedPreferences读取城市id，如果没有定义则缺省为101010100（北京城市ID）*/
            public void onClick(View view){
            if(view.getId() == R.id.title_city_manager){
                            Intent i = new Intent(this,SelectCity.class);
                                    //startActivity(i);
                                    startActivityForResult(i,1);
                        }
            if (view.getId() == R.id.title_update_btn){
                SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
                String cityCode = sharedPreferences.getString("main_city_code","101010100");
                Log.d("myWeather",cityCode);

                if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                    Log.d("myWeather", "网络OK");
                    queryWeatherCode(cityCode);
                }else
                {
                    Log.d("myWeather", "网络挂了");
                    Toast.makeText(MainActivity.this,"网络挂了！",Toast.LENGTH_LONG).show();
                }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为"+newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }

    void updateTodayWeather(TodayWeather todayWeather){

        String high = null;
        String low = null;
        String high_day0 = null;
        String low_day0 = null;
        String high_day1 = null;
        String low_day1 = null;
        String high_day2 = null;
        String low_day2 = null;
        String high_day3 = null;
        String low_day3 = null;
        String high_day4 = null;
        String low_day4 = null;

        if (todayWeather.getHigh()!=null && todayWeather.getLow()!=null ){
            high = todayWeather.getHigh().substring(2);
            low = todayWeather.getLow().substring(2);
        }
        if (todayWeather.getHigh_day0()!=null && todayWeather.getLow_day0()!=null ){
            high_day0 = todayWeather.getHigh_day0().substring(2);
            low_day0 = todayWeather.getLow_day0().substring(2);
        }
        if (todayWeather.getHigh_day1()!=null && todayWeather.getLow_day1()!=null ){
            high_day1 = todayWeather.getHigh_day1().substring(2);
            low_day1 = todayWeather.getLow_day1().substring(2);
        }
        if (todayWeather.getHigh_day2()!=null && todayWeather.getLow_day2()!=null ){
            high_day2 = todayWeather.getHigh_day2().substring(2);
            low_day2 = todayWeather.getLow_day2().substring(2);
        }
        if (todayWeather.getHigh_day3()!=null && todayWeather.getLow_day3()!=null ){
            high_day3 = todayWeather.getHigh_day3().substring(2);
            low_day3 = todayWeather.getLow_day3().substring(2);
        }
        if (todayWeather.getHigh_day4()!=null && todayWeather.getLow_day4()!=null ){
            high_day4 = todayWeather.getHigh_day4().substring(2);
            low_day4 = todayWeather.getLow_day4().substring(2);
        }


        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());

        temperatureTv.setText(low+"~"+high);
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:"+todayWeather.getFengli());



        ViewPagesAdapter.ViewHolder holder = (ViewPagesAdapter.ViewHolder)views.get(0).getTag();
        holder.temperatureTv_day1.setText(low_day0+"~"+high_day0);
        holder.climateTv_day1.setText(todayWeather.getType_day0());
        holder.weekTv_day1.setText(todayWeather.getDate_day0());
        holder.temperatureTv_day2.setText(low+"~"+high);
        holder.climateTv_day2.setText(todayWeather.getType());
        holder.weekTv_day2.setText(todayWeather.getDate());
        holder.temperatureTv_day3.setText(low_day1+"~"+high_day1);
        holder.climateTv_day3.setText(todayWeather.getType_day1());
        holder.weekTv_day3.setText(todayWeather.getDate_day1());
        //页面2
        ViewPagesAdapter.ViewHolder holder2 = (ViewPagesAdapter.ViewHolder)views.get(1).getTag();
        holder2.temperatureTv_day4.setText(low_day2+"~"+high_day2);
        holder2.climateTv_day4.setText(todayWeather.getType_day2());
        holder2.weekTv_day4.setText(todayWeather.getDate_day2());
        holder2.temperatureTv_day5.setText(low_day3+"~"+high_day3);
        holder2.climateTv_day5.setText(todayWeather.getType_day3());
        holder2.weekTv_day5.setText(todayWeather.getDate_day3());
        holder2.temperatureTv_day6.setText(low_day4+"~"+high_day4);
        holder2.climateTv_day6.setText(todayWeather.getType_day4());
        holder2.weekTv_day6.setText(todayWeather.getDate_day4());


        if(0==1){

        }
        System.out.println("---------------pm25"+todayWeather.getPm25());
        //System.out.println("---------------pm25"+todayWeather.getPm25().equals("null"));
        int IntPm25 = Integer.parseInt(todayWeather.getPm25()==null||todayWeather.getPm25().equals("null")?"0":todayWeather.getPm25());
        int Pm25Value = 0;
        if(IntPm25 > 50 && IntPm25 <= 100){
            Pm25Value = 1;
        }else if(IntPm25 >100 && IntPm25 <= 150){
            Pm25Value = 2;
        }else if(IntPm25 >150 && IntPm25 <= 200){
            Pm25Value = 3;
        }else if(IntPm25 >200 && IntPm25 <= 300){
            Pm25Value = 4;
        }else if(IntPm25 > 300){
            Pm25Value = 5;
        }
        switch (Pm25Value){
            case 0:
                pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
                break;
            case 1:
                pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
                break;
            case 2:
                pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
                break;
            case 3:
                pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
                break;
            case 4:
                pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
                break;
            case 5:
                pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
                break;
        }
        switch (todayWeather.getType()){
            case "暴雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                break;
            case "暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
                break;
            case "大暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
                break;
            case "大雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
                break;
            case "大雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
                break;
            case "多云":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
                break;
            case "雷阵雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                break;
            case "雷阵雨冰雹":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                break;
            case "沙尘暴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                break;
            case "特大暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
                break;
            case "雾":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
                break;
            case "小雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                break;
            case "小雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                break;
            case "阴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
                break;
            case "雨夹雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
                break;
            case "雨加雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
                break;
            case "阵雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
                break;
            case "阵雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                break;
            case "中雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
                break;
            case "中雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
                break;
        }

        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weather_info);


        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);

        initView();

        initViews();
        initDots();
    }


}
