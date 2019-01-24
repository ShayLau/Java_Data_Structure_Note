package com.sourlemon.collection.list;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sourlemon
 * @date 2019-01-21
 * @desciption ArrayList 数组列表的部分方法的简单分析
 * 1. ArrayList 是一个动态数组，它是线程不安全的,允许元素为null。
 * 2. 其底层数据结构依然是数组  ----->  new ArrayList ===   transient Object[] elementData;
 * 3. 它实现了List<E>, RandomAccess, Cloneable, java.io.Serializable接口;
 * 4. 为追求效率，ArrayList没有实现同步（synchronized），如果需要多个线程并发访问，用户可以手动同步，也可使用Vector替代。
 */
public class ArrayListDemo {


    /**
     * @author sourlemon
     * @desription 测试 ArrayList add(E element)方法，分析源码
     */
    public void testArrayListAdd() {
        List<Integer> numList = new ArrayList<Integer>();
        numList.add(1);
        // 使用add()方法在Arraylist数组列表添加元素，下标 index的起始位置从0开始,注意防止下标越界
        int numSize = numList.size();
        System.out.println("numList 的size：" + numSize+";");

        System.out.println("数组新增的元素数字：");
        for (int i = 0; i < numList.size(); i++) {
            System.out.print(numList.get(i) + ",");
        }
    }




    /**
     * @author sourlemon
     * @desription 测试 ArrayList add(int index,E element)方法，分析源码
     */
    public void testArrayListAdds() {
        List<Integer> numList = new ArrayList<Integer>();
        /**
         * ArrayList的add(int index,E element)方法底层实现  ---- >   public void add(int index, E element)
         * 源码注解: Inserts the specified element at the specified position in thislist. Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
         * 注解的大概意思：在这个集合指定的位置插入指定的元素,右移当前位置的元素以及当前位置后面的元素，前提是当前位置是有元素的
         * 重点就在这块： 为什么相比 LinkedList 链表，ArrayList的插入执行效率相对低， 就是因为ArrayList元素的插入添加时整体后移的， 这块得用测试工具测试一下
         * 源码分析：
         *      rangeCheckForAdd(index);   检查添加元素的位置 如果index 大于数组的 或者小于0 会抛出异常
         *      ensureCapacityInternal(size + 1);   检查容量
         *      System.arraycopy(elementData, index, elementData, index + 1,size - index); 索引移动
         *      elementData[index] = element;  元素赋值
         *      size++;   增加数组长度
         */
        numList.add(0, 2);
        for (int i = 0; i < numList.size(); i++) {

            System.out.print(numList.get(i) + ",");
        }
    }



    /**
     * @author sourlemon
     * @desription 测试 ArrayList set()方法，分析源码
     */
    public void testArrayListSet() {
        List<Integer> numList = new ArrayList<Integer>();
        numList.add(1);
        numList.add(2);
        numList.add(3);
        numList.add(4);
        numList.add(5);
        System.out.println("初始化5个数字：");
        for (int i = 0; i < numList.size(); i++) {
            System.out.print(numList.get(i) + ",");
        }
        int numSize = numList.size();
        /**
         *  set赋值
         *  使用set方法赋值的时候，会检查index的下标是否超过数组的大小
         *  检查方法为源码的  ---- > rangeCheck(index);
         *  if (index >= size) throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
         *
         */
        //numList.set(1,666);  //错误示例  下标位置越界， 调试过程 可以注解该行 比较set()的方法

        /**
         * 正确示例
         * 上面代码 numList.add(1) 已经添加元素，所以数组列表的size=1
         * 所以该行代码可以正常运行，
         * 但是正常的编写过程中，如果使用该方法
         * 容易造成下标越界的异常，因此需要根据具体情况控制index的位置防止越界
         */
        numList.set(0, 666);
        System.out.println(" \n numList的size：" + numSize + ",修改元素[0]位置后：" + numList.get(0));
        /**
         * ArrayList的set()方法底层实现  ---- >   public E set(int index, E element)
         * set 的形参数 int index, E element  下标和元素
         * 源码分析：
         *      rangeCheck(index);   检查下标的位置
         *      E oldValue = elementData(index);    获取 数组位置的元素,这里泛型类型是 Integer   所以 等同于 ====>   Integer oldValue= elementData(2);
         *      elementData[index] = element;     然后把我们的形参替换掉原来位置的信息  ====>  element[2]=10;
         *      最后 return oldVaule; 所以例子的 numList.set(2,10);   会返回原numList第三个元素的原值
         */
        numList.set(2, 10);
        System.out.println("\n使用Set()方法，修改第三个位置的元素，最新的数组列表信息为：");
        for (int i = 0; i < numList.size(); i++) {
            System.out.print(numList.get(i) + ",");
        }
    }


}

