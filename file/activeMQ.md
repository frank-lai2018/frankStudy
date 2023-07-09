 # MQ簡介

 * MQ就是消息間件

## 定義
- 面向消息的中間件（message-oriented middleware）MOM能夠很好的解決以上問題，是指利用高效可靠的消息傳遞機制與平台無關的數據交流，並基於數據通信來進行分佈式系統的集成。
- 通過提供消息傳遞和消息排隊模型在分佈式環境下提供應用解耦，彈性伸縮，冗餘存儲、流量削峰，異步通信，數據同步等功能。
- 大致的過程是這樣的：
  - 發送者把消息發送給消息服務器，消息服務器將消息存放在若干隊列/主題topic中，在合適的時候，消息服務器回將消息轉發給接受者。在這個過程中，發送和接收是異步的，也就是發送無需等待，而且發送者和接受者的生命週期也沒有必然的關係；
  - 尤其在發布pub/訂閱sub模式下，也可以完成一對多的通信，即讓一個消息有多個接受者。

![063](activemq/imgs/2.png)

## 相關功能技術點

- 1.api發送和接收
- 2.MQ的高可用性
- 3.MQ的集群和容錯配置
- 4.MQ的持久化
- 5.延時發送/定時發送
- 6.簽收機制
- 7.與spring整合
-  ....

## 特點

- 1.採異步處理模式
  - 消息發送者可以發送一個消息而無須等待響應。
  - 消息發送者將消息發送到一條虛擬的通道（主題或者隊列）上
  - 消息接收者則訂閱或者監聽該愛通道。一條消息可能最終轉發給一個或者多個消息接收者，這些消息接收者都無需對消息發送者做出同步回應。整個過程都是異步的。
  - 案例：
    - 也就是說，一個系統跟另一個系統之間進行通信的時候，假如係統A希望發送一個消息給系統B，讓他去處理。但是系統A不關注系統B到底怎麼處理或者有沒有處理好，所以系統A把消息發送給MQ，然後就不管這條消息的“死活了”，接著系統B從MQ裡面消費出來處理即可。至於怎麼處理，是否處理完畢，什麼時候處理，都是系統B的事兒，與系統A無關。

![063](activemq/imgs/3.png)

這樣的一種通信方式，就是所謂的“異步”通信方式，對於系統A來說，只需要把消息發送給MQ，然後系統B就會異步的去進行處理了，系統A不需要“同步”的等待系統B處理完成。這樣的好處是什麼呢？兩個字：解耦

- 2.應用系統之間的解耦合
  - 發送者和接受者不必了解對方，只需要確認消息
  - 發送者和接受者不必同時在線

- 3.案例
![063](activemq/imgs/4.png)

## 相關產品

- (1)	kafka
  - 編程語言：scala。
  - 大數據領域的主流MQ。

- (2)	rabbitmq
  - 編程語言：erlang
  - 基於erlang語言，不好修改底層，不要查找問題的原因，不建議選用。

- (3)	rocketmq
  - 編程語言：java
  - 適用於大型項目。適用於集群。

- (4)	activemq
  - 編程語言：java
  - 適用於中小型項目。

![063](activemq/imgs/1.png)

# 安裝

## 下載
https://activemq.apache.org/activemq-5016004-release
![063](activemq/imgs/7.png)


## 啟動、關閉
```
#查看有無開啟
ps -ef|grep activemq|grep -v grep

#查看port
netstat -anp|grep 61616

#
lsof -i:61616

#將log寫入LOG檔
./activemq start > /opt/module/apache-activemq-5.16.4/log

# 有修改設定檔，依照修改的設定檔啟動

./activemq start xbean:file:/opt/module/apache-activemq-5.16.4/conf/activemq02.xml
```

![063](activemq/imgs/5.png)
![063](activemq/imgs/6.png)

## 開啟後台

### 對外開放port號

```
#開啟 8161 port
firewall-cmd --zone=public --add-port=8161/tcp --permanent
firewall-cmd --zone=public --add-port=61616/tcp --permanent

#重啟防火牆
systemctl restart network
```
![063](activemq/imgs/8.png)

### 修改conf/jetty.xml

```xml

    <bean id="jettyPort" class="org.apache.activemq.web.WebConsolePort" init-method="start">
             <!-- the default port number for the web console -->
        <property name="host" value="192.168.47.129"/>
        <property name="port" value="8161"/>
    </bean>

```

## JMS編碼結構

![063](activemq/imgs/12.png)

### Activemq遵循了JMS規範，總體的流程分為以下幾步：

  * 1.創建ConnectionFactory
  * 2.使用ConnectionFactory創建一個Connection
  * 3.使用Connection創建一個Session
  * 4.使用Session創建消息的生產者(Message Producer)和消息的消費者(MessageConsumer)
  * 5.生產者往Destination發送消息
  * 6.消費者從Destination消費消息
### Destination分成兩個部分

![063](activemq/imgs/9.png)

* 1.隊列(Queue)
  ![063](activemq/imgs/13.png)
* 2.主題(Topic)
  ![063](activemq/imgs/14.png)

## 使用JAVA放入消息，處理消息(queue)

### 消息生產者

```
package com.frank.activemq.queue;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsProduce {
	
	public static final String ACTIVEMQ_URL = "tcp://192.168.47.129:61616";
	public static final String QUEUE_NAME = "queue01";
	
	public static void main(String[] args) throws JMSException {
		//1.創建連接工廠，按照給定的URL地址，採用默認用戶名和密碼
		ActiveMQConnectionFactory actuActiveMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		
		//2.通過連接工廠，獲得連接connection並開啟訪問
		Connection connection = actuActiveMQConnectionFactory.createConnection();
		connection.start();
		
		//3.創建session
		//兩個參數，第一個叫事務/第二個叫簽收
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		
		//4.創建目的地(具體是對列還是主題topic)
//		Destination destination = session.createQueue(QUEUE_NAME);
		Queue queue = session.createQueue(QUEUE_NAME);
		
		//5.創建消息的生產者
		MessageProducer messageProducer = session.createProducer(queue);
		
		//6.通過使用 MessageProducer 生產3條消息發送到MQ隊列裡面
		for(int i = 1 ;i<=3;i++) {
			
			//7.創建消息
			TextMessage textMessage = session.createTextMessage("msg----"+i);//理解為一個字串
			
			//8.通過MessageProducer發送給MQ
			messageProducer.send(textMessage);
		}
		
		//9.關閉資源
		messageProducer.close();
		session.close();
		connection.close();
		
		System.out.println("消息發布完成");
	}
}

```



 ![063](activemq/imgs/10.png)

### 消息消費者

- 1.阻塞式消費者(receive)

  - 訂閱者或接收者抵用MessageConsumer的receive()方法來接收消息，receive方法在能接收到消息之前（或超時之前）將一直阻塞。

 ![063](activemq/imgs/16.png)

