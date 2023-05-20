# 筆記

## 腳手架文件結構

	├── node_modules 
	├── public
	│   ├── favicon.ico: 頁簽圖標
	│   └── index.html: 主頁面
	├── src
	│   ├── assets: 存放靜態資源
	│   │   └── logo.png
	│   │── component: 存放組件
	│   │   └── HelloWorld.vue
	│   │── App.vue: 匯總所有組件
	│   │── main.js: 入口文件
	├── .gitignore: git版本管制忽略的配置
	├── babel.config.js: babel的配置文件
	├── package.json: 應用包配置文件 
	├── README.md: 應用描述文件
	├── package-lock.json：包版本控製文件

## 關於不同版本的Vue

1. vue.js與vue.runtime.xxx.js的區別：
    1. vue.js是完整版的Vue，包含：核心功能 + 模板解析器。
    2. vue.runtime.xxx.js是運行版的Vue，只包含：核心功能；沒有模板解析器。
2. 因為vue.runtime.xxx.js沒有模板解析器，所以不能使用template這個配置項，需要使用render函數接收到的createElement函數去指定具體內容。

## vue.config.js配置文件

1. 使用vue inspect > output.js可以查看到Vue腳手架的默認配置。
2. 使用vue.config.js可以對腳手架進行個性化定制，詳情見：https://cli.vuejs.org/zh

## ref屬性

1. 被用來給元素或子組件註冊引用信息（id的替代者）
2. 應用在html標籤上獲取的是真實DOM元素，應用在組件標籤上是組件實例對象（vc）
3. 使用方式：
    1. 打標識：```<h1 ref="xxx">.....</h1>``` 或 ```<School ref="xxx"></School>```
    2. 獲取：```this.$refs.xxx```

## props配置項

1. 功能：讓組件接收外部傳過來的數據

2. 傳遞數據：```<Demo name="xxx"/>```

3. 接收數據：

    1. 第一種方式（只接收）：```props:['name'] ```

    2. 第二種方式（限制類型）：```props:{name:String}```

    3. 第三種方式（限制類型、限制必要性、指定默認值）：

        ```js
        props:{
        	name:{
        	type:String, //類型
        	required:true, //必要性
        	default:'老王' //默認值
        	}
        }
        ```

    > 備註：props是只讀的，Vue底層會監測你對props的修改，如果進行了修改，就會發出警告，若業務需求確實需要修改，那麼請複制props的內容到data中一份，然後去修改data中的數據。

## mixin(混入)

1. 功能：可以把多個組件共用的配置提取成一個混入對象

2. 使用方式：

    第一步定義混合：

    ```
    {
        data(){....},
        methods:{....}
        ....
    }
    ```

    第二步使用混入：

    ​	全局混入：```Vue.mixin(xxx)```
    ​	局部混入：```mixins:['xxx']	```

## 插件

1. 功能：用於增強Vue

2. 本質：包含install方法的一個對象，install的第一個參數是Vue，第二個以後的參數是插件使用者傳遞的數據。

3. 定義插件：

    ```js
    對象.install = function (Vue, options) {
        // 1. 添加全局過濾器
        Vue.filter(....)
    
        // 2. 添加全局指令
        Vue.directive(....)
    
        // 3. 配置全局混入(合)
        Vue.mixin(....)
    
        // 4. 添加實例方法
        Vue.prototype.$myMethod = function () {...}
        Vue.prototype.$myProperty = xxxx
    }
    ```

4. 使用插件：```Vue.use()```

## scoped樣式

1. 作用：讓樣式在局部生效，防止衝突。
2. 寫法：```<style scoped>```

## 總結TodoList案例

1. 組件化編碼流程：

    ​	(1).拆分靜態組件：組件要按照功能點拆分，命名不要與html元素衝突。

    ​	(2).實現動態組件：考慮好數據的存放位置，數據是一個組件在用，還是一些組件在用：

    ​			1).一個組件在用：放在組件自身即可。

    ​			2). 一些組件在用：放在他們共同的父組件上（<span style="color:red">狀態提升</span>）。

    ​	(3).實現交互：從綁定事件開始。

2. props適用於：

    ​	(1).父組件 ==> 子組件 通信

    ​	(2).子組件 ==> 父組件 通信（要求父先給子一個函數）

