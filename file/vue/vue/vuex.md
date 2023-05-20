# Vuex

## 1.概念

​		在Vue中實現集中式狀態（數據）管理的一個Vue插件，對vue應用中多個組件的共享狀態進行集中式的管理（讀/寫），也是一種組件間通信的方式，且適用於任意組件間通信。

## 2.何時使用？

​		多個組件需要共享數據時

## 3.搭建vuex環境

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

##    4.基本使用

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

## 5.getters的使用

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

## 6.四個map方法的使用

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

## 7.模塊化+命名空間

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
