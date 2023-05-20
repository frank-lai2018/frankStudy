# 1 配置；

## 1.maven

給maven的settings.xml配置文件的profiles標籤添加

```xml
<profile>
  <id>jdk-1.8</id>
  <activation>
    <activeByDefault>true</activeByDefault>
    <jdk>1.8</jdk>
  </activation>
  <properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
  </properties>
</profile>
```



### 1、創建一個maven工程；（jar）

一個功能：

瀏覽器發送hello請求，服務器接受請求並處理，響應Hello World字符串；

### 2、導入spring boot相關的依賴

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
    </parent>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
```

### 3、編寫一個主程序；啟動Spring Boot應用

```java

/**
 *  @SpringBootApplication 來標註一個主程序類，說明這是一個Spring Boot應用
 */
@SpringBootApplication
public class HelloWorldMainApplication {

    public static void main(String[] args) {

        // Spring應用啟動起來
        SpringApplication.run(HelloWorldMainApplication.class,args);
    }
}
```

### 4、編寫相關的Controller、Service

```java
@Controller
public class HelloController {

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!";
    }
}

```



### 5、運行主程序測試

### 6、簡化部署

```xml
 <!-- 這個插件，可以將應用打包成一個可執行的jar包；-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

**將這個應用打包成JAR包，直接使用java -jar的命令進行執行測試**

### 7、異常處理

發生以下狀況，代表JDK版本問題，需加入以下指令，相容較舊版本
```
--add-opens java.base/java.lang=ALL-UNNAMED
```

![1](spring/springBoot/1.png)

![2](spring/springBoot/2.png)




## 2.範例說明

### 1、POM文件

#### 1、父項目

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.9.RELEASE</version>
</parent>

他的父項目是
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-dependencies</artifactId>
  <version>1.5.9.RELEASE</version>
  <relativePath>../../spring-boot-dependencies</relativePath>
</parent>
他來真正管理Spring Boot應用裡面的所有依賴版本；

```

Spring Boot的版本仲裁中心；

以後我們導入依賴默認是不需要寫版本；（沒有在dependencies裡面管理的依賴自然需要聲明版本號）

#### 2、啟動器

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

**spring-boot-starter**-==web==：

​	spring-boot-starter：spring-boot場景啟動器；幫我們導入了web模塊正常運行所依賴的組件；



Spring Boot將所有的功能場景都抽取出來，做成一個個的starters（啟動器），只需要在項目裡面引入這些starter相關場景的所有依賴都會導入進來。要用什麼功能就導入什麼場景的啟動器



### 2、主程序類，主入口類

```java
/**
 *  @SpringBootApplication 來標註一個主程序類，說明這是一個Spring Boot應用
 */
@SpringBootApplication
public class HelloWorldMainApplication {

    public static void main(String[] args) {

        // Spring應用啟動起來
        SpringApplication.run(HelloWorldMainApplication.class,args);
    }
}

```

@**SpringBootApplication**:    Spring Boot應用標註在某個類上說明這個類是SpringBoot的主配置類，SpringBoot就應該運行這個類的main方法來啟動SpringBoot應用；



```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
      @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
      @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
```

@**SpringBootConfiguration**:Spring Boot的配置類(spring boot提供)；

​		標註在某個類上，表示這是一個Spring Boot的配置類；

​		@**Configuration**:配置類上來標註這個註解(spring提供)；

​			配置類 -----  配置文件；配置類也是容器中的一個組件；@Component



@**EnableAutoConfiguration**：開啟自動配置功能；

​		以前我們需要配置的東西，Spring Boot幫我們自動配置；@**EnableAutoConfiguration**告訴SpringBoot開啟自動配置功能；這樣自動配置才能生效；

```java
@AutoConfigurationPackage
@Import(EnableAutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
```

​      	@**AutoConfigurationPackage**：自動配置包

​		@**Import**(AutoConfigurationPackages.Registrar.class)：

​		Spring的底層註解@Import，給容器中導入一個組件；導入的組件由AutoConfigurationPackages.Registrar.class；

==將主配置類（@SpringBootApplication標註的類）的所在包及下面所有子包裡面的所有組件掃描到Spring容器；==

​	@**Import**(EnableAutoConfigurationImportSelector.class)；

​		給容器中導入組件？

​		**EnableAutoConfigurationImportSelector**：導入哪些組件的選擇器；

​		將所有需要導入的組件以全類名的方式返回；這些組件就會被添加到容器中；

​		會給容器中導入非常多的自動配置類（xxxAutoConfiguration）；就是給容器中導入這個場景需要的所有組件，並配置好這些組件；		![自動配置類](images/搜狗截圖20180129224104.png)

有了自動配置類，免去了我們手動編寫配置注入功能組件等的工作；

​		SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class,classLoader)；



==Spring Boot在啟動的時候從類路徑下的META-INF/spring.factories中獲取EnableAutoConfiguration指定的值，將這些值作為自動配置類導入到容器中，自動配置類就生效，幫我們進行自動配置工作；==以前我們需要自己配置的東西，自動配置類都幫我們；

J2EE的整體整合解決方案和自動配置都在spring-boot-autoconfigure-1.5.9.RELEASE.jar；


==Spring註解版（穀粒學院）==



## 6、使用Spring Initializer快速創建Spring Boot項目

### 1、IDEA：使用 Spring Initializer快速創建項目

IDE都支持使用Spring的項目創建嚮導快速創建一個Spring Boot項目；

選擇我們需要的模塊；嚮導會聯網創建Spring Boot項目；

默認生成的Spring Boot項目；

- 主程序已經生成好了，我們只需要我們自己的邏輯
- resources文件夾中目錄結構
  - static：保存所有的靜態資源； js css  images；
  - templates：保存所有的模板頁面；（Spring Boot默認jar包使用嵌入式的Tomcat，默認不支持JSP頁面）；可以使用模板引擎（freemarker、thymeleaf）；
  - application.properties：Spring Boot應用的配置文件；可以修改一些默認設置；

### 2、STS使用 Spring Starter Project快速創建項目



-------------


# 2.YAML語法:

## **1. 配置文件**

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
### 2.**基本語法**

​	k:(空格)v : 表示一對件值隊	(空格必須有)

以空格的縮牌來控制層級關係，只要是左邊對其的一列數據，都是同一層級的數據

```yaml
server:
    port: 8081
    path: /hello
```

**有區分大小寫**



### 3.**值得寫法**

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

![pic001](images/pic001.png)

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

* default只能讀取properties文件，如果要讀取yaml文件，要實作factory

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

**實作factory**

```java
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

/**
 * 如果要使用@PropertySource讀取yml檔，要自己建立一個可以讀取yam Factory
 * */
public class YamlAndPropertyResourceFactory  implements PropertySourceFactory{

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
	       String resourceName = Optional.ofNullable(name).orElse(resource.getResource().getFilename());
	        if (resourceName.endsWith(".yml") || resourceName.endsWith(".yaml")) {
	            List<PropertySource<?>> yamlSources = new YamlPropertySourceLoader().load(resourceName, resource.getResource());
	            return yamlSources.get(0);
	        } else {
	            return new DefaultPropertySourceFactory().createPropertySource(resourceName, resource);
	        }
	}

}

```

**設定檔**

```yaml
company:
  name: 爽爽爽有限公司
  phone: 0912111222
  emails:
    - test@gmail.com
    - frank_lai@payeasy.com.tw
  address: 鬼島
  maps : {k1: yml111,k2: v2}
```

```properties
company.name=\u723D\u723D\u723D\u6709\u9650\u516C\u53F8
company.phone=0912111222
company.emails[0]=test@gmail.com
company.emails[1]=frank_lai@payeasy.com.tw
company.address=\u9B3C\u5CF6
company.maps.k1==company.properties
company.maps.k2==v2
```

**在@PropertySource 參數裡加入這個工廠**

```java
package com.frank.dao;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.frank.factory.YamlAndPropertyResourceFactory;

@PropertySource(value= {"classpath:company.yml"},factory= YamlAndPropertyResourceFactory.class)
//@PropertySource(value= {"classpath:company.properties"},factory= YamlAndPropertyResourceFactory.class)
@Component
@ConfigurationProperties(prefix = "company") //這行指定要讀取什麼開頭的參數，一定要搭配這個
public class Company {

	private String name;
	private String phone;
	private String address;
	private List<String> emails;
	
	private Map<String,String> maps;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public Map<String, String> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, String> maps) {
		this.maps = maps;
	}

	@Override
	public String toString() {
		return "Company [name=" + name + ", phone=" + phone + ", address=" + address + ", emails=" + emails + ", maps="
				+ maps + "]";
	}
	
	
	
}

```


@ImportResource: 導入Spring的配置文件，讓配置文件裡面的內容生效

### **方法1:**

* 主要用來讀取原來spring的配置xml文件

Spring boot 裡面沒有Spring的配置文件，我們自己編寫的配置文件，也不能自動識別，想讓Spring的配置文件生效，加載進來，就把@ImportResource標註在一個配置類上

```java
//導入Spring的配置文件讓其生效
@ImportResource(locations = {"classpath:beans.xml"})
@SpringBootApplication
public class SpringBoot02ConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot02ConfigApplication.class, args);
	}

}
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

以上方式不是SpringBoot推薦的，SpringBoot推薦給容器中添加組件的方式，推薦使用全註解的方式(寫配置類的方式)

### **方法2:**

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

1.隨機數(spring boot 提供的)

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
#可用:來設置默認值
person.dog.name=${person.hello:hello}_dog
person.dog.age=15
```

# 6.Profile

### **1.多Profile文件**

我們在主配置文件編寫的時候，文件名可以是application-**{profile}**.properties/yml

{profile}:可以自訂名稱，例如:DEV 、PROD....等

默認使用application.properties的配置

### **2.yml支持多文檔塊方式**

**注意:spring boot 預設是先讀取application.properties，沒有此檔案才會讀取**

**application.yml設定**

```yaml
server:
    port: 8081
spring:
    profiles:
        active: prod #用來指定運行時，要用哪個環境的設定檔
‐‐‐ #可以用---來區分3個區塊
server:
    port: 8083
spring:
    profiles: dev #指定屬於哪個環境
‐‐‐
server:
    port: 8084
spring:
    profiles: prod #指定屬於哪個環境
```

### **3.激活指定的profile方式**

1.在配置文件中指定 **spring.profiles.active**=dev (用來指定運行時，要用哪個環境的設定檔)

2..命令行: **--spring.profiles.active=dev**

**cmd/sh:**

​    java -jar spring-boot-02-config-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev；

**eclipse:**

![002](images/002.png)

**idea:**

![003](images/pic003.png)



可以直接在測試的時候，配置傳入命令行參數

3.虛擬機參數:

-Dspring.profiles.active=dev

![004](images/pic004.png)

# 7.配置文件加載位子

springboot啟動會掃描以下位子的application.properties或者application.yml文件作為spring boot的默認配置文件

–file:./config/
–file:./ (當前項目的文件路徑下)
–classpath:/config/
–classpath:/ (類路徑下的跟目錄)

**修先順序高到低，高修先級的配置會覆蓋低優先級的配置**

**也就是說如果file:./config/裡的配置跟classpath:/裡的配置有相同參數時，以file:./config/為準**

![005](images/pic005.png)



SpringBoot會從這四個位置全部加載主配置文件，互補配置



**==我們還可以通過spring.config.location來改變默認的配置文件位子==**

**項目打包好以後，我們可以使用命令行參數的形式，啟動項目的時候來指定配置文件的新位子，指定配置文件和默認加載的這些配置文件共同起作用形成互補配置**

java -jar spring-boot-02-config-02-0.0.1-SNAPSHOT.jar --spring.config.location=G:/application.properties



# 8、外部配置加載程序

**==SpringBoot也可以從以下位子加載配置，優先即從高到低，高優先級的配置覆蓋低修先級的配置，所有的配置會形成互補配置==**

**1.命令行參數**

所有的配置都可以在命令行上進行

```
java -jar spring-boot-02-config-02-0.0.1-SNAPSHOT.jar --server.port=8087  --server.context-path=/abc
```

多個配置用空格分開， --配置項=值



2.來自java:comp/env的JNDI屬性

3.Java系統屬性（System.getProperties()）

4.操作系統環境變量

5.RandomValuePropertySource配置的random.*屬性值



==**由jar包外向jar包內進行尋找；**==

==**優先加載帶profile**==

**6.jar包 外部的application-{profile}.properties或application.yml(带spring.profile)配置文件**

**7.jar包内部的application-{profile}.properties或application.yml(带spring.profile)配置文件**



==**再來加載不帶profile**==

**8.jar包外部的application.properties或application.yml(不帶spring.profile)配置文件**

**9.jar包内部的application.properties或application.yml(不帶spring.profile)配置文件**



10.@Configuration註解上的@PropertySource

11.通過SpringApplication.setDefaultProperties指定的默認屬性

所有支持的配置加載來源；

[參考官方文檔(https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/htmlsingle/#boot-features-external-config)




# 9、自動配置原理

配置文件到底能寫什麼?怎麼寫?自動配置原理

[配置文件能配置的屬性參照](https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/htmlsingle/#common-application-properties)



### 1、**自動配置原理：**

1）、SpringBoot啟動的時候加載主配置類，開啟了自動配置功能 ==@EnableAutoConfiguration==

**2）、@EnableAutoConfiguration 作用：**

 -  利用EnableAutoConfigurationImportSelector給容器導入一些組件？

- 可以查看selectImports()方法的內容；

- List<String> configurations = getCandidateConfigurations(annotationMetadata,      attributes);獲取候選配置





  - ```java
  SpringFactoriesLoader.loadFactoryNames()
    掃描所有jar包類路徑下  META-INF/spring.factories
  把掃描到的這些文件的內容包裝成properties物件
    從properties中獲取到EnableAutoConfiguration.class類（類名）對應的值，然後把它們添加到容器中

    ```



**==將類路徑下  META-INF/sring.factories 裡面配置的所有EnableAutoConfiguration的值加入到容器中；==**

```properties
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
org.springframework.boot.autoconfigure.cloud.CloudAutoConfiguration,\
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration,\
org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration,\
org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration,\
org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.ldap.LdapDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration,\
org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.solr.SolrRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,\
org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration,\
org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration,\
org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration,\
org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration,\
org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration,\
org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration,\
org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration,\
org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration,\
org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration,\
org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration,\
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration,\
org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration,\
org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration,\
org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration,\
org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration,\
org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration,\
org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration,\
org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration,\
org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration,\
org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration,\
org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration,\
org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration,\
org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration,\
org.springframework.boot.autoconfigure.mobile.DeviceResolverAutoConfiguration,\
org.springframework.boot.autoconfigure.mobile.DeviceDelegatingViewResolverAutoConfiguration,\
org.springframework.boot.autoconfigure.mobile.SitePreferenceAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,\
org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration,\
org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,\
org.springframework.boot.autoconfigure.reactor.ReactorAutoConfiguration,\
org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration,\
org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration,\
org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration,\
org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration,\
org.springframework.boot.autoconfigure.session.SessionAutoConfiguration,\
org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration,\
org.springframework.boot.autoconfigure.social.FacebookAutoConfiguration,\
org.springframework.boot.autoconfigure.social.LinkedInAutoConfiguration,\
org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration,\
org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration,\
org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration,\
org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration,\
org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration,\
org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration,\
org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration,\
org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration,\
org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration,\
org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration,\
org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration,\
org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration,\
org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration,\
org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration,\
org.springframework.boot.autoconfigure.websocket.WebSocketMessagingAutoConfiguration,\
org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration
```

每一個這樣的  xxxAutoConfiguration淚都是容器中的一個組件，都加入到容器中；用他們來做自動配置；

3）、每一個自動配置類進行自動配置功能；

4）、以**HttpEncodingAutoConfiguration（Http編碼自動配置）**為例解釋自動配置原理；

```java
@Configuration   //表示這是一個配置類，以前編寫的配置文件一樣，也可以給容器添加組件
@EnableConfigurationProperties(HttpEncodingProperties.class)  //啟動指定類的ConfigurationProperties功能，將配置文件中對應的值和HttpEncodingProperties綁定起來，並把HttpEncodingProperties加入倒IOC容器中