```java
package com.frank.activemq.queue;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsConsumer {
	
	public static final String ACTIVEMQ_URL = "tcp://192.168.47.129:61616";
	public static final String QUEUE_NAME = "queue01";

	public static void main(String[] args) throws JMSException, IOException {
		//1.創建連接工廠，按照給定的URL地址，採用默認用戶名和密碼
		ActiveMQConnectionFactory actuActiveMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		
		//2.通過連接工廠，獲得連接connection並開啟訪問
		Connection connection = actuActiveMQConnectionFactory.createConnection();
		connection.start();
		
		//3.創建session
		//兩個參數，第一個叫事務/第二個叫簽收
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		
		//4.創建目的地(具體是對列還是主題topic)
//		Destination destination = session.createQueue(QUEUE_NAME);
		Queue queue = session.createQueue(QUEUE_NAME);
		
		//5.創建消費者
		MessageConsumer messageConsumer = session.createConsumer(queue);
		
		while(true) {
			
			/*
			 * 同步阻塞方式(receive())
			 * 訂閱者或接收者調用MessageConsumer的receive()方法來接收消息，receive方法在能夠接收到消息之前(或超時之前)將一直堵塞
			 * */
			TextMessage textMessage = (TextMessage) messageConsumer.receive();//一直等
			//			TextMessage textMessage = (TextMessage) messageConsumer.receive(1000);//等待一秒後離開
			if(null != textMessage) {
				System.out.println("***消費者接收到消息:"+textMessage.getText());
			}else{
				break;
			}
		}
		
		
		System.in.read();
		
		messageConsumer.close();
		session.close();
		connection.close();
		
		

	}
}


```
- 2.異步監聽式消費者（監聽器onMessage()）
  - 訂閱者或接收者通過MessageConsumer的setMessageListener(MessageListener listener)註冊一個消息監聽器，
  - 當消息到達之後，系統會自動調用監聽器MessageListener的onMessage(Message message)方法。

```java
package com.frank.activemq.queue;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsConsumer {
	
	public static final String ACTIVEMQ_URL = "tcp://192.168.47.129:61616";
	public static final String QUEUE_NAME = "queue01";

	public static void main(String[] args) throws JMSException, IOException {
		//1.創建連接工廠，按照給定的URL地址，採用默認用戶名和密碼
		ActiveMQConnectionFactory actuActiveMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		
		//2.通過連接工廠，獲得連接connection並開啟訪問
		Connection connection = actuActiveMQConnectionFactory.createConnection();
		connection.start();
		
		//3.創建session
		//兩個參數，第一個叫事務/第二個叫簽收
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		
		//4.創建目的地(具體是對列還是主題topic)
//		Destination destination = session.createQueue(QUEUE_NAME);
		Queue queue = session.createQueue(QUEUE_NAME);
		
		//5.創建消費者
		MessageConsumer messageConsumer = session.createConsumer(queue);
		
		//通過監聽的方式來消費消息
		//異步非阻塞方式(監聽器onMessage())
		//訂閱者或接收者通過MmessageConsumer的setMessageListener(MessageListener listener)註冊一個消息監聽器
		//當消息到達之後，系統自動調用監聽器MessageListener 的onMessage(Message message)方法
		messageConsumer.setMessageListener(new MessageListener() {
			
			public void onMessage(Message message) {
				if(null != message && message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					
					try {
						System.out.println("***消費者接收到消息:"+textMessage.getText());
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		System.in.read();
		
		messageConsumer.close();
		session.close();
		connection.close();
		
		

	}
}
```



 ![063](activemq/imgs/11.png)

 ### 控制台說明

* Number Of Pending Messages=等待消費的消息，這個是未出隊列的數量，公式=總接收數-總出隊列數。
* Number Of Consumers=消費者數量，消費者端的消費者數量。
* Messages Enqueued=進隊消息數，進隊列的總消息量，包括出隊列的。這個數只增不減。
* Messages Dequeued=出隊消息數，可以理解為是消費者消費掉的數量。

- 總結：
  - 當有一個消息進入這個隊列時，等待消費的消息是1，進入隊列的消息是1。
  - 當消息消費後，等待消費的消息是0，進入隊列的消息是1，出隊列的消息是1。
  - 當再來一條消息時，等待消費的消息是1，進入隊列的消息就是2。

### 案例


- 1.先生產，只啟動1號消費者，問題:1號消費者還能消費消息嗎?
  - 可以
- 2.先生產，先啟動1號消費者，再啟動2號消費者 ，問題:2號消費者還能消費消息嗎?
  - 2.1 1號可以消費
    - 可以
  - 2.2 2號可以消費嗎?
    - 不能
 
- 3. 先啟動2個消費者，在生產6條消息，請問消費情況如何?
  - 3.2先到先得，6條權給給一個
    - 不是
  - 3.3一人一半
    - 輪流分配

## 使用JAVA放入消息，處理消息(topic)

- 發布/訂閱消息傳遞域的特點如下：
  - （1）生產者將消息發佈到topic中，每個消息可以有多個消費者，屬於1：N的關係；
  - （2）生產者和消費者之間有時間上的相關性。訂閱某一個主題的消費者只能消費自它訂閱之後發布的消息。
  - （3）生產者生產時，topic不保存消息它是無狀態的不落地，假如無人訂閱就去生產，那就是一條廢消息，所以，一般先啟動消費者再啟動生產者。
 
- JMS規範允許客戶創建持久訂閱，這在一定程度上放鬆了時間上的相關性要求。持久訂閱允許消費者消費它在未處於激活狀態時發送的消息。一句話，好比微信公眾號訂閱

 ![063](activemq/imgs/19.png)

### 生產者

```java
package com.frank.activemq.topic;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsProduce_topic {
	
	public static final String ACTIVEMQ_URL = "tcp://192.168.47.129:61616";
	public static final String TOPIC_NAME = "topic01";
	
	public static void main(String[] args) throws JMSException {
		//1.創建連接工廠，按照給定的URL地址，採用默認用戶名和密碼
		ActiveMQConnectionFactory actuActiveMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		
		//2.通過連接工廠，獲得連接connection並開啟訪問
		Connection connection = actuActiveMQConnectionFactory.createConnection();
		connection.start();
		
		//3.創建session
		//兩個參數，第一個叫事務/第二個叫簽收
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		
		//4.創建目的地(具體是對列還是主題topic)
//		Destination destination = session.createQueue(QUEUE_NAME);
		Topic topic = session.createTopic(TOPIC_NAME);
		
		//5.創建消息的生產者
		MessageProducer messageProducer = session.createProducer(topic);
		
		//6.通過使用 MessageProducer 生產3條消息發送到MQ隊列裡面
		for(int i = 1 ;i<=3;i++) {
			
			//7.創建消息
			TextMessage textMessage = session.createTextMessage("msg----"+i);//理解為一個字串
			
			//8.通過MessageProducer發送給MQ
			messageProducer.send(textMessage);
		}
		
		//9.關閉資源
		messageProducer.close();
		session.close();
		connection.close();
		
		System.out.println("消息發布完成");
	}
}

```

