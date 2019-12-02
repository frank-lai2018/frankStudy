#   javascript boolean為false的資料型態

null、undefined、''(空字串)、0、NaN 這5個就是所謂的Falsy Family

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

    1.執行 function

    2.明確指定 this

    3.其執行結果是一個包裝過的函式，可以帶預包裝參數的預設值

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