@ConditionalOnWebApplication //Spring底層@Conditional註解，根據不同的條件，如果滿足指定的條件，整個配置類裡面的配置就會生效， @ConditionalOnWebApplication也就是判斷當前應用是否是WEB應用，如果是，當前配置類生效

@ConditionalOnClass(CharacterEncodingFilter.class)  //判斷當前項目有沒有這個類CharacterEncodingFilter，SpringMVC中進行亂碼解決的過濾器
@ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true)  //判斷配置文件是否存在某個配置  spring.http.encoding.enabled。matchIfMissing:如果不存在，判斷也是成立的
//即使我們配置文件中不配置spring.http.encoding.enabled=true，也是默認生效的

public class HttpEncodingAutoConfiguration {

    //他已經和SpringBoot的配置文件映射了
  	private final HttpEncodingProperties properties;

    //只有一個有參數建構式的情況下，參數的值就會從容器中拿
  	public HttpEncodingAutoConfiguration(HttpEncodingProperties properties) {
		this.properties = properties;
	}

    @Bean   //給容器中添加一個組件，這個組件的某些值需要從properties中獲取
	@ConditionalOnMissingBean(CharacterEncodingFilter.class)
    //判斷容器沒有這個組件?
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding(this.properties.getCharset().name());
		filter.setForceRequestEncoding(this.properties.shouldForce(Type.REQUEST));
		filter.setForceResponseEncoding(this.properties.shouldForce(Type.RESPONSE));
		return filter;
	}
```

根據當前不同的條件判斷，決定這個配置類是否生效?

一但這個配置類生效，這個配置類就會給容器中添加各種組件，這些組件的屬性是從對應的properties類中獲取的，這些類裡面的每一個屬性又是和配置文件綁定的

5)、所有在配置文件中能配置的屬性都是在xxxxProperties類中封裝的，配置文件能配置什麼就可以參照某個功能對應的這個屬性類

```java
@ConfigurationProperties(prefix = "spring.http.encoding")
//從配置文件中獲取指定的值和bean的屬性進行綁定
public class HttpEncodingProperties {

   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
```



給容器中的自動配置類添加組件的時候，會從 類中獲取某些屬性。我們就可以在配置文件中指定這些屬性值

**精隨：**

​	**1）、SpringBoot啟動會加載大量的自動配置類**

​	**2）、我們看我們需要的功能有沒有SpringBoot 默認寫好的自動配置類；**

​	**3）、我們在來看這個自動配置類中到底配置了那些組件，(只要我們要用的組件有，我們就不需要再來配置了)**

​	**4）、給容器中的自動配置類添加組件的時候，會從properties類中獲取某些屬性。我們就可以在配置文件中指定這些屬性值**



xxxxAutoConfigurartion：自動配置類；

給容器中添加組件

xxxxProperties:封裝配置文件中相關屬性；


### 2、細節



#### 1、@Conditional原生註解（Spring註解版原生的@Conditional作用）

作用:必需是@Conditional指定的條件成立，才給容器中添加組件，配置類裡面的所有內容才會生效

| @Conditional擴展註解            | 作用（判斷是否滿足當前指定條件）                 |
| ------------------------------- | ------------------------------------------------ |
| @ConditionalOnJava              | 系統的java版本是否符合要求                       |
| @ConditionalOnBean              | 容器中存在指定的Bean；                           |
| @ConditionalOnMissingBean       | 容器中不存在指定的Bean；                         |
| @ConditionalOnExpression        | 滿足指定的SpEL表達式                             |
| @ConditionalOnClass             | 系統中有指定的類                                 |
| @ConditionalOnMissingClass      | 系統中沒有指定的類                               |
| @ConditionalOnSingleCandidate   | 容器中只有一個指定的Bean，或者這個Bean是首選Bean |
| @ConditionalOnProperty          | 系統中指定的屬性是否有指定的值                   |
| @ConditionalOnResource          | 類路徑下是否存在指定資源文件                     |
| @ConditionalOnWebApplication    | 當前是WEB環境                                    |
| @ConditionalOnNotWebApplication | 當前不是web環境                                  |
| @ConditionalOnJndi              | 存在指定的JNDI                                   |

### **自動配置類必須在一定的條件下才能生效**

我們怎麼知道那些自動配置類生效?

**我們可以通過在application.properties配值文件裡配置debug=true屬性，來讓控制台打印自動配置報告，這樣我們就可以很方便的知道那些自動配置類生效**

```java
=========================
AUTO-CONFIGURATION REPORT
=========================


Positive matches:（自動配置類匹配到的，有啟用的）
-----------------

   DispatcherServletAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.web.servlet.DispatcherServlet'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)


Negative matches:（沒有啟用的，沒有匹配的自動配置類）
-----------------

   ActiveMQAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'javax.jms.ConnectionFactory', 'org.apache.activemq.ActiveMQConnectionFactory' (OnClassCondition)

   AopAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.aspectj.lang.annotation.Aspect', 'org.aspectj.lang.reflect.Advice' (OnClassCondition)

```


# 三、日誌

## 1、日志框架

**市面上的日誌框架**

JUL、JCL、Jboss-logging、logback、log4j、log4j2、slf4j....

| 日誌接口                                                     | 日誌實作                                             |
| ------------------------------------------------------------ | ---------------------------------------------------- |
| ~~JCL（Jakarta  Commons Logging）~~    SLF4j（Simple  Logging Facade for Java）    **~~jboss-logging~~** | Log4j  JUL（java.util.logging）  Log4j2  **Logback** |

左邊選一個門面（抽象成）、右邊選一個實作；

日誌接口 ：  SLF4J；

日誌實作：Logback；



SpringBoot：底層式Spring框架，Spring框架默認是用JCL‘

​	**==SpringBoot選 SLF4j和logback；==**


## 2、SLF4j使用

### 1、如何在系統中使用SLF4j   https://www.slf4j.org

以後開發的時候，日誌紀錄方法的調用，不應該來直接調用日誌的實現類，而是調用日誌抽象層裡面的方法

首先給系統裡面導入slf4j的jar和  logback的實現jar

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}
```

圖示；

![images/pic010.png](images/pic010.png)

每一個日誌的實現框架都有自己的配置文件，**使用slf4j以後，配置文件還是做成日誌實現框架自己本身的配置文件**

### 2、遺留問題

a（slf4j+logback）: Spring（commons-logging）、Hibernate（jboss-logging）、MyBatis、xxxx

統一日誌紀錄，即使是別的框架和我們憶起統一使用slf4j進行輸出?

![011](images/pic011.png)

**如何讓系統中所有的日誌都統一到slf4j**

1.將系統中其他的日誌框架先排除出去

2.用中間包來替換原有的日誌框架

3.我們導入slf4j其他的實現



## 3、SpringBoot日誌關係

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
```



SpringBoot使用它來做日誌功能；

```xml
	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-logging</artifactId>
		</dependency>
```

底層依賴關係

![images/pic069.png](images/pic069.png)

總結：

​	1）、SpringBoot底層也是用slf4j+logback的方式進行日誌紀錄

​	2）、SpringBoot也把其他日誌都替換成了slf4j；

​	3）、中間替換包？

```java
@SuppressWarnings("rawtypes")
public abstract class LogFactory {

    static String UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J = "http://www.slf4j.org/codes.html#unsupported_operation_in_jcl_over_slf4j";

    static LogFactory logFactory = new SLF4JLogFactory();
```

![](images/搜狗截图20180131221411.png)



​	4）、如果我們要引入其他框架?一定要把這個框架的默認日誌依賴移除掉?

​			Spring框架用的是commons-logging；

```xml
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<exclusions>
				<exclusion>
					<groupId>commons-logging</groupId>
					<artifactId>commons-logging</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
```



### **SpringBoot能自動匹配所有日誌，而且底層使用slf4j+logback的方式記錄日誌，引入其他框架的時候，只需要把這個框架依賴的日誌框架排除掉即可**

## 4、日誌使用；

### 1、默認配置

SpringBoot默認幫我們配置好了日誌

```java
	//紀錄器
	Logger logger = LoggerFactory.getLogger(getClass());
	@Test
	public void contextLoads() {
		//System.out.println();

		//日誌級別；
		//由低到高   trace<debug<info<warn<error
        //可以調整輸出的日誌級別，日誌就只會在這個級別已以後的高級別生效
		logger.trace("這是trace日誌...");
		logger.debug("這是debug日誌...");
		//SpringBoot默認給我們使用的是info級別的，沒有指定級別的就用SpringBoot默認規定的級別，root級別
		logger.info("這是info日誌...");
		logger.warn("這是warn日誌...");
		logger.error("這是error日誌...");


	}
```



        日誌輸出格式：
    		%d表示日期時間，
    		%thread表示執行緒名，
    		%-5level：級別從左顯示5個字符寬度
    		%logger{50} 表示logger名子最長50個字，否則案這據點分割。
    		%msg：日誌消息，
    		%n是換行
        -->
        %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
SpringBoot修改日誌的默認配置

```properties
#com.frank 可以指定包名
logging.level.com.frank=trace


#logging.path=
#不指定路徑，就在當前項目下生成log日誌
#可以指定完整的路徑:E:/spring.log ，就會在E槽生成
logging.file=E:/spring.log

#在當前的磁盤的根路徑下創建 spring文件夾和裡面的log文件夾，使用spring.log作為默認文件
logging.path=/spring/log

#  在控制台输出的日志的格式
logging.pattern.console= %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
# 指定文件中日志输出的格式
logging.pattern.file= %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
```

| logging.file | logging.path | Example  | Description                        |
| ------------ | ------------ | -------- | ---------------------------------- |
| (none)       | (none)       |          | 指在控制台輸出                     |
| 指定文件名   | (none)       | my.log   | 輸出日誌到my.log文件               |
| (none)       | 指定目錄     | /var/log | 輸出到指定目錄的 spring.log 文件中 |

### 2、指定配置

給類路徑下放上每個日誌框架自己的配置文件即可，SpringBoot就不使用他默認配置的了

| Logging System          | Customization                            |
| ----------------------- | ---------------------------------------- |
| Logback                 | `logback-spring.xml`, `logback-spring.groovy`, `logback.xml` or `logback.groovy` |
| Log4j2                  | `log4j2-spring.xml` or `log4j2.xml`      |
| JDK (Java Util Logging) | `logging.properties`                     |

logback.xml：直接就被日誌框架識別了；

**logback-spring.xml**：日誌框架就不直接加載日誌的配置項，由SpringBoot解析日誌，可以使用SpringBoot的高級Profile功能

```xml
<springProfile name="staging">
    <!-- configuration to be enabled when the "staging" profile is active -->
    可以指定某段配置指在某個環境下生效
</springProfile>

```

如：

```xml
<appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <!--
            %d表示日期時間，
            %thread表示執行緒名，
            %-5level：級別從左顯示5個字符寬度
            %logger{50} 表示logger名子最長50個字，否則案這據點分割。
            %msg：日誌消息，
            %n是換行
        -->
        <layout class="ch.qos.logback.classic.PatternLayout">
            <springProfile name="dev">
                <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} ----> [%thread] ---> %-5level %logger{50} - %msg%n</pattern>
            </springProfile>
            <springProfile name="!dev">
                <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} ==== [%thread] ==== %-5level %logger{50} - %msg%n</pattern>
            </springProfile>
        </layout>
    </appender>
