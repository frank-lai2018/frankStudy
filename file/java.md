# 1.JAVA物件導向3大特性

1. 封裝(Encapsulation):將物件的狀態信息隱藏隱藏在物件內不，也就是成員屬性設成private，再提供public 的getter setter方法讓外界存取

2. 繼承:實現程式重複使用的手段

3. 多型:上層型別宣各的變數，參考到下層實體類別建立的物件

4. 方法override時，要都是類方法，或都是靜態方法，不能一個是類方法一個是靜態方法


# 2.Static Initialization Block and  Initialization Block:


1. ## Static Initialization Block and  Initialization Block:

   **普通初始化區塊負責對物建進行初始化**

   **靜態初始化區塊負責對類加載時對類進行初始化**

   **靜態初始化區塊只能對靜態成員進行處理**

   **靜態初始化區塊屬於靜態成員，所以遵循其規則(不能訪問非static 的變數或方法)**

   **當類被創建時，會先執行類的屬性根跟初始化區塊，期規則依程式寫的順序執行**

   **實際上初始化區塊是個假象，當編譯完成後，其初始化區快會被填進每個建構式中的最前面**

   

   1. 執行順序:

      Static Initialization Block --> Initialization Block --> constructor

   2. 當有父類時:

      ​      **父** Static Initialization Block 

      -->**子** Static Initialization Block 

      -->**父** Initialization Block 

      -->**父** constructor 

      -->**子** Initialization Block 

      -->**子** constructor

      

   3. 區別

      - static block前有static關鍵字，但non-static block沒有。
      - static block不論建構了多少實例，只會在第一次建構前執行一次，non-static block則是每次建構都被執行。
      - static block的順序先於non-static block。
      - static block是在JVM的class loader載入該類別時被執行，non-static block是在建構實例時被執行。
      - static block僅能存取靜態成員變數及方法，無法存取非靜態成員變數及方法，non-static block可以存取靜態及非靜態的成員變數及方法。

   4. 

   5. 

   6. 




# 3.基本型別包裝類:



1.因IntegerJDK預設會把127~-128的值放進cach裡，故直接用==式參考到同一個cach裡的值，但超過此範圍就無法用==比較，會建立新的物建在記憶體裡，其參考位子不同

![036](images/pic036.png)

2.JDK1.7後提供compare(value1,value2)來進行比較兩數相不相等

```java
        Integer i = 128;
        Integer j = 129;
        System.out.println(Integer.compare(i, j)); //-1
        i = 128;
        j = 128;
        System.out.println(Integer.compare(i, j)); //0
        i = 129;
        j = 128;
        System.out.println(Integer.compare(i, j)); //1
```



# 4.常見的時間格式:

```
dd-MM-yy                            31-01-12
dd-MM-yyyy                          31-01-2013
MM-dd-yyyy                          01-31-2013
yyyy-MM-dd                          2013-01-31
yyyy-MM-dd HH:mm:ss                 2013-01-31 23:59:59
yyyy-MM-dd HH:mm:ss.SSS             2013-01-31 23:59:59.999
yyyy-MM-dd HH:mm:ss.SSSZ            2013-01-31 23:59:59.999+0100
EEEEE MMMMM yyyy HH:mm:ss.SSSZ    Saturday November 2013 10:45:42.720+0100
```

EX:

String pattern = "yyyy-MM-dd";

SimpleDateFormat simpleDateFormat = new 
SimpleDateFormat(pattern);

Date date = simpleDateFormat.parse("2013-12-04");

EX:

String pattern = "EEEEE MMMMM yyyy HH:mm:ss.SSSZ";

SimpleDateFormat simpleDateFormat =
        new SimpleDateFormat(pattern, new Locale("zh", "ZH")); //指定具体語言環境

String date = simpleDateFormat.format(new Date());

System.out.println(date);

輸出:星期三 十二月 2013 17:09:04.757+0800


# 5.固定每天幾點做捨事寫法:

```java
	Calendar now = Calendar.getInstance();
	int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);

	if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
		String time = DateFormatUtils.format(now, "HHmm");

		if (time.compareTo("0000") >= 0 && time.compareTo("0600") <= 0) {
		   XXXXXX
		}
	}
```

# 6.使用LOG4J 記錄LOG方法:



```java
    /*LOG PRINT*/
    private void printErrorLog(Exception e) {
        StackTraceElement[] arr = e.getStackTrace();
        for (int i = 0; i < arr.length; i++) {
            log.error("  " + arr[i].toString());
        }
    }

    private void printWarnLog(Exception e) {
        StackTraceElement[] arr = e.getStackTrace();
        for (int i = 0; i < arr.length; i++) {
            log.warn("  " + arr[i].toString());
        }
    }

```
# 7.多執行緒:

## 1.創建執行緒的方法:
    a.繼承 Thread
    b.實作 Runnable

## 2.處理執行緒安全的方法:
    a.synchronized 同步代碼塊
    b.synchronized 同步方法
    c.Lock 鎖

## 3.synchronized與Lock的異同
 
相同:

    兩者都可以處理執行緒安全問題

不同:
    
    synchronized:在執行完相應的同步代碼後，會自動釋放同步監視器

    Lock:需要手動的啟動同步(lock())，同時結束同步也需要手動的實現(unlock())

```java
public class Ticket01 implements Runnable{
	private int count = 100;

	/**
	 * 建構是可以帶參數
	 * fair:預設式false，為true時代表，先進先執行，後進後執行，所有執行緒平等，不會發生1執行完又搶到CPU執行權的狀況
	 * 
	 * */
	ReentrantLock lock = new ReentrantLock();
	
	
	@Override
	public void run() {
		
		while(true) {
			try {
				//鎖啟動
				lock.lock();
				if(count > 0) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					System.out.println("執行緒 "+Thread.currentThread().getName()+" 售票號碼:"+count--+" 號");
				}else {
					break;
				}
			}finally {
				//不管成功或失敗都解鎖
				lock.unlock();
			}
		}
		
		
	}

}

```

# 8.java8 lambda

    lambda即為函數是接口的一個實例，以前是用匿名類方式實例化接口，現在可以改用lambda去實例化接口

    條件:需要函數式接口(functionalInterface)才能使用，即interface中只能有一個抽象方法

## 語法

    () -> {}
    參數體  lambda lambda體

### 參數
    1.如果只有一個參數，其小括號可以省略，其餘不可省略

### lambda體
    1.如果只有一個敘述式，大括號可以省略


# 9.java8 方法引用

    使用情境:當要傳遞給lambda體的操作，已經有實現的方法了，可以使用方法引用

    方法引用，本質上就是lambda表達式，而lambda表達式做為函數式接口的實例。所以方法引用，也是函數式接口的實例

## 語法

    類(或物件) :: 方法名

## 具體情況分以下3種

### 情況1 物件 :: 非靜態方法

### 情況2 類 :: 靜態方法

以上兩種狀況，引用的要求:要求接口中的抽象方法的形參列表和返回值類型與方法引用的方法的形參列表與返回值類型一致

### 情況3 類 :: 非靜態方法

第3種請況要求，接口的第一個形參，要式方法的調用者，第2個形參之後即為此調用方法的參數，其方法的返回值類型需與接口返回值類型一致



