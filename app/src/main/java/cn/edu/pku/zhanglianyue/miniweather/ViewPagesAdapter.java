package cn.edu.pku.zhanglianyue.miniweather;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhanglianyue on 2018/1/3.
 */

public class ViewPagesAdapter extends PagerAdapter {
    private List<View> views;
    private Context context;

    public ViewPagesAdapter(List<View> views,Context context){
        this.views = views;
        this.context = context;
    }
    @Override
    public int getCount() {
        return views.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position){
        ViewHolder holder = new ViewHolder(views.get(position));
        container.addView(views.get(position));
        views.get(position).setTag(holder);
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));

    }
    public  static class ViewHolder{
        public TextView temperatureTv_day1,climateTv_day1,weekTv_day1,temperatureTv_day2,climateTv_day2,weekTv_day2,temperatureTv_day3,climateTv_day3,weekTv_day3;
        public TextView temperatureTv_day4,climateTv_day4,weekTv_day4,temperatureTv_day5,climateTv_day5,weekTv_day5,temperatureTv_day6,climateTv_day6,weekTv_day6;
        public ImageView weather_day1,weather_day2,weather_day3;
        public ImageView weather_day4,weather_day5,weather_day6;
        public ViewHolder(View view){
            temperatureTv_day1 = (TextView) view.findViewById(R.id.temperature_day1);
            climateTv_day1 = (TextView)view.findViewById(R.id.climate_day1);
            weekTv_day1 = (TextView)view.findViewById(R.id.week_day1);
            weather_day1 = (ImageView)view.findViewById(R.id.weather_img_day1);
            temperatureTv_day2 = (TextView) view.findViewById(R.id.temperature_day2);
            climateTv_day2 = (TextView)view.findViewById(R.id.climate_day2);
            weekTv_day2 = (TextView)view.findViewById(R.id.week_day2);
            weather_day2 = (ImageView)view.findViewById(R.id.weather_img_day2);
            temperatureTv_day3 = (TextView) view.findViewById(R.id.temperature_day3);
            climateTv_day3 = (TextView)view.findViewById(R.id.climate_day3);
            weekTv_day3 = (TextView)view.findViewById(R.id.week_day3);
            weather_day3 = (ImageView)view.findViewById(R.id.weather_img_day3);

            temperatureTv_day4 = (TextView) view.findViewById(R.id.temperature_day4);
            climateTv_day4 = (TextView)view.findViewById(R.id.climate_day4);
            weekTv_day4 = (TextView)view.findViewById(R.id.week_day4);
            weather_day4 = (ImageView)view.findViewById(R.id.weather_img_day4);
            temperatureTv_day5 = (TextView) view.findViewById(R.id.temperature_day5);
            climateTv_day5 = (TextView)view.findViewById(R.id.climate_day5);
            weekTv_day5 = (TextView)view.findViewById(R.id.week_day5);
            weather_day5 = (ImageView)view.findViewById(R.id.weather_img_day5);
            temperatureTv_day6 = (TextView) view.findViewById(R.id.temperature_day6);
            climateTv_day6 = (TextView)view.findViewById(R.id.climate_day6);
            weekTv_day6 = (TextView)view.findViewById(R.id.week_day6);
            weather_day6 = (ImageView)view.findViewById(R.id.weather_img_day6);

        }
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