```



如果使用logback.xml作為日誌配置文件，還要使用profile功能，會有以下錯誤

 `no applicable action for [springProfile]`

## 5、切換日誌框架

可以按照slf4j的日誌匹配圖，進行相關的切換

slf4j+log4j的方式；

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <exclusions>
    <exclusion>
      <artifactId>logback-classic</artifactId>
      <groupId>ch.qos.logback</groupId>
    </exclusion>
    <exclusion>
      <artifactId>log4j-over-slf4j</artifactId>
      <groupId>org.slf4j</groupId>
    </exclusion>
  </exclusions>
</dependency>

<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-log4j12</artifactId>
</dependency>

```





切換為log4j2

```xml
   <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>spring-boot-starter-logging</artifactId>
                    <groupId>org.springframework.boot</groupId>
                </exclusion>
            </exclusions>
        </dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

-----------------

-----------------

# 四、Web開發

## 1、簡介



使用SpringBoot；

1)、創建SpringBoot應用，選種我們需要的模塊

2)、SpringBoot已經默認將這些廠景配置好了，只需要在配置文件中指定少量配置就可以運行起來

3)、自己編寫業務程式



**自動配置原理？**

這個場景SpringBoot幫我們配置了什麼？能不能修改？能修改哪些配置？能不能擴展？xxx

```
xxxxAutoConfiguration：幫我們給容器自鄧配置組件；
xxxxProperties:配置類來封裝配置內容；

```



## 2、SpringBoot對靜態資源的映射規則；

```java
@ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
public class ResourceProperties implements ResourceLoaderAware {
    //可以設置和靜態資源有關的參數，缓存時間等
```



```java
	WebMvcAuotConfiguration：
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			if (!this.resourceProperties.isAddMappings()) {
				logger.debug("Default resource handling disabled");
				return;
			}
			Integer cachePeriod = this.resourceProperties.getCachePeriod();
			if (!registry.hasMappingForPattern("/webjars/**")) {
				customizeResourceHandlerRegistration(
						registry.addResourceHandler("/webjars/**")
								.addResourceLocations(
										"classpath:/META-INF/resources/webjars/")
						.setCachePeriod(cachePeriod));
						//設置了缓存時間
			}
			String staticPathPattern = this.mvcProperties.getStaticPathPattern();
          	//靜態資源文件夾映射
			if (!registry.hasMappingForPattern(staticPathPattern)) {
				customizeResourceHandlerRegistration(
						registry.addResourceHandler(staticPathPattern)
								.addResourceLocations(
										this.resourceProperties.getStaticLocations())
						.setCachePeriod(cachePeriod));
			}
		}

        //配置歡迎頁映射
		@Bean
		public WelcomePageHandlerMapping welcomePageHandlerMapping(
				ResourceProperties resourceProperties) {
			return new WelcomePageHandlerMapping(resourceProperties.getWelcomePage(),
					this.mvcProperties.getStaticPathPattern());
		}

       //配置喜歡的圖標
		@Configuration
		@ConditionalOnProperty(value = "spring.mvc.favicon.enabled", matchIfMissing = true)
		public static class FaviconConfiguration {

			private final ResourceProperties resourceProperties;

			public FaviconConfiguration(ResourceProperties resourceProperties) {
				this.resourceProperties = resourceProperties;
			}

			@Bean
			public SimpleUrlHandlerMapping faviconHandlerMapping() {
				SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
				mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
              	//所有  **/favicon.ico
				mapping.setUrlMap(Collections.singletonMap("**/favicon.ico",
						faviconRequestHandler()));
				return mapping;
			}

			@Bean
			public ResourceHttpRequestHandler faviconRequestHandler() {
				ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
				requestHandler
						.setLocations(this.resourceProperties.getFaviconLocations());
				return requestHandler;
			}

		}

```



==1）、所有 /webjars/** ，都去 classpath:/META-INF/resources/webjars/ 找資源；==

​	webjars：以jar包的方式引入靜態資源；

http://www.webjars.org/

![012](images/pic012.png)

### 如果訪問路徑是:**localhost:8080/webjars/jquery/3.3.1/jquery.js**，可以訪問到靜態文件

```xml
<!--引入jquery-webjar-->在訪問的時候只需要寫webjars下面資源的名稱即可
		<dependency>
			<groupId>org.webjars</groupId>
			<artifactId>jquery</artifactId>
			<version>3.3.1</version>
		</dependency>
```



==2）、"/**" 訪問當前項目的任何資源，都去(靜態資源的文夾)找映射==

```
"classpath:/META-INF/resources/",
"classpath:/resources/",
"classpath:/static/",
"classpath:/public/"
"/"：當前項目跟路徑
```

localhost:8080/abc ===  去靜態資源文件夾裡面找abc

EX:http://localhost:8080/asserts/css/dashboard.css

==3）、歡迎頁，靜態資源文件夾下的所有index.html頁面，被"/**"映射；==

​	localhost:8080/   找index頁面

==4）、所有的 **/favicon.ico  都是在靜態資源文件下找；==

### **以上是SpringBoot默認對靜態資源處理的路徑**

也可以更改其除理路徑

```properties
#更改後SpringBoot默認的靜態資源處理路徑失效，只剩下下列路徑可以訪問
spring.resources.static-locations=classoath:/frank/,classpath:/apple/
```



## 3、模板引擎

JSP、Velocity、Freemarker、Thymeleaf

![](images/template-engine.png)



SpringBoot推薦的Thymeleaf；

語法簡單功能強大；



### 1、引入thymeleaf；

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
          	2.1.6
		</dependency>
切换thymeleaf版本
<properties>
		<thymeleaf.version>3.0.9.RELEASE</thymeleaf.version>
		<!-- 布局功能的支持程序  thymeleaf3主程序  layout2以上版本 -->
		<!-- thymeleaf2   layout1-->
		<thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>
  </properties>
```



### 2、Thymeleaf使用

```java
@ConfigurationProperties(prefix = "spring.thymeleaf")
public class ThymeleafProperties {

	private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

	private static final MimeType DEFAULT_CONTENT_TYPE = MimeType.valueOf("text/html");

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";
  	//
```

只要我們把HTML頁面放在classpath:/templates/，thymeleaf就能自動匹配html；

使用：

1、導入thymeleaf的命名空間

```xml
<html lang="en" xmlns:th="http://www.thymeleaf.org">
```

2、使用thymeleaf語法；

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h1>成功！</h1>
    <!--th:text 將div裡面的文本內容設置為 -->
    <div th:text="${hello}">這是喜事歡迎的訊息</div>
</body>
</html>
```

### 3、語法規則

1）、th:text；改變當前元素裡面的文本內容；

​	th：任意html數'ˋ；來替換原生屬性的值

![](images/2018-02-04_123955.png)



2）、表達式？

```properties
Simple expressions:（表達式語法）
    Variable Expressions: ${...}：獲取變量值；OGNL；
    		1）、獲取物件的屬性、調用方法
    		2）、使用內置基本物件：
    			#ctx : the context object.
    			#vars: the context variables.
                #locale : the context locale.
                #request : (only in Web Contexts) the HttpServletRequest object.
                #response : (only in Web Contexts) the HttpServletResponse object.
                #session : (only in Web Contexts) the HttpSession object.
                #servletContext : (only in Web Contexts) the ServletContext object.

                ${session.foo}
            3）、内置的一些工具物件：
#execInfo : information about the template being processed.
#messages : methods for obtaining externalized messages inside variables expressions, in the same way as they would be obtained using #{…} syntax.
#uris : methods for escaping parts of URLs/URIs
#conversions : methods for executing the configured conversion service (if any).
#dates : methods for java.util.Date objects: formatting, component extraction, etc.
#calendars : analogous to #dates , but for java.util.Calendar objects.
#numbers : methods for formatting numeric objects.
#strings : methods for String objects: contains, startsWith, prepending/appending, etc.
#objects : methods for objects in general.
#bools : methods for boolean evaluation.
#arrays : methods for arrays.
#lists : methods for lists.
#sets : methods for sets.
#maps : methods for maps.
#aggregates : methods for creating aggregates on arrays or collections.
#ids : methods for dealing with id attributes that might be repeated (for example, as a result of an iteration).

    Selection Variable Expressions: *{...}：選擇表達式：和${}在功能上是一樣；
    	補充：配合 th:object="${session.user}：
   <div th:object="${session.user}">
    <p>Name: <span th:text="*{firstName}">Sebastian</span>.</p>
    <p>Surname: <span th:text="*{lastName}">Pepper</span>.</p>
    <p>Nationality: <span th:text="*{nationality}">Saturn</span>.</p>
    </div>

    Message Expressions: #{...}：獲取國際化內容
    Link URL Expressions: @{...}：定義URL；
    		@{/order/process(execId=${execId},execType='FAST')}
    Fragment Expressions: ~{...}：片段引用表答式
    		<div th:insert="~{commons :: main}">...</div>

Literals（字面量）
      Text literals: 'one text' , 'Another one!' ,…
      Number literals: 0 , 34 , 3.0 , 12.3 ,…
      Boolean literals: true , false
      Null literal: null
      Literal tokens: one , sometext , main ,…
Text operations:（文本操作）
    String concatenation: +
    Literal substitutions: |The name is ${name}|
Arithmetic operations:（數據運算）
    Binary operators: + , - , * , / , %
    Minus sign (unary operator): -
Boolean operations:（布爾運算）
    Binary operators: and , or
    Boolean negation (unary operator): ! , not
Comparisons and equality:（比較運算）
    Comparators: > , < , >= , <= ( gt , lt , ge , le )
    Equality operators: == , != ( eq , ne )
Conditional operators:條件運算（三元運算符）
    If-then: (if) ? (then)
    If-then-else: (if) ? (then) : (else)
    Default: (value) ?: (defaultvalue)
Special tokens:
    No-Operation: _
```

## 4、SpringMVC自動配置

https://docs.spring.io/spring-boot/docs/1.5.10.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications

### 1. Spring MVC auto-configuration

Spring Boot 自動配置好了SpringMVC

以下是SpringBoot對SpringMVC的默認配置:**==（WebMvcAutoConfiguration）==**

- Inclusion of `ContentNegotiatingViewResolver` and `BeanNameViewResolver` beans.
  - 自動配置了ViewResolver（視圖解析器：根據方法的返回值得到視圖物件（View），視圖物件決定如何渲染（轉發？重定向？））
  - ContentNegotiatingViewResolver：組合所有的試圖解析器的；
  - ==如何定制:我們可以自己給容器中添加一個是圖解析器，自動的將其組合進來；==

- Support for serving static resources, including support for WebJars (see below).靜態資源文件夾路徑,webjars

- Static `index.html` support. 靜態首頁訪問

- Custom `Favicon` support (see below).  favicon.ico



- 自動註冊了of `Converter`, `GenericConverter`, `Formatter` beans.

  - Converter：轉換器；  public String hello(User user)：類型轉換使用Converter
  - `Formatter`  格式化器；  2017.12.17===Date；

```java
		@Bean
		@ConditionalOnProperty(prefix = "spring.mvc", name = "date-format")//在文件配置日期格式化規則
		public Formatter<Date> dateFormatter() {
			return new DateFormatter(this.mvcProperties.getDateFormat());//日期格式化組件
		}
```

​	==自己添加的格式化器轉換器，我們只需要放在容器中即可==

- Support for `HttpMessageConverters` (see below).

  - HttpMessageConverter：SpringMVC用來轉換Http請求和響應的；User---Json；

  - `HttpMessageConverters` 是從容器中確定，獲取所有的HttpMessageConverter；

    ==自己給容器添加HttpMessageConverter，只需要將自己的組件註冊在容器中（@Bean,@Component）==



- Automatic registration of `MessageCodesResolver` (see below).定義錯誤代碼生成規則

- Automatic use of a `ConfigurableWebBindingInitializer` bean (see below).

  ==我們可以配置一個ConfigurableWebBindingInitializer來替換默認的(添加到容器)==

  ```
  初始化WebDataBinder；
  請求數據=====JavaBean；
  ```

**org.springframework.boot.autoconfigure.web：web的所有自動場景；**

If you want to keep Spring Boot MVC features, and you just want to add additional [MVC configuration](https://docs.spring.io/spring/docs/4.3.14.RELEASE/spring-framework-reference/htmlsingle#mvc) (interceptors, formatters, view controllers etc.) you can add your own `@Configuration` class of type `WebMvcConfigurerAdapter`, but **without** `@EnableWebMvc`. If you wish to provide custom instances of `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter` or `ExceptionHandlerExceptionResolver` you can declare a `WebMvcRegistrationsAdapter` instance providing such components.

If you want to take complete control of Spring MVC, you can add your own `@Configuration` annotated with `@EnableWebMvc`.

### 2、擴展SpringMVC

```xml
    <mvc:view-controller path="/hello" view-name="success"/>
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/hello"/>
            <bean></bean>
        </mvc:interceptor>
    </mvc:interceptors>
```

**==編寫一個配置類（@Configuration），是WebMvcConfigurerAdapter類型；不能標註@EnableWebMvc==**;

既保留了所有的自動配置，也能用我們擴展的配置

```java
//使用WebMvcConfigurerAdapter可以來擴展SpringMVC的功能
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       // super.addViewControllers(registry);
        //瀏覽器發送/atguigu 請求來到 success
        registry.addViewController("/atguigu").setViewName("success");
    }
}
```

原理：

​	1）、WebMvcAutoConfiguration是SpringMVC的自動配置類

​	2）、在做其他自動配置時會導入；@Import(**EnableWebMvcConfiguration**.class)

```java
    @Configuration
	public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration {
      private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();

	 //從容器中獲取所有的WebMvcConfigurer
      @Autowired(required = false)
      public void setConfigurers(List<WebMvcConfigurer> configurers) {
          if (!CollectionUtils.isEmpty(configurers)) {
              this.configurers.addWebMvcConfigurers(configurers);
            	//一個參考實現，將所有的WebMvcConfigurer相關配置都來一起調用
            	@Override
             // public void addViewControllers(ViewControllerRegistry registry) {
              //    for (WebMvcConfigurer delegate : this.delegates) {
               //       delegate.addViewControllers(registry);
               //   }
              }
          }
	}
