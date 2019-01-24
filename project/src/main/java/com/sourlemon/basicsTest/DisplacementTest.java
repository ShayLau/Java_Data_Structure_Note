package com.sourlemon.basicsTest;

/**
 * @author sourlemon
 * @date 2019-01-24
 * @desciption 位移运算测试
 */
public class DisplacementTest {

    /**
     * @author sourlemon
     * @description 测试位移运算
     */
    public void testDisplacement() {

        int num = 10;

        int newNum = num << 2;   //右位移

        int newNum2= num >> 2;   //左位移

        System.out.print(newNum);
    }

}
