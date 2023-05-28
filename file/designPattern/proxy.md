# 代理模式
## 1 概述
由於某些原因需要給某對象提供一個代理以控制對該對象的訪問。這時，訪問對像不適合或者不能直接引用目標對象，代理對像作為訪問對象和目標對象之間的中介。

Java中的代理按照代理類生成時機不同又分為靜態代理和動態代理。靜態代理代理類在編譯期就生成，而動態代理代理類則是在Java運行時動態生成。動態代理又有JDK代理和CGLib代理兩種。

## 2 結構
代理（Proxy）模式分為三種角色：

抽象主題（Subject）類： 通過接口或抽像類聲明真實主題和代理對象實現的業務方法。
真實主題（Real Subject）類： 實現了抽象主題中的具體業務，是代理對象所代表的真實對象，是最終要引用的對象。
代理（Proxy）類： 提供了與真實主題相同的接口，其內部含有對真實主題的引用，它可以訪問、控製或擴展真實主題的功能。
## 3 靜態代理
我們通過案例來感受一下靜態代理。

【例】火車站賣票

如果要買火車票的話，需要去火車站買票，坐車到火車站，排隊等一系列的操作，顯然比較麻煩。而火車站在多個地方都有代售點，我們去代售點買票就方便很多了。這個例子其實就是典型的代理模式，火車站是目標對象，代售點是代理對象。類圖如下：

![035](files/21.png)

```java
//賣票接口
public  interface  SellTickets {
    void  sell ();
}
​
//火車站火車站具有賣票功能，所以需要實現SellTickets接口
public  class  TrainStation  implements  SellTickets {
​
    public  void  sell () {
        System.out.println ( "火車站賣票" );
    }
}
​
//代售點
public  class  ProxyPoint  implements  SellTickets {
​
    private  TrainStation  station  =  new  TrainStation ();
​
    public  void  sell () {
        System.out.println ( "代理點收取一些服務費用" );
        station.sell ();
    }
}
​
//測試類
public  class  Client {
    public  static  void  main ( String [] args ) {
        ProxyPoint  pp  =  new  ProxyPoint ();
        pp.sell ();
    }
}
```

- 從上面代碼中可以看出測試類直接訪問的是ProxyPoint類對象，也就是說ProxyPoint作為訪問對象和目標對象的中介。同時也對sell方法進行了增強（代理點收取一些服務費用）。

## 4 JDK動態代理
- 接下來我們使用動態代理實現上面案例，先說說JDK提供的動態代理。Java中提供了一個動態代理類Proxy，Proxy並不是我們上述所說的代理對象的類，而是提供了一個創建代理對象的靜態方法（newProxyInstance方法）來獲取代理對象。

```java
//賣票接口
public  interface  SellTickets {
    void  sell ();
}
​
//火車站火車站具有賣票功能，所以需要實現SellTickets接口
public  class  TrainStation  implements  SellTickets {
​
    public  void  sell () {
        System.out.println ( "火車站賣票" );
    }
}
​
//代理工廠，用來創建代理對象
public  class  ProxyFactory {
​
    private  TrainStation  station  =  new  TrainStation ();
​
    public  SellTickets  getProxyObject () {
        //使用Proxy獲取代理對象
        /*
            newProxyInstance()方法參數說明：
                ClassLoader loader ： 類加載器，用於加載代理類，使用真實對象的類加載器即可
                Class<?>[] interfaces ： 真實對象所實現的接口，代理模式真實對象和代理對象實現相同的接口
                InvocationHandler h ： 代理對象的調用處理程序
         */
        SellTickets  sellTickets  = ( SellTickets ) Proxy.newProxyInstance ( station.getClass (). getClassLoader (),
                station.getClass (). getInterfaces (),
                new  InvocationHandler () {
                    /*
                        InvocationHandler中invoke方法參數說明：
                            proxy ： 代理對象
                            method ： 對應於在代理對像上調用的接口方法的Method 實例
                            args ： 代理對象調用接口方法時傳遞的實際參數
                     */
                    public  Object  invoke ( Object  proxy , Method  method , Object [] args ) throws  Throwable {
​
                        System.out.println ( "代理點收取一些服務費用(JDK動態代理方式)" );
                        //執行真實對象
                        Object  result  =  method.invoke ( station , args );
                        return  result ;
                    }
                });
        return  sellTickets ;
    }
}
​
//測試類
public  class  Client {
    public  static  void  main ( String [] args ) {
        //獲取代理對象
        ProxyFactory  factory  =  new  ProxyFactory ();
        
        SellTickets  proxyObject  =  factory.getProxyObject ();
        proxyObject.sell ();
    }
}
```

- 使用了動態代理，我們思考下面問題：

  - ProxyFactory是代理類嗎？

  - ProxyFactory不是代理模式中所說的代理類，而代理類是程序在運行過程中動態的在內存中生成的類。通過阿里巴巴開源的Java 診斷工具（Arthas【阿爾薩斯】）查看代理類的結構：

