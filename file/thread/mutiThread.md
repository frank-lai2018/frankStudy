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
// 建立線程物件
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
// 建立線程物件
Thread t = new Thread( runnable );
// 啟動執行緒
t.start();
```

```java
// 建立任務物件
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
// 建立任務物件
Runnable task2 = () -> log.debug("hello");
// 參數1 是任務物件; 參數2 是執行緒名字，推薦
Thread t2 = new Thread(task2, "t2");
t2.start();
```

### 3.FutureTask 配和 Thread

```java
// 建立任務物件
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
|start()||啟動一個新的Thread，在新的Thread運行run方法中的代碼|start 方法只是讓線程進入就緒，裡面程式碼不一定立刻運行（CPU 的時間片還沒分給它）。 每個線程物件的start方法只能呼叫一次，如果呼叫了多次會出現IllegalThreadStateException|
|run()||新執行緒啟動後會呼叫的方法|如果在建構 Thread 物件時傳遞了Runnable 參數，則執行緒啟動後會呼叫 Runnable 中的 run 方法，否則默認不執行任何操作。但可以創建 Thread 的子類物件，來覆寫預設行為|
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

- 【初始狀態】僅是在語言層面創建了線程物件，尚未與作業系統線程關聯
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
 
本次課使用阻塞式的解決方案：synchronized，來解決上述問題，即俗稱的【物件鎖】，它採用互斥的方式讓同一時刻至多只有一個線程能持有【物件鎖】，其它線程再想獲取這個【物件鎖】時就會阻塞住。 這樣就能保證擁有鎖的線程可以安全的執行臨界區內的程式碼，不用擔心線程上下文切換

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
- 這中間即使 t1 的 cpu 時間片不幸用完，被踢出了門外（不要錯誤理解為鎖住了物件就能一直執行下去哦），
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

 其實就是考察 synchronized 鎖住的是哪個物件

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


## Monitor原理

- Monitor 翻譯為***監視器***或***管程***
- 每個 Java 物件都可以關聯一個 Monitor 物件，如果使用 synchronized 給對像上鎖（重量級）之後，該物件頭的Mark Word 中就被設定指向 Monitor 物件的指針

Monitor 結構如下
![17](imgs/17.png)

- 剛開始 Monitor 中 Owner 為 null
- 當 Thread-2 執行 synchronized(obj) 就會將 Monitor 的所有者 Owner 置為 Thread-2，Monitor中只能有一個 Owner
- 在 Thread-2 上鎖的過程中，如果 Thread-3，Thread-4，Thread-5 也來執行 synchronized(obj)，就會進入EntryList BLOCKED
- Thread-2 執行完同步程式碼區塊的內容，然後喚醒 EntryList 中等待的執行緒來競爭鎖，競爭的時是非公平的
- 圖中 WaitSet 中的 Thread-0，Thread-1 是之前獲得過鎖，但條件不滿足進入 WAITING 狀態的線程，後wait-notify 會分析

>注意：
>- synchronized 必須是進入同一個物件的 monitor 才有上述的效果
>- 不加 synchronized 的物件不會關聯監視器，不遵從上述規則

## synchronized 原理

```java
static final Object lock = new Object();
static int counter = 0;
public static void main(String[] args) {
  synchronized (lock) {
    counter++;
  }
}

```

對應的字節碼為

```java
public class mutiThread.Test
  minor version: 0
  major version: 52
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #1                          // mutiThread/Test
  super_class: #3                         // java/lang/Object
  interfaces: 0, fields: 2, methods: 3, attributes: 1
Constant pool:
   #1 = Class              #2             // mutiThread/Test
   #2 = Utf8               mutiThread/Test
   #3 = Class              #4             // java/lang/Object
   #4 = Utf8               java/lang/Object
   #5 = Utf8               lock
   #6 = Utf8               Ljava/lang/Object;
   #7 = Utf8               counter
   #8 = Utf8               I
   #9 = Utf8               <clinit>
  #10 = Utf8               ()V
  #11 = Utf8               Code
  #12 = Methodref          #3.#13         // java/lang/Object."<init>":()V
  #13 = NameAndType        #14:#10        // "<init>":()V
  #14 = Utf8               <init>
  #15 = Fieldref           #1.#16         // mutiThread/Test.lock:Ljava/lang/Object;
  #16 = NameAndType        #5:#6          // lock:Ljava/lang/Object;
  #17 = Fieldref           #1.#18         // mutiThread/Test.counter:I
  #18 = NameAndType        #7:#8          // counter:I
  #19 = Utf8               LineNumberTable
  #20 = Utf8               LocalVariableTable
  #21 = Utf8               this
  #22 = Utf8               LmutiThread/Test;
  #23 = Utf8               main
  #24 = Utf8               ([Ljava/lang/String;)V
  #25 = Utf8               args
  #26 = Utf8               [Ljava/lang/String;
  #27 = Utf8               StackMapTable
  #28 = Class              #26            // "[Ljava/lang/String;"
  #29 = Class              #30            // java/lang/Throwable
  #30 = Utf8               java/lang/Throwable
  #31 = Utf8               SourceFile
  #32 = Utf8               Test.java
{
  static final java.lang.Object lock;
    descriptor: Ljava/lang/Object;
    flags: (0x0018) ACC_STATIC, ACC_FINAL

  static int counter;
    descriptor: I
    flags: (0x0008) ACC_STATIC

  static {};
    descriptor: ()V
    flags: (0x0008) ACC_STATIC
    Code:
      stack=2, locals=0, args_size=0
         0: new           #3                  // class java/lang/Object
         3: dup
         4: invokespecial #12                 // Method java/lang/Object."<init>":()V
         7: putstatic     #15                 // Field lock:Ljava/lang/Object;
        10: iconst_0
        11: putstatic     #17                 // Field counter:I
        14: return
      LineNumberTable:
        line 4: 0
        line 5: 10
      LocalVariableTable:
        Start  Length  Slot  Name   Signature

  public mutiThread.Test();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #12                 // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   LmutiThread/Test;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=2, args_size=1
         0: getstatic     #15                 // <- lock引用 （synchronized開始）// Field lock:Ljava/lang/Object;
         3: dup
         4: astore_1                          // lock引用 -> slot 1
         5: monitorenter                      // 將 lock物件 MarkWord 置為 Monitor 指針
         6: getstatic     #17                 // <-i Field counter:I
         9: iconst_1                          // 準備常數 1
        10: iadd                              //+1
        11: putstatic     #17                 // -> i Field counter:I
        14: aload_1                           // <- lock引用
        15: monitorexit                       // 將 lock物件 MarkWord 重置, 喚醒 EntryList
        16: goto          22
        19: aload_1                           // <- lock引用
        20: monitorexit                       // 將 lock物件 MarkWord 重置, 喚醒 EntryList
        21: athrow                            // throw e
        22: return
      Exception table:
         from    to  target type
             6    16    19   any
            19    21    19   any
      LineNumberTable:
        line 7: 0
        line 8: 6
        line 7: 14
        line 10: 22
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      23     0  args   [Ljava/lang/String;
      StackMapTable: number_of_entries = 2
        frame_type = 255 /* full_frame */
          offset_delta = 19
          locals = [ class "[Ljava/lang/String;", class java/lang/Object ]
          stack = [ class java/lang/Throwable ]
        frame_type = 250 /* chop */
          offset_delta = 2
}
SourceFile: "Test.java"

```

 >***注意***
 >
>方法層級的 synchronized 不會在字節碼指令中有所體現

## synchronized 原理進階

### 1. 輕量級鎖
- 輕量級鎖的使用場景：如果一個物件雖然有多執行緒要加鎖，但加鎖的時間是錯開的（也就是沒有競爭），那麼可以使用輕量級鎖來優化。
- 輕量級鎖定對使用者是透明的，即語法仍然是 synchronized
- 假設有兩個方法同步區塊，利用同一個物件加鎖

```java
	static final Object obj = new Object();

	public static void method1() {
		synchronized (obj) {
			// 同步區塊 A
			method2();
		}
	}

	public static void method2() {
		synchronized (obj) {
			// 同步區塊 B
		}
	}
```


- 建立鎖記錄（Lock Record）物件，每個執行緒都的堆疊幀都會包含一個鎖記錄的結構，內部可以儲存鎖定物件的Mark Word
![18](imgs/18.png)


- 讓鎖定記錄中 Object reference 指向鎖定對象，並嘗試以 cas 取代 Object 的 Mark Word，將 Mark Word 的值存入鎖記錄
![19](imgs/19.png)


- 如果 cas 替換成功，物件頭中儲存了 鎖定記錄位址和狀態 00 ，表示由該執行緒為物件加鎖，這時圖示如下
![20](imgs/20.png)

- 如果 cas 失敗，有兩種情況
  - 如果是其它線程已經持有了該 Object 的輕量級鎖，這時表示有競爭，進入鎖膨脹過程
  - 如果是自己執行了 synchronized 鎖定重入，那麼再增加一條 Lock Record 作為重入的計數

![21](imgs/21.png)

- 當退出 synchronized 程式碼區塊（解鎖時）如果有取值為 null 的鎖定記錄，表示有重入，這時重置鎖定記錄，表示重入計數減一

![22](imgs/22.png)


- 當退出 synchronized 程式碼區塊（解鎖時）鎖定記錄的值不為 null，這時使用 cas 將 Mark Word 的值恢復給對象頭
  - 成功，則解鎖成功
  - 失敗，說明輕量級鎖進行了鎖膨脹或已經升級為重量級鎖，進入重量級鎖解鎖流程

### 鎖膨脹
- 如果在嘗試加輕量級鎖的過程中，CAS 操作無法成功，這時一種情況就是有其它線程為此物件加上了輕量級鎖定（有競爭），這時需要進行鎖膨脹，將輕量級鎖變為重量級鎖。

```java
	static Object obj = new Object();

	public static void method1() {
		synchronized (obj) {
			// 同步區塊
		}
	}
```


- 當 Thread-1 進行輕量級加鎖時，Thread-0 已經對該物件加了輕量級鎖

![23](imgs/23.png)

- 這時 Thread-1 加輕量級鎖定失敗，進入鎖定膨脹流程
  - 即為 Object 物件申請 Monitor 鎖，讓 Object 指向重量級鎖位址
  - 然後自己進入 Monitor 的 EntryList BLOCKED

![24](imgs/24.png)
- 當 Thread-0 退出同步區塊解鎖時，使用 cas 將 Mark Word 的值還原給物件頭，失敗。 這時會進入重量級解鎖流程，即依照 Monitor 位址找到Monitor 對象，設定 Owner 為 null，喚醒 EntryList 中 BLOCKED 執行緒


### 3. 自旋優化
- 重量級鎖競爭的時候，還可以使用自旋來進行最佳化，如果當前線程自旋成功（即這時候持鎖線程已經退出了同步塊，釋放了鎖），這時當前執行緒就可以避免阻塞。

自旋重試成功的情況
![25](imgs/25.png)

自旋重試失敗的情況
![26](imgs/26.png)

- 自旋會佔用 CPU 時間，單核心 CPU 自旋就是浪費，多核心 CPU 自旋才能發揮優勢。
- 在 Java 6 之後自旋鎖是自適應的，例如物件剛剛的一次自旋操作成功過，那麼認為這次自旋成功的可能性會高，就多自旋幾次；反之，就少自旋甚至不自旋，總之，比較智能。
- Java 7 之後無法控制是否開啟自旋功能


### 4. 偏向鎖
- 輕量級鎖在沒有競爭時（就自己這個執行緒），每次重入仍然需要執行 CAS 操作。
- Java 6 中引入了偏向鎖定來做進一步優化：只有第一次使用 CAS 將線程 ID 設定到物件的 Mark Word 頭，之後發現這個線程 ID 是自己的就表示沒有競爭，不用重新 CAS。 以後只要不發生競爭，這個物件就歸該執行緒所有
  
例如：

```java
	static final Object obj = new Object();

	public static void m1() {
		synchronized (obj) {
			// 同步區塊 A
			m2();
		}
	}

	public static void m2() {
		synchronized (obj) {
			// 同步區塊 B
			m3();
		}
	}

	public static void m3() {
		synchronized (obj) {// 同步區塊 C
		}
	}
```

![27](imgs/27.png)
![28](imgs/28.png)


### 偏向狀態

物件頭格式
![29](imgs/29.png)

- 一個物件創建時：
- 如果開啟了偏向鎖定（預設為開啟），那麼物件建立後，markword 值為 0x05 即最後 3 位元為 101，這時它的thread、epoch、age 都為 0
- 偏向鎖是預設是延遲的，不會在程式啟動時立即生效，如果想避免延遲，可以加 VM 參數 -XX:BiasedLockingStartupDelay=0 來停用延遲
- 如果沒有開啟偏向鎖，那麼物件建立後，markword 值為 0x01 即最後 3 位元為 001，這時它的 hashcode、age 都是 0，第一次用到 hashcode 時才會賦值

1） 測試延遲特性

2） 測試偏向鎖

```java
class Dog {}
```

利用 jol 第三方工具來查看物件頭資訊（注意這裡我擴展了 jol 讓它輸出更為簡潔）

```java
	// 新增虛擬機器參數 -XX:BiasedLockingStartupDelay=0
	public static void main(String[] args) throws IOException {
		Dog d = new Dog();
		ClassLayout classLayout = ClassLayout.parseInstance(d);
		new Thread(() -> {
			log.debug("synchronized 前");
			System.out.println(classLayout.toPrintableSimple(true));
			synchronized (d) {
				log.debug("synchronized 中");
				System.out.println(classLayout.toPrintableSimple(true));
			}
			log.debug("synchronized 後");
			System.out.println(classLayout.toPrintableSimple(true));
		}, "t1").start();
	}
```
```
11:08:58.117 c.TestBiased [t1] - synchronized 前
00000000 00000000 00000000 00000000 00000000 00000000 000000000 00000101
11:08:58.121 c.TestBiased [t1] - synchronized 中
00000000 00000000 00000000 00000000 00011111 11101011 11010000 00000101
11:08:58.121 c.TestBiased [t1] - synchronized 後
00000000 00000000 00000000 00000000 00011111 11101011 11010000 00000101
```

>注意
>處於偏向鎖的物件解鎖後，執行緒 id 仍儲存於物件頭中



3）測試禁用

在上面測試程式碼運行時在新增 VM 參數 -XX:-UseBiasedLocking 停用偏向鎖

輸出

```
11:13:10.018 c.TestBiased [t1] - synchronized 前
00000000 00000000 00000000 00000000 00000000 00000000 000000000 00000001
11:13:10.021 c.TestBiased [t1] - synchronized 中
00000000 00000000 00000000 00000000 00100000 00010100 11110011 10001000
11:13:10.021 c.TestBiased [t1] - synchronized 後
00000000 00000000 00000000 00000000 00000000 00000000 000000000 00000001
```


4) 測試 hashCode
   
正常狀態物件一開始是沒有 hashCode 的，第一次呼叫才生成

### 撤銷 - 呼叫物件 hashCode
呼叫了物件的 hashCode，但偏向鎖的物件 MarkWord 中儲存的是執行緒 id，如果呼叫 hashCode 會導致偏向鎖被撤銷
- 輕量級鎖定會在鎖定記錄中記錄 hashCode
- 重量級鎖會在 Monitor 中記錄 hashCode
  
呼叫 hashCode 後使用偏向鎖，記得去掉 -XX:-UseBiasedLocking 輸出


```
11:22:10.386 c.TestBiased [main] - 呼叫 hashCode:1778535015
11:22:10.391 c.TestBiased [t1] - synchronized 前
00000000 00000000 00000000 01101010 00000010 01001010 01100111 00000001
11:22:10.393 c.TestBiased [t1] - synchronized 中
00000000 00000000 00000000 00000000 00100000 11000011 11110011 01101000
11:22:10.393 c.TestBiased [t1] - synchronized 後
00000000 00000000 00000000 01101010 00000010 01001010 01100111 00000001
```

### 撤銷 - 其它線程使用對象
- 當有其它執行緒使用偏向鎖物件時，會將偏向鎖升級為輕量級鎖

```java
	private static void test2() throws InterruptedException {
		Dog d = new Dog();
		Thread t1 = new Thread(() -> {
			synchronized (d) {
				log.debug(ClassLayout.parseInstance(d).toPrintableSimple(true));
			}
			synchronized (TestBiased.class) {
				TestBiased.class.notify();
			}
			// 如果不用 wait/notify 使用 join 必須開啟下面的註釋
			// 因為：t1 線程不能結束，否則底層線程可能被 jvm 重用作為 t2 線程，底層線程 id 是一樣的
			/*
			 * try { System.in.read(); } catch (IOException e) { e.printStackTrace(); }
			 */
		}, "t1");
		t1.start();
		Thread t2 = new Thread(() -> {
			synchronized (TestBiased.class) {
				try {
					TestBiased.class.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			log.debug(ClassLayout.parseInstance(d).toPrintableSimple(true));
			synchronized (d) {
				log.debug(ClassLayout.parseInstance(d).toPrintableSimple(true));
			}
			log.debug(ClassLayout.parseInstance(d).toPrintableSimple(true));
		}, "t2");
		t2.start();
	}
```