### 消費者
```java
package com.frank.activemq.topic;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsConsumer_topic {
	
	public static final String ACTIVEMQ_URL = "tcp://192.168.47.129:61616";
	public static final String TOPIC_NAME = "topic01";

	public static void main(String[] args) throws JMSException, IOException {
		System.out.println("我是消費者3...........");
		//1.創建連接工廠，按照給定的URL地址，採用默認用戶名和密碼
		ActiveMQConnectionFactory actuActiveMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		
		//2.通過連接工廠，獲得連接connection並開啟訪問
		Connection connection = actuActiveMQConnectionFactory.createConnection();
		connection.start();
		
		//3.創建session
		//兩個參數，第一個叫事務/第二個叫簽收
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		
		//4.創建目的地(具體是對列還是主題topic)
//		Destination destination = session.createQueue(QUEUE_NAME);
		Topic topic = session.createTopic(TOPIC_NAME);
		
		//5.創建消費者
		MessageConsumer messageConsumer = session.createConsumer(topic);
		
		messageConsumer.setMessageListener(message -> {
				if(null != message && message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					
					try {
						System.out.println("***消費者接收到消息:"+textMessage.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
		});
		
		System.in.read();
		
		messageConsumer.close();
		session.close();
		connection.close();
		
		
	}
}

```

 ![063](activemq/imgs/18.png)

 ## Topic 跟 Queue比較

|比較項目|Topic隊列模式  | Queue隊列模式 |  |
| ---------- | --- |--- |--- |
|工作模式|"訂閱-發布"模式，如果當前沒有訂閱者，消息將會被丟棄，如果有多個訂閱者，那麼這些訂閱者都會收到所有消息| "負載均衡"模式，如果當前沒有消費者，消息也不會被丟棄；如果有多個消費者，那麼一條消息也只會發送給其中一個消費者，並且要求消費者ack(簽收)訊息|
|有無狀態|無狀態|Queue數據默認會在mq服務器上以文件形式保存，比如Active MQ一般保存在$AMQ_HOME\data\kr-store\data下面，也可以配置成DB存儲|
|傳遞完整性|如果沒有訂閱者，消息會被丟棄|消息不會被丟棄|
|處理效率|由於消息要按照訂閱者的數量進行複制，所以處理性能會隨著訂閱者的增加而明顯降低，並且還要結合不同消息協議自身的性能差異|由於一條消息只發送給一個消費者，所以就算消費者再多，性能也不會有明顯降低。當然不同消息協議的具體性能也是有差異的|


# JMS 簡介

[JMS](./jms.md )

# ActiveMQ的Broker

## 是什麼
- 相當於一個ActiveMQ服務器實例
 
- 說白了，Broker其實就是實現了用代碼的形式啟動ActiveMQ將MQ嵌入到Java代碼中，以便隨時用隨時啟動，
在用的時候再去啟動這樣能節省了資源，也保證了可用性。

### pom.xml

以下Jackson 的包一定要加，不然會報錯
```xml
		<!--  broker 的绑定  -->
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.9.10.4</version>
		</dependency>
```

### 程式範例

```java
package com.frank.activemq.broker;

import org.apache.activemq.broker.BrokerService;

public class EmbedBroker {
	public static void main(String[] args) throws Exception {
		BrokerService brokerService = new BrokerService();
		brokerService.setUseJmx(true);
		brokerService.addConnector("tcp://localhost:61616");
		brokerService.start();
		
		System.in.read();
	}
}

```

### 測試驗證

- 和Linux上的ActiveMQ是一樣的,Broker相當於一個Mini版本的ActiveMQ

# Spring整合ActiveMQ

## pom

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.frank.avtivemq</groupId>
	<artifactId>activemq_spring</artifactId>
	<version>1.0-SNAPSHOT</version>
	<packaging>war</packaging>
	<name>activemq_demo Maven Webapp</name>
	<!-- FIXME change it to the project's website -->
	<url>http://www.example.com</url>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
	</properties>
	<dependencies>
		<!--activemq所需要的jar包-->
		<dependency>
			<groupId>org.apache.activemq</groupId>
			<artifactId>activemq-all</artifactId>
			<version>5.12.0</version>
		</dependency>
		<!--  activemq 和 spring 整合的基礎包 -->
		<dependency>
			<groupId>org.apache.xbean</groupId>
			<artifactId>xbean-spring</artifactId>
			<version>3.16</version>
		</dependency>
		<!--  嵌入式activemq的broker所需要的依賴包   -->
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.10.1</version>
		</dependency>
		<!-- activemq連接池 -->
		<dependency>
			<groupId>org.apache.activemq</groupId>
			<artifactId>activemq-pool</artifactId>
			<version>5.12.0</version>
		</dependency>
		<!-- spring支持jms的包 -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-jms</artifactId>
			<version>5.2.1.RELEASE</version>
		</dependency>
		<!--spring相關依賴包-->
		<dependency>
			<groupId>org.apache.xbean</groupId>
			<artifactId>xbean-spring</artifactId>
			<version>4.15</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-aop</artifactId>
			<version>4.3.23.RELEASE</version>
		</dependency>
		<!-- Spring核心依賴 -->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
			<version>4.3.23.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-context</artifactId>
			<version>4.3.23.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-aop</artifactId>
			<version>4.3.23.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-orm</artifactId>
			<version>4.3.23.RELEASE</version>
		</dependency>
	</dependencies>
	<build>
		<finalName>activemq_spring</finalName>
		<pluginManagement>
			<!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
			<plugins>
				<plugin>
					<artifactId>maven-clean-plugin</artifactId>
					<version>3.1.0</version>
				</plugin>
				<!-- see http://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_war_packaging -->
				<plugin>
					<artifactId>maven-resources-plugin</artifactId>
					<version>3.0.2</version>
				</plugin>
				<plugin>
					<artifactId>maven-compiler-plugin</artifactId>
					<version>3.8.0</version>
				</plugin>
				<plugin>
					<artifactId>maven-surefire-plugin</artifactId>
					<version>2.22.1</version>
				</plugin>
				<plugin>
					<artifactId>maven-war-plugin</artifactId>
					<version>3.2.2</version>
				</plugin>
				<plugin>
					<artifactId>maven-install-plugin</artifactId>
					<version>2.5.2</version>
				</plugin>
				<plugin>
					<artifactId>maven-deploy-plugin</artifactId>
					<version>2.8.2</version>
				</plugin>
			</plugins>
		</pluginManagement>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>


