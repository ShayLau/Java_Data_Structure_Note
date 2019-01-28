### 浅析LinkedList



1. 源码解析
2. 参阅文章
3. 文章说明



#### 前言

上一篇  [浅析ArrayList]() 文章中，详细阐述了`ArrayList`的底层源码实现，但是大多步骤过于重复，所从本篇文章开始，只解析文章中比较重要的一些知识点，相似部分不再重复详解。



#### 1.源码解析



- ##### 私有静态内部类`Node   `

  - 在了解`LinkedList`之前，先了解`Node`,不夸张的说了解完`LinkedList`的 `Node` 就了解了`LinkedList`。

    

    **`Node源码：`**

    ```java
    /**
    * Node 对象有3个元素
    * item 存放元素
    * next 指向下一个Node的对象
    * prev 指向上一个Node的对象
    */
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;
    
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
    ```

    `Node` 是一个静态内部类对象，存放`LinkedList`的集合信息，有三个元素，如下：

    ```
    /**
    * Node 对象有3个元素
    * item 存放元素
    * next 指向下一个Node的对象
    * prev 指向上一个Node的对象
    */
    ```

    ##### 

- ##### 具体怎么存放数据？

  `LinkedList`和`ArrayList`使用`Object [] element`不一样，源码如下：

  ```java
  /**
   * Pointer to first node.
   * Invariant: (first == null && last == null) ||
   *            (first.prev == null && first.item != null)
   */
  transient Node<E> first;
  
  /**
   * Pointer to last node.
   * Invariant: (first == null && last == null) ||
   *            (last.next == null && last.item != null)
   */
  transient Node<E> last;
  ```

  

  `LinkedList` 是没有具体的容器，它是一个双向链表结构，真正把数据关联起来的是`first`和`last`；

  

  *为什么这么说呢？*

  ​		上面的注解说明，`first`是第一个`node`节点，`last`是最后的`node`节点。如果两者都是`null`的话，此时两者对应的`next 和prev === null`；

  

- ##### node`节点的first与last`是怎么关联到一起的？

  

  ​	默认构造方法中并没有执行任何的操作，所以通过`new LinkedList()`只是生成了一个集合的引用。

  ```java
  /**
   * Constructs an empty list.
   */
  public LinkedList() {
  }
  ```

  **链表关联**

  在第一次添加元素的时候，也就是` add(E e)`的时候关联，源码如下：

  ```java
  /**
   * Appends the specified element to the end of this list.
   * 注解：追加一个规定的元素到集合的末尾
   * <p>This method is equivalent to {@link #addLast}.
   *
   * @param e element to be appended to this list
   * 参数： 要被追加的元素
   * @return {@code true} (as specified by {@link Collection#add
   * 返回值： true
   */
  public boolean add(E e) {
      linkLast(e);
      return true;
  }
  /**
  * Links e as last element. 
  * 连接 e 到集合元素的最后
  */
  void linkLast(E e) {
       final Node<E> l = last;
       final Node<E> newNode = new Node<>(l, e, null);
       last = newNode;
       if (l == null)
       	first = newNode;
       else
       	l.next = newNode;
       size++;
       modCount++;
   }
  
  ```

  执行流程：

  在集合元素是空的情况下：

  1.`add(E e)`添加元素方法，调用类内方法 `linkLast(E e)`；

  2.生成一个新的节点元素`newNode`,集合元素此时`next==null`,就会把新增节点作为最后的`last`节点；

  3.同时，如果追加元素之前，最后一个节点是`null`，那么集合的`first`节点也等于`newNode`，反之则直接追加到`last`的后面；

  4.增加修改次数增加大小。

  因此，**当`LinkedList`集合空的时候，`first与last`都是 `null`**

  第一次，添加元素后，`first与last`的`next 和prev === null`；

  第二次添加时，`Node`的新元素 就会追加到 `last`的后面，此时 `last`的`prev`就不是`null`了，而`first`的`next`也就不是`null`了。

  