```
[t1] - 00000000 00000000 00000000 00000000 00011111 01000001 00010000 00000101 
[t2] - 00000000 00000000 00000000 00000000 00011111 01000001 00010000 00000101 
[t2] - 00000000 00000000 00000000 00000000 00011111 10110101 11110000 01000000 
[t2] - 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
```


### 撤銷 - 呼叫 wait/notify

- 因為wait/notify只有重量級鎖有，所以也會撤銷偏向鎖

```java
	public static void main(String[] args) throws InterruptedException {
		Dog d = new Dog();
		Thread t1 = new Thread(() -> {
			log.debug(ClassLayout.parseInstance(d).toPrintableSimple(true));
			synchronized (d) {
				log.debug(ClassLayout.parseInstance(d).toPrintableSimple(true));
				try {
					d.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				log.debug(ClassLayout.parseInstance(d).toPrintableSimple(true));
			}
		}, "t1");
		t1.start();
		new Thread(() -> {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (d) {
				log.debug("notify");
				d.notify();
			}
		}, "t2").start();
	}
```

```
[t1] - 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000101 
[t1] - 00000000 00000000 00000000 00000000 00011111 10110011 11111000 00000101 
[t2] - notify 
[t1] - 00000000 00000000 00000000 00000000 00011100 11010100 00001101 11001010 
```

### 批量重偏向
- 如果對象雖然被多個線程訪問，但沒有競爭，這時偏向了線程 T1 的對象仍有機會重新偏向 T2，重偏向會重置對象的 Thread ID
- 當撤銷偏向鎖閾值超過 20 次後，jvm 會這樣覺得，我是不是偏向錯了呢，於是會在給這些物件加鎖時重新偏向至加鎖線程

```java
	private static void test3() throws InterruptedException {
		Vector<Dog> list = new Vector<>();
		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 30; i++) {
				Dog d = new Dog();
				list.add(d);
				synchronized (d) {
					log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
				}
			}
			synchronized (list) {
				list.notify();
			}
		}, "t1");
		t1.start();

		Thread t2 = new Thread(() -> {
			synchronized (list) {
				try {
					list.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			log.debug("===============> ");
			for (int i = 0; i < 30; i++) {
				Dog d = list.get(i);
				log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
				synchronized (d) {
					log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
				}
				log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
			}
		}, "t2");
		t2.start();
	}
```

```
[t1] - 0 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 1 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 2 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 3 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 4 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 5 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 6 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 7 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 8 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 9 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 10 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 11 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 12 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 13 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 14 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 15 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 16 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 17 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 18 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 19 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 20 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 21 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 22 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 23 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 24 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 25 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 26 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 27 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 28 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t1] - 29 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - ===============> 
[t2] - 0 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 0 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 0 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 1 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 1 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 1 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 2 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 2 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 2 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 3 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 3 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 3 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 4 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 4 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 4 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 5 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 5 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 5 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 6 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 6 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 6 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 7 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 7 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 7 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 8 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 8 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 8 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 9 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 9 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 9 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 10 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 10 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 10 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 11 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 11 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 11 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 12 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 12 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 12 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 13 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 13 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 13 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 14 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 14 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 14 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 15 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 15 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 15 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 16 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 16 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 16 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 17 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 17 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 17 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 18 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 18 00000000 00000000 00000000 00000000 00100000 01011000 11110111 00000000 
[t2] - 18 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 
[t2] - 19 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 19 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 19 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 20 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 20 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 20 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 21 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 21 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 21 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 22 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 22 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 22 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 23 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 23 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 23 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 24 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 24 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 24 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 25 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 25 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 25 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 26 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 26 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 26 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 27 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 27 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 27 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 28 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 28 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 28 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 29 00000000 00000000 00000000 00000000 00011111 11110011 11100000 00000101 
[t2] - 29 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101 
[t2] - 29 00000000 00000000 00000000 00000000 00011111 11110011 11110001 00000101
```

### 大量撤銷
- 當撤銷偏向鎖定閾值超過 40 次後，jvm 會這樣覺得，自己確實偏向錯了，根本就不該偏向。 於是整個類別的所有對象都會變成不可偏向的，新建的物件也是不可偏向的

```java
	static Thread t1, t2, t3;

	private static void test4() throws InterruptedException {
		Vector<Dog> list = new Vector<>();
		int loopNumber = 39;
		t1 = new Thread(() -> {
			for (int i = 0; i < loopNumber; i++) {
				Dog d = new Dog();
				list.add(d);
				synchronized (d) {
					log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
				}
			}
			LockSupport.unpark(t2);
		}, "t1");
		t1.start();
		t2 = new Thread(() -> {
			LockSupport.park();
			log.debug("===============> ");
			for (int i = 0; i < loopNumber; i++) {
				Dog d = list.get(i);
				log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
				synchronized (d) {
					log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
				}
				log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
			}
			LockSupport.unpark(t3);
		}, "t2");
		t2.start();
		t3 = new Thread(() -> {
			LockSupport.park();
			log.debug("===============> ");
			for (int i = 0; i < loopNumber; i++) {
				Dog d = list.get(i);
				log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
				synchronized (d) {
					log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
				}
				log.debug(i + "\t" + ClassLayout.parseInstance(d).toPrintableSimple(true));
			}
		}, "t3");
		t3.start();
		t3.join();
		log.debug(ClassLayout.parseInstance(new Dog()).toPrintableSimple(true));
	}
```

## 5. 鎖消除
- 鎖消除

```java
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MyBenchmark {
	static int x = 0;

	@Benchmark
	public void a() throws Exception {
		x++;
	}

	@Benchmark
	public void b() throws Exception {
		Object o = new Object();
		synchronized (o) {
			x++;
		}
	}
}
```

```
java -jar benchmarks.jar

```

```
# Run complete. Total time: 00:00:20

Benchmark             Mode  Samples  Score  Score error  Units
mutiThread.Test1.a    avgt        5  1.611        0.061  ns/op
mutiThread.Test1.b    avgt        5  1.620        0.061  ns/op
```

- 因為JIT即時編譯器會針對局部變數進行優化，發現物件O不會逃離方法作用域，此物件不會被共享，所以執行的時候沒有加鎖的，所以測試時數據才會跟沒加鎖時相近


- 使用參數 ***-XX:-EliminateLocks*** 關閉鎖消除(前面的減號代表關閉)

```
java -XX:-EliminateLocks -jar benchmarks.jar
```

```
# Run complete. Total time: 00:00:20

Benchmark             Mode  Samples   Score  Score error  Units
mutiThread.Test1.a    avgt        5   1.615        0.020  ns/op
mutiThread.Test1.b    avgt        5  20.343        0.698  ns/op
```

- 鎖粗化
- 對相同物件多次加鎖，導致執行緒發生多次重入，可以使用鎖粗化方式來最佳化，這不同於先前講的細分鎖的粒度。




# wait &  notify

## wait notify 原理



- Owner 執行緒發現條件不滿足，呼叫 wait 方法，即可進入 WaitSet 變成 WAITING 狀態
- BLOCKED 和 WAITING 的執行緒都處於阻塞狀態，不佔用 CPU 時間片
- BLOCKED 執行緒會在 Owner 執行緒釋放鎖定時喚醒
- WAITING 執行緒會在 Owner 執行緒呼叫 notify 或 notifyAll 時喚醒，但喚醒後並不意味著者立刻獲得鎖定，仍需進入EntryList 重新競爭

![30](imgs/30.png)

## API 介绍

- ***obj.wait()*** 讓進入 object 監視器的執行緒到 waitSet 等待
- ***obj.notify()*** 在 object 上正在 waitSet 等待的執行緒中挑一個喚醒
- ***obj.notifyAll()*** 讓 object 上正在 waitSet 等待的執行緒全部喚醒

- 它們都是執行緒之間進行協作的手段，都屬於 Object 物件的方法。 必須取得此物件的鎖，才能呼叫這幾個方法

如果沒獲得鎖直接調用wait()，會拋出IllegalMonitorStateException
```java
	public static void main(String[] args) {
		Object lock = new Object();
		
		try {
			lock.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
```

![31](imgs/31.png)

需要先獲得鎖才能調用wait()

```java
	public static void main(String[] args) {
		Object lock = new Object();
		
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
```


```java
	final static Object obj = new Object();

	public static void main(String[] args) {
		new Thread(() -> {
			synchronized (obj) {
				log.debug("執行....");
				try {
					obj.wait(); // 讓執行緒在obj上一直等待下去
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				log.debug("其它程式碼....");
			}
		}).start();
		new Thread(() -> {
			synchronized (obj) {
				log.debug("執行....");
				try {
					obj.wait(); // 讓執行緒在obj上一直等待下去
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				log.debug("其它程式碼....");
			}
		}).start();
		// 主執行緒兩秒後執行
		sleep(2);
		log.debug("喚醒 obj 上其它執行緒");
		synchronized (obj) {
			obj.notify(); // 喚醒obj上一個執行緒
			// obj.notifyAll(); // 喚醒obj上所有等待執行緒
		}
	}
```


notify 的一種結果

```
20:00:53.096 [Thread-0] c.TestWaitNotify - 執行....
20:00:53.099 [Thread-1] c.TestWaitNotify - 執行....
20:00:55.096 [main] c.TestWaitNotify - 喚醒 obj 上其它線程
20:00:55.096 [Thread-0] c.TestWaitNotify - 其它程式碼....
```

notifyAll 的結果

```
19:58:15.457 [Thread-0] c.TestWaitNotify - 執行....
19:58:15.460 [Thread-1] c.TestWaitNotify - 執行....
19:58:17.456 [main] c.TestWaitNotify - 喚醒 obj 上其它線程
19:58:17.456 [Thread-1] c.TestWaitNotify - 其它程式碼....
19:58:17.456 [Thread-0] c.TestWaitNotify - 其它程式碼....
```

- ***wait()*** 方法會釋放物件的鎖，進入 WaitSet 等待區，讓其他執行緒就機會取得物件的鎖定。 無限制等待，直到
notify 為止
- ***wait(long n)*** 有時限的等待, 到 n 毫秒後結束等待，或是被 notify

## wait notify 的正確姿勢

### sleep(long n) 和 wait(long n) 的差別
- 1) sleep 是 Thread 方法，而 wait 是 Object 的方法
- 2) sleep 不需要強制和 synchronized 配合使用，但 wait 需要和 synchronized 一起用 
- 3) sleep 在睡眠的同時，不會釋放物件鎖的，但 wait 在等待的時候會釋放物件鎖定 
- 4) 它們狀態 TIMED_WAITING

### step 1

```java
static final Object room = new Object();
static boolean hasCigarette = false;
static boolean hasTakeout = false;
```

思考下面的解決方案好不好，為什麼？

```java
		new Thread(() -> {
			synchronized (room) {
				log.debug("有煙沒？[{}]", hasCigarette);
				if (!hasCigarette) {
					log.debug("沒煙，先歇會！");
					sleep(2);
				}
				log.debug("有煙沒？[{}]", hasCigarette);
				if (hasCigarette) {
					log.debug("可以開始工作了");
				}
			}
		}, "小南").start();
		for (int i = 0; i < 5; i++) {
			new Thread(() -> {
				synchronized (room) {
					log.debug("可以開始工作了");
				}
			}, "其它人").start();
		}
		sleep(1);
		new Thread(() -> {
			// 這裡能不能加 synchronized (room)？
			hasCigarette = true;
			log.debug("煙到了噢！");
		}, "送煙的").start();

```
輸出

```java
20:49:49.883 [小南] c.TestCorrectPosture - 有煙沒？ [false]
20:49:49.887 [小南] c.TestCorrectPosture - 沒煙，先歇會！
20:49:50.882 [送煙的] c.TestCorrectPosture - 煙到了噢！
20:49:51.887 [小南] c.TestCorrectPosture - 有煙沒？ [true]
20:49:51.887 [小南] c.TestCorrectPosture - 可以開始工作了
20:49:51.887 [其它人] c.TestCorrectPosture - 可以開始工作了
20:49:51.887 [其它人] c.TestCorrectPosture - 可以開始工作了
20:49:51.888 [其它人] c.TestCorrectPosture - 可以開始工作了
20:49:51.888 [其它人] c.TestCorrectPosture - 可以開始工作了
20:49:51.888 [其它人] c.TestCorrectPosture - 可以開始工作了
```

- 其它幹活的線程，都要一直阻塞，效率太低
- 小南線程必須睡足 2s 後才能醒來，就算煙提前送到，也無法立刻醒來
- 加了 synchronized (room) 後，就好比小南在裡面反鎖了門睡覺，煙根本沒辦法送進門，main 沒加synchronized 就好像 main 線程是翻窗戶進來的
- 解決方法，使用 wait - notify 機制

### step 2

思考下面的實現行嗎，為什麼？

```java
		new Thread(() -> {
			synchronized (room) {
				log.debug("有煙沒？[{}]", hasCigarette);
				if (!hasCigarette) {
					log.debug("沒煙，先歇會！");
					try {
						room.wait(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				log.debug("有煙沒？[{}]", hasCigarette);
				if (hasCigarette) {
					log.debug("可以開始工作了");
				}
			}
		}, "小南").start();
		for (int i = 0; i < 5; i++) {
			new Thread(() -> {
				synchronized (room) {
					log.debug("可以開始工作了");
				}
			}, "其它人").start();
		}
		sleep(1);
		new Thread(() -> {
			synchronized (room) {
				hasCigarette = true;
				log.debug("煙到了噢！");
				room.notify();
			}
		}, "送煙的").start();
```



輸出
```java
20:51:42.489 [小南] c.TestCorrectPosture - 有煙沒？ [false]
20:51:42.493 [小南] c.TestCorrectPosture - 沒煙，先歇會！
20:51:42.493 [其它人] c.TestCorrectPosture - 可以開始工作了
20:51:42.493 [其它人] c.TestCorrectPosture - 可以開始工作了
20:51:42.494 [其它人] c.TestCorrectPosture - 可以開始工作了
20:51:42.494 [其它人] c.TestCorrectPosture - 可以開始工作了
20:51:42.494 [其它人] c.TestCorrectPosture - 可以開始工作了
20:51:43.490 [送煙的] c.TestCorrectPosture - 煙到了噢！
20:51:43.490 [小南] c.TestCorrectPosture - 有煙沒？ [true]
20:51:43.490 [小南] c.TestCorrectPosture - 可以開始工作了
```

- 解決了其它工作的線程阻塞的問題
- 但如果有其它線程也在等待條件呢？

### step 3

```java
		new Thread(() -> {
			synchronized (room) {
				log.debug("有煙沒？[{}]", hasCigarette);
				if (!hasCigarette) {
					log.debug("沒煙，先歇會！");
					try {
						room.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				log.debug("有煙沒？[{}]", hasCigarette);
				if (hasCigarette) {
					log.debug("可以開始工作了");
				} else {
					log.debug("沒幹成活...");
				}
			}
		}, "小南").start();
		new Thread(() -> {
			synchronized (room) {
				Thread thread = Thread.currentThread();
				log.debug("外送送到沒？[{}]", hasTakeout);
				if (!hasTakeout) {
					log.debug("沒外賣，先歇會！");
					try {
						room.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				log.debug("外送送到沒？[{}]", hasTakeout);
				if (hasTakeout) {
					log.debug("可以開始工作了");
				} else {
					log.debug("沒幹成活...");
				}
			}
		}, "小女").start();
		sleep(1);
		new Thread(() -> {
			synchronized (room) {
				hasTakeout = true;
				log.debug("外送到了噢！");
				room.notify();
			}
		}, "外送的").start();
```

輸出

```java
20:53:12.173 [小南] c.TestCorrectPosture - 有煙沒？ [false]
20:53:12.176 [小南] c.TestCorrectPosture - 沒煙，先歇會！
20:53:12.176 [小女] c.TestCorrectPosture - 外送送到沒？ [false]
20:53:12.176 [小女] c.TestCorrectPosture - 沒外賣，先休會！
20:53:13.174 [外送的] c.TestCorrectPosture - 外賣到了噢！
20:53:13.174 [小南] c.TestCorrectPosture - 有煙沒？ [false]
20:53:13.174 [小南] c.TestCorrectPosture - 沒工作...
```

