package com.zgkj.fazhichun.adapter.city;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.zgkj.fazhichun.entity.city.City;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.city.CityInfo;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by next on 2016/3/25.
 */
public class CityListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<CityInfo> allCities;
    private String[] firstLetterArray;// 存放存在的汉语拼音首字母
    private Map<String, Integer> letterIndex;
    private final int VIEW_TYPE = 3;

    public CityListAdapter(Context context, List<CityInfo> allCities, Map<String, Integer> letterIndex) {
        this.context = context;
        this.allCities = allCities;
        this.letterIndex = letterIndex;
        inflater = LayoutInflater.from(this.context);

        setup();
    }

    private void setup() {
        firstLetterArray = new String[allCities.size()];
        for (int i = 0; i < allCities.size(); i++) {
            // 当前汉语拼音首字母
            String currentStr = getAlpha(allCities.get(i).getRegion_pinyin());
            // 上一个汉语拼音首字母，如果不存在为" "
            String previewStr = (i - 1) >= 0 ? getAlpha(allCities.get(i - 1).getRegion_pinyin()) : " ";
            if (!previewStr.equals(currentStr)) {
                String name = getAlpha(allCities.get(i).getRegion_pinyin());
                letterIndex.put(name, i);
                firstLetterArray[i] = name;
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE;
    }

    @Override
    public int getItemViewType(int position) {
        return position < 2 ? position : 2;
    }

    @Override
    public int getCount() {
        return allCities.size();
    }

    @Override
    public Object getItem(int position) {
        return allCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == 0) {//定位
            convertView = inflater.inflate(R.layout.item_city_location, null);
        }else if (viewType == 1) {
            convertView = inflater.inflate(R.layout.item_city_total_tag, null);
        } else {
            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_city_list, null);
                holder = new Holder();
                holder.letter = (TextView) convertView.findViewById(R.id.tv_letter);
                holder.name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            if (position >= 1) {
                holder.name.setText(allCities.get(position).getRegion_name());
                String currentStr = getAlpha(allCities.get(position).getRegion_pinyin());
                String previewStr = (position - 1) >= 0 ? getAlpha(allCities.get(position - 1).getRegion_pinyin()) : " ";
                if (!previewStr.equals(currentStr)) {
                    holder.letter.setVisibility(View.VISIBLE);
                    holder.letter.setText(currentStr);
                } else {
                    holder.letter.setVisibility(View.GONE);
                }
            }
        }

        return convertView;
    }

    class Holder {
        TextView letter, name;
    }


    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定位";
        } else if (str.equals("1")) {
            return "全部";
        } else {
            return "#";
        }
    }
}
