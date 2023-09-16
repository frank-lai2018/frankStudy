# 類文件結構

- JVM只關心字解碼文件的執行，跟語言無關，任何語言只要能編譯成JVM規範的.class舅舅可以在虛擬機上運行

![034](imgs/34.png)

- 一個簡單的HelloWorld.java

```java
package cn.itcast.jvm.t5;
// HelloWorld 示例
public class HelloWorld {
public static void main(String[] args) {
System.out.println("hello world");
}
}

```
- 執行 javac -parameters -d . HellowWorld.java
- 編譯為 HelloWorld.class 後是這個樣子的：

```
[root@localhost ~]# od -t xC HelloWorld.class
0000000 ca fe ba be 00 00 00 34 00 23 0a 00 06 00 15 09
0000020 00 16 00 17 08 00 18 0a 00 19 00 1a 07 00 1b 07
0000040 00 1c 01 00 06 3c 69 6e 69 74 3e 01 00 03 28 29
0000060 56 01 00 04 43 6f 64 65 01 00 0f 4c 69 6e 65 4e
0000100 75 6d 62 65 72 54 61 62 6c 65 01 00 12 4c 6f 63
0000120 61 6c 56 61 72 69 61 62 6c 65 54 61 62 6c 65 01
0000140 00 04 74 68 69 73 01 00 1d 4c 63 6e 2f 69 74 63
0000160 61 73 74 2f 6a 76 6d 2f 74 35 2f 48 65 6c 6c 6f
0000200 57 6f 72 6c 64 3b 01 00 04 6d 61 69 6e 01 00 16
0000220 28 5b 4c 6a 61 76 61 2f 6c 61 6e 67 2f 53 74 72
0000240 69 6e 67 3b 29 56 01 00 04 61 72 67 73 01 00 13
0000260 5b 4c 6a 61 76 61 2f 6c 61 6e 67 2f 53 74 72 69
0000300 6e 67 3b 01 00 10 4d 65 74 68 6f 64 50 61 72 61
0000320 6d 65 74 65 72 73 01 00 0a 53 6f 75 72 63 65 46
0000340 69 6c 65 01 00 0f 48 65 6c 6c 6f 57 6f 72 6c 64
0000360 2e 6a 61 76 61 0c 00 07 00 08 07 00 1d 0c 00 1e
0000400 00 1f 01 00 0b 68 65 6c 6c 6f 20 77 6f 72 6c 64
0000420 07 00 20 0c 00 21 00 22 01 00 1b 63 6e 2f 69 74
0000440 63 61 73 74 2f 6a 76 6d 2f 74 35 2f 48 65 6c 6c
0000460 6f 57 6f 72 6c 64 01 00 10 6a 61 76 61 2f 6c 61
0000500 6e 67 2f 4f 62 6a 65 63 74 01 00 10 6a 61 76 61
0000520 2f 6c 61 6e 67 2f 53 79 73 74 65 6d 01 00 03 6f
0000540 75 74 01 00 15 4c 6a 61 76 61 2f 69 6f 2f 50 72
0000560 69 6e 74 53 74 72 65 61 6d 3b 01 00 13 6a 61 76
0000600 61 2f 69 6f 2f 50 72 69 6e 74 53 74 72 65 61 6d
0000620 01 00 07 70 72 69 6e 74 6c 6e 01 00 15 28 4c 6a
0000640 61 76 61 2f 6c 61 6e 67 2f 53 74 72 69 6e 67 3b
0000660 29 56 00 21 00 05 00 06 00 00 00 00 00 02 00 01
0000700 00 07 00 08 00 01 00 09 00 00 00 2f 00 01 00 01
0000720 00 00 00 05 2a b7 00 01 b1 00 00 00 02 00 0a 00
0000740 00 00 06 00 01 00 00 00 04 00 0b 00 00 00 0c 00
0000760 01 00 00 00 05 00 0c 00 0d 00 00 00 09 00 0e 00
0001000 0f 00 02 00 09 00 00 00 37 00 02 00 01 00 00 00
0001020 09 b2 00 02 12 03 b6 00 04 b1 00 00 00 02 00 0a
0001040 00 00 00 0a 00 02 00 00 00 06 00 08 00 07 00 0b
0001060 00 00 00 0c 00 01 00 00 00 09 00 10 00 11 00 00
0001100 00 12 00 00 00 05 01 00 10 00 00 00 01 00 13 00
0001120 00 00 02 00 14
```

