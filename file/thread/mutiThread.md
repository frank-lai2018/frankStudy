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



## Thread原理

### 棧與棧禎

- Java Virtual Machine Stacks （Java 虛擬機器堆疊）

- 我們都知道 JVM 中由堆疊、堆疊、方法區所組成，其中棧記憶體是給誰用的呢？ 其實就是線程，每個線程啟動後，虛擬
機器就會為其分配一塊棧記憶體。
- 每個棧由多個棧幀（Frame）組成，對應每次方法呼叫時所佔用的記憶體
- 每個執行緒只能有一個活動棧幀，對應目前正在執行的那個方法

### 線程上下文切換(Thread Context Switch)


- 因為以下一些原因導致 cpu 不再執行目前的線程，轉而執行另一個線程的程式碼
  - 線程的 cpu 時間片用完
  - 垃圾回收
  - 有更高優先權的執行緒需要運行
  - 線程自己呼叫了 sleep、yield、wait、join、park、synchronized、lock 等方法
- 當 Context Switch 發生時，需要由作業系統保存目前執行緒的狀態，並恢復另一個執行緒的狀態，Java 中對應的概念
是程式計數器（Program Counter Register），它的功能是記住下一條 jvm 指令的執行位址，是執行緒私有的
  - 狀態包括程式計數器、虛擬機器棧中每個棧幀的信息，如局部變數、操作數棧、返回地址等
  - Context Switch 頻繁發生會影響效能

## 常見的方法

|方法名|static|功能說明|注意事項|
|--|--|--|--|
|start()||啟動一個新的Thread，在新的Thread運行run方法中的代碼|start 方法只是讓線程進入就緒，裡面程式碼不一定立刻運行（CPU 的時間片還沒分給它）。 每個線程對象的start方法只能呼叫一次，如果呼叫了多次會出現IllegalThreadStateException|
|run()||新執行緒啟動後會呼叫的方法|如果在建構 Thread 物件時傳遞了Runnable 參數，則執行緒啟動後會呼叫 Runnable 中的 run 方法，否則默認不執行任何操作。但可以創建 Thread 的子類對象，來覆寫預設行為|
|join()||等待調用該方法的執行緒運行結束||
|join(long n)||等待執行緒運行結束,最多等待 n毫秒||
|getId()||獲取執行緒長整型的id|id唯一|
|getName()||獲取執行緒名||
|setName(String)||修改執行緒名||
|getPriority()||獲取執行緒優先級||
|setPriority(int)||修改執行緒優先級|java中規定執行緒優先權是1~10 的整數，較大的優先權能提高該執行緒被 CPU 調度的機率|
|getState()||獲取執行緒狀態|Java 中執行緒狀態以 6 個 enum 表示，分別為：NEW, RUNNABLE, BLOCKED, WAITING,TIMED_WAITING, TERMINATED|
|isInterrupted()||判斷是否被打斷|不會清除打斷標記|
|isAlive()||執行緒是否存活(還沒有運行完畢)||
|interrupt()||打斷執行緒|如果被打斷線程正在 sleep，wait，join 會導致被打斷的執行緒拋出 InterruptedException，並清除打斷標記；如果打斷的正在運行的線程，則會設定 打斷標記；park 的線程被打斷，也會設定打斷標記|
|interrupted()|static|判斷當前線程是否被打斷|會清除打斷標記|
|currentThread()|static|獲取當前正在執行的執行緒||
|sleep(long n)|static|讓目前執行的線程休眠n毫秒，休眠時讓出 cpu
的時間片給其它執行緒||
|yield()|static|提示線程調度器，讓出當前線程對CPU的使用權|主要為了測試和調試|

## start與run

### 調用run

```java
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {

            @Override
            public void run() {
                log.debug(Thread.currentThread().getName());
                FileReader.read(Constants.MP4_FULL_PATH);
            }
        };
        t1.run();
        log.debug("do other things ...");
    }
```

```
19:39:14 [main] c.TestStart - main
19:39:14 [main] c.FileReader - read [1.mp4] start ...
19:39:18 [main] c.FileReader - read [1.mp4] end ... cost: 4227 ms
19:39:18 [main] c.TestStart - do other things ...
```
程式仍在 main 執行緒運行， FileReader.read() 方法呼叫還是同步的

