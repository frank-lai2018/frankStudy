# 原型模式
## 1 概述
- 用一個已經創建的實例作為原型，通過複製該原型對象來創建一個和原型對象相同的新對象。

## 2 結構

- 原型模式包含如下角色：

  - 抽象原型類：規定了具體原型對象必須實現的的clone() 方法。
  - 具體原型類：實現抽象原型類的clone() 方法，它是可被複製的對象。
  - 訪問類：使用具體原型類中的clone() 方法來複製新的對象。

接口類圖如下：

![7](files/17.png)


## 3 實現
- 原型模式的克隆分為淺克隆和深克隆。

  - 淺克隆：創建一個新對象，新對象的屬性和原來對象完全相同，對於非基本類型屬性，仍指向原有屬性所指向的對象的內存地址。

  - 深克隆：創建一個新對象，屬性中引用的其他對像也會被克隆，不再指向原有對像地址。

- Java中的Object類中提供了 clone() 方法來實現淺克隆。Cloneable 接口是上面的類圖中的抽象原型類，而實現了Cloneable接口的子實現類就是具體的原型類。代碼如下：

Realizetype（具體的原型類）：

```java
public  class  Realizetype  implements  Cloneable {
​
    public  Realizetype () {
        System . out . println ( "具體的原型對象創建完成！" );
    }
​
    @Override
    protected  Realizetype  clone () throws  CloneNotSupportedException {
        System . out . println ( "具體原型複製成功！" );
        return ( Realizetype ) super . clone ();
    }
}
```

PrototypeTest（測試訪問類）：

```java
public  class  PrototypeTest {
    public  static  void  main ( String [] args ) throws  CloneNotSupportedException {
        Realizetype  r1  =  new  Realizetype ();
        Realizetype  r2  =  r1 . clone ();
​
        System . out . println ( "對象r1和r2是同一個對象？"  + ( r1  ==  r2 ));
    }
}
```

## 4 案例
- 用原型模式生成“三好學生”獎狀

  - 同一學校的“三好學生”獎狀除了獲獎人姓名不同，其他都相同，可以使用原型模式複制多個“三好學生”獎狀出來，然後在修改獎狀上的名字即可。

類圖如下：

![7](files/18.png)

```java

//獎狀類
public  class  Citation  implements  Cloneable {
    private  String  name ;
​
    public  void  setName ( String  name ) {
        this . name  =  name ;
    }
​
    public  String  getName () {
        return ( this . name );
    }
​
    public  void  show () {
        System . out . println ( name  +  "同學：在2020學年第一學期中表現優秀，被評為三好學生。特發此狀！" );
    }
​
    @Override
    public  Citation  clone () throws  CloneNotSupportedException {
        return ( Citation ) super . clone ();
    }
}
​
//測試訪問類
public  class  CitationTest {
    public  static  void  main ( String [] args ) throws  CloneNotSupportedException {
        Citation  c1  =  new  Citation ();
        c1 . setName ( "張三" );
​
        //複製獎狀
        Citation  c2  =  c1 . clone ();
        //將獎狀的名字修改李四
        c2 . setName ( "李四" );
​
        c1 . show ();
        c2 . show ();
    }
}
```

## 5 使用場景
- 對象的創建非常複雜，可以使用原型模式快捷的創建對象。
- 性能和安全要求比較高。

## 6 擴展（深克隆）

將上面的“三好學生”獎狀的案例中Citation類的name屬性修改為Student類型的屬性。代碼如下：

```java
//獎狀類
public  class  Citation  implements  Cloneable {
    private  Student  stu ;
​
    public  Student  getStu () {
        return  stu ;
    }
​
    public  void  setStu ( Student  stu ) {
        this . stu  =  stu ;
    }
​
    void  show () {
        System . out . println ( stu . getName () +  "同學：在2020學年第一學期中表現優秀，被評為三好學生。特發此狀！" );
    }
​
    @Override
    public  Citation  clone () throws  CloneNotSupportedException {
        return ( Citation ) super . clone ();
    }
}
​
//學生類
public  class  Student {
    private  String  name ;
    private  String  address ;
​
    public  Student ( String  name , String  address ) {
        this . name  =  name ;
        this . address  =  address ;
    }
​
    public  Student () {
    }
​
    public  String  getName () {
        return  name ;
    }
​
    public  void  setName ( String  name ) {
        this . name  =  name ;
    }
​
    public  String  getAddress () {
        return  address ;
    }
​
    public  void  setAddress ( String  address ) {
        this . address  =  address ;
    }
}
​
//測試類
public  class  CitationTest {
    public  static  void  main ( String [] args ) throws  CloneNotSupportedException {
​
        Citation  c1  =  new  Citation ();
        Student  stu  =  new  Student ( "張三" , "西安" );
        c1 . setStu ( stu );
​
        //複製獎狀
        Citation  c2  =  c1 . clone ();
        //獲取c2獎狀所屬學生對象
        Student  stu1  =  c2 . getStu ();
        stu1 . setName ( "李四" );
​
        //判斷stu對象和stu1對像是否是同一個對象
        System . out . println ( "stu和stu1是同一個對象？"  + ( stu  ==  stu1 ));
​
        c1 . show ();
        c2 . show ();
    }
}
```



說明：

stu對象和stu1對像是同一個對象，就會產生將stu1對像中name屬性值改為“李四”，兩個Citation（獎狀）對像中顯示的都是李四。這就是淺克隆的效果，對具體原型類（Citation）中的引用類型的屬性進行引用的複制。這種情況需要使用深克隆，而進行深克隆需要使用對象流。代碼如下：

XXX

```java
public  class  CitationTest1 {
    public  static  void  main ( String [] args ) throws  Exception {
        Citation  c1  =  new  Citation ();
        Student  stu  =  new  Student ( "張三" , "西安" );
        c1 . setStu ( stu );
​
        //創建對象輸出流對象
        ObjectOutputStream  oos  =  new  ObjectOutputStream ( new  FileOutputStream ( "C:\\Users\\Think\\Desktop\\b.txt" ));
        //將c1對象寫出到文件中
        oos . writeObject ( c1 );
        oos . close ();
​
        //創建對像出入流對象
        ObjectInputStream  ois  =  new  ObjectInputStream ( new  FileInputStream ( "C:\\Users\\Think\\Desktop\\b.txt" ));
        //讀取對象
        Citation  c2  = ( Citation ) ois . readObject ();
        //獲取c2獎狀所屬學生對象
        Student  stu1  =  c2 . getStu ();
        stu1 . setName ( "李四" );
​
        //判斷stu對象和stu1對像是否是同一個對象
        System . out . println ( "stu和stu1是同一個對象？"  + ( stu  ==  stu1 ));
​
        c1 . show ();
        c2 . show ();
    }
}
```

XXX


- 注意：Citation類和Student類必須實現Serializable接口，否則會拋NotSerializableException異常。