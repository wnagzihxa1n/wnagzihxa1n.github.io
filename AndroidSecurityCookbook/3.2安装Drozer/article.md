# 安装Drozer

**Author：wnagzihxa1n
Mail：tudouboom@163.com**

## 0x00 前言
Drozer是一个Android软件漏洞测试和利用框架，非常好用

## 0x01 Drozer Introduce
Official Website
- https://labs.mwrinfosecurity.com/tools/drozer/

Github Repo
- https://github.com/mwrlabs/drozer

## 0x02 Drozer Installing & Usage
### 2.1 Building from Source
```
git clone https://github.com/mwrlabs/drozer/
cd drozer
make apks
source ENVIRONMENT
python setup.py build
sudo env "PYTHONPATH=$PYTHONPATH:$(pwd)/src" python setup.py install
```

### 2.2 Installing .egg
```
sudo easy_install drozer-2.x.x-py2.7.egg
```

### 2.3 Building for Debian/Ubuntu
```
sudo apt-get install python-stdeb fakeroot
git clone https://github.com/mwrlabs/drozer/
cd drozer
make apks
source ENVIRONMENT
python setup.py --command-packages=stdeb.command bdist_deb
```

### 2.4 Installing .deb (Debian/Ubuntu)
```
sudo dpkg -i deb_dist/drozer-2.x.x.deb
```

### 2.5 Arch Linux
```
yaourt -S drozer
```

Santoku默认安装Drozer，我们可以查看一下版本
```
wnagzihxa1n@Santoku:~$ drozer console version
drozer 2.3.3
```

官网上最新的版本是`drozer v2.3.4`

我还真不知道怎么更新。。。。。。

使用的话，需要先安装`drozer-agent-2.x.x.apk`

drozer-agent-2.3.4.apk
- https://github.com/mwrlabs/drozer/releases/download/2.3.4/drozer-agent-2.3.4.apk

科学上网保平安

那么就是说，需要在PC上安装Drozer，在安卓机上装`drozer-agent-2.x.x.apk`，然后通过端口进行控制和数据交互

## 0x03 小结
Have Fun:)