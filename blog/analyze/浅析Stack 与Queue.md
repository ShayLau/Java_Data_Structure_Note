### 浅析Stack 与 Queue



1. 源码解析
2. 参阅文章
3. 文章说明



#### 前言

详细阐述了`Stack 与 Queue`的源码实现



#### 1.源码解析

​	

- `FIFO( first in first out)`的先进先出的数据结构

  `Queue`队列接口的定义：

  ```java
  public interface Queue<E> extends Collection<E> {
      /**
       * Inserts the specified element into this queue if it is possible to do so
       * immediately without violating capacity restrictions, returning
       * {@code true} upon success and throwing an {@code IllegalStateException}
       * if no space is currently available.
       *
       * @param e the element to add
       * @return {@code true} (as specified by {@link Collection#add})
       * @throws IllegalStateException if the element cannot be added at this
       *         time due to capacity restrictions
       * @throws ClassCastException if the class of the specified element
       *         prevents it from being added to this queue
       * @throws NullPointerException if the specified element is null and
       *         this queue does not permit null elements
       * @throws IllegalArgumentException if some property of this element
       *         prevents it from being added to this queue
       */
      boolean add(E e);
  
      /**
       * Inserts the specified element into this queue if it is possible to do
       * so immediately without violating capacity restrictions.
       * When using a capacity-restricted queue, this method is generally
       * preferable to {@link #add}, which can fail to insert an element only
       * by throwing an exception.
       *
       * @param e the element to add
       * @return {@code true} if the element was added to this queue, else
       *         {@code false}
       * @throws ClassCastException if the class of the specified element
       *         prevents it from being added to this queue
       * @throws NullPointerException if the specified element is null and
       *         this queue does not permit null elements
       * @throws IllegalArgumentException if some property of this element
       *         prevents it from being added to this queue
       */
      boolean offer(E e);
  
      /**
       * Retrieves and removes the head of this queue.  This method differs
       * from {@link #poll poll} only in that it throws an exception if this
       * queue is empty.
       *
       * @return the head of this queue
       * @throws NoSuchElementException if this queue is empty
       */
      E remove();
  
      /**
       * Retrieves and removes the head of this queue,
       * or returns {@code null} if this queue is empty.
       *
       * @return the head of this queue, or {@code null} if this queue is empty
       */
      E poll();
  
      /**
       * Retrieves, but does not remove, the head of this queue.  This method
       * differs from {@link #peek peek} only in that it throws an exception
       * if this queue is empty.
       *
       * @return the head of this queue
       * @throws NoSuchElementException if this queue is empty
       */
      E element();
  
      /**
       * Retrieves, but does not remove, the head of this queue,
       * or returns {@code null} if this queue is empty.
       *
       * @return the head of this queue, or {@code null} if this queue is empty
       */
      E peek();
  }
  ```

  

  上篇文章 ***浅析LinkedList***  文章中`queue`实现的说明：

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
  * 注解： 
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

  

  

  

#### 2.参阅文章





#### 3.文章说明



*语雀地址：*  [ 数据结构目录 解析之浅谈Stack 与 Queue](https://www.yuque.com/sourlemon/java)

GitHUb源码地址： [点击查看源码](https://github.com/sour-lemon/Java_Data_Structure_Note)