- notify 只能隨機喚醒一個 WaitSet 中的線程，這時如果有其它線程也在等待，那麼就可能喚醒不了正確的線程，稱為【虛假喚醒】
- 解決方法，改為 notifyAll

### step 4

```java
		new Thread(() -> {
			synchronized (room) {
				hasTakeout = true;
				log.debug("外送到了噢！");
				room.notifyAll();
			}
		}, "外送的").start();
```



```java
20:55:23.978 [小南] c.TestCorrectPosture - 有煙沒？ [false]
20:55:23.982 [小南] c.TestCorrectPosture - 沒煙，先歇會！
20:55:23.982 [小女] c.TestCorrectPosture - 外送送到沒？ [false]
20:55:23.982 [小女] c.TestCorrectPosture - 沒外賣，先休會！
20:55:24.979 [外送的] c.TestCorrectPosture - 外賣到了噢！
20:55:24.979 [小女] c.TestCorrectPosture - 外送送到沒？ [true]
20:55:24.980 [小女] c.TestCorrectPosture - 可以開始工作了
20:55:24.980 [小南] c.TestCorrectPosture - 有煙沒？ [false]
20:55:24.980 [小南] c.TestCorrectPosture - 沒工作...
```
- 用 notifyAll 只解決某個執行緒的喚醒問題，但使用 if + wait 判斷只一次機會，一旦條件不成立，就沒有重新判斷的機會了
- 解法，用 while + wait，當條件不成立，再 wait

### step 5
將 if 改為 while
```java
		if (!hasCigarette) {
			log.debug("沒煙，先歇會！");
			try {
				room.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
```

改動後
```java
		while (!hasCigarette) {
			log.debug("沒煙，先歇會！");
			try {
				room.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
```


```java
20:58:34.322 [小南] c.TestCorrectPosture - 有煙沒？ [false]
20:58:34.326 [小南] c.TestCorrectPosture - 沒煙，先歇會！
20:58:34.326 [小女] c.TestCorrectPosture - 外送送到沒？ [false]
20:58:34.326 [小女] c.TestCorrectPosture - 沒外賣，先休會！
20:58:35.323 [外送的] c.TestCorrectPosture - 外賣到了噢！
20:58:35.324 [小女] c.TestCorrectPosture - 外送送到沒？ [true]
20:58:35.324 [小女] c.TestCorrectPosture - 可以開始工作了
20:58:35.324 [小南] c.TestCorrectPosture - 沒煙，先歇會！
```

#### 最後正確的格式

```java
		synchronized (lock) {
			while (條件不成立) {
				lock.wait();
			}
			// 幹活
		}
		// 另一個執行緒
		synchronized (lock) {
			lock.notifyAll();
		}
	}
```

## 模式之保護性暫停

- 跟join的區別在於，join是等待另一個thread結束，而保護性暫停是等待另一個thread的結果

### 1. 定義
- 即 Guarded Suspension，用在一個執行緒等待另一個執行緒的執行結果
- 重點
  - 有一個結果需要從一個線程傳遞到另一個線程，讓他們關聯同一個 GuardedObject
  - 如果有結果不斷從一個執行緒到另一個執行緒那麼可以使用訊息隊列（請參閱生產者/消費者）
  - JDK 中，join 的實作、Future 的實現，採用的就是此模式
  - 因為要等待另一方的結果，因此歸類到同步模式

![32](imgs/32.png)

## 2.實現

```java
class GuardedObject {

     private Object response;
     private final Object lock = new Object();

     public Object get() {
         synchronized (lock) {
             // 條件不滿足則等待
             while (response == null) {
                 try {
                     lock.wait();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
             return response;
         }
     }

     public void complete(Object response) {
         synchronized (lock) {
             // 條件滿足，通知等待執行緒
             this.response = response;
             lock.notifyAll();
         }
     }
}
```

### 應用

```java
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(() -> {
            try {
                // 子執行緒執行下載
                List<String> response = download();
                log.debug("download complete...");
                guardedObject.complete(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        log.debug("waiting...");
        // 主執行緒阻塞等待
        Object response = guardedObject.get();
        log.debug("get response: [{}] lines", ((List<String>) response).size());
    }
```

執行結果

```
08:42:18.568 [main] c.TestGuardedObject - waiting...
08:42:23.312 [Thread-0] c.TestGuardedObject - download complete...
08:42:23.312 [main] c.TestGuardedObject - get response: [3] lines
```

## 3. 帶超時版 GuardedObject

如果要控制超時時間呢

```java
class GuardedObjectV2 {

     private Object response;
     private final Object lock = new Object();

     public Object get(long millis) {
         synchronized (lock) {
             // 1) 記錄最初時間
             long begin = System.currentTimeMillis();
             // 2) 已經經歷過的時間
             long timePassed = 0;
             while (response == null) {
                 // 4) 假設 millis 是 1000，結果在 400 時喚醒了，那麼還有 600 要等
                 long waitTime = millis - timePassed;
                 log.debug("waitTime: {}", waitTime);
                 if (waitTime <= 0) {
                     log.debug("break...");
                     break;
                 }
                 try {
                     lock.wait(waitTime);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 // 3) 若事先被喚醒，這時已經經歷的時間假設為 400
                 timePassed = System.currentTimeMillis() - begin;
                 log.debug("timePassed: {}, object is null {}",
                         timePassed, response == null);
             }
             return response;
         }
     }

     public void complete(Object response) {
         synchronized (lock) {
             // 條件滿足，通知等待執行緒
             this.response = response;
             log.debug("notify...");
             lock.notifyAll();
         }
     }
}
```

測試，沒有超時

```java
public static void main(String[] args) {
         GuardedObjectV2 v2 = new GuardedObjectV2();
         new Thread(() -> {
             sleep(1);
             v2.complete(null);
             sleep(1);
             v2.complete(Arrays.asList("a", "b", "c"));
         }).start();
         Object response = v2.get(2500);
         if (response != null) {
             log.debug("get response: [{}] lines", ((List<String>) response).size());
         } else {
             log.debug("can't get response");
         }
     }
```
輸出

```
08:49:39.917 [main] c.GuardedObjectV2 - waitTime: 2500
08:49:40.917 [Thread-0] c.GuardedObjectV2 - notify...
08:49:40.917 [main] c.GuardedObjectV2 - timePassed: 1003, object is null true
08:49:40.917 [main] c.GuardedObjectV2 - waitTime: 1497
08:49:41.918 [Thread-0] c.GuardedObjectV2 - notify...
08:49:41.918 [main] c.GuardedObjectV2 - timePassed: 2004, object is null false
08:49:41.918 [main] c.TestGuardedObjectV2 - get response: [3] lines
```

測試，超時

```java
// 等待時間不足
List<String> lines = v2.get(1500);
```

輸出

```
08:47:54.963 [main] c.GuardedObjectV2 - waitTime: 1500
08:47:55.963 [Thread-0] c.GuardedObjectV2 - notify...
08:47:55.963 [main] c.GuardedObjectV2 - timePassed: 1002, object is null true
08:47:55.963 [main] c.GuardedObjectV2 - waitTime: 498
08:47:56.461 [main] c.GuardedObjectV2 - timePassed: 1500, object is null true
08:47:56.461 [main] c.GuardedObjectV2 - waitTime: 0
08:47:56.461 [main] c.GuardedObjectV2 - break...
08:47:56.461 [main] c.TestGuardedObjectV2 - can't get response
08:47:56.963 [Thread-0] c.GuardedObjectV2 - notify...
```


## 4. 多工版 GuardedObject
- 圖中 Futures 就好比居民樓層的信箱（每個信箱有房間編號），左側的 t0，t2，t4 就好比等待郵件的居民，右側的 t1，t3，t5 就好比郵差
- 如果需要在多個類別之間使用 GuardedObject 對象，作為參數傳遞不是很方便，因此設計一個用來解耦的中間類，這樣不僅能夠解耦【結果等待者】和【結果生產者】，還能夠同時支援多個任務的管理

![33](imgs/33.png)

新增 id 用來標識 Guarded Object

```java
class GuardedObject {

     // 標識 Guarded Object
     private int id;

     public GuardedObject(int id) {
         this.id = id;
     }

     public int getId() {
         return id;
     }

     // 結果
     private Object response;

     // 取得結果
     // timeout 表示要等多久 2000
     public Object get(long timeout) {
         synchronized (this) {
             // 開始時間 15:00:00
             long begin = System.currentTimeMillis();
             // 經歷的時間
             long passedTime = 0;
             while (response == null) {
                 // 這一輪循環應該等待的時間
                 long waitTime = timeout - passedTime;
                 // 經歷的時間超過了最大等待時間時，退出循環
                 if (timeout - passedTime <= 0) {
                     break;
                 }
                 try {
                     this.wait(waitTime); // 虛假喚醒 15:00:01
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 //求得經歷時間
                 passedTime = System.currentTimeMillis() - begin; // 15:00:02 1s
             }
             return response;
         }
     }

     //產生結果
     public void complete(Object response) {
         synchronized (this) {
             //給結果成員變數賦值
             this.response = response;
             this.notifyAll();
         }
     }
}
```

中間解耦類

```java
class Mailboxes {

    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    private static int id = 1;

    // 產生唯一 id
    private static synchronized int generateId() {
        return id++;
    }

    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id);
    }

    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}
```

業務相關類

```java
class People extends Thread {

     @Override
     public void run() {
         // 收信
         GuardedObject guardedObject = Mailboxes.createGuardedObject();
         log.debug("開始收信 id:{}", guardedObject.getId());
         Object mail = guardedObject.get(5000);
         log.debug("收到信 id:{}, 內容:{}", guardedObject.getId(), mail);
     }
}
```

```java
class Postman extends Thread {

    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        guardedObject.complete(mail);
    }
}
```

測試

```java
public static void main(String[] args) throws InterruptedException {
         for (int i = 0; i < 3; i++) {
             new People().start();
         }
         Sleeper.sleep(1);
         for (Integer id : Mailboxes.getIds()) {
             new Postman(id, "內容" + id).start();
         }
     }
```

某次運行結果

```
10:35:05.689 c.People [Thread-1] - 開始收信 id:3
10:35:05.689 c.People [Thread-2] - 開始收信 id:1
10:35:05.689 c.People [Thread-0] - 開始收信 id:2
10:35:06.688 c.Postman [Thread-4] - 送信 id:2, 內容:內容2
10:35:06.688 c.Postman [Thread-5] - 送信 id:1, 內容:內容1
10:35:06.688 c.People [Thread-0] - 收到信 id:2, 內容:內容2
10:35:06.688 c.People [Thread-2] - 收到信 id:1, 內容:內容1
10:35:06.688 c.Postman [Thread-3] - 送信 id:3, 內容:內容3
10:35:06.689 c.People [Thread-1] - 收到信 id:3, 內容:內容3
```



# 模式生產者消費者

### 1. 定義
- 重點
- 與前面的保護性暫停中的 GuardObject 不同，不需要產生結果和消費結果的線程一一對應
- 消費隊列可以用來平衡生產和消費的線程資源
- 生產者僅負責產生結果數據，不關心數據該如何處理，而消費者專心處理結果數據
- 消息隊列是有容量限制的，滿時不會再加入數據，空時不會再消耗數據
- JDK 中各種阻塞佇列，採用的就是這種模式


![34](imgs/34.png)


### 2.實現

```java
class MessageQueue {

     private LinkedList<Message> queue;
     private int capacity;

     public MessageQueue(int capacity) {
         this.capacity = capacity;
         queue = new LinkedList<>();
     }

     public Message take() {
         synchronized (queue) {
             while (queue.isEmpty()) {
                 log.debug("沒貨了, wait");
                 try {
                     queue.wait();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
             Message message = queue.removeFirst();
             queue.notifyAll();
             return message;
         }
     }

     public void put(Message message) {
         synchronized (queue) {
             while (queue.size() == capacity) {
                 log.debug("庫存已達上限, wait");
                 try {
                     queue.wait();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
             queue.addLast(message);
             queue.notifyAll();
         }
     }
}
```
#### 應用

```java
MessageQueue messageQueue = new MessageQueue(2);
         // 4 個生產者執行緒, 下載任務
         for (int i = 0; i < 4; i++) {
             int id = i;
             new Thread(() -> {
                 try {
                     log.debug("download...");
                     List<String> response = Downloader.download();
                     log.debug("try put message({})", id);
                     messageQueue.put(new Message(id, response));
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }, "生產者" + i).start();
         }
         // 1 個消費者線程, 處理結果
         new Thread(() -> {
             while (true) {
                 Message message = messageQueue.take();
                 List<String> response = (List<String>) message.getMessage();
                 log.debug("take message({}): [{}] lines", message.getId(), response.size());
             }
         }, "消費者").start();
```


某次運行結果

```
10:48:38.070 [生產者3] c.TestProducerConsumer - download...
10:48:38.070 [生產者0] c.TestProducerConsumer - download...
10:48:38.070 [消費者] c.MessageQueue - 沒貨了, wait
10:48:38.070 [生產者1] c.TestProducerConsumer - download...
10:48:38.070 [生產者2] c.TestProducerConsumer - download...
10:48:41.236 [生產者1] c.TestProducerConsumer - try put message(1)
10:48:41.237 [生產者2] c.TestProducerConsumer - try put message(2)
10:48:41.236 [生產者0] c.TestProducerConsumer - try put message(0)
10:48:41.237 [生產者3] c.TestProducerConsumer - try put message(3)
10:48:41.239 [生產者2] c.MessageQueue - 庫存已達上限, wait
10:48:41.240 [生產者1] c.MessageQueue - 庫存已達上限, wait
10:48:41.240 [消費者] c.TestProducerConsumer - take message(0): [3] lines
10:48:41.240 [生產者2] c.MessageQueue - 庫存已達上限, wait
10:48:41.240 [消費者] c.TestProducerConsumer - take message(3): [3] lines
10:48:41.240 [消費者] c.TestProducerConsumer - take message(1): [3] lines
10:48:41.240 [消費者] c.TestProducerConsumer - take message(2): [3] lines
10:48:41.240 [消費者] c.MessageQueue - 沒貨了, wait
```

# Park & Unpark

## 基本使用
- 它們是 LockSupport 類別中的方法

```java
// 暫停目前執行緒
LockSupport.park();
// 恢復某個執行緒的運行
LockSupport.unpark(暫停線程物件)
```

- 先 park 再 unpark

```java
Thread t1 = new Thread(() -> {
    log.debug("start...");
    sleep(1);
    log.debug("park...");
    LockSupport.park();
    log.debug("resume...");
},"t1");
t1.start();
sleep(2);
log.debug("unpark...");
LockSupport.unpark(t1);
```


```java
18:42:52.585 c.TestParkUnpark [t1] - start... 
18:42:53.589 c.TestParkUnpark [t1] - park... 
18:42:54.583 c.TestParkUnpark [main] - unpark... 
18:42:54.583 c.TestParkUnpark [t1] - resume... 
```


- 先 unpark 再 park
```java
Thread t1 = new Thread(() -> {
    log.debug("start...");
    sleep(2);
    log.debug("park...");
    LockSupport.park();
    log.debug("resume...");
}, "t1");
t1.start();
sleep(1);
log.debug("unpark...");
LockSupport.unpark(t1);
```

```java
18:43:50.765 c.TestParkUnpark [t1] - start... 
18:43:51.764 c.TestParkUnpark [main] - unpark... 
18:43:52.769 c.TestParkUnpark [t1] - park... 
18:43:52.769 c.TestParkUnpark [t1] - resume... 
```

## 特點
  - 與 Object 的 wait & notify 相比
    - wait，notify 和 notifyAll 必須配合 Object Monitor 一起使用，而 park，unpark 不必

    - park & unpark 是以線程為單位來【阻塞】和【喚醒】線程，而 notify 只能隨機喚醒一個等待線程，notifyAll
    是喚醒所有等待線程，就不那麼【精確】
    - park & unpark 可以先 unpark，而 wait & notify 不能先 notify

## park unpark 原理


- 每個線程都有自己的一個 Parker 對象，由三個部分組成 _counter ， _cond 和 _mutex 打個比喻
  - 線就像旅人，Parker 就像他隨身攜帶的背包，條件變數就好比背包裡的帳篷。 _counter 就好比背包中
  的備用乾糧（0 為耗盡，1 為充足）
  - 呼叫 park 就是要看需不需要停下來歇息
    - 如果備用乾糧耗盡，那麼鑽進帳篷歇息
    - 如果備用乾糧充足，那麼不需停留，繼續前進
  - 調用 unpark，就好比令乾糧充足
    - 如果這時線程還在帳篷，就喚醒讓他繼續前進
    - 如果此時線程還在運行，那麼下次他調用 park 時，僅是消耗掉備用乾糧，不需停留繼續前進
      - 因為背包空間有限，多次呼叫 unpark 僅會補充一份備用乾糧

