# 收集表單數據
- 若：<input type="text"/>，則v-model 如果收集的是value值，用戶輸入的就是value值。
- 若：<input type="radio"/>，則v-model收集的是value值，且要給標籤配置value值。
- 若：<input type="checkbox"/>
	- 1.沒有配置輸入的值屬性，那麼收集的就是checked（勾選或未勾選，是布爾值）
	- 2.配置input的value屬性：
		- (1)v-model的初始值是非數組('',null)，那麼收集的就是checked（勾選或未勾選，是布爾值）
		- (2)v-model的初始值是數組([])，那麼收集的就是值組成的數組

- 備註：v-model 的三個標籤符：
	- lazy：失去焦點再收集數據
	- number：輸入字符串轉為有效的數字
	- trim：輸入首尾空間過濾

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>收集表單數據</title>
		<script type="text/javascript" src="../js/vue.js"></script>
	</head>
	<body>
		<div id="root">
			<form @submit.prevent="demo">
				賬號：<input type="text" v-model.trim="userInfo.account"> <br/><br/>
				密碼：<input type="password" v-model="userInfo.password"> <br/><br/>
				年齡：<input type="number" v-model.number="userInfo.age"> <br/><br/>
				性別：
				男<input type="radio" name="sex" v-model="userInfo.sex" value="male">
				女<input type="radio" name="sex" v-model="userInfo.sex" value="female"> <br/><br/>
				愛好：
				學習<input type="checkbox" v-model="userInfo.hobby" value="study">
				打遊戲<input type="checkbox" v-model="userInfo.hobby" value="game">
				吃飯<input type="checkbox" v-model="userInfo.hobby" value="eat">
				<br/><br/>
				所屬校區
				<select v-model="userInfo.city">
					<option value="">請選擇校區</option>
					<option value="taipei">台北</option>
					<option value="kaohsiung">高雄</option>
					<option value="xhiayi">嘉義</option>
					<option value="yilan">宜蘭</option>
				</select>
				<br/><br/>
				其他信息：
				<textarea v-model.lazy="userInfo.other"></textarea> <br/><br/>
				<input type="checkbox" v-model="userInfo.agree">閱讀並接受<a href="http://www.google.com">《用戶協議》</a>
				<button>提交</button>
			</form>
		</div>
	</body>

	<script type="text/javascript">
		Vue.config.productionTip = false

		new Vue({
			el:'#root',
			data:{
				userInfo:{
					account:'',
					password:'',
					age:18,
					sex:'female',
					hobby:[],
					city:'xhiayi',
					other:'',
					agree:''
				}
			},
			methods: {
				demo(){
					console.log(JSON.stringify(this.userInfo))
				}
			}
		})
	</script>
</html> 
```