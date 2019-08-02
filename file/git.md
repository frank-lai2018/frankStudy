# 1.查詢環境所有指令

### **git config --list**

Git可以在三個地方設定環境變數，
	1. –system: 系統，git config –system –list。
	2. –global: 全域，git config –global –list。
	3. –local: repository專案，git config –local –list。

# 2.更改提交帳密指令

### **git config --global user.name "frank lai"**

### **git config --global user.email "frank@test.com.tw"**

# 3.取消設置的環境面數

### **git config --local --unset user.name**