package com.yunzhongjun.weiyang.mocloudcastle.treeviewdemo;

import com.yunzhongjun.weiyang.uicomponent.treeview.INodeModule;

import java.util.List;

/**
 * Created by weiyang on 2018/4/9 0009.
 */

public class NodeModule implements INodeModule<NodeModule> {

    public String deviceName;
    public String uuid;
    public String weight;
    public List<NodeModule> nodeList;
    public int nodeType;

    @Override
    public List<NodeModule> getSubModuleList() {
        return nodeList;
    }

    @Override
    public String getDes() {
        return null;
    }
}