### 調用start

將上述代碼的t1.run()改為

```java
t1.start();
```

```
19:41:30 [main] c.TestStart - do other things ...
19:41:30 [t1] c.TestStart - t1
19:41:30 [t1] c.FileReader - read [1.mp4] start ...
19:41:35 [t1] c.FileReader - read [1.mp4] end ... cost: 4542 ms
```

程式在 t1 執行緒運行， FileReader.read() 方法呼叫是異步的

- 小結
  - 直接呼叫 run 是在主線程中執行了 run，沒有啟動新的線程
  - 使用 start 是啟動新的線程，透過新的線程間接執行 run 中的程式碼

## sleep 與 yield

### sleep

- 1. 呼叫 sleep 會讓目前執行緒從 Running 進入 Timed Waiting 狀態（阻塞）
- 2. 其它線程可以使用 interrupt 方法打斷正在睡眠的線程，這時 sleep 方法會拋出 InterruptedException
- 3. 睡眠結束後的執行緒未必會立刻得到執行
- 4. 建議用 TimeUnit 的 sleep 取代 Thread 的 sleep 來獲得更好的可讀性

### yield

- 1. 呼叫 yield 會讓目前執行緒從 Running 進入 Runnable 就緒狀態，然後調度執行其它線程
- 2. 具體的實作依賴作業系統的任務調度器

### 執行緒優先級

- 線程優先權會提示（hint）調度器優先調度該線程，但它只是一個提示，調度器可以忽略它
- 如果 cpu 比較忙，那麼優先順序高的執行緒會獲得更多的時間片，但 cpu 閒時，優先權幾乎沒作用

```java
        Runnable task1 = () -> {
            int count = 0;
            for (;;) {
                System.out.println("---->1 " + count++);
            }
        };
        Runnable task2 = () -> {
            int count = 0;
            for (;;) {
                // Thread.yield();
                System.out.println(" ---->2 " + count++);
            }
        };
        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");
        // t1.setPriority(Thread.MIN_PRIORITY);
        // t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();
```


## join方法

### 為什麼需要 join

- 下面的程式碼執行，列印 r 是什麼？

```java
static int r = 0;

     public static void main(String[] args) throws InterruptedException {
         test1();
     }

     private static void test1() throws InterruptedException {
         log.debug("開始");
         Thread t1 = new Thread(() -> {
             log.debug("開始");
             sleep(1);
             log.debug("結束");
             r = 10;
         });
         t1.start();
         log.debug("結果為:{}", r);
         log.debug("結束");
     }
```

- 分析
  - 因為主執行緒和執行緒 t1 是並行執行的，t1 執行緒需要 1 秒後才能算出 r=10
  - 而主線程一開始就要印出 r 的結果，所以只能印出 r=0
- 解決方法
  - 用 sleep 行不行？ 為什麼？
  - 用 join，加在 t1.start() 之後即可


![035](imgs/3.png)
- 結論
  - 需要等待結果返回，才能繼續運行就是同步
  - 不需要等待結果返回，就能繼續運行就是非同步

下面代碼cost大約多少秒?

```java
    static int r1 = 0;
    static int r2 = 0;

    public static void main(String[] args) throws InterruptedException {
        test2();
    }

    private static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            sleep(1);
            r1 = 10;
        });
        Thread t2 = new Thread(() -> {
            sleep(2);
            r2 = 20;
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }
```

- 分析如下
  - 第一個 join：等待 t1 時, t2 並沒有停止, 而在運行
  - 第二個 join：1s 後, 執行到此, t2 也運行了 1s, 因此也只需再等待 1s
- 如果顛倒兩個 join 呢？
- 最終都是輸出

```
20:45:43.239 [main] c.TestJoin - r1: 10 r2: 20 cost: 2005
北
```

![035](imgs/4.png)

### 有時效的join

#### 有等夠時間