```

​	3）、容器中所有的WebMvcConfigurer都會一起起作用；

​	4）、我們的配置類也會被調用；

​	效果：SpringMVC的自動配置和我們的擴展配置都會起作用；

### 3、全面接管SpringMVC；

SpringBoot对SpringMVC的自动配置不需要了，所有都是我们自己配置；所有的SpringMVC的自动配置都失效了

**我们需要在配置类中添加@EnableWebMvc即可；**

```java
//使用WebMvcConfigurerAdapter可以來擴展SpringMVC的功能
@EnableWebMvc
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       // super.addViewControllers(registry);
        //瀏覽器方送 /atguigu 情求來到 success
        registry.addViewController("/atguigu").setViewName("success");
    }
}
```

原理：

為什麼@EnableWebMvc自動配置就生效了；

1）@EnableWebMvc的核心

```java
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
```

2）、

```java
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
```

3）、

```java
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class,
		WebMvcConfigurerAdapter.class })
//容器中沒有這個組件的時候，這個自動配置類才能生效
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class,
		ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
```

4）、@EnableWebMvc將WebMvcConfigurationSupport組件導入進來；

5）、导入的WebMvcConfigurationSupport只是SpringMVC最基本的功能；



## 5、如何修改SpringBoot的默認配置

模式：

​	1）、SpringBoot在自動配置很多組件的時候，先看容器中有沒有用戶自己配置的（@Bean、@Component）如果有就用用戶配置的，如果沒有，才自動配置，如果有些組件可以有多少(ViewResolver)  將用戶配置的合自己默認的組合起來

​	2）、在SpringBoot中會有非常多的xxxConfigurer幫助我們進行擴展配置

​	3）、在SpringBoot中會有非常多xxxCustomizer幫助我們進行擴展配置

## 6、RestfulCRUD

### 1）、默認訪問首頁

```java

//使用WebMvcConfigurerAdapter可以來擴展SpringMVC的功能
//@EnableWebMvc   不要接管SpringMVC
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       // super.addViewControllers(registry);
        //瀏覽器發送 /atguigu 請求來到 success
        registry.addViewController("/atguigu").setViewName("success");
    }

    //所有的WebMvcConfigurerAdapter組件都會一起起作用
    @Bean //將組件註冊在容器
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("login");
                registry.addViewController("/index.html").setViewName("login");
            }
        };
        return adapter;
    }
}

```

### 2）、國際化

**1）、編寫國際化配置文件；**

2）、使用ResourceBundleMessageSource管理國際化資源文件

3）、在頁面使用fmt:message取出國際化內容



步驟：

1)、編寫國際化配置文件，抽取頁面需要顯示的國際化消息

![](images/搜狗截图20180211130721.png)



2）、SpringBoot自動配置好了管理國際化資源文件的組件；

```java
@ConfigurationProperties(prefix = "spring.messages")
public class MessageSourceAutoConfiguration {

    /**
	 * Comma-separated list of basenames (essentially a fully-qualified classpath
	 * location), each following the ResourceBundle convention with relaxed support for
	 * slash based locations. If it doesn't contain a package qualifier (such as
	 * "org.mypackage"), it will be resolved from the classpath root.
	 */
	private String basename = "messages";

    //我們的配置文件可以直接放在類路徑下叫messages.properties
    @Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		if (StringUtils.hasText(this.basename)) {
			//設置國際化資源文件的基礎名(去掉語言國家代碼的)
            messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(
					StringUtils.trimAllWhitespace(this.basename)));
		}
		if (this.encoding != null) {
			messageSource.setDefaultEncoding(this.encoding.name());
		}
		messageSource.setFallbackToSystemLocale(this.fallbackToSystemLocale);
		messageSource.setCacheSeconds(this.cacheSeconds);
		messageSource.setAlwaysUseMessageFormat(this.alwaysUseMessageFormat);
		return messageSource;
	}
```



3）、去頁面獲取國際化的值；

![](images/搜狗截图20180211134506.png)



```html
<!DOCTYPE html>
<html lang="en"  xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<meta name="description" content="">
		<meta name="author" content="">
		<title>Signin Template for Bootstrap</title>
		<!-- Bootstrap core CSS -->
		<link href="asserts/css/bootstrap.min.css" th:href="@{/webjars/bootstrap/4.0.0/css/bootstrap.css}" rel="stylesheet">
		<!-- Custom styles for this template -->
		<link href="asserts/css/signin.css" th:href="@{/asserts/css/signin.css}" rel="stylesheet">
	</head>

	<body class="text-center">
		<form class="form-signin" action="dashboard.html">
			<img class="mb-4" th:src="@{/asserts/img/bootstrap-solid.svg}" src="asserts/img/bootstrap-solid.svg" alt="" width="72" height="72">
			<h1 class="h3 mb-3 font-weight-normal" th:text="#{login.tip}">Please sign in</h1>
			<label class="sr-only" th:text="#{login.username}">Username</label>
			<input type="text" class="form-control" placeholder="Username" th:placeholder="#{login.username}" required="" autofocus="">
			<label class="sr-only" th:text="#{login.password}">Password</label>
			<input type="password" class="form-control" placeholder="Password" th:placeholder="#{login.password}" required="">
			<div class="checkbox mb-3">
				<label>
          		<input type="checkbox" value="remember-me"/> [[#{login.remember}]]
        </label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit" th:text="#{login.btn}">Sign in</button>
			<p class="mt-5 mb-3 text-muted">© 2017-2018</p>
			<a class="btn btn-sm">中文</a>
			<a class="btn btn-sm">English</a>
		</form>

	</body>

</html>
```

效果:根據瀏覽器語言設置的信息切換了國際化

原理：

​	國際化Locale（區域信息物件）；LocaleResolver（或取區域信息物件）；

```java
		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = "spring.mvc", name = "locale")
		public LocaleResolver localeResolver() {
			if (this.mvcProperties
					.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
				return new FixedLocaleResolver(this.mvcProperties.getLocale());
			}
			AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
			localeResolver.setDefaultLocale(this.mvcProperties.getLocale());
			return localeResolver;
		}
默認的就是根據請求投來的區域訊息獲取Locale進行國際化
```

4）、點及連接切換國際化

```java
/**
 *
 可以在連接上攜帶區域信息
 */
public class MyLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String l = request.getParameter("l");
        Locale locale = Locale.getDefault();
        if(!StringUtils.isEmpty(l)){
            String[] split = l.split("_");
            locale = new Locale(split[0],split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}


 @Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }
}


```

### 3）、登入

開發期間模板引擎頁面修改後，要實時生效

1）、禁用模板引擎的緩存

```
# 禁用緩存
spring.thymeleaf.cache=false
```

2）、頁面修改完成以後ctrl+f9：重新編譯；

登入錯誤消息的顯示

```html
<p style="color: red" th:text="${msg}" th:if="${not #strings.isEmpty(msg)}"></p>
```



### 4）、攔截器進行登陸檢查

攔截器

```java

/**
 * 登陸檢查，
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
    //目標方法執行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("loginUser");
        if(user == null){
            //未登陸，返回登陸頁面
            request.setAttribute("msg","沒有權限請先登陸");
            request.getRequestDispatcher("/index.html").forward(request,response);
            return false;
        }else{
            //已登陸，放行請求
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

```

註冊攔截器

```java
  //所有的WebMvcConfigurerAdapter組件都會一起起作用
    @Bean //將組件註冊在容器
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("login");
                registry.addViewController("/index.html").setViewName("login");
                registry.addViewController("/main.html").setViewName("dashboard");
            }

            //註冊攔截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //super.addInterceptors(registry);
                //靜態資源；  *.css , *.js
                //SpringBoot已經做好了靜態資源映射
                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/index.html","/","/user/login");
            }
        };
        return adapter;
    }
```
### 5）、CRUD-員工列表

實驗要求：

1）、RestfulCRUD：CRUD滿足Rest風格；

URI：  /資源名稱/資源標識       HTTP請求方式區分對資源CRUD操作

|      | 普通CRUD（uri來區分操作） | RestfulCRUD       |
| ---- | ------------------------- | ----------------- |
| 查詢 | getEmp                    | emp---GET         |
| 添加 | addEmp?xxx                | emp---POST        |
| 修改 | updateEmp?id=xxx&xxx=xx   | emp/{id}---PUT    |
| 刪除 | deleteEmp?id=1            | emp/{id}---DELETE |

2）、實驗的請求架構;

| 實驗功能                             | 請求URI | 請求方式 |
| ------------------------------------ | ------- | -------- |
| 查詢所有員工                         | emps    | GET      |
| 查詢某個員工(來到修改頁面)           | emp/1   | GET      |
| 來到添加頁面                         | emp     | GET      |
| 添加員工                             | emp     | POST     |
| 來到修改頁面（查出員工進行信息回顯） | emp/1   | GET      |
| 修改員工                             | emp     | PUT      |
| 刪除員工                             | emp/1   | DELETE   |

3）、員工列表：

#### thymeleaf公共頁面元素抽取

```html

1、抽取公共片段
<div th:fragment="copy">
&copy; 2011 The Good Thymes Virtual Grocery
</div>

2、引入公共片段
<div th:insert="~{footer :: copy}"></div>
~{templatename::selector}：模板名::選擇器
~{templatename::fragmentname}:模板名::片段名

3、默認效果：
insert的公共片段在div標籤中
如果使用th:insert等屬性進行引入，可以不用寫~{}：
行內寫法可以加上：[[~{}]];[(~{})]；
```



三種引入公共片段的th屬性：

**th:insert**：將公共片段整個插入到聲明引入的元素中

**th:replace**：將聲明引入的元素替換為公共片段

**th:include**：將被引入的片段的內容包含進這個標籤中



```html
<footer th:fragment="copy">
&copy; 2011 The Good Thymes Virtual Grocery
</footer>

引入方式
<div th:insert="footer :: copy"></div>
<div th:replace="footer :: copy"></div>
<div th:include="footer :: copy"></div>

效果
<div>
    <footer>
    &copy; 2011 The Good Thymes Virtual Grocery
    </footer>
</div>

<footer>
&copy; 2011 The Good Thymes Virtual Grocery
</footer>

<div>
&copy; 2011 The Good Thymes Virtual Grocery
</div>
```


引入片段的時候傳入參數：

```html

<nav class="col-md-2 d-none d-md-block bg-light sidebar" id="sidebar">
    <div class="sidebar-sticky">
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link active"
                   th:class="${activeUri=='main.html'?'nav-link active':'nav-link'}"
                   href="#" th:href="@{/main.html}">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-home">
                        <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                        <polyline points="9 22 9 12 15 12 15 22"></polyline>
                    </svg>
                    Dashboard <span class="sr-only">(current)</span>
                </a>
            </li>

<!--引入側邊欄;傳入參數-->
<div th:replace="commons/bar::#sidebar(activeUri='emps')"></div>
```
### 6）、CRUD-員工添加

添加頁面

```html
<form>
    <div class="form-group">
        <label>LastName</label>
        <input type="text" class="form-control" placeholder="zhangsan">
    </div>
    <div class="form-group">
        <label>Email</label>
        <input type="email" class="form-control" placeholder="zhangsan@atguigu.com">
    </div>
    <div class="form-group">
        <label>Gender</label><br/>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender"  value="1">
            <label class="form-check-label">男</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender"  value="0">
            <label class="form-check-label">女</label>
        </div>
    </div>
    <div class="form-group">
        <label>department</label>
        <select class="form-control">
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
            <option>5</option>
        </select>
    </div>
    <div class="form-group">
        <label>Birth</label>
        <input type="text" class="form-control" placeholder="zhangsan">
    </div>
    <button type="submit" class="btn btn-primary">添加</button>
</form>
```

提交的數據格式不對：生日：日期；

2017-12-12；2017/12/12；2017.12.12；

日期的格式化；SpringMVC將頁面提交的值需要轉換為指定的類型;

2017-12-12---Date； 類型轉換，格式化;

默認日期是按照/的方式；

### 7）、CRUD-員工修改

修改添加二合一表單

```html
<!--需要區分是員工修改還是添加；-->
<form th:action="@{/emp}" method="post">
    <!--發送put請求修改員工數據-->
    <!--
1、SpringMVC中配置HiddenHttpMethodFilter;（SpringBoot自動配置好的）
2、頁面創建一個post表單
3、創建一個input項，name="_method";值就是我們指定的請求方式
-->
    <input type="hidden" name="_method" value="put" th:if="${emp!=null}"/>
    <input type="hidden" name="id" th:if="${emp!=null}" th:value="${emp.id}">
    <div class="form-group">
        <label>LastName</label>
        <input name="lastName" type="text" class="form-control" placeholder="zhangsan" th:value="${emp!=null}?${emp.lastName}">
    </div>
    <div class="form-group">
        <label>Email</label>
        <input name="email" type="email" class="form-control" placeholder="zhangsan@atguigu.com" th:value="${emp!=null}?${emp.email}">
    </div>
    <div class="form-group">
        <label>Gender</label><br/>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender" value="1" th:checked="${emp!=null}?${emp.gender==1}">
            <label class="form-check-label">男</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender" value="0" th:checked="${emp!=null}?${emp.gender==0}">
            <label class="form-check-label">女</label>
        </div>
    </div>
    <div class="form-group">
        <label>department</label>
        <!--提交的是部門的id-->
        <select class="form-control" name="department.id">
            <option th:selected="${emp!=null}?${dept.id == emp.department.id}" th:value="${dept.id}" th:each="dept:${depts}" th:text="${dept.departmentName}">1</option>
        </select>
    </div>
    <div class="form-group">
        <label>Birth</label>
        <input name="birth" type="text" class="form-control" placeholder="zhangsan" th:value="${emp!=null}?${#dates.format(emp.birth, 'yyyy-MM-dd HH:mm')}">
    </div>
    <button type="submit" class="btn btn-primary" th:text="${emp!=null}?'修改':'添加'">添加</button>
</form>
```

### 8）、CRUD-員工刪除

```html
<tr th:each="emp:${emps}">
    <td th:text="${emp.id}"></td>
    <td>[[${emp.lastName}]]</td>
    <td th:text="${emp.email}"></td>
    <td th:text="${emp.gender}==0?'女':'男'"></td>
    <td th:text="${emp.department.departmentName}"></td>
    <td th:text="${#dates.format(emp.birth, 'yyyy-MM-dd HH:mm')}"></td>
    <td>
        <a class="btn btn-sm btn-primary" th:href="@{/emp/}+${emp.id}">編輯</a>
        <button th:attr="del_uri=@{/emp/}+${emp.id}" class="btn btn-sm btn-danger deleteBtn">刪除</button>
    </td>
</tr>


<script>
    $(".deleteBtn").click(function(){
        //刪除當前員工的
        $("#deleteEmpForm").attr("action",$(this).attr("del_uri")).submit();
        return false;
    });
</script>
```



## 7、錯誤處理機制

### 1）、SpringBoot默認的錯誤處理機制

默認效果：

​		1）、瀏覽器，返回一個默認的錯誤頁面

![](images/搜狗截圖20180226173408.png)

  瀏覽器發送請求的請求頭：

![](images/搜狗截圖20180226180347.png)

​		2）、如果是其他客戶端，默認響應一個json數據

![](images/搜狗截圖20180226173527.png)

​		![](images/搜狗截圖20180226180504.png)

原理：

​	可以參照ErrorMvcAutoConfiguration；錯誤處理的自動配置；

  	給容器中添加了以下組件

​	1、DefaultErrorAttributes：

```java
幫我們在頁面共享信息；
@Override
	public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
			boolean includeStackTrace) {
		Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
		errorAttributes.put("timestamp", new Date());
		addStatus(errorAttributes, requestAttributes);
		addErrorDetails(errorAttributes, requestAttributes, includeStackTrace);
		addPath(errorAttributes, requestAttributes);
		return errorAttributes;
	}
```



​	2、BasicErrorController：處理默認/error請求

```java
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {

    @RequestMapping(produces = "text/html")//產生html類型的數據；瀏覽器發送的請求來到這個方法處理
	public ModelAndView errorHtml(HttpServletRequest request,
			HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
				request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
		response.setStatus(status.value());

        //去哪個頁面作為錯誤頁面；包含頁面地址和頁面內容
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
		return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
	}

	@RequestMapping
	@ResponseBody    //產生json數據，其他客戶端來到這個方法處理；
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
		HttpStatus status = getStatus(request);
		return new ResponseEntity<Map<String, Object>>(body, status);
	}
```

​3、ErrorPageCustomizer：

```java
	@Value("${error.path:/error}")
	private String path = "/error";  系統出現錯誤以後來到error請求進行處理；（web.xml註冊的錯誤頁面規則）
```



​	4、DefaultErrorViewResolver：

```java
@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status,
			Map<String, Object> model) {
		ModelAndView modelAndView = resolve(String.valueOf(status), model);
		if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
			modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
		}
		return modelAndView;
	}

	private ModelAndView resolve(String viewName, Map<String, Object> model) {
        //默認SpringBoot可以去找到一個頁面？ error/404
		String errorViewName = "error/" + viewName;

        //模板引擎可以解析這個頁面地址就用模板引擎解析
		TemplateAvailabilityProvider provider = this.templateAvailabilityProviders
				.getProvider(errorViewName, this.applicationContext);
		if (provider != null) {
            //模板引擎可用的情況下返回到errorViewName指定的視圖地址
			return new ModelAndView(errorViewName, model);
		}
        //模板引擎不可用，就在靜態資源文件夾下找errorViewName對應的頁面   error/404.html
		return resolveResource(errorViewName, model);
	}
```


​步驟：

​		一但係統出現4xx或者5xx之類的錯誤；ErrorPageCustomizer就會生效（定制錯誤的響應規則）；就會來到/error請求；就會被**BasicErrorController**處理；

​		1）響應頁面；去哪個頁面是由**DefaultErrorViewResolver**解析得到的；

```java
protected ModelAndView resolveErrorView(HttpServletRequest request,
      HttpServletResponse response, HttpStatus status, Map<String, Object> model) {
    //所有的ErrorViewResolver得到ModelAndView
   for (ErrorViewResolver resolver : this.errorViewResolvers) {
      ModelAndView modelAndView = resolver.resolveErrorView(request, status, model);
      if (modelAndView != null) {
         return modelAndView;
      }
   }
   return null;
}
```

### 2）、如果定制錯誤響應：

#### 	**1）、如何定制錯誤的頁面；**

​			**1）、有模板引擎的情況下；error/狀態碼;** 【將錯誤頁面命名為  錯誤狀態碼.html 放在模板引擎文件夾裡面的 error文件夾下】，發生此狀態碼的錯誤就會來到  對應的頁面；

​			我們可以使用4xx和5xx作為錯誤頁面的文件名來匹配這種類型的所有錯誤，精確優先（優先尋找精確的狀態碼.html）；

​			頁面能獲取的信息；

​				timestamp：時間戳

​				status：狀態碼

​				error：錯誤提示

​				exception：異常對象

​				message：異常消息

​				errors：JSR303數據校驗的錯誤都在這裡

​			2）、沒有模板引擎（模板引擎找不到這個錯誤頁面），靜態資源文件夾下找；

​			3）、以上都沒有錯誤頁面，就是默認來到SpringBoot默認的錯誤提示頁面；

#### 	2）、如何定制錯誤的json數據；

​		1）、自定義異常處理&返回定制json數據；

```java
@ControllerAdvice
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(UserNotExistException.class)
    public Map<String,Object> handleException(Exception e){
        Map<String,Object> map = new HashMap<>();
        map.put("code","user.notexist");
        map.put("message",e.getMessage());
        return map;
    }
}
//沒有自適應效果...
```



​		2）、轉發到/error進行自適應響應效果處理

```java
 @ExceptionHandler(UserNotExistException.class)
    public String handleException(Exception e, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        //傳入我們自己的錯誤狀態碼  4xx 5xx，否則就不會進入定制錯誤頁面的解析流程
        /**
         * Integer statusCode = (Integer) request
         .getAttribute("javax.servlet.error.status_code");
         */
        request.setAttribute("javax.servlet.error.status_code",500);
        map.put("code","user.notexist");
        map.put("message",e.getMessage());
        //轉發到/error
        return "forward:/error";
    }
