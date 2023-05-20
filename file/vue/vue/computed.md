# 計算屬性 computed

## 基本寫法

- 1.定義：要用的屬性不存在，要通過已有屬性計算得來。
- 2.原理：底層借助了Objcet.defineproperty方法提供的getter和setter。
- 3.get函數什麼時候執行？
	- (1).初次讀取時會執行一次。
	- (2).當依賴的數據發生改變時會被再次調用。
- 4.優勢：與methods實現相比，內部有緩存機制（復用），效率更高，調試方便。
- 5.備註：
	- 1.計算屬性最終會出現在vm上，直接讀取使用即可。
	- 2.如果計算屬性要被修改，那必須寫set函數去響應修改，且set中要引起計算時依賴的數據發生改變。

```javascript
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
```

## 簡單寫法

- 1.若此屬性只讀不寫，才可以用簡單寫法
- 2.computed 裡面定義的函數屬性及當為getter使用

```javascript
Vue.config.productionTip = false //阻止 vue 在啟動時生成生產提示。

		const vm = new Vue({
			el:'#root',
			data:{
				firstName:'張',
				lastName:'三',
			},
			computed:{
				//完整寫法
				/* fullName:{
					get(){
						console.log('get被調用了')
						return this.firstName + '-' + this.lastName
					},
					set(value){
						console.log('set',value)
						const arr = value.split('-')
						this.firstName = arr[0]
						this.lastName = arr[1]
					}
				} */
				//簡寫
				fullName(){
					console.log('get被調用了')
					return this.firstName + '-' + this.lastName
				}
			}
		})
```