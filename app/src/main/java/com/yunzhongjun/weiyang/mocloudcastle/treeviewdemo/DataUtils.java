package com.yunzhongjun.weiyang.mocloudcastle.treeviewdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by weiyang on 2018/4/9 0009.
 */

public class DataUtils {
    public static NodeModule mockData() {
        Random random = new Random();
        NodeModule rootNodeModule = new NodeModule();
        rootNodeModule.deviceName = "网管";
        rootNodeModule.uuid = "987654";
        rootNodeModule.weight = random.nextInt(100) + "";
        if (true) {
            rootNodeModule.nodeList = getFullNodeList(5, 0);
            return rootNodeModule;
        }
        rootNodeModule.nodeList = getNodeList(5);
        for (int i = 0; i < rootNodeModule.nodeList.size(); i++) {
            NodeModule nodeModule = rootNodeModule.nodeList.get(i);
            if (i == 0) {
                nodeModule.nodeList = getNodeList(2);
                for (int j = 0; j < nodeModule.nodeList.size(); j++) {
                    if (j == 0) {
                        NodeModule nodeModule1 = nodeModule.nodeList.get(j);
                        nodeModule1.nodeList = getNodeList(1);
                        nodeModule1.nodeList.get(0).nodeList = getNodeList(1);
                    }
                }
            } else if (i == 1) {
                nodeModule.nodeList = getNodeList(3);
                for (int i1 = 0; i1 < nodeModule.nodeList.size(); i1++) {
                    if (i1 == 1) {
                        nodeModule.nodeList.get(1).nodeList = getNodeList(1);
                        nodeModule.nodeList.get(1).nodeList.get(0).nodeList = getNodeList(1);
                    }
                }
                for (int j = 0; j < nodeModule.nodeList.size(); j++) {
                    if (j == 0) {
                        NodeModule nodeModule1 = nodeModule.nodeList.get(j);
                        nodeModule1.nodeList = getNodeList(1);
                        nodeModule1.nodeList.get(0).nodeList = getNodeList(3);
                    }
                }
            }
        }
        return rootNodeModule;
    }

    //int yHierarchy = 0;
    public static List<NodeModule> getNodeList(int nodeNum) {
        NodeModule rootNodeModule;
        String[] nodeNames = new String[]{"电视", "空调", "微波炉", "吸尘器", "摄像头"};
        List<NodeModule> nodeModuleListbb = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < nodeNum; i++) {
            rootNodeModule = new NodeModule();
            rootNodeModule.deviceName = nodeNames[random.nextInt(5)];
            rootNodeModule.uuid = "987654" + i;
            rootNodeModule.weight = random.nextInt(100) + "";
            nodeModuleListbb.add(rootNodeModule);
        }
        return nodeModuleListbb;
    }


    public static List<NodeModule> getFullNodeList(int nodeNum, int m) {
        NodeModule rootNodeModule;
        String[] nodeNames = new String[]{"电视", "空调", "微波炉", "吸尘器", "摄像头"};
        List<NodeModule> nodeModuleListbb = new ArrayList<>();
        Random random = new Random();
        m++;
        for (int i = 0; i < nodeNum; i++) {
            rootNodeModule = new NodeModule();
            rootNodeModule.deviceName = nodeNames[random.nextInt(5)];
            rootNodeModule.uuid = "987654" + i;
            rootNodeModule.weight = random.nextInt(100) + "";
            if (m <= 6) {
                rootNodeModule.nodeList = getFullNodeList(random.nextInt(3), m);
            }
            nodeModuleListbb.add(rootNodeModule);
        }
        return nodeModuleListbb;
    }
}
