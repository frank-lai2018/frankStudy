# Node介紹

## Node.js是什麼

- Node.js是JavaScript 運行時
- 通俗易懂的講，Node.js是JavaScript的運行平台
- Node.js既不是語言，也不是框架，它是一個平台
- 瀏覽器中的JavaScript
  - EcmaScript
    - 基本語法
    - if
    - var
    - function
    - Object
    - Array
  - Bom
  - Dom
- Node.js中的JavaScript
  - 沒有Bom，Dom
  - EcmaScript
  - 在Node中這個JavaScript執行環境為JavaScript提供了一些服務器級別的API
    - 例如文件的讀寫
    - 網絡服務的構建
    - 網絡通信
    - http服務器
- 構建與Chrome的V8引擎之上
  - 代碼只是具有特定格式的字符串
  - 引擎可以認識它，幫你解析和執行
  - Google Chrome的V8引擎是目前公認的解析執行JavaScript代碼最快的
  - Node.js的作者把Google Chrome中的V8引擎移植出來，開發了一個獨立的JavaScript運行時環境
- Node.js uses an envent-driven,non-blocking I/O mode that makes it lightweight and efficent.
  -  envent-driven	事件驅動
  - non-blocking I/O mode   非阻塞I/O模型（異步）
  - ightweight and efficent.   輕量和高效
- Node.js package ecosystem,npm,is the larget scosystem of open sourcr libraries in the world
  - npm 是世界上最大的開源生態系統
  - 絕大多數JavaScript相關的包都存放在npm上，這樣做的目的是為了讓開發人員更方便的去下載使用
  - npm install jquery

## Node能做什麼

- web服務器後台
- 命令行工具
  - npm(node)
  - git(c語言)
  - hexo（node）
  - ...
- 對於前端工程師來講，接觸最多的是它的命令行工具
  - 自己寫的很少，主要是用別人第三方的
  - webpack
  - gulp
  - npm

# node 中的路徑問題

## 1.`模塊中的路徑標示就是相對於當前文件模塊，不受執行node命令所處的路徑影響`

## 2.`文件操作時(fs模塊)，其相對路徑是以執行node命令所處的路徑為根，去找文件，故通成會跟path模塊中的path.join()一啟用，使其改為絕對路徑`
 
 


# 開始

## 安裝Node環境

- 查看Node環境的版本號
- 下載：https://nodejs.org/en/
- 安裝：
  - 傻瓜式安裝，一路`next`
  - 安裝過再次安裝會升級
- 確認Node環境是否安裝成功
  - 查看node的版本號：`node --version`
  - 或者`node -v`
- 配置環境變量

## 解析執行JavaScript

1. 創建編寫JavaScript腳本文件
2. 打開終端，定位腳本文件的所屬目錄
3. 輸入`node  文件名`執行對應的文件

注意：文件名不要用`node.js`來命名，也就是說除了`node`這個名字隨便起，最好不要使用中文。

## 文件的讀寫 (fs 模塊)

文件讀取:

```javascript
//瀏覽器中的JavaScript是沒有文件操作能力的
//但是Node中的JavaScript具有文件操作能力
//fs是file-system的簡寫，就是文件系統的意思
//在Node中如果想要進行文件的操作就必須引用fs這個核心模塊
//在fs這個和興模塊中，就提供了人所有文件操作相關的API
//例如 fs.readFile就是用來讀取文件的

//  1.使用fs核心模塊
var fs = require('fs');

// 2.讀取文件
fs.readFile('./data/a.txt',function(err,data){
   if(err){
        console.log('文件讀取失敗');
   }
    else{
         console.log(data.toString());
    }
})
```

文件寫入：

```javascript
//  1.使用fs核心模塊
var fs = require('fs');

// 2.將數據寫入文件
fs.writeFile('./data/a.txt','我是文件寫入的信息',function(err,data){
   if(err){
        console.log('文件寫入失敗');
   }
    else{
         console.log(data.toString());
    }
})
```

## http

服務器：

```javascript
// 1.加載http核心模塊
var http = require('http');

// 2.使用http.createServer()創建一個web服務器
var server = http.createServer();

// 3.服務器要做的事兒
// 提供服務：對數據服務
// 發請求
//	接收請求
//	處理請求
//	反饋（發送響應）
//	當客戶端請求過來，就會自動觸發服務器的request請求事件，然後執行第二個參數：回調處理函數
server.on('request',function(){
    console.log('收到客戶的請求了')
})

// 4.綁定端口號，啟動服務
server.listen(3000,function(){
    console.log('runing...')
})

```

## 301和302 區別
* 1.`301永久重定向,瀏覽器會記住，下次訪問時會重本地緩存中讀取對應的URL，只接跳轉，不會再發原始還未重定向的URL請求`
* 2.`302臨時重定向，每次都會發原始還未重定向的URL請求，取得重新定向URL，再發重新定向URL出去`

# Node中的模塊系統

使用Node編寫應用程序主要就是在使用：

- EcmaScript語言
  - 和瀏覽器一樣，在Node中沒有Bom和Dom

- 核心模塊
  - 文件操作的fs
  - http服務操作的http
  - url路徑操作模塊
  - path路徑處理模塊
  - os操作系統信息
- 第三方模塊
  - art-template
  - 必須通過npm來下載才可以使用
- 自己寫的模塊
  - 自己創建的文件



## 什麼是模塊化

- 文件作用域(模塊是獨立的，在不同的文件使用必須要重新引用)【在node中沒有全局作用域，它是文件模塊作用域】
- 通信規則
  - 加載require
  - 導出exports

## CommonJS模塊規範

在Node中的JavaScript還有一個重要的概念，模塊系統。

- 模塊作用域

- 使用require方法來加載模塊

- 使用exports接口對象來導出模板中的成員

  ### 加載`require`

  語法：

  ~~~java
  var 自定義變量名 = require('模塊')
  ~~~

  作用：

  - 執行被加載模塊中的代碼
  - 得到被加載模塊中的`exports`導出接口對象

  ### 導出`exports`

  - Node中是模塊作用域，默認文件中所有的成員只在當前模塊有效

  - 對於希望可以被其他模塊訪問到的成員，我們需要把這些公開的成員都掛載到`exports`接口對像中就可以了

    導出多個成員（必須在對像中）：

    ```javascript
    exports.a = 123;
    exports.b = function(){
        console.log('bbb')
    };
    exports.c = {
        foo:"bar"
    };
    exports.d = 'hello';
    ```

    

    導出單個成員（拿到的就是函數，字符串）：

    ```javascript
    module.exports = 'hello';
    ```

    以下情況會覆蓋：

    ```javascript
    module.exports = 'hello';
    //後者會覆蓋前者
    module.exports = function add(x,y) {
        return x+y;
    }
    ```

    也可以通過以下方法來導出多個成員：

    ```javascript
    module.exports = {
        foo = 'hello',
        add:function(){
            return x+y;
        }
    };
    ```

