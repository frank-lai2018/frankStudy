1.查詢檔案大小

```
ls -al --block-size=M 檔案大小改用M顯示 
ls -alt 新到舊
ls -alrt 舊到新

```

2.查找檔案中關鍵字

```
find .|xargs grep -ri "app_notification_rec_log" -l 


find . -name "*.java" |xargs grep -ri "APP_NOTIFICATION_REC_LOG" -l

```

3.dos2unix 多個檔案轉換

```
find . -name "*.sh" | xargs dos2unix
```