![35](imgs/35.png)

1. 目前執行緒呼叫 Unsafe.park() 方法
2. 檢查 _counter ，本情況為 0，這時，獲得 _mutex 互斥鎖
3. 線程進入 _cond 條件變數阻塞
4. 設定 _counter = 0


![36](imgs/36.png)

1. 呼叫 Unsafe.unpark(Thread_0) 方法，設定 _counter 為 1
2. 喚醒 _cond 條件變數中的 Thread_0
3. Thread_0 恢復運行
4. 設定 _counter 為 0

![37](imgs/37.png)


1. 呼叫 Unsafe.unpark(Thread_0) 方法，設定 _counter 為 1
2. 目前執行緒呼叫 Unsafe.park() 方法
3. 檢查 _counter ，本情況為 1，此時執行緒無需阻塞，繼續執行
4. 設定 _counter 為 0


# 執行緒狀態轉換

![38](imgs/38.png)
假設有線程 Thread t

### 情況 1 NEW --> RUNNABLE
- 當呼叫 t.start() 方法時，由 NEW --> RUNNABLE


### 情況 2 RUNNABLE <--> WAITING
- t 線程用 synchronized(obj) 取得了物件鎖定後
  - 當呼叫 obj.wait() 方法時，t 執行緒從 RUNNABLE --> WAITING
  - 呼叫 obj.notify() ， obj.notifyAll() ， t.interrupt() 時
    - 競爭鎖定成功，t 執行緒從 WAITING --> RUNNABLE
    - 競爭鎖失敗，t 執行緒從 WAITING --> BLOCKED

```java
public class TestWaitNotify {
	final static Object obj = new Object();

	public static void main(String[] args) {
		new Thread(() -> {
			synchronized (obj) {
				log.debug("執行....");
				try {
					obj.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				log.debug("其它程式碼...."); // 斷點
			}
		}, "t1").start();
		new Thread(() -> {
			synchronized (obj) {
				log.debug("執行....");
				try {
					obj.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				log.debug("其它程式碼...."); // 斷點
			}
		}, "t2").start();
		sleep(0.5);
		log.debug("喚醒 obj 上其它執行緒");
		synchronized (obj) {
			obj.notifyAll(); // 喚醒obj上所有等待執行緒 斷點
		}
	}
}
```



### 情況 3 RUNNABLE <--> WAITING
- 當目前執行緒呼叫 t.join() 方法時，當前執行緒從 RUNNABLE --> WAITING
  - 注意是當前執行緒在t 執行緒物件的監視器上等待
- t 執行緒運行結束，或呼叫了目前執行緒的 interrupt() 時，目前執行緒從 WAITING --> RUNNABLE

### 情況 4 RUNNABLE <--> WAITING
- 目前執行緒呼叫 LockSupport.park() 方法會讓目前執行緒從 RUNNABLE --> WAITING
- 呼叫 LockSupport.unpark(目標執行緒) 或呼叫了執行緒 的 interrupt() ，會讓目標執行緒從 WAITING --> RUNNABLE

### 情況 5 RUNNABLE <--> TIMED_WAITING

t 線程用 synchronized(obj) 取得了物件鎖定後
- 當呼叫 obj.wait(long n) 方法時，t 執行緒從 RUNNABLE --> TIMED_WAITING
- t 執行緒等待時間超過了 n 毫秒，或呼叫 obj.notify() ， obj.notifyAll() ， t.interrupt() 時
  - 競爭鎖定成功，t 執行緒從 TIMED_WAITING --> RUNNABLE
  - 競爭鎖定失敗，t 執行緒從 TIMED_WAITING --> BLOCKED


### 情況 6 RUNNABLE <--> TIMED_WAITING
- 當目前執行緒呼叫 t.join(long n) 方法時，目前執行緒從 RUNNABLE --> TIMED_WAITING
  - 注意是當前執行緒在t 執行緒物件的監視器上等待
- 當前線程等待時間超過了 n 毫秒，或t 線程運行結束，或調用了當前線程的 interrupt() 時，當前線程從 TIMED_WAITING --> RUNNABLE

### 情況 7 RUNNABLE <--> TIMED_WAITING
- 目前執行緒呼叫 Thread.sleep(long n) ，目前執行緒從 RUNNABLE --> TIMED_WAITING
- 目前執行緒等待時間超過了 n 毫秒，目前執行緒從 TIMED_WAITING --> RUNNABLE

### 情況 8 RUNNABLE <--> TIMED_WAITING
- 當目前執行緒呼叫 LockSupport.parkNanos(long nanos) 或 LockSupport.parkUntil(long millis) 時，當線程從 RUNNABLE --> TIMED_WAITING
- 呼叫 LockSupport.unpark(目標線程) 或呼叫了執行緒 的 interrupt() ，或是等待逾時，會讓目標執行緒從 TIMED_WAITING--> RUNNABLE


### 情況 9 RUNNABLE <--> BLOCKED
- t 線程用 synchronized(obj) 獲取了對象鎖時如果競爭失敗，從 RUNNABLE --> BLOCKED
- 持 obj 鎖執行緒的同步程式碼區塊執行完畢，會喚醒該物件上所有 BLOCKED 的執行緒重新競爭，如果其中 t 執行緒競爭成功，從 BLOCKED --> RUNNABLE ，其它失敗的線程仍然 BLOCKED
  
### 情況 10 RUNNABLE <--> TERMINATED
- 目前執行緒所有程式碼都運行完畢，進入TERMINATED

# 活躍性

### 死鎖
- 有這樣的情況：一個執行緒需要同時取得多把鎖，這時就容易發生死鎖t1 執行緒 取得 A物件 鎖，接下來想取得 B物件的鎖 t2 執行緒 取得 B物件 鎖，接下來想取得 A物件的鎖 
- 範例：

```java
		Object A = new Object();
		Object B = new Object();
		Thread t1 = new Thread(() -> {
			synchronized (A) {
				log.debug("lock A");
				sleep(1);
				synchronized (B) {
					log.debug("lock B");
					log.debug("操作...");
				}
			}
		}, "t1");
		Thread t2 = new Thread(() -> {
			synchronized (B) {
				log.debug("lock B");
				sleep(0.5);
				synchronized (A) {
					log.debug("lock A");
					log.debug("操作...");
				}
			}
		}, "t2");
		t1.start();
		t2.start();
```

### 定位死鎖

- 偵測死鎖可以使用 jconsole工具，或使用 jps 定位進程 id，再用 jstack 定位死鎖：

```java
cmd > jps
Picked up JAVA_TOOL_OPTIONS: -Dfile.encoding=UTF-8
12320 Jps
22816 KotlinCompileDaemon
33200 TestDeadLock // JVM 進程
11508 Main
28468 Launcher
```
![39](imgs/39.png)
![40](imgs/40.png)

- 避免死鎖要注意加鎖順序
- 另外如果因為某個執行緒進入了死循環，導致其它執行緒一直等待，對於這種情況 linux 下可以透過 top 先定位到CPU 佔用高的 Java 進程，再利用 top -Hp 進程id 來定位是哪個線程，最後再用 jstack 排查


### 活鎖

- 活鎖出現在兩個線程互相改變對方的結束條件，最後誰也無法結束，例如

```java
public class TestLiveLock {
	static volatile int count = 10;
	static final Object lock = new Object();

	public static void main(String[] args) {
		new Thread(() -> {
// 期望減到 0 退出循環
			while (count > 0) {
				sleep(0.2);
				count--;
				log.debug("count: {}", count);
			}
		}, "t1").start();
		new Thread(() -> {
// 期望超過 20 退出循環
			while (count < 20) {
				sleep(0.2);
				count++;
				log.debug("count: {}", count);
			}
		}, "t2").start();
	}
}
```


### 飢餓


- 很多教程中把飢餓定義為，一個執行緒由於優先權太低，始終得不到 CPU 調度執行，也不能夠結束，飢餓的情況不易演示，講讀寫鎖時會涉及飢餓問題
- 下面我來講一下我遇到的一個線程飢餓的例子，先來看看使用順序加鎖的方式解決之前的死鎖問題

![41](imgs/41.png)
順序加鎖的解決方案
![42](imgs/42.png)


# ReentrantLock


- 相對於 synchronized 它具備以下特點
  - 可中斷
  - 可以設定超時時間
  - 可以設定為公平鎖
  - 支援多個條件變數
- 與 synchronized 一樣，都支援可重入


### 基本文法

```java
// 取得鎖
reentrantLock.lock();
try {
// 臨界區
} finally {
// 釋放鎖
    reentrantLock.unlock();
}
```


### 可重入
- 可重入是指同一個線程如果首次獲得了這把鎖，那麼因為它是這把鎖的擁有者，因此有權利再次獲取這把鎖，如果是不可重入鎖，那麼第二次獲得鎖時，自己也會被鎖擋住


```java
	static ReentrantLock lock = new ReentrantLock();

	public static void main(String[] args) {
		method1();
	}

	public static void method1() {
		lock.lock();
		try {
			log.debug("execute method1");
			method2();
		} finally {
			lock.unlock();
		}
	}

	public static void method2() {
		lock.lock();
		try {
			log.debug("execute method2");
			method3();
		} finally {
			lock.unlock();
		}
	}

	public static void method3() {
		lock.lock();
		try {
			log.debug("execute method3");
		} finally {
			lock.unlock();
		}
	}
```


```
17:59:11.862 [main] c.TestReentrant - execute method1
17:59:11.865 [main] c.TestReentrant - execute method2
17:59:11.865 [main] c.TestReentrant - execute method3
```


###可打斷

範例

```java
		ReentrantLock lock = new ReentrantLock();
		Thread t1 = new Thread(() -> {
			log.debug("啟動...");
			try {
				lock.lockInterruptibly();
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.debug("等鎖的過程中被打斷");
				return;
			}
			try {
				log.debug("獲得了鎖");
			} finally {
				lock.unlock();
			}
		}, "t1");
		lock.lock();
		log.debug("獲得了鎖");
		t1.start();
		try {
			sleep(1);
			t1.interrupt();
			log.debug("執行打斷");
		} finally {
			lock.unlock();
		}
```



```
18:02:40.520 [main] c.TestInterrupt - 獲得了鎖
18:02:40.524 [t1] c.TestInterrupt - 啟動...
18:02:41.530 [main] c.TestInterrupt - 執行打斷
java.lang.InterruptedException
at
java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireInterruptibly(AbstractQueuedSynchr
onizer.java:898)
at
java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireInterruptibly(AbstractQueuedSynchron
izer.java:1222)
at java.util.concurrent.locks.ReentrantLock.lockInterruptibly(ReentrantLock.java:335)
at cn.itcast.n4.reentrant.TestInterrupt.lambda$main$0(TestInterrupt.java:17)
at java.lang.Thread.run(Thread.java:748)
18:02:41.532 [t1] c.TestInterrupt - 等鎖的過程中被打斷
```


注意如果是不可中斷模式，那麼即使使用了 interrupt 也不會讓等待中斷

```java
		ReentrantLock lock = new ReentrantLock();
		Thread t1 = new Thread(() -> {
			log.debug("啟動...");
			lock.lock();
			try {
				log.debug("獲得了鎖");
			} finally {
				lock.unlock();
			}
		}, "t1");
		lock.lock();
		log.debug("獲得了鎖");
		t1.start();
		try {
			sleep(1);
			t1.interrupt();
			log.debug("執行打斷");
			sleep(1);
		} finally {
			log.debug("釋放了鎖");
			lock.unlock();
		}
```


```
18:06:56.261 [main] c.TestInterrupt - 獲得了鎖
18:06:56.265 [t1] c.TestInterrupt - 啟動...
18:06:57.266 [main] c.TestInterrupt - 執行打斷 // 這時 t1 並沒有被真正打斷, 而是仍繼續等待鎖
18:06:58.267 [main] c.TestInterrupt - 釋放了鎖
18:06:58.267 [t1] c.TestInterrupt - 獲得了鎖
```


### 鎖逾時

立刻失敗

```java
		ReentrantLock lock = new ReentrantLock();
		Thread t1 = new Thread(() -> {
			log.debug("啟動...");
			if (!lock.tryLock()) {
				log.debug("取得立刻失敗，返回");
				return;
			}
			try {
				log.debug("獲得了鎖");
			} finally {
				lock.unlock();
			}
		}, "t1");
		lock.lock();
		log.debug("獲得了鎖");
		t1.start();
		try {
			sleep(2);
		} finally {
			lock.unlock();
		}
```

```
18:15:02.918 [main] c.TestTimeout - 獲得了鎖
18:15:02.921 [t1] c.TestTimeout - 啟動...
18:15:02.921 [t1] c.TestTimeout - 取得立刻失敗，返回
```


超时失败

```java
		ReentrantLock lock = new ReentrantLock();
		Thread t1 = new Thread(() -> {
			log.debug("啟動...");
			try {
				if (!lock.tryLock(1, TimeUnit.SECONDS)) {
					log.debug("取得等待 1s 後失敗，返回");
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				log.debug("獲得了鎖");
			} finally {
				lock.unlock();
			}
		}, "t1");
		lock.lock();
		log.debug("獲得了鎖");
		t1.start();
		try {
			sleep(2);
		} finally {
			lock.unlock();
		}
```

```
18:19:40.537 [main] c.TestTimeout - 獲得了鎖
18:19:40.544 [t1] c.TestTimeout - 啟動...
18:19:41.547 [t1] c.TestTimeout - 取得等待 1s 後失敗，返回
```


### 公平鎖
- ReentrantLock 預設是不公平的

```java
		ReentrantLock lock = new ReentrantLock(false);
		lock.lock();
		for (int i = 0; i < 500; i++) {
			new Thread(() -> {
				lock.lock();
				try {
					System.out.println(Thread.currentThread().getName() + " running...");
				} finally {
					lock.unlock();
				}
			}, "t" + i).start();
		}
		// 1s 之後去爭搶鎖
		Thread.sleep(1000);
		new Thread(() -> {
			System.out.println(Thread.currentThread().getName() + " start...");
			lock.lock();
			try {
				System.out.println(Thread.currentThread().getName() + " running...");
			} finally {
				lock.unlock();
			}
		}, "強行插入").start();
		lock.unlock();
```

- 強行插入，有機會在中間輸出
>- 注意：此實驗不一定總是能復現

```
t39 running...
t40 running...
t41 running...
t42 running...
t43 running...
強行插入 start...
強行插入 running...
t44 running...
t45 running...
t46 running...
t47 running...
t49 running...
```

改為公平鎖後

```java
ReentrantLock lock = new ReentrantLock(true);
```

強行插入，總是在最後輸出
```
t465 running...
t464 running...
t477 running...
t442 running...
t468 running...
t493 running...
t482 running...
t485 running...
t481 running...
強行插入 running...
```

公平鎖一般沒必要，會降低並發度


### 條件變數
synchronized 中也有條件變量，就是我們講原理時那個 waitSet 休息室，當條件不滿足時進入 waitSet 等待

ReentrantLock 的條件變數比 synchronized 強大之處在於，它是支援多個條件變數的，這就好比

- synchronized 是那些不滿足條件的線程都在一間休息室等訊息
- 而 ReentrantLock 支援多間休息室，有專門等煙的休息室、專門等早餐的休息室、喚醒時也是按休息室來喚醒

使用要點：

- await 前需要取得鎖
- await 執行後，會釋放鎖，進入 conditionObject 等待
- await 的線程被喚醒（或打斷、或超時）取重新競爭 lock 鎖
- 競爭 lock 鎖成功後，從 await 後繼續執行

例子:

```java
	static ReentrantLock lock = new ReentrantLock();
	static Condition waitCigaretteQueue = lock.newCondition();
	static Condition waitbreakfastQueue = lock.newCondition();
	static volatile boolean hasCigrette = false;
	static volatile boolean hasBreakfast = false;

	public static void main(String[] args) {
		new Thread(() -> {
			try {
				lock.lock();
				while (!hasCigrette) {
					try {
						waitCigaretteQueue.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				log.debug("等到它的煙了");
			} finally {
				lock.unlock();
			}
		}).start();
		new Thread(() -> {
			try {
				lock.lock();
				while (!hasBreakfast) {
					try {
						waitbreakfastQueue.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				log.debug("等到它的早餐了");
			} finally {
				lock.unlock();
			}
		}).start();
		sleep(1);
		sendBreakfast();
		sleep(1);
		sendCigarette();
	}

	private static void sendCigarette() {
		lock.lock();
		try {
			log.debug("送煙來了");
			hasCigrette = true;
			waitCigaretteQueue.signal();
		} finally {
			lock.unlock();
		}
	}

	private static void sendBreakfast() {
		lock.lock();
		try {
			log.debug("送早餐來了");
			hasBreakfast = true;
			waitbreakfastQueue.signal();
		} finally {
			lock.unlock();
		}
	}
```