```

## Spring配置文件(applicationContext.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"

	xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/context
         http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop
     http://www.springframework.org/schema/aop/spring-aop.xsd
     http://camel.apache.org/schema/spring http://camel.apache.org/schema/spring/camel-spring.xsd">

	<!-- 開啟包的自動掃描 -->
	<context:component-scan
		base-package="com.frank.activemq" />

	<!-- 配置生產者 -->
	<bean id="jmsFactory"
		class="org.apache.activemq.pool.PooledConnectionFactory"
		destroy-method="stop">
		<property name="connectionFactory">
			<bean class="org.apache.activemq.ActiveMQConnectionFactory">
				<property name="brokerURL"
					value="tcp://192.168.47.129:61616"></property>
			</bean>
		</property>
		<property name="maxConnections" value="100"></property>
	</bean>


	<!-- 這個是對列的目的地,點對點的Queue -->
	<bean id="destinationQueue"
		class="org.apache.activemq.command.ActiveMQQueue">
		<!-- 通過構造注入Queue名 -->
		<constructor-arg index="0" value="spring-active-queue" />
	</bean>

	<!-- 這個是隊列目的地, 發布訂閱的主題Topic -->
	<bean id="destinationTopic"
		class="org.apache.activemq.command.ActiveMQTopic">
		<constructor-arg index="0" value="spring-active-topic" />
	</bean>

	<!-- Spring提供的JMS工具類,他可以進行消息發送,接收等 -->
	<bean id="jmsTemplate"
		class="org.springframework.jms.core.JmsTemplate">
		<!-- 傳入連接工廠 -->
		<property name="connectionFactory" ref="jmsFactory" />
		<!-- 傳入目的地看要傳入Queue或者Topic -->
		<property name="defaultDestination" ref="destinationQueue" />
		<!-- 消息自動轉換器 -->
		<property name="messageConverter">
			<bean
				class="org.springframework.jms.support.converter.SimpleMessageConverter" />
		</property>
	</bean>
	
	<!--  配置Jms消息監聽器  -->
    <bean id="defaultMessageListenerContainer" class="org.springframework.jms.listener.DefaultMessageListenerContainer">
        <!--  Jms連接的工廠     -->
        <property name="connectionFactory" ref="jmsFactory"/>
        <!--   設置默認的監聽目的地     -->
        <property name="destination" ref="destinationTopic"/>
        <!--  指定自己實現了MessageListener的類     -->
        <property name="messageListener" ref="myMessageListener"/>
    </bean>

	<!--<bean id = "myMessageListener" class="com.frank.activemq.MyMessageListener"></bean>-->
</beans>
```

## Queue隊列

### 生產者

```java
package com.frank.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpringMQ_Produce {

	@Autowired
	private JmsTemplate jsmTemplate;
	
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		SpringMQ_Produce produce = (SpringMQ_Produce) ctx.getBean("springMQ_Produce");
		
		produce.jsmTemplate.send(session -> {
			return session.createTextMessage("****Spring和activeMQ的整合case33333*****");
		});
		
		System.out.println("send task OK....");
		
		//關閉Spring
//		((ConfigurableApplicationContext)ctx).close();
	}
}

```

### 消費者

```java
package com.frank.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpringMQ_Consumer {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	public static void main(String[] args) throws JMSException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		SpringMQ_Consumer consumer = (SpringMQ_Consumer) ctx.getBean("springMQ_Consumer");
		
		TextMessage textMessage = (TextMessage)consumer.jmsTemplate.receive();
//		
//		System.out.println("消費者得到消息...."+textMessage.getText());
		consumer.jmsTemplate.setReceiveTimeout(3000);
//		consumer.jmsTemplate.set
		String returnValue = (String) consumer.jmsTemplate.receiveAndConvert();
		System.out.println("消費者得到消息11...."+returnValue);
		
		//關閉Spring
//		((ConfigurableApplicationContext)ctx).close();
	}
}

```

## Topic 主題

### receive方式

- 只需要改spring 配置文件 applicationContext.xml，把傳入目的地(defaultDestination)改成Topic即可，如下

```java

	<!-- Spring提供的JMS工具類,他可以進行消息發送,接收等 -->
	<bean id="jmsTemplate"
		class="org.springframework.jms.core.JmsTemplate">
		<!-- 傳入連接工廠 -->
		<property name="connectionFactory" ref="jmsFactory" />
		<!-- 傳入目的地看要傳入Queue或者Topic -->
		<property name="defaultDestination" ref="destinationTopic" />
		<!-- 消息自動轉換器 -->
		<property name="messageConverter">
			<bean
				class="org.springframework.jms.support.converter.SimpleMessageConverter" />
		</property>
	</bean>
```

### 使用監聽方式

- 在Spring裡面實現消費者不啟動，直接通過配置監聽完成

- 在配置文件裡applicationContext.xml，加入以下訊息

```xml
	<!--  配置Jms消息監聽器  -->
    <bean id="defaultMessageListenerContainer" class="org.springframework.jms.listener.DefaultMessageListenerContainer">
        <!--  Jms連接的工廠     -->
        <property name="connectionFactory" ref="jmsFactory"/>
        <!--   設置默認的監聽目的地     -->
        <property name="destination" ref="destinationTopic"/>
        <!--  指定自己實現了MessageListener的類     -->
        <property name="messageListener" ref="myMessageListener"/>
    </bean>
```

- 創建實作MessageListener的myMessageListener類

```java
package com.frank.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

@Component
public class MyMessageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		if(null != message && message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				System.out.println("監聽器3333:"+textMessage.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
	}

}

```

- 執行結果


 ![063](activemq/imgs/25.png)

 消費者配置了自動監聽，就相當於在spring裡面後台運行，有消息就運行我們實現監聽類裡面的方法


 # SpringBoot整合ActiveMQ

- 新建父組件 POM
- 名稱:activemq
- POM

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.frank</groupId>
	<artifactId>activemq</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>pom</packaging>
	<modules>
		<module>springboot_activemq_api</module>
		<module>springboot_activemq_queue_produce</module>
		<module>springboot_activemq_queue_consumer</module>
		<module>springboot_activemq_topic_produce</module>
		<module>springboot_activemq_topic_consumer</module>
	</modules>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
		<junit.version>4.12</junit.version>
		<log4j.version>1.2.17</log4j.version>
		<lombok.version>1.16.18</lombok.version>
	</properties>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-parent</artifactId>
				<version>2.7.13</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
			<dependency>
				<groupId>mysql</groupId>
				<artifactId>mysql-connector-java</artifactId>
				<version>5.0.4</version>
			</dependency>
			<dependency>
				<groupId>com.alibaba</groupId>
				<artifactId>druid</artifactId>
				<version>1.0.31</version>
			</dependency>
			<dependency>
				<groupId>org.mybatis.spring.boot</groupId>
				<artifactId>mybatis-spring-boot-starter</artifactId>
				<version>1.3.0</version>
			</dependency>
			<dependency>
				<groupId>ch.qos.logback</groupId>
				<artifactId>logback-core</artifactId>
				<version>1.2.3</version>
			</dependency>
			<dependency>
				<groupId>junit</groupId>
				<artifactId>junit</artifactId>
				<version>${junit.version}</version>
				<scope>test</scope>
			</dependency>
			<dependency>
				<groupId>log4j</groupId>
				<artifactId>log4j</artifactId>
				<version>${log4j.version}</version>
			</dependency>
		</dependencies>
	</dependencyManagement>
	<build>
		<finalName>activemq</finalName>
		<resources>
			<resource>
				<directory>src/main/resources</directory>
				<filtering>true</filtering>
			</resource>
		</resources>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-resources-plugin</artifactId>
				<configuration>
					<delimiters>
						<delimit>$</delimit>
					</delimiters>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
```
## 新建共用API

- name:springboot_activemq_api
- POM

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.frank</groupId>
    <artifactId>activemq</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  <artifactId>springboot_activemq_api</artifactId>
</project>
```
- 建立共用DTO

