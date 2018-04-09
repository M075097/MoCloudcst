package com.yunzhongjun.weiyang.uicomponent.treeview;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by wb-zyl208210 on 2017/3/31.
 */
@Deprecated
public class TreeHolder {
    public TreeNode mTreeNode;
    public int rowNum;
    public int colNum;
    public List<TreeNode> mTreeNodeList;
    public int[][] graphXY;

    public View getNodeView(Context context) {
        //View nodeView = View.inflate(context, R.layout.view_treenode, null);
        return null;
    }
}
