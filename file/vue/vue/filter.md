
# 過濾器

- 定義：對要顯示的數據進行特定格式化後再顯示（適用於一些簡單邏輯的處理）。
- 語法：
	- 1.註冊過濾器：Vue.filter(name,callback) 或 new Vue{filters:{}}
	- 2.使用過濾器：{{ xxx | 過濾器名}}  或  v-bind:屬性 = "xxx | 過濾器名"
- 備註：
	- 1.過濾器也可以接收額外參數、多個過濾器也可以串聯
	- 2.並沒有改變原本的數據, 是產生新的對應的數據

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>過濾器</title>
		<script type="text/javascript" src="../js/vue.js"></script>
		<script type="text/javascript" src="../js/dayjs.min.js"></script>
	</head>
	<body>
		<div id="root">
			<h2>顯示格式化後的時間</h2>
			<!-- 計算屬性實現 -->
			<h3>現在是：{{fmtTime}}</h3>
			<!-- methods實現 -->
			<h3>現在是：{{getFmtTime()}}</h3>
			<!-- 過濾器實現 -->
			<h3>現在是：{{time | timeFormater}}</h3>
			<!-- 過濾器實現（傳參） -->
			<h3>現在是：{{time | timeFormater('YYYY_MM_DD') | mySlice}}</h3>
			<h3 :x="msg | mySlice">尚矽谷</h3>
		</div>

		<div id="root2">
			<h2>{{msg | mySlice}}</h2>
		</div>
	</body>

	<script type="text/javascript">
		Vue.config.productionTip = false
		//全局過濾器
		Vue.filter('mySlice',function(value){
			return value.slice(0,4)
		})
		
		new Vue({
			el:'#root',
			data:{
				time:1621561377603, //時間戳
				msg:'你好，FRANK'
			},
			computed: {
				fmtTime(){
					return dayjs(this.time).format('YYYY年MM月DD日 HH:mm:ss')
				}
			},
			methods: {
				getFmtTime(){
					return dayjs(this.time).format('YYYY年MM月DD日 HH:mm:ss')
				}
			},
			//局部過濾器
			filters:{
				timeFormater(value,str='YYYY年MM月DD日 HH:mm:ss'){
					// console.log('@',value)
					return dayjs(value).format(str)
				}
			}
		})

		new Vue({
			el:'#root2',
			data:{
				msg:'hello,taiwan!'
			}
		})
	</script>
</html> 
<!DOCTYPE html>
```