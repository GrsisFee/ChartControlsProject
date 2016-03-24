package com.grsisfee.chartcontrolsproject;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.grsisfee.gfcharts.BarChart.VerticalBarChart;
import com.grsisfee.gfcharts.Base.StatisticData;

import java.util.ArrayList;

/**
 * Author: GrsisFee
 * Date:   2016/1/31
 * Desc:
 * All rights reserved.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // 组合统计数据
        ArrayList<StatisticData> data = new ArrayList<>();
        data.add(new StatisticData<>("测", -0.6));
        data.add(new StatisticData<>("c1", 0.07));
        data.add(new StatisticData<>("c2", 6));
        data.add(new StatisticData<>("c3", 6));
        data.add(new StatisticData<>("c7c7c7c7c7c7", -70));
        data.add(new StatisticData<>("c8c8c8c8c8c8", 960.1));
        data.add(new StatisticData<>("c9c9c9c9c9c9", 968.123));
        data.add(new StatisticData<>("c10c10c10c10c10c10", 1280));
        data.add(new StatisticData<>("c4", -1380));
        data.add(new StatisticData<>("c0", -600));
        data.add(new StatisticData<>("c1", 700));
        data.add(new StatisticData<>("c2", 690));
        data.add(new StatisticData<>("c3", 670));
        data.add(new StatisticData<>("c4", 789));
        data.add(new StatisticData<>("c11", -580));
        data.add(new StatisticData<>("c8", -960.1));
        data.add(new StatisticData<>("c9", -968.123));
        data.add(new StatisticData<>("c10", 1320));
        data.add(new StatisticData<>("c11", 580));
        data.add(new StatisticData<>("c1", 7));
        data.add(new StatisticData<>("c2", -6));
        data.add(new StatisticData<>("c3", -6));
        data.add(new StatisticData<>("c7c7c7c7c7c7", 70));
        data.add(new StatisticData<>("c8c8c8c8c8c8", 960.1));
        data.add(new StatisticData<>("c9c9c9c9c9c9", -968.123));
        data.add(new StatisticData<>("c10c10c10c10c10c10", -1280));
        data.add(new StatisticData<>("c4", 1380));
        data.add(new StatisticData<>("c0", 600));
        data.add(new StatisticData<>("c1", 700));
        data.add(new StatisticData<>("c2", -690));
        data.add(new StatisticData<>("c3", -670));
        data.add(new StatisticData<>("c4", -789));
        data.add(new StatisticData<>("c11", -580));
        data.add(new StatisticData<>("c8", 960.1));
        data.add(new StatisticData<>("c9", 968.123));
        data.add(new StatisticData<>("c10", -1320));
        data.add(new StatisticData<>("c11", -580));
        data.add(new StatisticData<>("c1", -7));
        data.add(new StatisticData<>("c2", 6));
        data.add(new StatisticData<>("c3", -6));
        data.add(new StatisticData<>("c7c7c7c7c7c7", -70));
        data.add(new StatisticData<>("c8c8c8c8c8c8", 960.1));
        data.add(new StatisticData<>("c9c9c9c9c9c9", 968.123));
        data.add(new StatisticData<>("c10c10c10c10c10c10", -1280));
        data.add(new StatisticData<>("c4", 1380));
        data.add(new StatisticData<>("c0", 600));
        data.add(new StatisticData<>("c1", 700));
        data.add(new StatisticData<>("c2", 690));
        data.add(new StatisticData<>("c3", -670));
        data.add(new StatisticData<>("c4", -789));
        data.add(new StatisticData<>("c11", -580));
        data.add(new StatisticData<>("c8", -960.1));
        data.add(new StatisticData<>("c9", -968.123));
        data.add(new StatisticData<>("c10", -1320));
        data.add(new StatisticData<>("c11", -580));

        VerticalBarChart barChart = (VerticalBarChart) findViewById(R.id.barChart);
        barChart.setData(data);
        barChart.setTitle("我是统计图标题，我可以外部自定义画笔");
        barChart.setxLabel("我是x轴描述，我可以外部自定义画笔");
        barChart.setyLabel("我是y轴描述，我可以外部自定义画笔");
    }
}
