## 浅谈ArrayList分析源码过程



1. ArrayList分析前必读
2. 方法解析
3. 源码中的单词
4. 比较重要的部分
5. 参阅文章
6. 文章说明



#### 1.ArrayList分析前必读



1. `ArrayList` 是一个**动态数组**，它是**线程不安全**的,允许元素为null，在执行System.arraycopy移动索引时可以看出来。
2. 其底层数据结构依然是**数组** , `new ArrayList ===   transient Object[ ] elementData;` 
3. 它实现了`List<E>, RandomAccess, Cloneable, java.io.Serializable`接口;
4. 为追求效率，`ArrayList`没有实现同步（`synchronized`），如果需要多个线程并发访问，用户可以手动同步，也可使用`Vector`替代。



源码分析重要的一些字段,组成了Arraylist的代码逻辑,如下：

`private static final int DEFAULT_CAPACITY = 10; //静态常量默认的容量`  

`private static final Object[] EMPTY_ELEMENTDATA = {}; 空的数组元素`

`private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};//默认的空数组元素`	

`transient Object[] elementData; //ArrayList真正存放数组元素的信息`

`private int size; //ArrayList 的数组大小`



**怎么看方法解析：**

	*先查看` add(int index, E element)`方法，其他的方法大致都是一样的，*细细查看该方法，后面基本可以快熟查阅。

	*如果对数据结构基础不是很熟，请先阅读**第4部分 比较重要的部分**，一个是位移运算，一个是`System.ArrayCopy（...）`的文章说明。ArrayList其实就是操作数组元素的增删改查。*



**源码分析的版本说明：**

	分析的Jdk版本为**jdk8**



#### 2.方法解析



#### ***增加方法***

##### 1. add(int index, E element)

**方法含义**：在指定索引位置，插入指定的元素

**方法签名：**

 ` add(int index, E element)`  

方法名：`add   `

方法参数：`int index` 索引下标 , `E element` 泛型元素



**源码：**

```java
/**
 * Inserts the specified element at the specified position in this
 * list. Shifts the element currently at that position (if any) and
 * any subsequent elements to the right (adds one to their indices).
 * 注解的大概意思：在这个数组集合指定的位置插入指定的元素,右移当前位置的元素以及当前位置后面的元素，
 * 前提是当前位置是有元素的。
 * @param index index at which the specified element is to be inserted 
 * 将插入指定元素的索引
 * @param element element to be inserted  
 * 将要插入的元素
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public void add(int index, E element) {
    rangeCheckForAdd(index); 

    ensureCapacityInternal(size + 1);  // Increments modCount!!
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);
    elementData[index] = element;
    size++;
}
```



**源码详细分析：**



**步骤： 1.检查下标--->2.检查内部容量---->3.确定容量---->4.数组扩容-->5.索引移动-->6.设置元素值**



 *1.检查下标*：` rangeCheckForAdd(index); `

```java
/**
 * A version of rangeCheck used by add and addAll.
 * 检查添加元素的位置 如果index 大于数组的大小或者小于0会抛出异常 
 * 所以，使用 add(int index, E element) 方法时
 * 如果列表中没有数据，必须从下标0开始，否则会出现越界
 * 如果列表中有数据，注意不要超过 size的大小
 * index 的位置可以是追加的，当index == size 时
 * 也可以是插入到指定位置前的
 */
private void rangeCheckForAdd(int index) {
    if (index > size || index < 0)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```

*2.检查内部容量*：` ensureCapacityInternal(size + 1);   `		

```java
 /**
 * 检查内部容量
 * 先检查 ArrayList是不是一个空的数组(没有执行过添加或数组列表时是空的)，
 * 如果是空的，最小容量minCapacity就是10
 * 如果不是空的ArrayList 就开始明确容量
 */
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }

    ensureExplicitCapacity(minCapacity);
}
```

*3.确定容量：*

```java
/**
* 修改次数modCount +1操作
* 如果最小容量minCapacity 比ArrayList 的elementData数组的length大的话
* 执行数组扩容
*/
private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```

*4.数组扩容：*

