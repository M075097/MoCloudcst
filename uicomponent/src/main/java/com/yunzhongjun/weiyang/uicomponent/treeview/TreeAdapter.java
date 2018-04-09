package com.yunzhongjun.weiyang.uicomponent.treeview;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wb-zyl208210 on 2017/3/31.
 */
public abstract class TreeAdapter {

    private static final String TAG = "TreeAdapter";
    private int[][] mGraphXY;
    private List<TreeNode> mTreeNodeList;
    private TreeNode mTreeNode;
    private int colNum;
    private int rowNum;
    private TreeGraphView mTreeGraphView;

    public TreeAdapter() {
        if (getNodeNum() != 0 && getTreeNodeData() != null) {
            initThreeGraphNodeBean(getNodeNum(), getTreeNodeData());
        }
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public int[][] getGraphXY() {
        if (mGraphXY == null) {
            Log.d(TAG, "坐标值没有生成！");
        }
        return mGraphXY;
    }

    public TreeNode getTreeNodeBean() {
        return mTreeNode;
    }

    public abstract View getNodeView();

    public abstract void bindView(View nodeView, INodeModule nodeData);

    public abstract INodeModule getTreeNodeData();//声明节点从属结构

    public abstract int getNodeNum();//声明 节点个数

    public void attatchTreeView(TreeGraphView treeGraphView) {
        this.mTreeGraphView = treeGraphView;
    }

    //需是树状的Bean，已经整理好的
    public void initThreeGraphNodeBean(int nodeNum, INodeModule rootNode) {
        //建立坐标系，用来判断新支线是否和已确定的有交叉，如有交叉，回溯重绘该支线
        mGraphXY = new int[nodeNum][nodeNum];
        //保存生成的节点信息
        mTreeNodeList = new ArrayList<>();
        mTreeNode = getNodeGraph(mTreeNodeList, rootNode, 0, 0, mGraphXY, 0);
        int colNum = 0;
        int rowNum = 0;
        boolean isRowNumSure = false;

        for (int i = 0; i < mGraphXY.length; i++) {//生成的坐标及位置示意图
            for (int j = 0; j < mGraphXY[i].length; j++) {
                System.out.print(mGraphXY[i][j]);
                if (mGraphXY[i][j] == 1) {
                    rowNum = i > rowNum ? i : rowNum;
                    colNum = j > colNum ? j : colNum;
                }
            }
            System.out.println();
        }
        this.colNum = colNum + 1;
        this.rowNum = rowNum + 1;
    }

    //该方法生成的二位数组已经删除布错的node
    public TreeNode getNodeGraph(List<TreeNode> graphNodeModuleList, INodeModule nodeModule, int xHierarchy, int yHierarchy, int[][] graphXY, int nodeHier) {
        if(nodeModule==null){
            return  null;
        }
        TreeNode treeNode = new TreeNode();
        treeNode.x = xHierarchy;//层级深度代表 x值

        if (graphXY[yHierarchy][xHierarchy] == 1) {//判断坐标上面该列a（及x = a）处的y = j处是否可放，如果可放，则赋值该点的y坐标，同时占位
            return null;//有重叠，则开始回溯，如果是不是分支根节点，则一直回溯
        } else {
            for (int i = yHierarchy; i < graphXY.length; i++) {//fixbug 线交叉
                if (graphXY[i][xHierarchy] == 1) {
                    return null;
                }
            }
            treeNode.y = yHierarchy;//基准y坐标，相对于分支基线只增不减
            graphXY[yHierarchy][xHierarchy] = 1;
            treeNode.setNodeModule(nodeModule);
            treeNode.des = nodeModule.getDes();
            graphNodeModuleList.add(treeNode);
            //TODO 保存Nodule的信息
        }

        if (nodeModule.getSubModuleList() != null && nodeModule.getSubModuleList().size() > 0) {
            List<TreeNode> subTreeNodeList = new ArrayList<>();//记录子节点信息
            List<INodeModule> subNodeModuleList = nodeModule.getSubModuleList();
            xHierarchy++;
            for (int i = 0; i < subNodeModuleList.size(); i++) {
                INodeModule subNodeModule = subNodeModuleList.get(i);
                if (i == 0) {//是分支的分支 一直return 到底
                    nodeHier++;//分支深度
                    TreeNode subTreeNode = getNodeGraph(graphNodeModuleList, subNodeModule, xHierarchy, yHierarchy, graphXY, nodeHier);
                    if (subTreeNode == null) {//每次回溯只删除一个，修改一个，全删删的过多，回溯到上一层还是要删
                        System.out.println("分支深度为" + nodeHier + "处重叠");
                        TreeNode treeNode1 = graphNodeModuleList.get(graphNodeModuleList.size() - 1);
                        System.out.println("原有值" + graphXY[treeNode1.y][treeNode1.x]);
                        graphXY[treeNode1.y][treeNode1.x] = 0;
                        System.out.println("修改值" + graphXY[treeNode1.y][treeNode1.x]);
                        graphNodeModuleList.remove(graphNodeModuleList.size() - 1);
                        return null;//一直回溯直到根节点
                    } else {
                        subTreeNodeList.add(subTreeNode);//已确定子节点位置添加
                    }
                } else {//回溯到此分支根节点，i！=0即为根节点 root节点除外 从此处记录分支深度
                    nodeHier = 0;
                    TreeNode subTreeNode1 = getNodeGraph(graphNodeModuleList, subNodeModule, xHierarchy, yHierarchy, graphXY, nodeHier);
                    if (subTreeNode1 == null) {
                        i = i - 1;//重新生成该分支
                    } else {
                        subTreeNodeList.add(subTreeNode1);//已确定子节点位置添加
                    }
                }
                yHierarchy++;//Y基线值++
            }
            treeNode.mSubTreeNodeList = subTreeNodeList;
        }
        return treeNode;//返回此时所处的分支的分支深度
    }

    //该方法生成的二位数组不会删除布错的node，但是不影响最终node的坐标值
    public TreeNode getNodeGraph(INodeModule nodeModule, int xHierarchy, int yHierarchy, int[][] graphXY, int nodeHier) {
        TreeNode treeNode = new TreeNode();
        treeNode.x = xHierarchy;//层级深度代表 x值
        if (graphXY[yHierarchy][xHierarchy] == 1) {//判断坐标上面该列a（及x = a）处的y = j处是否可放，如果可放，则赋值该点的y坐标，同时占位
            return null;//有重叠，则开始回溯，如果是不是分支根节点，则一直回溯
        } else {
            for (int i = yHierarchy; i < graphXY.length; i++) {//fixbug 线交叉
                if (graphXY[i][xHierarchy] == 1) {
                    return null;
                }
            }
            treeNode.y = yHierarchy;//基准y坐标，相对于分支基线只增不减
            graphXY[yHierarchy][xHierarchy] = 1;
            treeNode.setNodeModule(nodeModule);
            treeNode.des = nodeModule.getDes();
        }

        /*if (graphXY[yHierarchy][xHierarchy] == 1) {//判断坐标上面该列a（及x = a）处的y = j处是否可放，如果可放，则赋值该点的y坐标，同时占位
            return null;//有重叠，则开始回溯，如果是不是分支根节点，则一直回溯
        } else {
            mTreeNode.y = yHierarchy;//基准y坐标，相对于分支基线只增不减
            graphXY[yHierarchy][xHierarchy] = 1;
            mTreeNode.setNodeModule(nodeModule);
            mTreeNode.des = nodeModule.getDes();
        }*/

        if (nodeModule.getSubModuleList() != null && nodeModule.getSubModuleList().size() > 0) {
            List<TreeNode> subTreeNodeList = new ArrayList<>();//记录子节点信息
            List<INodeModule> subNodeModuleList = nodeModule.getSubModuleList();
            xHierarchy++;
            for (int i = 0; i < subNodeModuleList.size(); i++) {
                INodeModule subNodeModule = subNodeModuleList.get(i);
                if (i == 0) {//是分支的分支 一直return 到底
                    nodeHier++;//分支深度
                    TreeNode subTreeNode = getNodeGraph(subNodeModule, xHierarchy, yHierarchy, graphXY, nodeHier);
                    if (subTreeNode == null) {
                        System.out.println("分支深度为" + nodeHier + "处重叠");
                        return null;//一直回溯到根节点
                    } else {
                        subTreeNodeList.add(subTreeNode);//已确定子节点位置添加
                    }
                } else {//回溯到此分支根节点，i！=0即为根节点 root节点除外 从此处记录分支深度
                    nodeHier = 0;
                    TreeNode subTreeNode1 = getNodeGraph(subNodeModule, xHierarchy, yHierarchy, graphXY, nodeHier);
                    if (subTreeNode1 == null) {
                        i = i - 1;//重新生成该分支
                    } else {
                        subTreeNodeList.add(subTreeNode1);//已确定子节点未知添加
                    }
                }
                yHierarchy++;//各分支Y基线值++
            }
            treeNode.mSubTreeNodeList = subTreeNodeList;
        }
        return treeNode;//返回此时所处的分支的分支深度
    }

    public void notifyDataSetChange() {
        mTreeGraphView.notifyDataSetChange();
    }
}
