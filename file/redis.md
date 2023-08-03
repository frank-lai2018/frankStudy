# Redis:REmote Dictionary Server(遠程字典服務器)

- Remote Dictionary Server(遠程字典服務)是完全開源的，使用ANSIC語言編寫遵守BSD協議，是一個高性能的Key-Value數據庫提供了豐富的數據結構，例如String、Hash、List、Set、SortedSet等等。數據是存在內存中的，同時Redis支持事務、持久化、LUA腳本、發布/訂閱、緩存淘汰、流技術等多種功能特性提供了主從模式、Redis Sentinel和Redis Cluster集群架構方案
- 官網 https://redis.io
- 中文 http://www.redis.cn/
- 下載 https://redis.io/download/

- redis中文文檔:https://redis.com.cn/documentation.html

- 指令:http://doc.redisfans.com/

## 版本

- Redis從發布至今，已經有十餘年的時光了，一直遵循著自己的命名規則：

- 版本號第二位如果是奇數，則為非穩定版本 如2.7、2.9、3.1

- 版本號第二位如果是偶數，則為穩定版本 如2.6、2.8、3.0、3.2

- 當前奇數版本就是下一個穩定版本的開發版本，如2.9版本是3.0版本的開發版本

- 我們可以通過redis.io官網來下載自己感興趣的版本進行源碼閱讀：

- 歷史發布版本的源碼：https://download.redis.io/releases/

## Redis7.0 部分新特性

|||
|--|--|
|多AOF文件支持|7.0 版本中一個比較大的變化就是 aof 文件由一個變成了多個，主要分為兩種類型：基本文件(base files)、增量文件(incr files)，請注意這些文件名稱是複數形式說明每一類文件不僅僅只有一個。在此之外還引入了一個清單文件(manifest) 用於跟踪文件以及文件的創建和應用順序（恢復）|
|config命令增強|對於Config Set 和Get命令，支持在一次調用過程中傳遞多個配置參數。例如，現在我們可以在執行一次Config Set命令中更改多個參數： config set maxmemory 10000001 maxmemory-clients 50% port 6399 |
|限制客戶端內存使用 Client-eviction|一旦 Redis 連接較多，再加上每個連接的內存佔用都比較大的時候， Redis總連接內存佔用可能會達到maxmemory的上限，可以增加允許限制所有客戶端的總內存使用量配置項，redis.config 中對應的配置項，兩種配置形式：指定內存大小、基於 maxmemory 的百分比。maxmemory-clients 1g maxmemory-clients 10%|
|listpack緊湊列表調整|listpack 是用來替代 ziplist 的新數據結構，在 7.0 版本已經沒有 ziplist 的配置了（6.0版本僅部分數據類型作為過渡階段在使用）listpack 已經替換了 ziplist 類似 hash-max-ziplist-entries 的配置|
|訪問安全性增強ACLV2|在redis.conf配置文件中，protected-mode默認為yes，只有當你希望你的客戶端在沒有授權的情況下可以連接到Redis server的時候可以將protected-mode設置為no|
|Redis Functions|Redis函數，一種新的通過服務端腳本擴展Redis的方式，函數與數據本身一起存儲。簡言之，redis自己要去搶奪Lua腳本的飯|
|RDB保存時間調整|將持久化文件RDB的保存規則發生了改變，尤其是時間記錄頻度變化|
|命令新增和變動|Zset (有序集合)增加 ZMPOP、BZMPOP、ZINTERCARD 等命令 Set (集合)增加 SINTERCARD 命令 LIST (列表)增加 POP、BLMPOP ，從提供的鍵名列表中的第一個非空列表鍵中彈出一個或多個元素。|
|性能資源利用率、安全、等改進|自身底層部分優化改動，Redis核心在許多方面進行了重構和改進，主動碎片整理V2：增強版主動碎片整理，合Jemalloc版本更新，更快更智能，延時更低，HyperLogLog改進：在Redis5.0中，HyperLogLog算法得到改進，優化了計數統計時的內存使用效率，7更加優秀，更好的內存統計報告，如果不為了API向後兼容，我們將不再使用slave一詞......(政治正確)|

## 安裝(linxu)

### 官網下載 

![019](redis/imgs/1.png)

### 解壓縮放進指定目錄

```
 tar -zxvf redis-7.0.12.tar.gz
```

### 使用make安裝

- 使用make安裝，並使用PREFIX指定安裝路徑
```
# 安裝
make && make PREFIX=/opt/module/redis-7.0.12/build/ install

# 移除
make PREFIX=/opt/module/redis-7.0.12/build/ uninstall
make clean
```
![019](redis/imgs/2.png)

### 設定redis.conf

- 複製原始redis.conf到 /opt/frank/redis_conf/

