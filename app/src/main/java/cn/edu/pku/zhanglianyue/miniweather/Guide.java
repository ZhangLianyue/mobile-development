package cn.edu.pku.zhanglianyue.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglianyue on 2018/1/3.
 */

public class Guide extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPagesAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = {R.id.iv1,R.id.iv2,R.id.iv3};
    private Button btn;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.guide);
        initViews();
        initDots();

    }
    void initDots(){
        dots = new ImageView[views.size()];
        for (int i = 0;i < views.size();i++){
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }
    private  void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(layoutInflater.inflate(R.layout.start_page1,null));
        views.add(layoutInflater.inflate(R.layout.start_page2,null));
        views.add(layoutInflater.inflate(R.layout.start_page3,null));
        vpAdapter = new ViewPagesAdapter(views,this);
        vp = (ViewPager)findViewById(R.id.viewPage);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);
        btn = (Button)views.get(2).findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Guide.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
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
}
