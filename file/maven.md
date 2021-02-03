# 1.私服 nexus 安裝指令

```
nexus.exe /install nexus
nexus.exe /start nexus
```

# 2.常見錯誤

a.出現以下錯誤時

![019](images/pic019.png)

### **處理方式:**

​		**在Deployment Assembly 加入MAVEN Dependencies**

![020](images/pic020.png)

![021](images/pic021.png)

# 3. 設定JDK version

```
<plugins>
    <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
            <source>1.8</source>
            <target>1.8</target>
        </configuration>
    </plugin>
</plugins>
```