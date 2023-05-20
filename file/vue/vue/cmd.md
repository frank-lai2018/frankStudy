# 內置指令

- v-bind	: 單向綁定解析表達式, 可簡寫為 :xxx
- v-model	: 雙向數據綁定					- - v-for  	: 遍歷數組/對象/字符串
- v-on   	: 綁定事件監聽, 可簡寫為@
- v-if 	 	: 條件渲染（動態控制節點是否存存在）
- v-else 	: 條件渲染（動態控制節點是否存存在）
- v-show 	: 條件渲染 (動態控制節點是否展示)
- v-text指令：
	- 1.作用：向其所在的節點中渲染文本內容。
	- 2.與插值語法({{XX}})的區別：v-text會替換掉節點中的內容，{{xx}}則不會。
- v-html指令：
	- 1.作用：向指定節點中渲染包含html結構的內容。
	- 2.與插值語法的區別：
		- (1).v-html會替換掉節點中所有的內容，{{xx}}則不會。
		- (2).v-html可以識別html結構。
	- 3.嚴重註意：v-html有安全性問題！ ！ ！ ！
		- (1).在網站上動態渲染任意HTML是非常危險的，容易導致XSS攻擊。
		- (2).一定要在可信的內容上使用v-html，永不要用在用戶提交的內容上！

- v-cloak指令（沒有值）：
	- 1.本質是一個特殊屬性，Vue實例創建完畢並接管容器後，會刪掉v-cloak屬性。
	- 2.使用css配合v-cloak可以解決網速慢時頁面展示出{{xxx}}的問題。

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>v-cloak指令</title>
		<style>
			[v-cloak]{
				display:none;
			}
		</style>
	</head>
	<body>
		<div id="root">
			<h2 v-cloak>{{name}}</h2>
		</div>
		<script type="text/javascript" src="http://localhost:8080/resource/5s/vue.js"></script>
	</body>
	
	<script type="text/javascript">
		console.log(1)
		Vue.config.productionTip = false //阻止 vue 在啟動時生成生產提示。
		
		new Vue({
			el:'#root',
			data:{
				name:'AAAA'
			}
		})
	</script>
</html>
```	

- v-once指令：
	- 1.v-once所在節點在初次動態渲染後，就視為靜態內容了。
	- 2.以後數據的改變不會引起v-once所在結構的更新，可以用於優化性能。

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>v-once指令</title>
		<!-- 引入Vue -->
		<script type="text/javascript" src="../js/vue.js"></script>
	</head>
	<body>
		<div id="root">
			<h2 v-once>初始化的n值是:{{n}}</h2>
			<h2>當前的n值是:{{n}}</h2>
			<button @click="n++">點我n+1</button>
		</div>
	</body>

	<script type="text/javascript">
		Vue.config.productionTip = false //阻止 vue 在啟動時生成生產提示。
		
		new Vue({
			el:'#root',
			data:{
				n:1
			}
		})
	</script>
</html>
```

- v-pre指令：
	- 1.跳過其所在節點的編譯過程。
	- 2.可利用它跳過：沒有使用指令語法、沒有使用插

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>v-pre指令</title>
		<!-- 引入Vue -->
		<script type="text/javascript" src="../js/vue.js"></script>
	</head>
	<body>
		<div id="root">
			<h2 v-pre>Vue其實很簡單</h2>
			<h2 >當前的n值是:{{n}}</h2>
			<button @click="n++">點我n+1</button>
		</div>
	</body>

	<script type="text/javascript">
		Vue.config.productionTip = false //阻止 vue 在啟動時生成生產提示。

		new Vue({
			el:'#root',
			data:{
				n:1
			}
		})
	</script>
</html>
```


# 自訂義指令

				
- 一、定義語法：
	- (1).局部指令：
	```javascript
		new Vue({															
			directives:{指令名:配置對象} 		
		}) 																		
		new Vue({
			directives{指令名:回調函數}
		})
	```
	- (2).全局指令：
		- Vue.directive(指令名,配置對象) 或   Vue.directive(指令名,回調函數)

- 二、配置對像中常用的3個回調：
	- (1).bind：指令與元素成功綁定時調用。
	- (2).inserted：指令所在元素被插入頁面時調用。
	- (3).update：指令所在模板結構被重新解析時調用。

- 三、備註：
	- 1.指令定義時不加v-，但使用時要加v-；
	- 2.指令名如果是多個單詞，要使用kebab-case命名方式，不要用camelCase命名。

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>自定義指令</title>
		<script type="text/javascript" src="../js/vue.js"></script>
	</head>
	<body>
		<div id="root">
			<h2>{{name}}</h2>
			<h2>當前的n值是：<span v-text="n"></span> </h2>
			<!-- <h2>放大10倍後的n值是：<span v-big-number="n"></span> </h2> -->
			<h2>放大10倍後的n值是：<span v-big="n"></span> </h2>
			<button @click="n++">點我n+1</button>
			<hr/>
			<input type="text" v-fbind:value="n">
		</div>
	</body>
	
	<script type="text/javascript">
		Vue.config.productionTip = false

		//定義全局指令
		/* Vue.directive('fbind',{
			//指令與元素成功綁定時（一上來）
			bind(element,binding){
				element.value = binding.value
			},
			//指令所在元素被插入頁面時
			inserted(element,binding){
				element.focus()
			},
			//指令所在的模板被重新解析時
			update(element,binding){
				element.value = binding.value
			}
		}) */

		new Vue({
			el:'#root',
			data:{
				name:'尚矽谷',
				n:1
			},
			directives:{
				//big函數何時會被調用？ 1.指令與元素成功綁定時（一上來）。 2.指令所在的模板被重新解析時。
				/* 'big-number'(element,binding){
					// console.log('big')
					element.innerText = binding.value * 10
				}, */
				big(element,binding){
					console.log('big',this) //注意此處的this是window
					// console.log('big')
					element.innerText = binding.value * 10
				},
				fbind:{
					//指令與元素成功綁定時（一上來）
					bind(element,binding){
						element.value = binding.value
					},
					//指令所在元素被插入頁面時
					inserted(element,binding){
						element.focus()
					},
					//指令所在的模板被重新解析時
					update(element,binding){
						element.value = binding.value
					}
				}
			}
		})
		
	</script>
</html>
```