```java
/**
 * Increases the capacity to ensure that it can hold at least the
 * number of elements specified by the minimum capacity argument.
 * 增加容量，以确保它至少能容纳最小容量参数指定的元素数
 *
 * @param minCapacity the desired minimum capacity  最小容量
 * 先判断当前数组的大小，
 * 如果数组空的 newCapacity就会小于默认的大小，容量就会变为10
 * 如果数组长度超过 Integer.maxValue 就会出现 OutOfMemoryError();
 * 最后使用 Arrays.copyOf()将 elementData 容量扩容
 */
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

*5.索引移动 和元素赋值：*

```java
/**
 * 索引移动和元素赋值
 * 通过 System.arrayCopy(....) 来操作数组的元素的下标位置
 * 重点：
 * 为什么相比 LinkedList 链表比ArrayList的插入执行效率相对低？ 
 * ArrayList元素的插入添加元素的情况，会把index开始的位置整体后移
 * 也就意味着该方法有着线性的时间复杂度，根据index在整个数组中的位置，来决定效率
 *
 */
public void add(int index, E element) {
    rangeCheckForAdd(index);

    ensureCapacityInternal(size + 1);  // Increments modCount!!
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);   // //将index索引位置的元素整体向后移动一位
    elementData[index] = element; //index下标位置赋值
    size++; 增加长度
}
```





##### 2. add(E e)

**方法含义**：插入一个指定的元素

**方法签名：**

` add( E e)`   

方法名：`add` 

方法参数：`E e`  泛型元素



**源码：**

```java
/**
 * Appends the specified element to the end of this list.
 * 方法含义：在这个list列表后面追加指定的元素
 * @param e element to be appended to this list
 
 * 参数：要追加的元素
 * @return <tt>true</tt> (as specified by {@link Collection#add})
 * 返回值： 默认返回true
 */
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}
```



**源码详细分析：**

**步骤：1.检查容量--->2.检查内部容量---->3.确定容量---->4.数组扩容-->5.设置元素值**



` add( E e)`    方法详细说明：

1. 源码` ensureCapacityInternal` 对应的步骤2、3、4不再重复叙述，查看`add(int index,E elemtn)`方法解析;
2. 相比于`Add(int index,Element e) `  ，`Add(E e)`简单多了，只需要考虑容量问题，然后直接赋值即可。后者直接是 `append`的方式，追加到数组列表最后，而前者元素位置移动。







##### 3.addAll(int index, Collection<? extends E> c) 

*方法含义：在指定索引位置，插入一个泛型子类集合*

**方法签名：**

 ` addAll(int index, Collection<? extends E> c)`  

 方法名:`addAll`

 方法参数：`int index`索引下标 ， `Collection<? extends E> c`    泛型子类的集合



**源码：**

```java
/**
 * Inserts all of the elements in the specified collection into this
 * list, starting at the specified position.  Shifts the element
 * currently at that position (if any) and any subsequent elements to
 * the right (increases their indices).  The new elements will appear
 * in the list in the order that they are returned by the
 * specified collection's iterator.
 * 注解：从指定的位置开始，将指定集合中的所有元素插入到此列表中。
 * 如果当前位置有元素，就把后面的元素话右移。
 * 新元素将按指定集合的迭代器返回的顺序出现在列表中。
 *
 * @param index index at which to insert the first element from the
 *              specified collection
 * 参数： 要插入的指定集合的第一个元素的索引位置
 * @param c collection containing elements to be added to this list
 * 参数： 包含要添加到此列表中的元素的集合
 * @return <tt>true</tt> if this list changed as a result of the call
 * 返回值：元素修改成功返回true
 * @throws IndexOutOfBoundsException {@inheritDoc}
 * @throws NullPointerException if the specified collection is null
 */
