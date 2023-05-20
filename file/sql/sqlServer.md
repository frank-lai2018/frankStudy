# 增加 index

```sql
CREATE INDEX index名稱
ON [dbo].[表格] ({需要添加的欄位}})WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON);

```

#select insert

這種語法，交易LOG會最少

```sql
select value1,value2.... into {新表格名} from {舊表格} where 條件
```