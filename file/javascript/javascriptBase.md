# 作用域(ES5)

## ES5只有全局作用域跟局部作用域(函數作用域)

### 全局作用域:只要寫在script標籤裡，不再function宣告的函數裡，即為全局作用域

### 局部作用域:只要寫在function宣告個函數裡即為局部作用域，也稱函數作用域

### 注意:在function宣告裡沒有用var 宣告的變數，會被自動提升為全局變數

```javascript
var a = 3 //全局作用域
function fn(){
    //局部作用域
    var b = 4

    c = 5 //沒有用var 宣告會被提升為全局作用域裡的變數
}
```


# 預解析

## javascript 解析時，遇到var 或 function 宣告的變數或函數，會先提升到目前作用預的最上面(指提升宣告，賦值不執行)

### EX:以下代碼
```javascript
console.log(a)
var a = 3
function fn(){
    console.log(b)
    var b = 4
}
```

### 預解析完，後變成以下代碼

```javascript
var a
function fn(){
    var b
    console.log(b)
    b = 4
}
console.log(a) //undefinded
a = 3
```
# 資料型態

* 6大資料型態:Number、String、Boolean、Object、null、undefined

## Number 數字型別

### 1.NaN(Not a Number)

#### 1.NaN內容
* 1.NaN是 JavaScript 的特殊值，表示“非數字”（Not a Number），主要出現在將字符串解析成數字出錯的場合。
* 2.一些數學函數的運算結果會出現NaN
```javascript
Math.acos(2) // NaN
Math.log(-1) // NaN
Math.sqrt(-1) // NaN
```
* 3.0除以0也會得到NaN

```javascript
0 / 0 // NaN
```
* 4.NaN是一個數字類型

```javascript
typeof NaN // 'number'
```

#### 2. NaN運算規則

* 1.NaN不等於任何值，包括它本身。

```javascript
NaN === NaN // false
```

* 2.陣列的indexOf方法內部使用的是嚴格相等運算符，所以該方法對NaN不成立。

```javascript
[NaN].indexOf(NaN) // -1
```

* 3.NaN在布爾運算時被當作false。

```javascript
Boolean(NaN) // false
```

* 4.NaN與任何數（包括它自己）的運算，得到的都是NaN。

```javascript
NaN + 32 // NaN
NaN - 32 // NaN
NaN * 32 // NaN
NaN / 32 // NaN
```
### 2.Infinity

#### 1.Infinity內容

* 1.Infinity表示“無窮”，用來表示兩種場景。一種是一個正的數值太大，或一個負的數值太小，無法表示；另一種是非0數值除以0，得到Infinity。

```javascript
// 場景一
Math.pow(2, 1024)
// Infinity

// 場景二
0 / 0 // 0除以0會得到NaN
1 / 0 // 非0數值除以0，會返回Infinity
```

* 2.Infinity有正負之分，Infinity表示正的無窮，-Infinity表示負的無窮。

```javascript
Infinity === -Infinity // false

1 / -0 // -Infinity
-1 / -0 // Infinity
```
** 由於數值正向溢出（overflow）、負向溢出（underflow）和被0除，JavaScript 都不報錯，而是返回Infinity，所以單純的數學運算幾乎沒有可能拋出錯誤。

* 3.Infinity大于一切数值（除了NaN），-Infinity小于一切数值（除了NaN）。

```javascript
Infinity > 1000 // true
-Infinity < -1000 // true
```

* 4.Infinity與NaN比較，總是返回false。

```javascript
Infinity > NaN // false
-Infinity > NaN // false

Infinity < NaN // false
-Infinity < NaN // false
```

#### 2. Infinity運算規則

* 1.Infinity的四則運算，符合無窮的數學計算規則。

```javascript
5 * Infinity // Infinity
5 - Infinity // -Infinity
Infinity / 5 // Infinity
5 / Infinity // 0
```

* 2.0乘以Infinity，返回NaN；0除以Infinity，返回0；Infinity除以0，返回Infinity。

```javascript
0 * Infinity // NaN
0 / Infinity // 0
Infinity / 0 // Infinity
```

* 3.Infinity加上或乘以Infinity，返回的還是Infinity。

```javascript
Infinity + Infinity // Infinity
Infinity * Infinity // Infinity
```


* 4.Infinity減去或除以Infinity，得到NaN。

```javascript
Infinity - Infinity // NaN
Infinity / Infinity // NaN
```
* 5.Infinity與null計算時，null會轉成0，等同於與0的計算。

```javascript
null * Infinity // NaN
null / Infinity // 0
Infinity / null // Infinity
```
* 6.Infinity與undefined計算，返回的都是NaN。

