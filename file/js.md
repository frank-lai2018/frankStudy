# [javascript Base](javascript/javascriptBase.md )

# call  apply   bind

##  call

    call 是 Function 的內建函式，第一個參數傳入this，第二個參數開始開始傳入function需要的參數

* 功能

    1.執行 function

    2.明確指定 this

* 語法

    fn.call(this, arg1, arg2..., argn)

執行 function 這點，基本上只要用 () 就是可以取代的，但沒辦法使用 () 來指定 this。

```javascript
function add(a, b) {
  return a + b;
}
console.log(add(1, 2));
console.log(add.call(undefined, 1, 2));//沒有指定this基本上跟()功能一樣
```

##  apply

    apply也是 Function 的內建函式，執行作用跟call一樣，唯一的區別是傳入參數，第一個參數傳入this，第二個參數傳入參數陣列

* 功能

    1.執行 function

    2.明確指定 this

* 語法

    fn.apply(this, [arg1, arg2..., argn])

```javascript
function add(a, b) {
  return a + b;
}
console.log(add(1, 2));
console.log(add.apply(undefined, [1, 2]));//沒有指定this基本上跟()功能一樣
```

##  bind

* 第一個參數：

輸入的物件會被指定為目標函式中的 this

* 第二以後的參數：

會作為往後傳進目標函式的參數，如果目標函式中不需要參數則不要傳入即可

* 回傳：

回傳包裹後的目標函式。執行這個包裹函式後，可以幾乎確定 this 不會被改變，另外，也可以把先前傳入 bind 的參數 一並帶進目標函式中

* 功能

    1.明確指定 this

    2.其執行結果是一個包裝過的函式，可以帶預包裝參數的預設值

* 語法

    fn.bind(this, arg1, arg2..., argn)

```javascript
function add(a, b) {
  return a + b;
}
var add1 = add.bind(undefined, 1);
console.log(add1(2));			// 3
console.log(add1(4));			// 5
```

#   取得所有全域變數的方法
```
globalThis
```

#   物件

##  定義物件私有屬性、get set方法

*   定義set 方法 ，在obj.xxx=xxx即會調用
*   定義get 方法 ，在obj.xxx即會調用

*   使用Object.defineProperty定義物件屬性

```javascript
function UserName(name,age){
    Object.defineProperty(this,'name',{
            set:() => name,
            get:() => name
        });
    Object.defineProperty(this,'age',{
            set:() => age,
            get:() => age
        });

    this.toString = () => `${this.name}--${this.age}`

}

let user1 = new UserName('frank',31);
let user2 = new UserName('apple',31);

console.log(user1.name);
console.log(user2.name);
```


*   使用Object.defineProperties批量定義物件屬性


```javascript
function UserName(name,age){
    Object.defineProperties(this,{
        name:{
            set:() => name,
            get:() => name
        },
        age:{
            set:() => age,
            get:() => age
        },
    });

    this.toString = () => `${this.name}--${this.age}`

}

let user1 = new UserName('frank',31);
let user2 = new UserName('apple',31);

console.log(user1.name);
console.log(user2.name);
```

# 物件特性

    value: 物件屬性值,
    writable: 屬性值是否可以修改,
    enumerable: 屬姓名稱是否可以列舉,
    configurable: 可否用delete刪除屬性，或是用Object.defineProperty，Object.defineProperties修改屬性的屬性設定

    Object.getOwnPropertyDescriptor(物件名,屬姓名):可以取得屬性的屬性設定

*   使用Object.defineProperty或Object.defineProperties設置屬性，預設屬性特性皆為false

*   如果設置了set get方法，代表屬性值要自己設定，故不可以再設定value及writable屬性的屬性

```javascript
function UserName(name,age){
    Object.defineProperty(this,'name',{
            // set:() => name,
            get:() => name
        });
    Object.defineProperty(this,'age',{
            // set:() => age,
            get:() => age
        });
    Object.defineProperty(this,'status',{
            // set:() => age,
            value: 10,
            writable: false,
            enumerable: false,
            configurable: false
        });

    this.toString = () => `${this.name}--${this.age}`

}

let user1 = new UserName('frank',31);
let user2 = new UserName('apple',31);
user1.name ='frank1';
console.log(user1.status);
console.log(user2.status);

console.log(Object.getOwnPropertyDescriptor(user1,'status'));
```

# 變數提升
## 在變量定義之前可以訪問此變量，其值為undefined。

	用var聲明的變數具有，變數提升，只是在宣告行前使用會取到undefined的值

	var foo = 3

	以下也是變數提升不是函式提升，因為用Var宣告了
	var foo =function (){}

# 函數提升
## 在函數定義之前就可以去調用執行這個函數。

	一定要用function 聲明的才有函數提升

		function foo(){}

## 變數提升完再執行函數提升

# 物件

實例物件的隱式原型等於建構函式的顯示原型


# 全局執行上下文
* 在執行全局代碼前將window確定為全局執行上下文
* 對全局數據進行預處理:(以下依順序執行)
    * var定義的全局變量==>undefined, 添加為window的屬性
    * function聲明的全局函數==>賦值(fun), 添加為window的方法
    * this==>賦值(window)
* 開始執行全局代碼

# 函數執行上下文
* 在調用函數, 準備執行函數體之前, 創建對應的函數執行上下文對象
* 對局部數據進行預處理
    * 形參變量==>賦值(實參)==>添加為執行上下文的屬性
    * arguments==>賦值(實參列表), 添加為執行上下文的屬性
    * var定義的局部變量==>undefined, 添加為執行上下文的屬性
* function聲明的函數 ==>賦值(fun), 添加為執行上下文的方法
* this==>賦值(調用函數的對象)
* 開始執行函數體代碼

```javascript
 //EX1:
 function a() {}
  var a;
  console.log(typeof a) //function


 //EX2:
  if (!(b in window)) {
    var b = 1;
  }
  console.log(b)//undefined

 //EX3:
  var c = 1
  function c(c) {
    console.log(c)
    var c = 3
  }
  c(2) //報錯，c不是一個函式
```

# 閉包

## 1.在函式定義執行時創建，需有嵌套函式，才產生閉包，且閉包內部變數，是內部函式引用到外部函式的變量時才產生

## 2.閉包生命週期:
    1.函式定義時創建
    2.內部函是沒人引用，成為垃圾函式時消失於內存

## 3.閉包作用，用來包裝函式理的變數，使其可以在函式執行完後依然存在於內存，並依此函式對外提供的方法調用變數

# [js event](javascript/jsevent.md)

# [js httpRequest](javascript/http.md)