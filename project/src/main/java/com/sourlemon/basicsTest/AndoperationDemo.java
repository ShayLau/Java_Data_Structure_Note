package com.sourlemon.basicsTest;

/**
 * @author sourlemon
 * @date 2019-01-29
 * @desciption 与运算符 demo
 */
public class AndoperationDemo {

    /**
     * @author sourlemon
     * 计算测试
     */
    public void computeDemo() {

        int num1 = 2; //0010
        int num2 = 3; //0011
        /**
         * 计算规则：只要有一个是0就算成0
         * 模拟：
         * num1   -->0010
         * num2   -->0011
         * result -->0010
         */
        int result = num1 ^ num2;  //0010  ---> 1

        System.out.println("与运算结果：" + result);
    }
}
