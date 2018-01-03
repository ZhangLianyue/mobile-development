package cn.edu.pku.zhanglianyue.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.pku.zhanglianyue.app.MyApplication;
import cn.edu.pku.zhanglianyue.bean.City;

/**
 * Created by zhanglianyue on 2018/1/1.
 */

public class SelectCity extends Activity implements View.OnClickListener {

    private ImageView mBackBtn;

    private ListView mList;
    private SearchView mSearchView;
    private List<City> cityList;
    private List<String> filterDateList = new ArrayList<>();
    private List<String> originalList = new ArrayList<>();
    private HashMap<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);

        initViews();

        mBackBtn = (ImageView) findViewById(R.id.title_back);
                mBackBtn.setOnClickListener(this);
    }

    private void initViews() {
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mList = (ListView) findViewById(R.id.title_list);
        MyApplication myApplication = (MyApplication) getApplication();
        cityList = myApplication.getCityList();
        for (City city : cityList) {
            filterDateList.add(city.getCity());
            originalList.add(city.getCity());
            map.put(city.getCity(), city.getNumber());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SelectCity.this, android.R.layout.simple_list_item_1, filterDateList);
        mList.setAdapter(adapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    filterDateList.clear();
                    filterDateList.addAll(getFiltList(originalList, newText));
                    Log.d("select city", filterDateList.toString());
                    adapter.notifyDataSetChanged();
                } else {
                    filterDateList.clear();
                    filterDateList.addAll(originalList);
                    Log.d("select city", filterDateList.toString());
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String cityName = filterDateList.get(position);
                Log.d("select city", cityName);
                String cityCode = map.get(cityName);
                SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                editor.putString("cityCode", cityCode);
                editor.apply();

                Intent i = new Intent();
                Log.d("select city", cityName);
                i.putExtra("cityCode",cityCode);
                Log.d("select city", cityCode);
                setResult(RESULT_OK, i);

                finish();
            };
        });
    }

    private List<String> getFiltList(List<String> list, String filtValue){
        List<String> filtList = new ArrayList<>();
        for(String cityName : list){
            if (cityName.contains(filtValue)){
                filtList.add(cityName);
            }
        }
        return filtList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:

                SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
                String cityCode = pref.getString("cityCode", "101010100");

                Intent i = new Intent();
                i.putExtra("cityCode", "101160101");
                setResult(RESULT_OK, i);
                finish();
                break;
            default:
                break;
        }
    }
}