```
18:52:27.680 [main] c.TestCondition - 送早餐來了
18:52:27.682 [Thread-1] c.TestCondition - 等到了它的早餐
18:52:28.683 [main] c.TestCondition - 送煙來了
18:52:28.683 [Thread-0] c.TestCondition - 等到了它的煙
```

# JAVA內存模型 (JMM)

JMM 即 ***Java Memory Model***，它定義了主記憶體(所有執行緒共享的數據，例如類的類的靜態變量、成員變量)、工作記憶體(執行緒私有的數據，例如局部變量)抽象概念，底層對應 CPU 暫存器、快取、硬體記憶體、CPU 指令最佳化等。

- JMM 體現在以下幾個方面
  - 原子性 - 保證指令不會受到執行緒上下文切換的影響
  - 可見性 - 保證指令不會受 cpu 快取的影響
  - 有序性 - 保證指令不會受 cpu 指令並行最佳化的影響

## 可見性
### 退不出的循環
- 先來看一個現象，main 執行緒對 run 變數的修改對於 t 執行緒不可見，導致了 t 執行緒無法停止：


```java
	static boolean run = true;

	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(() -> {
			while (run) {
				// ....
			}
		});
		t.start();
		sleep(1);
		run = false; // 線程t不會如預想的停下來
	}
```

為什麼呢？ 分析一下：
- 1. 初始狀態， t 執行緒剛開始從主記憶體讀取了 run 的值到工作記憶體。

![43](imgs/43.png)

- 2. 因為 t 執行緒要經常從主記憶體讀取 run 的值，JIT 編譯器會將 run 的值快取到自己工作記憶體中的快取中，減少對主記憶體中 run 的訪問，提高效率

![44](imgs/44.png)

- 3. 1 秒之後，main 執行緒修改了 run 的值，並同步至主記憶體，而 t 是從自己工作記憶體中的快取中讀取這個變量的值，結果永遠是舊值

![45](imgs/45.png)

### 解決方法
volatile（易變關鍵字）

它可以用來修飾成員變數和靜態成員變量，他可以避免執行緒從自己的工作快取中尋找變數的值，必須到主記憶體中取得

它的值，線程操作 volatile 變數都是直接操作主存

### 可見性 vs 原子性
前面例子體現的實際上是可見性，它保證的是在多個線程之間，一個線程對 volatile 變數的修改對另一個線程可見， 不能保證原子性，僅用在一個寫線程，多個讀線程的情況： 上例從字節碼理解是這樣的：


```
getstatic run // 線程 t 取得 run true
getstatic run // 線程 t 取得 run true
getstatic run // 線程 t 取得 run true
getstatic run // 線程 t 取得 run true
putstatic run // 執行緒 main 修改 run 為 false， 僅此一次
getstatic run // 線程 t 取得 run false
```

比較一下之前我們將線程安全時舉的例子：兩個線程一個 i++ 一個 i-- ，只能保證看到最新值，不能解決指令交錯


```
// 假設i的初始值為0
getstatic i // 線程2-取得靜態變數i的值 線程內i=0
getstatic i // 線程1-取得靜態變數i的值 線程內i=0
iconst_1 // 執行緒1-準備常數1
iadd // 線程1-自增 線程內i=1
putstatic i // 線程1-將修改後的值存入靜態變數i 靜態變數i=1
iconst_1 // 線程2-準備常數1
isub // 線程2-自減 線程內i=-1
putstatic i // 線程2-將修改後的值存入靜態變數i 靜態變數i=-1
```

>- 注意 synchronized 語句區塊既可以保證程式碼區塊的原子性，也同時保證程式碼區塊內變數的可見性。 但缺點是synchronized 是屬於重量級操作，性能相對較低
>- 如果在前面範例的死迴圈中加入 System.out.println() 會發現即使不加 volatile 修飾符，則執行緒 t 也能正確看到對 run 變數的修改了，想想為什麼？


* 原理之 CPU 快取結構
* 模式之兩階段終止
* 模式之 Balking
## 有序性

JVM 會在不影響正確性的前提下，可以調整語句的執行順序，思考下面一段程式碼


```java
static int i;
static int j;
// 在某個執行緒內執行如下賦值操作
i = ...;
j = ...;
```

可以看到，至於是先執行 i 還是 先執行 j ，對最終的結果不會產生影響。 所以，當上面程式碼真正執行時，可以是

```java
i = ...;
j = ...;
```

也可以是

```java
j = ...;
i = ...;
```

這個特性稱為『指令重排』，多執行緒下『指令重排』會影響正確性。 為什麼要有重排指令這項最佳化呢？ 從 CPU執行指令的原理來理解一下吧

### 指令級並行原理

#### 名詞
- Clock Cycle Time
  - 主頻的概念大家接觸的比較多，而 CPU 的 Clock Cycle Time（時脈週期時間），等於主頻的倒數，意思是 CPU 能 夠辨識的最小時間單位，比如說 4G 主頻的 CPU 的 Clock Cycle Time 就是 0.25 ns，作為對比，我們牆上掛鐘的Cycle Time 是 1s
  - 例如，執行一條加法指令一般需要一個時脈週期時間
- CPI
  - 有的指令需要更多的時脈週期時間，所以引出了 CPI （Cycles Per Instruction）指令平均時脈週期數
- IPC
  - IPC（Instruction Per Clock Cycle） 即 CPI 的倒數，表示每個時脈週期能夠運行的指令數
- CPU 執行時間
  - 程式的 CPU 執行時間，也就是我們前面提到的 user + system 時間，可以用下面的公式來表示

>- 程式 CPU 執行時間 = 指令數 * CPI * Clock Cycle Time 

#### 魚罐頭的故事

- 加工一條魚需要 50 分鐘，只能一條魚、一條魚順序加工...

![47](imgs/47.png)

- 可以將每個魚罐頭的加工流程細分為 5 個步驟：
  - 去鱗清洗 10分鐘
  - 蒸煮瀝水 10分鐘
  - 加註湯料 10分鐘
  - 殺菌出鍋 10分鐘
  - 真空封罐 10分鐘

即使只有一個工人，最理想的情況是：他能夠在 10 分鐘內同時做好這 5 件事，因為對第一條魚的真空裝罐，不會影響對第二條魚的殺菌出鍋...


#### 指令重排序最佳化
- 事實上，現代處理器會設計為一個時脈週期完成一條執行時間最長的 CPU 指令。 為什麼要這麼做呢？ 可以想到指令也可以再分割成一個個較小的階段，例如，每條指令都可以分為： ***取指令 - 指令譯碼 - 執行指令 - 記憶體存取 - 數據寫回*** 這 5 個階段

![48](imgs/48.png)

>術語參考：
>- instruction fetch (IF)
>- instruction decode (ID)
>- execute (EX)
>- memory access (MEM)
>- register write back (WB)

- 在不改變程式結果的前提下，這些指令的各個階段可以透過重排序和組合來實現指令級並行，這項技術在 80's 中葉到 90's 中葉佔據了運算架構的重要地位。

> 提示：
>- 分階段，分工是提升效率的關鍵！


指令重排的前提是，重排指令不能影響結果，例如


```java
// 可以重排的例子
int a = 10; // 指令1
int b = 20; // 指令2
System.out.println( a + b );
// 不能重排的例子
int a = 10; // 指令1
int b = a - 5; // 指令2
```

> 参考： Scoreboarding and the Tomasulo algorithm (which is similar to scoreboarding but makes use of
register renaming) are two of the most common techniques for implementing out-of-order execution
and instruction-level parallelism.

#### 支援管線的處理器
- 現代 CPU 支援多層指令管線，例如支援同時執行 取指令 - 指令譯碼 - 執行指令 - 記憶體存取 - 資料寫回 的處理器，就可以稱為五級指令管線。 這時 CPU 可以在一個時脈週期內，同時運行五個指令的不同階段（相當於一條執行時間最長的複雜指令），IPC = 1，本質上，管線技術並不能縮短單一指令的執行時間，但它變相地提高了指令地吞吐率。

> 提示：
>- 奔騰四（Pentium 4）支援高達 35 等級管線，但由於功耗太高被廢棄

![50](imgs/50.png)

#### SuperScalar 處理器
- 大多數處理器包含多個執行單元，並非所有運算功能都集中在一起，可以再細分為整數運算單元、浮點數運算單元等，這樣可以把多條指令也可以做到並行獲取、譯碼等，CPU 可以在一個時鐘週期內，執行多於一條指令，IPC > 1

![50](imgs/51.png)
![50](imgs/52s.png)

### 詭異的結果


```java
int num = 0;
boolean ready = false;

	// 執行緒1 執行此方法
	public void actor1(I_Result r) {
		if (ready) {
			r.r1 = num + num;
		} else {
			r.r1 = 1;
		}
	}

	// 執行緒2 執行此方法
	public void actor2(I_Result r) {
		num = 2;
		ready = true;
	}

```


I_Result 是一個對象，有一個屬性 r1 用來保存結果，問，可能的結果有幾種？

有同學這麼分析

情況1：線程1 先執行，這時 ready = false，所以進入 else 分支結果為 1

情況2：線程2 先執行 num = 2，但來不及執行 ready = true，線程1 執行，還是進入 else 分支，結果為1

情況3：線程2 執行到 ready = true，線程1 執行，這回進入 if 分支，結果為 4（因為 num 已經執行過了）

但我告訴你，結果還有可能是 0 😁😁😁，信不信吧！

這種情況下是：線程2 執行 ready = true，切換到線程1，進入 if 分支，相加為 0，然後切回線程2 執行 num = 2

相信很多人已經暈了 😵😵

這種現象叫做指令重排，是 JIT 編譯器在執行時的一些最佳化，這個現象需要通過大量測試才能重現：

使用 java 並發壓測工具 jcstress https://wiki.openjdk.java.net/display/CodeTools/jcstress


```
mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jcstress -DarchetypeArtifactId=jcstress-java-test-archetype -DarchetypeVersion=0.5 -DgroupId=com.frank -DartifactId=ordering -Dversion=1.0
```


建立 maven 項目，提供以下測試類

```java
@JCStressTest
@Outcome(id = { "1", "4" }, expect = Expect.ACCEPTABLE, desc = "ok")
@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "!!!!")
@State
public class ConcurrencyTest {
	int num = 0;
	boolean ready = false;

	@Actor
	public void actor1(II_Result r) {
		if (ready) {
			r.r1 = num + num;
		} else {
			r.r1 = 1;
		}
	}

	@Actor
	public void actor2(II_Result r) {
		num = 2;
		ready = true;
	}
}
```

執行


```java
mvn clean install
java -jar target/jcstress.jar
```

會輸出我們感興趣的結果，摘錄其中一次結果：

![46](imgs/46.png)


可以看到，出現結果為 0 的情況有 638 次，雖然次數相對很少，但畢竟是出現了。

### 解決方法

volatile 修飾的變量，可以停用指令重排


```java
@JCStressTest
@Outcome(id = { "1", "4" }, expect = Expect.ACCEPTABLE, desc = "ok")
@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "!!!!")
@State
public class ConcurrencyTest {
	int num = 0;
	volatile boolean ready = false;

	@Actor
	public void actor1(I_Result r) {
		if (ready) {
			r.r1 = num + num;
		} else {
			r.r1 = 1;
		}
	}

	@Actor
	public void actor2(I_Result r) {
		num = 2;
		ready = true;
	}
}
```

```
*** INTERESTING tests
Some interesting behaviors observed. This is for the plain curiosity.
0 matching test results.
```

* 原理之 volatile

## volatile 原理
- volatile 的底層實作原理是記憶體屏障，Memory Barrier（Memory Fence）
  - 對 volatile 變數的寫入指令後會加入寫入屏障，防止寫入屏障之前的代表被重排序到屏障後面
  - 對 volatile 變數的讀指令前會加入讀取屏障，防止讀取屏障之後的代碼被重排序到屏障前面



### 1. 如何保證可見性

- 寫入屏障（sfence）保證在該屏障之前的，對共享變數的改動，都同步到主存當中

```java
public void actor2(I_Result r) {
	num = 2;
	ready = true; // ready 是 volatile 賦值帶寫屏障
	// 寫屏障
}
```

- 而讀屏障（lfence）保證在該屏障之後，對共享變數的讀取，載入的是主存中最新數據

```java
public void actor1(I_Result r) {
	// 讀屏障
	// ready 是 volatile 讀取值帶讀屏障
	if(ready) {
		r.r1 = num + num;
	} else {
		r.r1 = 1;
	}
}
```
![53](imgs/53.png)

### 2. 如何保證有序性
- 寫入屏障會確保指令重新排序時，不會將寫入屏障之前的程式碼排在寫入屏障之後

```java
public void actor2(I_Result r) {
	num = 2;
	ready = true; // ready 是 volatile 賦值帶寫屏障
	// 寫屏障
}
```

- 讀取屏障會確保指令重新排序時，不會將讀取屏障之後的程式碼排在讀取屏障之前

```java
public void actor1(I_Result r) {
	// 讀屏障
	// ready 是 volatile 讀取值帶讀屏障
	if(ready) {
		r.r1 = num + num;
	} else {
		r.r1 = 1;
	}
}
```

![54](imgs/54.png)


- volatile不能解決指令交錯：
  - 寫屏障只是保證之後的讀能夠讀到最新的結果，但不能保證讀跑到它前面去
  - 而有序性的保證也只是保證了本線程內相關程式碼不被重新排序

![55](imgs/55.png)

### 3. double-checked locking 問題

- 以著名的 double-checked locking 單例模式為例

```java
public final class Singleton {
	private Singleton() { }
	private static Singleton INSTANCE = null;
	public static Singleton getInstance() {
		if(INSTANCE == null) { // t2
			// 首次存取會同步，而之後的使用沒有 synchronized
			synchronized(Singleton.class) {
			if (INSTANCE == null) { // t1
				INSTANCE = new Singleton();
				}
			}
		}
		return INSTANCE;
	}
}
```

- 以上的實現特點是：
  - 懶惰實例化
  - 首次使用 getInstance() 才使用 synchronized 加鎖，後續使用時無需加鎖
  - 有隱含的，但很關鍵的一點：第一個 if 使用了 INSTANCE 變量，是在同步區塊之外


- 但在多執行緒環境下，上面的程式碼是有問題的，getInstance 方法對應的字節碼為：

```java
0: getstatic #2      // Field INSTANCE:Lcn/itcast/n5/Singleton;
3: ifnonnull 37
6: ldc #3            // class cn/itcast/n5/Singleton
8: dup
9: astore_0
10: monitorenter
11: getstatic #2      // Field INSTANCE:Lcn/itcast/n5/Singleton;
14: ifnonnull 27
17: new #3            // class cn/itcast/n5/Singleton
20: dup
21: invokespecial #4  // Method "<init>":()V
24: putstatic #2      // Field INSTANCE:Lcn/itcast/n5/Singleton;
27: aload_0
28: monitorexit
29: goto 37
32: astore_1
33: aload_0
34: monitorexit
35: aload_1
36: athrow
37: getstatic #2       // Field INSTANCE:Lcn/itcast/n5/Singleton;
40: areturn
```

其中
- 17 表示建立對象，將物件參考入棧 // new Singleton
- 20 表示複製一份物件參考 // 引用位址
- 21 表示利用一個物件引用，呼叫建構方法
- 24 表示利用一個物件引用，賦值給 static INSTANCE
- 也許 jvm 會優化為：先執行 24，再執行 21。 如果兩個執行緒 t1，t2 如下時間序列執行：

![56](imgs/56.png)

- 關鍵在於 0: getstatic 這行程式碼在 monitor 控制之外，它就像之前舉例中不守規則的人，可以越過 monitor 讀取INSTANCE 變數的值
- 這時 t1 還未完全將構造方法執行完畢，如果在構造方法中要執行很多初始化操作，那麼 t2 拿到的是將是一個未初始化完畢的單例
- 對 INSTANCE 使用 volatile 修飾即可，可以停用指令重排，但要注意在 JDK 5 以上的版本的 volatile 才會真正有效

### 4. double-checked locking 解決

```java
public final class Singleton {
	private Singleton() { }
	private static volatile Singleton INSTANCE = null;
	public static Singleton getInstance() {
		// 實例沒創建，才會進入內部的 synchronized程式碼區塊
		if (INSTANCE == null) {
			synchronized (Singleton.class) { // t2
				// 也許有其它線程已經創建實例，所以再判斷一次
				if (INSTANCE == null) { // t1
					INSTANCE = new Singleton();
				}
			}
		}
		return INSTANCE;
	}
}
```