3. 使用v-model時要切記：v-model綁定的值不能是props傳過來的值，因為props是不可以修改的！

4. props傳過來的若是對像類型的值，修改對像中的屬性時Vue不會報錯，但不推薦這樣做。

## webStorage

1. 存儲內容大小一般支持5MB左右（不同瀏覽器可能還不一樣）

2. 瀏覽器端通過 Window.sessionStorage 和 Window.localStorage 屬性來實現本地存儲機制。

3. 相關API：

    1. ```xxxxxStorage.setItem('key', 'value');```
        				該方法接受一個鍵和值作為參數，會把鍵值對添加到存儲中，如果鍵名存在，則更新其對應的值。

    2. ```xxxxxStorage.getItem('person');```

        ​		該方法接受一個鍵名作為參數，返回鍵名對應的值。

    3. ```xxxxxStorage.removeItem('key');```

        ​		該方法接受一個鍵名作為參數，並把該鍵名從存儲中刪除。

    4. ``` xxxxxStorage.clear()```

        ​		該方法會清空存儲中的所有數據。

4. 備註：

    1. SessionStorage存儲的內容會隨著瀏覽器窗口關閉而消失。
    2. LocalStorage存儲的內容，需要手動清除才會消失。
    3. ```xxxxxStorage.getItem(xxx)```如果xxx對應的value獲取不到，那麼getItem的返回值是null。
    4. ```JSON.parse(null)```的結果依然是null。

## 組件的自定義事件

1. 一種組件間通信的方式，適用於：<strong style="color:red">子組件 ===> 父組件</strong>

2. 使用場景：A是父組件，B是子組件，B想給A傳數據，那麼就要在A中給B綁定自定義事件（<span style="color:red">事件的回調在A中</span>）。

3. 綁定自定義事件：

    1. 第一種方式，在父組件中：```<Demo @atguigu="test"/>```  或 ```<Demo v-on:atguigu="test"/>```

    2. 第二種方式，在父組件中：

        ```js
        <Demo ref="demo"/>
        ......
        mounted(){
           this.$refs.xxx.$on('atguigu',this.test)
        }
        ```

    3. 若想讓自定義事件只能觸發一次，可以使用```once```修飾符，或```$once```方法。

4. 觸發自定義事件：```this.$emit('atguigu',數據)```		

5. 解綁自定義事件```this.$off('atguigu')```

6. 組件上也可以綁定原生DOM事件，需要使用```native```修飾符。

7. 注意：通過```this.$refs.xxx.$on('atguigu',回調)```綁定自定義事件時，回調<span style="color:red">要么配置在methods中</span>，<span style="color:red">要么用箭頭函數</span>，否則this指向會出問題！

## 全局事件總線（GlobalEventBus）

1. 一種組件間通信的方式，適用於<span style="color:red">任意組件間通信</span>。

2. 安裝全局事件總線：

   ```js
   new Vue({
   	......
   	beforeCreate() {
   		Vue.prototype.$bus = this //安裝全局事件總線，$bus就是當前應用的vm
   	},
       ......
   }) 
   ```

3. 使用事件總線：

   1. 接收數據：A組件想接收數據，則在A組件中給$bus綁定自定義事件，事件的<span style="color:red">回調留在A組件自身。 </span>

      ```js
      methods(){
        demo(data){......}
      }
      ......
      mounted() {
        this.$bus.$on('xxxx',this.demo)
      }
      ```

   2. 提供數據：```this.$bus.$emit('xxxx',數據)```

4. 最好在beforeDestroy鉤子中，用$off去解綁<span style="color:red">當前組件所用到的</span>事件。

## 消息訂閱與發布（pubsub）

1.   一種組件間通信的方式，適用於<span style="color:red">任意組件間通信</span>。

2. 使用步驟：

   1. 安裝pubsub：```npm i pubsub-js```

   2. 引入: ```import pubsub from 'pubsub-js'```

   3. 接收數據：A組件想接收數據，則在A組件中訂閱消息，訂閱的<span style="color:red">回調留在A組件自身。 </span>

      ```js
      methods(){
        demo(data){......}
      }
      ......
      mounted() {
        this.pid = pubsub.subscribe('xxx',this.demo) //訂閱消息
      }
      ```

   4. 提供數據：```pubsub.publish('xxx',數據)```

   5. 最好在beforeDestroy鉤子中，用```PubSub.unsubscribe(pid)```去<span style="color:red">取消訂閱。 </span>
	
