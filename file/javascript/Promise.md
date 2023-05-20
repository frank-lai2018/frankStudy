# Promise

## 特點

- 1.物件的狀態不受外界影響。 Promise 物件代表一個異步操作，有三種狀
態： pending （進行中）、 fulfilled or resolved（已成功）和 rejected （已失敗）。只有異步操作的結果，可以決定當前是哪一種狀態，任何其他操作都無法改變這個狀態。
- 2.一旦狀態改變，就不會再變，任何時候都可以得到這個結果。 Promise 物件的狀態改變，只有兩種可能：從 pending 變為 fulfilled 和從 pending 變
為 rejected 。只要這兩種情況發生，狀態就凝固了，不會再變了，會一直保持這個結果，這時就稱為 resolved（已定型）。如果改變已經發生了，你再
對 Promise 物件添加回調函數，也會立即得到這個結果。這與事件（Event）完全不同，事件的特點是，如果你錯過了它，再去監聽，是得不到結果的。
- 3.Promise物件一創建，構造函數裡面的函數會立即執行

## 缺點

- 1.無法取消 Promise ，一旦新建它就會立即執行，無法中途取消。
- 2.如果不設置回調函數(resolve、reject)， Promise 內部拋出的錯誤，不
會反應到外部。
- 3.當處於 pending 狀態時，無法得知目前進展到哪一個階段（剛剛開始還是即將完成）。

## 基本用法

- 1.執行成功回調函數(resolve())，PromiseState將被改成fulfilled
- 1.執行失敗回掉函數(reject())，PromiseState將被改成rejected

```javascript
var promise = new Promise(function(resolve, reject) {
    // ... some code
if (/* 異步操作成功 */){
    resolve(value);//執行成功回調函數，PromiseState將被改成
} else {
    reject(error);//執行失敗回掉函數，PromiseState將被改成rejected
}
});
```

## then的基本用法

- 1.then 方法可以接受兩個回調函數作為參數。
  - a.第一個回調函數是 Promise 物件的狀態變為 resolved 時調用，
  - b.第二個回調函數是 Promise 物件的狀態變為 rejected 時調用。此函數是可選的，不一定要提供。
  - c.這兩個函數都接受 Promise 做為回傳值
  - d.Promise 實例的狀態變為 resolved或rejected，就會觸發 then 方法綁定的對應回調函數(resolved狀態觸發第一個參數，rejected狀態觸發第2個參數)。

```javascript
    promise.then(function(value) {
    // success
    }, function(error) {
    // failure
    });
```
example:
```javascript
function timeout(ms) {
    return new Promise((resolve, reject) => {
    setTimeout(resolve, ms, 'done');
    });
}

timeout(100).then((value) => {
    console.log(value);
});
```

## `狀態改變`

### 只有以下兩種狀態的改變
- 1.pading ---> fulfilled(resolved)
- 2.pading ---> rejected


## Promise.prototype.then

- 1.promise.then()返回的新promise的結果狀態由什麼決定?
    - (1)簡單表達: 由then()指定的回調函數執行的結果決定
    - (2)詳細表達:
        - ①如果拋出異常, 新promise變為rejected, reason為拋出的異常
        - ②如果返回的是非promise的任意值(例如:1,2,'123'...), 會被自動封裝成新的會被自動封裝成新的promise,新promise變為resolved新promise變為resolved, value為返回的值
        - ③如果返回的是另一個新promise, 此promise的結果就會成為新promise的結果


```javascript
    new Promise((resolve, reject) => {
        setTimeout(() => {
            console.log('promise1 異步任執行中..');
            resolve(1)
        }, 2000)
    }).then(
        value => {
            console.log('promise1 異步任執行結果', value)

            //狀態改為resolve時，所調用的回調函數，執行同步處理並返回非Promise值，
            // 此時會被包裝成，成功的Promise實力物件值為 'promise2 同步任執行結果' 往下傳
            return 'promise2 同步任執行結果'
        }
    ).then(
        value => {
            console.log('promise2 同步任執行結果', value)
            console.log('promise3 同步任執行中...')
            //如果回調函數沒有回傳值，此函數的執行結果為undifined，
            // 因回傳為非Promise物件，故也會被包裝成，成功的Promise實力物件，值為undifined往下傳
        }
    ).then(
        value => {
            console.log('promise3 同步任執行結果', value)
            //如果還需要執行異步任務，需包裝成Promise實例物件，這樣下一個then才會等此異步任務節行結束
            console.log('promise4 異步任執行中....');
            
            //沒有封裝成Promise物件的異步任務，下一個then無法等待他執行結束，並取得結果
            // setTimeout(() => {
            //     console.log('setTimeout....');
            //     return 4
            // }, 5000)

            return new Promise((resolve, reject) => {
                setTimeout(() => {
                    resolve(4)
                }, 5000)
            })
        }
    ).then(value => {
        console.log('5執行中...');
        console.log('promise4 異步任執行結果', value);
    })
```

### Promise.all

- 語法:
    - 1

```

```