- 字節碼上看不出來 volatile 指令的效果

```java
// -------------------------------------> 加入對 INSTANCE 變數的讀取屏障
0: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
3: ifnonnull 37
6: ldc #3 // class cn/itcast/n5/Singleton
8: dup
9: astore_0
10: monitorenter -----------------------> 保證原子性、可見性
11: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
14: ifnonnull 27
17: new #3 // class cn/itcast/n5/Singleton
20: dup
21: invokespecial #4 // Method "<init>":()V
24: putstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
// -------------------------------------> 加入對 INSTANCE 變數的寫入屏障
27: aload_0
28: monitorexit ------------------------> 保證原子性、可見性
29: goto 37
32: astore_1
33: aload_0
34: monitorexit
35: aload_1
36: athrow
37: getstatic #2 // Field INSTANCE:Lcn/itcast/n5/Singleton;
40: areturn
```


如上面的註解內容所示，讀寫 volatile 變數時會加入記憶體屏障（Memory Barrier（Memory Fence）），保證下面兩點：

- 可見性
  - 寫入屏障（sfence）保證在該屏障之前的 t1 對共享變數的改動，都同步到主存當中
  - 而讀屏障（lfence）保證在該屏障之後 t2 對共享變數的讀取，載入的是主存中最新數據
- 有序性
  - 寫入屏障會確保指令重新排序時，不會將寫入屏障之前的程式碼排在寫入屏障之後
  - 讀取屏障會確保指令重新排序時，不會將讀取屏障之後的程式碼排在讀取屏障之前
- 更底層是讀寫變數時使用 lock 指令來多核心 CPU 之間的可見性與有序性

![57](imgs/57.png)

### happens-before
happens-before 規定了對共享變數的寫入操作對其它線程的讀取操作可見，它是可見性與有序性的一套規則總結，拋開啟以下 happens-before 規則，JMM 並不能保證一個執行緒對共享變數的寫，對於其它執行緒對該共享變數的讀取可見

- 執行緒解鎖 m 之前對變數的寫，對於接下來對 m 加鎖的其它執行緒對該變數的讀取可見

```java
	static int x;
	static Object m = new Object();new Thread(()->
	{
		synchronized (m) {
			x = 10;
		}
	},"t1").start();new Thread(()->
	{
		synchronized (m) {
			System.out.println(x);
		}
	},"t2").start();
```

- 線程對 volatile 變數的寫，對接下來其它線程對該變數的讀可見

```java
	volatile static int x;new Thread(()->
	{
		x = 10;
	},"t1").start();new Thread(()->
	{
		System.out.println(x);
	},"t2").start();
```


- 執行緒 start 前對變數的寫，對該執行緒開始後對該變數的讀可見

```java
	static int x;x=10;new Thread(()->
	{
		System.out.println(x);
	},"t2").start();
```

- 線程結束前對變數的寫，對其它線程得知它結束後的讀可見（比如其它線程調用 t1.isAlive() 或 t1.join()等待它結束）


```java
	static int x;
	Thread t1 = new Thread(()->{
	x = 10;
	},"t1");
	t1.start();
	t1.join();
	System.out.println(x);
```


執行緒 t1 打斷 t2（interrupt）前對變數的寫，對於其他執行緒得知 t2 被打斷後對變數的讀可見（透過 t2.interrupted 或 t2.isInterrupted）

```java
	static int x;

	public static void main(String[] args) {
		Thread t2 = new Thread(() -> {
			while (true) {
				if (Thread.currentThread().isInterrupted()) {
					System.out.println(x);
					break;
				}
			}
		}, "t2");
		t2.start();
		new Thread(() -> {
			sleep(1);
			x = 10;
			t2.interrupt();
		}, "t1").start();
		while (!t2.isInterrupted()) {
			Thread.yield();
		}
		System.out.println(x);
	}
```

- 對變數預設值（0，false，null）的寫，對其它執行緒對該變數的讀可見
- 具有傳遞性，如果 x hb-> y 且 y hb-> z 那麼有 x hb-> z ，配合 volatile 的防指令重排，有下面的例子


```java
	volatile static int x;
	static int y;new Thread(()->
	{
		y = 10;
		x = 20;
	},"t1").start();new Thread(()->
	{
		// x=20 對 t2 可見, 同時 y=10 也對 t2 可見
		System.out.println(x);
	},"t2").start();
```

>- 變數都是指成員變數或靜態成員變數


### balking 模式習題
- 希望 doInit() 方法只被呼叫一次，下面的實作是否有問題，為什麼？

```java
public class TestVolatile {
	volatile boolean initialized = false;
	void init() {
		if (initialized) {
			return;
		}
		doInit();
		initialized = true;
	}
	private void doInit() {
	}
}
```

#### 線程安全單例習題
- 單例模式有很多實作方法，餓漢、懶漢、靜態內部類別、枚舉類，試分析每個實作下取得單例物件（即調用getInstance）時的線程安全，並思考註解中的問題
  >- 餓漢式：類別載入就會導致該單一實例物件被創建
  >- 懶漢式：類別載入不會導致該單一實例物件被創建，而是首次使用該物件時才會創建

```java
	// 問題1：為什麼要加 final
	// 問題2：如果實現了序列化介面, 還要做什麼來防止反序列化破壞單例
	public final class Singleton implements Serializable {
		// 問題3：為什麼設定為私有? 是否能防止反射創建新的實例?
		private Singleton() {
		}

		// 問題4：這樣初始化是否能保證單例物件建立時的執行緒安全?
		private static final Singleton INSTANCE = new Singleton();

		// 問題5：為什麼提供靜態方法而不是直接將 INSTANCE 設為 public, 說出你知道的理由
		public static Singleton getInstance() {
			return INSTANCE;
		}

		public Object readResolve() {
			return INSTANCE;
		}
	}

```

- 問題一:怕將來有子類會破壞他單利的方法
- 問題二:在反序列化的過程中，發現readResolve()返回了一個物件就會用這個物件
- 問題三:如果設置成public其他類都可以創建這個物件，就不能說他是個單例了，但不能防止反射創建新的實例
- 問題四:可以，因為靜態變數是在類加載時完成賦值得，透過JVM保證執行緒安全
- 問題五:

實現2:

```java
	// 問題1：枚舉單例是如何限制實例個數的
	// 問題2：列舉單例在建立時是否有並發問題
	// 問題3：枚舉單例能否被反射破壞單例
	// 問題4：枚舉單例能否被反序列化破壞單例
	// 問題5：列舉單例屬於懶漢式還是餓漢式
	// 問題6：枚舉單例如果希望加入一些單例建立時的初始化邏輯該如何做

	enum Singleton {
		INSTANCE;
	}
```

實現3：
```java
public final class Singleton {
	private Singleton() {
	}

	private static Singleton INSTANCE = null;

// 分析這裡的線程安全, 並說明有什麼缺點
	public static synchronized Singleton getInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}
		INSTANCE = new Singleton();
		return INSTANCE;
	}
}
```

實作4：DCL

```java
public final class Singleton {
	private Singleton() {
	}

// 問題1：解釋為什麼要加 volatile ?
	private static volatile Singleton INSTANCE = null;

// 問題2：對比實現3, 說出這樣做的意義
	public static Singleton getInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}
		synchronized (Singleton.class) {
// 問題3：為什麼還要在這裡加為空判斷, 之前不是判斷過了嗎
			if (INSTANCE != null) { // t2
				return INSTANCE;
			}
			INSTANCE = new Singleton();
			return INSTANCE;
		}
	}
}
```

- 問題一:因為有可能會發生指令從排序，導致INSTANCE先賦值再調用建構式，這樣如果有發生值執行緒上下文切換，第2個執行緒就會拿到不完整的INSTANCE，加了volatile可以防止發生重排序

實現5：

```java

public final class Singleton {
	private Singleton() {
	}

// 問題1：屬於懶漢式還是餓漢式
	private static class LazyHolder {
		static final Singleton INSTANCE = new Singleton();
	}

// 問題2：建立時是否有並發問題
	public static Singleton getInstance() {
		return LazyHolder.INSTANCE;
	}
}
```

- 問題一:因為靜態內部類，是使用時才會被類加載，所以屬於懶漢式
- 問題二:類加載對靜態變數的賦值操作，是有JVM保證執行緒安全的

# 共享模型之無鎖

## 1 問題提出
- 有以下需求，確保 ***account.withdraw*** 提款方法的執行緒安全

```java
package cn.itcast;

import java.util.ArrayList;
import java.util.List;

interface Account {
// 取得餘額
	Integer getBalance();

// 提款
	void withdraw(Integer amount);

	/**
	 * 方法內會啟動 1000 個線程，每個線程做 -10 元 的操作 如果初始餘額為 10000 那麼正確的結果應為 0
	 */
	static void demo(Account account) {
		List<Thread> ts = new ArrayList<>();
		long start = System.nanoTime();
		for (int i = 0; i < 1000; i++) {
			ts.add(new Thread(() -> {
				account.withdraw(10);
			}));
		}
		ts.forEach(Thread::start);
		ts.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		long end = System.nanoTime();
		System.out.println(account.getBalance() + " cost: " + (end - start) / 1000_000 + " ms");
	}
}
```

原有實作並不是線程安全的

```java
class AccountUnsafe implements Account {
	private Integer balance;

	public AccountUnsafe(Integer balance) {
		this.balance = balance;
	}

	@Override
	public Integer getBalance() {
		return balance;
	}

	@Override
	public void withdraw(Integer amount) {
		balance -= amount;
	}
}
```
執行測試程式碼

```java
public static void main(String[] args) {
	Account.demo(new AccountUnsafe(10000));
}
```

某次的執行結果

```java
330 cost: 306 ms
```

為什麼不安全
withdraw 方法

```java
public void withdraw(Integer amount) {
	balance -= amount;
}
```
對應的字節碼

```java
ALOAD 0 // <- this
ALOAD 0
GETFIELD cn/itcast/AccountUnsafe.balance : Ljava/lang/Integer; // <- this.balance
INVOKEVIRTUAL java/lang/Integer.intValue ()I // 拆箱
ALOAD 1 // <- amount
INVOKEVIRTUAL java/lang/Integer.intValue ()I // 拆箱
ISUB // 減法
INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer; // 結果包裝箱
PUTFIELD cn/itcast/AccountUnsafe.balance : Ljava/lang/Integer; // -> this.balance
```

多執行緒執行流程

```java
ALOAD 0 // thread-0 <- this
ALOAD 0
GETFIELD cn/itcast/AccountUnsafe.balance // thread-0 <- this.balance
INVOKEVIRTUAL java/lang/Integer.intValue // thread-0 拆箱
ALOAD 1 // thread-0 <- amount
INVOKEVIRTUAL java/lang/Integer.intValue // thread-0 拆箱
ISUB // thread-0 減法
INVOKESTATIC java/lang/Integer.valueOf // thread-0 結果裝箱
PUTFIELD cn/itcast/AccountUnsafe.balance // thread-0 -> this.balance
ALOAD 0 // thread-1 <- this
ALOAD 0
GETFIELD cn/itcast/AccountUnsafe.balance // thread-1 <- this.balance
INVOKEVIRTUAL java/lang/Integer.intValue // thread-1 拆箱
ALOAD 1 // thread-1 <- amount
INVOKEVIRTUAL java/lang/Integer.intValue // thread-1 拆箱
ISUB // thread-1 減法
INVOKESTATIC java/lang/Integer.valueOf // thread-1 結果裝箱
PUTFIELD cn/itcast/AccountUnsafe.balance // thread-1 -> this.balance
```

- 單核心的指令交錯
- 多核心的指令交錯

##　解決思路-鎖
－　首先想到的是給 Account 物件加鎖


```java
class AccountUnsafe implements Account {
	private Integer balance;

	public AccountUnsafe(Integer balance) {
		this.balance = balance;
	}

	@Override
	public synchronized Integer getBalance() {
		return balance;
	}

	@Override
	public synchronized void withdraw(Integer amount) {
		balance -= amount;
	}
}
```

結果為

```java
0 cost: 399 ms
```

## 解決思路-無鎖


```java
class AccountSafe implements Account {
	private AtomicInteger balance;

	public AccountSafe(Integer balance) {
		this.balance = new AtomicInteger(balance);
	}

	@Override
	public Integer getBalance() {
		return balance.get();
	}

	@Override
	public void withdraw(Integer amount) {
		while (true) {
			int prev = balance.get();
			int next = prev - amount;
			if (balance.compareAndSet(prev, next)) {
				break;
			}
		}
// 可以簡化為下面的方法
// balance.addAndGet(-1 * amount);
	}
}
```

執行測試程式碼

```java
public static void main(String[] args) {
	Account.demo(new AccountSafe(10000));
}
```


某次的執行結果


```java
0 cost: 302 ms
```

## 2 CAS 與 volatile

前面看到的 AtomicInteger 的解決方法，內部並沒有用鎖來保護共享變數的執行緒安全性。 那麼它是如何實現的呢？

```java
	public void withdraw(Integer amount) {
		while (true) {
			// 需要不斷嘗試，直到成功為止
			while (true) {
				// 例如拿到了舊值 1000
				int prev = balance.get();
				// 在這個基礎上 1000-10 = 990
				int next = prev - amount;
				/*
				 * compareAndSet 正是做這個檢查，在 set 前，先比較 prev 與當前值 - 不一致了，next 作廢，返回 false 表示失敗
				 * 例如，別的線程已經做了減法，當前值已經被減成了 990 那麼本線程的這次 990 就作廢了，進入 while 下次循環重試 - 一致，以 next
				 * 設定為新值，回傳 true 表示成功
				 */if (balance.compareAndSet(prev, next)) {
					break;
				}
			}
		}
	}
```

其中的關鍵是 compareAndSet，它的簡稱就是 CAS （也有 Compare And Swap 的說法），它必須是原子運算。


![58](imgs/58.png)


> 注意
>- 其實 CAS 的底層是 lock cmpxchg 指令（X86 架構），在單核心 CPU 和多核心 CPU 下都能夠保證【比較-交換】的原子性。
>- 在多核心狀態下，某個核心執行到帶有 lock 的指令時，CPU 會讓總線鎖住，當這個核把此指令執行完畢，再
開啟總線。 這個過程中不會被執行緒的調度機制打斷，確保了多個執行緒對記憶體操作的準確性，是原子
的。

### 慢動作分析

```java
@Slf4j
public class SlowMotion {
	public static void main(String[] args) {
		AtomicInteger balance = new AtomicInteger(10000);
		int mainPrev = balance.get();
		log.debug("try get {}", mainPrev);
		new Thread(() -> {
			sleep(1000);
			int prev = balance.get();
			balance.compareAndSet(prev, 9000);
			log.debug(balance.toString());
		}, "t1").start();
		sleep(2000);
		log.debug("try set 8000...");
		boolean isSuccess = balance.compareAndSet(mainPrev, 8000);
		log.debug("is success ? {}", isSuccess);
		if (!isSuccess) {
			mainPrev = balance.get();
			log.debug("try set 8000...");
			isSuccess = balance.compareAndSet(mainPrev, 8000);
			log.debug("is success ? {}", isSuccess);
		}
	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
```

```java
2019-10-13 11:28:37.134 [main] try get 10000
2019-10-13 11:28:38.154 [t1] 9000
2019-10-13 11:28:39.154 [main] try set 8000...
2019-10-13 11:28:39.154 [main] is success ? false
2019-10-13 11:28:39.154 [main] try set 8000...
2019-10-13 11:28:39.154 [main] is success ? true
```

### volatile

- 在取得共享變數時，為了確保該變數的可見性，需要使用 volatile 修飾。
- 它可以用來修飾成員變數和靜態成員變量，他可以避免執行緒從自己的工作快取中尋找變數的值，必須到主記憶體中取得它的值，線程操作 volatile 變數都是直接操作主存。 即一個線程對 volatile 變數的修改，對另一個線程可見。

>- 注意
>- volatile 僅僅保證了共享變數的可見性，讓它執行緒能夠看到最新值，但不能解決指令交錯問題（不能保證原
子性）
- CAS 必須藉助 volatile 才能讀取到共享變數的最新值來實現【比較並交換】的效果

### 為什麼無鎖效率高

