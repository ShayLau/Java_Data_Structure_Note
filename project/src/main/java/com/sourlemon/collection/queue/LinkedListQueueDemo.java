package com.sourlemon.collection.queue;

import java.util.LinkedList;
import java.util.List;

/**
 * @author sourlemon
 * @date 2019-01-27
 * @desciption linkedList中Queue测试
 */
public class LinkedListQueueDemo {


    /**
     * @author sourlemon
     * @desciption linkedList中Deque 测试
     */
    public void dequeDemo() {

        LinkedList<Integer> nums = new LinkedList<Integer>();
        nums.offer(1);  //add  队列的尾部添加

        nums.offerFirst(0); //add  队列的首部添加

        nums.offerLast(2);  //add  队列的尾部添加


        int firstNum = nums.peek();  //get   队列的首部  不会删除索引位置元素
        System.out.println("firstNum:" + firstNum);

        firstNum = nums.peekFirst(); //get  队列的首部  不会删除索引位置元素
        System.out.println("firstNum:" + firstNum);

        int lastNum = nums.peekLast(); //get 队列的尾部  不会删除索引位置元素
        System.out.println("lastNum:" + lastNum);


        int pollNum = nums.poll();  //remove  队列的首部 同时删除首个元素
        System.out.println("pollNum:" + pollNum);

        pollNum = nums.pollFirst(); //remove   队列的首部 同时删除首个元素
        System.out.println("pollNum:" + pollNum);

        int pollLastNum = nums.pollLast(); //remove   队列的尾部 同时删除该元素
        System.out.println("pollLastNum:" + pollLastNum);

        nums.push(100); //add  队列的首部添加
        firstNum = nums.peekFirst(); //get  队列的首部  不会删除索引位置元素
        System.out.println("firstNum:" + firstNum+",push集合长度："+nums.size());


        nums.pop(); //remove  删除首部元素
        System.out.println("删除的元素:" + firstNum+",pop后集合长度："+nums.size());

    }
}
