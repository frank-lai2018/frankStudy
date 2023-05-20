
# el 與 data的兩種寫法

- data與el的2種寫法
    - 1.el有2種寫法
        - (1).new Vue時候配置el屬性。
        - (2).先創建Vue實例，隨後再通過vm.$mount('#root')指定el的值。
    - 2.data有2種寫法
        - (1).物件式
        - (2).函數式
        如何選擇：目前哪種寫法都可以，以後學習到組件時，data必須使用函數式，否則會報錯。
    - 3.一個重要的原則：
        - 由Vue管理的函數，一定不要寫箭頭函數，一旦寫了箭頭函數，this就不再是Vue實例了。

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>el與data的兩種寫法</title>
		<!-- 引入Vue -->
		<script type="text/javascript" src="../js/vue.js"></script>
	</head>
	<body>
		<!-- 準備好一個容器-->
		<div id="root">
			<h1>你好，{{name}}</h1>
		</div>
	</body>

	<script type="text/javascript">
		Vue.config.productionTip = false //阻止 vue 在啟動時生成生產提示。

		//el的兩種寫法
		/* const v = new Vue({
			//el:'#root', //第一種寫法
			data:{
				name:'frank'
			}
		})
		console.log(v)
		v.$mount('#root') //第二種寫法 */

		//data的兩種寫法
		new Vue({
			el:'#root',
			//data的第一種寫法：物件式
			/* data:{
				name:'frank'
			} */

			//data的第二種寫法：函數式
			data(){
				console.log('@@@',this) //此處的this是Vue實例對象
				return{
					name:'frank'
				}
			}
		})
	</script>
</html>
```


# [數據綁定、數據代理](vue/data.md)


# [事件](vue/event.md)


# [計算屬性(computed)](vue/computed.md)


# [v-for key的作用](vue/for.md)


# [Vue監聽數據的原理](vue/monitor.md)


# [收集表單數據](vue/form.md)


# [過濾器](vue/filter.md)


# [內置指令、自訂義指令](vue/cmd.md)


# [生命週期](vue/life.md)


# [組件](vue/component.md)


# [vue-cli](vue/vue_cli.md)


# [vuex](vue/vuex.md)


# [router](vue/router.md)