## nextTick

1. 語法：```this.$nextTick(回調函數)```
2. 作用：在下一次 DOM 更新結束後執行其指定的回調。
3. 什麼時候用：當改變數據後，要基於更新後的新DOM進行某些操作時，要在nextTick所指定的回調函數中執行。

## Vue封裝的過度與動畫

1. 作用：在插入、更新或移除 DOM元素時，在合適的時候給元素添加樣式類名。

2. 圖示：<img src="https://img04.sogoucdn.com/app/a/100520146/5990c1dff7dc7a8fb3b34b4462bd0105" style="width:60%" />

3. 寫法：

   1. 準備好樣式：

      - 元素進入的樣式：
        1. v-enter：進入的起點
        2. v-enter-active：進入過程中
        3. v-enter-to：進入的終點
      - 元素離開的樣式：
        1. v-leave：離開的起點
        2. v-leave-active：離開過程中
        3. v-leave-to：離開的終點

   2. 使用```<transition>```包裹要過度的元素，並配置name屬性：

      ```vue
      <transition name="hello">
      	<h1 v-show="isShow">你好啊！ </h1>
      </transition>
      ```

   3. 備註：若有多個元素需要過度，則需要使用：```<transition-group>```，且每個元素都要指定```key```值。

## vue腳手架配置代理

### 方法一

​	在vue.config.js中添加如下配置：

```js
devServer:{
  proxy:"http://localhost:5000"
}
```

說明：

1. 優點：配置簡單，請求資源時直接發給前端（8080）即可。
2. 缺點：不能配置多個代理，不能靈活的控制請求是否走代理。
3. 工作方式：若按照上述配置代理，當請求了前端不存在的資源時，那麼該請求會轉發給服務器 （優先匹配前端資源）

### 方法二

​	編寫vue.config.js配置具體代理規則：

```js
module.exports = {
	devServer: {
      proxy: {
      '/api1': {// 匹配所有以 '/api1'開頭的請求路徑
        target: 'http://localhost:5000',// 代理目標的基礎路徑
        changeOrigin: true,
        pathRewrite: {'^/api1': ''}
      },
      '/api2': {// 匹配所有以 '/api2'開頭的請求路徑
        target: 'http://localhost:5001',// 代理目標的基礎路徑
        changeOrigin: true,
        pathRewrite: {'^/api2': ''}
      }
    }
  }
}
/*
   changeOrigin設置為true時，服務器收到的請求頭中的host為：localhost:5000
   changeOrigin設置為false時，服務器收到的請求頭中的host為：localhost:8080
   changeOrigin默認值為true
*/
```

說明：

1. 優點：可以配置多個代理，且可以靈活的控制請求是否走代理。
2. 缺點：配置略微繁瑣，請求資源時必須加前綴。

## 插槽

1. 作用：讓父組件可以向子組件指定位置插入html結構，也是一種組件間通信的方式，適用於 <strong style="color:red">父組件 ===> 子組件</strong> 。

2. 分類：默認插槽、具名插槽、作用域插槽