## 模塊原理

* 1.`在 Node 中，每個模塊內部都有一個自己的 module 對象`
* 2.`該 module 對像中，有一個成員叫：exports 也是一個對象，也就是說如果你需要對外導出成員，只需要把導出的成員掛載到 module.exports 中`
* 3.`每次導出接口成員的時候都通過 module.exports.xxx = xxx 的方式很麻煩，所以Node 為了簡化你的操作，專門提供了一個變量：exports 等於 module.exports`

## `node 底層提供的程式，每個模塊都有`
```javascript
//每個模塊都會預先定義一個物件module
var module = {
  exports: {
  }
  //....
}
// 每個模塊中還有這麼一句代碼
var exports = module.exports

//每個模塊最後會 return module.exports
return module.exports

```

exports是`module.exports`的一個引用：

```javascript
console.log(exports === module.exports);	//true

exports.foo = 'bar';

//等價於
module.exports.foo = 'bar';
```

`當給exports重新賦值後，exports！ = module.exports.`

`最終return的是module.exports,無論exports中的成員是什麼都沒用。 `

```javascript
真正去使用的時候：
	導出單個成員：exports.xxx = xxx;
	導出多個成員：module.exports 或者 modeule.exports = {};
```



# require的加载规则

- 核心模块

  - 模块名

- 第三方模块

  - 模块名

- 用户自己写的

  - 路径




## require的加載規則：

- 1.優先從緩存加載

- 2.判別模塊標識符(require('模塊標識符'))

  - a.核心模塊
  - b.自己寫的模塊（路徑形式的模塊）
  - c.第三方模塊（node_modules）
    - 第三方模塊的標識就是第三方模塊的名稱（不可能有第三方模塊和核心模塊的名字一致）
    - 查找方式：
      - 找node_modules/express/package.json裡的main屬性指向的文件
      - 如果package.json不存在，或者main屬性不存在或文件名不正確，則查找備用選擇項：index.js
      - 如果以上條件都不滿足，則繼續進入上一級目錄中的node_modules按照上面的規則依次查找，直到當前文件所屬此盤根目錄都找不到最後報錯



```javascript
// 如果非路徑形式的標識
// 路徑形式的標識：
    // ./  當前目錄 不可省略
    // ../  上一級目錄  不可省略
    //  /xxx也就是D:/xxx
    // 帶有絕對路徑幾乎不用（D:/a/foo.js）
// 首位表示的是當前文件模塊所屬磁盤根目錄
// require('./a'); 


// 核心模塊
// 核心模塊本質也是文件，核心模塊文件已經被編譯到了二進製文件中了，我們只需要按照名字來加載就可以了
require('fs'); 

// 第三方模塊
// 凡是第三方模塊都必須通過npm下載（npm i node_modules），使用的時候就可以通過require('包名')來加載才可以使用
// 第三方包的名字不可能和核心模塊的名字是一樣的
// 既不是核心模塊，也不是路徑形式的模塊
//      先找到當前文所述目錄的node_modules
//      然後找node_modules/art-template目錄
//      node_modules/art-template/package.json
//      node_modules/art-template/package.json中的main屬性
//      main屬性記錄了art-template的入口模塊
//      然後加載使用這個第三方包
//      實際上最終加載的還是文件

//      如果package.json不存在或者mian指定的入口模塊不存在
//      則node會自動找該目錄下的index.js
//      也就是說index.js是一個備選項，如果main沒有指定，則加載index.js文件
//      
        // 如果條件都不滿足則會進入上一級目錄進行查找
// 注意：一個項目只有一個node_modules，放在項目根目錄中，子目錄可以直接調用根目錄的文件
var template = require('art-template');

```

# npm

- node package manage(node包管理器)
- 通過npm命令安裝jQuery包（npm install --save jquery），在安裝時加上--save會主動生成說明書文件信息（將安裝文件的信息添加到package.json裡面）

### npm網站

> ​	npmjs.com	網站   是用來搜索npm包的

### npm命令行工具

npm是一個命令行工具，只要安裝了node就已經安裝了npm。

npm也有版本概念，可以通過`npm --version`來查看npm的版本

升級npm(自己升級自己)：

```javascript
npm install --global npm
```

### 常用命令

- npm init(生成package.json說明書文件)
  - npm init -y(可以跳過嚮導，快速生成)
- npm install
  - 一次性把dependencies選項中的依賴項全部安裝
  - 簡寫（npm i）
- npm install 包名
  - 只下載
  - 簡寫（npm i 包名）
- npm install --save 包名
  - 下載並且保存依賴項（package.json文件中的dependencies選項）
  - 簡寫（npm i  包名）
- npm uninstall 包名
  - 只刪除，如果有依賴項會依然保存
  - 簡寫（npm un 包名）
- npm uninstall --save 包名
  - 刪除的同時也會把依賴信息全部刪除
  - 簡寫（npm un 包名）
- npm help
  - 查看使用幫助
- npm 命令 --help
  - 查看具體命令的使用幫助（npm uninstall --help）

### 解決npm被牆問題

npm存儲包文件的服務器在國外，有時候會被牆，速度很慢，所以需要解決這個問題。

> https://developer.aliyun.com/mirror/NPM?from=tnpm淘寶的開發團隊把npm在國內做了一個鏡像（也就是一個備份）。
>

安裝淘寶的cnpm：

```javascript
npm install -g cnpm --registry=https://registry.npm.taobao.org;
```



```shell
#在任意目錄執行都可以
#--global表示安裝到全局，而非當前目錄
#--global不能省略，否則不管用
npm install --global cnpm
```

安裝包的時候把以前的`npm`替換成`cnpm`。

```shell
#走國外的npm服務器下載jQuery包，速度比較慢
npm install jQuery;

#使用cnpm就會通過淘寶的服務器來下載jQuery
cnpm install jQuery;
```

如果不想安裝`cnpm`又想使用淘寶的服務器來下載：

```shell
npm install jquery --registry=https://npm.taobao.org;
```

但是每次手動加參數就很麻煩，所以我們可以把這個選項加入到配置文件中：

