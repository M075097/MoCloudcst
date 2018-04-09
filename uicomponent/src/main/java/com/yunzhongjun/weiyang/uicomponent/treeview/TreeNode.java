package com.yunzhongjun.weiyang.uicomponent.treeview;

import java.util.List;

/**
 * Created by wb-zyl208210 on 2017/3/30.
 * 绘制TreeView直接用到的Module
 * 还有一个INodeBean，是实际绘制时传入的Node必须实现INodeBean
 */
public class TreeNode<T extends INodeModule> {
    public int x;//坐标信息
    public int y;//坐标信息
    public String des;
    public TreeNode parentNote;
    public List<TreeNode> mSubTreeNodeList;
    private T nodeModule;//节点的描述信息

    public T getNodeModule() {//拿取具体描述信息
        return nodeModule;
    }

    public void setNodeModule(T nodeModule) {//保存应有描述信息
        this.nodeModule = nodeModule;
    }

    public List<TreeNode> getSubTreeNodeList() {//返回子节点信息
        return mSubTreeNodeList;
    }
}
