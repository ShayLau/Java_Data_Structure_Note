package com.sourlemon.collection.stack;

import com.sourlemon.collection.array.DynamicArray;

/**
 * @author sourlemon
 * @date 2019-02-13
 * @desciption 数组结构的栈实现
 */
public class ArrayStack<E> implements Stack<E> {


    private DynamicArray<E>  stack=new DynamicArray<E>();



    public int getSize() {
        return stack.getSize();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void push(E e) {
        stack.addLast(e);
    }

    public E pop() {
        return stack.removeLast();
    }

    public E peek() {
        return stack.getLast();
    }


    @Override
    public String toString() {
        StringBuilder res=new StringBuilder();


        for(int i=0;i<stack.getSize();i++){

        }
        res.append("「");
        for(int i=0;i<stack.getSize();i++){
            res.append(stack.get(i));
            res.append(",");
        }
        res.append("」");

        return res.toString();
    }
}
