package com.yunzhongjun.weiyang.uicomponent.treeview;


import java.util.ArrayList;
import java.util.List;

/**
 * @author weiyang
 * @date 2017/03/31
 */
public class NodeTreeHelper {
 /*   public void get() {
        INodeModule rootNodeModule = mockData();
        List<TreeNode> node = new ArrayList<>();//保存生成的节点，信息
        int nodeNum = 30;
        int[][] graphXY = new int[30][30];
        TreeNode nodeGraph = getNodeGraph(node, rootNodeModule, 0, 0, graphXY, 0);
        int[][] graphXY2 = new int[30][30];
        TreeNode nodeGraph1 = getNodeGraph( rootNodeModule, 0, 0, graphXY2, 0);

        for (int i = 0; i < graphXY.length; i++) {
            for (int j = 0; j < graphXY[i].length; j++) {
                System.out.print(graphXY[i][j]);
            }
            System.out.println();
        }

        for (int i = 0; i < graphXY2.length; i++) {
            for (int j = 0; j < graphXY2[i].length; j++) {
                System.out.print(graphXY2[i][j]);
            }
            System.out.println();
        }
    }
*/

    public TreeNode getNodeGraph(INodeModule nodeModule, int xHierarchy, int yHierarchy, int[][] graphXY, int nodeHier) {
        TreeNode treeNode = new TreeNode();
        treeNode.x = xHierarchy;//层级深度代表 x值

        if (graphXY[yHierarchy][xHierarchy] == 1) {//判断坐标上面该列a（及x = a）处的y = j处是否可放，如果可放，则赋值该点的y坐标，同时占位
            return null;//有重叠，则开始回溯，如果是不是分支根节点，则一直回溯
        } else {
            treeNode.y = yHierarchy;//基准y坐标，相对于分支基线只增不减
            graphXY[yHierarchy][xHierarchy] = 1;
            treeNode.setNodeModule(nodeModule);
            //graphNodeModuleList.add(mTreeNode);
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
                    TreeNode subTreeNode = getNodeGraph(subNodeModule, xHierarchy, yHierarchy, graphXY, nodeHier);
                    if (subTreeNode == null) {
                        System.out.println("分支深度为" + nodeHier + "处重叠");
                        //TreeNode nodeBean1 = graphNodeModuleList.get(graphNodeModuleList.size() - 1);
                        //System.out.println("原有值" + graphXY[nodeBean1.y][nodeBean1.x]);
                        //graphXY[nodeBean1.y][nodeBean1.x] = 0;
                        //System.out.println("修改值" + graphXY[nodeBean1.y][nodeBean1.x]);
                        //graphNodeModuleList.remove(graphNodeModuleList.size() - 1);
                       /* for (int h = 0; h < nodeHier; h++) {//每次回溯只删除一个，修改一个，全删删的过多，回溯到上一层还是要删
                            TreeNode nodeBean1 = graphNodeModuleList.get(graphNodeModuleList.size() - 1);
                            System.out.println("原有值" + graphXY[nodeBean1.y][nodeBean1.x]);
                            //graphXY[nodeBean1.y][nodeBean1.x] = 0;
                            System.out.println("修改值" + graphXY[nodeBean1.y][nodeBean1.x]);
                            graphNodeModuleList.remove(graphNodeModuleList.size() - 1);
                        }*/
                        return null;//一直回溯到根节点
                    } else {
                        subTreeNodeList.add(subTreeNode);//已确定子节点未知添加
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
                yHierarchy++;//Y基线值++
            }
            treeNode.mSubTreeNodeList = subTreeNodeList;
        }
        return treeNode;//返回此时所处的分支的分支深度
    }


    public TreeNode getNodeGraph(List<TreeNode> graphNodeModuleList, INodeModule nodeModule, int xHierarchy, int yHierarchy, int[][] graphXY, int nodeHier) {
        TreeNode treeNode = new TreeNode();
        treeNode.x = xHierarchy;//层级深度代表 x值

        if (graphXY[yHierarchy][xHierarchy] == 1) {//判断坐标上面该列a（及x = a）处的y = j处是否可放，如果可放，则赋值该点的y坐标，同时占位
            return null;//有重叠，则开始回溯，如果是不是分支根节点，则一直回溯
        } else {
            treeNode.y = yHierarchy;//基准y坐标，相对于分支基线只增不减
            graphXY[yHierarchy][xHierarchy] = 1;
            treeNode.setNodeModule(nodeModule);
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
                    if (subTreeNode == null) {
                        System.out.println("分支深度为" + nodeHier + "处重叠");
                        TreeNode treeNode1 = graphNodeModuleList.get(graphNodeModuleList.size() - 1);
                        System.out.println("原有值" + graphXY[treeNode1.y][treeNode1.x]);
                        graphXY[treeNode1.y][treeNode1.x] = 0;
                        System.out.println("修改值" + graphXY[treeNode1.y][treeNode1.x]);
                        graphNodeModuleList.remove(graphNodeModuleList.size() - 1);
                       /* for (int h = 0; h < nodeHier; h++) {//每次回溯只删除一个，修改一个，全删删的过多，回溯到上一层还是要删
                            TreeNode treeNode1 = graphNodeModuleList.get(graphNodeModuleList.size() - 1);
                            System.out.println("原有值" + graphXY[treeNode1.y][treeNode1.x]);
                            //graphXY[treeNode1.y][treeNode1.x] = 0;
                            System.out.println("修改值" + graphXY[treeNode1.y][treeNode1.x]);
                            graphNodeModuleList.remove(graphNodeModuleList.size() - 1);
                        }*/
                        return null;//一直回溯到根节点
                    } else {
                        subTreeNodeList.add(subTreeNode);//已确定子节点未知添加
                    }
                } else {//回溯到此分支根节点，i！=0即为根节点 root节点除外 从此处记录分支深度
                    nodeHier = 0;
                    TreeNode subTreeNode1 = getNodeGraph(graphNodeModuleList, subNodeModule, xHierarchy, yHierarchy, graphXY, nodeHier);
                    if (subTreeNode1 == null) {
                        i = i - 1;//重新生成该分支
                    } else {
                        subTreeNodeList.add(subTreeNode1);//已确定子节点未知添加
                    }
                }
                yHierarchy++;//Y基线值++
            }
            treeNode.mSubTreeNodeList = subTreeNodeList;
        }
        return treeNode;//返回此时所处的分支的分支深度
    }
}


