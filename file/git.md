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



# ４.初次設定

```console
$ git config --global user.name "Frank Lai"
$ git config --global user.email jianhanlai@gmail.com
```

# 5.建立或刪除遠端branch

建立遠端branch跟關聯
git push --set-upstream origin branchName

刪除遠端分支
git push origin :branchName

git branch -a #查看所有分支(包含遠端)
git branch -r #查看遠端分支 
git branch -vv #查看本地分支所關聯的遠端分支

git branch -m old_branch new_branch #修改分支名子