```java
    static int r1 = 0;
    static int r2 = 0;

    public static void main(String[] args) throws InterruptedException {
        test3();
    }

    public static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            sleep(1);
            r1 = 10;
        });
        long start = System.currentTimeMillis();
        t1.start();
        // 執行緒執行結束會導致 join 結束
        t1.join(1500);
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }
```

輸出

```
20:48:01.320 [main] c.TestJoin - r1: 10 r2: 0 cost: 1010
```

#### 沒等夠時間

```java
    static int r1 = 0;
    static int r2 = 0;

    public static void main(String[] args) throws InterruptedException {
        test3();
    }

    public static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            sleep(2);
            r1 = 10;
        });
        long start = System.currentTimeMillis();
        t1.start();
        // 執行緒執行結束會導致 join 結束
        t1.join(1500);
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }
```

輸出

```
20:52:15.623 [main] c.TestJoin - r1: 0 r2: 0 cost: 1502
```

## interrupt 方法

### 打斷 sleep，wait，join 的線程

- 這幾個方法都會讓執行緒進入阻塞狀態
- 打斷 sleep 的線程, 會清空打斷狀態，以 sleep 為例

```java
private static void test1() throws InterruptedException {
         Thread t1 = new Thread(() -> {
             sleep(1);
         }, "t1");
         t1.start();
         sleep(0.5);
         t1.interrupt();
         log.debug(" 打斷狀態: {}", t1.isInterrupted());
     }
```

輸出

```
java.lang.InterruptedException: sleep interrupted
at java.lang.Thread.sleep(Native Method)
at java.lang.Thread.sleep(Thread.java:340)
at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
at cn.itcast.n2.util.Sleeper.sleep(Sleeper.java:8)
at cn.itcast.n4.TestInterrupt.lambda$test1$3(TestInterrupt.java:59)
at java.lang.Thread.run(Thread.java:745)
21:18:10.374 [main] c.TestInterrupt - 打斷狀態: false

```

### 打斷正常運行的線程
- 打斷正常運作的執行緒, 不會清空打斷狀態

```java
    private static void test2() throws InterruptedException {
        Thread t2 = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                boolean interrupted = current.isInterrupted();
                if (interrupted) {
                    log.debug(" 打斷狀態: {}", interrupted);
                    break;
                }
            }
        }, "t2");
        t2.start();
        sleep(0.5);
        t2.interrupt();
    }
```

輸出

```
20:57:37.964 [t2] c.TestInterrupt - 打斷狀態: true
```

## 模式:兩階段中止

## 主執行緒與守護執行緒

- 預設情況下，Java 程序需要等待所有執行緒都運行結束，才會結束。 有一個特殊的執行緒叫做守護執行緒，只要其它非守護執行緒運行結束了，即使守護執行緒的程式碼沒有執行完，也會強制結束。

```java
log.debug("開始運作...");
Thread t1 = new Thread(() -> {
     log.debug("開始運作...");
     sleep(2);
     log.debug("運行結束...");
}, "daemon");
// 設定該線程為守護線程
t1.setDaemon(true);
t1.start();
sleep(1);
log.debug("運行結束...");
```

輸出
```
08:26:38.123 [main] c.TestDaemon - 開始運作...
08:26:38.213 [daemon] c.TestDaemon - 開始運行...
08:26:39.215 [main] c.TestDaemon - 運行結束...
```

>注意
>- 垃圾回收器線程就是一種守護線程
>- Tomcat 中的 Acceptor 和 Poller 線程都是守護線程，所以 Tomcat 接收到 shutdown 指令後，不會等待它們處理完當前請求

## 5種執行緒狀態(以操作系統層面來描述)


![5](imgs/5.png)

- 【初始狀態】僅是在語言層面創建了線程對象，尚未與作業系統線程關聯
- 【可運行狀態】（就緒狀態）指該執行緒已經被建立（與作業系統執行緒關聯），可以由 CPU 調度執行
- 【運作狀態】指取得了 CPU 時間片運作中的狀態，當 CPU 時間片用完，會從【運行狀態】轉換至【可運行狀態】，會導致執行緒的上下文切換
- 【阻塞狀態】
  - 如果呼叫了阻塞 API，如 BIO 讀寫文件，這時該執行緒實際上不會用到 CPU，會導致執行緒上下文切換，進入【阻塞狀態】
  - 等 BIO 操作完畢，會由作業系統喚醒阻塞的線程，轉換至【可運行狀態】
  - 與【可運行狀態】的差別是，對【阻塞狀態】的執行緒來說只要它們一直不喚醒，調度器就一直不會考慮調度它們