- **有index索引的处理集合元素**

  

  上一部分，讲解了`LinkedList`的数据存储实现，主要是私有静态内部类`Node`对象，来存储数据，就是就把`LinkedList`的实现讲差不多了，下面开始解析一下有index的处理`LinkedList`。

  

  以`addAll(int index, Collection<? extends E> c) `添加元素为例。

  ```java
  /**
   * Inserts all of the elements in the specified collection into this
   * list, starting at the specified position.  Shifts the element
   * currently at that position (if any) and any subsequent elements to
   * the right (increases their indices).  The new elements will appear
   * in the list in the order that they are returned by the
   * specified collection's iterator.
   * 注解：在这个集合的规定位置插入 一个集合，如果插入后边有元素则后移，新元素将按指定集合的
   * 迭代器返回的顺序出现在列表中。
   * @param index index at which to insert the first element
   *              from the specified collection
   * 参数：插入位置的索引
   * @param c collection containing elements to be added to this list
   * 参数：插入的集合
   * @return {@code true} if this list changed as a result of the call
   * 返回值：如果集合发生改变也就是说插入成功返回true
   * @throws IndexOutOfBoundsException {@inheritDoc}
   * @throws NullPointerException if the specified collection is null
   */
  public boolean addAll(int index, Collection<? extends E> c) {
      checkPositionIndex(index);
      //确定拼接元素的长度
      Object[] a = c.toArray();
      int numNew = a.length;
      if (numNew == 0)
          return false;
      //确定查询元素的前后
      Node<E> pred, succ;
      if (index == size) {  //如果插入位置刚好是集合的最后，如果空集合index==size==0
          succ = null;  //那么集合的last.next==succ==null
          pred = last;  //index位置的就成了参数集合的上一个，如果集合本就是null,那么pred=null
      } else {
          succ = node(index);  //查找指定的元素作为index插入位置的下一个元素
          pred = succ.prev;  // succ的上一个元素就是index位置的上一个元素
      }
      //for循环组装成一个链表
      for (Object o : a) {
          @SuppressWarnings("unchecked") E e = (E) o; //拆箱操作
          Node<E> newNode = new Node<>(pred, e, null);  //组装链表的新元素
          if (pred == null)  //从索引0开始添加元素时
              first = newNode;  // 链表的first就是pred
          else
              pred.next = newNode;  //如果链表不是空的，就在查询的pred后面拼接
              pred = newNode; //为了方便下一次循环的之前，准备好作为newNode的prev元素(重要步骤)
      }
     //组装的链表开始拼接
      if (succ == null) {  //index是最后一个位置时，因为链表结构中last.next==null
          last = pred;  // 因为在上面的for循环执行完 pred=newNode，所以这里的last也就=pred
      } else {
          pred.next = succ; //pred的指针引用从 pred==null  改为 pred.next==succ 
          succ.prev = pred; //succ元素也就是参数index位置的指针引用 index.prev改为 pred
      }
  	
      size += numNew; //增加size
      modCount++;  //增加 modCount 修改次数
      return true;  
  }
  ```

  大致流程：

  1.检查 参数`index`索引位置是否出现越界的情况，方法：`checkPositionIndex(index);`;

  2.确定拼接元素是否是空集合，如果没有元素，直接返回 `false`。

  3.**重要：**在插入参数 `Collection<? extends E> c`的时候，先确定这个集合的前一个元素和后一个元素，这里用了两个局部变量，分别是 `Node<E> pred, succ。`为拼接给定集合做准备。

  拼接元素需要逻辑思考，详细看上面代码分析。

  *（这一块有一个比较重要的内容，**`node(index)`**方法二分查找`index`位置的元素，这一块在下一节详细说明）*。

  4.拼接完成后，修改链表的长度和链表的修改次数。

  

- 通过链表的`Node<E> node(int index)`方法看二分查找元素

  `Node<E> node(int index)`源码：

  ```java
  /**
   * Returns the (non-null) Node at the specified element index.
   * 返回index位置的Node元素(不是空的)
   */
  Node<E> node(int index) {
      // assert isElementIndex(index);
  	//位移运算二分了整个集合长度
      if (index < (size >> 1)) {  //小于size的一半 
          Node<E> x = first;
          for (int i = 0; i < index; i++)
              x = x.next;
          return x;
      } else {    //大于size的一半 
          Node<E> x = last;
          for (int i = size - 1; i > index; i--)
              x = x.prev;
          return x;
      }
  }
  ```

  步骤：

  1.先进行数据长度的二分;

  2.然后`for`循环，条件是 `i<index 或 i>index  `，查询到了`index`的元素。

  这块需要了解位移运算，`size >> 1 等同于 size/2`  进行了集合长度的二分，然后查元素

  其他新增元素的方法大致都是处理 `first 和 last`元素，以及链表的元素拼接

  