```

#### 	3）、將我們的定制數據攜帶出去；

出現錯誤以後，會來到/error請求，會被BasicErrorController處理，響應出去可以獲取的數據是由getErrorAttributes得到的（是AbstractErrorController（ErrorController）規定的方法）；

​	1、完全來編寫一個ErrorController的實現類【或者是編寫AbstractErrorController的子類】，放在容器中；

​	2、頁面上能用的數據，或者是json返回能用的數據都是通過errorAttributes.getErrorAttributes得到；

​			容器中DefaultErrorAttributes.getErrorAttributes()；默認進行數據處理的；

自定義ErrorAttributes

```java
//給容器中加入我們自己定義的ErrorAttributes
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(requestAttributes, includeStackTrace);
        map.put("company","atguigu");
        return map;
    }
}
```

![](images/搜狗截图20180228135513.png)



## 8、配置嵌入式Servlet容器

SpringBoot默認使用Tomcat作為嵌入式的Servlet容器；

![](images/搜狗截图20180301142915.png)



問題？

### 1）、如何定制和修改Servlet容器的相關配置；

1、修改和server有關的配置（ServerProperties【也是EmbeddedServletContainerCustomizer】）；

```properties
server.port=8081
server.context-path=/crud

server.tomcat.uri-encoding=UTF-8

//通用的Servlet容器設置
server.xxx
//Tomcat的設置
server.tomcat.xxx
```

2、編寫一個**EmbeddedServletContainerCustomizer**：嵌入式的Servlet容器的定制器；來修改Servlet容器的配置

```java
@Bean  //一定要將這個定制器加入到容器中
public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer(){
    return new EmbeddedServletContainerCustomizer() {

        //定制嵌入式的Servlet容器相關的規則
        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.setPort(8083);
        }
    };
}
```
### 2）、註冊Servlet三大組件【Servlet、Filter、Listener】

由於SpringBoot默認是以jar包的方式啟動嵌入式的Servlet容器來啟動SpringBoot的web應用，沒有web.xml文件。

註冊三大組件用以下方式

ServletRegistrationBean

```java
//註冊三大組件
@Bean
public ServletRegistrationBean myServlet(){
    ServletRegistrationBean registrationBean = new ServletRegistrationBean(new MyServlet(),"/myServlet");
    return registrationBean;
}