```java
package com.frank.activemq.dto;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = -5292603378214046021L;
	private String id;
	private String msg;
	
	
	
	public Message(String id, String msg) {
		super();
		this.id = id;
		this.msg = msg;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	@Override
	public String toString() {
		return "Message [id=" + id + ", msg=" + msg + "]";
	}
	
	
}

```

## Queue 隊列

### 隊列生產者

####  建立maven module
- name:springboot_activemq_queue_produce

#### POM

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.frank</groupId>
		<artifactId>activemq</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</parent>
	<artifactId>springboot_activemq_queue_produce</artifactId>
	<dependencies>
		<dependency>
			<groupId>com.frank</groupId>
			<artifactId>springboot_activemq_api</artifactId>
			<version>${project.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-activemq</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
</project>
```

#### 配置yml

```yml
server: 
  port: 8081

spring: 
  activemq:
    broker-url: tcp://192.168.47.129:61616 #MQ服務器地址
    user: admin
    password: admin
  
  jms: 
    pub-sub-domain: false # false = Queue(默認)，true = Topic



```

#### 配置bean

- 類似於Spring的ApplicationContext.xml文件
- 開啟JMS註解 @EnableJms

```java
package com.frank.activemq.config;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

@Component
@EnableJms//開啟JMS註解
public class ConfigBean {
	
//	@Value("${frank.myqueue}")
	private String myQueue="boot-activemq-queue";
	
	@Bean
	public ActiveMQQueue queue() {
		return new ActiveMQQueue(this.myQueue);
	}
}

```

#### Queue_Producer

```java
package com.frank.activemq.produce;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.frank.activemq.dto.Message;

@Component
public class Queue_Produce {

	@Autowired
	private JmsMessagingTemplate jsmMessagingTemplate;
	
	@Autowired
	private Queue queue;
	
	private int count=1;
	
	public void produceMsg() {
		
		//轉換+發送
		jsmMessagingTemplate.convertAndSend(queue,"TESTTEST");
		jsmMessagingTemplate.convertAndSend(queue,new Message("001", "Object..."));
		System.out.println("  produceMessage  send   ok   ");
	}
	
	//間隔時間3秒鐘定投
	@Scheduled(fixedDelay = 3000)
	public void produceMsgScheduled() {
		jsmMessagingTemplate.convertAndSend(queue,new Message("@Scheduled00"+count, "Object..."));
//		jsmMessagingTemplate.convertAndSend(queue,"TESTTEST"+count);
		count++;
		System.out.println("  produceMsgScheduled  send   ok ....  ");
	}
}

```

#### 主啟動類

```java
package com.frank.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Queue_Produce_App {

	public static void main(String[] args) {
		SpringApplication.run(Queue_Produce_App.class, args);
	}

}

```


### 隊列消費者

####  建立maven module
- name:springboot_activemq_queue_consumer

#### POM

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.frank</groupId>
		<artifactId>activemq</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</parent>
	<artifactId>springboot_activemq_queue_consumer</artifactId>
	<dependencies>
		<dependency>
			<groupId>com.frank</groupId>
			<artifactId>springboot_activemq_api</artifactId>
			<version>${project.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-activemq</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
</project>
```

#### 配置yml

- 如果接收的消息是ObjectMessage，需要在yml配置packages.trust-all = true

```yml
server: 
  port: 8083

spring: 
  activemq:
    broker-url: tcp://192.168.47.129:61616 #MQ服務器地址
    user: admin
    password: admin
    packages:
      trust-all: true #傳遞物件需要打開他
     # trusted:
     # - com.frank.activemq.dto.Message

      
  jms: 
    pub-sub-domain: false # false = Queue(默認)，true = Topic



```

#### Queue_Consumer

- 設置springboot的消息監聽註解 @JmsListener(destination = "boot-activemq-queue")
- 監聽過後會隨著springboot一起啟動,有消息就執行加了該註解的方法

```java
package com.frank.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;



@Component
public class Queue_Consumer {
	
	@JmsListener(destination = "boot-activemq-queue")
	public void receive(ObjectMessage objectmessage) throws JMSException {
			System.out.println("textMessage:"+objectmessage.getObject());
//			System.out.println("textMessage:"+message.getBody(Message.class));
	}
}

```

## Topic 主題訂閱發佈

### Topic生產者

####  建立maven module
- name:springboot_activemq_topic_produce

#### POM

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.frank</groupId>
		<artifactId>activemq</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</parent>
	<artifactId>springboot_activemq_topic_produce</artifactId>
	<dependencies>
		<dependency>
			<groupId>com.frank</groupId>
			<artifactId>springboot_activemq_api</artifactId>
			<version>${project.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-activemq</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
</project>
```

#### 配置yml

```yml
server: 
  port: 5501

spring: 
  activemq:
    broker-url: tcp://192.168.47.129:61616 #MQ服務器地址
    user: admin
    password: admin
  
  jms: 
    pub-sub-domain: true # false = Queue(默認)，true = Topic



```

#### 配置Bean

```java
package com.frank.activemq.config;

import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

@Component
@EnableJms//開啟JMS註解
public class ConfigBean {
	
	private String myTopic="boot-activemq-topic";
	
	@Bean
	public Topic topic() {
		return new ActiveMQTopic(this.myTopic);
	}
}

```

#### Topic_Producer

```java
package com.frank.activemq.produce;

import javax.jms.Queue;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class Topic_Produce {

	@Autowired
	private JmsMessagingTemplate jsmMessagingTemplate;
	
	@Autowired
	private Topic topic;
	
	private int count=1;
	
	
	//間隔時間3秒鐘定投
	@Scheduled(fixedDelay = 3000)
	public void produceMsgScheduled() {
		jsmMessagingTemplate.convertAndSend(topic,"test_topic_"+count);
//		jsmMessagingTemplate.convertAndSend(queue,"TESTTEST"+count);
		count++;
		System.out.println("  produceMsgScheduled  send   ok ....  ");
	}
}

```

#### 主啟動類

```java
package com.frank.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Topic_Produce_App {

	public static void main(String[] args) {
		SpringApplication.run(Topic_Produce_App.class, args);
	}

}

```
- 先啟動消費者,後啟動生產者

### Topic消費者

####  建立maven module
- name:springboot_activemq_topic_consumer

#### POM

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.frank</groupId>
		<artifactId>activemq</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</parent>
	<artifactId>springboot_activemq_topic_consumer</artifactId>
	<dependencies>
		<dependency>
			<groupId>com.frank</groupId>
			<artifactId>springboot_activemq_api</artifactId>
			<version>${project.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-activemq</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
</project>
```

#### 配置yml

```yml
server: 
  port: 55666

spring: 
  activemq:
    broker-url: tcp://192.168.47.129:61616 #MQ服務器地址
    user: admin
    password: admin
    packages:
      trust-all: true #傳遞物件需要打開他

      
  jms: 
    pub-sub-domain: true # false = Queue(默認)，true = Topic



```

#### Topic_Consumer

```java
package com.frank.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;



@Component
public class Topic_Consumer {
	
	@JmsListener(destination = "boot-activemq-topic")
	public void receive(TextMessage textmessage) throws JMSException {
			System.out.println("textMessage:"+textmessage.getText());
//			System.out.println("textMessage:"+message.getBody(Message.class));
	}
}

```

#### 主啟動類

```java
package com.frank.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Topic_Consumer_App5555 {

	public static void main(String[] args) {
		SpringApplication.run(Topic_Consumer_App5555.class, args);
	}

}

```

####  建立maven module
- name:springboot_activemq_topic_consumer

#### POM

```xml
```

#### 配置yml

#### Topic_Consumer

#### 主啟動類

# ActiveMQ的傳輸協議

## 官網

## 是什麼

- ActiveMQ支持的client-broker通訊協議有：TVP、NIO、UDP、SSL、Http(s)、VM。
- 其中配置Transport Connector的文件在ActiveMQ安裝目錄的conf/activemq.xml中的<transportConnectors>標籤之內。

![063](activemq/imgs/27.png)

- 在上文給出的配置信息中，
URI描述信息的頭部都是採用協議名稱：例如
  - 描述amqp協議的監聽端口時，採用的URI描述格式為“amqp://······”；
  - 描述Stomp協議的監聽端口時，採用URI描述格式為“stomp://······”；
  - 唯獨在進行openwire協議描述時，URI頭卻採用的“tcp://······”。這是因為ActiveMQ中默認的消息協議就是openwire

## ActiveMQ支援協議

### 1.Transmission Control Protocol(TCP)默認

- 1.這是默認的Broker配置，TCP的Client監聽端口61616
- 2.在網絡傳輸數據前，必須要先序列化數據，消息是通過一個叫wire protocol的來序列化成字節流。
- 3.TCP連接的URI形式如：tcp://domainName:port?key=value&key=value，後面的參數是可選的。
- 4.TCP傳輸的的優點：
  - (4.1)TCP協議傳輸可靠性高，穩定性強
  - (4.2)高效率：字節流方式傳遞，效率很高
  - (4.3)有效性、可用性：應用廣泛，支持任何平台
- 5.關於Transport協議的可選配置參數可以參考官網http://activemq.apache.org/configuring-version-5-transports.html

### 2.New I/O API Protocol(NIO)(生產環境用)

- 1.NIO協議和TCP協議類似，但NIO更側重於底層的訪問操作。它允許開發人員對同一資源可有更多的client調用和服務器端有更多的負載。
- 2.適合使用NIO協議的場景：
  - (2.1)可能有大量的Client去連接到Broker上，一般情況下，大量的Client去連接Broker是被操作系統的線程所限制的。因此，NIO的實現比TCP需要更少的線程去運行，所以建議使用NIO協議。
  - (2.2)可能對於Broker有一個很遲鈍的網絡傳輸，NIO比TCP提供更好的性能。
- 3.NIO連接的URI形式：nio://domainName:port?key=value&key=value
- 4.關於Transport協議的可選配置參數可以參考官網http://activemq.apache.org/configuring-version-5-transports.html

### 3.AMQP協議


- Advanced Message Queuing Protocol，一個提供統一消息服務的應用層標準高級消息隊列協議，是應用層協議的一個開放標準，為面向消息的中間件設計。基於此協議的客戶端與消息中間件可傳遞消息，並不受客戶端/中間件不同產品，不同開發語言等條件限制。

### 4.Stomp協議

- STOP，Streaming Text Orientation Message Protocol，是流文本定向消息協議，是一種為MOM(Message Oriented Middleware，面向消息中間件)設計的簡單文本協議。

### 5.Secure Sockets Layer Protocol(SSL)

### 6.MQTT協議

- MQTT(Message Queuing Telemetry Transport，消息隊列遙測傳輸)是IBM開發的一個即時通訊協議，有可能成為物聯網的重要組成部分。該協議支持所有平台，幾乎可以把所有聯網物品和外部連接起來，被用來當作傳感器和致動器(比如通過Twitter讓房屋聯網)的通信協議。

### 7.WS協議(websocket)

## 設定

### 修改activemq.xml

```xml
<transportConnectors>
      <transportConnector name="nio" uri="nio://0.0.0.0:61618?trace=true" />
</transportConnectors>
```

- 如果你不特別指定ActiveMQ的網絡監聽端口，那麼這些端口都講使用BIO網絡IO模型
- 所以為了首先提高單節點的網絡吞吐性能，我們需要明確指定ActiveMQ網絡IO模型。
- 如下所示：URI格式頭以“nio”開頭，表示這個端口使用以TCP協議為基礎的NIO網絡IO模型。
![063](activemq/imgs/28.png)

- 生產和消費兩端協議代碼修改

```java
private static final String ACTIVEMQ_URL = "nio://192.168.47.129:61618";
```

- 使用auto關鍵字
  - 使用"+"符號來為端口設置多種特性
  - 如果我們既需要使用某一個端口支持NIO網絡模型,又需要它支持多個協議
  - 參考https://activemq.apache.org/auto 配置
![063](activemq/imgs/29.png)

# ActiveMQ的消息存儲和持久化

* 官網 http://activemq.apache.org/persistence

- 為了避免意外宕機以後丟失信息，需要做到重啟後可以恢復消息隊列，消息系統一半都會採用持久化機制。
ActiveMQ的消息持久化機制有JDBC，AMQ，KahaDB和LevelDB，無論使用哪種持久化方式，消息的存儲邏輯都是一致的。
 
- 就是在發送者將消息發送出去後，消息中心首先將消息存儲到本地數據文件、內存數據庫或者遠程數據庫等。再試圖將消息發給接收者，成功則將消息從存儲中刪除，失敗則繼續嘗試嘗試發送。

- 消息中心啟動以後，要先檢查指定的存儲位置是否有未成功發送的消息，如果有，則會先把存儲位置中的消息發出去。

## 種類

### AMQ Mesage Store

- AMQ是一種文件存儲形式，它具有寫入速度快和容易恢復的特點。消息存儲再一個個文件中文件的默認大小為32M，當一個文件中的消息已經全部被消費，那麼這個文件將被標識為可刪除，在下一個清除階段，這個文件被刪除。 AMQ適用於ActiveMQ5.3之前的版本

- 基於文件的存儲方式，是以前的默認消息存儲，現在不用了

### KahaDB消息存儲(默認)

- 基於日誌文件，從ActiveMQ5.4開始默認的持久化插件
- 官網 https://activemq.apache.org/kahadb

```xml
 <broker brokerName="broker">
		<persistenceAdapter>
            <kahaDB directory="${activemq.data}/kahadb"/>
        </persistenceAdapter>
 </broker>
```
#### 參數

- 1.   director: KahaDB存放的路徑，默認值activemq-data
- 2.   indexWriteBatchSize: 批量寫入磁盤的索引page數量,默認值為1000
- 3.   indexCacheSize: 內存中緩存索引page的數量,默認值10000
- 4.   enableIndexWriteAsync: 是否異步寫出索引,默認false
- 5.   journalMaxFileLength: 設置每個消息data log的大小，默認是32MB
- 6.   enableJournalDiskSyncs: 設置是否保證每個沒有事務的內容，被同步寫入磁盤，JMS持久化的時候需要，默認為true
- 7.   cleanupInterval: 在檢查到不再使用的消息後,在具體刪除消息前的時間,默認30000
- 8.   checkpointInterval: checkpoint的間隔時間，默認是5000
- 9.   ignoreMissingJournalfiles: 是否忽略丟失的消息日誌文件，默認false
- 10.  checkForCorruptJournalFiles: 在啟動的時候，將會驗證消息文件是否損壞，默認false
- 11.  checksumJournalFiles: 是否為每個消息日誌文件提供checksum,默認false
- 12.  archiveDataLogs: 是否移動文件到特定的路徑，而不是刪除它們，默認false
- 13.  directoryArchive: 定義消息已經被消費過後，移動data log到的路徑，默認null
- 14.  databaseLockedWaitDelay: 獲得數據庫鎖的等待時間(used by shared master/slave),默認10000。用於之後主從復制的時候配置
- 15.  maxAsyncJobs: 設置最大的可以存儲的異步消息隊列，默認值10000，可以和concurrent MessageProducers設置成一樣的值。
- 16.  concurrentStoreAndDispatchTransactions:是否分發消息到客戶端，同時事務存儲消息，默認true
- 17.  concurrentStoreAndDispatchTopics: 是否分發Topic消息到客戶端，同時進行存儲，默認true
- 18.  concurrentStoreAndDispatchQueues: 是否分發queue消息到客戶端，同時進行存儲，默認true

![063](activemq/imgs/30.png)

- KahaDB是目前默認的存儲方式，可用於任何場景，提高了性能和恢復能力。
- 消息存儲使用一個事務日誌和僅僅用一個索引文件來存儲它所有的地址。
- KahaDB是一個專門針對消息持久化的解決方案，它對典型的消息使用模型進行了優化。
- 數據被追加到data logs中。當不再需要log文件中的數據的時候，log文件會被丟棄。


- KahaDB在消息保存的目錄中有4類文件和一個lock，跟ActiveMQ的其他幾種文件存儲引擎相比，這就非常簡潔了。

消息存儲使用一個事務日誌和僅僅用一個索引文件來存儲它所有的地址。


#### kahadb消息4類文件

- kahadb在消息保存目錄中有4類文件（我剛剛重啟了服務，所以只有3個，少了一個db.free文件）和一個lock。

- db-<Number>.log：KahaDB存儲消息到預定義大小的數據記錄文件中，文件名為db-<Number>.log。當前文件已滿時，一個新的文件會隨之創建，number的數值隨之遞增。如每32M一個文件，文件名按照數字進行編號。當不再有引用到的數據文件中的任何消息時，文件會被刪除或歸檔，由後續的清理機制來清除文件。

- db.data：包含了持久化的BTree索引，索引了消息數據記錄中的消息，它是消息的索引文件，本質上是B-Tree（B樹），使用B-Tree作為索引指向db-<Number>.log裡存儲的消息。

- db.free：當前db.data文件裡，哪些頁面是空閒的，文件具體內容是所有空閒頁的ID，方便後續建索引的時候，先從空閒頁開始建立，保證索引的連續性，沒有碎片。

- db.redo：用來進行消息恢復，如果KahaDB消息存儲在強制退出後啟動，用於恢復B-Tree索引。

- lock：文件鎖，表示當前獲得KahaDB讀寫權限的Broker。

- 這裡的log和data類似於MySQL數據庫裡的數據和索引，可以類比來理解。

![063](activemq/imgs/31.png)

### JDBC消息儲存

- 官網 http://activemq.apache.org/persistence

#### 設定

- 1.添加mysql數據庫的驅動包到lib文件夾
![063](activemq/imgs/32.png)
- 2.配置 apache-activemq-5.16.4/conf/activemq.xml

```xml
        <persistenceAdapter>
            <jdbcPersistenceAdapter dataSource="#mysql-ds"/>
        </persistenceAdapter>


       <bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
          <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
          <property name="url" value="jdbc:mysql://192.168.47.1:3306/activemq?relaxAutoCommit=true"/>
          <property name="username" value="root"/>
          <property name="password" value="1234"/>
          <property name="poolPreparedStatements" value="true"/>
        </bean>

```
![063](activemq/imgs/33.png)

#### 建庫SQL和創表說明

- 1.建一個名為activemq的數據庫
- 2.會建立三張表
  - ACTIVEMQ_MSGS
    - ID：自增的數據庫主鍵
    - CONTAINER：消息的Destination
    - MSGID_PROD：消息發送者的主鍵
    - MSG_SEQ：是發送消息的順序，MSGID_PROD+MSG_SEQ可以組成JMS的MessageID
    - EXPIRATION：消息的過期時間，存儲的是從1970-01-01到現在的毫秒數
    - MSG：消息本體的Java序列化對象的二進制數據
    - PRIORITY：優先級，從0-9，數值越大優先級越高
  - ACTIVEMQ_ACKS:用於儲存訂閱關係，如果是持久化Topic，訂閱者和服務器的訂閱關係在這個表保存，儲存持久訂閱的信息和最後一個持久訂閱接收的消息
    - CONTAINER:消息的Destination
    - SUB_DEST:如果是使用Static集群，這個字段會有集群其他系統的信息
    - CLIENT_ID:每個訂閱者都必須有一個唯一的客戶端ID用以區分
    - SUB_NAME:訂閱者名稱
    - SELECTOR:選擇器，可以選擇只消費滿足條件的消息，條件可以用字定義屬性實現，可以支持多屬性AND和OR操作
    - LAST_ACKED_ID:記錄消費過的消息ID
  - ACTIVEMQ_LOCK
    - 表ACTIVEMQ_LOCK在集群環境中才有用，只有一個Broker可以獲得消息，其他的只能作為備份等待Master Broker不可用，才可能成為下一個Master Broker。這個表用於紀錄哪個Broker是當前的Master Broker


- 必需開啟持久化
```java
messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
```

#### 資料庫運行情況

- 在點對點類型中
  - 當DeliveryMode設置為NON_PERSISTENCE時，消息被保存在內存中
  - 當DeliveryMode設置為PERSISTENCE時，消息保存在broker的相應的文件或者數據庫中。

  - 而且點對點類型中消息一旦被Consumer消費，就從數據中刪除

#### 如果使用root帳號

- 1.一般生產環境不會使用Root帳號，root帳號允許遠端連線

- 2.執行以下指令，使root帳號可以遠端連線

```sql
use mysql;
update user set host='%' where user='root';

select host from user
```

#### 小結

- 如果是queue
  - 在沒有消費者消費的情況下會將消息保存到activemq_msgs表中，只要有任意一個消費者消費了，就會刪除消費過的消息

- 如果是topic，
  - 一般是先啟動消費訂閱者然後再生產的情況下會將持久訂閱者永久保存到qctivemq_acks，而消息則永久保存在activemq_msgs，
  - 在acks表中的訂閱者有一個last_ack_id對應了activemq_msgs中的id字段，這樣就知道訂閱者最後收到的消息是哪一條。

### LevelDB消息儲存

- 官網 http://activemq.apache.org/leveldb-store

- 這種文件系統是從ActiveMQ5.8之後引進的，它和KahaDB非常相似，也是基於文件的本地數據庫存儲形式，但是它提供比KahaDB更快的持久性。
- 但它不使用自定義B-Tree實現來索引獨寫日誌，而是使用基於LevelDB的索引
 
- 默認配置如下：

```xml
<persistenceAdapter>
      <levelDB directory="activemq-data"/>
</persistenceAdapter>
```

### JDBC Message Store with ActiveMQ Journal

- 這種方式克服了JDBC Store的不足，JDBC每次消息過來，都需要去寫庫讀庫。
- ActiveMQ Journal，使用高速緩存寫入技術，大大提高了性能。

- 當消費者的速度能夠及時跟上生產者消息的生產速度時，journal文件能夠大大減少需要寫入到DB中的消息。
- 舉個例子：
  - 生產者生產了1000條消息，這1000條消息會保存到journal文件，如果消費者的消費速度很快的情況下，在journal文件還沒有同步到DB之前，消費者已經消費了90%的以上消息，那麼這個時候只需要同步剩餘的10%的消息到DB。如果消費者的速度很慢，這個時候journal文件可以使消息以批量方式寫到DB


- 以前是實時寫入mysql，在使用了journal後，數據會被journal處理，如果在一定時間內journal處理（消費）完了，就不寫入mysql，如果沒消費完，就寫入mysql，起到一個緩存的作用

#### 配置
- 將原本的持久化機制註解，添加以下設置

```xml
    <!--    <persistenceAdapter>
            <kahaDB directory="${activemq.data}/kahadb"/>
        </persistenceAdapter> -->
<!--        <persistenceAdapter>
            <jdbcPersistenceAdapter dataSource="#mysql-ds"/>
        </persistenceAdapter> -->

      <persistenceFactory>
            <journalPersistenceAdapterFactory
                                journalLogFiles="4"
                                journalLogFileSize="32768"
                                useJournal="true"
                                useQuickJournal="true"
                                dataSource="#mysql-ds"
                                dataDirectory="../activemq-data"/>
        </persistenceFactory>

```

## ActiveMQ持久化機制小總結

- 持久化消息主要指的是：
  - MQ所在服務器宕機了消息不會丟試的機制。

- 持久化機制演變的過程：
  - 從最初的AMQ Message Store方案到ActiveMQ V4版本退出的High Performance Journal（高性能事務支持）附件，並且同步推出了關於關係型數據庫的存儲方案。 ActiveMQ5.3版本又推出了對KahaDB的支持（5.4版本後被作為默認的持久化方案），後來ActiveMQ 5.8版本開始支持LevelDB，到現在5.9提供了標準的Zookeeper+LevelDB集群化方案。

- ActiveMQ消息持久化機制有：
  - AMQ              基於日誌文件
  - KahaDB          基於日誌文件，從ActiveMQ5.4開始默認使用
  - JDBC              基於第三方數據庫
  - Replicated LevelDB Store 從5.9開始提供了LevelDB和Zookeeper的數據複製方法，用於Master-slave方式的首選數據複製方案。

# 配置ActiveMQ集群

## 配置Zookeeper 

- 配置3台Zookeeper集群

## 集群部屬規劃

| 主機 | Zookeeper集群端口 | AMQ集群bind端口 | AMQ消息tcp端 | 管理控制台端口 | AMQ節點安裝目錄|
| ---------- | --- |--- |--- |--- |--- |
|192.168.47.129| 2191 |bind="tcp://192.168.47.129:63631"|61616|8161|/mq_cluster/mq_node01|
|192.168.47.130| 2191 |bind="tcp://192.168.47.130:63631"|61616|8161|/mq_cluster/mq_node02|
|192.168.47.131| 2191 |bind="tcp://192.168.47.131:63631"|61616|8161|/mq_cluster/mq_node03|

## 創建3台MQ集群目錄

- 分別在3台主機上創建 /opt/module/mq_cluster/mq_node1、/opt/module/mq_cluster/mq_node2、/opt/module/mq_cluster/mq_node3
- 複製apache-activemq-5.16.4，到以上3個目錄下

```
mkdir -p /opt/module/mq_cluster/mq_node
cp -r /mnt/hgfs/fileShare/apache-activemq-5.16.4 /opt/module/mq_cluster/mq_node3
```

### 修改管理控制台端口和IP

- 修改/opt/module/mq_cluster/mq_node1/conf/jetty.xml
  - 1.調整host 改成各自主機IP
  - 2.調整端口 port 改成 8181

![063](activemq/imgs/34.png)
![063](activemq/imgs/35.png)
![063](activemq/imgs/36.png)


### ActiveMQ集群配置

#### BrokerName配置

- 3台機器activemq.xml配置文件裡面的BrokerName要全部一致

![063](activemq/imgs/37.png)

#### 持久化配置

```xml
  <persistenceAdapter>
    <replicatedLevelDB
      directory="${activemq.data}/leveldb"
      replicas="3"
      bind="tcp://192.168.47.129:61616"
      zkAddress="192.168.47.129:2181,192.168.47.130:2181,192.168.47.131:2181"
      zkPassword="password" <!--有密碼再設-->
      zkPath="/activemq/leveldb-stores"
      hostname="192.168.47.129"
      />
  </persistenceAdapter>
```

![063](activemq/imgs/38.png)
![063](activemq/imgs/39.png)
![063](activemq/imgs/40.png)

### 修改各個節點的消息端口

- 修改activemq.xml port號

![063](activemq/imgs/41.png)

### 啟動

- 1.先啟動zookeeper
- 2.再依順序啟動activeMQ

### 驗證

- 登入zooleeper zkCli.sh 輸入以下指令，驗證
  
```
ls /activemq/leveldb-stores

get /activemq/leveldb-stores/00000000003

get /activemq/leveldb-stores/00000000004

get /activemq/leveldb-stores/00000000005

```
- elected 裡有值代表master，沒有的代表Slave

![063](activemq/imgs/42.png)

### 程式裡需要配置

- 集群需要使用(failover:(tcp://192.168.10.130:61616,tcp://192.168.10.132:61616,tcp://192.168.10.133:61616))配置多個ActiveMQ

### 小結

- ActiveMQ的客戶端只能訪問Master的Broker，其他處於Slave的Broker不能訪問，所以客戶端連接的Broker應該使用failover協議（失敗轉移）
 
- 當一個ActiveMQ節點掛掉或者一個Zookeeper節點掛點，ActiveMQ服務依然正常運轉，如果僅剩一個ActiveMQ節點，由於不能選舉Master，所以ActiveMQ不能正常運行。
 
- 同樣的，
如果zookeeper僅剩一個活動節點，不管ActiveMQ各節點存活，ActiveMQ也不能正常提供服務。
（ActiveMQ集群的高可用依賴於Zookeeper集群的高可用）





