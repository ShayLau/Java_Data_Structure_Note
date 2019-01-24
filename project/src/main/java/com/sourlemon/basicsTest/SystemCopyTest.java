package com.sourlemon.basicsTest;

import java.util.Arrays;

/**
 * @author sourlemon
 * @description 数组的拷贝测试
 */
public class SystemCopyTest {


    /**
     * @author sourlemon
     * 测试System.ArrayCopy方法的简单使用
     */
    public void copyTest() {

        int[] sourceArray = new int[]{11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21};  //源数组

        int[] destArray = new int[20];  //目标数组
        destArray[0] = 1;
        destArray[1] = 2;
        destArray[2] = 3;
        destArray[3] = 4;

        System.out.println("执行 System.ArrayCopy之前");

        for (int i = 0; i < destArray.length; i++) {
            System.out.print(destArray[i] + " ,");
        }

        System.arraycopy(sourceArray, 5, destArray, 0, 6);

        System.out.println("\n执行 System.ArrayCopy之后");
        for (int i = 0; i < destArray.length; i++) {
            System.out.print(destArray[i] + " ,");
        }

    }

    /**
     * @author sourlemon
     * @description 通过 <method>System.ArrayCopy(...)</method>,模拟 ArrayList的元素移动实现
     * <p>
     * 模拟过程：
     * 1.准备一个int[]
     * 2.位移参数设置
     * 3.数组扩容(当然，这里面只是简单的按照1.5倍扩容)，如果不扩容，位置移动 会出现 <error>IndexOutOfBoundsException</error> 下标位置越界
     * 4.使用 <method>System.ArrayCopy(...)</method> 方法进行元素copy
     * 5.设置移动位置的元素
     */
    public void testArrayShift() {

        //1.准备一个int[]
        int[] num = new int[10];
        for (int i = 0; i < num.length; i++) {
            num[i] = i;
        }

        //2.位移参数设置
        int moveIndex = 5; //要在该位置开始插入元素，后面的元素后移
        int destpos = moveIndex;  //目标数组的开始拷贝位置
        destpos++;
        int length = num.length - moveIndex; //拷贝长度(例子中要移动位置的元素 到数组的结尾)


        System.out.println("执行System.ArrayCopy之前");
        for (int i = 0; i < num.length; i++) {
            System.out.print(num[i] + " ,");
        }


        //3.数组扩容
        int newLength = num.length;
        newLength = newLength + (newLength >> 1);  //ArrayList 按照1.5倍扩容
        num = Arrays.copyOf(num, newLength);

        //4.模拟元素后移
        System.arraycopy(num, moveIndex, num, destpos, length);

        //5.设置移动位置的元素
        int insertNum = 100;
        num[destpos] = insertNum;


        System.out.println("\n执行System.ArrayCopy之后");
        for (int i = 0; i < num.length; i++) {
            System.out.print(num[i] + " ,");
        }


    }


}