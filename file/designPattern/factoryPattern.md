# 工廠模式

## 1 概述
需求：設計一個咖啡店點餐系統。

設計一個咖啡類（Coffee），並定義其兩個子類（美式咖啡【AmericanCoffee】和拿鐵咖啡【LatteCoffee】）；再設計一個咖啡店類（CoffeeStore），咖啡店具有點咖啡的功能。

具體類的設計如下：

![7](files/12.png)

在java中，萬物皆對象，這些對像都需要創建，如果創建的時候直接new該對象，就會對該對象耦合嚴重，假如我們要更換對象，所有new對象的地方都需要修改一遍，這顯然違背了軟件設計的開閉原則。如果我們使用工廠來生產對象，我們就只和工廠打交道就可以了，徹底和對象解耦，如果要更換對象，直接在工廠裡更換該對象即可，達到了與對象解耦的目的；所以說，工廠模式最大的優點就是：解耦。

在本教程中會介紹三種工廠的使用

簡單工廠模式（不屬於GOF的23種經典設計模式）
工廠方法模式
抽象工廠模式
 

## 2 簡單工廠模式
簡單工廠不是一種設計模式，反而比較像是一種編程習慣。

### 2.1 結構
簡單工廠包含如下角色：

抽象產品：定義了產品的規範，描述了產品的主要特性和功能。
具體產品：實現或者繼承抽象產品的子類
具體工廠：提供了創建產品的方法，調用者通過該方法來獲取產品。

### 2.2 實現
現在使用簡單工廠對上面案例進行改進，類圖如下：

![7](files/13.png)

```java
public  class  SimpleCoffeeFactory {
​
    public  Coffee  createCoffee ( String  type ) {
        Coffee  coffee  =  null ;
        if ( "americano" . equals ( type )) {
            coffee  =  new  AmericanoCoffee ();
        } else  if ( "latte" . equals ( type )) {
            coffee  =  new  LatteCoffee ();
        }
        return  coffee ;
    }
}
```

* 工廠（factory）處理創建對象的細節，一旦有了SimpleCoffeeFactory，CoffeeStore類中的orderCoffee()就變成此對象的客戶，後期如果需要Coffee對象直接從工廠中獲取即可。這樣也就解除了和Coffee實現類的耦合，同時又產生了新的耦合，CoffeeStore對象和SimpleCoffeeFactory工廠對象的耦合，工廠對象和商品對象的耦合。

* 後期如果再加新品種的咖啡，我們勢必要需求修改SimpleCoffeeFactory的代碼，違反了開閉原則。工廠類的客戶端可能有很多，比如創建美團外賣等，這樣只需要修改工廠類的代碼，省去其他的修改操作。


### 2.3 擴展(靜態工廠)


在開發中也有一部分人將工廠類中的創建對象的功能定義為靜態的，這個就是靜態工廠模式，它也不是23種設計模式中的。代碼如下：


```java
public  class  SimpleCoffeeFactory {
​
    public  static  Coffee  createCoffee ( String  type ) {
        Coffee  coffee  =  null ;
        if ( "americano" . equals ( type )) {
            coffee  =  new  AmericanoCoffee ();
        } else  if ( "latte" . equals ( type )) {
            coffee  =  new  LatteCoffee ();
        }
        return  coffe ;
    }
}
```

### 2.4 優缺點
- 優點：

  - 封裝了創建對象的過程，可以通過參數直接獲取對象。把對象的創建和業務邏輯層分開，這樣以後就避免了修改客戶代碼，如果要實現新產品直接修改工廠類，而不需要在原代碼中修改，這樣就降低了客戶代碼修改的可能性，更加容易擴展。

- 缺點：

  - 增加新產品時還是需要修改工廠類的代碼，違背了“開閉原則”。


## 3 工廠方法模式
針對上例中的缺點，使用工廠方法模式就可以完美的解決，完全遵循開閉原則。

### 3.1 概念
定義一個用於創建對象的接口，讓子類決定實例化哪個產品類對象。工廠方法使一個產品類的實例化延遲到其工廠的子類。

### 3.2 結構
工廠方法模式的主要角色：

抽象工廠（Abstract Factory）：提供了創建產品的接口，調用者通過它訪問具體工廠的工廠方法來創建產品。
具體工廠（ConcreteFactory）：主要是實現抽象工廠中的抽象方法，完成具體產品的創建。
抽象產品（Product）：定義了產品的規範，描述了產品的主要特性和功能。
具體產品（ConcreteProduct）：實現了抽象產品角色所定義的接口，由具體工廠來創建，它同具體工廠之間一一對應。