- 無鎖情況下，即使重試失敗，執行緒始終在高速運行，沒有停歇，而 synchronized 會讓執行緒在沒有獲得鎖的時候，發生上下文切換，進入阻塞。 打個比喻
- 線程就好像高速跑道上的賽車，高速運行時，速度超快，一旦發生上下文切換，就好比賽車要減速、熄火，等被喚醒又得重新打火、啟動、加速... 恢復到高速運行，代價比較大
- 但無鎖情況下，因為線程要保持運行，需要額外 CPU 的支持，CPU 在這裡就好比高速跑道，沒有額外的跑道，線程想高速運行也無從談起，雖然不會進入阻塞，但由於沒有分到時間片，仍然會進入可運行狀態，還
是會導致上下文切換。

![59](imgs/59.png)


### CAS 的特點
- 結合 CAS 和 volatile 可以實現無鎖並發，適用於執行緒數少、多核心 CPU 的場景。
- CAS 是基於樂觀鎖的想法：最樂觀的估計，不怕別的線程來修改共享變量，就算改了也沒關係，我吃虧點再重試唄。
- synchronized 是基於悲觀鎖的想法：最悲觀的估計，得防著其它線程來修改共享變量，我上了鎖你們都別想改，我改完了解開鎖，你們才有機會。
- ***CAS 體現的是無鎖並發、無阻塞並發***，請仔細體會這兩句話的意思
  - 因為沒有使用 synchronized，所以線程不會陷入阻塞，這是效率提升的因素之一
  - 但如果競爭激烈，可以想到重試必然頻繁發生，反而效率會受影響


## 原子整数

- J.U.C 並發包提供了：
  - AtomicBoolean
  - AtomicInteger
  - AtomicLong
- 以 AtomicInteger 為例

```java
AtomicInteger i = new AtomicInteger(0);
// 取得並自增（i = 0, 結果 i = 1, 回傳 0），類似 i++
System.out.println(i.getAndIncrement());
// 自增並取得（i = 1, 結果 i = 2, 回傳 2），類似 ++i
System.out.println(i.incrementAndGet());
// 自減並取得（i = 2, 結果 i = 1, 回傳 1），類似 --i
System.out.println(i.decrementAndGet());
// 取得並自減（i = 1, 結果 i = 0, 回傳 1），類似 i--
System.out.println(i.getAndDecrement());
// 取得並加值（i = 0, 結果 i = 5, 回傳 0）
System.out.println(i.getAndAdd(5));
// 加值並取得（i = 5, 結果 i = 0, 回傳 0）
System.out.println(i.addAndGet(-5));
// 取得並更新（i = 0, p 為 i 的目前值, 結果 i = -2, 傳回 0）
// 其中函數中的操作能保證原子，但函數需要無副作用
System.out.println(i.getAndUpdate(p -> p - 2));
// 更新並取得（i = -2, p 為 i 的目前值, 結果 i = 0, 傳回 0）
// 其中函數中的操作能保證原子，但函數需要無副作用
System.out.println(i.updateAndGet(p -> p + 2));
// 取得並計算（i = 0, p 為 i 的目前值, x 為參數1, 結果 i = 10, 傳回 0）
// 其中函數中的操作能保證原子，但函數需要無副作用
// getAndUpdate 如果在 lambda 中引用了外部的局部變量，要確保該局部變數是 final 的
// getAndAccumulate 可以透過 參數1 來引用外部的局部變量，但因為其不在 lambda 中因此不必是 final
System.out.println(i.getAndAccumulate(10, (p, x) -> p + x));
// 計算並取得（i = 10, p 為 i 的目前值, x 為參數1, 結果 i = 0, 傳回 0）
// 其中函數中的操作能保證原子，但函數需要無副作用
System.out.println(i.accumulateAndGet(-10, (p, x) -> p + x));
```

## 原子引用
- 為什麼需要原子引用型別？
  - AtomicReference
  - AtomicMarkableReference
  - AtomicStampedReference
- 有以下方法

```java
public interface DecimalAccount {
	// 取得餘額
	BigDecimal getBalance();

	// 提款
	void withdraw(BigDecimal amount);

	/**
	 * 方法內會啟動 1000 個線程，每個線程做 -10 元 的操作 若初始餘額為 10000 那麼正確的結果應為 0
	 */
	static void demo(DecimalAccount account) {
		List<Thread> ts = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			ts.add(new Thread(() -> {
				account.withdraw(BigDecimal.TEN);
			}));
		}
		ts.forEach(Thread::start);
		ts.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		System.out.println(account.getBalance());
	}
}
```
- 試著提供不同的 DecimalAccount 實現，實現安全的提款操作

## 不安全實現

```java
class DecimalAccountUnsafe implements DecimalAccount {
	BigDecimal balance;

	public DecimalAccountUnsafe(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public BigDecimal getBalance() {
		return balance;
	}

	@Override
	public void withdraw(BigDecimal amount) {
		BigDecimal balance = this.getBalance();
		this.balance = balance.subtract(amount);
	}
}
```


## 安全實作-使用鎖

```java
class DecimalAccountSafeLock implements DecimalAccount {
	private final Object lock = new Object();
	BigDecimal balance;

	public DecimalAccountSafeLock(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public BigDecimal getBalance() {
		return balance;
	}

	@Override
	public void withdraw(BigDecimal amount) {
		synchronized (lock) {
			BigDecimal balance = this.getBalance();
			this.balance = balance.subtract(amount);
		}
	}
}
```


## 安全實作-使用 CAS

```java
class DecimalAccountSafeCas implements DecimalAccount {
	AtomicReference<BigDecimal> ref;

	public DecimalAccountSafeCas(BigDecimal balance) {
		ref = new AtomicReference<>(balance);
	}

	@Overridepublic
	BigDecimal getBalance() {
		return ref.get();
	}

	@Override
	public void withdraw(BigDecimal amount) {
		while (true) {
			BigDecimal prev = ref.get();
			BigDecimal next = prev.subtract(amount);
			if (ref.compareAndSet(prev, next)) {
				break;
			}
		}
	}
}
```
測試代碼
```java
DecimalAccount.demo(new DecimalAccountUnsafe(new BigDecimal("10000")));
DecimalAccount.demo(new DecimalAccountSafeLock(new BigDecimal("10000")));
DecimalAccount.demo(new DecimalAccountSafeCas(new BigDecimal("10000")));
```

運行結果

```java
4310 cost: 425 ms
0 cost: 285 ms
0 cost: 274 ms
```


## ABA 問題及解決
### ABA 問題


```java
	static AtomicReference<String> ref = new AtomicReference<>("A");

	public static void main(String[] args) throws InterruptedException {
		log.debug("main start...");
// 取得值 A
// 這個共享變數被它執行緒修改過？
		String prev = ref.get();
		other();
		sleep(1);
// 嘗試改為 C
		log.debug("change A->C {}", ref.compareAndSet(prev, "C"));
	}

	private static void other() {
		new Thread(() -> {
			log.debug("change A->B {}", ref.compareAndSet(ref.get(), "B"));
		}, "t1").start();
		sleep(0.5);
		new Thread(() -> {
			log.debug("change B->A {}", ref.compareAndSet(ref.get(), "A"));
		}, "t2").start();
	}
```

輸出

```java
11:29:52.325 c.Test36 [main] - main start...
11:29:52.379 c.Test36 [t1] - change A->B true
11:29:52.879 c.Test36 [t2] - change B->A true
11:29:53.880 c.Test36 [main] - change A->C true
```

- 主執行緒只能判斷共享變數的值與原先值 A 是否相同，不能感知到這種從 A 改為 B 又 改回 A 的情況，如果主執行緒
- 希望：
  - 只要有其它線程【動過了】共享變量，那麼自己的 cas 就算失敗，這時，僅比較值是不夠的，需要再加一個版本號


### AtomicStampedReference

```java
	static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

	public static void main(String[] args) throws InterruptedException {
		log.debug("main start...");
		// 取得值 A
		String prev = ref.getReference();
		// 取得版本號
		int stamp = ref.getStamp();
		log.debug("版本 {}", stamp);
		// 如果中間有其它線程幹擾，則發生了 ABA 現象
		other();
		sleep(1);
		// 嘗試改為 C
		log.debug("change A->C {}", ref.compareAndSet(prev, "C", stamp, stamp + 1));
	}

	private static void other() {
		new Thread(() -> {
			log.debug("change A->B {}", ref.compareAndSet(ref.getReference(), "B", ref.getStamp(), ref.getStamp() + 1));
			log.debug("更新版本為 {}", ref.getStamp());
		}, "t1").start();
		sleep(0.5);
		new Thread(() -> {
			log.debug("change B->A {}", ref.compareAndSet(ref.getReference(), "A", ref.getStamp(), ref.getStamp() + 1));
			log.debug("更新版本為 {}", ref.getStamp());
		}, "t2").start();
	}
```

```java
15:41:34.891 c.Test36 [main] - main start...
15:41:34.894 c.Test36 [main] - 版本 0
15:41:34.956 c.Test36 [t1] - change A->B true
15:41:34.956 c.Test36 [t1] - 更新版本為 1
15:41:35.457 c.Test36 [t2] - change B->A true
15:41:35.457 c.Test36 [t2] - 更新版本為 2
15:41:36.457 c.Test36 [main] - change A->C false
```

- AtomicStampedReference 可以為原子引用加上版本號，追蹤原子引用整個的變化過程，如： ***A -> B -> A ->C*** ，透過AtomicStampedReference，我們可以知道，引用變數中途被更改了幾次。
- 但有時候，並不關心引用變數更改了幾次，只是單純的關心是否更改過，所以就有了


AtomicMarkableReference

![60](imgs/60.png)


### AtomicMarkableReference


```java
class GarbageBag {
	String desc;

	public GarbageBag(String desc) {
		this.desc = desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return super.toString() + " " + desc;
	}
}

```

```java
@Slf4j
public class TestABAAtomicMarkableReference {
	public static void main(String[] args) throws InterruptedException {
		GarbageBag bag = new GarbageBag("裝滿了垃圾");
// 參數2 mark 可以看成一個標記，表示垃圾袋滿了
		AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag, true);
		log.debug("主線程 start...");
		GarbageBag prev = ref.getReference();
		log.debug(prev.toString());
		new Thread(() -> {
			log.debug("打掃的線程 start...");
			bag.setDesc("空垃圾袋");
			while (!ref.compareAndSet(bag, bag, true, false)) {
			}
			log.debug(bag.toString());
		}).start();
		Thread.sleep(1000);
		log.debug("主執行緒想換一個新垃圾袋？");
		boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);
		log.debug("換了麼？" + success);
		log.debug(ref.getReference().toString());
	}
}
```


```java
2019-10-13 15:30:09.264 [main] 主線程 start...
2019-10-13 15:30:09.270 [main] cn.itcast.GarbageBag@5f0fd5a0 裝滿了垃圾
2019-10-13 15:30:09.293 [Thread-1] 打掃的線程 start...
2019-10-13 15:30:09.294 [Thread-1] cn.itcast.GarbageBag@5f0fd5a0 空垃圾袋
2019-10-13 15:30:10.294 [main] 主執行緒想換一個新垃圾袋？
2019-10-13 15:30:10.294 [main] 換了呢？ false
2019-10-13 15:30:10.294 [main] cn.itcast.GarbageBag@5f0fd5a0 空垃圾袋
```

## 原子陣列

- AtomicIntegerArray
- AtomicLongArray
- AtomicReferenceArray



```java
	/**
	 * 參數1，提供數組、可以是線程不安全數組或線程安全數組 參數2，取得數組長度的方法 參數3，自增方法，回傳 array, index 參數4，列印數組的方法
	 */
	// supplier 提供者 無中生有 ()->結果
	// function 函數 一個參數一個結果 (參數)->結果 , BiFunction (參數1,參數2)->結果
	// consumer 消費者 一個參數沒結果 (參數)->void, BiConsumer (參數1,參數2)->
	private static <T> void demo(Supplier<T> arraySupplier, Function<T, Integer> lengthFun,
			BiConsumer<T, Integer> putConsumer, Consumer<T> printConsumer) {
		List<Thread> ts = new ArrayList<>();
		T array = arraySupplier.get();
		int length = lengthFun.apply(array);
		for (int i = 0; i < length; i++) {
			// 每個執行緒對數組作 10000 次操作
			ts.add(new Thread(() -> {
				for (int j = 0; j < 10000; j++) {
					putConsumer.accept(array, j % length);
				}
			}));
		}
		ts.forEach(t -> t.start()); // 啟動所有線程
		ts.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}); // 等所有執行緒結束
		printConsumer.accept(array);
	}
```


### 不安全陣列



```java
demo(
	()->new int[10],
	(array)->array.length,
	(array, index) -> array[index]++,
	array-> System.out.println(Arrays.toString(array))
);
````

```java
[9870, 9862, 9774, 9697, 9683, 9678, 9679, 9668, 9680, 9698]
```

### 安全陣列

```java
demo(
	()-> new AtomicIntegerArray(10),
	(array) -> array.length(),
	(array, index) -> array.getAndIncrement(index),
	array -> System.out.println(array)
);
```

```java
[10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000]
```

## 字段更新器

- AtomicReferenceFieldUpdater // 域 字段
- AtomicIntegerFieldUpdater
- AtomicLongFieldUpdater
 
利用欄位更新器，可以針對物件的某個域（Field）進行原子操作，只能配合 volatile 修飾的欄位使用，否則會出現例外

```java
Exception in thread "main" java.lang.IllegalArgumentException: Must be volatile type
```

```java
public class Test5 {
	private volatile int field;

	public static void main(String[] args) {
		AtomicIntegerFieldUpdater fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Test5.class, "field");
		Test5 test5 = new Test5();
		fieldUpdater.compareAndSet(test5, 0, 10);
// 修改成功 field = 10
		System.out.println(test5.field);
// 修改成功 field = 20
		fieldUpdater.compareAndSet(test5, 10, 20);
		System.out.println(test5.field);
// 修改失敗 field = 20
		fieldUpdater.compareAndSet(test5, 10, 30);
		System.out.println(test5.field);
	}
}
```

```java
10
20
20
```

## 原子累加器
### 累加器性能比較


```java
	private static <T> void demo(Supplier<T> adderSupplier, Consumer<T> action) {
		T adder = adderSupplier.get();
		long start = System.nanoTime();
		List<Thread> ts = new ArrayList<>();
		// 4 個線程，每人累加 50 萬
		for (int i = 0; i < 40; i++) {
			ts.add(new Thread(() -> {
				for (int j = 0; j < 500000; j++) {
					action.accept(adder);
				}
			}));
		}
		ts.forEach(t -> t.start());
		ts.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		long end = System.nanoTime();
		System.out.println(adder + " cost:" + (end - start) / 1000_000);
	}
```

比較 AtomicLong 與 LongAdder
```java
for (int i = 0; i < 5; i++) {
	demo(() -> new LongAdder(), adder -> adder.increment());
}
for (int i = 0; i < 5; i++) {
	demo(() -> new AtomicLong(), adder -> adder.getAndIncrement());
}
```

```java
1000000 cost:43
1000000 cost:9
1000000 cost:7
1000000 cost:7
1000000 cost:7
1000000 cost:31
1000000 cost:27
1000000 cost:28
1000000 cost:24
1000000 cost:22
```

效能提升的原因很簡單，就是在有競爭時，設定多個累加單元，Therad-0 累加 Cell[0]，而 Thread-1 累加Cell[1]... 最後將結果匯總。 這樣它們在累加時操作的不同的 Cell 變量，因此減少了 CAS 重試失敗，從而提高性能。


## 源碼之 LongAdder
- LongAdder 是並發大師 @author Doug Lea （大哥李）的作品，設計的非常精巧
- LongAdder 類別有幾個關鍵域

```java
// 累加單元數組, 懶惰初始化
transient volatile Cell[] cells;
// 基礎值, 如果沒有競爭, 則用 cas 累加這個域
transient volatile long base;
// 在 cells 建立或擴充時, 置為 1, 表示加鎖
transient volatile int cellsBusy;
```

## cas鎖

```java
//不要用於實作！ ！ ！
public class LockCas {
	private AtomicInteger state = new AtomicInteger(0);

	public void lock() {
		while (true) {
			if (state.compareAndSet(0, 1)) {
				break;
			}
		}
	}

	public void unlock() {
		log.debug("unlock...");
		state.set(0);
	}
}

```

測試

```java
	LockCas lock = new LockCas();new Thread(()->
	{
		log.debug("begin...");
		lock.lock();
		try {
			log.debug("lock...");
			sleep(1);
		} finally {
			lock.unlock();
		}
	}).start();new Thread(()->
	{
		log.debug("begin...");
		lock.lock();
		try {
			log.debug("lock...");
		} finally {
			lock.unlock();
		}
	}).start();