- 链表的`remove 与 GC`

  - 链表的数据删除

    `rmove()`相关源码：

    ```java
    /**
    * Retrieves and removes the head (first element) of this list.
    * 注解：检索集中的第一个头元素删除
    * @return the head of this list
    * 返回值： 删除的头元素
    * @throws NoSuchElementException if this list is empty
    * @since 1.5
    */
    public E remove() {
        return removeFirst();
    }
    /**
    * Removes and returns the first element from this list.
    * 注解：删除这个集合中的第一个元素，并删除
    * @return the first element from this list
    * 返回值：
    * @throws NoSuchElementException if this list is empty
    */
    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)  //链表没有元素，会直接抛出异常
            throw new NoSuchElementException();
        return unlinkFirst(f);
    }
    /**
    * Unlinks non-null first node f.
    * 取消非空第一节点的链接
    */
    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;
        final E element = f.item;  //要删除的元素
        final Node<E> next = f.next; // 删除元素的下一个
        f.item = null;  //设置为null
        f.next = null; // help GC 设置为null
        first = next;  //设置为null
        if (next == null) //如果没有下一个元素，相当于删除的元素就是链表的最后一个
            last = null;  //设置为null
        else
            next.prev = null;  // 要删除的first元素，引用关联设置为null
        size--; //修改大小
        modCount++; //修改次数
        return element; //返回被删除的元素
    }
    ```

    **解析：**

    链表的`remove()`，会从头开始删除元素。不一定非得从头删除元素，你也可以使用其他删除方法，如：`remove(int index)、remove(Object o)、removeLast() `等,关于为什么从头开始删除，下文有详解。

    

    *注意*：使用`remove()`方法删除链表前，最好先判断一下数组的长度，如果获取不到元素，如上面代码 直接`throw new NoSuchElementException();`

    

    *删除方法的GC:*

    删除方法，其实就是把链表的引用关系改变，被删除的元素的引用全部修改为`null`，等待`jvm`自动回收回收机制回收。这里面用了`jvm 的 gc策略`。

    

- 链表数据的修改与获取

  ```java
  /**
   * Returns the element at the specified position in this list.
   * 注解：返回集合中索引位置的元素
   * @param index index of the element to return
   * 参数：索引
   * @return the element at the specified position in this list
   * 返回值： 返回索引位置的元素
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public E get(int index) {
      checkElementIndex(index);
      return node(index).item;
  }
  
  /**
   * Replaces the element at the specified position in this list with the
   * specified element.
   * 注解：替换指定位置的元素
   * @param index index of the element to replace
   * 参数： 替换索引位置的元素
   * @param element element to be stored at the specified position
   * 参数：要被存储到指定位置的元素
   * @return the element previously at the specified position
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  public E set(int index, E element) {
      checkElementIndex(index);
      Node<E> x = node(index);
      E oldVal = x.item;
      x.item = element;
      return oldVal;
  }
  ```

  **解析：**

  设置元素和获取元素的索引位置都会进行索引检查，都是对 `node`元素的`item`进行处理。

  

  

