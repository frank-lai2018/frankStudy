# Vue監聽數據的原理

- 1. vue會監視數據中所有層次的數據。

- 2. 如何監測物件中的數據？
	- 通過setter實現監視，且要在新的Vue時就監視的數據。
		- (1).Vue物件建立後追加的屬性，Vue默認不做響應式處理
		- (2).如需給物件建立後添加的屬性做響應式，請使用如下API：
			- Vue.set(target，propertyName/index，value) 或
			- vm.$set(target，propertyName/index，value)

```javascript
	Vue.set(this.student.hobby,0,'開車')
	this.$set(this.student.hobby,0,'開車')
```

- 3. 如何監控陣列中的數據？
	- 通過整體更新元素的方法實現，本質就是兩件事：
		- (1).調用原生的方法對陣列進行更新。
		- (2).重解析模板，再更新頁面。

- 4.在Vue修改陣列 中的某個元素一定要用下面的方法：
	- 1.使用這些API：push()、pop()、shift()、unshift()、splice()、sort()、reverse()
	- 2.Vue.set() 或 vm.$set()

##　特別注意：Vue.set() 和 vm.$set() 不能給 vm 或 vm 的根數據對象添加屬性！！！