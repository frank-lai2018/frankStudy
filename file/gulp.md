#   1.前置作業

*   安裝:
    *  安裝 nodejs, 查看版本: node -v (注意:要使用gulp3.x版本，node 不能使用12以上的版本，需安裝11.15.0)
    *    npm install gulp -g
    *     npm install gulp --save-dev

* 安裝gulp:
    * 全局安裝gulp

    ```
    npm install gulp -g
    ```

*    * 局部安裝gulp

    ```
    npm install gulp --save-dev

    ```

*    * 配置編碼: gulpfile.js
    ```javascript
    //引入gulp模塊
    var gulp = require('gulp');
    //定義默認任務
    gulp.task('任務名', function() {
      // 將你的任務的任務代碼放在這
    });
    gulp.task('default', ['任務'])//異步執行
    ```
*    * 構建命令:
    ```
    gulp
    ```
* 使用gulp插件
*    * 相關插件:
*    *    * gulp-concat : 合併文件(js/css)
*    *    * gulp-uglify : 壓縮js文件
*    *    * gulp-rename : 文件重命名
*    *    * gulp-less : 編譯less
*    *    * gulp-clean-css : 壓縮css
*    *    * gulp-livereload : 實時自動編譯刷新
*    * 重要API
*    *    * gulp.src(filePath/pathArr) :
*    *    *    * 指向指定路徑的所有文件, 返回文件流對象
*    *    *    * 用於讀取文件
*    *    * gulp.dest(dirPath/pathArr)
*    *    *    * 指向指定的所有文件夾
*    *    *    * 用於向文件夾中輸出文件
*    *    * gulp.task(name, [deps], fn)
*    *    *    * 定義一個任務
*    *    * gulp.watch()
*    *    *    * 監視文件的變化

# 2.設置合併文件及壓縮

*   js合併壓縮