3. 使用方式：

   1. 默認插槽：

      ```vue
      父組件中：
              <Category>
                 <div>html結構1</div>
              </Category>
      子組件中：
              <template>
                  <div>
                     <!-- 定義插槽 -->
                     <slot>插槽默認內容...</slot>
                  </div>
              </template>
      ```

   2. 具名插槽：

      ```vue
      父組件中：
              <Category>
                  <template slot="center">
                    <div>html結構1</div>
                  </template>
      
                  <template v-slot:footer>
                     <div>html結構2</div>
                  </template>
              </Category>
      子組件中：
              <template>
                  <div>
                     <!-- 定義插槽 -->
                     <slot name="center">插槽默認內容...</slot>
                     <slot name="footer">插槽默認內容...</slot>
                  </div>
              </template>
      ```

   3. 作用域插槽：

      1. 理解：<span style="color:red">數據在組件的自身，但根據數據生成的結構需要組件的使用者來決定。 </span>（games數據在Category組件中，但使用數據所遍歷出來的結構由App組件決定）

      2. 具體編碼：

         ```vue
         父組件中：
         		<Category>
         			<template scope="scopeData">
         				<!-- 生成的是ul列表 -->
         				<ul>
         					<li v-for="g in scopeData.games" :key="g">{{g}}</li>
         				</ul>
         			</template>
         		</Category>
         
         		<Category>
         			<template slot-scope="scopeData">
         				<!-- 生成的是h4標題 -->
         				<h4 v-for="g in scopeData.games" :key="g">{{g}}</h4>
         			</template>
         		</Category>
         子組件中：
                 <template>
                     <div>
                         <slot :games="games"></slot>
                     </div>
                 </template>
         		
                 <script>
                     export default {
                         name:'Category',
                         props:['title'],
                         //數據在子組件自身
                         data() {
                             return {
                                 games:['紅色警戒','穿越火線','勁舞團','超級瑪麗']
                             }
                         },
                     }
                 </script>
         ```
   ```
   
   ```

## Vuex

### 1.概念

​		在Vue中實現集中式狀態（數據）管理的一個Vue插件，對vue應用中多個組件的共享狀態進行集中式的管理（讀/寫），也是一種組件間通信的方式，且適用於任意組件間通信。

### 2.何時使用？

​		多個組件需要共享數據時

### 3.搭建vuex環境

1. 創建文件：```src/store/index.js```

   ```js
   //引入Vue核心庫
   import Vue from 'vue'
   //引入Vuex
   import Vuex from 'vuex'
   //應用Vuex插件
   Vue.use(Vuex)
   
   //準備actions對象——響應組件中用戶的動作
   const actions = {}
   //準備mutations對象——修改state中的數據
   const mutations = {}
   //準備state對象——保存具體的數據
   const state = {}
   
   //創建並暴露store
   export default new Vuex.Store({
   	actions,
   	mutations,
   	state
   })
   ```

2. 在```main.js```中創建vm時傳入```store```配置項

   ```js
   ......
   //引入store
   import store from './store'
   ......
   
   //創建vm
   new Vue({
   	el:'#app',
   	render: h => h(App),
   	store
   })
   ```

###    4.基本使用

1. 初始化數據、配置```actions```、配置```mutations```，操作文件```store.js```

   ```js
   //引入Vue核心庫
   import Vue from 'vue'
   //引入Vuex
   import Vuex from 'vuex'
   //引用Vuex
   Vue.use(Vuex)
   
   const actions = {
       //響應組件中加的動作
   	jia(context,value){
   		// console.log('actions中的jia被調用了',miniStore,value)
   		context.commit('JIA',value)
   	},
   }
   
   const mutations = {
       //執行加
   	JIA(state,value){
   		// console.log('mutations中的JIA被調用了',state,value)
   		state.sum += value
   	}
   }
   
   //初始化數據
   const state = {
      sum:0
   }
   
   //創建並暴露store
   export default new Vuex.Store({
   	actions,
   	mutations,
   	state,
   })
   ```

2. 組件中讀取vuex中的數據：```$store.state.sum```

3. 組件中修改vuex中的數據：```$store.dispatch('action中的方法名',數據)``` 或 ```$store.commit('mutations中的方法名',數據)```

   >  備註：若沒有網絡請求或其他業務邏輯，組件中也可以越過actions，即不寫```dispatch```，直接編寫```commit```

### 5.getters的使用

1. 概念：當state中的數據需要經過加工後再使用時，可以使用getters加工。

2. 在```store.js```中追加```getters```配置

   ```js
   ......
   
   const getters = {
   	bigSum(state){
   		return state.sum * 10
   	}
   }
   
   //創建並暴露store
   export default new Vuex.Store({
   	......
   	getters
   })
   ```

3. 組件中讀取數據：```$store.getters.bigSum```

### 6.四個map方法的使用

1. <strong>mapState方法：</strong>用於幫助我們映射```state```中的數據為計算屬性

   ```js
   computed: {
       //借助mapState生成計算屬性：sum、school、subject（對象寫法）
        ...mapState({sum:'sum',school:'school',subject:'subject'}),
            
       //借助mapState生成計算屬性：sum、school、subject（數組寫法）
       ...mapState(['sum','school','subject']),
   },
   ```