public boolean addAll(int index, Collection<? extends E> c) {
    rangeCheckForAdd(index);

    Object[] a = c.toArray();
    int numNew = a.length;
    ensureCapacityInternal(size + numNew);  // Increments modCount

    int numMoved = size - index;
    if (numMoved > 0)
        System.arraycopy(elementData, index, elementData, index + numNew,
                         numMoved);

    System.arraycopy(a, 0, elementData, index, numNew);
    size += numNew;
    return numNew != 0;
}
```



**源码详细分析：**

**步骤：1.检查下标--->2.获取元素长度--->3.检查内部容量---->4.确定容量---->5.数组扩容-->6.索引移动-->7.设置元素值-->8.修改数组大小**



 ` addAll(int index, Collection<? extends E> c)`  方法详细说明：



1. 源码` ensureCapacityInternal` 对应的步骤3、4、5不再重复叙述，查看`add(int index,E elemtn)`方法解析;

2. 和 `add`方法 基本逻辑是一样的，`add` 添加的是单个元素，`addAll`添加的是一个集合；

3. 区别：在检查容量及扩容的时候，参数 `(size + numNew)`是集合参数的大小，不是`size+1`；

4. 为什么执行了2次 `System.ArrayCopy(...)`?

   1.第一次，是为 ` Collection<? extends E> c`元素腾地方，把`index`后的数组元素后移，并没执行插入元素；

   2.第二次，把` Collection<? extends E> c`元素插入到第一次腾出来的位置；

5. 执行完元素插入后，增长数组长度，元素插入成功就返回 `true`;





##### 4.addAll(Collection<? extends E> c)

*方法含义：插入一个泛型子类集合*

**方法签名：**

 ` addAll(Collection<? extends E> c)`  

 方法名:`addAll`

 方法参数： `Collection<? extends E> c`    泛型子类的集合



**源码：**

```java
/**
 * Appends all of the elements in the specified collection to the end of
 * this list, in the order that they are returned by the
 * specified collection's Iterator.  The behavior of this operation is
 * undefined if the specified collection is modified while the operation
 * is in progress.  (This implies that the behavior of this call is
 * undefined if the specified collection is this list, and this
 * list is nonempty.)
 *
 * 注解：把集合元素追加到结合的后面
 * 这个操作的行为 如果在操作过程中修改了指定的集合，则为undefined；
 *
 * @param c collection containing elements to be added to this list
 * 参数：要追加的集合信息
 * @return <tt>true</tt> if this list changed as a result of the call
 * 返回值:集合发生该表则返回true
 * @throws NullPointerException if the specified collection is null
 */
public boolean addAll(Collection<? extends E> c) {
    Object[] a = c.toArray();
    int numNew = a.length;
    ensureCapacityInternal(size + numNew);  // Increments modCount
    System.arraycopy(a, 0, elementData, size, numNew);
    size += numNew;
    return numNew != 0;
}
```

**源码详细分析：**

**步骤：1.获取元素长度  --->2.检查内部容量---->3.确定容量---->4.数组扩容-->5.追加元素---->6.修改数组大小**



`addAll(Collection<? extends E> c)` 方法详细说明：



1. 源码` ensureCapacityInternal` 对应的步骤2、3、4不再重复叙述，查看`add(int index,E elemtn)`方法解析;
2. `addAll(Collection<? extends E> c)`直接使用 `System.Arraycopy(...)`最佳到集合的后面，添加完后然后数组的长度，数组发生变化，返回`true`;





#### ***删除方法***

##### 5.remove(int index)

*方法含义：插入一个泛型子类集合*

**方法签名：**

 ` addAll(Collection<? extends E> c)`  

 方法名:`addAll`

 方法参数： `Collection<? extends E> c`    泛型子类的集合















##### 6.boolean remove(Object o)

*方法含义：插入一个泛型子类集合*

**方法签名：**

 ` addAll(Collection<? extends E> c)`  

 方法名:`addAll`

 方法参数： `Collection<? extends E> c`    泛型子类的集合











#### ***修改方法***

##### 7.set(int index, E element)

*方法含义： 替换指定位置的元素*

**方法签名：**

`set(int index, E element)`

 方法名: `set`

 方法参数：`int index` 调换元素的下标位置调换元素的下标位置，  `E element`    要存储在指定位置的元素



**源码：**

```java
/**
 * Replaces the element at the specified position in this list with
 * the specified element.
 * 注解： 替换指定位置的元素
 * @param index index of the element to replace  
 * 参数：调换元素的下标位置
 * @param element element to be stored at the specified position
 * 参数：要存储在指定位置的元素
 * @return the element previously at the specified position
 * 返回值：替换元素的原元素信息
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public E set(int index, E element) {
    rangeCheck(index);

    E oldValue = elementData(index);
    elementData[index] = element;
    return oldValue;
}
```

**源码详细分析：**

**步骤：1.检查下标-->2.设置元素值**



`set(int index, E element)`  方法详细说明：

1. 首先检查下标是否越界
2. 然后设置指定下标位置的元素
3. 返回原元素的`value`







#### ***查询方法***

##### 8.get(int index)

*方法含义：获取索引位置的元素*

**方法签名：**

`get(int index)`

 方法名:  `get`

 方法参数： `int index`    元素索引下标



**源码：**

```java
/**
 * Returns the element at the specified position in this list.
 * 注解： 返回指定索引下标位置的元素
 * @param  index index of the element to return
 * 参数： 索引下标
 * @return the element at the specified position in this list
 * 返回值：索引位置的元素信息
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public E get(int index) {
    rangeCheck(index);

    return elementData(index);
}
```

**步骤：1.检查下标-->2.获取元素值**

`get(int index)`   方法详细说明：

1. 首先检查下标是否越界
2. 返回索引位置的`value`



#### ***其他方法***

##### 9.contains(Object o)

*方法含义：对象是否包含在指定的集合中

**方法签名：**

`contains(Object o)`

 方法名:`contains`

 方法参数： `Object o`  被比较是否包含的元素



**源码：**

```java
/**
 * Returns <tt>true</tt> if this list contains the specified element.
 * More formally, returns <tt>true</tt> if and only if this list contains
 * at least one element <tt>e</tt> such that
 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
 * 注解: 如果这个集合包含指定的元素，当且仅当此列表包含至少有一个这样的元素
 * @param o element whose presence in this list is to be tested
 * 参数：元素，比较其在此列表中的是否
 * @return <tt>true</tt> if this list contains the specified element
 */