## JVM規範，類文件結構如下

- u4 => 4代表4個字節


- 每個.class文件，嚴格遵守以下排列方式
- 前面沒有u開頭的代表很多，其數目為前面count表示幾個

```
ClassFile {
    u4 magic; 魔術
    u2 minor_version; 小版本號
    u2 major_version; 主版本號
    u2 constant_pool_count; 常量池信息
    cp_info constant_pool[constant_pool_count-1]; 常量池信息，前面count會說有幾個，就會占用一段區塊
    u2 access_flags; 訪問修飾符(public、private...)
    u2 this_class; 包名、類名信息
    u2 super_class; 父類
    u2 interfaces_count; 接口信息
    u2 interfaces[interfaces_count]; 接口信息
    u2 fields_count; 類中的變數(成員變數、靜態變數) 信息
    field_info fields[fields_count]; 類中的變數(成員變數、靜態變數) 信息
    u2 methods_count; 方法信息(成員方法、靜態方法...等等)
    method_info methods[methods_count]; 方法信息
    u2 attributes_count; 類的附加屬性信息
    attribute_info attributes[attributes_count]; 類的附加屬性信息
}

```

## 1.1魔術

- 所有文件都是自己的特定類型，用來標示文件是什麼類型
  

- 0~3 字節，表示它是否是【class】類型的文件
  
0000000 **ca fe ba be** 00 00 00 34 00 23 0a 00 06 00 15 09


## 1.2 版本
- 4~7 字節，表示類的版本 00 34（52） 表示是 Java 8

0000000 ca fe ba be **00 00 00 34** 00 23 0a 00 06 00 15 09


## 1.3 常量池

- 常量池主要有以下兩大類
  - 1.字面量(Literal):如文本字串、被聲明為final的常量值等
  - 2.符號引用(Symbolic References):屬於編譯原理方面的概念，主要包括下面幾個常量
    - 1.被模塊導出或者開放的包（Package）
    - 2.類和接口的全限定名（Fully Qualified Name）
    - 3.字段的名稱和描述符（Descriptor）
    - 4.方法的名稱和描述符
    - 5.方法句柄和方法類型（Method Handle、Method Type、Invoke Dynamic）
    - 6.動態調用點和動態常量（Dynamically-Computed Call Site、Dynamically-Computed Constant）


||||
|--|--|---|
|Constant Type| DEC Value|Hex Value|
|CONSTANT_Class |7|7|
|CONSTANT_Fieldref |9|9|
|CONSTANT_Methodref |10|A|
|CONSTANT_InterfaceMethodref |11|B|
|CONSTANT_String |8|8|
|CONSTANT_Integer |3|3|
|CONSTANT_Float |4|4|
|CONSTANT_Long |5|5|
|CONSTANT_Double |6|6|
|CONSTANT_NameAndType |12|C|
|CONSTANT_Utf8 |1|1|
|CONSTANT_MethodHandle |15|F|
|CONSTANT_MethodType| 16|10|
|CONSTANT_InvokeDynamic |18|12|

- 因為常量池是不固定的，所以版本號後，會用U2 兩個字節，表示常量池的數目
- 8~9 字節，表示常量池長度，00 23 （35） 表示常量池有 #1~#34項，注意 #0 項不計入，也沒有值

0000000 ca fe ba be 00 00 00 34 **00 23** 0a 00 06 00 15 09

- 常量池中每一個常量都是一張表(如下圖)，表的第一個字節是一個u1類型的標示位，代表著當前常量屬於哪種常量類型

![034](imgs/35.png)

- 知道常量類型後，就可以查以下表，知道其表後面的數據占用幾個字節，及其所代表的涵義

![034](imgs/36.png)
![034](imgs/37.png)
![034](imgs/33.png)

- 第#1項 0a(表示一個類型，需要常上面的表才知道是什麼) 表示一個 Method 信息(從數據類型結構總表得知以下4個字節為該方法的說明)，00 06 和 00 15（21） 表示它引用了常量池中 #6 和 #21 項來獲得
這個方法的【所屬類】和【方法名】

0000000 ca fe ba be 00 00 00 34 00 23 **0a 00 06 00 15** 09