### 3.3 實現
使用工廠方法模式對上例進行改進，類圖如下：


![7](files/14.png)

抽象工廠：

```java

public  interface  CoffeeFactory {
​
    Coffee  createCoffee ();
}
```

具體工廠：

```java
public  class  LatteCoffeeFactory  implements  CoffeeFactory {
​
    public  Coffee  createCoffee () {
        return  new  LatteCoffee ();
    }
}
​
public  class  AmericanCoffeeFactory  implements  CoffeeFactory {
​
    public  Coffee  createCoffee () {
        return  new  AmericanCoffee ();
    }
}
```

咖啡店類：

```java

public  class  CoffeeStore {
​
    private  CoffeeFactory  factory ;
​
    public  CoffeeStore ( CoffeeFactory  factory ) {
        this . factory  =  factory ;
    }
​
    public  Coffee  orderCoffee ( String  type ) {
        Coffee  coffee  =  factory . createCoffee ();
        coffee . addMilk ();
        coffee . addsugar ();
        return  coffee ;
    }
}
```
* 從以上的編寫的代碼可以看到，要增加產品類時也要相應地增加工廠類，不需要修改工廠類的代碼了，這樣就解決了簡單工廠模式的缺點。

* 工廠方法模式是簡單工廠模式的進一步抽象。由於使用了多態性，工廠方法模式保持了簡單工廠模式的優點，而且克服了它的缺點。

### 3.4 優缺點
- 優點：

  - 用戶只需要知道具體工廠的名稱就可得到所要的產品，無須知道產品的具體創建過程；
在系統增加新的產品時只需要添加具體產品類和對應的具體工廠類，無須對原工廠進行任何修改，滿足開閉原則；
- 缺點：

  - 每增加一個產品就要增加一個具體產品類和一個對應的具體工廠類，這增加了系統的複雜度。


## 4 抽象工廠模式

- 前面介紹的工廠方法模式中考慮的是一類產品的生產，如畜牧場只養動物、電視機廠只生產電視機等。

- 這些工廠只生產同種類產品，同種類產品稱為同等級產品，也就是說：工廠方法模式只考慮生產同等級的產品，但是在現實生活中許多工廠是綜合型的工廠，能生產多等級（種類） 的產品，如電器廠既生產電視機又生產洗衣機或空調，大學既有軟件專業又有生物專業等。

- 本節要介紹的抽象工廠模式將考慮多等級產品的生產，將同一個具體工廠所生產的位於不同等級的一組產品稱為一個產品族，下圖所示橫軸是產品等級，也就是同一類產品；縱軸是產品族，也就是同一品牌的產品，同一品牌的產品產自同一個工廠。


### 4.1 概念

* 是一種為訪問類提供一個創建一組相關或相互依賴對象的接口，且訪問類無須指定所要產品的具體類就能得到同族的不同等級的產品的模式結構。

* 抽象工廠模式是工廠方法模式的升級版本，工廠方法模式只生產一個等級的產品，而抽象工廠模式可生產多個等級的產品。

### 4.2 結構

抽象工廠模式的主要角色如下：

- 抽象工廠（Abstract Factory）：提供了創建產品的接口，它包含多個創建產品的方法，可以創建多個不同等級的產品。
- 具體工廠（Concrete Factory）：主要是實現抽象工廠中的多個抽象方法，完成具體產品的創建。
- 抽象產品（Product）：定義了產品的規範，描述了產品的主要特性和功能，抽象工廠模式有多個抽象產品。
- 具體產品（ConcreteProduct）：實現了抽象產品角色所定義的接口，由具體工廠來創建，它同具體工廠之間是多對一的關係。

### 4.2 實現

現咖啡店業務發生改變，不僅要生產咖啡還要生產甜點，如提拉米蘇、抹茶慕斯等，要是按照工廠方法模式，需要定義提拉米蘇類、抹茶慕斯類、提拉米蘇工廠、抹茶慕斯工廠、甜點工廠類，很容易發生類爆炸情況。其中拿鐵咖啡、美式咖啡是一個產品等級，都是咖啡；提拉米蘇、抹茶慕斯也是一個產品等級；拿鐵咖啡和提拉米蘇是同一產品族（也就是都屬於意大利風味），美式咖啡和抹茶慕斯是同一產品族（也就是都屬於美式風味）。所以這個案例可以使用抽象工廠模式實現。類圖如下：


抽象工廠：

![7](files/15.png)

抽象工廠：