- redis.conf配置文件，改完後確保生效，記得重啟，記得重啟
  - 1 默認daemonize no (是否使用後台啟動)             改為  daemonize yes
  - 2 默認protected-mode  yes    改為  protected-mode no
  - 3 默認bind 127.0.0.1             改為  直接註釋掉(默認bind 127.0.0.1只能本機訪問)或改成本機IP地址，否則影響遠程IP連接
  - 4 添加redis密碼                      改為 requirepass 你自己設置的密碼

![019](redis/imgs/4.png)
![019](redis/imgs/5.png)
![019](redis/imgs/6.png)
![019](redis/imgs/7.png)

## 啟動

- 到安裝目錄裡 /opt/module/redis-7.0.12/build/
- 執行 

```
./redis-server /home/frank/redis_config/redis.conf
```
- 使用redis-cli 連線redis
- 使用auth 輸入密碼

![019](redis/imgs/8.png)


## 停止

- 單實例關閉：redis-cli -a 1234 shutdown
- 多實例關閉，指定端口關閉:redis-cli -p 6379 shutdown
- 在redis-cli 裡使用 SHUTDOWN也可以


## 移除Redis

- 停止redis server
- 刪除安裝目錄底下的redis相關文件即可

# 10大數據類型

![019](redis/imgs/9.png)

## String

- string是redis最基本的類型，一個key對應一個value。
 
- string類型是二進制安全的，意思是redis的string可以包含任何數據，比如jpg圖片或者序列化的對象 。
 
- string類型是Redis最基本的數據類型，一個redis中字符串value最多可以是512M

## List列表

- Redis列表是簡單的字符串列表，按照插入順序排序。你可以添加一個元素到列表的頭部（左邊）或者尾部（右邊）
- 它的底層實際是個雙端鍊錶，最多可以包含 2^32 - 1 個元素 (4294967295, 每個列表超過40億個元素)

## Hash哈希表

- Redis hash 是一個 string 類型的 field（字段） 和 value（值） 的映射表，hash 特別適合用於存儲對象。
 
- Redis 中每個 hash 可以存儲 2^32 - 1 鍵值對（40多億）

## Set集合

- Redis 的 Set 是 String 類型的無序集合。集合成員是唯一的，這就意味著集合中不能出現重複的數據，集合對象的編碼可以是 intset 或者 hashtable。
 
- Redis 中Set集合是通過哈希表實現的，所以添加，刪除，查找的複雜度都是 O(1)。
 
- 集合中最大的成員數為 2^32 - 1 (4294967295, 每個集合可存儲40多億個成員)

## ZSet有序集合

- Redis 的 Set 是 String 類型的無序集合。集合成員是唯一的，這就意味著集合中不能出現重複的數據，集合對象的編碼可以是 intset 或者 hashtable。
 
- Redis 中Set集合是通過哈希表實現的，所以添加，刪除，查找的複雜度都是 O(1)。
 
- 集合中最大的成員數為 2^32 - 1 (4294967295, 每個集合可存儲40多億個成員)

## GEO地理空間

- Redis GEO 主要用於存儲地理位置信息，並對存儲的信息進行操作，包括

- 添加地理位置的坐標。
- 獲取地理位置的坐標。
- 計算兩個位置之間的距離。
- 根據用戶給定的經緯度坐標來獲取指定範圍內的地理位置集合

## HyperLogLog基數統計

- HyperLogLog 是用來做基數統計的算法，HyperLogLog 的優點是，在輸入元素的數量或者體積非常非常大時，計算基數所需的空間總是固定且是很小的。
 
- 在 Redis 裡面，每個 HyperLogLog 鍵只需要花費 12 KB 內存，就可以計算接近 2^64 個不同元素的基 數。這和計算基數時，元素越多耗費內存就越多的集合形成鮮明對比。
 
- 但是，因為 HyperLogLog 只會根據輸入元素來計算基數，而不會儲存輸入元素本身，所以 HyperLogLog 不能像集合那樣，返回輸入的各個元素。

## bitmap

由0和1狀態表現的二進制位的bit數組

## bitfield

- 通過bitfield命令可以一次性操作多個比特位域(指的是連續的多個比特位)，它會執行一系列操作並返回一個響應數組，這個數組中的元素對應參數列表中的相應操作的執行結果。
 
- 說白了就是通過bitfield命令我們可以一次性對多個比特位域進行操作。

## Stream

- Redis Stream 是 Redis 5.0 版本新增加的數據結構。

- Redis Stream 主要用於消息隊列（MQ，Message Queue），Redis 本身是有一個Redis 發布訂閱(pub/sub) 來實現消息隊列的功能，但它有個缺點就是消息無法持久化，如果出現網絡斷開、Redis 宕機等，消息就會被丟棄。

- 簡單來說發布訂閱 (pub/sub) 可以分發消息，但無法記錄歷史消息。

- 而 Redis Stream 提供了消息的持久化和主備複製功能，可以讓任何客戶端訪問任何時刻的數據，並且能記住每一個客戶端的訪問位置，還能保證消息不丟失