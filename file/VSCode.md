# 設定自動格式化

在編輯器的settings.json 加上以下這段

```
{
    "eslint.alwaysShowStatus": true,
    "eslint.format.enable": true,
    "editor.codeActionsOnSave": {
        "source.fixAll.eslint": true
    },
    "editor.defaultFormatter": "dbaeumer.vscode-eslint",
    "editor.formatOnSave": true
}
```
![055](images/pic055.png)

# 更改 terminal fontSize

在 setting.json 加上 `terminal.integrated.fontSize : 24` 即可
```
{
    "editor.fontSize": 24,
    "workbench.colorTheme": "Default Light+",
    "terminal.integrated.fontSize":24
}
```

