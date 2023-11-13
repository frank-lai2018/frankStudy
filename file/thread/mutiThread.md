# muti thread

## 進程與執行續(線程)

### 進程
- 程式由指令和資料組成，但這些指令要運行，資料要讀寫，就必須將指令載入到 CPU，資料載入至記憶體。 在指令運行過程中還需要用到磁碟、網路等設備。 進程就是用來載入指令、管理記憶體、管理 IO 的
- 當一個程式被運行，從磁碟加載這個程式的程式碼到內存，這時就開啟了一個進程。
- 進程就可以視為程式的一個實例。 大部分程式可以同時執行多個實例進程（例如記事本、畫圖、瀏覽器
等等），也有的程式只能啟動一個實例進程（例如網易雲音樂、360 安全衛士等）

### 執行緒
- 一個行程之內可以分為一到多個執行緒。
- 一個執行緒就是一個指令流，將指令流中的一條指令以一定的順序交給 CPU 執行
- Java 中，執行緒作為最小調度單位，進程作為資源分配的最小單位。 在 windows 中進程是不活動的，只是作為線程的容器

## 併行(parallel)與併發(concurrent)

### 併發(concurrent)

- 就是一個CPU輪流執行多個執行緒


- 單核心 cpu 下，執行緒實際上還是 串列執行 的。 作業系統中有一個元件叫做任務調度器，將 cpu 的時間片（windows下時間片最小約為 15 毫秒）分給不同的程式使用，只是由於 cpu 在線程間（時間片很短）的切換非常快，人類感覺是 同時運行的 。 總結為一句話是： 微觀串行，宏觀並行，

- 一般會將這種 執行緒輪流使用 CPU 的做法稱為並發， concurrent

![1](imgs/1.png)

### 併行(parallel)

- 就是多個CPU同時執行多個執行緒

- 多核心 cpu下，每個 核心（core） 都可以調度運行線程，這時候線程可以是並行的。

![1](imgs/2.png)



- 引用 Rob Pike 的一段描述：
  - 並發（concurrent）是同一時間應對（dealing with）多件事情的能力
  - 並行（parallel）是同一時間動手做（doing）多件事情的能力

## 創建線程

### 1.直接使用Thread

```java
// 建立線程對象
Thread t = new Thread() {
  public void run() {
  // 要執行的任務
  }
};
// 啟動執行緒
t.start();
```

例如:
```java
// 建構方法的參數是給執行緒指定名字，推薦
Thread t1 = new Thread("t1") {
  @Override
  // run 方法內實作了要執行的任務
  public void run() {
  log.debug("hello");
  }
};
t1.start();
```

輸出
```
19:19:00 [t1] c.ThreadStarter - hello
```


### 2.使用Runnable配合Thread

- 把【線程】和【任務】（要執行的程式碼）分開
  - Thread 代表線程
  - Runnable 可運行的任務（執行緒要執行的程式碼）


```java
Runnable runnable = new Runnable() {
  public void run(){
  // 要執行的任務
  }
};
// 建立線程對象
Thread t = new Thread( runnable );
// 啟動執行緒
t.start();
```

```java
// 建立任務對象
Runnable task2 = new Runnable() {
  @Override
  public void run() {
  log.debug("hello");
  }
};
// 參數1 是任務物件; 參數2 是執行緒名字，推薦
Thread t2 = new Thread(task2, "t2");
t2.start();
```

輸出:

```
19:19:00 [t2] c.ThreadStarter - hello
```

java8 以後使用lambda

```java
// 建立任務對象
Runnable task2 = () -> log.debug("hello");
// 參數1 是任務物件; 參數2 是執行緒名字，推薦
Thread t2 = new Thread(task2, "t2");
t2.start();
```

### 3.FutureTask 配和 Thread

```java
// 建立任務對象
FutureTask<Integer> task3 = new FutureTask<>(() -> {
  log.debug("hello");
  return 100;
});
// 參數1 是任務物件; 參數2 是執行緒名字，推薦
new Thread(task3, "t3").start();
// 主執行緒阻塞，同步等待 task 執行完畢的結果
Integer result = task3.get();
log.debug("結果是:{}", result);
```

輸出:

```
19:22:27 [t3] c.ThreadStarter - hello
19:22:27 [main] c.ThreadStarter - 结果是:100

```

## 




