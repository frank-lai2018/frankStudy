# 1.判斷查詢出來值為NULL顯示的值

a.NVL(expr1, expr2)函数

*expr1 為NULL，返回expr2；不為NULL，返回expr1*

b.NVL2(expr1, expr2, expr3)函数

如果 expr1不是NULL 顯示 expr2 如果是的話顯示expr3

c.NULLIF (expr1, expr2) 函数

*expr1和expr2相等返回NULL，否則返回expr1*

```

```

# 2.分頁查詢

需要加上ROWNUM計算資料筆數，在控制ROWNUM顯示的資訊，來達到分頁效果


```html
SELECT * FROM(
       SELECT ROWNUM aa,PE_EXTERNAL_PROD.* 
       FROM PE_EXTERNAL_PROD) 
WHERE aa<=10
AND aa BETWEEN 2 AND 11
```