```

FilterRegistrationBean

```java
@Bean
public FilterRegistrationBean myFilter(){
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new MyFilter());
    registrationBean.setUrlPatterns(Arrays.asList("/hello","/myServlet"));
    return registrationBean;
}
```

ServletListenerRegistrationBean

```java
@Bean
public ServletListenerRegistrationBean myListener(){
    ServletListenerRegistrationBean<MyListener> registrationBean = new ServletListenerRegistrationBean<>(new MyListener());
    return registrationBean;
}
```



SpringBoot幫我們自動SpringMVC的時候，自動的註冊SpringMVC的前端控制器；DIspatcherServlet；

DispatcherServletAutoConfiguration中：

```java
@Bean(name = DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME)
@ConditionalOnBean(value = DispatcherServlet.class, name = DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
public ServletRegistrationBean dispatcherServletRegistration(
      DispatcherServlet dispatcherServlet) {
   ServletRegistrationBean registration = new ServletRegistrationBean(
         dispatcherServlet, this.serverProperties.getServletMapping());
    //默認攔截： /  所有請求；包靜態資源，但是不攔截jsp請求；   /*會攔截jsp
    //可以通過server.servletPath來修改SpringMVC前端控制器默認攔截的請求路徑

   registration.setName(DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
   registration.setLoadOnStartup(
         this.webMvcProperties.getServlet().getLoadOnStartup());
   if (this.multipartConfig != null) {
      registration.setMultipartConfig(this.multipartConfig);
   }
   return registration;
}

```

2）、SpringBoot能不能支持其他的Servlet容器；

### 3）、替換為其他嵌入式Servlet容器

![](images/搜狗截圖20180302114401.png)

默認支持：

Tomcat（默認使用）

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   引入web模塊默認就是使用嵌入式的Tomcat作為Servlet容器；
</dependency>
```

Jetty

```xml
<!-- 引入web模塊 -->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   <exclusions>
      <exclusion>
         <artifactId>spring-boot-starter-tomcat</artifactId>
         <groupId>org.springframework.boot</groupId>
      </exclusion>
   </exclusions>
</dependency>

<!--引入其他的Servlet容器-->
<dependency>
   <artifactId>spring-boot-starter-jetty</artifactId>
   <groupId>org.springframework.boot</groupId>
</dependency>
```

Undertow

```xml
<!-- 引入web模塊 -->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   <exclusions>
      <exclusion>
         <artifactId>spring-boot-starter-tomcat</artifactId>
         <groupId>org.springframework.boot</groupId>
      </exclusion>
   </exclusions>
</dependency>

<!--引入其他的Servlet容器-->
<dependency>
   <artifactId>spring-boot-starter-undertow</artifactId>
   <groupId>org.springframework.boot</groupId>
</dependency>
```

### 4), Qiànrù shì Servlet róngqì zìdòng pèizhì yuánlǐ; EmbeddedServletContainerAutoConfiguration: Qiànrù shì de Servlet róngqì zìdòng pèizhì? ```Java @AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE) @Configuration @ConditionalOnWebApplication @Import(BeanPostProcessorsRegistrar.Class) //dǎorù BeanPostProcessorsRegistrar:Spring zhùjiě bǎn; gěi róngqì zhōng dǎorù yīxiē zǔjiàn //dǎorùle EmbeddedServletContainerCustomizerBeanPostProcessor: //Hòu zhì chǔlǐ qì:Bean chūshǐhuà qiánhòu (chuàngjiàn wán duìxiàng, hái méi fùzhí fùzhí) zhíxíng chūshǐhuà gōngzuò public class EmbeddedServletContainerAutoConfiguration { @Configuration @ConditionalOnClass({Servlet.Class, Tomcat.Class})//pànduàn dāngqián shìfǒu yǐnrùle Tomcat yīlài; @ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.Class, search = SearchStrategy.CURRENT)//pànduàn dāngqián róngqì méiyǒu yònghù zìjǐ dìngyì EmbeddedServletContainerFactory: Qiànrù shì de Servlet róngqì gōngchǎng; zuòyòng: Chuàngjiàn qiànrù shì de Servlet róngqì public static class EmbeddedTomcat { @Bean public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() { return new TomcatEmbeddedServletContainerFactory(); } } /** * Nested configuration if Jetty is being used. */ @Configuration @ConditionalOnClass({Servlet.Class, Server.Class, Loader.Class, WebAppContext.Class}) @ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.Class, search = SearchStrategy.CURRENT) public static class EmbeddedJetty { @Bean public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() { return new JettyEmbeddedServletContainerFactory(); } } /** * Nested configuration if Undertow is being used. */ @Configuration @ConditionalOnClass({Servlet.Class, Undertow.Class, SslClientAuthMode.Class}) @ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.Class, search = SearchStrategy.CURRENT) public static class EmbeddedUndertow { @Bean public UndertowEmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory() { return new UndertowEmbeddedServletContainerFactory(); } } ```
顯示更多
1,874 / 5,000
翻譯結果
### 4）、嵌入式Servlet容器自動配置原理；



EmbeddedServletContainerAutoConfiguration：嵌入式的Servlet容器自動配置？

```java
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnWebApplication
@Import(BeanPostProcessorsRegistrar.class)
//導入BeanPostProcessorsRegistrar：Spring註解版；給容器中導入一些組件
//導入了EmbeddedServletContainerCustomizerBeanPostProcessor：
//後置處理器：bean初始化前後（創建完對象，還沒賦值賦值）執行初始化工作
public class EmbeddedServletContainerAutoConfiguration {

    @Configuration
	@ConditionalOnClass({ Servlet.class, Tomcat.class })//判斷當前是否引入了Tomcat依賴；
	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)//判斷當前容器沒有用戶自己定義EmbeddedServletContainerFactory：嵌入式的Servlet容器工廠；作用：創建嵌入式的Servlet容器
	public static class EmbeddedTomcat {

		@Bean
		public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
			return new TomcatEmbeddedServletContainerFactory();
		}

	}

    /**
	 * Nested configuration if Jetty is being used.
	 */
	@Configuration
	@ConditionalOnClass({ Servlet.class, Server.class, Loader.class,
			WebAppContext.class })
	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedJetty {

		@Bean
		public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() {
			return new JettyEmbeddedServletContainerFactory();
		}

	}

	/**
	 * Nested configuration if Undertow is being used.
	 */
	@Configuration
	@ConditionalOnClass({ Servlet.class, Undertow.class, SslClientAuthMode.class })
	@ConditionalOnMissingBean(value = EmbeddedServletContainerFactory.class, search = SearchStrategy.CURRENT)
	public static class EmbeddedUndertow {

		@Bean
		public UndertowEmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory() {
			return new UndertowEmbeddedServletContainerFactory();
		}

	}
```

1）、EmbeddedServletContainerFactory（嵌入式Servlet容器工廠）

```java
public interface EmbeddedServletContainerFactory {

   //獲取嵌入式的Servlet容器
   EmbeddedServletContainer getEmbeddedServletContainer(
         ServletContextInitializer... initializers);

}
```

![](images/搜狗截图20180302144835.png)

2）、EmbeddedServletContainer：（嵌入式的Servlet容器）

![](images/搜狗截图20180302144910.png)



3）、以**TomcatEmbeddedServletContainerFactory**為例

```java
@Override
public EmbeddedServletContainer getEmbeddedServletContainer(
      ServletContextInitializer... initializers) {
    //創建一個Tomcat
   Tomcat tomcat = new Tomcat();

    //配置Tomcat的基本環節
   File baseDir = (this.baseDirectory != null ? this.baseDirectory
         : createTempDir("tomcat"));
   tomcat.setBaseDir(baseDir.getAbsolutePath());
   Connector connector = new Connector(this.protocol);
   tomcat.getService().addConnector(connector);
   customizeConnector(connector);
   tomcat.setConnector(connector);
   tomcat.getHost().setAutoDeploy(false);
   configureEngine(tomcat.getEngine());
   for (Connector additionalConnector : this.additionalTomcatConnectors) {
      tomcat.getService().addConnector(additionalConnector);
   }
   prepareContext(tomcat.getHost(), initializers);

    //將配置好的Tomcat傳入進去，返回一個EmbeddedServletContainer；並且啟動Tomcat服務器
   return getTomcatEmbeddedServletContainer(tomcat);
}
```

4）、我們對嵌入式容器的配置修改是怎么生效？

```
ServerProperties、EmbeddedServletContainerCustomizer
```



**EmbeddedServletContainerCustomizer**：定制器幫我們修改了Servlet容器的配置？

怎麼修改的原理？

5）、容器中導入了**EmbeddedServletContainerCustomizerBeanPostProcessor**

```java
//初始化之前
@Override
public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    //如果當前初始化的是一個ConfigurableEmbeddedServletContainer類型的組件
   if (bean instanceof ConfigurableEmbeddedServletContainer) {
       //
      postProcessBeforeInitialization((ConfigurableEmbeddedServletContainer) bean);
   }
   return bean;
}

private void postProcessBeforeInitialization(
			ConfigurableEmbeddedServletContainer bean) {
    //獲取所有的定制器，調用每一個定制器的customize方法來給Servlet容器進行屬性賦值；
    for (EmbeddedServletContainerCustomizer customizer : getCustomizers()) {
        customizer.customize(bean);
    }
}

private Collection<EmbeddedServletContainerCustomizer> getCustomizers() {
    if (this.customizers == null) {
        // Look up does not include the parent context
        this.customizers = new ArrayList<EmbeddedServletContainerCustomizer>(
            this.beanFactory
            //從容器中獲取所有這葛類型的組件：EmbeddedServletContainerCustomizer
            //定制Servlet容器，給容器中可以添加一個EmbeddedServletContainerCustomizer類型的組件
            .getBeansOfType(EmbeddedServletContainerCustomizer.class,
                            false, false)
            .values());
        Collections.sort(this.customizers, AnnotationAwareOrderComparator.INSTANCE);
        this.customizers = Collections.unmodifiableList(this.customizers);
    }
    return this.customizers;
}

ServerProperties也是定制器
```

步驟：

1）、SpringBoot根據導入的依賴情況，給容器中添加相應的EmbeddedServletContainerFactory【TomcatEmbeddedServletContainerFactory】

2）、容器中某個組件要創建對象就會驚動後置處理器；EmbeddedServletContainerCustomizerBeanPostProcessor；

只要是嵌入式的Servlet容器工廠，後置處理器就工作；

3）、後置處理器，從容器中獲取所有的**EmbeddedServletContainerCustomizer**，調用定制器的定制方法



###5）、嵌入式Servlet容器啟動原理；

什麼時候創建嵌入式的Servlet容器工廠？什麼時候獲取嵌入式的Servlet容器並啟動Tomcat；

獲取嵌入式的Servlet容器工廠：

1）、SpringBoot應用啟動運行run方法

2）、refreshContext(context);SpringBoot刷新IOC容器【創建IOC容器對象，並初始化容器，創建容器中的每一個組件】；如果是web應用創建**AnnotationConfigEmbeddedWebApplicationContext**，否則：**AnnotationConfigApplicationContext**

3）、refresh(context);**刷新剛才創建好的ioc容器；**

```java
public void refresh() throws BeansException, IllegalStateException {
   synchronized (this.startupShutdownMonitor) {
      // Prepare this context for refreshing.
      prepareRefresh();

      // Tell the subclass to refresh the internal bean factory.
      ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

      // Prepare the bean factory for use in this context.
      prepareBeanFactory(beanFactory);

      try {
         // Allows post-processing of the bean factory in context subclasses.
         postProcessBeanFactory(beanFactory);

         // Invoke factory processors registered as beans in the context.
         invokeBeanFactoryPostProcessors(beanFactory);

         // Register bean processors that intercept bean creation.
         registerBeanPostProcessors(beanFactory);

         // Initialize message source for this context.
         initMessageSource();

         // Initialize event multicaster for this context.
         initApplicationEventMulticaster();

         // Initialize other special beans in specific context subclasses.
         onRefresh();

         // Check for listener beans and register them.
         registerListeners();

         // Instantiate all remaining (non-lazy-init) singletons.
         finishBeanFactoryInitialization(beanFactory);

         // Last step: publish corresponding event.
         finishRefresh();
      }

      catch (BeansException ex) {
         if (logger.isWarnEnabled()) {
            logger.warn("Exception encountered during context initialization - " +
                  "cancelling refresh attempt: " + ex);
         }

         // Destroy already created singletons to avoid dangling resources.
         destroyBeans();

         // Reset 'active' flag.
         cancelRefresh(ex);

         // Propagate exception to caller.
         throw ex;
      }

      finally {
         // Reset common introspection caches in Spring's core, since we
         // might not ever need metadata for singleton beans anymore...
         resetCommonCaches();
      }
   }
}
```

4）、  onRefresh(); web的ioc容器重寫了onRefresh方法

5）、webioc容器會創建嵌入式的Servlet容器；**createEmbeddedServletContainer**();

**6）、獲取嵌入式的Servlet容器工廠：**

EmbeddedServletContainerFactory containerFactory = getEmbeddedServletContainerFactory();

​	從ioc容器中獲取EmbeddedServletContainerFactory 組件；**TomcatEmbeddedServletContainerFactory**創建對象，後置處理器一看是這個對象，就獲取所有的定制器來先定制Servlet容器的相關配置；

7）、**使用容器工廠獲取嵌入式的Servlet容器**：this.embeddedServletContainer = containerFactory      .getEmbeddedServletContainer(getSelfInitializer());

8）、嵌入式的Servlet容器創建對象並啟動Servlet容器；

**先啟動嵌入式的Servlet容器，再將ioc容器中剩下沒有創建出的對象獲取出來；**

**==IOC容器啟動創建嵌入式的Servlet容器==**

## 9、使用外置的Servlet容器

嵌入式Servlet容器：應用打成可執行的jar

​		優點：簡單、便攜；

​		缺點：默認不支持JSP、優化定制比較複雜（使用定制器【ServerProperties、自定義EmbeddedServletContainerCustomizer】，自己編寫嵌入式Servlet容器的創建工廠【EmbeddedServletContainerFactory】）；



外置的Servlet容器：外面安裝Tomcat---應用war包的方式打包；

### 步驟

1）、必須創建一個war項目；（利用idea創建好目錄結構）

2）、將嵌入式的Tomcat指定為provided；

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-tomcat</artifactId>
   <scope>provided</scope>
</dependency>
```

3）、必須編寫一個**SpringBootServletInitializer**的子類，並調用configure方法

```java
public class ServletInitializer extends SpringBootServletInitializer {

   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
       //傳入SpringBoot應用的主程序
      return application.sources(SpringBoot04WebJspApplication.class);
   }

}
```

4）、啟動服務器就可以使用；

### 原理

jar包：執行SpringBoot主類的main方法，啟動ioc容器，創建嵌入式的Servlet容器；

war包：啟動服務器，**服務器啟動SpringBoot應用**【SpringBootServletInitializer】，啟動ioc容器；



servlet3.0（Spring註解版）：

8.2.4 Shared libraries / runtimes pluggability：

規則：

​	1）、服務器啟動（web應用啟動）會創建當前web應用裡面每一個jar包裡面ServletContainerInitializer實例：

​	2）、ServletContainerInitializer的實現放在jar包的META-INF/services文件夾下，有一個名為javax.servlet.ServletContainerInitializer的文件，內容就是ServletContainerInitializer的實現類的全類名

​	3）、還可以使用@HandlesTypes，在應用啟動的時候加載我們感興趣的類；



流程：

1）、啟動Tomcat

2）、org\springframework\spring-web\4.3.14.RELEASE\spring-web-4.3.14.RELEASE.jar!\META-INF\services\javax.servlet.ServletContainerInitializer：

Spring的web模塊裡面有這個文件：**org.springframework.web.SpringServletContainerInitializer**

3）、SpringServletContainerInitializer將@HandlesTypes(WebApplicationInitializer.class)標註的所有這個類型的類都傳入到onStartup方法的Set<Class<?>>；為這些WebApplicationInitializer類型的類創建實例；

4）、每一個WebApplicationInitializer都調用自己的onStartup；

![](images/搜狗截圖20180302221835.png)

5）、相當於我們的SpringBootServletInitializer的類會被創建對象，並執行onStartup方法

6）、SpringBootServletInitializer實例執行onStartup的時候會createRootApplicationContext；創建容器

```java
protected WebApplicationContext createRootApplicationContext(
      ServletContext servletContext) {
    //1、創建SpringApplicationBuilder
   SpringApplicationBuilder builder = createSpringApplicationBuilder();
   StandardServletEnvironment environment = new StandardServletEnvironment();
   environment.initPropertySources(servletContext, null);
   builder.environment(environment);
   builder.main(getClass());
   ApplicationContext parent = getExistingRootWebApplicationContext(servletContext);
   if (parent != null) {
      this.logger.info("Root context already created (using as parent).");
      servletContext.setAttribute(
            WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, null);
      builder.initializers(new ParentContextApplicationContextInitializer(parent));
   }
   builder.initializers(
         new ServletContextApplicationContextInitializer(servletContext));
   builder.contextClass(AnnotationConfigEmbeddedWebApplicationContext.class);

    //調用configure方法，子類重寫了這個方法，將SpringBoot的主程序類傳入了進來
   builder = configure(builder);

    //使用builder創建一個Spring應用
   SpringApplication application = builder.build();
   if (application.getSources().isEmpty() && AnnotationUtils
         .findAnnotation(getClass(), Configuration.class) != null) {
      application.getSources().add(getClass());
   }
   Assert.state(!application.getSources().isEmpty(),
         "No SpringApplication sources have been defined. Either override the "
               + "configure method or add an @Configuration annotation");
   // Ensure error pages are registered
   if (this.registerErrorPageFilter) {
      application.getSources().add(ErrorPageFilterConfiguration.class);
   }
    //啟動Spring應用
   return run(application);
}
```

7）、Spring的應用就啟動並且創建IOC容器

```java
public ConfigurableApplicationContext run(String... args) {
   StopWatch stopWatch = new StopWatch();
   stopWatch.start();
   ConfigurableApplicationContext context = null;
   FailureAnalyzers analyzers = null;
   configureHeadlessProperty();
   SpringApplicationRunListeners listeners = getRunListeners(args);
   listeners.starting();
   try {
      ApplicationArguments applicationArguments = new DefaultApplicationArguments(
            args);
      ConfigurableEnvironment environment = prepareEnvironment(listeners,
            applicationArguments);
      Banner printedBanner = printBanner(environment);
      context = createApplicationContext();
      analyzers = new FailureAnalyzers(context);
      prepareContext(context, environment, listeners, applicationArguments,
            printedBanner);

       //刷新IOC容器
      refreshContext(context);
      afterRefresh(context, applicationArguments);
      listeners.finished(context, null);
      stopWatch.stop();
      if (this.logStartupInfo) {
         new StartupInfoLogger(this.mainApplicationClass)
               .logStarted(getApplicationLog(), stopWatch);
      }
      return context;
   }
   catch (Throwable ex) {
      handleRunFailure(context, listeners, analyzers, ex);
      throw new IllegalStateException(ex);
   }
}
```

**==啟動Servlet容器，再啟動SpringBoot應用==**


# 五、Docker

## 1、簡介

**Docker**是一個開源的應用容器引擎；是一個輕量級容器技術；

Docker支持將軟件編譯成一個鏡像；然後在鏡像中各種軟件做好配置，將鏡像發佈出去，其他使用者可以直接使用這個鏡像；

運行中的這個鏡像稱為容器，容器啟動是非常快速的。

![](images/搜狗截圖20180303145450.png)



![](images/搜狗截圖20180303145531.png)

## 2、核心概念

docker主機(Host)：安裝了Docker程序的機器（Docker直接安裝在操作系統之上）；

docker客戶端(Client)：連接docker主機進行操作；

docker倉庫(Registry)：用來保存各種打包好的軟件鏡像；

docker鏡像(Images)：軟件打包好的鏡像；放在docker倉庫中；

docker容器(Container)：鏡像啟動後的實例稱為一個容器；容器是獨立運行的一個或一組應用

![](images/搜狗截圖20180303165113.png)

使用Docker的步驟：

1）、安裝Docker

2）、去Docker倉庫找到這個軟件對應的鏡像；

3）、使用Docker運行這個鏡像，這個鏡像就會生成一個Docker容器；

4）、對容器的啟動停止就是對軟件的啟動停止；

## 3、安裝Docker

#### 1）、安裝linux虛擬機

​	1）、VMWare、VirtualBox（安裝）；

​	2）、導入虛擬機文件centos7-atguigu.ova；

​	3）、雙擊啟動linux虛擬機;使用  root/ 123456登陸

​	4）、使用客戶端連接linux服務器進行命令操作；

​	5）、設置虛擬機網絡；

​		橋接網絡===選好網卡====接入網線；

​	6）、設置好網絡以後使用命令重啟虛擬機的網絡

```shell
service network restart
```

​	7）、查看linux的ip地址

```shell
ip addr
```

​	8）、使用客戶端連接linux；

#### 2）、在linux虛擬機上安裝docker

步驟：

```shell
1、檢查內核版本，必須是3.10及以上
uname -r
2、安裝docker
yum install docker
3、輸入y確認安裝
4、啟動docker
[root@localhost ~]# systemctl start docker
[root@localhost ~]# docker -v
Docker version 1.12.6, build 3e8e77d/1.12.6
5、開機啟動docker
[root@localhost ~]# systemctl enable docker
Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.
6、停止docker
systemctl stop docker
```

## 4、Docker常用命令&操作

### 1）、鏡像操作

| 操作 | 命令                                            | 說明                                                     |
| ---- | ----------------------------------------------- | -------------------------------------------------------- |
| 檢索 | docker  search 關鍵字  eg：docker  search redis | 我們經常去docker  hub上檢索鏡像的詳細信息，如鏡像的TAG。 |
| 拉取 | docker pull 鏡像名:tag                          | :tag是可選的，tag表示標籤，多為軟件的版本，默認是latest  |
| 列表 | docker images                                   | 查看所有本地鏡像                                         |
| 刪除 | docker rmi image-id                             | 刪除指定的本地鏡像                                       |

https://hub.docker.com/

### 2）、容器操作

軟件鏡像（QQ安裝程序）----運行鏡像----產生一個容器（正在運行的軟件，運行的QQ）；

步驟：

````shell
1、搜索鏡像
[root@localhost ~]# docker search tomcat
2、拉取鏡像
[root@localhost ~]# docker pull tomcat
3、根據鏡像啟動容器
docker run --name mytomcat -d tomcat:latest
4、docker ps
查看運行中的容器
5、 停止運行中的容器
docker stop  容器的id
6、查看所有的容器
docker ps -a
7、啟動容器
docker start 容器id
8、刪除一個容器
 docker rm 容器id
9、啟動一個做了端口映射的tomcat
[root@localhost ~]# docker run -d -p 8888:8080 tomcat
-d：後台運行
-p: 將主機的端口映射到容器的一個端口    主機端口:容器內部的端口

10、為了演示簡單關閉了linux的防火牆
service firewalld status ；查看防火牆狀態
service firewalld stop：關閉防火牆
11、查看容器的日誌
docker logs container-name/container-id

更多命令參看
https://docs.docker.com/engine/reference/commandline/docker/
可以參考每一個鏡像的文檔

````



### 3）、安裝MySQL示例

```shell
docker pull mysql
```



錯誤的啟動

```shell
[root@localhost ~]# docker run --name mysql01 -d mysql
42f09819908bb72dd99ae19e792e0a5d03c48638421fa64cce5f8ba0f40f5846

mysql退出了
[root@localhost ~]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                           PORTS               NAMES
42f09819908b        mysql               "docker-entrypoint.sh"   34 seconds ago      Exited (1) 33 seconds ago                            mysql01
538bde63e500        tomcat              "catalina.sh run"        About an hour ago   Exited (143) About an hour ago                       compassionate_
goldstine
c4f1ac60b3fc        tomcat              "catalina.sh run"        About an hour ago   Exited (143) About an hour ago                       lonely_fermi
81ec743a5271        tomcat              "catalina.sh run"        About an hour ago   Exited (143) About an hour ago                       sick_ramanujan


//錯誤日誌
[root@localhost ~]# docker logs 42f09819908b
error: database is uninitialized and password option is not specified
  You need to specify one of MYSQL_ROOT_PASSWORD, MYSQL_ALLOW_EMPTY_PASSWORD and MYSQL_RANDOM_ROOT_PASSWORD；這個三個參數必須指定一個
```

正確的啟動

```shell
[root@localhost ~]# docker run --name mysql01 -e MYSQL_ROOT_PASSWORD=123456 -d mysql
b874c56bec49fb43024b3805ab51e9097da779f2f572c22c695305dedd684c5f
[root@localhost ~]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
b874c56bec49        mysql               "docker-entrypoint.sh"   4 seconds ago       Up 3 seconds        3306/tcp            mysql01
```

做了端口映射

```shell
[root@localhost ~]# docker run -p 3306:3306 --name mysql02 -e MYSQL_ROOT_PASSWORD=123456 -d mysql
ad10e4bc5c6a0f61cbad43898de71d366117d120e39db651844c0e73863b9434
[root@localhost ~]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
ad10e4bc5c6a        mysql               "docker-entrypoint.sh"   4 seconds ago       Up 2 seconds        0.0.0.0:3306->3306/tcp   mysql02
```


幾個其他的高級操作

```
docker run --name mysql03 -v /conf/mysql:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:tag
把主機的/conf/mysql文件夾掛載到 mysqldocker容器的/etc/mysql/conf.d文件夾裡面
改mysql的配置文件就只需要把mysql配置文件放在自定義的文件夾下（/conf/mysql）

docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:tag --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
指定mysql的一些配置參數
```


# Liù,SpringBoot yǔ shùjù fǎngwèn ## 1,JDBC ```xml <dependency> <groupId>org.Springframework.Boot</groupId> <artifactId>spring-boot-starter-jdbc</artifactId> </dependency> <dependency> <groupId>mysql</groupId> <artifactId>mysql-connector-java</artifactId> <scope>runtime</scope> </dependency> ``` ```yaml spring: Datasource: Username: Root password: 123456 Url: Jdbc:Mysql://192.168.15.22:3306/Jdbc driver-class-name: Com.Mysql.Jdbc.Driver ``` xiàoguǒ:​ Mòrèn shì yòng org.Apache.Tomcat.Jdbc.Pool.DataSource zuòwéi shùjù yuán;​ shùjù yuán de xiāngguān pèizhì dōu zài DataSourceProperties lǐmiàn; zìdòng pèizhì yuánlǐ: Org.Springframework.Boot.Autoconfigure.Jdbc: 1, Cānkǎo DataSourceConfiguration, gēnjù pèizhì chuàngjiàn shùjù yuán, mòrèn shǐyòng Tomcat liánjiē chí; kěyǐ shǐyòng spring.Datasource.Type zhǐdìng zì dìngyì de shùjù yuán lèixíng; 2,SpringBoot mòrèn kěyǐ zhīchí; ``` org.Apache.Tomcat.Jdbc.Pool.DataSource,HikariDataSource,BasicDataSource, ``` 3, zì dìngyì shùjù yuán lèixíng ```java /** * Generic DataSource configuration. */ @ConditionalOnMissingBean(DataSource.Class) @ConditionalOnProperty(name ="spring.Datasource.Type") static class Generic { @Bean public DataSource dataSource(DataSourceProperties properties) { //shǐyòng DataSourceBuilder chuàngjiàn shùjù yuán, lìyòng fǎnshè chuàngjiàn xiǎngyìng type de shùjù yuán, bìngqiě bǎng dìng xiāngguān shǔxìng return properties.InitializeDataSourceBuilder().Build(); } } ``` 4,**DataSourceInitializer:ApplicationListener**;​ zuòyòng:​ 1),RunSchemaScripts(); yùnxíng jiàn biǎo yǔjù;​ 2),runDataScripts(); yùnxíng chārù shùjù de sql yǔjù; mòrèn zhǐ xūyào jiāng wénjiàn mìngmíng wèi: ```Properties schema-*.Sql,data-*.Sql mòrèn guīzé:Schema.Sql,schema-all.Sql; kěyǐ shǐyòng schema: - Classpath:Department.Sql zhǐdìng wèizhì ``` 5, cāozuò shùjùkù: Zìdòng pèizhìle JdbcTemplate cāozuò shùjùkù
顯示更多
1,527 / 5,000
翻譯結果
# 六、SpringBoot與數據訪問

## 1、JDBC

```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
```



```yaml
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://192.168.15.22:3306/jdbc
    driver-class-name: com.mysql.jdbc.Driver
```

效果：

​	默認是用org.apache.tomcat.jdbc.pool.DataSource作為數據源；

​	數據源的相關配置都在DataSourceProperties裡面；

自動配置原理：

org.springframework.boot.autoconfigure.jdbc：

1、參考DataSourceConfiguration，根據配置創建數據源，默認使用Tomcat連接池；可以使用spring.datasource.type指定自定義的數據源類型；

2、SpringBoot默認可以支持；

```
org.apache.tomcat.jdbc.pool.DataSource、HikariDataSource、BasicDataSource、
```

3、自定義數據源類型

```java
/**
 * Generic DataSource configuration.
 */
@ConditionalOnMissingBean(DataSource.class)
@ConditionalOnProperty(name = "spring.datasource.type")
static class Generic {

   @Bean
   public DataSource dataSource(DataSourceProperties properties) {
       //使用DataSourceBuilder創建數據源，利用反射創建響應type的數據源，並且綁定相關屬性
      return properties.initializeDataSourceBuilder().build();
   }

}
```

4、**DataSourceInitializer：ApplicationListener**；

​	作用：

​		1）、runSchemaScripts();運行建表語句；

​		2）、runDataScripts();運行插入數據的sql語句；

默認只需要將文件命名為：

```properties
schema-*.sql、data-*.sql
默認規則：schema.sql，schema-all.sql；
可以使用
	schema:
      - classpath:department.sql
      指定位置
```

5、操作數據庫：自動配置了JdbcTemplate操作數據庫

## 2, Zhěnghé Druid shùjù yuán ```java dǎorù druid shùjù yuán @Configuration public class DruidConfig { @ConfigurationProperties(prefix ="spring.Datasource") @Bean public DataSource druid(){ return new DruidDataSource(); } //pèizhì Druid de jiānkòng //1, pèizhì yīgè guǎnlǐ hòutái de Servlet @Bean public ServletRegistrationBean statViewServlet(){ ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*"); Map<String,String> initParams = new HashMap<>(); initParams.Put("loginUsername","admin"); initParams.Put("loginPassword","123456"); initParams.Put("allow","");//mòrèn jiùshì yǔnxǔ suǒyǒu fǎngwèn initParams.Put("deny","192.168.15.21"); Bean.SetInitParameters(initParams); return bean; } //2, pèizhì yīgè web jiānkòng de filter @Bean public FilterRegistrationBean webStatFilter(){ FilterRegistrationBean bean = new FilterRegistrationBean(); bean.SetFilter(new WebStatFilter()); Map<String,String> initParams = new HashMap<>(); initParams.Put("exclusions","*.Js,*.Css,/druid/*"); bean.SetInitParameters(initParams); bean.SetUrlPatterns(Arrays.AsList("/*")); return bean; } } ```
顯示更多
1,209 / 5,000
翻譯結果
## 2、整合Druid數據源

```java
導入druid數據源
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
       return  new DruidDataSource();
    }

    //配置Druid的監控
    //1、配置一個管理後台的Servlet
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();

        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","123456");
        initParams.put("allow","");//默認就是允許所有訪問
        initParams.put("deny","192.168.15.21");

        bean.setInitParameters(initParams);
        return bean;
    }


    //2、配置一個web監控的filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        Map<String,String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.css,/druid/*");

        bean.setInitParameters(initParams);

        bean.setUrlPatterns(Arrays.asList("/*"));

        return  bean;
    }
}

```

## 3、整合MyBatis

```xml
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>1.3.1</version>
		</dependency>
```

![](images/搜狗截圖20180305194443.png)

步驟：

​	1）、配置數據源相關屬性（見上一節Druid）

​	2）、給數據庫建表

​	3）、創建JavaBean

### 	4）、註解版

```java
//指定這是一個操作數據庫的mapper
@Mapper
public interface DepartmentMapper {

    @Select("select * from department where id=#{id}")
    public Department getDeptById(Integer id);

    @Delete("delete from department where id=#{id}")
    public int deleteDeptById(Integer id);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into department(departmentName) values(#{departmentName})")
    public int insertDept(Department department);

    @Update("update department set departmentName=#{departmentName} where id=#{id}")
    public int updateDept(Department department);
}
```

問題：

自定義MyBatis的配置規則；給容器中添加一個ConfigurationCustomizer；

```java
@org.springframework.context.annotation.Configuration
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return new ConfigurationCustomizer(){

            @Override
            public void customize(Configuration configuration) {
                configuration.setMapUnderscoreToCamelCase(true);
            }
        };
    }
}
```



```java
使用MapperScan批量掃描所有的Mapper接口；
@MapperScan(value = "com.atguigu.springboot.mapper")
@SpringBootApplication
public class SpringBoot06DataMybatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot06DataMybatisApplication.class, args);
	}
}
```

### 5）、配置文件版

```yaml
mybatis:
  config-location: classpath:mybatis/mybatis-config.xml 指定全局配置文件的位置
  mapper-locations: classpath:mybatis/mapper/*.xml  指定sql映射文件的位置
```

更多使用參照

http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/



## 4、整合SpringData JPA

### 1）、SpringData簡介

![](images/搜狗截圖20180306105412.png)

### 2）、整合SpringData JPA

JPA:ORM（Object Relational Mapping）；

1）、編寫一個實體類（bean）和數據表進行映射，並且配置好映射關係；

```java
//使用JPA註解配置映射關係
@Entity //告訴JPA這是一個實體類（和數據表映射的類）
@Table(name = "tbl_user") //@Table來指定和哪個數據表對應;如果省略默認表名就是user；
public class User {

    @Id //這是一個主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主鍵
    private Integer id;

    @Column(name = "last_name",length = 50) //這是和數據表對應的一個列
    private String lastName;
    @Column //省略默認列名就是屬性名
    private String email;
```

2）、編寫一個Dao接口來操作實體類對應的數據表（Repository）

```java
//繼承JpaRepository來完成對數據庫的操作
public interface UserRepository extends JpaRepository<User,Integer> {
}

```

3）、基本的配置JpaProperties

```yaml
spring:
 jpa:
    hibernate:
#     更新或者創建數據表結構
      ddl-auto: update
#    控制台顯示SQL
    show-sql: true
```


# Qī, qǐdòng pèizhì yuánlǐ jǐ gè zhòngyào de shìjiàn huítiáo jīzhì pèizhì zài META-INF/spring.Factories **ApplicationContextInitializer** **SpringApplicationRunListener** zhǐ xūyào fàng zài ioc róngqì zhōng **ApplicationRunner** **CommandLineRunner** qǐdòng liúchéng: ## **1, Chuàngjiàn SpringApplication duìxiàng** ```java initialize(sources); private void initialize(Object[] sources) { //bǎocún zhǔ pèizhì lèi if (sources!= Null&& sources.Length > 0) { this.Sources.AddAll(Arrays.AsList(sources)); } //pànduàn dāngqián shìfǒu yīgè web yìngyòng this.WebEnvironment = deduceWebEnvironment(); //cóng lèi lùjìng xià zhǎodào META-INF/spring.Factories pèizhì de suǒyǒu ApplicationContextInitializer; ránhòu bǎocún qǐlái setInitializers((Collection) getSpringFactoriesInstances( ApplicationContextInitializer.Class)); //cóng lèi lùjìng xià zhǎodào ETA-INF/spring.Factories pèizhì de suǒyǒu ApplicationListener setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.Class)); //cóng duō gè pèizhì lèi zhōng zhǎodào yǒu main fāngfǎ de zhǔ pèizhì lèi this.MainApplicationClass = deduceMainApplicationClass(); } ``` ![](Images/sōugǒu jiétú 20180306145727.Png) ![](Images/sōugǒu jiétú 20180306145855.Png) ## 2, yùnxíng run fāngfǎ ```java public ConfigurableApplicationContext run(String... Args) { StopWatch stopWatch = new StopWatch(); stopWatch.Start(); ConfigurableApplicationContext context = null; FailureAnalyzers analyzers = null; configureHeadlessProperty(); //huòqǔ SpringApplicationRunListeners; cóng lèi lùjìng xià META-INF/spring.Factories SpringApplicationRunListeners listeners = getRunListeners(args); //huítiáo suǒyǒu de huòqǔ SpringApplicationRunListener.Starting() fāngfǎ listeners.Starting(); try { //fēngzhuāng mìnglìng xíng cānshù ApplicationArguments applicationArguments = new DefaultApplicationArguments( args); //zhǔnbèi huánjìng ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments); //chuàngjiàn huánjìng wánchéng hòu huítiáo SpringApplicationRunListener.EnvironmentPrepared(); biǎoshì huánjìng zhǔnbèi wánchéng Banner printedBanner = printBanner(environment); //chuàngjiàn ApplicationContext; juédìng chuàngjiàn web de ioc háishì pǔtōng de ioc context = createApplicationContext(); analyzers = new FailureAnalyzers(context); //zhǔnbèi shàngxiàwén huánjìng; jiāng environment bǎocún dào ioc zhōng; érqiě applyInitializers(); //applyInitializers(): Huítiáo zhīqián bǎocún de suǒyǒu de ApplicationContextInitializer de initialize fāngfǎ //huítiáo suǒyǒu de SpringApplicationRunListener de contextPrepared(); // prepareContext(context, environment, listeners, applicationArguments, printedBanner); //prepareContext yùnxíng wánchéng yǐhòu huítiáo suǒyǒu de SpringApplicationRunListener de contextLoaded(); //s shuāxīn róngqì;ioc róngqì chūshǐhuà (rúguǒ shì web yìngyòng hái huì chuàngjiàn qiànrù shì de Tomcat);Spring zhùjiě bǎn //sǎomiáo, chuàngjiàn, jiāzài suǒyǒu zǔjiàn dì dìfāng;(pèizhì lèi, zǔjiàn, zìdòng pèizhì) refreshContext(context); //cóng ioc róngqì zhōng huòqǔ suǒyǒu de ApplicationRunner hé CommandLineRunner jìnxíng huítiáo //ApplicationRunner xiān huítiáo,CommandLineRunner zài huítiáo afterRefresh(context, applicationArguments); //suǒyǒu de SpringApplicationRunListener huítiáo finished fāngfǎ listeners.Finished(context, null); stopWatch.Stop(); if (this.LogStartupInfo) { new StartupInfoLogger(this.MainApplicationClass) .LogStarted(getApplicationLog(), stopWatch); } //zhěnggè SpringBoot yìngyòng qǐdòng wánchéng yǐhòu fǎnhuí qǐdòng de ioc róngqì; return context; } catch (Throwable ex) { handleRunFailure(context, listeners, analyzers, ex); throw new IllegalStateException(ex); } } ```
顯示更多
3,184 / 5,000
翻譯結果
# 七、啟動配置原理

幾個重要的事件回調機制

配置在META-INF/spring.factories

**ApplicationContextInitializer**

**SpringApplicationRunListener**



只需要放在ioc容器中

**ApplicationRunner**

**CommandLineRunner**



啟動流程：

## **1、創建SpringApplication對象**

```java
initialize(sources);
private void initialize(Object[] sources) {
    //保存主配置類
    if (sources != null && sources.length > 0) {
        this.sources.addAll(Arrays.asList(sources));
    }
    //判斷當前是否一個web應用
    this.webEnvironment = deduceWebEnvironment();
    //從類路徑下找到META-INF/spring.factories配置的所有ApplicationContextInitializer；然後保存起來
    setInitializers((Collection) getSpringFactoriesInstances(
        ApplicationContextInitializer.class));
    //從類路徑下找到ETA-INF/spring.factories配置的所有ApplicationListener
    setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
    //從多個配置類中找到有main方法的主配置類
    this.mainApplicationClass = deduceMainApplicationClass();
}
```

![](images/搜狗截圖20180306145727.png)

![](images/搜狗截圖20180306145855.png)

## 2、運行run方法

```java
public ConfigurableApplicationContext run(String... args) {
   StopWatch stopWatch = new StopWatch();
   stopWatch.start();
   ConfigurableApplicationContext context = null;
   FailureAnalyzers analyzers = null;
   configureHeadlessProperty();

   //獲取SpringApplicationRunListeners；從類路徑下META-INF/spring.factories
   SpringApplicationRunListeners listeners = getRunListeners(args);
    //回調所有的獲取SpringApplicationRunListener.starting()方法
   listeners.starting();
   try {
       //封裝命令行參數
      ApplicationArguments applicationArguments = new DefaultApplicationArguments(
            args);
      //準備環境
      ConfigurableEnvironment environment = prepareEnvironment(listeners,
            applicationArguments);
       		//創建環境完成後回調SpringApplicationRunListener.environmentPrepared()；表示環境準備完成

      Banner printedBanner = printBanner(environment);

       //創建ApplicationContext；決定創建web的ioc還是普通的ioc
      context = createApplicationContext();

      analyzers = new FailureAnalyzers(context);
       //準備上下文環境;將environment保存到ioc中；而且applyInitializers()；
       //applyInitializers()：回調之前保存的所有的ApplicationContextInitializer的initialize方法
       //回調所有的SpringApplicationRunListener的contextPrepared()；
       //
      prepareContext(context, environment, listeners, applicationArguments,
            printedBanner);
       //prepareContext運行完成以後回調所有的SpringApplicationRunListener的contextLoaded（）；

       //s刷新容器；ioc容器初始化（如果是web應用還會創建嵌入式的Tomcat）；Spring註解版
       //掃描，創建，加載所有組件的地方；（配置類，組件，自動配置）
      refreshContext(context);
       //從ioc容器中獲取所有的ApplicationRunner和CommandLineRunner進行回調
       //ApplicationRunner先回調，CommandLineRunner再回調
      afterRefresh(context, applicationArguments);
       //所有的SpringApplicationRunListener回調finished方法
      listeners.finished(context, null);
      stopWatch.stop();
      if (this.logStartupInfo) {
         new StartupInfoLogger(this.mainApplicationClass)
               .logStarted(getApplicationLog(), stopWatch);
      }
       //整個SpringBoot應用啟動完成以後返回啟動的ioc容器；
      return context;
   }
   catch (Throwable ex) {
      handleRunFailure(context, listeners, analyzers, ex);
      throw new IllegalStateException(ex);
   }
}
```


## 3、事件監聽機制

配置在META-INF/spring.factories

**ApplicationContextInitializer**

```java
public class HelloApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("ApplicationContextInitializer...initialize..."+applicationContext);
    }
}

```

**SpringApplicationRunListener**

```java
public class HelloSpringApplicationRunListener implements SpringApplicationRunListener {

    //必須有的構造器
    public HelloSpringApplicationRunListener(SpringApplication application, String[] args){

    }

    @Override
    public void starting() {
        System.out.println("SpringApplicationRunListener...starting...");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        Object o = environment.getSystemProperties().get("os.name");
        System.out.println("SpringApplicationRunListener...environmentPrepared.."+o);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...contextPrepared...");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("SpringApplicationRunListener...contextLoaded...");
    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("SpringApplicationRunListener...finished...");
    }
}

```

配置（META-INF/spring.factories）

```properties
org.springframework.context.ApplicationContextInitializer=\
com.atguigu.springboot.listener.HelloApplicationContextInitializer

org.springframework.boot.SpringApplicationRunListener=\
com.atguigu.springboot.listener.HelloSpringApplicationRunListener
```





只需要放在ioc容器中

**ApplicationRunner**

```java
@Component
public class HelloApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationRunner...run....");
    }
}
```



**CommandLineRunner**

```java
@Component
public class HelloCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner...run..."+ Arrays.asList(args));
    }
}
```



# 八、自定義starter

starter：

​	1、這個場景需要使用到的依賴是什麼？

​	2、如何編寫自動配置

```java
@Configuration  //指定這個類是一個配置類
@ConditionalOnXXX  //在指定條件成立的情況下自動配置類生效
@AutoConfigureAfter  //指定自動配置類的順序
@Bean  //給容器中添加組件

@ConfigurationPropertie結合相關xxxProperties類來綁定相關的配置
@EnableConfigurationProperties //讓xxxProperties生效加入到容器中

自動配置類要能加載
將需要啟動就加載的自動配置類，配置在META-INF/spring.factories
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
```

​	3、模式：

啟動器只用來做依賴導入；

專門來寫一個自動配置模塊；

啟動器依賴自動配置；別人只需要引入啟動器（starter）

mybatis-spring-boot-starter；自定義啟動器名-spring-boot-starter



步驟：

1）、啟動器模塊

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.atguigu.starter</groupId>
    <artifactId>atguigu-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--啟動器-->
    <dependencies>

        <!--引入自動配置模塊-->
        <dependency>
            <groupId>com.atguigu.starter</groupId>
            <artifactId>atguigu-spring-boot-starter-autoconfigurer</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>

</project>
```

2）、自動配置模塊

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>

   <groupId>com.atguigu.starter</groupId>
   <artifactId>atguigu-spring-boot-starter-autoconfigurer</artifactId>
   <version>0.0.1-SNAPSHOT</version>
   <packaging>jar</packaging>

   <name>atguigu-spring-boot-starter-autoconfigurer</name>
   <description>Demo project for Spring Boot</description>

   <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>1.5.10.RELEASE</version>
      <relativePath/> <!-- lookup parent from repository -->
   </parent>

   <properties>
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
      <java.version>1.8</java.version>
   </properties>

   <dependencies>

      <!--引入spring-boot-starter；所有starter的基本配置-->
      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter</artifactId>
      </dependency>

   </dependencies>



</project>

```



```java
package com.atguigu.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "atguigu.hello")
public class HelloProperties {

    private String prefix;
    private String suffix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}

```

```java
package com.atguigu.starter;

public class HelloService {

    HelloProperties helloProperties;

    public HelloProperties getHelloProperties() {
        return helloProperties;
    }

    public void setHelloProperties(HelloProperties helloProperties) {
        this.helloProperties = helloProperties;
    }

    public String sayHellAtguigu(String name){
        return helloProperties.getPrefix()+"-" +name + helloProperties.getSuffix();
    }
}

```

```java
package com.atguigu.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication //web應用才生效
@EnableConfigurationProperties(HelloProperties.class)
public class HelloServiceAutoConfiguration {

    @Autowired
    HelloProperties helloProperties;
    @Bean
    public HelloService helloService(){
        HelloService service = new HelloService();
        service.setHelloProperties(helloProperties);
        return service;
    }
}

```

# 更多SpringBoot整合示例

https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples