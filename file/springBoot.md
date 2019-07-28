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

# 3.SpringBoot resources文件夾中目錄結構

1. ​	static : 保存所有的靜態資源(js、CSS、image...等)
2. ​    templates : 保存所有的模板頁面(spring boot默認jar包使用嵌入式的    tomcat ，默認不支持JSP頁面)，可以使用模板引擎(freemarker、 thymeleaf)
3. ​    application.properties：Spring Boot應用的配置文件，可以修改一些默認的配置；

# 4.配置文件注入值

配置文件

```yaml
person:
    lastName: hello
    age: 18
    boss: false
    birth: 2017/12/12
    maps: {k1: v1,k2: 12}
    lists:
        ‐ lisi
        ‐ zhaoliu
    dog:
        name: 小狗
        age: 12
```

javaBean:

```java
/**
 * 將配置文件中配置的每一個屬性值，映射到這個組件中
 * @ConfigurationProperties: 告訴springboot將本類中的所有屬性和配置文件中相關的配置進行綁定
 * 		prefix = "person" :配置文件中哪個下面的所有屬性進行一一映射
 * 
 * 只有這個組件是容器中的組件，才能使用容器提供的@ConfigurationProperties功能
 * */
@Component
@ConfigurationProperties(prefix = "person")
public class Persion {
	
	    private String lastName;
	    private Integer age;
	    private Boolean boss;
	    private Date birth;
	    private Map<String,Object> maps;
	    private List<Object> lists;
	    private Dog dog;
    
```

我們可以導入配置文件處理器，以後編寫配置就有提示了

```xml
	<!--導入配置文件處理器，配置文件進行綁定就會有提示-->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-configuration-processor</artifactId>
<optional>true</optional>
</dependency>

```

#### 1.properties配置文件在idea中默認utf-8可能會亂碼

調整

![](images\pic001.png)

#### 2.@Value 獲取值和@ConfigurationProperties獲取值比較

|                | @ConfigurationProperties | @VALUE     |
| :------------- | ------------------------ | ---------- |
| 功能           | 批量注入配置文件中屬性   | 一個個指定 |
| 鬆散綁定       | 支持                     | 不支持     |
| SpEL           | 不支持                   | 支持       |
| JSR303數據較驗 | 支持                     | 不支持     |
| 複雜類型封裝   | 支持                     | 不支持     |

配置文件yaml還是properties他們都能獲取到值

如過說，我們只是在某個業務邏輯中需要獲取一下配置文件中的某個值，使用@Value

如果說，我們專門編寫了一個javaBean來和配置文件進行映射，我們就直接使用  @ConfigurationProperties

#### 3.配置文件注入值數據校驗

```java
@Component
@ConfigurationProperties(prefix = "person")
@Validated
public class Persion {
	/**
	* <bean class="Person">
	* 	<property name="lastName" value="字面量/${key}從環境變亮、配置文件中獲取/#{SpEL}"></property>
	* <bean/>
	*/
	   //lastName必須是郵件格式
	   // @Email
	    //@Value("${person.last-name}")
	    private String lastName;
	    //@Value("#{11*2}")
	    private Integer age;
	    //@Value("true")
	    private Boolean boss;

	    private Date birth;
	    //@Value("${person.maps}")
	    private Map<String,Object> maps;
	    private List<Object> lists;
	    private Dog dog;
```

#### 4.@PropertySource&@ImportResource&@Bean

@PropertySource: 加載指定的配置文件

