# 1.let

a.宣告

1.作用：
   *與var類似，用於聲明一個變量
2.特點：
   *在塊作用域內有效
   *不能重複聲明
   *不會預，不存在提升
3.應用：
   *循環遍歷加監聽
   *使用let取代var是趨勢

```javascript
    //console.log(age);// age is not defined
    let age = 13;
    //let age = 13;不能重複宣告
    console.log(age);
    let btns = document.getElementsByTagName('button');
    for(let i = 0;i<btns.length;i++){
            btns[i].onclick = function () {
               alert(i);
            }
    }
```

b.變數的賦值

1.理解：
  *從對像或數組中提取數據，並賦值給變量（多個）
2.對象的解構賦值
  讓{n，a} = {n：'tom'，a：12}
3.副本的解構賦值
  令[a，b] = [1，'atguigu'];
4.用途
  *給多個形參賦值

```javascript
   let obj = {name : 'frank', age : 39};
//    let name = obj.name;
//    let age = obj.age;
//    console.log(name, age);
    //物件解構賦值
    let {age} = obj;
    console.log(age);
//    let {name, age} = {name : 'frank', age : 39};
//    console.log(name, age);

    //3. 陣列的解構賦值  不常使用
    let arr = ['abc', 23, true];
    let [a, b, c, d] = arr;
    console.log(a, b, c, d);
    //console.log(e);
    function person(p) {//不用解構賦值
        console.log(p.name, p.age);
    }
    person(obj);

    function person1({name, age}) {
     console.log(name, age);
    }
    person1(obj);
```





# 2.const



1.作用：
   *定義一個常量
2.特點：
   *不能修改
   *其他特點同let
3.應用：
   *保存不用改變的數據

# 3.模板字串

1.模板字符串：簡化字符串的拼接
   *模板字符串必須用``包含
   *變化的部分使用$ {xxx}定義

```javascript
    let obj = {
        name : 'frank',
        age : 44
    };
    console.log('我叫:' + obj.name + ', 我的年齡是：' + obj.age);

    console.log(`我叫:${obj.name}, 我的年齡是：${obj.age}`);
```

# 4.物件寫法簡化

簡化的物件寫法
*省略同名的屬性值
*省略方法的功能
*例如：
   令x = 1;
   令y = 2;
   let obj= {
     x，
     y，
     setX（x）{this.x = x}
   };

```javascript
    let x = 3;
    let y = 5;
    //es5 寫法
//    let obj = {
//        x : x,
//        y : y,
//        getPoint : function () {
//            return this.x + this.y
//        }
//    };
    //es6 簡化寫法
    let obj = {
        x,
        y,
        getPoint(){
            return this.x
        }
    };
    console.log(obj, obj.getPoint());
```

# 5.箭頭函數

*作用：定義匿名函數
*基本語法：
   *沒有參數：（）=> console.log（'xxxx'）
   *一個參數：i => i + 2
   *大於一個參數：（i，j）=> i + j
   *函數體不用大括號：默認返回結果
   *函數體如果有多個語句，需要用{}包圍，若有需要返回的內容，需要手動返回
*使用場景：多用於定義局部函數

*箭頭函數的特點：
     1，簡潔
     2，箭頭函數沒有自己的這個，箭頭函數的這個不是調用的時候決定的，而是在定義的時候處在的對象就是它的這個
    3，箭頭函數的這個看外層的是否有函數，
         如果有，外層函數的這個就是內部箭頭函數的這個，
         如果沒有，則這是窗口。

```javascript
let fun = function () {
       console.log('fun()');
   };
   fun();
   //沒有參數，並且函數體只有一條語句
   let fun1 = () => console.log('fun1()');
    fun1();
   console.log(fun1());
    //一個參數，並且函數體只有一條語句
    let fun2 = x => x;
    console.log(fun2(5));
    //參數是一個以上
    let fun3 = (x, y) => x + y;
    console.log(fun3(25, 39));//64

    //函數體有多條語句
    let fun4 = (x, y) => {
        console.log(x, y);
        return x + y;
    };
    console.log(fun4(34, 48));//82

    setTimeout(() => {
        console.log(this);
    },1000)

   let btn = document.getElementById('btn');
   //沒有箭頭函數
   btn.onclick = function () {
       console.log(this);//btn
   };
   //箭頭函數
   let btn2 = document.getElementById('btn2');

    let obj = {
        name : 'kobe',
        age : 39,
        getName : () => {
            btn2.onclick = () => {
                console.log(this);//obj
            };
        }
    };
    obj.getName();


 function Person() {
     this.obj = {
         showThis : () => {
             console.log(this);
         }
     }
 }
    let fun5 = new Person();
    fun5.obj.showThis();
```