- 第#2項 09 表示一個 Field 信息，00 16（22）和 00 17（23） 表示它引用了常量池中 #22 和 # 23 項
來獲得這個成員變量的【所屬類】和【成員變量名】

0000000 ca fe ba be 00 00 00 34 00 23 0a 00 06 00 15 **09**

0000020 **00 16 00 17** 08 00 18 0a 00 19 00 1a 07 00 1b 07

- 第#3項 08 表示一個字符串常量名稱，00 18（24）表示它引用了常量池中 #24 項

0000020 00 16 00 17 **08 00 18** 0a 00 19 00 1a 07 00 1b 07


- 第#4項 0a 表示一個 Method 信息，00 19（25） 和 00 1a（26） 表示它引用了常量池中 #25 和 #26
項來獲得這個方法的【所屬類】和【方法名】

0000020 00 16 00 17 08 00 18 **0a 00 19 00 1a** 07 00 1b 07


- 第#5項 07 表示一個 Class 信息，00 1b（27） 表示它引用了常量池中 #27 項

0000020 00 16 00 17 08 00 18 0a 00 19 00 1a **07 00 1b** 07

- 第#6項 07 表示一個 Class 信息，00 1c（28） 表示它引用了常量池中 #28 項

0000020 00 16 00 17 08 00 18 0a 00 19 00 1a 07 00 1b **07**

0000040 **00 1c** 01 00 06 3c 69 6e 69 74 3e 01 00 03 28 29

- 第#7項 01 表示一個 utf8 串，00 06 表示長度，3c 69 6e 69 74 3e 是【<init> 】(建構式方法)
- 
0000040 00 1c **01 00 06 3c 69 6e 69 74 3e** 01 00 03 28 29

- 第#8項 01 表示一個 utf8 串，00 03 表示長度，28 29 56 是【()V】其實就是()表示無參、V代表無返回值

0000040 00 1c 01 00 06 3c 69 6e 69 74 3e **01 00 03 28 29**

0000060 **56** 01 00 04 43 6f 64 65 01 00 0f 4c 69 6e 65 4e

- 第#9項 01 表示一個 utf8 串，00 04 表示長度，43 6f 64 65 是【Code】(方法的屬性)

0000060 56 **01 00 04 43 6f 64 65** 01 00 0f 4c 69 6e 65 4e

- 第#10項 01 表示一個 utf8 串，00 0f（15） 表示長度，4c 69 6e 65 4e 75 6d 62 65 72 54 61 62 6c 65
是【LineNumberTable】(方法的屬性)

0000060 56 01 00 04 43 6f 64 65 **01 00 0f 4c 69 6e 65 4e**

0000100 **75 6d 62 65 72 54 61 62 6c 65** 01 00 12 4c 6f 63

- 第#11項 01 表示一個 utf8 串，00 12（18） 表示長度，4c 6f 63 61 6c 56 61 72 69 61 62 6c 65 54 61
62 6c 65是【LocalVariableTable】(方法的屬性)

0000100 75 6d 62 65 72 54 61 62 6c 65 **01 00 12 4c 6f 63**

0000120 **61 6c 56 61 72 69 61 62 6c 65 54 61 62 6c 65** 01

- 第#12項 01 表示一個 utf8 串，00 04 表示長度，74 68 69 73 是【this】

0000120 61 6c 56 61 72 69 61 62 6c 65 54 61 62 6c 65 **01**

0000140 **00 04 74 68 69 73** 01 00 1d 4c 63 6e 2f 69 74 63

- 第#13項 01 表示一個 utf8 串，00 1d（29） 表示長度，是【Lcn/itcast/jvm/t5/HelloWorld;】

0000140 00 04 74 68 69 73 **01 00 1d 4c 63 6e 2f 69 74 63**

0000160 **61 73 74 2f 6a 76 6d 2f 74 35 2f 48 65 6c 6c 6f**

0000200 **57 6f 72 6c 64 3b** 01 00 04 6d 61 69 6e 01 00 16

- 第#14項 01 表示一個 utf8 串，00 04 表示長度，74 68 69 73 是【main】

0000200 57 6f 72 6c 64 3b **01 00 04 6d 61 69 6e** 01 00 16

