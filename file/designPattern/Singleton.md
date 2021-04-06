# 單利模式(Singleton)

所謂類的單例設計模式，就是採取一定的方法保證在整個軟體係統中，對某個類<font color="#f00">只能存在一個物件實例</font>，並起該類只提供一個取得其物件實例的方法(靜態方法)

條件:
1.有一個private 的無參建構式
2.使用一個public方法對外提供物件實例(通常是靜態方法)

創建單利模式的方法

## 1.餓漢式

### 如果確定此類一定會被用到一次，那此方式可以使用，不會造成內存浪費

 * 優點:簡單，執行續安全，推薦使用
 * 缺點:如果此物件不一定會被使用，會造成內存浪費問題

```java
public class Singleton1 {
	private static final Singleton1 INSTANCE= new Singleton1();

	//私有建構式
	private Singleton1() {}

	public static Singleton1 getInstance() {
		return Singleton1.INSTANCE;
	}

}
```

或

```java
public class Singleton2 {

	//私有建構式
	private Singleton2() {}

	private static Singleton2 instance;

	static {
		instance = new Singleton2();
	}

	public static Singleton2 getInstance() {
		return Singleton2.instance;
	}
}
```

## 2.懶漢式Lazy

### 當物件第一次被使用才創建出來

第一種方式:
 * 優點:實現Lazy Loading
 * 缺點:有執行緒安全問題，如果有多個現成通過IF判斷，但物件還沒被NEW出來，就會NEW出多個物件

```java
public class Singleton3 {
	private Singleton3() {}

	private static Singleton3 instance;

	public static Singleton3 getInstance() {
		if(instance == null) {//此處會發生執行緒安全問題
			instance = new Singleton3();
		}
		return instance;
	}

}
```

第二種方式:

 * 優點:實現Lazy Loading ，沒有執行緒安全問題
 * 缺點:因為加了synchronized，但實例化物件只需做一次，但家在方法上每次都會等有效率問題

```java
public class Singleton4 {
	private Singleton4() {}

	private static Singleton4 instance;

	public synchronized static Singleton4 getInstance() {
		if(instance == null) {
			instance = new Singleton4();
		}
		return instance;
	}
}
```

## double check

### 在同步代碼區塊哩，再判斷一次物件有沒有被實例化

 * 優點:實現Lazy Loading ，沒有執行緒安全問題，推薦使用


```java
public class Singleton5 {
	private Singleton5() {}

	private static Singleton5 instance;

	public static Singleton5 getInstance() {
		if(instance == null) {//之後進來的執行緒，就不須再執行IF區塊代碼，處理synchronized效率問題
			synchronized (Singleton5.class) {
				if(instance == null) {//在判別一次，解決懶漢式執行緒安全問題
					instance = new Singleton5();
				}
			}
		}

		return Singleton5.instance;
	}

}
```

## 靜態內部類方式

 * 1.類的靜態屬性只會在第一次加載的時候初始化，在這裡JVM保證了執行緒安全，在類進行初始化時，別的執行緒是無法進入的
 * 2.利用了靜態內部類的特性，實現了Lazy Loading

```java
public class Singleton6 {
  private Singleton6(){}

  private static class Singleton{//靜態內部類在外部類加載時，不會被加載，所以實現了Lazy Loading
    private static final Singleton6 INSTANCE = new Singleton6();
  }

  public Singleton6 getInstance(){//當此方法被調用時，才加載靜態內部類
    return Singleton.INSTANCE;
  }
}

```

## 使用enum方式

 * 此方法是JAVA作者推薦方式

```java
public class Singleton7 {
	public static void main(String[] args) {
		Singleton instance = Singleton.INSTANCE;
		Singleton instance1 = Singleton.INSTANCE;

		System.out.println(instance == instance1);
		System.out.println(instance.hashCode());
		System.out.println(instance1.hashCode());

		instance.sayOK();
	}
}

enum Singleton{
	INSTANCE;
	public void sayOK() {
		System.out.println("OK");
	}


}
```







