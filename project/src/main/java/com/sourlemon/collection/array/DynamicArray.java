package com.sourlemon.collection.array;

import java.util.Arrays;

/**
 * @author sourlemon
 * @date 2019-02-13
 * @desciption 动态数组的实现
 * 简单模拟 ArrayList 实现，其中不涉及到modCount的实现，不涉及到数组容量的阈值及一些其他的操作
 *
 */
public class DynamicArray<E> {

    //动态数据数据
    private E [] data;
    //动态数组大小
    private int size;

    /**
     * 构造默认容量10的动态数组
     */
    public DynamicArray() {
        this.data=(E[])new Object[10];
        this.size=0;
    }

    /**
     * 构造动态大小的数组
     * @param capacity 容量
     */
    public DynamicArray(int capacity) {
        this.data=(E[])new Object[capacity];
        this.size=0;
    }

    //获取数组大小
    public int getSize(){
        return this.size;
    }

    //获取数组的实际容量
    private int getCapacity(){
        return data.length;
    }

    //数组是否为空
    public  boolean  isEmpty(){
        return this.size==0;
    }

    /**
     * 添加元素
     * @param element 元素
     *
     */
    public  void addLast(E element){

        if(size==this.getCapacity()){
            dilatation();
        }
        data[size]=element;
        size++;
    }

    /**
     * 指定元素位置添加元素
     * @param index 下标
     * @param element 元素
     *
     */
    public void add(int index,E element){

        if(!checkIndex(index)){
            return;
        }
        if(index==this.getCapacity()){
            dilatation();
        }
        System.arraycopy(data,index,data,index+1,size-index);
        data[index]=element;
        size++;
    }

    /**
     * 数组扩容
     */
    private void dilatation(){

        int oldCapacity=this.getCapacity();
        int newCapacity=oldCapacity+(oldCapacity>>1);

        data=Arrays.copyOf(data,newCapacity);
    }


    /**
     * 检查下标
     * @param index 下标
     * @return
     */
    public boolean checkIndex(int index){
        if(index<0||index>size){
            throw new  IndexOutOfBoundsException("index 下标越界");
        }
        return true;
    }

    /**
     * 获取元素
     * @param index 下标
     * @return  未获取到元素 返回-1
     */
    public E get(int index){
        if(!checkIndex(index)){

        }
        return data[index];
    }

    /**
     * 获取元素
     * @return  获取最后的元素
     */
    public E getLast(){
        return data[size-1];
    }

    /**
     * 设置元素
     * @param index 下标
     * @return  未获取到元素 返回-1
     */
    public void  set(int index,E element){
        if(checkIndex(index)){
            data[index]=element;
        }
    }

    /**
     * 包含元素
     * @param e 元素
     * @return
     */
    public boolean contains(E  e){
        for(int i=0;i<size;i++){
            if(data[i].equals(e)){
                return true;
            }
        }
        return false;
    }

    /**
     * 查找下标
     * @param e 元素
     * @return
     */
    public int findIndex(E  e){
        for(int i=0;i<size;i++){
            if(data[i].equals(e)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 删除元素
     * @param index 下标
     */
    public E remove(int index){
        if(!checkIndex(index)){

        }
        E removeVal=data[index];
        System.arraycopy(data,index+1,data,index,size-index+1);
        size--;
       // data[size]=null; //loitering  object  流浪的对象
        return removeVal;
    }

    /**
     * 删除最后的元素
     */
    public E removeLast(){
        int index=size-1;
        E removeVal=data[index];
        System.arraycopy(data,index+1,data,index,size-index+1);
        size--;
        // data[size]=null; //loitering  object  流浪的对象
        return removeVal;
    }


    @Override
    public String toString() {
        StringBuilder res=new StringBuilder();
        res.append("「");
        for(int i=0;i<size;i++){
            res.append(data[i]);
            res.append(",");
        }
        res.append("」");
       return res.toString();
    }
}