- 第#15項 01 表示一個 utf8 串，00 16（22） 表示長度，是【([Ljava/lang/String;)V】其實就是參數為
字符串數組，無返回值

0000200 57 6f 72 6c 64 3b 01 00 04 6d 61 69 6e **01 00 16**

0000220 **28 5b 4c 6a 61 76 61 2f 6c 61 6e 67 2f 53 74 72**

0000240 **69 6e 67 3b 29 56** 01 00 04 61 72 67 73 01 00 13

- 第#16項 01 表示一個 utf8 串，00 04 表示長度，是【args】

0000240 69 6e 67 3b 29 56 **01 00 04 61 72 67 73** 01 00 13

- 第#17項 01 表示一個 utf8 串，00 13（19） 表示長度，是【[Ljava/lang/String;】

0000240 69 6e 67 3b 29 56 01 00 04 61 72 67 73 **01 00 13**

0000260 **5b 4c 6a 61 76 61 2f 6c 61 6e 67 2f 53 74 72 69**

0000300 **6e 67 3b** 01 00 10 4d 65 74 68 6f 64 50 61 72 61

- 第#18項 01 表示一個 utf8 串，00 10（16） 表示長度，是【MethodParameters】

0000300 6e 67 3b **01 00 10 4d 65 74 68 6f 64 50 61 72 61**

0000320 **6d 65 74 65 72 73** 01 00 0a 53 6f 75 72 63 65 46

- 第#19項 01 表示一個 utf8 串，00 0a（10） 表示長度，是【SourceFile】

0000320 6d 65 74 65 72 73 **01 00 0a 53 6f 75 72 63 65 46**

0000340 **69 6c 65** 01 00 0f 48 65 6c 6c 6f 57 6f 72 6c 64

- 第#20項 01 表示一個 utf8 串，00 0f（15） 表示長度，是【HelloWorld.java】

0000340 69 6c 65 **01 00 0f 48 65 6c 6c 6f 57 6f 72 6c 64**

0000360 **2e 6a 61 76 61** 0c 00 07 00 08 07 00 1d 0c 00 1e

- 第#21項 0c 表示一個 【名+類型】，00 07 00 08 引用了常量池中 #7 #8 兩項

0000360 2e 6a 61 76 61 **0c 00 07 00 08** 07 00 1d 0c 00 1e

- 第#22項 07 表示一個 Class 信息，00 1d（29） 引用了常量池中 #29 項

0000360 2e 6a 61 76 61 0c 00 07 00 08 **07 00 1d** 0c 00 1e


- 第#23項 0c 表示一個 【名+類型】，00 1e（30） 00 1f （31）引用了常量池中 #30 #31 兩項

0000360 2e 6a 61 76 61 0c 00 07 00 08 07 00 1d **0c 00 1e**

0000400 **00 1f** 01 00 0b 68 65 6c 6c 6f 20 77 6f 72 6c 64

- 第#24項 01 表示一個 utf8 串，00 0f（15） 表示長度，是【hello world】

0000400 00 1f **01 00 0b 68 65 6c 6c 6f 20 77 6f 72 6c 64**

- 第#25項 07 表示一個 Class 信息，00 20（32） 引用了常量池中 #32 項

0000420 **07 00 20** 0c 00 21 00 22 01 00 1b 63 6e 2f 69 74

- 第#26項 0c 表示一個 【名+類型】，00 21（33） 00 22（34）引用了常量池中 #33 #34 兩項

0000420 07 00 20 **0c 00 21 00 22** 01 00 1b 63 6e 2f 69 74

- 第#27項 01 表示一個 utf8 串，00 1b（27） 表示長度，是【cn/itcast/jvm/t5/HelloWorld】

0000420 07 00 20 0c 00 21 00 22 **01 00 1b 63 6e 2f 69 74**

0000440 **63 61 73 74 2f 6a 76 6d 2f 74 35 2f 48 65 6c 6c**

0000460 **6f 57 6f 72 6c 64** 01 00 10 6a 61 76 61 2f 6c 61

- 第#28項 01 表示一個 utf8 串，00 10（16） 表示長度，是【java/lang/Object】

0000460 6f 57 6f 72 6c 64 **01 00 10 6a 61 76 61 2f 6c 61**

0000500 **6e 67 2f 4f 62 6a 65 63 74** 01 00 10 6a 61 76 61

