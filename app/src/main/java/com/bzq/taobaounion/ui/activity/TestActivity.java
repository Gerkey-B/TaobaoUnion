package com.bzq.taobaounion.ui.activity;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bzq.taobaounion.R;
import com.bzq.taobaounion.ui.custom.TextFlowLayout;
import com.bzq.taobaounion.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Gerkey
 * Created on 2021/8/13
 * 测试 TextFlowLayout
 */
public class TestActivity extends AppCompatActivity {

    @BindView(R.id.test_flow_text)
    public TextFlowLayout flowText;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        List<String> testList = new ArrayList<>();
        testList.add("电脑");
        testList.add("电脑显示器");
        testList.add("Nuxt.js");
        testList.add("Vue.js课程");
        testList.add("机械键盘");
        testList.add("滑板鞋");
        testList.add("运动鞋");
        testList.add("肥宅快乐水");
        testList.add("阳光沙滩");
        testList.add("android编程");
        testList.add("机械键盘");
        testList.add("滑板鞋");
        testList.add("运动鞋");
        testList.add("肥宅快乐水");
        testList.add("阳光沙滩");
        testList.add("android编程");
        testList.add("机械键盘");
        testList.add("滑板鞋");
        testList.add("肥宅快乐水");
        testList.add("阳光沙滩");
        testList.add("android编程");
        testList.add("机械键盘");
        testList.add("滑板鞋");
        testList.add("运动鞋");
        testList.add("肥宅快乐水");
        testList.add("阳光沙滩");
        testList.add("android编程");
        testList.add("JavaWeb后台");
        flowText.setTextList(testList);
        flowText.setOnFlowTextItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowItemClick(String text) {
                ToastUtils.showToast(text);
            }
        });
    }
}