- `LinkedList` 作为队列的实现

  - 队列结构：**队列是一个 `FIFO( first in first out)`的先进先出的数据结构**。

    `LinkedList implements Deque 接口,`而`interface Deque<E> extends Queue<E>`。

    所以，也可以说 `LinkedList`也可以作为队列来使用。

    *部分`Deque`队列的实现源码：*

    ```java
    // Queue operations.
    
    /**
     * Retrieves, but does not remove, the head (first element) of this list.
     * 注解： 检索但不删除此列表的头(第一个元素)
     * @return the head of this list, or {@code null} if this list is empty
     * 返回值： 返回头元素，如果集合是空的则返回null
     * @since 1.5
     */
    public E peek() { 
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }
    
    /**
     * Retrieves, but does not remove, the head (first element) of this list.
     * 注解： 检索但不删除此列表的头(第一个元素)
     * @return the head of this list
     * 返回值: 返回头元素，但是如果集合是空的，则会抛出异常，详细看getFirst()方法的实现
     * @throws NoSuchElementException if this list is empty
     * @since 1.5
     */
    public E element() {
        return getFirst();
    }
    
    /**
     * Retrieves and removes the head (first element) of this list.
     * 注解：检索并删除这个元素
     * @return the head of this list, or {@code null} if this list is empty
     * 返回值： 返回头元素并删除，如果集合是空的则返回null
     * @since 1.5
     */
    public E poll() {
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }
    
    /**
     * Retrieves and removes the head (first element) of this list.
     *
     * @return the head of this list
     * 返回值: 返回头元素并删除，但是如果集合是空的，则会抛出异常，详细看removeFirst()方法的实现
     * @throws NoSuchElementException if this list is empty
     * @since 1.5
     */
    public E remove() {
        return removeFirst();
    }
    /**
    * Adds the specified element as the tail (last element) of this list.
    * 注解： 在集合的尾部添加元素
    * @param e the element to add
    * 参数: 要插入的元素
    * @return {@code true} (as specified by {@link Queue#offer})
    * @since 1.5
    */
    public boolean offer(E e) {
        return add(e);
    }
    
    // Deque operations
    /**
    * Inserts the specified element at the front of this list.
    * 注解：在集合的最前面添加元素
    * @param e the element to insert
    * 参数: 要插入的元素
    * @return {@code true} (as specified by {@link Deque#offerFirst})
    * @since 1.6
    */
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }
    
    /**
    * Inserts the specified element at the end of this list.
    * 注解：在集合的最后边添加元素
    * @param e the element to insert
    * 参数: 要插入的元素
    * @return {@code true} (as specified by {@link Deque#offerLast})
    * @since 1.6
    */
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }
    /**
    * Pushes an element onto the stack represented by this list.  In other
    * words, inserts the element at the front of this list.
    * 注解：将一个元素推入由这个列表表示的堆栈中。换句话说，将元素插入到这个列表的前面
    * <p>This method is equivalent to {@link #addFirst}.
    * 注解： 这个方法等价于addFirst()
    * @param e the element to push
    * 参数：被push的元素
    * @since 1.6
    */
    public void push(E e) {
        addFirst(e);
    }
    /**
    * Pops an element from the stack represented by this list.  In other
    * words, removes and returns the first element of this list.
    * 注解：从这个列表表示的堆栈中弹出一个元素。换句话说，删除并返回列表的第一个元素。
    * <p>This method is equivalent to {@link #removeFirst()}.
    * 注解： 这个方法等同于 removeFirst()
    * @return the element at the front of this list (which is the top
    *         of the stack represented by this list)
    * 返回值: 这个列表前面的元素(它是由这个列表表示的堆栈的顶部)
    * @throws NoSuchElementException if this list is empty
    * @since 1.6
    */
    public E pop() {
        return removeFirst();
    }
    ```

    以上方法，实现了队列的`FIFO`的数据结构方式，所以把`LinkedList`也可作为队列使用，但注意 `LinkedList`是线程不安全的，没有做同步处理，需要注意使用场景。

    

- 链表的克隆和清空

  - *源码如下：*

    ```java
    /**
     * Removes all of the elements from this list.
     * The list will be empty after this call returns.
     * 注解：集合将在方法执行完后变成空的
     */
    public void clear() {
        // Clearing all of the links between nodes is "unnecessary", but:
        // - helps a generational GC if the discarded nodes inhabit
        //   more than one generation
        // - is sure to free memory even if there is a reachable Iterator
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
        modCount++;
    }
    /**
    * Returns a shallow copy of this {@code LinkedList}. (The elements
    * themselves are not cloned.)
    * 注解： 返回这个集合的一个浅拷贝。(没有克隆元素本身)
    * @return a shallow copy of this {@code LinkedList} instance
    * 返回值： 浅拷贝的实例
    */
    public Object clone() {
        LinkedList<E> clone = superClone();
    
        // Put clone into "virgin" state
        clone.first = clone.last = null;
        clone.size = 0;
        clone.modCount = 0;
    
        // Initialize clone with our elements
        for (Node<E> x = first; x != null; x = x.next)
            clone.add(x.item);
    
        return clone;
    }
    
    ```

    **解析：**

  `clear()`方法会把整个集合中的`node`引用全部变成空，同时把` first = last = null;size= 0;`,但是不会把`modCount`修改次数修改。

  

  `clone()`克隆方法只是做一个浅拷贝，不会拷贝 *被拷贝对象本身*。

- 遍历的实现



#### 2.参阅文章

​	本文章参阅 [LinkedList分析](https://blog.csdn.net/zxt0601/article/details/77341098)



#### 3.文章说明



*语雀地址：*  [ 数据结构目录 解析之浅析LinkedList](https://www.yuque.com/sourlemon/java)

GitHUb源码地址： [点击查看源码](https://github.com/sour-lemon/Java_Data_Structure_Note)