- 【終止狀態】表示執行緒已經執行完畢，生命週期已經結束，不會再轉換為其它狀態

## 6種執行緒狀態(以JAVA API層面來描述)

![6](imgs/6.png)

- NEW 線程剛被創建，但還沒有呼叫 start() 方法
- RUNNABLE 當呼叫了 start() 方法之後，注意，Java API 層面的 RUNNABLE 狀態涵蓋了 作業系統 層面的【可運行狀態】、【運行狀態】和【阻塞狀態】（由於 BIO 導致的執行緒阻塞，在 Java 裡無法區分，仍然認為是可運行）
- BLOCKED ， WAITING ， TIMED_WAITING 都是 Java API 層面對【阻塞狀態】的細分，後面會在狀態轉換一節詳述
- TERMINATED 當執行緒程式碼運行結束


# 共享模型之管程

## 1.共享帶來問題

- 兩個執行緒對初始值為 0 的靜態變數一個做自增，一個做自減，各做 5000 次，結果是 0 嗎？

```java
static int counter = 0;
public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread(() -> {
        for (int i = 0; i < 5000; i++) {
            counter++;
        }
    }, "t1");
    Thread t2 = new Thread(() -> {
        for (int i = 0; i < 5000; i++) {
            counter--;
        }
    }, "t2");
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    log.debug("{}",counter);
}
```

- 問題分析
  - 以上的結果可能是正數、負數、零。 為什麼呢？ 因為 Java 中對靜態變數的自增，自減並不是原子操作，要徹底理解，必須從字節碼來進行分析，例如 i++ 而言（i 為靜態變數），實際上會產生如下的 JVM 字節碼指令：
  
```java
getstatic i // 取得靜態變數i的值
iconst_1 // 準備常數1
iadd // 自增
putstatic i // 將修改後的值存入靜態變數i
```

- 而對應 i-- 也是類似：

```java
getstatic i // 取得靜態變數i的值
iconst_1 // 準備常數1
isub // 自減
putstatic i // 將修改後的值存入靜態變數i
```

- 而 Java 的記憶體模型如下，完成靜態變數的自增，自減則需要在主記憶體和工作記憶體中進行資料交換：

![7](imgs/7.png)

- 如果是單執行緒以上 8 行程式碼是順序執行（不會交錯）沒有問題：

![8](imgs/8.png)

- 但多執行緒下這 8 行程式碼可能交錯運行：
- 出現負數的情況：

![9](imgs/9.png)

- 出現正數的情況：

![10](imgs/10.png)

### 1.臨界區 Critical Section
- 一個程式運行多個執行緒本身是沒有問題的
- 問題出在多個執行緒存取共享資源
  - 多個執行緒讀取共享資源其實也沒問題
  - 在多個執行緒對共享資源讀寫操作時發生指令交錯，就會出現問題
- 一段程式碼區塊內如果存在共享資源的多執行緒讀寫操作，稱這段程式碼區塊為臨界區

- 例如，下面程式碼中的臨界區

```java
static int counter = 0;
static void increment() 
// 臨界區
{ 
 counter++;
}
static void decrement() 
// 臨界區
{ 
 counter--;
}

```

### 2.競態條件 Race Condition
- 個執行緒在臨界區內執行，由於程式碼的執行序列不同而導致結果無法預測，稱為發生了競態條件


## 2. synchronized 解決方案

* 應用之互斥

為了避免臨界區的競態條件發生，有許多手段可以達到目的。
  - 阻塞式的解決方案：synchronized，Lock
  - 非阻塞式的解決方案：原子變數
 
本次課使用阻塞式的解決方案：synchronized，來解決上述問題，即俗稱的【對象鎖】，它採用互斥的方式讓同一時刻至多只有一個線程能持有【物件鎖】，其它線程再想獲取這個【物件鎖】時就會阻塞住。 這樣就能保證擁有鎖的線程可以安全的執行臨界區內的程式碼，不用擔心線程上下文切換