2. <strong>mapGetters方法：</strong>用於幫助我們映射```getters```中的數據為計算屬性

   ```js
   computed: {
       //借助mapGetters生成計算屬性：bigSum（對象寫法）
       ...mapGetters({bigSum:'bigSum'}),
   
       //借助mapGetters生成計算屬性：bigSum（數組寫法）
       ...mapGetters(['bigSum'])
   },
   ```

3. <strong>mapActions方法：</strong>用於幫助我們生成與```actions```對話的方法，即：包含```$store.dispatch(xxx)```的函數

   ```js
   methods:{
       //靠mapActions生成：incrementOdd、incrementWait（對象形式）
       ...mapActions({incrementOdd:'jiaOdd',incrementWait:'jiaWait'})
   
       //靠mapActions生成：incrementOdd、incrementWait（數組形式）
       ...mapActions(['jiaOdd','jiaWait'])
   }
   ```

4. <strong>mapMutations方法：</strong>用於幫助我們生成與```mutations```對話的方法，即：包含```$store.commit(xxx)```的函數

   ```js
   methods:{
       //靠mapActions生成：increment、decrement（對象形式）
       ...mapMutations({increment:'JIA',decrement:'JIAN'}),
       
       //靠mapMutations生成：JIA、JIAN（對象形式）
       ...mapMutations(['JIA','JIAN']),
   }
   ```

> 備註：mapActions與mapMutations使用時，若需要傳遞參數需要：在模板中綁定事件時傳遞好參數，否則參數是事件對象。

### 7.模塊化+命名空間

1. 目的：讓代碼更好維護，讓多種數據分類更加明確。

2. 修改```store.js```

   ```javascript
   const countAbout = {
     namespaced:true,//開啟命名空間
     state:{x:1},
     mutations: { ... },
     actions: { ... },
     getters: {
       bigSum(state){
          return state.sum * 10
       }
     }
   }
   
   const personAbout = {
     namespaced:true,//開啟命名空間
     state:{ ... },
     mutations: { ... },
     actions: { ... }
   }
   
   const store = new Vuex.Store({
     modules: {
       countAbout,
       personAbout
     }
   })
   ```

3. 開啟命名空間後，組件中讀取state數據：

   ```js
   //方式一：自己直接讀取
   this.$store.state.personAbout.list
   //方式二：借助mapState讀取：
   ...mapState('countAbout',['sum','school','subject']),
   ```

4. 開啟命名空間後，組件中讀取getters數據：

   ```js
   //方式一：自己直接讀取
   this.$store.getters['personAbout/firstPersonName']
   //方式二：借助mapGetters讀取：
   ...mapGetters('countAbout',['bigSum'])
   ```

5. 開啟命名空間後，組件中調用dispatch

   ```js
   //方式一：自己直接dispatch
   this.$store.dispatch('personAbout/addPersonWang',person)
   //方式二：借助mapActions：
   ...mapActions('countAbout',{incrementOdd:'jiaOdd',incrementWait:'jiaWait'})
   ```

6. 開啟命名空間後，組件中調用commit

   ```js
   //方式一：自己直接commit
   this.$store.commit('personAbout/ADD_PERSON',person)
   //方式二：借助mapMutations：
   ...mapMutations('countAbout',{increment:'JIA',decrement:'JIAN'}),
   ```

 ## 路由

1. 理解： 一個路由（route）就是一組映射關係（key - value），多個路由需要路由器（router）進行管理。
2. 前端路由：key是路徑，value是組件。

### 1.基本使用

1. 安裝vue-router，命令：```npm i vue-router```

2. 應用插件：```Vue.use(VueRouter)```

3. 編寫router配置項:

   ```js
   //引入VueRouter
   import VueRouter from 'vue-router'
   //引入Luyou 組件
   import About from '../components/About'
   import Home from '../components/Home'
   
   //創建router實例對象，去管理一組一組的路由規則
   const router = new VueRouter({
   	routes:[
   		{
   			path:'/about',
   			component:About
   		},
   		{
   			path:'/home',
   			component:Home
   		}
   	]
   })
   
   //暴露router
   export default router
   ```

