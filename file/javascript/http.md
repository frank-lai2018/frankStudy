# 瀏覽器發送http post 請求
```javascript
// let XMLHttpRequest = require('XMLHttpRequest').XMLHttpRequest
const http = new XMLHttpRequest()
let url = 'https://www.payeasy.com.tw/BEShopWebServices/api/redeem/rememberme'
let params = 'brdFullCode=FveqAnqeaye2&bpNum=15'
http.open('POST', url, true)


//Send the proper header information along with the request
http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded')
http.setRequestHeader('User-Agent', 'Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)')
http.setRequestHeader(
  'Cookie',
  'be_shop_redeem_remember-UnuiizmQzQz2=VW51aWl6bVF6UXoyOjM3NTk4MDE3MDAwMjQ6YmMwYjFiNjkxNjVlNzE4OGUwMjE2YmQ3YThmMjJhMjI; be_shop_redeem_remember-FveqAnqeaye2=RnZlcUFucWVheWUyOjM3NTk4MDE3MTA0MzI6YzA5OWQ0ZDQ2Yzk3ODVmOTQ1ZmU1NjY4MzdhMTkzOTE; _ga=GA1.3.666543499.1612317961; _gid=GA1.3.1567578632.1612317961'
)

http.onreadystatechange = function() {
  //Call a function when the state changes.
  if (http.readyState == 4 && http.status == 200) {
    console.log(http.responseText)
  }
}
http.send(params)

```