>注意
>>雖然 java 中互斥和同步都可以採用 synchronized 關鍵字來完成，但它們還是有區別的：
  >>- 互斥是保證臨界區的競態條件發生，同一時刻只能有一個執行緒執行臨界區程式碼
  >>- 同步是由於執行緒執行的先後、順序不同、需要一個執行緒等待其它執行緒運行到某個點


### synchronized語法

```java
synchronized(物件) // 執行緒1， 執行緒2(blocked)
{
  臨界區
}

```

解决

```java
	static int counter = 0;
	static final Object room = new Object();

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 5000; i++) {
				synchronized (room) {
					counter++;
				}
			}
		}, "t1");
		Thread t2 = new Thread(() -> {
			for (int i = 0; i < 5000; i++) {
				synchronized (room) {
					counter--;
				}
			}
		}, "t2");
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		log.debug("{}", counter);
	}
```

![11](imgs/11.png)


- ***synchronized(物件)*** 中的物件，可以想像為一個房間（room），有唯一入口（門）房間只能一次進入一人進行計算，線程 t1，t2 想像成兩個人
- 當線程 t1 執行到 ***synchronized(room)*** 時就好比 t1 進入了這個房間，並鎖住了門拿走了鑰匙，在門內執行***count++*** 程式碼
- 這時候如果 t2 也運行到了 ***synchronized(room)*** 時，它發現門被鎖住了，只能在門外等待，發生了上下文切換，阻塞住了
- 這中間即使 t1 的 cpu 時間片不幸用完，被踢出了門外（不要錯誤理解為鎖住了對象就能一直執行下去哦），
這時門還是鎖住的，t1 仍拿著鑰匙，t2 線程還在阻塞狀態進不來，只有下次輪到 t1 自己再次獲得時間片時才能開門進入
- 當 t1 執行完 ***synchronized{}*** 區塊內的程式碼，這時候才會從 obj 房間出來並解開門上的鎖，喚醒 t2 執行緒把鑰匙給他。 t2 執行緒這時才可以進入 obj 房間，鎖住了門拿上鑰匙，執行它的 ***count--*** 程式碼

![12](imgs/12.png)

### 思考
- synchronized 實際上是用物件鎖定保證了臨界區內程式碼的原子性，臨界區內的程式碼對外是不可分割的，不會被執行緒切換所打斷。

為了加深理解，請思考下面的問題

- 如果把 synchronized(obj) 放在 for 迴圈的外面，如何理解？ -- 原子性
- 如果 t1 synchronized(obj1) 而 t2 synchronized(obj2) 會如何運作？ -- 鎖物件
- 如果 t1 synchronized(obj) 而 t2 沒有加會怎麼樣？ 如何理解？ -- 鎖物件


### 物件導向改進

把需要保護的共享變數放入一個類

```java
class Room {
	int value = 0;

	public void increment() {
		synchronized (this) {
			value++;
		}
	}

	public void decrement() {
		synchronized (this) {
			value--;
		}
	}

	public int get() {
		synchronized (this) {
			return value;
		}
	}
}

@Slf4j
public class Test1 {

	public static void main(String[] args) throws InterruptedException {
		Room room = new Room();
		Thread t1 = new Thread(() -> {
			for (int j = 0; j < 5000; j++) {
				room.increment();
			}
		}, "t1");
		Thread t2 = new Thread(() -> {
			for (int j = 0; j < 5000; j++) {
				room.decrement();
			}
		}, "t2");
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		log.debug("count: {}", room.get());
	}
}

```

### 物件導向改進

把需要保護的共享變數放入一個類

```java
class Test {
	public synchronized void test() {

	}
}

//等價於
class Test {
	public void test() {
		synchronized (this) {

		}
	}
}

```


```java
class Test {
	public synchronized static void test() {
	}
}

//等價於
class Test {
	public static void test() {
		synchronized (Test.class) {

		}
	}
}

```


### 不加 synchronized 的方法
- 不加 synchronzied 的方法就好比不遵守規則的人，不去老實排隊（好比翻窗戶進去的）

