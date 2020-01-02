#   12.computed

*   在Vue物件的computed屬性中定義方法，稱為計算屬性，其只把它當成屬性來用，不作方法使用，故不能調用()

*   computed屬性定義的方法，會存在緩存裡，會監視方法依賴的值有無變化，有變化及會更新緩存，無變化時，調用屬性不在調用方法

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
