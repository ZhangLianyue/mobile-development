package cn.edu.pku.zhanglianyue.miniweather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by zhanglianyue on 2018/1/3.
 */

public class page extends Activity {
    private TextView climateTv_day1;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.page1);
        climateTv_day1 = (TextView)findViewById(R.id.climate_day1);
        climateTv_day1.setText(getIntent().getStringExtra("climateTv_day1") );
        Log.d("esedse","qqqqqqqqqqqqqqqqq"+getIntent().getStringExtra("climateTv_day1"));
        finish();

    }
}
