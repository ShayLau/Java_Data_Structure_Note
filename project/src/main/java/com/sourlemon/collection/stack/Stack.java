package com.sourlemon.collection.stack;


/**
 * @author sourlemon
 * @date 2019-02-13
 * @desciption 栈的基本功能接口说明
 */
public interface Stack<E> {

    /**
     * 获取栈的大小
     * @return 栈大小
     */
    int getSize();

    /**
     * 查看是否为空
     * @return if result==false  isEmpty else !isEmpty
     */
    boolean isEmpty();

    /**
     * 向栈中添加元素
     * @param e 添加元素
     */
    void push(E e);

    /**
     * 从栈中取元素
     * @return 取出的元素
     */
    E pop();

    /**
     * 获取栈的栈顶元素
     * @return 栈顶元素
     */
    E peek();
}
