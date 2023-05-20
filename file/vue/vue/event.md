# 事件

## 基本使用

- 1.使用v-on:xxx 或 @xxx 綁定事件，其中xxx是事件名；
- 2.事件的回調需要配置在methods對像中，最終會在vm上；
- 3.methods中配置的函數，不要用箭頭函數！否則this就不是vm了；
- 4.methods中配置的函數，都是被Vue所管理的函數，this的指向是vm 或 組件實例對象；
- 5.@click="demo" 和 @click="demo($event)" 效果一致，但後者可以傳參；

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>事件的基本使用</title>
		<!-- 引入Vue -->
		<script type="text/javascript" src="../js/vue.js"></script>
	</head>
	<body>
		<!-- 準備好一個容器-->
		<div id="root">
			<h2>歡迎來到{{name}}學習</h2>
			<!-- <button v-on:click="showInfo">點我提示信息</button> -->
			<button @click="showInfo1">點我提示信息1（不傳參）</button>
			<button @click="showInfo2($event,66)">點我提示信息2（傳參）</button>
		</div>
	</body>

	<script type="text/javascript">
		Vue.config.productionTip = false //阻止 vue 在啟動時生成生產提示。

		const vm = new Vue({
			el:'#root',
			data:{
				name:'123',
			},
			methods:{
				showInfo1(event){
					// console.log(event.target.innerText)
					// console.log(this) //此處的this是vm
					alert('同學你好！')
				},
				showInfo2(event,number){
					console.log(event,number)
					// console.log(event.target.innerText)
					// console.log(this) //此處的this是vm
					alert('同學你好！！')
				}
			}
		})
	</script>
</html>
```

## 事件修飾符

- 1.prevent：阻止默認事件（常用）；
- 2.stop：阻止事件冒泡（常用）；
- 3.once：事件只觸發一次（常用）；
- 4.capture：使用事件的捕獲模式；
- 5.self：只有event.target是當前操作的元素時才觸發事件；
- 6.passive：事件的默認行為立即執行，無需等待事件回調執行完畢；
- 7.修飾符可以連續寫(EX:@click.prevent.stop="showInfo")

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>事件修飾符</title>
		<!-- 引入Vue -->
		<script type="text/javascript" src="../js/vue.js"></script>
		<style>
			*{
				margin-top: 20px;
			}
			.demo1{
				height: 50px;
				background-color: skyblue;
			}
			.box1{
				padding: 5px;
				background-color: skyblue;
			}
			.box2{
				padding: 5px;
				background-color: orange;
			}
			.list{
				width: 200px;
				height: 200px;
				background-color: peru;
				overflow: auto;
			}
			li{
				height: 100px;
			}
		</style>
	</head>
	<body>
		<!-- 準備好一個容器-->
		<div id="root">
			<h2>歡迎來到{{name}}學習</h2>
			<!-- 阻止默認事件（常用） -->
			<a href="http://www.atguigu.com" @click.prevent="showInfo">點我提示信息</a>

			<!-- 阻止事件冒泡（常用） -->
			<div class="demo1" @click="showInfo">
				<button @click.stop="showInfo">點我提示信息</button>
				<!-- 修飾符可以連續寫 -->
				<!-- <a href="http://www.atguigu.com" @click.prevent.stop="showInfo">點我提示信息</a> -->
			</div>

			<!-- 事件只觸發一次（常用） -->
			<button @click.once="showInfo">點我提示信息</button>

			<!-- 使用事件的捕獲模式 -->
			<div class="box1" @click.capture="showMsg(1)">
				div1
				<div class="box2" @click="showMsg(2)">
					div2
				</div>
			</div>

			<!-- 只有event.target是當前操作的元素時才觸發事件； -->
			<div class="demo1" @click.self="showInfo">
				<button @click="showInfo">點我提示信息</button>
			</div>

			<!-- 事件的默認行為立即執行，無需等待事件回調執行完畢； -->
			<ul @wheel.passive="demo" class="list">
				<li>1</li>
				<li>2</li>
				<li>3</li>
				<li>4</li>
			</ul>

		</div>
	</body>

	<script type="text/javascript">
		Vue.config.productionTip = false //阻止 vue 在啟動時生成生產提示。

		new Vue({
			el:'#root',
			data:{
				name:'12333'
			},
			methods:{
				showInfo(e){
					alert('同學你好！')
					// console.log(e.target)
				},
				showMsg(msg){
					console.log(msg)
				},
				demo(){
					for (let i = 0; i < 100000; i++) {
						console.log('#')
					}
					console.log('累壞了')
				}
			}
		})
	</script>
</html>
```

## 鍵盤事件

- 1.Vue中常用的按鍵別名：
	- 回車 => enter
	- 刪除 => delete (捕獲“刪除”和“退格”鍵)
	- 退出 => esc
	- 空格 => space
	- 換行 => tab (特殊，必須配合keydown去使用)
	- 上 => up
	- 下 => down
	- 左 => left
	- 右 => right

- 2.Vue未提供別名的按鍵，可以使用按鍵原始的key值去綁定，但注意要轉為kebab-case（短橫線命名）

- 3.系統修飾鍵（用法特殊）：ctrl、alt、shift、meta
			- (1).配合keyup使用：按下修飾鍵的同時，再按下其他鍵，隨後釋放其他鍵，事件才被觸發。
			- (2).配合keydown使用：正常觸發事件。

- 4.也可以使用keyCode去指定具體的按鍵（不推薦）

- 5.Vue.config.keyCodes.自定義鍵名 = 鍵碼，可以去定制按鍵別名

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>鍵盤事件</title>
		<!-- 引入Vue -->
		<script type="text/javascript" src="../js/vue.js"></script>
	</head>
	<body>
		<!-- 準備好一個容器-->
		<div id="root">
			<h2>歡迎來到{{name}}學習</h2>
			<input type="text" placeholder="按下回車提示輸入" @keydown.huiche="showInfo">
		</div>
	</body>

	<script type="text/javascript">
		Vue.config.productionTip = false //阻止 vue 在啟動時生成生產提示。
		Vue.config.keyCodes.huiche = 13 //定義了一個別名按鍵

		new Vue({
			el:'#root',
			data:{
				name:'321321'
			},
			methods: {
				showInfo(e){
					// console.log(e.key,e.keyCode)
					console.log(e.target.value)
				}
			},
		})
	</script>
</html>
```