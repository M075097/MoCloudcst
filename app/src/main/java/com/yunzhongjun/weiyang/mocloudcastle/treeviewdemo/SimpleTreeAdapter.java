package com.yunzhongjun.weiyang.mocloudcastle.treeviewdemo;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunzhongjun.weiyang.mocloudcastle.MoApplication;
import com.yunzhongjun.weiyang.mocloudcastle.R;
import com.yunzhongjun.weiyang.uicomponent.treeview.INodeModule;
import com.yunzhongjun.weiyang.uicomponent.treeview.NodeTreeHelper;
import com.yunzhongjun.weiyang.uicomponent.treeview.TreeAdapter;

/**
 * Created by wb-zyl208210 on 2017/4/5.
 */
public class SimpleTreeAdapter extends TreeAdapter {
    private NodeModule mRootNode;
    private int mNodeNum;

    @Override
    public View getNodeView() {
        LinearLayout nodeView = (LinearLayout) View.inflate(MoApplication.getInstance(), R.layout.demo_treeview_nodeview, null);
        return nodeView;
    }

    @Override
    public void bindView(View nodeView, INodeModule nodeData) {//TODO 拿取实际数据 根据数据
        TextView nodeIcon = (TextView) nodeView.findViewById(R.id.demo_treeview_nodeview_icon);
        TextView nodeText = (TextView) nodeView.findViewById(R.id.demo_treeview_nodeview_des);
        //        TextView nodeRoot = () nodeView.findViewById(R.id.nodeview_root);
        NodeModule nodeModule = (NodeModule) nodeData;
        if (nodeModule.nodeType == 1 || nodeModule.nodeType == 3 || nodeModule.nodeType == 5) {
            nodeIcon.setBackgroundDrawable(MoApplication.getInstance().getResources().getDrawable(R.drawable.demo_threview_circle_redbg));
        } else {
            nodeIcon.setBackgroundDrawable(MoApplication.getInstance().getResources().getDrawable(R.drawable.demo_threview_circle_bluebg));

        }
        String deviceName = nodeModule.deviceName;

        // nodeText.setText(TextUtils.isEmpty(nodeModule.deviceName) ? "未知" : nodeModule.deviceName);
      /*  nodeIcon.setText(TextUtils.isEmpty(nodeModule.deviceName) ? "未知" : nodeModule.deviceName.substring(0, 1 > nodeModule.deviceName.length() - 1 ? 0 : 1)); */
        String displayName;//要展示的名字
        if (TextUtils.isEmpty(deviceName)) {
            displayName = "未知";
        } else {
            if (deviceName.length() > 4) {
                if (deviceName.contains("-")) {
                    String substring = deviceName.substring(deviceName.indexOf("-")+1, deviceName.length());
                    if (substring.length() > 4) {
                        displayName = substring.substring(0, 4);
                    } else if (substring.length() <= 0) {
                        displayName = deviceName.substring(0, deviceName.indexOf("-"));
                    } else {
                        displayName = substring;
                    }
                } else {
                    displayName = deviceName.substring(0,4);
                }
            } else {
                displayName = deviceName;
            }
        }
        nodeText.setText(displayName);
        nodeIcon.setText(displayName);


    }

    @Override
    public INodeModule getTreeNodeData() {//整理数据 NodeModule 是自定义数据类型 只需实现相关接口即可
        NodeTreeHelper nodeTreeHelper = new NodeTreeHelper();
        NodeModule nodeModule = DataUtils.mockData();
                return mRootNode == null ? nodeModule : mRootNode;//TODO 真实数据
//        return mRootNode;
    }

    @Override
    public int getNodeNum() {
        return mNodeNum == 0 ? 30 : mNodeNum;//TODO真实数据
    }

    public void setRootNode(NodeModule rootNode) {
        this.mRootNode = rootNode;
        Log.d("simpleTreeAdapter", "rootNode =" + rootNode);
        mNodeNum = statisticalNodeNum(0, mRootNode);
        initThreeGraphNodeBean(mNodeNum, mRootNode);
    }

    public int statisticalNodeNum(int sum, NodeModule nodeModule) {//TODO 更改为接口接收 升级至 baseAdapter中
        int num = 0;
        if (nodeModule != null) {
            num++;
            if (nodeModule.getSubModuleList() != null && nodeModule.getSubModuleList().size() > 0) {
                //sum += nodeModule.nodeList.size();
                for (int i = 0; i < nodeModule.getSubModuleList().size(); i++) {
                    num=num+statisticalNodeNum(sum, nodeModule.getSubModuleList().get(i));
                }
            }
        }
        return num;
    }
}