- 第#29項 01 表示一個 utf8 串，00 10（16） 表示長度，是【java/lang/System】

0000500 6e 67 2f 4f 62 6a 65 63 74 **01 00 10 6a 61 76 61**

0000520 **2f 6c 61 6e 67 2f 53 79 73 74 65 6d** 01 00 03 6f

- 第#30項 01 表示一個 utf8 串，00 03 表示長度，是【out】

0000520 2f 6c 61 6e 67 2f 53 79 73 74 65 6d **01 00 03 6f**

0000540 **75 74** 01 00 15 4c 6a 61 76 61 2f 69 6f 2f 50 72

- 第#31項 01 表示一個 utf8 串，00 15（21） 表示長度，是【Ljava/io/PrintStream;】

0000540 75 74 **01 00 15 4c 6a 61 76 61 2f 69 6f 2f 50 72**

0000560 **69 6e 74 53 74 72 65 61 6d 3b** 01 00 13 6a 61 76


- 第#32項 01 表示一個 utf8 串，00 13（19） 表示長度，是【java/io/PrintStream】

0000560 69 6e 74 53 74 72 65 61 6d 3b **01 00 13 6a 61 76**

0000600 **61 2f 69 6f 2f 50 72 69 6e 74 53 74 72 65 61 6d**

- 第#33項 01 表示一個 utf8 串，00 07 表示長度，是【println】

0000620 **01 00 07 70 72 69 6e 74 6c 6e** 01 00 15 28 4c 6a

- 第#34項 01 表示一個 utf8 串，00 15（21） 表示長度，是【(Ljava/lang/String;)V】

0000620 01 00 07 70 72 69 6e 74 6c 6e **01 00 15 28 4c 6a**

0000640 **61 76 61 2f 6c 61 6e 67 2f 53 74 72 69 6e 67 3b**

0000660 **29 56** 00 21 00 05 00 06 00 00 00 00 00 02 00 01

## 1.4 訪問標識與繼承信息 

### access_flags

- 21 表示該 class 是一個類，公共的

- 由下表可知 public + super => 0X0001 + 0X0020 = 0X0021

0000660 29 56 **00 21** 00 05 00 06 00 00 00 00 00 02 00 01

![034](imgs/39.png)


### this_class，u2表示常量值索引

- u2表示常量值索引
- 05 表示根據常量池中 #5 找到本類全限定名

0000660 29 56 00 21 **00 05** 00 06 00 00 00 00 00 02 00 01

### super_class

- u2表示常量值索引
- 06 表示根據常量池中 #6 找到父類全限定名

0000660 29 56 00 21 00 05 **00 06** 00 00 00 00 00 02 00 01

### interfaces_count

- u2表示常量值索引
- 表示接口的數量，本類為 0

0000660 29 56 00 21 00 05 00 06 **00 00** 00 00 00 02 00 01

## 1.5 Field 信息

- 表示成員變量數量，本類為 0
0000660 29 56 00 21 00 05 00 06 00 00 **00 00** 00 02 00 01

![034](imgs/40.png)


## 1.6 Method 信息

- 表示方法數量，本類為 2

0000660 29 56 00 21 00 05 00 06 00 00 00 00 **00 02** 00 01

- 一個方法由 訪問修飾符，名稱，參數描述，方法屬性數量，方法屬性組成
  - 紅色代表訪問修飾符（本類中是 public）
  - 藍色代表引用了常量池 #07 項作為方法名稱
  - 綠色代表引用了常量池 #08 項作為方法參數描述
  - 黃色代表方法屬性數量，本方法是 1
  - 紅色代表方法屬性
    - 00 09 表示引用了常量池 #09 項，發現是【Code】屬性
    - 00 00 00 2f 表示此屬性的長度是 47
    - 00 01 表示【操作數棧】最大深度
    - 00 01 表示【局部變量表】最大槽（slot）數
    - 00 00 00 05 表示字節碼長度，本例是 5
    - 2a b7 00 01 b1 是字節碼指令
    - 00 00 00 02 表示方法細節屬性數量，本例是 2
    - 00 0a 表示引用了常量池 #10 項，發現是【LineNumberTable】屬性
      - 00 00 00 06 表示此屬性的總長度，本例是 6
      - 00 01 表示【LineNumberTable】長度
      - 00 00 表示【字節碼】行號 00 04 表示【java 源碼】行號
    - 00 0b 表示引用了常量池 #11 項，發現是【LocalVariableTable】屬性
      - 00 00 00 0c 表示此屬性的總長度，本例是 12
      - 00 01 表示【LocalVariableTable】長度
      - 00 00 表示局部變量生命週期開始，相對於字節碼的偏移量
      - 00 05 表示局部變量覆蓋的範圍長度
      - 00 0c 表示局部變量名稱，本例引用了常量池 #12 項，是【this】
      - 00 0d 表示局部變量的類型，本例引用了常量池 #13 項，是
      【Lcn/itcast/jvm/t5/HelloWorld;】
      - 00 00 表示局部變量佔有的槽位（slot）編號，本例是 0