```shell
npm config set registry https://npm.taobao.org;

#查看npm配置信息
npm config list;
```

只要經過上面的配置命令，則以後所有的`npm install`都會通過淘寶的服務器來下載

# package.json

每一個項目都要有一個`package.json`文件（包描述文件，就像產品的說明書一樣）

這個文件可以通過`npm init`自動初始化出來

```javascript

D:\code\node中的模塊系統>npm init
This utility will walk you through creating a package.json file.
It only covers the most common items, and tries to guess sensible defaults.

See `npm help json` for definitive documentation on these fields
and exactly what they do.

Use `npm install <pkg>` afterwards to install a package and
save it as a dependency in the package.json file.

Press ^C at any time to quit.
package name: (node中的模塊系統)
Sorry, name can only contain URL-friendly characters.
package name: (node中的模塊系統) cls
version: (1.0.0) //項目版本
description: 這是一個測試項目
entry point: (main.js) //設定入口文件
test command: //
git repository:
keywords: //npm上面的KEY
author: frank//作者
license: (ISC)
About to write to D:\code\node中的模塊系統\package.json:

{
  "name": "cls",
  "version": "1.0.0",
  "description": "這是一個測試項目",
  "main": "main.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "frank",
  "license": "ISC"
}


Is this OK? (yes) yes
```

對於目前來講，最有用的是`dependencies`選項，可以用來幫助我們保存第三方包的依賴信息。

如果`node_modules`刪除了也不用擔心，只需要在控制面板中`npm install`就會自動把`package.json`中的`dependencies`中所有的依賴項全部都下載回來。

- 建議每個項目的根目錄下都有一個`package.json`文件
- 建議執行`npm install 包名`的時候都加上`--save`選項，目的是用來保存依賴信息

## package.json和package-lock.json

npm 5以前是不會有`package-lock.json`這個文件

npm5以後才加入這個文件

當你安裝包的時候，npm都會生成或者更新`package-lock.json`這個文件

- npm5以後的版本安裝都不要加`--save`參數，它會自動保存依賴信息
- 當你安裝包的時候，會自動創建或者更新`package-lock.json`文件
- `package-lock.json`這個文件會包含`node_modules`中所有包的信息（版本，下載地址。。。）
  - 這樣的話重新`npm install`的時候速度就可以提升
- 從文件來看，有一個`lock`稱之為鎖
  - 這個`lock`使用來鎖版本的
  - 如果項目依賴了`1.1.1`版本
  - 如果你重新install其實會下載最細版本，而不是`1.1.1`
  - ``package-lock.json``的另外一個作用就是鎖定版本號，防止自動升級

## path路徑操作模塊

> 參考文檔：https://nodejs.org/docs/latest-v13.x/api/path.html

- path.basename：獲取路徑的文件名，默認包含擴展名
- path.dirname：獲取路徑中的目錄部分
- path.extname：獲取一個路徑中的擴展名部分
- path.parse：把路徑轉換為對象
  - root：根路徑
  - dir：目錄
  - base：包含後綴名的文件名
  - ext：後綴名
  - name：不包含後綴名的文件名
- path.join：拼接路徑
- path.isAbsolute：判斷一個路徑是否為絕對路徑![image-20200315150610001](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200315150610001.png)

# Node中的其它成員(__dirname,__filename)

在每個模塊中，除了`require`,`exports`等模塊相關的API之外，還有兩個特殊的成員：

- `__dirname`，是一個成員，可以用來**動態**獲取當前文件模塊所屬目錄的絕對路徑

- `__filename`，可以用來**動態**獲取當前文件的絕對路徑（包含文件名）

- `__dirname`和`filename`是不受執行node命令所屬路徑影響的

  ![image-20200315151551873](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200315151551873.png)

在文件操作中，使用相對路徑是不可靠的，因為node中文件操作的路徑被設計為相對於執行node命令所處的路徑。

所以為了解決這個問題，只需要把相對路徑變為絕對路徑（絕對路徑不受任何影響）就可以了。

就可以使用`__dirname`或者`__filename`來幫助我們解決這個問題

在拼接路徑的過程中，為了避免手動拼接帶來的一些低級錯誤，推薦使用`path.join()`來輔助拼接

```javascript
var fs = require('fs');
var path = require('path');

// console.log(__dirname + 'a.txt');
// path.join方法會將文件操作中的相對路徑都統一的轉為動態的絕對路徑
fs.readFile(path.join(__dirname + '/a.txt'),'utf8',function(err,data){
	if(err){
		throw err
	}
	console.log(data);
});
```

> 補充：模塊中的路徑標識和這裡的路徑沒關係，不受影響（就是相對於文件模塊）

> **注意：**
>
> **模塊中的路徑標識和文件操作中的相對路徑標識不一致**
>
> **模塊中的路徑標識就是相對於當前文件模塊，不受node命令所處路徑影響**

# Express（快速的）

作者：Tj

原生的http在某些方面表現不足以應對我們的開發需求，所以就需要使用框架來加快我們的開發效率，框架的目的就是提高效率，讓我們的代碼高度統一。

在node中有很多web開發框架。主要學習express

- `http://expressjs.com/`,其中主要封裝的是http。

- ```javascript
  // 1 安裝
  // 2 引包
  var express = require('express');
  // 3 創建服務器應用程序
  //      也就是原來的http.createServer();
  var app = express();
  
  // 公開指定目錄
  // 只要通過這樣做了，就可以通過/public/xx的方式來訪問public目錄中的所有資源
  // 在Express中開放資源就是一個API的事
  app.use('/public/',express.static('/public/'));
  
  //模板引擎在Express中開放模板也是一個API的事
  
  // 當服務器收到get請求 / 的時候，執行回調處理函數
  app.get('/',function(req,res){
      res.send('hello express');
  })
  
  // 相當於server.listen
  app.listen(3000,function(){
      console.log('app is runing at port 3000');
  })
  ```


### 學習Express

#### 起步

```javascript
npm install express
```

```javascript
// 引入express
var express = require('express');

// 1. 創建app
var app = express();

//  2. 
app.get('/',function(req,res){
    // 1
    // res.write('Hello');
    // res.write('World');
    // res.end()

    // 2
    // res.end('hello world');

    // 3
    res.send('hello world');
})

app.listen(3000,function(){
    console.log('express app is runing...');
})
```

##### 基本路由

路由：

- 請求方法

- 請求路徑
- 請求處理函數

get:

```javascript
//當你以get方法請求/的時候，執行對應的處理函數
app.get('/',function(req,res){
    res.send('hello world');
})
```

