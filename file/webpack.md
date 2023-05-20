# `webpack`

# webpack 五大核心

## 1.Entry

  - 1.入口，指示webpack以哪個文件為入口起點開始打包

## 2.Output

- 1.輸出,指示webpack打包後的資源bindles輸出到哪裡去，以及如何命名

## 3.Loader

- 1.Loader讓webpack能夠去處理那些非javascript文件(webpack自身只能打包javascript 跟 json)，也就是說less ，img 需要第3方Loader來幫助webpack去翻譯使webpack可以打包

## 4.Plugins

- 1.插件，可以用於執行範圍更廣的任務，包括打包優化和壓縮，移植到重新定義環境中的變量等

## 5.Mode


# webpack.config.js webpack的配置文件

- 1.作用:指示webpack幹那些活(當你運行webpack指令時，會加載裡面的配置)

