# Linux環境安裝JDK

## 1.下載linux版本JDK，並放進linux環境底下

### 下載open jdk網址 https://jdk.java.net/

## 2.解壓縮JDK

```
tar -zxvf openjdk-11+28_linux-x64_bin.tar.gz
```

## 3.配置環境變數

### 環境變數檔案 /etc/profile

### 一般來說都放在 /usr/local 底下

在最後加上以下兩行
```
export JAVA_HOME=/usr/local/java/jdk-11
export PATH=$JAVA_HOME/bin:$PATH

```

## 4.重新讀取環境變數檔案

```
source /etc/profile
```