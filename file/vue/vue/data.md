# 數據綁定

- 1.Vue中有2種數據綁定的方式：
    - 1.單向綁定(`v-bind`)：數據只能從data流向頁面。
    - 2.雙向綁定(`v-model`)：數據不僅能從data流向頁面，還可以從頁面流向data。
        備註：
        - 1.雙向綁定一般都應用在表單類元素上（如：input、select等）
        - 2.v-model:value 可以簡寫為 v-model，因為v-model默認收集的就是value值。

```html
			<input type="text" :value="name"><br/>
			<input type="text" v-model="name"><br/>
```

- 2.{{XXX}}，xxx裡面可以寫JS表達式，或者Vue實例(vm)上可以看見的屬性或方法

- 3.data中所有的屬性，最後都出現在了vm身上。
- 4.vm身上所有的屬性 及 Vue原型上所有屬性，在Vue模板中都可以直接使用。

# 數據代理


- 1.Vue中的數據代理：
	- 通過vm對象來代理data物件中屬性的操作（讀/寫）
- 2.Vue中數據代理的好處：
	- 更加方便的操作data中的數據
- 3.基本原理：
	- 通過Object.defineProperty()把data物件中所有屬性添加到vm上。
	- 為每一個添加到vm上的屬性，都指定一個getter/setter。
	- 在getter/setter內部去操作（讀/寫）data中對應的屬性。

```html
```

## 結論
- 1.只在要工具看到 ... 需要點開才看的到值的，就代表此數據有被getter代理過

![001](img/1.png)

- 2.所有屬性都會被代理到vm實例物件裡
- 3.所有data都會被存進vm實例物件裡的_data屬性(此屬性有被數據劫持，因為需綁定監聽監測變化)