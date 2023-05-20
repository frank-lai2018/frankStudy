
 # 路由

1. 理解： 一個路由（route）就是一組映射關係（key - value），多個路由需要路由器（router）進行管理。
2. 前端路由：key是路徑，value是組件。

## 1.基本使用

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

- router-link標籤會被VUE轉成 a 標籤

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