![034](imgs/40.png)



- 紅色代表訪問修飾符（本類中是 public static）
- 藍色代表引用了常量池 #14 項作為方法名稱
- 綠色代表引用了常量池 #15 項作為方法參數描述
- 黃色代表方法屬性數量，本方法是 2
- 紅色代表方法屬性（屬性1）

  - 00 09 表示引用了常量池 #09 項，發現是【Code】屬性
  - 00 00 00 37 表示此屬性的長度是 55
  - 00 02 表示【操作數棧】最大深度
  - 00 01 表示【局部變量表】最大槽（slot）數
  - 00 00 00 05 表示字節碼長度，本例是 9
  - b2 00 02 12 03 b6 00 04 b1 是字節碼指令
  - 00 00 00 02 表示方法細節屬性數量，本例是 2
  - 00 0a 表示引用了常量池 #10 項，發現是【LineNumberTable】屬性

    - 00 00 00 0a 表示此屬性的總長度，本例是 10
    - 00 02 表示【LineNumberTable】長度
    - 00 00 表示【字節碼】行號 00 06 表示【java 源碼】行號
    - 00 08 表示【字節碼】行號 00 07 表示【java 源碼】行號

  - 00 0b 表示引用了常量池 #11 項，發現是【LocalVariableTable】屬性
    - 00 00 00 0c 表示此屬性的總長度，本例是 12
    - 00 01 表示【LocalVariableTable】長度
    - 00 00 表示局部變量生命週期開始，相對於字節碼的偏移量
    - 00 09 表示局部變量覆蓋的範圍長度
    - 00 10 表示局部變量名稱，本例引用了常量池 #16 項，是【args】
    - 00 11 表示局部變量的類型，本例引用了常量池 #17 項，是【[Ljava/lang/String;】
    - 00 00 表示局部變量佔有的槽位（slot）編號，本例是 0

![034](imgs/42.png)

  - 紅色代表方法屬性（屬性2）
    - 00 12 表示引用了常量池 #18 項，發現是【MethodParameters】屬性
      - 00 00 00 05 表示此屬性的總長度，本例是 5
      - 01 參數數量
      - 00 10 表示引用了常量池 #16 項，是【args】
      - 00 00 訪問修飾符
![034](imgs/43.png)


## 1.7 附加屬性
  - 00 01 表示附加屬性數量
  - 00 13 表示引用了常量池 #19 項，即【SourceFile】
  - 00 00 00 02 表示此屬性的長度
  - 00 14 表示引用了常量池 #20 項，即【HelloWorld.java】

0001100 00 12 00 00 00 05 01 00 10 00 00 **00 01 00 13 00**

0001120 **00 00 02 00 14**

- 詳細文檔

https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html


#　2. 字節碼指令
## 2.1 入門
- 接著上一節，研究一下兩組字節碼指令，一個是
  - public cn.itcast.jvm.t5.HelloWorld(); 構造方法的字節碼指令

2a b7 00 01 b1

- 
  - 1.2a => aload_0 加載 slot 0 的局部變量，即 this，做為下面的 invokespecial 構造方法調用的參數
  - 2.b7 => invokespecial 預備調用構造方法，哪個方法呢？
  - 3.00 01 引用常量池中 #1 項，即【Method java/lang/Object."<init>":()V 】
  - 4.b1 表示返回

- 另一個是 public static void main(java.lang.String[]); 主方法的字節碼指令

b2 00 02 12 03 b6 00 04 b1

