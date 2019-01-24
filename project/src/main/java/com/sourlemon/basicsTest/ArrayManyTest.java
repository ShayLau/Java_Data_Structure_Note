package com.sourlemon.basicsTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sourlemon
 * @date 2019-01-23
 * @desciption 集合框架涉及到的一些知识点测试
 */
public class ArrayManyTest {


    /**
     * @author sourlemon
     * 测试Arrays 集合工具类
     */
    public void testArrays() {

        String[] objects = new String[]{"hello", "world"};

        String[] objects1 = Arrays.copyOf(objects, 8);

        System.out.println("objects:" + objects.length);
        for (int i = 0; i < objects.length; i++) {
            System.out.println("objects[" + i + "]" + objects[i]);
        }
        System.out.println("object1:" + objects1.length);
        for (int i = 0; i < objects1.length; i++) {
            System.out.println("objects1[" + i + "]" + objects1[i]);
        }
    }

    /**
     * @author sourlemon
     * @description 测试ArrayList 数组列表的初始化容量
     */
    public void testListCapacity() {

        List<String> strList = new ArrayList<String>(20);

        strList.add("Hello World");

        System.out.println(strList.size());

    }

}


