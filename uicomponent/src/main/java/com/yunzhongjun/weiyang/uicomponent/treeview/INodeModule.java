package com.yunzhongjun.weiyang.uicomponent.treeview;

import java.util.List;

/**
 * @author weiyang
 * @date 2017/03/31
 */
public interface INodeModule<T> {//TODO T 更换为INodeBean
    List<T> getSubModuleList();

    String getDes();
}