# 6.點點點運算符

*用途
1.休息
     *已取代取代參數，但比例取代參數靈活，只能是最後部分形參參數
   函數add（... values）{
     令總和= 0;
     for（值的值）{
       總和==值;
     }
     返回總和
   }
2.擴展運算符
   令arr1 = [1,3,5];
   令arr2 = [2，... arr1,6];
   arr2.push（... arr1）;

```javascript
    function fun(...values) {
        console.log(arguments);
//        arguments.forEach(function (item, index) {
//            console.log(item, index);
//        });
        console.log(values);
        values.forEach(function (item, index) {
            console.log(item, index);
        })
    }
    fun(1,2,3);

    let arr = [2,3,4,5,6];
    let arr1 = ['abc',...arr, 'fg'];
    console.log(arr1);

```



# 7.Promise

1.理解：
  * Promise物件：代表了未來某個將要發生的事件（通常是一個異步操作）
    *有了promise物件，可以將異步操作以同步的流程表達出來，避免了層層層疊的替換函數（俗稱'放置地獄'）
  * ES6的Promise是一個構造函數，用於生成promise實例
2.使用promise基本步驟（2步）：
    *創建承諾物件
    讓諾言=新的承諾（（解決，拒絕）=> {
        //初始化promise狀態為待定
      //執行異步操作
      if（異步操作成功）{
        resolve（value）; //修改promise的狀態為fullfilled
      }其他{
        reject（errMsg）; //修改promise的狀態為已拒絕
      }
    }）
    *調用promise的then（）
    promise.then（function（
      結果=> console.log（result），
      errorMsg =>警報（errorMsg）
    ））
3. promise物件的3個狀態
    *待定：初始化狀態
    *完整：成功狀態
    *被拒絕：失敗狀態
4.應用：
    *使用Promise實現超時處理

  *使用promise封裝處理ajax請求
    讓request = new XMLHttpRequest（）;
    request.onreadystatechange = function（）{
    }
    request.responseType ='json';
    request.open（“ GET”，url）;
    request.send（）;

```javascript
    //創建一個promise實例物件
    let promise = new Promise((resolve, reject) => {
        //初始化promise的狀態為pending---->初始化狀態
        console.log('1111');//同步執行
        //啟動異步任務
        setTimeout(function () {
            console.log('3333');
            //resolve('atguigu.com');//修改promise的狀態pending---->fullfilled（成功狀態）
            reject('xxxx');//修改promise的狀態pending----->rejected(失敗狀態)
        },1000)
    });
    promise.then((data) => {
        console.log('成功了。。。' + data);
    }, (error) => {
        console.log('失敗了' + error);
    });
    console.log('2222');
```

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title></title>
	</head>
	<body>
		<div id="showDetail"></div>
	</body>
	<script>
        //定義一個請求URL的方法
		function getUrlResponse(url){
            //創建一個promise物件
			let promise = new Promise((resolve, reject) => {
                //初始化promise狀態為pending
                //啟動異部任務
				let request = new XMLHttpRequest();
				request.onreadystatechange = function(){
					if(request.readyState == 4){
						if(request.status == 200){
							resolve(request.response);
						}else{
							reject("連接錯誤.....");
						}
					}
				}
				request.responseType="json";//設置返回的數據類型
				request.open("GET",url);//規定請求的方法，創建連接
				request.send();//發送
			});
			return promise;
		}

		let weatherUrl="https://ptx.transportdata.tw/MOTC/v2/Bus/RealTimeByFrequency/City/Taipei?$top=30&$format=JSON";
		getUrlResponse(weatherUrl)
			.then((res) => {
				document.getElementById("showDetail").innerText=JSON.stringify(res);
			},(errors) => {
				document.getElementById("showDetail").innerText=errors;
			});

		
	</script>
</html>

```



