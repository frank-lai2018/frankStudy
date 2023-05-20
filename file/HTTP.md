# HTTP
HTTP（hypertext transport protocol）協議『超文本傳輸協議』，協議詳細規定了瀏覽器和網際網路服務器之間互相通信的規則。

## 请求报文
```
行      POST  URL  HTTP/1.1 
頭      Host: yahoo.com
        Cookie: name=yahoo
        Content-type: application/x-www-form-urlencoded
        User-Agent: chrome 83
空行
體      username=admin&password=admin
```

## 响应报文
```
行      HTTP/1.1  200  OK
頭      Content-Type: text/html;charset=utf-8
        Content-length: 2048
        Content-encoding: gzip
空行    
體      <html>
            <head>
            </head>
            <body>
                <h1>HTTP</h1>
            </body>
        </html>
```
* 404:找不到網頁
* 403:被禁止
* 401:沒有權限
* 500:系統內部錯誤
* 200:成功