- 1. b2 => getstatic 用來加載靜態變量，哪個靜態變量呢？
- 2. 00 02 引用常量池中 #2 項，即【Field java/lang/System.out:Ljava/io/PrintStream;】
- 3. 12 => ldc 加載參數，哪個參數呢？
- 4. 03 引用常量池中 #3 項，即 【String hello world】
- 5. b6 => invokevirtual 預備調用成員方法，哪個方法呢？
- 6. 00 04 引用常量池中 #4 項，即【Method java/io/PrintStream.println:(Ljava/lang/String;)V】
- 7. b1 表示返回

- 參考網站
https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5


## 2.2 javap 工具

- 自己分析類文件結構太麻煩了，Oracle 提供了 javap 工具來反編譯 class 文件

- 語法
```
javap -v [class文件]
```
- -v:代表顯示詳細訊息

![034](imgs/44.png)
![034](imgs/45.png)

## 圖解方法執行流程

### 原始java代碼

```java
package cn.itcast.jvm.t3.bytecode;
/**
* 演示 字节码指令 和 操作数栈、常量池的关系
*/
public class Demo3_1 {
    public static void main(String[] args) {
        int a = 10;
        int b = Short.MAX_VALUE + 1;
        int c = a + b;
        System.out.println(c);
    }
}
```

### 編譯後字解碼文件

![034](imgs/46.png)
![034](imgs/47.png)
![034](imgs/48.png)

### 常量池載入運行時常量池

![034](imgs/49.png)

### 方法字節碼載入方法區

- 當運行到main時，會由類加載氣把main方法所在的類加載到內存中，
- 類加載實際上就是把.class裡的字節讀到內存中
- 其中常量池的數據會被放進運行時常量池裡
- 32768也被放進到常量池裡，小於32767(short範圍內)的數字是跟方法字結指令存在一起的


![034](imgs/50.png)

### 5）main 線程開始運行，分配棧幀內存
（stack=2(棧的最大深度)，locals=4(局部變量表的長度)）這兩項決定棧幀內存的大小
![034](imgs/51.png)

- 左邊綠色的區域代表局部變量表
- 藍色的部分代表操作樹棧

### 6）執行引擎開始執行字節碼



- bipush 將一個 byte 壓入操作數棧（其長度會補齊 4 個字節），類似的指令還有
- sipush 將一個 short 壓入操作數棧（其長度會補齊 4 個字節）
- ldc 將一個 int 壓入操作數棧
- ldc2_w 將一個 long 壓入操作數棧（分兩次壓入，因為 long 是 8 個字節）
- 這裡小的數字都是和字節碼指令存在一起，超過 short 範圍的數字存入了常量池

下圖顯示 bipush 10的操作
![034](imgs/52.png)

- istore_1

- 將操作數棧頂數據彈出，存入局部變量表的 slot 1，1代表要存到哪個位子

下圖顯示 bipush 10的操作

將10彈出放進1位子
![034](imgs/53.png)

結果:
![034](imgs/54.png)


- ldc #3

  - 從常量池加載 #3 數據到操作數棧
  - 注意 Short.MAX_VALUE 是 32767，所以 32768 = Short.MAX_VALUE + 1 實際是在編譯期間計算
好的

![034](imgs/55.png)

- istore_2

![034](imgs/56.png)
![034](imgs/57.png)

- iload_1

![034](imgs/58.png)

- 接下來要執行a+b，這個運算動作，需要在操作樹中執行，局部變量表中不能執行a+b操作，所以需要再讀出來
  
- iload_2
  
![034](imgs/59.png)

- iadd

![034](imgs/60.png)
![034](imgs/61.png)

- istore_3

![034](imgs/62.png)
![034](imgs/63.png)

- getstatic #4
  
![034](imgs/64.png)
![034](imgs/65.png)

- iload_3

![034](imgs/66.png)
![034](imgs/67.png)

- invokevirtual #5

  - 找到常量池 #5 項
  - 定位到方法區 java/io/PrintStream.println:(I)V 方法
  - 生成新的棧幀（分配 locals、stack等）
  - 傳遞參數，執行新棧幀中的字節碼
  - 
![034](imgs/68.png)

- 執行完畢，彈出棧幀
- 清除 main 操作數棧內容

![034](imgs/69.png)

- return
  - 完成 main 方法調用，彈出 main 棧幀
  - 程序結束


