# ubuntu

1.apt

* sudo apt install 名稱 ==>安裝軟件

* sudo apt remove 名稱 ==>移除軟件

* sudo apt update ==> 更新可用軟件包列表(只檢查可用更新，不會實際安裝更新)

* sudo apt upgrade ==>更新已安裝的包(實際安裝更新)

2.安裝 ssh server

* 1. sudo apt-get install openssh-server
* 2.Enable the ssh service by typing sudo systemctl enable ssh
* 3. Start the ssh service by typing sudo systemctl start ssh