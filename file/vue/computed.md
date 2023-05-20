#   12.computed

*   在Vue物件的computed屬性中定義方法，稱為計算屬性，其只把它當成屬性來用，不作方法使用，故不能調用()

*   computed屬性定義的方法，會存在緩存裡，會監視方法依賴的值有無變化，有變化及會更新緩存，無變化時，調用屬性不在調用方法(嵌套陣列第2層以後裡的元素物件屬性無法監視，可用JSON轉換字串物件搭配處理)


1. 計算屬性
  在computed屬性對像中定義計算屬性的方法
  在頁面中使用{{方法名}}來顯示計算的結果
2. 監視屬性:
  通過通過vm對象的$watch()或watch配置來監視指定的屬性
  當屬性變化時, 回調函數自動調用, 在函數內部進行計算
3. 計算屬性高級:
  通過get/set實現對屬性數據的顯示和監視
  計算屬性存在緩存, 多次讀取只執行一次get計算
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
    <div id="app">
        <input type="text" v-model="firstName"> +
        <input type="text" v-model="lastName" > =
        <input type="text" v-model="fullName">
    
        <p>{{fullName}}</p>
        <p>{{fullName}}</p>
        <p>{{fullName}}</p>
    </div>

</body>
<script src="./lib/vue.js"></script>

<script>
    let vm = new Vue({
        el:"#app",
        data() {
            return {
                firstName:"",
                lastName:"",
            }
        },
        methods: {
            
        },
        watch: {
        },
        // 在computed 中，可以定義一些屬性，這些屬性，叫做【計算屬性】， 計算屬性的，本質，就是一個方法，只不過，我們在使用這些計算屬性的時候，是把它們的名稱，直接當作屬性來使用的；並不會把計算屬性，當作方法去調用；
        // 注意1： 計算屬性，在引用的時候，一定不要加 () 去調用，直接把它 當作 普通 屬性去使用就好了；
        // 注意2： 只要 計算屬性，這個 function 內部，所用到的 任何 data 中的數據發送了變化，就會 立即重新計算 這個 計算屬性的值
        // 注意3： 計算屬性的求值結果，會被緩存起來，方便下次直接使用； 如果計算屬性方法中，所以來的任何數據，都沒有發生過變化，則，不會重新對計算屬性求值；
        computed: {
            fullName(){
                console.log("調用fullName()");
                return this.firstName + "-" +this.lastName;
            }
            
        },
    })
</script>
</html>
```


```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>計算屬性和監視</title>
</head>
<body>
<!--
1. 計算屬性
  在computed屬性對像中定義計算屬性的方法
  在頁面中使用{{方法名}}來顯示計算的結果
2. 監視屬性:
  通過通過vm對象的$watch()或watch配置來監視指定的屬性
  當屬性變化時, 回調函數自動調用, 在函數內部進行計算
3. 計算屬性高級:
  通過getter/setter實現對屬性數據的顯示和監視
  計算屬性存在緩存, 多次讀取只執行一次getter計算
-->
<div id="demo">
  姓: <input type="text" placeholder="First Name" v-model="firstName"><br>
  名: <input type="text" placeholder="Last Name" v-model="lastName"><br>
  <!--fullName1是根據fistName和lastName計算產生-->
  姓名1(單向): <input type="text" placeholder="Full Name1" v-model="fullName1"><br>
  姓名2(單向): <input type="text" placeholder="Full Name2" v-model="fullName2"><br>
  姓名3(雙向): <input type="text" placeholder="Full Name3" v-model="fullName3"><br>

  <p>{{fullName1}}</p>
  <p>{{fullName1}}</p>
</div>

<script type="text/javascript" src="../js/vue.js"></script>
<script type="text/javascript">
  const vm = new Vue({
    el: '#demo',
    data: {
      firstName: 'A',
      lastName: 'B',
       fullName2: 'A-B'
    },

    // 計算屬性配置: 值為對象
    computed: {
      fullName1 () { // 屬性的get()
        console.log('fullName1()', this)
        return this.firstName + '-' + this.lastName
      },

      fullName3: {
        // 當獲取當前屬性值時自動調用, 將返回值(根據相關的其它屬性數據)作為屬性值
        get () {
          console.log('fullName3 get()')
          return this.firstName + '-' + this.lastName
        },
        // 當屬性值發生了改變時自動調用, 監視當前屬性值變化, 同步更新相關的其它屬性值
        set (value) {// fullName3的最新value值  A-B23
          console.log('fullName3 set()', value)
          // 更新firstName和lastName
          const names = value.split('-')
          this.firstName = names[0]
          this.lastName = names[1]
        }
      }
    },

    watch: {
      // 配置監視firstName
      firstName: function (value) { // 相當於屬性的set
        console.log('watch firstName', value)
        // 更新fullName2
        this.fullName2 = value + '-' + this.lastName
      }
    }
  })

  // 監視lastName
  vm.$watch('lastName', function (value) {
    console.log('$watch lastName', value)
    // 更新fullName2
    this.fullName2 = this.firstName + '-' + value
  })

</script>
</body>
</html>
```