```javascript
undefined + Infinity // NaN
undefined - Infinity // NaN
undefined * Infinity // NaN
undefined / Infinity // NaN
Infinity / undefined // NaN
```

### 3.isNaN()

* isNaN()方法可以用來判斷一個值是否為NaN

```javascript
isNaN(NaN) // true
isNaN(123) // false
```

* 2.isNaN只對數值有效，如果傳入其他值，會被先轉成數值。比如，傳入字符串的時候，字符串會被先轉成NaN，所以最後返回true，這一點要特別引起注意。也就是說，isNaN為true的值，有可能不是NaN，而是一個字符串。

```javascript
isNaN('Hello') // true
// 相當於
isNaN(Number('Hello')) // true
```

* 3.對於物件和陣列，isNaN也返回true。

```javascript
isNaN({}) // true
// 等同於
isNaN(Number({})) // true

isNaN(['xzy']) // true
// 等同於
isNaN(Number(['xzy'])) // true
```

* 4.對於空陣列和只有一個數值成員的數組，isNaN返回false。

```javascript
isNaN([]) // false
isNaN([123]) // false
isNaN(['123']) // false
```

* 5.使用isNaN之前，最好判斷一下數據類型。

```javascript
function myIsNaN(value) {
  return typeof value === 'number' && isNaN(value);
}
```

* 6.判斷NaN更可靠的方法是，利用NaN為唯一不等於自身的值的這個特點，進行判斷。

```javascript
function myIsNaN(value) {
  return value !== value;
}
```

### 4.isFinite()
* isFinite方法返回一個布爾值，表示某個值是否為正常的數值。

```javascript
isFinite(Infinity) // false
isFinite(-Infinity) // false
isFinite(NaN) // false
isFinite(undefined) // false
isFinite(null) // true
isFinite(-1) // true
```

** 除了Infinity、-Infinity、NaN和undefined這幾個值會返回false，isFinite對於其他的數值都會返回true。




## String 字串型別

### 1.任何植跟字串相加，接變為字串相加

    Ex: undefined +'A' ===> undefinedA
    Ex: null +'A' ===> nullA
    Ex: 0 +'A' ===> 0A
    Ex: NaN +'A' ===> NaNA

### 2.

## Boolean

### ''(空字串)、undefined、null、0、NaN 皆為false

## undefined


## null

# 函數

## 宣告方式

### 命名函數
```javascript
function fn(){
    //....
}
```

### 匿名函數
```javascript

    var fn = function(){
        //...
    }
```

## 函數裡有 arguments 接收我有串進來的參數


# 物件

## 語法
```javascript
{
    屬姓名1 : 值1
    屬姓名2 : 值2
    //....
}
```

## 創建的3種方式

### {}創建

```javascript
var obj={}
```

### new Object() 創建

```javascript
var obj= new Object()
```

### 利用構造函數進行創建

```javascript
var obj= 構造式名稱(參數1,參數2...)
```

# 構造函數

## 語法

```javascript
function 構造式名稱(型參1，型參2...){
    this.屬姓名1 = 型參1
    this.屬姓名2 = 型參2
    //....
}
```

## 用法
```javascript
new 構造式名稱(參數1,參數2...)
```

## 注意事項

*   1.構造函數名子首字母要大寫
*   2.我麼構造函數不需要RETURN就可以返回結果
*   3.我麼調用構造函數必需要使用new
*   4.我們只要new 構造式名稱(型參1，型參2...) ，調用函數就創建一個物件
*   5.構造函數物件的屬性和方法前面必需添加this

## new關鍵字的作用

### new在執行時會做以下4件事情
*   1.在內存中創建一個空物件
*   2.讓this指向這個空物件
*   3.執行構造函數裡面的代碼，給這個新物件添加屬性或方法
*   4.返回個新物件(所以構造函數裡面不需要return 就可以返回物件)

# Date內置物件

### 取得當前時間的毫秒數方式

*   1.通過valueOf()或getTime()
```javascript
var date = new Date()
console.log(date.valueOf())
console.log(date.getTime())
```
*   2.+new Date() 再new 前面加上加號，返回的就是總毫秒數 (常用)
```javascript
var date = +new Date()
console.log(date)
```
*   3.H5 新增的 獲得總好秒數方式
```javascript
console.log(Date().now())
```