```java
package  com.sun.proxy ;
​
import  com.itheima.proxy.dynamic.jdk.SellTickets ;
import  java.lang.reflect.InvocationHandler ;
import  java.lang.reflect.Method ;
import  java.lang.reflect.Proxy ;
import  java.lang.reflect.UndeclaredThrowableException ;
​
public  final  class  $Proxy0  extends  Proxy  implements  SellTickets {
    private  static  Method  m1 ;
    private  static  Method  m2 ;
    private  static  Method  m3 ;
    private  static  Method  m0 ;
​
    public  $Proxy0 ( InvocationHandler  invocationHandler ) {
        super ( invocationHandler );
    }
​
    static {
        try {
            m1  =  Class.forName ( "java.lang.Object" ). getMethod ( "equals" , Class.forName ( "java.lang.Object" ));
            m2  =  Class.forName ( "java.lang.Object" ). getMethod ( "toString" , new  Class [ 0 ]);
            m3  =  Class.forName ( "com.itheima.proxy.dynamic.jdk.SellTickets" ). getMethod ( "sell" , new  Class [ 0 ]);
            m0  =  Class.forName ( "java.lang.Object" ). getMethod ( "hashCode" , new  Class [ 0 ]);
            return ;
        }
        catch ( NoSuchMethodException  noSuchMethodException ) {
            throw  new  NoSuchMethodError ( noSuchMethodException.getMessage ());
        }
        catch ( ClassNotFoundException  classNotFoundException ) {
            throw  new  NoClassDefFoundError ( classNotFoundException.getMessage ());
        }
    }
​
    public  final  boolean  equals ( Object  object ) {
        try {
            return ( Boolean ) this.h.invoke ( this , m1 , new  Object []{ object });
        }
        catch ( Error  |  RuntimeException  throwable ) {
            throw  throwable ;
        }
        catch ( Throwable  throwable ) {
            throw  new  UndeclaredThrowableException ( throwable );
        }
    }
​
    public  final  String  toString () {
        try {
            return ( String ) this.h.invoke ( this , m2 , null );
        }
        catch ( Error  |  RuntimeException  throwable ) {
            throw  throwable ;
        }
        catch ( Throwable  throwable ) {
            throw  new  UndeclaredThrowableException ( throwable );
        }
    }
​
    public  final  int  hashCode () {
        try {
            return ( Integer ) this.h.invoke ( this , m0 , null );
        }
        catch ( Error  |  RuntimeException  throwable ) {
            throw  throwable ;
        }
        catch ( Throwable  throwable ) {
            throw  new  UndeclaredThrowableException ( throwable );
        }
    }
​
    public  final  void  sell () {
        try {
            this.h.invoke ( this , m3 , null );
            return ;
        }
        catch ( Error  |  RuntimeException  throwable ) {
            throw  throwable ;
        }
        catch ( Throwable  throwable ) {
            throw  new  UndeclaredThrowableException ( throwable );
        }
    }
}
```

- 從上面的類中，我們可以看到以下幾個信息：

  - 代理類（$Proxy0）實現了SellTickets。這也就印證了我們之前說的真實類和代理類實現同樣的接口。
  - 代理類（$Proxy0）將我們提供了的匿名內部類對像傳遞給了父類。

動態代理的執行流程是什麼樣？

下面是摘取的重點代碼：

```java
//程序運行過程中動態生成的代理類
public  final  class  $Proxy0  extends  Proxy  implements  SellTickets {
    private  static  Method  m3 ;
​
    public  $Proxy0 ( InvocationHandler  invocationHandler ) {
        super ( invocationHandler );
    }
​
    static {
        m3  =  Class.forName ( "com.itheima.proxy.dynamic.jdk.SellTickets" ). getMethod ( "sell" , new  Class [ 0 ]);
    }
​
    public  final  void  sell () {
        this.h.invoke ( this , m3 , null );
    }
}
​
//Java提供的動態代理相關類
public  class  Proxy  implements  java.io.Serializable {
    protected  InvocationHandler  h ;
     
    protected  Proxy ( InvocationHandler  h ) {
        this.h  =  h ;
    }
}
​
//代理工廠類
public  class  ProxyFactory {
​
    private  TrainStation  station  =  new  TrainStation ();
​
    public  SellTickets  getProxyObject () {
        SellTickets  sellTickets  = ( SellTickets ) Proxy.newProxyInstance ( station.getClass (). getClassLoader (),
                station.getClass (). getInterfaces (),
                new  InvocationHandler () {
                    
                    public  Object  invoke ( Object  proxy , Method  method , Object [] args ) throws  Throwable {
​
                        System.out.println ( "代理點收取一些服務費用(JDK動態代理方式)" );
                        Object  result  =  method.invoke ( station , args );
                        return  result ;
                    }
                });
        return  sellTickets ;
    }
}
​
​
//測試訪問類
public  class  Client {
    public  static  void  main ( String [] args ) {
        //獲取代理對象
        ProxyFactory  factory  =  new  ProxyFactory ();
        SellTickets  proxyObject  =  factory.getProxyObject ();
        proxyObject.sell ();
    }
}
```