```

輸出

```java
18:27:07.198 c.Test42 [Thread-0] - begin...
18:27:07.202 c.Test42 [Thread-0] - lock...
18:27:07.198 c.Test42 [Thread-1] - begin...
18:27:08.204 c.Test42 [Thread-0] - unlock...
18:27:08.204 c.Test42 [Thread-1] - lock...
18:27:08.204 c.Test42 [Thread-1] - unlock...
```


## 原理之偽共享

其中 Cell 即為累加單元

```java
//防止快取行偽共享
@sun.misc.Contended
static final class Cell {
	volatile long value;

	Cell(long x) {
		value = x;
	}

//最重要的方法, 用來 cas 方式進行累加, prev 表示舊值, next 表示新值
	final boolean cas(long prev, long next) {
		return UNSAFE.compareAndSwapLong(this, valueOffset, prev, next);
	}
//省略不重要程式碼
}

```

得從緩存說起
快取與記憶體的速度比較

![61](imgs/61.png)

- 因為 CPU 與 記憶體的速度差異很大，需要靠預讀資料至快取來提升效率。
- 而緩存以緩存行為單位，每個緩存行對應著一塊內存，一般是 64 byte（8 個 long）
- 快取的加入會造成資料副本的產生，即同一份資料會快取在不同核心的快取行中
- CPU 要確保資料的一致性，如果某個 CPU 核心更改了數據，其它 CPU 核心對應的整個緩存行必須失效

![62](imgs/62.png)

- 因為 Cell 是陣列形式，在記憶體中是連續儲存的，一個 Cell 為 24 個位元組（16 個位元組的物件頭和 8 個位元組的 value），因此快取行可以存下 2 個的 Cell 物件。 這樣問題來了：
  - Core-0 要修改 Cell[0]
  - Core-1 要修改 Cell[1]
- 無論誰修改成功，都會導致對方 Core 的快取行失效，例如 Core-0 中 Cell[0]=6000, Cell[1]=8000 要累加Cell[0]=6001, Cell[1]=8000 ，這時會讓 Core-1 的快取行失效
- @sun.misc.Contended 用來解決這個問題，它的原理是在使用此註解的物件或欄位的前後各增加 128 位元組大小的padding，從而讓 CPU 將物件預讀至快取時佔用不同的快取行，這樣不會造成對方快取行的失效

![63](imgs/63.png)

累加主要呼叫下面的方法

```java
	public void add(long x) {
		// as 為累加單元數組
		// b 為基礎值
		// x 為累加值
		Cell[] as;
		長 b, v;
		int m;
		Cell a;
		// 進入 if 的兩個條件
		// 1. as 有值, 表示已經發生過競爭, 進入 if// 2. cas 給 base 累加時失敗了, 表示 base 發生了競爭, 進入 if
		if ((as = cells) != null || !casBase(b = base, b + x)) {
			// uncontended 表示 cell 沒有競爭
			boolean uncontended = true;
			if (
			// as 還沒有創建
			as == null || (m = as.length - 1) < 0 ||
			// 目前執行緒對應的 cell 還沒有
					(a = as[getProbe() & m]) == null ||
					// cas 給目前執行緒的 cell 累加失敗 uncontended=false ( a 為目前執行緒的 cell )
					!(uncontended = a.cas(v = a.value, v + x))) {
				// 進入 cell 陣列建立、cell 建立的流程
				longAccumulate(x, null, uncontended);
			}
		}
	}
```

![64](imgs/64.png)

```java
	final void longAccumulate(long x, LongBinaryOperator fn, boolean wasUncontended) {
		int h;
		// 目前執行緒還沒有對應的 cell, 需要隨機產生一個 h 值用來將目前執行緒綁定到 cell
		if ((h = getProbe()) == 0) {
			// 初始化 probe
			ThreadLocalRandom.current();
			// h 對應新的 probe 值, 用來對應 cell
			h = getProbe();
			wasUncontended = true;
		}
		// collide 為 true 表示需要擴容
		boolean collide = false;
		for (;;) {
			Cell[] as;
			Cell a;
			int n;
			long v;
			// 已經有了 cells
			if ((as = cells) != null && (n = as.length) > 0) {
				// 還沒有 cell
				if ((a = as[(n - 1) & h]) == null) {
					// 為 cellsBusy 加鎖, 建立 cell, cell 的初始累加值為 x
					// 成功則 break, 否則繼續 continue 循環
				}
				// 有競爭, 改變線程對應的 cell 來重試 cas
				else if (!wasUncontended)
					wasUncontended = true;
				// cas 嘗試累加, fn 配合 LongAccumulator 不為 null, 配合 LongAdder 為 null
				else if (a.cas(v = a.value, ((fn == null) ? v + x : fn.applyAsLong(v, x))))
					break;
				// 如果 cells 長度已經超過了最大長度, 或已經擴容, 改變線程對應的 cell 來重試 cas
				else if (n >= NCPU || cells != as)
					collide = false;
				// 確保 collide 為 false 進入此分支, 就不會進入下面的 else if 進行擴容了
				else if (!collide)
					collide = true;
				// 加鎖
				else if (cellsBusy == 0 && casCellsBusy()) {
					// 加鎖成功, 擴容
					continue;
				}
				// 改變線程對應的 cell
				h = advanceProbe(h);
			}
			// 還沒有 cells, 嘗試給 cellsBusy 加鎖
			else if (cellsBusy == 0 && cells == as && casCellsBusy()) {
				// 加鎖成功, 初始化 cells, 最開始長度為 2, 並填滿一個 cell
				// 成功則 break;
			}
			// 上兩種情況失敗, 嘗試給 base 累積
			else if (casBase(v = base, ((fn == null) ? v + x : fn.applyAsLong(v, x))))
				break;
		}
	}
```


longAccumulate流程圖

![65](imgs/65.png)
![66](imgs/66.png)

每個執行緒剛進入 longAccumulate 時，會嘗試對應一個 cell 物件（找到一個坑位）

![67](imgs/67.png)


取得最終結果通過 sum 方法

```java
	public long sum() {
		Cell[] as = cells;
		Cell a;
		long sum = base;
		if (as != null) {
			for (int i = 0; i < as.length; ++i) {
				if ((a = as[i]) != null)
					sum += a.value;
			}
		}
		return sum;
	}
```

## Unsafe

- 概述
  - Unsafe 物件提供了非常底層的，操作記憶體、執行緒的方法，Unsafe 物件不能直接調用，只能透過反射獲得

```java
public class UnsafeAccessor {
	static Unsafe unsafe;
	static {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			unsafe = (Unsafe) theUnsafe.get(null);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new Error(e);
		}
	}

	static Unsafe getUnsafe() {
		return unsafe;
	}
}

```

## Unsafe CAS 操作

```java
@Data
class Student {
	volatile int id;
	volatile String name;
}
```

```java
Unsafe unsafe = UnsafeAccessor.getUnsafe();
Field id = Student.class.getDeclaredField("id");
Field name = Student.class.getDeclaredField("name");
// 取得成員變數的偏移量
long idOffset = UnsafeAccessor.unsafe.objectFieldOffset(id);
long nameOffset = UnsafeAccessor.unsafe.objectFieldOffset(name);
Student student = new Student();
// 使用 cas 方法取代成員變數的值
UnsafeAccessor.unsafe.compareAndSwapInt(student, idOffset, 0, 20); // 回傳 true
UnsafeAccessor.unsafe.compareAndSwapObject(student, nameOffset, null, "張三"); // 回傳 true
System.out.println(student);
```

```java
Student(id=20, name=張三)
```

使用自訂的 AtomicData 實作之前線程安全的原子整數 Account 實現
Account 實

```java
class AtomicData {
	private volatile int data;
	static final Unsafe unsafe;
	static final long DATA_OFFSET;
	static {
		unsafe = UnsafeAccessor.getUnsafe();
		try {
// data 屬性在 DataContainer 物件中的偏移量，用於 Unsafe 直接存取該屬性
			DATA_OFFSET = unsafe.objectFieldOffset(AtomicData.class.getDeclaredField("data"));
		} catch (NoSuchFieldException e) {
			throw new Error(e);
		}
	}

	public AtomicData(int data) {
		this.data = data;
	}

	public void decrease(int amount) {
		int oldValue;
		while (true) {
// 取得共享變數舊值，可以在這一行加入斷點，修改 data 偵錯來加深理解
			oldValue = data;
// cas 嘗試修改 data 為 舊值 + amount，如果期間舊值被別的線程改了，回傳 false
			if (unsafe.compareAndSwapInt(this, DATA_OFFSET, oldValue, oldValue - amount)) {
				return;
			}
		}
	}

	public int getData() {
		return data;
	}
}
```


Account 實現


```java
		Account.demo(new Account() {
			AtomicData atomicData = new AtomicData(10000);

			@Override
			public Integer getBalance() {
				return atomicData.getData();
			}

			@Override
			public void withdraw(Integer amount) {
				atomicData.decrease(amount);
			}
		});
```

# 共享模型之不可變


## 日期轉換的問題
- 問題提出
  - 下面的程式碼在運行時，由於 SimpleDateFormat 不是線程安全的

```java
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				try {
					log.debug("{}", sdf.parse("1951-04-21"));
				} catch (Exception e) {
					log.error("{}", e);
				}
			}).start();
		}
```
有很大幾率出現 java.lang.NumberFormatException 或出現不正確的日期解析結果，例如：

```java
19:10:40.859 [Thread-2] c.TestDateParse - {}
java.lang.NumberFormatException: For input string: ""
at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
at java.lang.Long.parseLong(Long.java:601)
at java.lang.Long.parseLong(Long.java:631)
at java.text.DigitList.getLong(DigitList.java:195)
at java.text.DecimalFormat.parse(DecimalFormat.java:2084)
at java.text.SimpleDateFormat.subParse(SimpleDateFormat.java:2162)
at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1514)
at java.text.DateFormat.parse(DateFormat.java:364)
at cn.itcast.n7.TestDateParse.lambda$test1$0(TestDateParse.java:18)
at java.lang.Thread.run(Thread.java:748)
19:10:40.859 [Thread-1] c.TestDateParse - {}
java.lang.NumberFormatException: empty String
at sun.misc.FloatingDecimal.readJavaFormatString(FloatingDecimal.java:1842)
at sun.misc.FloatingDecimal.parseDouble(FloatingDecimal.java:110)
at java.lang.Double.parseDouble(Double.java:538)
at java.text.DigitList.getDouble(DigitList.java:169)
at java.text.DecimalFormat.parse(DecimalFormat.java:2089)
at java.text.SimpleDateFormat.subParse(SimpleDateFormat.java:2162)
at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1514)
at java.text.DateFormat.parse(DateFormat.java:364)
at cn.itcast.n7.TestDateParse.lambda$test1$0(TestDateParse.java:18)
at java.lang.Thread.run(Thread.java:748)
19:10:40.857 [Thread-8] c.TestDateParse - Sat Apr 21 00:00:00 CST 1951
19:10:40.857 [Thread-9] c.TestDateParse - Sat Apr 21 00:00:00 CST 1951
19:10:40.857 [Thread-6] c.TestDateParse - Sat Apr 21 00:00:00 CST 1951
19:10:40.857 [Thread-4] c.TestDateParse - Sat Apr 21 00:00:00 CST 1951
19:10:40.857 [Thread-5] c.TestDateParse - Mon Apr 21 00:00:00 CST 178960645
19:10:40.857 [Thread-0] c.TestDateParse - Sat Apr 21 00:00:00 CST 1951
19:10:40.857 [Thread-7] c.TestDateParse - Sat Apr 21 00:00:00 CST 1951
19:10:40.857 [Thread-3] c.TestDateParse - Sat Apr 21 00:00:00 CST 1951
```

- 思路 - 同步鎖
  - 這樣雖能解決問題，但帶來的是效能上的損失，並不算很好：

```java
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < 50; i++) {
			new Thread(() -> {
				synchronized (sdf) {
					try {
						log.debug("{}", sdf.parse("1951-04-21"));
					} catch (Exception e) {
						log.error("{}", e);
					}
				}
			}).start();
		}
```

- 思路 - 不可變
  - 如果一個物件在不能夠修改其內部狀態（屬性），那麼它就是線程安全的，因為不存在並發修改啊！ 這樣的對像在Java 中有很多，例如在 Java 8 後，提供了一個新的日期格式化類別：

```java
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				LocalDate date = dtf.parse("2018-10-01", LocalDate::from);
				log.debug("{}", date);
			}).start();
		}
```

可以看 DateTimeFormatter 的文件：

```java
@implSpec
This class is immutable and thread-safe.
```
不可變對象，實際上是另一種避免競爭的方式。


- 不可變設計
  - 另一個大家較為熟悉的 String 類別也是不可變的，以它為例，說明一下不可變設計的要素

```java
public final class String implements java.io.Serializable, Comparable<String>, CharSequence {
	/** The value is used for character storage. */
	private final char value[];
	/** Cache the hash code for the string */
	private int hash; // Default to 0
// ...
}
```

## final 的使用
- 發現該類別、類別中所有屬性都是 final 的
  - 屬性用 final 修飾保證了該屬性是唯讀的，不能修改
  - 類別以 final 修飾保證了該類別中的方法不能被覆蓋，防止子類別無意間破壞不可變性
- 保護性拷貝
  - 但有同學會說，使用字串時，也有一些跟修改相關的方法啊，比如 substring 等，那麼下面就看一看這些方法是如何實現的，以 substring 為例：

```java
	public String substring(int beginIndex) {
		if (beginIndex < 0) {
			throw new StringIndexOutOfBoundsException(beginIndex);
		}
		int subLen = value.length - beginIndex;
		if (subLen < 0) {
			throw new StringIndexOutOfBoundsException(subLen);
		}
		return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);
	}
```

發現其內部是呼叫 String 的建構方法創建了一個新字串，再進入這個構造看看，是否對 final char[] value 做出了修改：

```java
    /**
     * Allocates a new {@code String} that contains characters from a subarray
     * of the character array argument. The {@code offset} argument is the
     * index of the first character of the subarray and the {@code count}
     * argument specifies the length of the subarray. The contents of the
     * subarray are copied; subsequent modification of the character array does
     * not affect the newly created string.
     *
     * @param  value
     *         Array that is the source of characters
     *
     * @param  offset
     *         The initial offset
     *
     * @param  count
     *         The length
     *
     * @throws  IndexOutOfBoundsException
     *          If the {@code offset} and {@code count} arguments index
     *          characters outside the bounds of the {@code value} array
     */
    public String(char value[], int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count <= 0) {
            if (count < 0) {
                throw new StringIndexOutOfBoundsException(count);
            }
            if (offset <= value.length) {
                this.value = "".value;
                return;
            }
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > value.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }
        this.value = Arrays.copyOfRange(value, offset, offset+count);
    }
```

結果發現也沒有，建構新字串物件時，會產生新的 char[] value，對內容進行複製 。 這種透過創建副本物件來避免共享的手段稱為【保護性拷貝（defensive copy）】


# 模式之享元

## 1. 簡介
- 定義 英文名稱：Flyweight pattern. 當需要重複使用數量有限的相同類別物件時
> wikipedia： A flyweight is an object that minimizes memory usage by sharing as much data as
possible with other similar objects
- 來自 "Gang of Four" design patterns
- 歸類 Structual patterns

## 2. 體現
### 2.1 包裝類
在JDK中 Boolean，Byte，Short，Integer，Long，Character 等包裝類別提供了 valueOf 方法，例如 Long 的
valueOf 會快取 -128~127 之間的 Long 對象，在這個範圍之間會重複使用對象，大於這個範圍，才會新建 Long 對
象：

```java
	public static Long valueOf(long l) {
		final int offset = 128;
		if (l >= -128 && l <= 127) { // will cache
			return LongCache.cache[(int) l + offset];
		}
		return new Long(l);
	}

```

> 注意：
>- Byte, Short, Long 快取的範圍都是 -128~127
>- Character 快取的範圍是 0~127
>- Integer的預設範圍是 -128~127
>	- 最小值不能改變
>	- 但最大值可以透過調整虛擬機器參數 `
>- -Djava.lang.Integer.IntegerCache.high` 來改變
Boolean 快取了 TRUE 和 FALSE

# 原理之 final

## 1. 設定 final 變數的原理
- 了解 volatile 原理，再對比 final 的實作就比較簡單了

```java
public class TestFinal {
	final int a = 20;
}
```

字節碼

```java
0: aload_0
1: invokespecial #1 // Method java/lang/Object."<init>":()V
4: aload_0
5: bipush 20
7: putfield #2 // Field a:I
<-- 写屏障
10: return
```

發現 final 變數的賦值也會透過 putfield 指令來完成，同樣在這條指令之後也會加入寫屏障，保證在其它線程讀到它的值時不會出現為 0 的情況
## 2. 取得 final 變數的原理


[共享模型之工具](mutiThreadTools.md )



