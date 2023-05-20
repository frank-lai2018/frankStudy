# 組件

## Vue中使用組件的三大步驟：
	- 一、定義組件(創建組件)
	- 二、註冊組件
	- 三、使用組件(寫組件標籤)

- 一、如何定義一個組件？
	- 使用Vue.extend(options)創建，其中options和new Vue(options)時傳入的那個options幾乎一樣，但也有點區別；
	- 區別如下：
		- 1.el不要寫，為什麼？ ——— 最終所有的組件都要經過一個vm的管理，由vm中的el決定服務哪個容器。
		- 2.data必須寫成函數，為什麼？ ———— 避免組件被復用時，數據存在引用關係。
	- 備註：使用template可以配置組件結構。

- 二、如何註冊組件？
	- 1.局部註冊：靠new Vue的時候傳入components選項
	- 2.全局註冊：靠Vue.component('組件名',組件)

- 三、編寫組件標籤：
	-<school></school>


## 注意事項
- 1.使用 Vue.extend，Vue就會創建一個新的VueCompont構造函數
- 2.寫上組件標籤時，就會new出VueCompont實例
- 3.關於組件名:
	- 一個單詞組成：
		- 第一種寫法(首字母小寫)：school
		- 第二種寫法(首字母大寫)：School
	- 多個單詞組成：
		- 第一種寫法(kebab-case命名)：my-school
		- 第二種寫法(CamelCase命名)：MySchool (需要Vue腳手架支持)
	- 備註：
		- (1).組件名盡可能迴避HTML中已有的元素名稱，例如：h2、H2都不行。
		- (2).可以使用name配置項指定組件在開發者工具中呈現的名字。

- 4.關於組件標籤:
		- 第一種寫法：<school></school>
		- 第二種寫法：<school/>
		備註：不用使用腳手架時，<school/>會導致後續組件不能渲染。

- 5.一個簡寫方式：
		- const school = Vue.extend(options) 可簡寫為：const school = options

## 關於VueComponent：

- 1.school組件本質是一個名為VueComponent的構造函數，且不是程序員定義的，是Vue.extend生成的。

- 2.我們只需要寫<school/>或<school></school>，Vue解析時會幫我們創建school組件的實例對象，
	即Vue幫我們執行的：new VueComponent(options)。

- 3.特別注意：每次調用Vue.extend，返回的都是一個全新的VueComponent！ ！ ！ ！

- 4.關於this指向：
	- (1).組件配置中：
				data函數、methods中的函數、watch中的函數、computed中的函數 它們的this均是【VueComponent實例對象】。
	- (2).new Vue(options)配置中：
				data函數、methods中的函數、watch中的函數、computed中的函數 它們的this均是【Vue實例對象】。

## Vue與VueComponent關係
- 1.一個重要的內置關係：VueComponent.prototype.__proto__ === Vue.prototype
- 2.為什麼要有這個關係：讓組件實例對象（vc）可以訪問到 Vue原型上的屬性、方法。


![003](img/3.png)


## mixin(混入)

1. 功能：可以把多個組件共用的配置提取成一個混入物件

2. 使用方式：

    第一步定義混合：

    ```
    {
        data(){....},
        methods:{....}
        ....
    }
    ```

    第二步使用混入：

    ​	全局混入：```Vue.mixin(xxx)```
    ​	局部混入：```mixins:['xxx']	```