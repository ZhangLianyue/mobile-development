package cn.edu.pku.zhanglianyue.miniweather;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by zhanglianyue on 2017/9/27.
 */

public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weather_info);
    }
}
