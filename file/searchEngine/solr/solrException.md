# 當Solr重啟發生以下異常

![215](imgs/215.png)
![216](imgs/216.png)

## 原因:可能是Solr正在做同步更新正在寫入index，servere掛掉，或者重啟，導致正在寫入的不個檔案損毀或者遺失

## 除理方式:

* 把有問題index砍掉(刪除下圖中 index(index_xxxxxxxx) 跟 tlog 資料夾)，重啟server

![217](imgs/217.png)