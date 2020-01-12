# 組件間傳值方式

1.使用props

props 可以是陣列或物件，用於接收來自父組件的數據。 props 可以是簡單的陣列，或者使用物件作為替代，物件允許配置高級選項，如類型檢測、自定義驗證和設置默認值。


* 在父tag上以v-bind傳遞數據或函式

* 在子tag裡，使用Vue的props屬性接收父組件傳遞的參數

        // 簡單語法
        Vue.component('props-demo-simple', {
        props: ['size', 'myMessage']
        })

        // 物件語法，提供驗證
        Vue.component('props-demo-advanced', {
        props: {
            // 檢測類型
            height: Number,
            // 檢測類型 + 其他驗證
            age: {
            type: Number,
            default: 0,
            required: true,
            validator: function (value) {
                return value >= 0
            }
            }
        }
        })

type: 可以是下列原生建構函數中的一種：String、Number、Boolean、Array、Object、Date、Function、Symbol、任何自定義建構函數、或上述內容組成的陣列。會檢查一個 prop 是否是給定的類型，否則拋出警告。 Prop 類型的更多信息在此。

default: any
為該 prop 指定一個默認值。如果該 prop 沒有被傳入，則換做用這個值。物件或陣列的默認值必需從一個工廠函數返回。

required: Boolean
定義該 prop 是否是必填項。在非生產環境中，如果這個值為 truthy 且該 prop 沒有被傳入的，則一個控制台警告將會被拋出。

validator: Function
自定義驗證函數會將該 prop 的值作為唯一的參數代入。在非生產環境下，如果該函數返回一個 falsy 的值 (也就是驗證失敗)，一個控制台警告將會被拋出

2.使用 v-on $emit

* 在父組件的tag上使用 v-on綁定函示
* 在子組件使用$emit執行函數(其第一個參數為執行函數名稱，第2個參數開始指定參數)

2.使用$on $emit

* 在父組件的tag上加上 ref定義名子，讓VM可以取到該tag
* 使用 this.$refs.定義名子.$on('函數名稱',傳遞的參數)
* 在子組件使用$emit執行函數(其第一個參數為執行函數名稱，第2個參數開始指定參數)