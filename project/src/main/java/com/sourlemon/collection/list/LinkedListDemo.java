package com.sourlemon.collection.list;

import java.util.LinkedList;
import java.util.List;

/**
 * @author sourlemon
 * @date 2019-01-25
 * @desciption linkedList 链表数据集合的简单源码分析
 *
 */
public class LinkedListDemo {

    /**
     * @author sourlemon
     * @description 初始化的demo 测试
     */
    public  void initDemo(){

        List<Integer> list=new LinkedList<Integer>();

    }


    /**
     * @author sourlemon
     * @desription 添加元素
     */
    public void addDemo(){

        List<Integer> list=new LinkedList<Integer>();

        list.add(1);

        for(int i=0;i<list.size();i++)
            System.out.println("list.get("+i+"):"+list.get(i));

    }

    /**
     * @author sourlemon
     * @desription 在一个集合的指定位置添加集合
     */
    public void addCollectionDemo(){

        List<Integer> list=new LinkedList<Integer>();

        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);


        List<Integer> appendList=new LinkedList<Integer>();

        appendList.add(10);
        appendList.add(11);
        appendList.add(12);
        appendList.add(13);
        appendList.add(14);
        appendList.add(15);

        list.addAll(3,appendList);

        for(int i=0;i<list.size();i++)
            System.out.println("list.get("+i+"):"+list.get(i));

    }





    /**
     * @author sourlemon
     * @desription 根据在指定位置添加元素，debug查看执行流程
     * @param index 索引位置
     * @param o 插入元素
     */
    public void addByIndexDemo(int index,Object o){

        List<Integer> list=new LinkedList<Integer>();

        for(int i=0;i<10;i++)
            list.add(i);

        //在第索引4，插入个元素
        list.add(index,(Integer) o);

        for(int i=0;i<list.size();i++)
            System.out.println("list.get("+i+"):"+list.get(i));

    }




}
