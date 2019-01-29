package com.sourlemon.basicsTest;

/**
 * @author sourlemon
 * @date 2019-01-29
 * @desciption 异或运算符 demo
 */
public class XorDemo {


    /**
     * @author sourlemon
     * 计算测试
     */
    public void computeDemo() {

        int num1 = 2; //0010
        int num2 = 3; //0011


        /**
         * 计算规则：相同的为0,不同的为1
         * 模拟：
         * num1   -->0010
         * num2   -->0011
         * result -->0001
         */
        int result = num1 ^ num2;  //0001  1

        System.out.println("异或运算结果：" + result);
    }

}