4. 實現切換（active-class可配置高亮樣式）

   ```vue
   <router-link active-class="active" to="/about">About</router-link>
   ```

5. 指定展示位置

   ```vue
   <router-view></router-view>
   ```

### 2.幾個注意點

1. 路由組件通常存放在```pages```文件夾，一般組件通常存放在```components```文件夾。
2. 通過切換，“隱藏”了的路由組件，默認是被銷毀掉的，需要的時候再去掛載。
3. 每個組件都有自己的```$route```屬性，裡面存儲著自己的路由信息。
4. 整個應用只有一個router，可以通過組件的```$router```屬性獲取到。

### 3.多級路由（多級路由）

1. 配置路由規則，使用children配置項：

   ```js
   routes:[
   	{
   		path:'/about',
   		component:About,
   	},
   	{
   		path:'/home',
   		component:Home,
   		children:[ //通過children配置子級路由
   			{
   				path:'news', //此處一定不要寫：/news
   				component:News
   			},
   			{
   				path:'message',//此處一定不要寫：/message
   				component:Message
   			}
   		]
   	}
   ]
   ```

2. 跳轉（要寫完整路徑）：

   ```vue
   <router-link to="/home/news">News</router-link>
   ```

### 4.路由的query參數

1. 傳遞參數

   ```vue
   <!-- 跳轉並攜帶query參數，to的字符串寫法 -->
   <router-link :to="/home/message/detail?id=666&title=你好">跳轉</router-link>
   				
   <!-- 跳轉並攜帶query參數，to的對象寫法 -->
   <router-link 
   	:to="{
   		path:'/home/message/detail',
   		query:{
   		   id:666,
               title:'你好'
   		}
   	}"
   >跳轉</router-link>
   ```

2. 接收參數：

   ```js
   $route.query.id
   $route.query.title
   ```

### 5.命名路由

1. 作用：可以簡化路由的跳轉。

2. 如何使用

   1. 給路由命名：

      ```js
      {
      	path:'/demo',
      	component:Demo,
      	children:[
      		{
      			path:'test',
      			component:Test,
      			children:[
      				{
                            name:'hello' //給路由命名
      					path:'welcome',
      					component:Hello,
      				}
      			]
      		}
      	]
      }
      ```

   2. 簡化跳轉：

      ```vue
      <!--簡化前，需要寫完整的路徑 -->
      <router-link to="/demo/test/welcome">跳轉</router-link>
      
      <!--簡化後，直接通過名字跳轉 -->
      <router-link :to="{name:'hello'}">跳轉</router-link>
      
      <!--簡化寫法配合傳遞參數 -->
      <router-link 
      	:to="{
      		name:'hello',
      		query:{
      		   id:666,
                  title:'你好'
      		}
      	}"
      >跳轉</router-link>
      ```

### 6.路由的params參數

1. 配置路由，聲明接收params參數

   ```js
   {
   	path:'/home',
   	component:Home,
   	children:[
   		{
   			path:'news',
   			component:News
   		},
   		{
   			component:Message,
   			children:[
   				{
   					name:'xiangqing',
   					path:'detail/:id/:title', //使用佔位符聲明接收params參數
   					component:Detail
   				}
   			]
   		}
   	]
   }
   ```

2. 傳遞參數

   ```vue
   <!-- 跳轉並攜帶params參數，to的字符串寫法 -->
   <router-link :to="/home/message/detail/666/你好">跳轉</router-link>
   				
   <!-- 跳轉並攜帶params參數，to的對象寫法 -->
   <router-link 
   	:to="{
   		name:'xiangqing',
   		params:{
   		   id:666,
               title:'你好'
   		}
   	}"
   >跳轉</router-link>
   ```

   > 特別注意：路由攜帶params參數時，若使用to的對象寫法，則不能使用path配置項，必須使用name配置！

3. 接收參數：

   ```js
   $route.params.id
   $route.params.title
   ```

### 7.路由的props配置

​	作用：讓路由組件更方便的收到參數