### 所謂的“線程八鎖”

 其實就是考察 synchronized 鎖住的是哪個對象

- 情况1：12 或 21

```java
@Slf4j(topic = "c.Number")
class Number {
	public synchronized void a() {
		log.debug("1");
	}

	public synchronized void b() {
		log.debug("2");
	}

}

public static void main(String[] args) {
 Number n1 = new Number();
 new Thread(()->{ n1.a(); }).start();
 new Thread(()->{ n1.b(); }).start();
}
```

- 情况2：1s后12，或 2 1s后 1

```java
@Slf4j(topic = "c.Number")
class Number {
	public synchronized void a() {
		sleep(1);
		log.debug("1");
	}

	public synchronized void b() {
		log.debug("2");
	}

}

public static void main(String[] args) {
 Number n1 = new Number();
 new Thread(()->{ n1.a(); }).start();
 new Thread(()->{ n1.b(); }).start();
}
```

- 情况3：3 1s 12 或 23 1s 1 或 32 1s 1

```java
@Slf4j(topic = "c.Number")
class Number {
	public synchronized void a() {
		sleep(1);
		log.debug("1");
	}

	public synchronized void b() {
		log.debug("2");
	}

	public void c() {
		log.debug("3");
	}

	}

	public static void main(String[] args) {
	 Number n1 = new Number();
	 new Thread(()->{ n1.a(); }).start();
	 new Thread(()->{ n1.b(); }).start();
	 new Thread(()->{ n1.c(); }).start();
	}
```
- 情况4：2 1s 后 1

```java
@Slf4j(topic = "c.Number")
class Number {
	public synchronized void a() {
		sleep(1);
		log.debug("1");
	}

	public synchronized void b() {
		log.debug("2");
	}

}

public static void main(String[] args) {
 Number n1 = new Number();
 Number n2 = new Number();
 new Thread(()->{ n1.a(); }).start();
 new Thread(()->{ n2.b(); }).start();
}

```

- 情况5：2 1s 后 1

```java
@Slf4j(topic = "c.Number")
class Number {
	public static synchronized void a() {
		sleep(1);
		log.debug("1");
	}

	public synchronized void b() {
		log.debug("2");
	}

}

public static void main(String[] args) {
 Number n1 = new Number();
 new Thread(()->{ n1.a(); }).start();
 new Thread(()->{ n1.b(); }).start();
}

```

- 情况6：1s 后12， 或 2 1s后 1

```java
@Slf4j(topic = "c.Number")
class Number {
	public static synchronized void a() {
		sleep(1);
		log.debug("1");
	}

	public static synchronized void b() {
		log.debug("2");
	}

}

public static void main(String[] args) {
 Number n1 = new Number();
 new Thread(()->{ n1.a(); }).start();
 new Thread(()->{ n1.b(); }).start();
}

```
- 情况7：2 1s 后 1

```java
@Slf4j(topic = "c.Number")
class Number {
	public static synchronized void a() {
		sleep(1);
		log.debug("1");
	}

	public synchronized void b() {
		log.debug("2");
	}

}

public static void main(String[] args) {
 Number n1 = new Number();
 Number n2 = new Number();
 new Thread(()->{ n1.a(); }).start();
 new Thread(()->{ n2.b(); }).start();
}
```

- 情况8：1s 后12， 或 2 1s后 1

```java
@Slf4j(topic = "c.Number")
class Number {
	public static synchronized void a() {
		sleep(1);
		log.debug("1");
	}

	public static synchronized void b() {
		log.debug("2");
	}

	}

	public static void main(String[] args) {
	 Number n1 = new Number();
	 Number n2 = new Number();
	 new Thread(()->{ n1.a(); }).start();
	 new Thread(()->{ n2.b(); }).start();
	}

```
# Monitor概念

- Java 物件頭
- 以 32 位元虛擬機器為例


普通物件
![13](imgs/13.png)

陣列物件
![14](imgs/14.png)

其中Mark Word結構為
![15](imgs/15.png)

64 位元虛擬機器 Mark Word
![16](imgs/16.png)





