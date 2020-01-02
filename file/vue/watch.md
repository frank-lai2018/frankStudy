# 11.watch

vue物件裡哦watch屬性監視數據變動，有變動才會被監測到，故可以用來監測html tag 的value變動，或者監視router連結的變動

*   監視input tag value的變動

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
                fullName:""
            }
        },
        methods: {

        },
        watch: {
            firstName(newVal,oldVal){
                console.log(newVal,"--",oldVal);
                this.fullName =newVal +this.lastName;
            },
            lastName(newVal,oldVal){
                console.log(newVal,"--",oldVal);
                this.fullName =this.firstName +oldVal;
            }
        },
    })
</script>
</html>
```

*   監視router url變動

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
        <router-link to="/login">login</router-link>
        <router-link to="/register">register</router-link>
        <router-view></router-view>
    </div>

    <template id= "login"> 
        <div>
            <h1>login</h1>
        </div>
    </template>
    <template id= "register"> 
        <div>
            <h1>register</h1>
        </div>
    </template>
</body>

<script src="./lib/vue.js"></script>
<script src="./lib/vue-router.js"></script>
<script>

    let login = {
        template:"#login"
    };
    let register = {
        template:"#register"
    };

    let router = new VueRouter({
        routes:[
            {path:"/login",component:login},
            {path:"/register",component:register}

        ],
    });

    let vm = new Vue({
        el:"#app",
        data() {
            return {
                
            }
        },
        methods: {
            watchPath(path) {
                if(path === "/login"){
                    console.log("您正在登入...");
                }else if(path === "/register"){
                    console.log("您正在註冊...");
                }
            },
        },
        watch: {
            "$route.path"(newValue,oldValue){
                if(newValue === "/login"){
                    console.log("您正在登入...");
                }else if(newValue === "/register"){
                    console.log("您正在註冊...");
                }
            }
        },
        
        router
        
    });
</script>
</html>
```