### 倒數計時器例子
```javascript
    function conutDown(time){
        let nowTime = +new Date() //目前時間毫秒數
        let inputTime = +new Date(time) //用戶輸入時間總毫秒數
        let times = (inputTime - nowTime)/1000 //times紹愚時間總豪秒數

        let day = parseInt(times/60/60/24) //天
        let hour = parseInt(times/60/60%24) //時
        hour = hour < 10 ? '0'+ hour : hour
        let minute = parseInt(times/60%60) //分
        minute = minute < 10 ? '0'+ minute : minute
        let second = parseInt(times%60) //當前秒
        second = second < 10 ? '0'+ second : second
        return day + '天' + hour + '時' + minute + '分' + second + '秒'
    }
    console.log(conutDown('2021/07/20 23:59:59'))
```

# Array 內置物件

## 判別是否為陣列的兩種方式

### 1.以instanceof 判別
```javascript
    let arr = [1,2,3]
    let a = 3
    console.log(arr instanceof Array) //true
    console.log(a instanceof Array) //false

```

### 2.以Array.isArray判別 (H5新增的方法，IE9以上版本支持)

```javascript
    let arr = [1,2,3]
    let a = 3
    console.log(Array.isArray(arr)) //true
    console.log(Array.isArray(Array)) //false IE9以下不支援

```

## 異動陣列元素

### 1.push 後端追加新的元素

* 1.push 是可以給陣列後端追加新的元素
* 2.push() 參數直接寫陣列元素即可
* 3.push 完畢後，返回的結果新陣列的長度
* 4.原陣列也會發生變化
```javascript
    let arr = [0,1,2]
    let res = arr.push(3)
    console.log(res) //4
    console.log(arr) //[0,1,2,3]

```

### 2.unshift 前端追加新的元素

* 1.unshift 是可以給陣列前端追加新的元素
* 2.unshift() 參數直接寫陣列元素即可
* 3.unshift 完畢後，返回的結果新陣列的長度
* 4.原陣列也會發生變化
```javascript
    let arr = [0,1,2]
    let res = arr.unshift(3)
    console.log(res) //4
    console.log(arr) //[3,0,1,2]

```

### 3.pop 刪除最後一個元素

* 1.pop 是可以刪除陣列的最後一個元素，記住一次只能刪除一個元素
* 2.pop() 沒有參數
* 3.pop 完畢後，返回的結果是刪除的那個元素
* 4.原陣列也會發生變化

```javascript
    let arr = ['apple',1,2,3,'frank']
    console.log(arr.pop()) //['apple',1,2,3]
    console.log(arr) //frank

```

### 4.shift 刪除第一個元素

* 1.shift 是可以刪除陣列的第一個元素，記住一次只能刪除一個元素
* 2.shift() 沒有參數
* 3.shift 完畢後，返回的結果是刪除的那個元素
* 4.原陣列也會發生變化

```javascript
    let arr = ['apple',1,2,3,'frank']
    console.log(arr.shift()) //[1,2,3,'frank']
    console.log(arr) //apple

```

### 5.sort 陣列排序
```javascript
    //一般排序
    let arr = [11,25,1,3,5,34,24,26]
    arr.sort()
    console.log(arr) //[1, 11, 24, 25, 26, 3, 34, 5] 因為sort預設氏一個字元一個字元比
    arr = [11,25,1,3,5,34,24,26]
    arr.sort(function(a,b){
        // return a - b //升序
        return b - a //降序
    })
    console.log(arr) // [34, 26, 25, 24, 11, 5, 3, 1]

    //物件排序
    let arrObj = [
        {
            name:'apple',
            age:18
        },
        {
            name:'frank',
            age:33
        },
        {
            name:'amy',
            age:23
        }
    ]
    arrObj.sort(function(obj1,obj2){
        return obj1.age - obj2.age //升序
        // return obj2.age - obj1.age //降序
    })
    console.log(arrObj );
                        // 0: {name: "apple", age: 18}
                        // 1: {name: "amy", age: 23}
                        // 2: {name: "frank", age: 33}

```

## 陣列轉換成字串方式

### 1.toString()

```javascript
    let arr = [0,1,2,3]
    console.log(arr.toString()) // 0,1,2,3

```

### 2.join(分割符)

```javascript
    let arr1 = ['frank','apple','amy','jim']
    console.log(arr1.join()) // frank,apple,amy,jim
    console.log(arr1.join('-')) // frank-apple-amy-jim
    console.log(arr1.join('$')) // frank$apple$amy$jim

```

# String 內置物件

## 查找某字符在字串中出現幾次


```javascript
    let str = 'aoddosdsdosdsdosdsdo'

    let index = str.indexOf('o')
    let conut = 0
    while(index !==  -1){
        console.log(index);
        conut++
        index = str.indexOf('o',index+1)
    }
    console.log('o總共有'+conut +'個');

```