- 執行流程如下：

  - 在測試類中通過代理對象調用sell()方法
  - 根據多態的特性，執行的是代理類（$Proxy0）中的sell()方法
  - 代理類（$Proxy0）中的sell()方法中又調用了InvocationHandler接口的子實現類對象的invoke方法
  - invoke方法通過反射執行了真實對象所屬類(TrainStation)中的sell()方法

## 5 CGLIB動態代理
同樣是上面的案例，我們再次使用CGLIB代理實現。

- 如果沒有定義SellTickets接口，只定義了TrainStation(火車站類)。很顯然JDK代理是無法使用了，因為JDK動態代理要求必須定義接口，對接口進行代理。

- CGLIB是一個功能強大，高性能的代碼生成包。它為沒有實現接口的類提供代理，為JDK的動態代理提供了很好的補充。

- CGLIB是第三方提供的包，所以需要引入jar包的坐標：
  
```xml
< dependency >
    < groupId > cglib </ groupId >
    < artifactId > cglib </ artifactId >
    < version > 2.2.2 </ version >
</ dependency >
```

代碼如下：

```java

//火車站
public  class  TrainStation {
​
    public  void  sell () {
        System.out.println ( "火車站賣票" );
    }
}
​
//代理工廠
public  class  ProxyFactory  implements  MethodInterceptor {
​
    private  TrainStation  target  =  new  TrainStation ();
​
    public  TrainStation  getProxyObject () {
        //創建Enhancer對象，類似於JDK動態代理的Proxy類，下一步就是設置幾個參數
        Enhancer  enhancer  = new  Enhancer ();
        //設置父類的字節碼對象
        enhancer.setSuperclass ( target.getClass ());
        //設置回調函數
        enhancer.setCallback ( this );
        //創建代理對象
        TrainStation  obj  = ( TrainStation ) enhancer.create ();
        return  obj ;
    }
​
    /*
        intercept方法參數說明：
            o ： 代理對象
            method ： 真實對像中的方法的Method實例
            args ： 實際參數
            methodProxy ：代理對像中的方法的method實例
     */
    public  TrainStation  intercept ( Object  o , Method  method , Object [] args , MethodProxy  methodProxy ) throws  Throwable {
        System.out.println ( "代理點收取一些服務費用(CGLIB動態代理方式)" );
        TrainStation  result  = ( TrainStation ) methodProxy.invokeSuper ( o , args );
        return  result ;
    }
}
​
//測試類
public  class  Client {
    public  static  void  main ( String [] args ) {
        //創建代理工廠對象
        ProxyFactory  factory  =  new  ProxyFactory ();
        //獲取代理對象
        TrainStation  proxyObject  =  factory.getProxyObject ();
​
        proxyObject.sell ();
    }
}
```

## 6 三種代理的對比
- jdk代理和CGLIB代理

  - 使用CGLib實現動態代理，CGLib底層採用ASM字節碼生成框架，使用字節碼技術生成代理類，在JDK1.6之前比使用Java反射效率要高。唯一需要注意的是，CGLib不能對聲明為final的類或者方法進行代理，因為CGLib原理是動態生成被代理類的子類。

  - 在JDK1.6、JDK1.7、JDK1.8逐步對JDK動態代理優化之後，在調用次數較少的情況下，JDK代理效率高於CGLib代理效率，只有當進行大量調用的時候，JDK1.6和JDK1.7比CGLib代理效率低一點，但是到JDK1.8的時候，JDK代理效率高於CGLib代理。所以如果有接口使用JDK動態代理，如果沒有接口使用CGLIB代理。

- 動態代理和靜態代理

  - 動態代理與靜態代理相比較，最大的好處是接口中聲明的所有方法都被轉移到調用處理器一個集中的方法中處理（InvocationHandler.invoke）。這樣，在接口方法數量比較多的時候，我們可以進行靈活處理，而不需要像靜態代理那樣每一個方法進行中轉。

  - 如果接口增加一個方法，靜態代理模式除了所有實現類需要實現這個方法外，所有代理類也需要實現此方法。增加了代碼維護的複雜度。而動態代理不會出現該問題

 

## 7 優缺點
- 優點：

  - 代理模式在客戶端與目標對象之間起到一個中介作用和保護目標對象的作用；
  - 代理對象可以擴展目標對象的功能；
  - 代理模式能將客戶端與目標對象分離，在一定程度上降低了系統的耦合度；
  
- 缺點：

  - 增加了系統的複雜度；

## 8 使用場景
- 遠程（Remote）代理

  - 本地服務通過網絡請求遠程服務。為了實現本地到遠程的通信，我們需要實現網絡通信，處理其中可能的異常。為良好的代碼設計和可維護性，我們將網絡通信部分隱藏起來，只暴露給本地服務一個接口，通過該接口即可訪問遠程服務提供的功能，而不必過多關心通信部分的細節。

- 防火牆（Firewall）代理

  - 當你將瀏覽器配置成使用代理功能時，防火牆就將你的瀏覽器的請求轉給互聯網；當互聯網返迴響應時，代理服務器再把它轉給你的瀏覽器。

- 保護（Protect or Access）代理

  - 控制對一個對象的訪問，如果需要，可以給不同的用戶提供不同級別的使用權限。