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

## 	變量:普通的值(數字、字串、布爾值)

k: v：字面量直接寫

字串默認不用加上單引號或者雙引號

"": 雙引號，不會轉譯字串裡面的特殊字，特殊字會昨為其本身要表達的意思，例如換行、空格...等

​	EX : name: "zhangsan \n lisi" ---> 輸出 : zhangsan 換行 lisi

'' : 單引號，會轉譯特殊字，特殊字最終只是一個普通的字串數據，例如直接輸出 \

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

```java
@PropertySource(value = {"classpath:person.properties"})
@Component
//@ConfigurationProperties(prefix = "person")
//@Validated
public class Person {

    /**
     * <bean class="Person">
     *      <property name="lastName" value="變量/${key}從環境變量、配置文件中獲取值/#{SpEL}"></property>
     * <bean/>
     */

   //lastName必需是郵件格式
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



@ImportResource: 導入Spring的配置文件，讓配置文件裡面的內容生效

Spring boot 裡面沒有Spring的配置文件，我們自己編寫的配置文件，也不能自動識別，想讓Spring的配置文件生效，加載進來，就把@ImportResource標註在一個配置類上

```java
@ImportResource(locations = {"classpath:beans.xml"})
導入Spring的配置文件讓其生效
```

不用編寫Spring的配置文件

```xml
<?xml version="1.0" encoding="UTF‐8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring‐beans.xsd">
<bean id="helloService" class="com.frank.springboot.service.HelloService"></bean>
</beans>
```

SpringBoot推薦給容器中添加組件的方式，推薦使用全註解的方式

1.配置類@Configuration ---->Spring配置文件

2.使用@Bean給容器中添加組件

```java
/**
 * @Configuration：指名當前類是一個配置類，就是來替代之前的Spring配置文件
 *
 *配置文件中用<bean><bean/>標籤添加組件
 *
 */
@Configuration
public class MyAppConfig {

	//將方法的返回值添加到容器中，容器中這個組件默認的ID就是方法名
    @Bean
    public HelloService helloService02(){
        System.out.println("配置類@Bean給容器中添加組件了");
        return new HelloService();
    }

}
```

# 5.配置占位符

1.隨機數

```java
${random.value}、${random.int}、${random.long}
${random.int(10)}、${random.int[1024,65536]}
```

2.站位符獲取之前配置的值，如果沒有可以是用指定默認值

```properties
person.last‐name=張三${random.uuid}
person.age=${random.int}
person.birth=2017/12/15
person.boss=false
person.maps.k1=v1
person.maps.k2=14
person.lists=a,b,c
person.dog.name=${person.hello:hello}_dog
person.dog.age=15
```

# 6.Profile

### 1.多Profile文件

我們在主配置文件編寫的時候，文件名可以是application-{profile}.properties/yml

默認使用application.properties的配置

### 2.yml支持多文檔塊方式

```yaml
server:
port: 8081
spring:
profiles:
active: prod
‐‐‐
server:
port: 8083
spring:
profiles: dev
‐‐‐
server:
port: 8084
spring:
profiles: prod #指定屬於哪個環境
```

### 3.激活指定的profile

1.在配置文件中指定 spring.profiles.active=dev

2..命令行:

​    java -jar spring-boot-02-config-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev；

可以直接在測試的時候，配置傳入命令行參數

3.虛擬機參數:

-Dspring.profiles.active=dev



# 7.配置文件加載位子

springboot啟動會掃描以下位子的application.properties或者application.yml文件作為spring boot的默認配置文件

–file:./config/
–file:./
–classpath:/config/
–classpath:/

**修先順序高到低，高修先級的配置會覆蓋低優先級的配置**



SpringBoot會從這四個位置全部加載主配置文件，互補配置



==我們還可以通過spring.config.location來改變默認的配置文件位子==

**項目打包好以後，我們可以使用命令行參數的形式，啟動項目的時候來指定配置文件的新位子，指定配置文件和默認加載的這些配置文件共同起作用形成互補配置**

java -jar spring-boot-02-config-02-0.0.1-SNAPSHOT.jar --spring.config.location=G:/application.properties