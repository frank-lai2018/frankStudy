# 1.遇到跨域問題

a.瀏覽器出現以下錯誤，代表請求遇到跨域問題
![041](images/pic041.png)

b.處理方式，在server端的response 加入以下header，即可

```java
    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
       response.setHeader("Access-Control-Allow-Origin","http://localhost:52330");//加入請求端的域名
//       response.setHeader("Access-Control-Allow-Origin","*");//不管域名
    }
```

c.Android處理方式

Android的WebView處理跨域問題很簡單，只要將AllowUniversalAccessFromFileURLs設置為True就行了

```java
WebSettings webSetting = mWebView.getSettings();
webSetting.setAllowUniversalAccessFromFileURLs(true);

```

# 2.手機APP遇到談窗後，回上一頁無法滑動問題

- 網頁版回上一頁也會遇到

- 通常是有在body加上某個class,原來在關掉這個談窗時會去移除這個class,但按了回上一頁，沒有觸發刪除這個class的動作

處理方式:

## Vue

### 方法一:

- 在beforeDestroy生命週期鉤子裡，移除這個class

```js
  beforeDestroy() {
      $('body').removeClass('noscroll')
  }

```
### 方法二:



# 3.使用vue 遇到手機無法放大縮小問題

- 主要原因為index.html頁的meta標籤設定，不能放大縮小

```html
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" id="experience_scale" />
```

## 解決方式

### 全開

- 把meta 標籤內 user-scalable 屬性設成yes

```html
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes" id="experience_scale1" />
```

### 單獨某頁可以縮小放大

- 以Vue為例，在created或者mounted 生命週期鉤子哩，打開user-scalable

```js
    let scaleElement = document.getElementById('experience_scale')
    if (scaleElement) {
        scaleElement.content = 'width=device-width, initial-scale=1, user-scalable=yes'
    }
```

- 在生命週期結束後，再改回來 ，beforeDestroy生命週期鉤子中執行

```js
    let scaleElement = document.getElementById('experience_scale')
    if (scaleElement) {
      scaleElement.content = 'width=device-width, initial-scale=1, user-scalable=no'
    }
```

