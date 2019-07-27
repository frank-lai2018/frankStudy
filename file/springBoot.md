# **1. 配置文件**

SpringBoot使用一個全局的配置文件，配置文件的名稱是固定的:

​	application.properties

​	application.yaml 或 application.yml 

配置文件的作用:修改SpringBoot自動配置的默認值，SpringBoot在底層都給我們自動配置好了

YAML: (YAML Ain't Markup Language)

標記語言

​	以前的配置文件，大都使用XXXX.xml文件
​	YAML:是以數據為中心的文件，比json、XML等更適合做配置文件

​	YAML:配置的例子

```yaml
server:
	port: 8081
```


​	XML:

```xml
<server>
	<port>8081</port>
</server>
```



# 2.YAML語法:

### 1.**基本語法**

​	k:(空格)v : 表示一對件值隊	(空格必須有)

以空格的縮牌來控制層級關係，只要是左邊對其的一列數據，都是同一層級的數據

```yaml
server:
	port: 8081
	path: /hello
```

**有區分大小寫**



### 2.**值得寫法**

## 	字面量:普通的值(數字、字串、布爾值)

k: v：字面量直接寫

字串默認不用加上單引號或者雙引號

"": 雙引號，不會轉譯字串裡面的特殊字，特殊字會昨為其本身要表達的意思，例如換行、空格...等

​	EX : name: "zhangsan \n lisi" ---> 輸出 : zhangsan 換行 lisi

'' : 單引號，患轉譯特殊字，特殊字最終只是一個普通的字串數據，例如直接輸出 \

​	EX : name: ‘zhangsan \n lisi’：輸出；zhangsan \n lisi



​	**物件、Map(屬性和值)(鍵值對) :**

​		K : V 在下一行來寫物件的屬性和值的關係，注意縮排

​		物件還是K: V的方式

```yaml
friends:
	lastName: zhangsan
    age: 20
```

​		同一行的寫法:

```yaml
friends: {lastName: zhangsan,age: 18}
```

​	**陣列(List、Set)**

​		用-(空格)值表示陣列的一個元素

```yaml
pets:
	- cat
	- dog
	- pig
```

​		同一行的寫法:

```yaml
pets: [cat,dog,pig]
```