```js
{
	name:'xiangqing',
	path:'detail/:id',
	component:Detail,

	//第一種寫法：props值為對象，該對像中所有的key-value的組合最終都會通過props傳給Detail組件
	// props:{a:900}

	//第二種寫法：props值為布爾值，布爾值為true，則把路由收到的所有params參數通過props傳給Detail組件
	// props:true
	
	//第三種寫法：props值為函數，該函數返回的對像中每一組key-value都會通過props傳給Detail組件
	props(route){
		return {
			id:route.query.id,
			title:route.query.title
		}
	}
}
```

### 8.```<router-link>```的replace屬性

1. 作用：控制路由跳轉時操作瀏覽器歷史記錄的模式
2. 瀏覽器的歷史記錄有兩種寫入方式：分別為```push```和```replace```，```push```是追加歷史記錄，```replace```是替換當前記錄。路由跳轉時候默認為```push```
3. 如何開啟```replace```模式：```<router-link replace .......>News</router-link>```

### 9.編程式路由導航

1. 作用：不借助```<router-link> ```實現路由跳轉，讓路由跳轉更加靈活

2. 具體編碼：

   ```js
   //$router的兩個API
   this.$router.push({
   	name:'xiangqing',
   		params:{
   			id:xxx,
   			title:xxx
   		}
   })
   
   this.$router.replace({
   	name:'xiangqing',
   		params:{
   			id:xxx,
   			title:xxx
   		}
   })
   this.$router.forward() //前進
   this.$router.back() //後退
   this.$router.go() //可前進也可後退
   ```

### 10.緩存路由組件

1. 作用：讓不展示的路由組件保持掛載，不被銷毀。

2. 具體編碼：

   ```vue
   <keep-alive include="News"> 
       <router-view></router-view>
   </keep-alive>
   ```

### 11.兩個新的生命週期鉤子

1. 作用：路由組件所獨有的兩個鉤子，用於捕獲路由組件的激活狀態。
2. 具體名字：
   1. ```activated```路由組件被激活時觸發。
   2. ```deactivated```路由組件失活時觸發。

### 12.路由守衛

1. 作用：對路由進行權限控制

2. 分類：全局守衛、獨享守衛、組件內守衛

3. 全局守衛:

   ```js
   //全局前置守衛：初始化時執行、每次路由切換前執行
   router.beforeEach((to,from,next)=>{
   	console.log('beforeEach',to,from)
   	if(to.meta.isAuth){ //判斷當前路由是否需要進行權限控制
   		if(localStorage.getItem('school') === 'atguigu'){ //權限控制的具體規則
   			next() //放行
   		}else{
   			alert('暫無權限查看')
   			// next({name:'guanyu'})
   		}
   	}else{
   		next() //放行
   	}
   })
   
   //全局後置守衛：初始化時執行、每次路由切換後執行
   router.afterEach((to,from)=>{
   	console.log('afterEach',to,from)
   	if(to.meta.title){ 
   		document.title = to.meta.title //修改網頁的title
   	}else{
   		document.title = 'vue_test'
   	}
   })
   ```

4. 獨享守衛:

   ```js
   beforeEnter(to,from,next){
   	console.log('beforeEnter',to,from)
   	if(to.meta.isAuth){ //判斷當前路由是否需要進行權限控制
   		if(localStorage.getItem('school') === 'atguigu'){
   			next()
   		}else{
   			alert('暫無權限查看')
   			// next({name:'guanyu'})
   		}
   	}else{
   		next()
   	}
   }
   ```

5. 組件內守衛：

   ```js
   //進入守衛：通過路由規則，進入該組件時被調用
   beforeRouteEnter (to, from, next) {
   },
   //離開守衛：通過路由規則，離開該組件時被調用
   beforeRouteLeave (to, from, next) {
   }
   ```

### 13.路由器的兩種工作模式



1. 對於一個url來說，什麼是hash值？ —— #及其後面的內容就是hash值。
2. hash值不會包含在 HTTP 請求中，即：hash值不會帶給服務器。
3. hash模式：
   1. 地址中永遠帶著#號，不美觀 。
   2. 若以後將地址通過第三方手機app分享，若app校驗嚴格，則地址會被標記為不合法。
   3. 兼容性較好。
4. history模式：
   1. 地址乾淨，美觀 。
   2. 兼容性和hash模式相比略差。
   3. 應用部署上線時需要後端人員支持，解決刷新頁面服務端404的問題。