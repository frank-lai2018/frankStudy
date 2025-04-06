# Docker

## 原理

![5](imgs/5.png)

Docker是一個Client-Server結構的系統，Docker守護程序運行在主機上， 然後透過Socket連接從客戶端訪問，守護程序從客戶端接受命令並管理運行在主機上的容器。 容器，是一個運行時環境，就是我們前面說到的貨櫃。

![5](imgs/6.png)



- Docker 是一個 C/S 模式的架構，後端是一個松耦合架構，眾多模組各司其職。

Docker運行的基本流程為：

- 使用者是使用Docker Client與Docker Daemon建立通信，並發送請求給後者。
- Docker Daemon作為Docker架構中的主體部分，首先提供Docker Server的功能使其可以接受Docker Client的請求。
- Docker Engine執行Docker內部的一系列工作，每一項工作都是以一個Job的形式的存在。
- 在Job的運作過程中，當需要容器鏡像時，則從Docker Registry下載鏡像，並透過鏡像管理驅動Graph driver將下載鏡像以Graph的形式儲存。
- 當需要為Docker建立網路環境時，透過網路管理驅動Network driver建立並配置Docker容器網路環境。
- 當需要限制Docker容器運行資源或執行使用者指令等操作時，則透過Exec driver來完成。
- Libcontainer是一項獨立的容器管理包，Network driver以及Exec driver都是透過Libcontainer來實現具體對容器進行的操作。


## [Docker安裝](install.md) 

## [Docker指令](command.md) 



## Docker鏡像

- 是什麼：鏡像是一種輕量級、可執行的獨立軟體包，它包含運行某個軟體所需的所有內容，我們把應用程式和配置依賴打包好形成一個可交付的運行環境(包括程式碼、運行時需要的庫、環境變量和配置文件等)，這個打包好的運行環境就是image鏡像檔。

- 分層的鏡像：UnionFS（聯合檔案系統），Union檔案系統（UnionFS）是一種分層、輕量級且高效能的檔案系統，它支援對檔案系統的修改作為一次提交來一層層的疊加，同時可以將不同目錄掛載到同一個虛擬檔案系統下（unite several directories into a single virttosystemsingle）。 Union 檔案系統是 Docker鏡像的基礎。鏡像可以透過分層來進行繼承，基於基研鏡像（沒有父鏡像），可以製作各和具體的應用鏡像。

- 特性：一次同時載入多個檔案系統，但從外面看起來，只能看到一個檔案系統，聯合載入會把各層檔案系統疊加起來，這樣最終的檔案系統會包含所有底層的檔案和目錄

- Docker鏡像載入原理：docker的鏡像其實是由一層一層的檔案系統所組成，這種層級的檔案系統UnionFS。

- bootfs(boot file system)主要包含bootloader和kernel, bootloader主要是引導載入kernel，Linux剛啟動時會載入bootfs檔案系統，在Docker鏡像的最底層是引導檔案系統bootfs。這一層與我們典型的Linux/unix系統是一樣的，包含boot載入器和核心。當boot載入完成之後整個內核就都在記憶體中了，此時記憶體的使用權已由bootfs轉交給內核，此時系統也會卸載bootfs。

- rootfs (root file system)，在bootfs之上，包含的就是典型 Linux 系統中的/dev，/proc，/bin，/etc等標準目錄和檔案。 rootfs就是各種不同的作業系統發行版，像是Ubuntu，Centos。

![8](imgs/8.png)

- 對於一個精簡的OS，rootfs可以很小，只需要包含最基本的指令、工具和程式庫就可以了，因為底層直接用Host和Kernel，自己只需要提供rootfs就行了。由此可見對於不同的linux發行版，bootfs基本上是一致的，rootfs會有差別，因此不同的發行版可以公用bootfs。

- ❓ 為什麼Docker鏡像要採用分層結構？

  - 鏡像分層最大的一個好處就是共享資源，方便複製遷移，就是為了重複使用。

  - 比方說有多個鏡像都從相同的 base 鏡像建置而來，那麼 Docker Host 只需在磁碟上保存一份 base 鏡像；同時記憶體中也只需載入一份 base 鏡像，就可以為所有容器服務了。而且鏡像的每一層都可以被分享。


- Docker鏡像層都是唯讀的，容器層是可寫入的
- 當容器啟動時，一個新的可寫入層被載入到鏡像的頂部。這一層通常被稱為“容器層"，“容器層"之下的都叫“鏡像層”

- Docker中的鏡像分層，支援透過擴充現有鏡像，建立新的鏡像。類似Java繼承於一個Base基礎類，自己再按需擴展。新鏡像是從 base 鏡像一層一層疊加產生的。每安裝一個軟體，就在現有鏡像的基礎上增加一層。
![1](imgs/10.png)

### 製作Docker鏡像

**docker commit -m="提交的描述資訊" -a="作者" 容器ID 要建立的目標鏡像名稱[:標籤名]**

  - docker commit 提交容器副本，使其成為一個新的鏡像
  
- Docker鏡像commit作業案例

![9](imgs/9.png)

## 提交遠端流程

![1](imgs/11.png)



## 安裝軟體實例

### 安裝Tomcat

### 安裝mysql 5.7

```
docker run -d -p 3306:3306 --privileged=true -v 'G:\docker\mysql\log':/var/log/mysql -v 'G:\docker\mysql\data':/var/lib/mysql -v 'G:\docker\mysql\conf':/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=123456  --name mysql mysql:5.7
```

### 安裝redis 6.0.8

```
docker run  -p 6379:6379 --name myr3 --privileged=true -v 'G:\docker\redis\redis.conf':/etc/redis/redis.conf -v 'G:\docker\redis\data':/data -d redis:6.0.8 redis-server /etc/redis/redis.conf
```