public boolean contains(Object o) {
    return indexOf(o) >= 0;
}
```

`contains(Object o)`   方法详细说明：

使用`indexOf(Object o)`,返回第一个出现在当前集合中的元素的索引位置，判断指定 `object o`是否存在，所以如果索引大于0，就表示该元素包含在集合当中。



##### 10.indexOf(Object o)

*方法含义：查询索引位置*

**方法签名：**

`indexOf(Object o)`

 方法名:`indexOf`

 方法参数：`Object o`    被检查的元素

**源码：**

```java
/**
 * Returns the index of the first occurrence of the specified element
 * in this list, or -1 if this list does not contain the element.
 * More formally, returns the lowest index <tt>i</tt> such that
 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
 * or -1 if there is no such index.
 *
 * 注解：返回第一个出现在当前集合中的元素的索引位置，如果没有找到该元素则 返回 -1
 */
public int indexOf(Object o) {
    if (o == null) {
        for (int i = 0; i < size; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))
                return i;
    }
    return -1;
}
```



`indexOf(Object o)`方法详解：

使用 `if 和 for ` 循环判断的方式，检测是否包含 `objec o` 参数元素

注意：`ArrayList`的数据存放在 `Object []`,允许存放`null`元素，所以源码中如果检测元素是`null`，也会返回元素下标。如果没有该元素则返回 `-1`。





#### 3.源码中的单词



| 单词     | 含义           |
| -------- | -------------- |
| ensure   | 保证、确保     |
| Capacity | 容量           |
| explicit | 明确的、清楚地 |
| Internal | 内部的         |



#### 4.比较重要的部分



1. 位移运算

   *语雀地址：*  [ 数据结构目录 基础之位移运算](https://www.yuque.com/sourlemon/java)

2. System.copy()

   *语雀地址：*  [ 数据结构目录 基础之数组的拷贝](https://www.yuque.com/sourlemon/java)





#### 5.参阅文章

	





#### 6.文章说明



*语雀地址：*  [ 数据结构目录 解析之浅析ArrayList](https://www.yuque.com/sourlemon/java)

GitHUb源码地址： [点击查看源码](https://github.com/sour-lemon/Java_Data_Structure_Note)

