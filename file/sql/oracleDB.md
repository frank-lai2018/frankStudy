# 日期查詢


* DATE - 格式：YYYY-MM-DD
* TIMESTAMP - 格式：YYYY-MM-DD HH:MM:SS

```sql

select * from [某張表] t

where t.XXX_DATE_TIME > to_date('2020-06-29','YYYY-MM-DD');


select * from [某張表] t

where t.XXX_DATE_TIME>to_date('2020-06-29 00:00:00','YYYY-MM-DD HH24:mi:ss');


```

* to_char

```sql
select * from [某張表] t

where to_char(t.SCAN_DATE_TIME,'yyyy-mm-dd')='2020-06-29';
```

* 查詢當天數據

```sql
select * from [某張表] t

where trunc(t.XXXX_DATE_TIME)=date'2020-06-29';


select * from [某張表] t

where t.XXX_DATE_TIME between to_date('2020-06-29 00:00:00','YYYY-MM-DD HH24:mi:ss')

and to_date('2020-06-29 23:59:59','YYYY-MM-DD HH24:mi:ss');


```