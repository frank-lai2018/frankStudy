1.在Android中，只有在UIThread(主線程)中才能直接更新介面

2.在Android中，長時間的工作(連接WS)都需要再worker Thread(分線程)中執行

3.如何實現現成通信呢?

​		a.消息機制

​		b.非同步任務

4.Android Test 步驟

a.在模組的build.gradle上加入以下設定

```xml
android {
  // ...
  testOptions { 
    unitTests.returnDefaultValues = true
  }
}
```

```xml
dependencies {
  testCompile 'junit:junit:4.12'
  testCompile "org.mockito:mockito-core:1.9.5"
}
```

b.在src\test目錄下進行測試

EX:E:\app\APP04_SQLiteTest\app05_message\src\test\java\com\frank\app05_message\ExampleUnitTest.java