post:

```javascript
//當你以post方法請求/的時候，執行對應的處理函數
app.post('/',function(req,res){
    res.send('hello world');
})
```

##### Express靜態服務API

-  開放靜態資源
   - 1.當以/public/開頭的時候，去./public/目錄中找對應資源
   ```javascript
     // 訪問：http://127.0.0.1:3000/public/login.html
    app.use('/public/',express.static('./public/')); 
   ```

   - 2.當省略第一個參數的時候，可以通過省略/public的方式來訪問
    ```javascript
      // 訪問：http://127.0.0.1:3000/login.html
      app.use(express.static('./public/'));   
    ```

   - 3.取URL路徑別名
   ```javascript
    // a相當於public的別名
    //訪問：http://127.0.0.1:3000/a/login.html
    app.use('/a/',express.static('./public/')); 
    //訪問：http://127.0.0.1:3000/static/css/login.html
    app.use('/static/css',express.static('./public/')); 
   ```



```javascript
// app.use不僅僅是用來處理靜態資源的，還可以做很多工作(body-parser的配置)
app.use(express.static('public'));

app.use(express.static('files'));

app.use('/stataic',express.static('public'));
```

## `使用模板引擎`

### `art-templete`

##### 在Express中配置使用`art-templete`模板引擎

- [art-template官方文檔](https://aui.github.io/art-template/)
- 在node中，有很多第三方模板引擎都可以使用，不是只有`art-template`
  - 還有ejs，jade（pug），handlebars，nunjucks

安裝：

```shell
npm install --save art-template
npm install --save express-art-template

//兩個一起安裝
npm i --save art-template express-art-template
```

配置：

* 1.第一個參數是指定文件後綴名，如果指定art的話，只會讀取.art文件
```javascript
app.engine('html', require('express-art-template'));
```

使用：

* 2.使用express 提供的render方法，去渲染視圖，如果沒有綁定引擎此方法不能用

```javascript
app.get('/',function(req,res){
    // express默認會去views目錄找index.html
    res.render('index.html',{
           title:'hello world'     
    });
})
```

* 3.如果希望修改默認的`views`視圖渲染存儲目錄，可以：

```javascript
// 第一個參數views千萬不要寫錯
app.set('views',目錄路徑);
```

##### 在Express中獲取表單請求數據

###### 獲取get請求數據：

* 1.Express內置了一個api，可以直接通過`req.query`來獲取數據
* 2.`req.query`只能拿到get請求的數據

```javascript
 var comment = req.query;
```

###### 獲取post請求數據：

在Express中沒有內置獲取表單post請求體的api，這裡我們需要使用一個第三方包`body-parser`來獲取數據。

安裝：

```javascript
npm install --save body-parser;
```

配置：

// 配置解析表單 POST 請求體插件（注意：一定要在 app.use(router) 之前 ）

```javascript
var express = require('express')
// 引包
var bodyParser = require('body-parser')

var app = express()

// 配置body-parser
// 只要加入這個配置，則在req請求對像上會多出來一個屬性：body
// 也就是說可以直接通過req.body來獲取表單post請求數據
// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }))

// parse application/json
app.use(bodyParser.json())
```

使用：

```javascript
app.use(function (req, res) {
  res.setHeader('Content-Type', 'text/plain')
  res.write('you posted:\n')
  // 可以通過req.body來獲取表單請求數據
  res.end(JSON.stringify(req.body, null, 2))
})
```

### `ejs`

##### 在Express中配置使用`art-templete`模板引擎

- [EJS官方文檔](https://github.com/mde/ejs/wiki/Using-EJS-with-Express)
- [EJS官方例子](https://github.com/expressjs/express/tree/master/examples/ejs)

```javascript

```


### 在Express中配置使用`express-session`插件操作

> 參考文檔：https://github.com/expressjs/session

安裝：

```javascript
npm install express-session
```

配置：

```javascript
//該插件會為req請求對象添加一個成員:req.session默認是一個對象
//這是最簡單的配置方式
//Session是基於Cookie實現的
app.use(session({
  //配置加密字符串，他會在原有的基礎上和字符串拼接起來去加密
  //目的是為了增加安全性，防止客戶端惡意偽造
  secret: 'keyboard cat',
  resave: false,
  saveUninitialized: true,//無論是否適用Session，都默認直接分配一把鑰匙
  cookie: { secure: true }
}))
```

使用：

```javascript
// 讀
//添加Session數據
//session就是一個對象
req.session.foo = 'bar';

//寫
//獲取session數據
req.session.foo

//刪
req.session.foo = null;
delete req.session.foo
```

提示：

默認Session數據時內存儲數據，服務器一旦重啟，真正的生產環境會把Session進行持久化存儲。

### 利用Express實現ADUS項目

#### 模塊化思想

模塊如何劃分:

- 模塊職責要單一

javascript模塊化：

- Node 中的 CommonJS
- 瀏覽器中的：
  - AMD	require.js
  - CMD     sea.js
- es6中增加了官方支持

#### 起步

- 初始化
- 模板處理

#### 路由設計

|      請求方法|   請求路徑   |  get參數    |   post參數   |    備註  |
| ---- | :--- | :--- | ---- | :--- |
|    GET  |   /students   |      |      |   渲染首頁   |
| GET | /students/new | | |渲染添加學生頁面  |
| POST|/students/new||name,age,gender,hobbies|處理添加學生請求|
|GET|/students/edit|id||渲染編輯頁面|
|POST|/students/edit||id,name,age,gender,hobbies|處理編輯請求|
|GET|/students/delete|id||處理刪除請求|

#### 提取路由模塊

router.js:

```javascript
/**
 * router.js路由模塊
 * 職責：
 *      處理路由
 *      根據不同的請求方法+請求路徑設置具體的請求函數
 * 模塊職責要單一，我們劃分模塊的目的就是增強代碼的可維護性，提升開發效率
 */
var fs = require('fs');

// Express專門提供了一種更好的方式
// 專門用來提供路由的
var express = require('express');
// 1 創建一個路由容器
var router = express.Router();
// 2 把路由都掛載到路由容器中

router.get('/students', function(req, res) {
    // res.send('hello world');
    // readFile的第二個參數是可選的，傳入utf8就是告訴他把讀取到的文件直接按照utf8編碼，直接轉成我們認識的字符
    // 除了這樣來轉換，也可以通過data.toString（）來轉換
    fs.readFile('./db.json', 'utf8', function(err, data) {
        if (err) {
            return res.status(500).send('Server error.')
        }
        // 讀取到的文件數據是string類型的數據
        // console.log(data);
        // 從文件中讀取到的數據一定是字符串，所以一定要手動轉換成對象
        var students = JSON.parse(data).students;
        res.render('index.html', {
            // 讀取文件數據
            students:students
        })
    })
});

router.get('/students/new',function(req,res){
    res.render('new.html')
});

router.get('/students/edit',function(req,res){
    
});

router.post('/students/edit',function(req,res){
    
});

router.get('/students/delete',function(req,res){
    
});

// 3 把router導出
module.exports = router;

```

app.js:

```javascript
var router = require('./router');

// router(app);
// 把路由容器掛載到app服務中
// 掛載路由
app.use(router);
```


#### 設計操作數據的API文件模塊

es6中的find和findIndex：

find接受一個方法作為參數，方法內部返回一個條件

find會便利所有的元素，執行你給定的帶有條件返回值的函數

符合該條件的元素會作為find方法的返回值

如果遍歷結束還沒有符合該條件的元素，則返回undefined![image-20200313103810731](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200313103810731.png)

```javascript
/**
 * student.js
 * 數據操作文件模塊
 * 職責：操作文件中的數據，只處理數據，不關心業務
 */
var fs = require('fs');
 /**
  * 獲取所有學生列表
  * return []
  */
exports.find = function(){
    
}


 /**
  * 獲取添加保存學生
  */
exports.save = function(){
        
}

/**
 * 更新學生
 */
exports.update = function(){
        
}

 /**
 * 刪除學生
 */
exports.delete = function(){
        
}
```

#### 步驟

- 處理模板
- 配置靜態開放資源
- 配置模板引擎
- 簡單的路由，/studens渲染靜態頁出來
- 路由設計
- 提取路由模塊
- 由於接下來的一系列業務操作都需要處理文件數據，所以我們需要封裝Student.js'
- 先寫好student.js文件結構
  - 查詢所有學生列別哦的API
  - findById
  - save
  - updateById
  - deleteById
- 實現具體功能
  - 通過路由收到請求
  - 接受請求中的參數（get，post）
    - req.query
    - req.body
  - 調用數據操作API處理數據
  - 根據操作結果給客戶端發送請求

- 業務功能順序
  - 列表
  - 添加
  - 編輯
  - 刪除

#### 子模板和模板的繼承（模板引擎高級語法）【include，extend，block】

注意:

模板頁：

```javascript
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>模板頁</title>
	<link rel="stylesheet" href="/node_modules/bootstrap/dist/css/bootstrap.css"/>
	{{ block 'head' }}{{ /block }}
</head>
<body>
	<!-- 通過include導入公共部分 -->
	{{include './header.html'}}
	
	<!-- 留一個位置 讓別的內容去填充 -->
	{{ block  'content' }}
		<h1>默認內容</h1>
	{{ /block }}
	
	<!-- 通過include導入公共部分 -->
	{{include './footer.html'}}
	
	<!-- 公共樣式 -->
	<script src="/node_modules/jquery/dist/jquery.js" ></script>
	<script src="/node_modules/bootstrap/dist/js/bootstrap.js" ></script>
	{{ block 'script' }}{{ /block }}
</body>
</html>
```

模板的繼承：

​	header頁面：

```javascript
<div id="">
	<h1>公共的頭部</h1>
</div>
```

​	footer頁面：

```javascript
<div id="">
	<h1>公共的底部</h1>
</div>
```

模板頁的使用：

```javascript
<!-- 繼承(extend:延伸，擴展)模板也layout.html -->
<!-- 把layout.html頁面的內容都拿進來作為index.html頁面的內容 -->
{{extend './layout.html'}}

<!-- 向模板頁面填充新的數據 -->
<!-- 填充後就會替換掉layout頁面content中的數據 -->
<!-- style樣式方面的內容 -->
{{ block 'head' }}
	<style type="text/css">
		body{
			background-color: skyblue;
		}
	</style>
{{ /block }}
{{ block 'content' }}
	<div id="">
		<h1>Index頁面的內容</h1>
	</div>
{{ /block }}
<!-- js部分的內容 -->
{{ block 'script' }}
	<script type="text/javascript">
		
	</script>
{{ /block }}
```

最終的顯示效果：

![image-20200316134759517](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200316134759517.png)


# MongoDB

## 關係型和非關係型數據庫

### 關係型數據庫（表就是關係，或者說表與表之間存在關係）。

- 所有的關係型數據庫都需要通過`sql`語言來操作
- 所有的關係型數據庫在操作之前都需要設計表結構
- 而且數據表還支持約束
  - 唯一的
  - 主鍵
  - 默認值
  - 非空

### 非關係型數據庫

- 非關係型數據庫非常的靈活
- 有的關係型數據庫就是key-value對兒
- 但MongDB是長得最像關係型數據庫的非關係型數據庫
  - 數據庫 -》 數據庫
  - 數據表 -》 集合（數組）
  - 表記錄 -》文檔對象

一個數據庫中可以有多個數據庫，一個數據庫中可以有多個集合（數組），一個集合中可以有多個文檔（表記錄）

```javascript
{
    qq:{
       user:[
           {},{},{}...
       ]
    }
}
```



- 也就是說你可以任意的往裡面存數據，沒有結構性這麼一說

## 安裝

- 下載

  - 下載地址：https://www.mongodb.com/download-center/community

- 安裝

  ```javascript
  npm i mongoose
  ```

- 配置環境變量

- 最後輸入`mongod --version`測試是否安裝成功

## 啟動和關閉數據庫

啟動：

```shell
# mongodb 默認使用執行mongod 命令所處盼復根目錄下的/data/db作為自己的數據存儲目錄
# 所以在第一次執行該命令之前先自己手動新建一個 /data/db
mongod
```

如果想要修改默認的數據存儲目錄，可以：

```javascript
mongod --dbpath = 數據存儲目錄路徑
```

停止：

```javascript
在開啟服務的控制台，直接Ctrl+C;
或者直接關閉開啟服務的控制台。
```

![image-20200314101047100](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200314101047100.png)

## 連接數據庫

連接：

```javascript
# 該命令默認連接本機的 MongoDB 服務
mongo
```

退出：

```javascript
# 在連接狀態輸入 exit 退出連接
exit
```



![image-20200314100821112](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200314100821112.png)

## 基本命令

- `show dbs`
  - 查看數據庫列表(數據庫中的所有數據庫)
- `db`
  - 查看當前連接的數據庫
- `use 數據庫名稱`
  - 切換到指定的數據庫，（如果沒有會新建）
- `show collections`
  - 查看當前目錄下的所有數據表
- `db.表名.find()`
  - 查看表中的詳細信息

## 在Node中如何操作MongoDB數據庫

### 使用官方的`MongoDB`包來操作

> ​	http://mongodb.github.io/node-mongodb-native/
>

### 使用第三方包`mongoose`來操作MongoDB數據庫

​	第三方包：`mongoose`基於MongoDB官方的`mongodb`包再一次做了封裝，名字叫`mongoose`，是WordPress項目團隊開發的。

 

> ​	https://mongoosejs.com/
>

![image-20200314105632745](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200314105632745.png)

![image-20200314105717993](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200314105717993.png)

## 學習指南（步驟）

官方學習文檔：https://mongoosejs.com/docs/index.html

### 設計Scheme 發布Model (創建表)

```javascript
// 1.引包
// 注意：按照後才能require使用
var mongoose = require('mongoose');

// 拿到schema圖表
var Schema = mongoose.Schema;

// 2.連接數據庫
// 指定連接數據庫後不需要存在，當你插入第一條數據庫後會自動創建數據庫
mongoose.connect('mongodb://localhost/test');

// 3.設計集合結構（表結構）
// 用戶表
var userSchema = new Schema({
	username: { //姓名
		type: String,
		require: true //添加約束，保證數據的完整性，讓數據按規矩統一
	},
	password: {
		type: String,
		require: true
	},
	email: {
		type: String
	}
});

// 4.將文檔結構發佈為模型
// mongoose.model方法就是用來將一個架構發佈為 model
// 		第一個參數：傳入一個大寫名詞單數字符串用來表示你的數據庫的名稱
// 					mongoose 會自動將大寫名詞的字符串生成 小寫複數 的集合名稱
// 					例如 這裡會變成users集合名稱
// 		第二個參數：架構
// 	返回值：模型構造函數
var User = mongoose.model('User', userSchema);
```

### 添加數據（增）

```javascript
// 5.通過模型構造函數對User中的數據進行操作
var user = new User({
	username: 'admin',
	password: '123456',
	email: 'xiaochen@qq.com'
});

user.save(function(err, ret) {
	if (err) {
		console.log('保存失敗');
	} else {
		console.log('保存成功');
		console.log(ret);
	}
});
```

### 刪除（刪）

根據條件刪除所有：

```javascript
User.remove({
	username: 'xiaoxiao'
}, function(err, ret) {
	if (err) {
		console.log('刪除失敗');
	} else {
		console.log('刪除成功');
		console.log(ret);
	}
});
```

根據條件刪除一個：

```javascript
Model.findOneAndRemove(conditions,[options],[callback]);
```

根據id刪除一個：

```javascript
User.findByIdAndRemove(id,[options],[callback]);
```

### 更新（改）

更新所有：

```javascript
User.remove(conditions,doc,[options],[callback]);
```

根據指定條件更新一個：

```javascript
User.FindOneAndUpdate([conditions],[update],[options],[callback]);
```

根據id更新一個：

```javascript
// 更新	根據id來修改表數據
User.findByIdAndUpdate('5e6c5264fada77438c45dfcd', {
	username: 'junjun'
}, function(err, ret) {
	if (err) {
		console.log('更新失敗');
	} else {
		console.log('更新成功');
	}
});
```



### 查詢（查）

查詢所有：

```javascript
// 查詢所有
User.find(function(err,ret){
	if(err){
		console.log('查詢失敗');
	}else{
		console.log(ret);
	}
});
```

條件查詢所有：

```javascript
// 根據條件查詢
User.find({ username:'xiaoxiao' },function(err,ret){
	if(err){
		console.log('查詢失敗');
	}else{
		console.log(ret);
	}
});
```

條件查詢單個：

```javascript
// 按照條件查詢單個，查詢出來的數據是一個對象（{}）
// 沒有條件查詢使用findOne方法，查詢的是表中的第一條數據
User.findOne({
	username: 'xiaoxiao'
}, function(err, ret) {
	if (err) {
		console.log('查詢失敗');
	} else {
		console.log(ret);
	}
});
```

# 使用Node操作MySQL數據庫

文檔：https://www.npmjs.com/package/mysql

安裝：

```shell
npm install --save  mysql
```

```javascript
// 引入mysql包
var mysql      = require('mysql');

// 創建連接
var connection = mysql.createConnection({
  host     : 'localhost',	//本機
  user     : 'me',		//賬號root
  password : 'secret',	//密碼12345
  database : 'my_db'	//數據庫名
});
 
// 連接數據庫	（打開冰箱門）
connection.connect();
 
//執行數據操作	（把大象放到冰箱）
connection.query('SELECT * FROM `users` ', function (error, results, fields) {
  if (error) throw error;//拋出異常阻止代碼往下執行
  // 沒有異常打印輸出結果
  console.log('The solution is: ',results);
});

//關閉連接	（關閉冰箱門）
connection.end();
```


# 異步編程

## 回調函數

不成立的情況下：

```javascript
function add(x,y){
    console.log(1);
    setTimeout(function(){
        console.log(2);
        var ret = x + y;
        return ret;
    },1000);
    console.log(3);
    //到這裡執行就結束了，不會i等到前面的定時器，所以直接返回了默認值 undefined
}

console.log(add(2,2));
// 結果是 1 3 undefined 4
```

![image-20200313085008929](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200313085008929.png)

使用回調函數解決：

回調函數：通過一個函數，獲取函數內部的操作。 （根據輸入得到輸出結果）

```javascript
var ret;
function add(x,y,callback){
    // callback就是回調函數
    // var x = 10;
    // var y = 20;
    // var callback = function(ret){console.log(ret);}
    console.log(1);
    setTimeout(function(){
        var ret = x + y;
        callback(ret);
    },1000);
    console.log(3);
}
add(10,20,function(ret){
    console.log(ret);
});
```

<img src="C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200313084746701.png" alt="image-20200313084746701" style="zoom:100%;" />

注意：

​	凡是需要得到一個函數內部異步操作的結果（setTimeout,readFile,writeFile,ajax,readdir）

​	這種情況必須通過   回調函數 (異步API都會伴隨著一個回調函數)

ajax:

基於原生XMLHttpRequest封裝get方法：

```javascript
var oReq = new XMLHttpRequest();
// 當請求加載成功要調用指定的函數
oReq.onload = function(){
    console.log(oReq.responseText);
}
oReq.open("GET", "請求路徑",true);
oReq.send();
```

```javascript
function get(url,callback){
    var oReq = new XMLHttpRequest();
    // 當請求加載成功要調用指定的函數
    oReq.onload = function(){
        //console.log(oReq.responseText);
        callback(oReq.responseText);
    }
    oReq.open("GET", url,true);
    oReq.send();
}
get('data.json',function(data){
    console.log(data);
});
```

## Promise

callback  hell（回調地獄）:

![image-20200314143410972](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200314143410972.png)

文件的讀取無法判斷執行順序（文件的執行順序是依據文件的大小來決定的）(異步api無法保證文件的執行順序)

```javascript
var fs = require('fs');

fs.readFile('./data/a.text','utf8',function(err,data){
	if(err){
		// 1 讀取失敗直接打印輸出讀取失敗
		return console.log('讀取失敗');
		// 2 拋出異常
		// 		阻止程序的執行
		// 		把錯誤信息打印到控制台
		throw err;
	}
	console.log(data);
});

fs.readFile('./data/b.text','utf8',function(err,data){
	if(err){
		// 1 讀取失敗直接打印輸出讀取失敗
		return console.log('讀取失敗');
		// 2 拋出異常
		// 		阻止程序的執行
		// 		把錯誤信息打印到控制台
		throw err;
	}
	console.log(data);
});
```

通過回調嵌套的方式來保證順序：

```javascript
var fs = require('fs');

fs.readFile('./data/a.text','utf8',function(err,data){
	if(err){
		// 1 讀取失敗直接打印輸出讀取失敗
		return console.log('讀取失敗');
		// 2 拋出異常
		// 		阻止程序的執行
		// 		把錯誤信息打印到控制台
		throw err;
	}
	console.log(data);
	fs.readFile('./data/b.text','utf8',function(err,data){
		if(err){
			// 1 讀取失敗直接打印輸出讀取失敗
			return console.log('讀取失敗');
			// 2 拋出異常
			// 		阻止程序的執行
			// 		把錯誤信息打印到控制台
			throw err;
		}
		console.log(data);
		fs.readFile('./data/a.text','utf8',function(err,data){
			if(err){
				// 1 讀取失敗直接打印輸出讀取失敗
				return console.log('讀取失敗');
				// 2 拋出異常
				// 		阻止程序的執行
				// 		把錯誤信息打印到控制台
				throw err;
			}
			console.log(data);
		});
	});
});
```

![image-20200314144807008](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200314144807008.png)為了解決以上編碼方式帶來的問題（回調地獄嵌套），所以在EcmaScript6新增了一個API:`Promise`。 ![image-20200314150050839](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200314150050839.png)

- Promise：承諾，保證
- Promise本身不是異步的，但往往都是內部封裝一個異步任務



基本語法：

```javascript
// 在EcmaScript 6中新增了一個API Promise
// Promise 是一個構造函數

var fs = require('fs');
// 1 創建Promise容器		resolve:解決   reject：失敗
var p1 = new Promise(function(resolve, reject) {
	fs.readFile('./a.text', 'utf8', function(err, data) {
		if (err) {
			// console.log(err);
			// 把容器的Pending狀態變為rejected
			reject(err);
		} else {
			// console.log(data);
			// 把容器的Pending狀態變為resolve
			resolve(1234);
		}
	});
});

// 當p1成功了，然後就（then）做指定的操作
// then方法接收的function就是容器中的resolve函數
p1
	.then(function(data) {
		console.log(data);
	}, function(err) {
		console.log('讀取文件失敗了', err);
	});

```

![image-20200315100611620](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200315100611620.png)

鍊式循環：![image-20200315125559136](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200315125559136.png)

封裝Promise的`readFile`：

```javascript
var fs = require('fs');

function pReadFile(filePath) {
	return new Promise(function(resolve, reject) {
		fs.readFile(filePath, 'utf8', function(err, data) {
			if (err) {
				reject(err);
			} else {
				resolve(data);
			}
		});
	});
}

pReadFile('./a.txt')
	.then(function(data) {
		console.log(data);
		return pReadFile('./b.txt');
	})
	.then(function(data) {
		console.log(data);
		return pReadFile('./a.txt');
	})
	.then(function(data) {
		console.log(data);
	})

```

mongoose所有的API都支持Promise：

```javascript
// 查詢所有
User.find()
	.then(function(data){
        console.log(data)
    })
```

註冊：

```javascript
User.findOne({username:'admin'},function(user){
    if(user){
        console.log('用戶已存在')
    } else {
        new User({
             username:'aaa',
             password:'123',
             email:'fffff'
        }).save(function(){
            console.log('註冊成功');
        })
    }
})
```



```javascript
User.findOne({
    username:'admin'
})
    .then(function(user){
        if(user){
            // 用戶已經存在不能註冊
            console.log('用戶已存在');
        }
        else{
            // 用戶不存在可以註冊
            return new User({
                username:'aaa',
                password:'123',
                email:'fffff'
            }).save();
        }
    })
    .then(funciton(ret){
		console.log('註冊成功');
    })
```


## Generator

async函數



# 其他

## 修改完代碼自動重啟

我們在這裡可以使用一個第三方命名行工具：`nodemon`來幫助我們解決頻繁修改代碼重啟服務器的問題。

`nodemon`是一個基於Node.js開發的一個第三方命令行工具，我們使用的時候需要獨立安裝：

```javascript
#在任意目錄執行該命令都可以
#也就是說，所有需要 --global安裝的包都可以在任意目錄執行
npm install --global nodemon
npm install -g nodemon

#如果安裝不成功的話，可以使用cnpm安裝
cnpm install -g nodemon
```

安裝完畢之後使用：

```javascript
node app.js

#使用nodemon
nodemon app.js
```

只要是通過`nodemon`啟動的服務，則他會監視你的文件變化，當文件發生變化的時候，會自動幫你重啟服務器。

## 封裝異步API

回調函數：獲取異步操作的結果

```javascript
function fn(callback){
    // var callback = funtion(data){ console.log(data); }
	setTimeout(function(){
        var data = 'hello';
        callback(data);
    },1000);
}
// 如果需要獲取一個函數中異步操作的結果，則必須通過回調函數的方式來獲取
fn(function(data){
    console.log(data);
})
```

## 數組的遍歷方法，都是對函數作為一種參數

![image-20200314094620191](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200314094620191.png)

## EcmaScript 6

> 參考文檔：https://es6.ruanyifeng.com/

# 項目案例

## 目錄結構

```javascript
.
app.js	項目的入口文件
controllers
models	存儲使用mongoose設計的數據模型
node_modules	第三方包
package.json	包描述文件
package-lock.json	第三方包版本鎖定文件（npm5之後才有）
public	公共靜態資源
routes
views	存儲視圖目錄
```

## 模板頁

- 子模板
- 模板繼承

## 路由設計

| 路由            | 方法 | get參數 | post參數                | 是否需要登錄 | 備註         |
| --------------- | ---- | ------- | ----------------------- | ------------ | ------------ |
| /               | get  |         |                         |              | 渲染首頁     |
| /register(登錄) | get  |         |                         |              | 渲染註冊頁面 |
| /register       | post |         | email,nickname,password |              | 處理註冊請求 |
| /login          | get  |         |                         |              | 渲染登陸界面 |
| /login          | post |         | email,password          |              | 處理登錄請求 |
| /loginout       | get  |         |                         |              | 處理退出請求 |
|                 |      |         |                         |              |              |

## 模型設計

## 功能實現

## 步驟

- 創建目錄結構
- 整合靜態也-模板頁
  - include
  - block
  - extend
- 設計用戶登陸，退出，註冊的路由
- 用戶註冊
  - 先處理客戶端頁面的內容（表單控件的name，收集表單數據，發起請求）
  - 服務端
    - 獲取從客戶端收到的數據
    - 操作數據庫
      - 如果有錯，發送500告訴客戶端服務器錯了‘
      - 其他的根據業務發送不同的響應數據
- 登錄
- 退出


# Express中間件

## 中間件的概念

> 參考文檔：http://expressjs.com/en/guide/using-middleware.html

![image-20200316202757617](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200316202757617.png)

中間件：把很複雜的事情分割成單個，然後依次有條理的執行。就是一個中間處理環節，有輸入，有輸出。

說的通俗易懂點兒，中間件就是一個（從請求到響應調用的方法）方法。

把數據從請求到響應分步驟來處理，每一個步驟都是一個中間處理環節。

```javascript
var http = require('http');
var url = require('url');

var cookie = require('./expressPtoject/cookie');
var query = require('./expressPtoject/query');
var postBody = require('./expressPtoject/post-body');

var server = http.createServer(function(){
	// 解析請求地址中的get參數
	// var obj = url.parse(req.url,true);
	// req.query = obj.query;
	query(req,res);	//中間件
	
	// 解析請求地址中的post參數
	req.body = {
		foo:'bar'
	}
});

if(req.url === 'xxx'){
	// 處理請求
	...
}

server.listen(3000,function(){
	console.log('3000 runing...');
});
```

同一個請求對象所經過的中間件都是同一個請求對象和響應對象。

```javascript
var express = require('express');
var app = express();
app.get('/abc',function(req,res,next){
	// 同一個請求的req和res是一樣的，
	// 可以前面存儲下面調用
	console.log('/abc');
	// req.foo = 'bar';
	req.body = {
		name:'xiaoxiao',
		age:18
	}
	next();
});
app.get('/abc',function(req,res,next){
	// console.log(req.foo);
	console.log(req.body);
	console.log('/abc');
});
app.listen(3000, function() {
	console.log('app is running at port 3000.');
});

```

![image-20200317110520098](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20200317110520098.png)

## 中間件的分類:

### 應用程序級別的中間件

萬能匹配（不關心任何請求路徑和請求方法的中間件）：

```javascript
app.use(function(req,res,next){
    console.log('Time',Date.now());
    next();
});
```

關心請求路徑和請求方法的中間件：

```javascript
app.use('/a',function(req,res,next){
    console.log('Time',Date.now());
    next();
});
```

### 路由級別的中間件

嚴格匹配請求路徑和請求方法的中間件

get:

```javascript
app.get('/',function(req,res){
	res.send('get');
});
```

post：

```javascript
app.post('/a',function(req,res){
	res.send('post');
});
```

put:

```javascript
app.put('/user',function(req,res){
	res.send('put');
});
```

delete:

```javascript
app.delete('/delete',function(req,res){
	res.send('delete');
});
```

### 總

```javascript
var express = require('express');
var app = express();

// 中間件：處理請求，本質就是個函數
// 在express中，對中間件有幾種分類

// 1 不關心任何請求路徑和請求方法的中間件
// 也就是說任何請求都會進入這個中間件
// 中間件本身是一個方法，該方法接收三個參數
// Request 請求對象
// Response 響應對象
// next 下一個中間件
// // 全局匹配中間件
// app.use(function(req, res, next) {
// 	console.log('1');
// 	// 當一個請求進入中間件後
// 	// 如果需要請求另外一個方法則需要使用next（）方法
// 	next();
// 	// next是一個方法，用來調用下一個中間件
//  // 注意：next（）方法調用下一個方法的時候，也會匹配（不是調用緊挨著的哪一個）
// });
// app.use(function(req, res, next) {
// 	console.log('2');
// });

// // 2 關心請求路徑的中間件
// // 以/xxx開頭的中間件
// app.use('/a',function(req, res, next) {
// 	console.log(req.url);
// });

// 3 嚴格匹配請求方法和請求路徑的中間件
app.get('/',function(){
	console.log('/');
});
app.post('/a',function(){
	console.log('/a');
});

app.listen(3000, function() {
	console.log('app is running at port 3000.');
});

```

## 錯誤處理中間件

```javascript
app.use(function(err,req,res,next){
    console.error(err,stack);
    res.status(500).send('Something broke');
});
```

配置使用404中間件：

```javascript
app.use(function(req,res){
    res.render('404.html');
});
```

配置全局錯誤處理中間件:

```javascript
app.get('/a', function(req, res, next) {
	fs.readFile('.a/bc', funtion() {
		if (err) {
        	// 當調用next()傳參後，則直接進入到全局錯誤處理中間件方法中
        	// 當發生全局錯誤的時候，我們可以調用next傳遞錯誤對象
        	// 然後被全局錯誤處理中間件匹配到並進行處理
			next(err);
		}
	})
});
//全局錯誤處理中間件
app.use(function(err,req,res,next){
    res.status(500).json({
        err_code:500,
        message:err.message
    });
});
```


## 內置中間件

- express.static(提供靜態文件)
  - http://expressjs.com/en/starter/static-files.html#serving-static-files-in-express

## 第三方中間件

> 參考文檔：http://expressjs.com/en/resources/middleware.html

- body-parser
- compression
- cookie-parser
- mogran
- response-time
- server-static
- session