# 2.第一個VUE

```html
<script src="./lib/vue.js"></script>
<body>
    <div id="firstVue">
        <p>{{msg}}</p>
    </div>
</body>
<script>
    let vm = new Vue({
        el: "#firstVue", //表示，當前NEW出來的物件，要控制頁面上哪個區域
        data: { //data屬性，存放的是el中要用的數據
            msg:"Hello Vue"
        }
    });
</script>
```
執行結果:

![040](../images/pic040.png)