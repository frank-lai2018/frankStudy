# 1.第一個angular程式

*   ng-app(指令) : 告訴angular核心它管理當前標籤所包含的整個區域,並且會自動創建$rootScope根作用域對象
*   ng-model : 將當前輸入框的值與誰關聯(屬性名:屬性值), 並作為當前作用域對象($rootScope)的屬性
*   {{}} (表達式) : 顯示數據,從作用域對象的指定屬性名上取

```html
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title></title>
</head>
<body ng-app>

<input type="text" ng-model="username">
<p>您输入的内容是：<span>{{username}}</span></p>
<script src="../../js/angular-1.2.29/angular.js"></script>
<script type="text/javascript">

</script>
</body>
</html>
```

# 2.雙向數據綁定(ng-model)、數據初始化(ng-init)

*   1. 數據綁定: 數據從一個地方A轉移(傳遞)到另一個地方B, 而且這個操作由框架來完成
*   2. 雙向數據綁定: 數據可以從View(視圖層)流向Model（模型）, 也可以從Model流向View
    * 視圖(View): 也就是我們的頁面(主要是Andular指令和表達式)
    * 模型(Model) : 作用域對象(當前為$rootScope), 它可以包含一些屬性或方法
  * 當改變View中的數據, Model對象的對應屬性也會隨之改變: ng-model指令 數據從View==>Model
    * 當Model域對象的屬性發生改變時, 頁面對應數據隨之更新: {{}}表達式 數據從Model==>View
    * ng-model是雙向數據綁定, 而{{}}是單向數據綁定
*   3.ng-init 用來初始化當前作用域變量

```html
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title></title>
</head>
<body ng-app="" ng-init="name='tom'">

    <input type="text" ng-model="name">
    <p>name1：{{name}}</p>
    <input type="text" ng-model="name">
    <p>name2：{{name}}</p>

    <script type="text/javascript" src="../../js/angular-1.2.29/angular.js"></script>
</body>
</html>
```

#   3.1.5版本之後的anguler寫法，模塊和控制器

```html
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title></title>
</head>
<body ng-app="myApp">

<div ng-controller="MyController">
  <input type="text" ng-model="empName">
  <p>{{empName}}</p>
</div>
<div ng-controller="MyController1">
  <input type="text" ng-model="empName">
  <p>{{empName}}</p>
</div>

<script type="text/javascript" src="../../js/angular-1.5.5/angular.js"></script>
<script type="text/javascript">
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title></title>
</head>
<body ng-app="myApp">

<div ng-controller="MyController">
  <input type="text" ng-model="empName">
  <p>{{empName}}</p>
</div>
<div ng-controller="MyController1">
  <input type="text" ng-model="empName">
  <p>{{empName}}</p>
</div>

<script type="text/javascript" src="../../js/angular-1.5.5/angular.js"></script>
<script type="text/javascript">
  //創建當前應用的模塊物件
  // let module=angular.module("myApp",[]);
  // module.controller("MyController",function($scope){
  //   $scope.empName="frank";
  // });
  // module.controller("MyController1",function($scope){
  //   $scope.empName="apple";
  // });
  
  //方法鏈調用
  // angular
  //   .module("myApp",[])//模塊物件的方法執行完返回的就是模塊物件本身
  //   .controller("MyController",function($scope){
  //       $scope.empName="frank";
  //   })
  //   .controller("MyController1",function($scope){
  //       $scope.empName="apple";
  //   });

    //優化，顯示聲明依賴注入
    angular
      .module("myApp",[])//模塊物件的方法執行完返回的就是模塊物件本身
      .controller("MyController",["$scope",function($scope){
          $scope.empName="frank";
      }])
      .controller("MyController1",["$scope",function($scope){
          $scope.empName="apple";
      }]);  


</script>
</body>
</html>
```

# 4.常用指令

* ng-app: 指定模塊名，angular管理的區域
* ng-model： 雙向綁定，輸入相關標籤
* ng-init： 初始化數據
* ng-click： 調用作用域對象的方法（點擊時）
* ng-controller: 指定控制器構造函數名，內部會自動創建一個新的子作用域（外部的）
* ng-bind： 解決使用{{}}顯示數據閃屏（在很短時間內顯示{{}}）
* ng-repeat： 遍歷數組顯示數據， 數組有幾個元素就會產生幾個新的作用域
  * $index, $first, $last, $middle, $odd, $even
* ng-show: 布爾類型， 如果為true才顯示
* ng-hide: 布爾類型， 如果為true就隱藏

```html
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
</head>
<!--
1. Angular指令
	* Angular为HTML页面扩展的: 自定义标签属性或标签
	* 与Angular的作用域对象(scope)交互,扩展页面的动态表现力
2. 常用指令(一)
  * ng-app: 指定模块名，angular管理的区域
  * ng-model： 双向绑定，输入相关标签
  * ng-init： 初始化数据
  * ng-click： 调用作用域对象的方法（点击时）
  * ng-controller: 指定控制器构造函数名，内部会自动创建一个新的子作用域（外部的）
  * ng-bind： 解决使用{{}}显示数据闪屏（在很短时间内显示{{}}）
  * ng-repeat： 遍历数组显示数据， 数组有几个元素就会产生几个新的作用域
    * $index, $first, $last, $middle, $odd, $even
  * ng-show: 布尔类型， 如果为true才显示
  * ng-hide: 布尔类型， 如果为true就隐藏
-->

<body ng-app="myApp" >

  <div ng-controller="myController1">
    <div>
      <h1>計算總價(自動)</h1>
      單價: <input type="text" ng-model="num1">
      數量: <input type="text" ng-model="num2">
      <p>總價: {{num1 * num2}}</p>
    </div>
    <div>
      <h1>計算總價(手動)</h1>
      單價: <input type="text" ng-model="num3">
      數量: <input type="text" ng-model="num4">
      <button ng-click="getTotal()">計算</button>
      <p>總價: {{totalprice}}</p>
    </div>
    <div>
      <ul>
        <li ng-repeat="person in persons">{{$index}}---{{person.username}}------{{person.age}}</li>
      </ul>
    </div>

    <p ng-show="isshow">我是show</p>
    <p ng-hide="isshow">我是hide</p>
  </div>

<script type='text/javascript' src='../../js/angular-1.5.5/angular.js'></script>
<script type='text/javascript'>
  angular
    .module("myApp",[])
    .controller("myController1",["$scope",function(prams){
      prams.num1=20;
      prams.num2=1;
      prams.num3=10;
      prams.num4=1;
      prams.totalprice=10;
      prams.getTotal=function(){
        prams.totalprice=prams.num3 * prams.num4;
      };
      prams.persons=[
        {username:"frank",age:31},
        {username:"frank1",age:31},
        {username:"frank2",age:31},
        {username:"frank3",age:31},
        {username:"frank4",age:31}
      ];
      prams.isshow=false;
    }])

</script>

</body>
</html>
```