```java

public  interface  DessertFactory {
​
    Coffee  createCoffee ();
​
    Dessert  createDessert ();
}
```

具體工廠：

```java

//美式甜點工廠
public  class  AmericanDessertFactory  implements  DessertFactory {
​
    public  Coffee  createCoffee () {
        return  new  AmericanCoffee ();
    }
​
    public  Dessert  createDessert () {
        return  new  MatchaMousse ();
    }
}
//意大利風味甜點工廠
public  class  ItalyDessertFactory  implements  DessertFactory {
​
    public  Coffee  createCoffee () {
        return  new  LatteCoffee ();
    }
​
    public  Dessert  createDessert () {
        return  new  Tiramisu ();
    }
}
```

- 如果要加同一個產品族的話，只需要再加一個對應的工廠類即可，不需要修改其他的類。

### 4.3 優缺點
- 優點：

  - 當一個產品族中的多個對像被設計成一起工作時，它能保證客戶端始終只使用同一個產品族中的對象。

- 缺點：

  - 當產品族中需要增加一個新的產品時，所有的工廠類都需要進行修改。

### 4.4 使用場景
- 當需要創建的對像是一系列相互關聯或相互依賴的產品族時，如電器工廠中的電視機、洗衣機、空調等。
系統中有多個產品族，但每次只使用其中的某一族產品。如有人只喜歡穿某一個品牌的衣服和鞋。
系統中提供了產品的類庫，且所有產品的接口相同，客戶端不依賴產品實例的創建細節和內部結構。
如：輸入法換皮膚，一整套一起換。生成不同操作系統的程序。


## 5 模式擴展
- 簡單工廠+配置文件解除耦合

可以通過工廠模式+配置文件的方式解除工廠對象和產品對象的耦合。在工廠類中加載配置文件中的全類名，並創建對象進行存儲，客戶端如果需要對象，直接進行獲取即可。

第一步：定義配置文件

為了演示方便，我們使用properties文件作為配置文件，名稱為bean.properties

```
american = com.itheima.pattern.factory.config_factory.AmericanCoffee
latte = com.itheima.pattern.factory.config_factory.LatteCoffee
```

第二步：改進工廠類

```java
public  class  CoffeeFactory {
​
    private  static  Map < String , Coffee >  map  =  new  HashMap ();
​
    static {
        Properties  p  =  new  Properties ();
        InputStream  is  =  CoffeeFactory . class . getClassLoader (). getResourceAsStream ( "bean.properties" );
        try {
            p . load ( is );
            //遍歷Properties集合對象
            Set < Object >  keys  =  p . keySet ();
            for ( Object  key : keys ) {
                //根據鍵獲取值（全類名）
                String  className  =  p . getProperty (( String ) key );
                //獲取字節碼對象
                Class  clazz  =  Class . forName ( className );
                Coffee  obj  = ( Coffee ) clazz . newInstance ();
                map . put (( String ) key , obj );
            }
        } catch ( Exception  e ) {
            e . printStackTrace ();
        }
    }
​
    public  static  Coffee  createCoffee ( String  name ) {
​
        return  map . get ( name );
    }
}
```

- 靜態成員變量用來存儲創建的對象（鍵存儲的是名稱，值存儲的是對應的對象），而讀取配置文件以及創建對象寫在靜態代碼塊中，目的就是只需要執行一次。


## 6 JDK源碼解析-Collection.iterator方法

```java
public  class  Demo {
    public  static  void  main ( String [] args ) {
        List < String >  list  =  new  ArrayList <> ();
        list . add ( "令狐衝" );
        list . add ( "風清揚" );
        list . add ( "任我行" );
​
        //獲取迭代器對象
        Iterator < String >  it  =  list . iterator ();
        //使用迭代器遍歷
        while ( it . hasNext ()) {
            String  ele  =  it . next ();
            System . out . println ( ele );
        }
    }
}
```

- 對上面的代碼大家應該很熟，使用迭代器遍歷集合，獲取集合中的元素。而單列集合獲取迭代器的方法就使用到了工廠方法模式。我們看通過類圖看看結構：

![7](files/16.png)

- Collection接口是抽象工廠類，ArrayList是具體的工廠類；Iterator接口是抽象商品類，ArrayList類中的Iter內部類是具體的商品類。在具體的工廠類中iterator()方法創建具體的商品類的對象。

## 7 其他地方 
- 1,DateForamt類中的getInstance()方法使用的是工廠模式；

- 2,Calendar類中的getInstance()方法使用的是工廠模式；

