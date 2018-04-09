package com.yunzhongjun.weiyang.mocloudcastle;

import android.app.Activity;
import android.os.Bundle;

import com.yunzhongjun.weiyang.mocloudcastle.treeviewdemo.SimpleTreeAdapter;
import com.yunzhongjun.weiyang.uicomponent.treeview.TreeGraphView;

public class MainActivity extends Activity {

    private TreeGraphView mTreeGraphView;
    private SimpleTreeAdapter mTreeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTreeGraphView = (TreeGraphView) findViewById(R.id.demo_treeview);
        mTreeAdapter = new SimpleTreeAdapter();
        mTreeGraphView.setTreeAdapter(mTreeAdapter);
    }
}