## 2.4分析 i++

- a++ + ++a + a--

- 遇到變數 => 從局部變量兩表讀出數據到操作樹棧中
- 遇到++ 或 -- => 直接在局部變量表中操作+1或-1 
- 先把變數讀出來，在做運算符 + 和 - 操做

```java
package jvm;

public class Demo3_2 {
	public static void main(String[] args) {
		int a = 10;
		int b = a++ + ++a + a--;
		System.out.println(a);
		System.out.println(b);
	}
}

```

字節碼:

```
$ javap -v Demo3_2.class 
Classfile /M:/2022-12/eclipse/workspace/JVM/jvm/target/classes/jvm/Demo3_2.class
  Last modified 2023年9月16日; size 566 bytes
  SHA-256 checksum 0fde5512674015391d16c4ed2c3bdfb270e10f10b26cb1f5459f35988bbf72ad
  Compiled from "Demo3_2.java"
public class jvm.Demo3_2
  minor version: 0
  major version: 52
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #1                          // jvm/Demo3_2
  super_class: #3                         // java/lang/Object
  interfaces: 0, fields: 0, methods: 2, attributes: 1
Constant pool:
   #1 = Class              #2             // jvm/Demo3_2
   #2 = Utf8               jvm/Demo3_2
   #3 = Class              #4             // java/lang/Object
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Utf8               Code
   #8 = Methodref          #3.#9          // java/lang/Object."<init>":()V
   #9 = NameAndType        #5:#6          // "<init>":()V
  #10 = Utf8               LineNumberTable
  #11 = Utf8               LocalVariableTable
  #12 = Utf8               this
  #13 = Utf8               Ljvm/Demo3_2;
  #14 = Utf8               main
  #15 = Utf8               ([Ljava/lang/String;)V
  #16 = Fieldref           #17.#19        // java/lang/System.out:Ljava/io/PrintStream;
  #17 = Class              #18            // java/lang/System
  #18 = Utf8               java/lang/System
  #19 = NameAndType        #20:#21        // out:Ljava/io/PrintStream;
  #20 = Utf8               out
  #21 = Utf8               Ljava/io/PrintStream;
  #22 = Methodref          #23.#25        // java/io/PrintStream.println:(I)V
  #23 = Class              #24            // java/io/PrintStream
  #24 = Utf8               java/io/PrintStream
  #25 = NameAndType        #26:#27        // println:(I)V
  #26 = Utf8               println
  #27 = Utf8               (I)V
  #28 = Utf8               args
  #29 = Utf8               [Ljava/lang/String;
  #30 = Utf8               a
  #31 = Utf8               I
  #32 = Utf8               b
  #33 = Utf8               SourceFile
  #34 = Utf8               Demo3_2.java
{
  public jvm.Demo3_2();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #8                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Ljvm/Demo3_2;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1
         0: bipush        10
         2: istore_1
         3: iload_1
         4: iinc          1, 1
         7: iinc          1, 1
        10: iload_1
        11: iadd
        12: iload_1
        13: iinc          1, -1
        16: iadd
        17: istore_2
        18: getstatic     #16                 // Field java/lang/System.out:Ljava/io/PrintStream;
        21: iload_1
        22: invokevirtual #22                 // Method java/io/PrintStream.println:(I)V
        25: getstatic     #16                 // Field java/lang/System.out:Ljava/io/PrintStream;
        28: iload_2
        29: invokevirtual #22                 // Method java/io/PrintStream.println:(I)V
        32: return
      LineNumberTable:
        line 5: 0
        line 6: 3
        line 7: 18
        line 8: 25
        line 9: 32
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      33     0  args   [Ljava/lang/String;
            3      30     1     a   I
           18      15     2     b   I
}
SourceFile: "Demo3_2.java"
```

- 分析：
  - 注意 iinc 指令是直接在局部變量 slot 上進行運算
  - a++ 和 ++a 的區別是先執行 iload 還是 先執行 iinc

![034](imgs/70.png)
![034](imgs/71.png)
![034](imgs/72.png)
![034](imgs/73.png)
![034](imgs/74.png)
![034](imgs/75.png)
![034](imgs/76.png)
![034](imgs/77.png)
![034](imgs/78.png)
![034](imgs/79.png)
![034](imgs/80.png)