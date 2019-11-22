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
  //創建當前應用的模塊物件
  // let module=angular.module("myApp",[]);
  // module.controller("MyController",function($scope){
  //   $scope.empName="frank";
  // });
  // module.controller("MyController1",function($scope){
  //   $scope.empName="apple";
  // });
  
  //方法鏈調用
  angular
    .module("myApp",[])//模塊物件的方法執行完返回的就是模塊物件本身
    .controller("MyController",function($scope){
        $scope.empName="frank";
    })
    .controller("MyController1",function($scope){
        $scope.empName="apple";
    });


</script>
</body>
</html>
```


