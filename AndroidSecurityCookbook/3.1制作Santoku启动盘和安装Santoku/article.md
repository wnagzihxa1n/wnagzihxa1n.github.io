# 制作Santoku启动盘和安装Santoku

**Author：wnagzihxa1n
Mail：tudouboom@163.com**

## 0x00 前言
Santoku好像不同版本安装时基于的系统不一样，以前是Debian，后来是Ubuntu

我在不同的地方看到的安装文章都不是很统一

所以还是按照官网的说明来，第一个是Santoku的种子，第二个是安装Santoku，第三个是安装VMware Tools
- [santoku_0.5.iso.torrent](https://jaist.dl.sourceforge.net/project/santoku/Torrents/santoku_0.5.iso.torrent)
- [HOWTO install Santoku in a virtual machine](http://santoku-linux.com/howto/installing-santoku/installing-santoku-in-a-virtual-machine/)
- [HOWTO install VMWare tools on Santoku Linux](http://santoku-linux.com/howto/installing-santoku/howto-install-vmware-tools-on-santoku-linux/)

另外官网在最前强调了几点
- Santoku Linux .ISO file
	- Note: Santoku Linux versions 0.4 and later are 64-bit and require 64-bit hardware to run
- Virtual Box or VMWare Player
- A host machine with a minimum dual-core processor, 2GB RAM, and 40 GB free hard drive space or larger recommended


简单翻译一下
- Santoku的`.iso`安装包
	- 注意：Santoku 0.4及以后的版本是64位的，需要64位的机子作为硬件支持
- Virtual Box或者VMWare Player
- 宿主机至少需要：双核处理器，2GB的内存和40G空余的硬盘容量


## 0x01 安装步骤
新建虚拟机，选择自定义模式，然后点击下一步

![](Image/1.png)

点击下一步

![](Image/2.png)

选择第三个`稍后安装操作系统`

![](Image/3.png)

这里的选择得根据官网提示，如果Santoku是0.4及以后的，比如我目前下载到的是0.5，所以需要选择`Ubuntu 64位`
```
Note: For Santoku Linux 0.4 and newer, select “Ubuntu 64-bit”. 
```

![](Image/4.png)

给两个处理，毕竟我这有四核，大家根据自己机子的情况看着给

![](Image/5.png)

我的机子是8G内存，给4G，官网已经友情提示了如果想在Santoku里跑模拟器就至少得给4G的内存
```
Select an appropriate amount of memory for the VM. 512MB is standard, however increasing the memory size will typically make your VM run faster (but your host machine slower). If you’re going to use the Android Virtual Device Manager (AVD) and Android device Emulator frequently, we recommend selecting at least 4 GB of memory.
```

![](Image/6.png)

这里网络连接的方式默认即可

![](Image/7.png)

默认

![](Image/8.png)

默认

![](Image/9.png)

选择创建新磁盘

![](Image/10.png)

给60G，选择`将虚拟磁盘存储为单个文件`

![](Image/11.png)

默认，将来在赋值虚拟机文件夹的时候，如果需要重新打开，就是选择的这个文件

![](Image/12.png)

点击`自定义硬件`

![](Image/13.png)

点击`新 CD/DVD`，看右边，先选择`使用ISO镜像文件`，再点击`浏览`，选择我们下载的Santoku镜像文件，最后点击右下角的关闭

![](Image/14.png)

基本的配置完成后，回到主界面，点击`开启虚拟机`

![](Image/15.png)

选择第三项，按回车进行安装

![](Image/16.png)

进入语言选择，我这里选择英语

![](Image/17.png)

下面有两个选项，全都不勾选，安装过程中更新以及安装其它的软件会很慢

![](Image/18.png)

选择第一个，作用是抹掉分配的硬盘空间所有数据

![](Image/19.png)

最开始定位到了`Harbin`，我就不能理解了，手动回到`Shanghai`

![](Image/20.png)

键盘的选择默认即可，如果语言选择的是`简体中文`，那么这里应该是中文的键盘

![](Image/21.png)

用户名，密码什么什么的

![](Image/22.png)

然后漫长的安装过程，等等等等。。。。。。

![](Image/23.png)

安装完后弹出登录框，按照设置的密码输入即可登录，然后点击`虚拟机->安装VMware Tools`

![](Image/24.png)

弹出一个窗口，点击`OK`，接着会弹出一个文件夹窗口，如图直接拷贝这个压缩文件到主目录下

![](Image/25.png)

为了方便，我先切换到Root用户，因为是新装的系统，需要设置Root用户密码，然后输入密码，再输入两次密码即可
```
wnagzihxa1n@Santoku:~$ sudo passwd
```

切换到Root用户
```
wnagzihxa1n@Santoku:~$ su
Password:
root@Santoku:/home/wnagzihxa1n/# 
```

想解压的话，得先给权限
```
root@Santoku:/home/wnagzihxa1n/# chmod 777 VMwareTools-10.1.6-5214329.tar.gz
```

然后再解压
```
root@Santoku:/home/wnagzihxa1n/# tar -zxvf VMwareTools-10.1.6-5214329.tar.gz
```

最后安装即可
```
root@Santoku:/home/wnagzihxa1n/# cd vmware-tools-distrip
root@Santoku:/home/wnagzihxa1n/vmware-tools-distrip# ./vmware-install.pl
```

![](Image/26.png)

执行安装脚本的时候，出现一个询问，输入`y`
```
Do you still want to proceed with this installation?[no]
```

然后一路回车，最后安装完成，重启

![](Image/27.png)

## 0x02 工具介绍
最关键的工具在`Santoku`路径下

![](Image/28.png)

简单的整理了一波，这是Santoku 0.5的工具集，直观的看，缺少了JEB，如果有需要可以自己补上
- Development Tools(开发工具)
	- Android SDK Manager(Android SDK管理工具)
	- Android Studio(目前开发安卓主流的工具)
	- AXMLPrinter 2(解析AndroidManifest.xml文件的工具)
	- Eclipse(Java开发工具，集成ADT版本)
	- Fastboot(刷机用的)
	- Google Play API
	- Heimdall(刷机工具)
	- Heimdall(GUI)(刷机工具界面版)
	- SBF Flash(这个工具没有用过，看了官网，好像是第三方开发者做的)
- Device forensics(设备取证)
	- AF Logical OSE
	- Android Brute Force Encryption
	- ExifTool
	- iOS Backup Analyzer 2
	- libimobiledevice
	- scalpel
	- SleuthKit
	- Yaffey
- Penetration Testing(渗透测试)
	- Burp Suite(渗透时强大的抓包工具)
	- Ettercap(我只在中间人攻击中用过)
	- nmap(强大的嗅探工具)
	- SSLStrip(配合Ettercap突破SSL加密的协议)
	- w3af(Console)(Web扫描框架-控制台版本)
	- w3af(GUI)(Web扫描框架-界面版)
	- ZAP(Zed Attack Proxy，主动扫描很强大)
	- Zenmap(as root)(NMap的界面化工具)
- Reverse Engineering(逆向工程)
	- Androguard(静态分析蛮厉害的)
	- AntiLVL(这啥玩意我真的没用过)
	- APKTool(工具集)
	- Baksmali(把Dex文件转为smali文件)
	- Bulb Security SPF()
	- dex2jar(这其实是一个组合套件，早期逆向的工具集合)
	- Drozer(Android应用安全评估套件)
	- Jasmin(反编译Java)
	- JD-GUI(dex2jar套件把Dex文件转为Jar文件，JD-GUI可以对Jar文件进行分析)
	- Procyon(反编译Java)
	- radare2(开源的一个逆向平台)
	- Smali(把smali文件转为Dex文件)
- Wireless Analyzers(无线分析)
	- Chaosreader()
	- dnschef(DNS欺骗的工具)
	- DSniff(嗅探数据的工具)
	- mitmproxy(中间人攻击)
	- tcpdump(抓包)
	- wifite(破解WiFi)
	- Wireshark(数据包捕获加分析)

## 0x03 更新SDK
这个其实没有太大更新的必要，因为以前搞开发，所以随着谷歌的更新，4到7的SDK就全都下了，70个G。。。。。。

这里只是简单地跑个模拟器而已，默认安装的那些就行了

![](Image/29.png)

## 0x04 Android Studio
本来想看一下Santoku自带的Android Studio玩起来感觉咋样，结果是不咋样

需要连VPN这里才能完成，不过，吃饱撑着才在虚拟机里开Android Studio，而且这里的Java是OpenJDK，Android Studio需要的是Oracle JDK

![](Image/30.png)

所以这里我就不管了，反正开发这种活通常宿主机干

## 0x05 小结
JEB和Jadx这些工具不打算在Santoku里装了，这种静态分析在宿主机搞搞就好，其实在Ubuntu里使用JEB的体验蛮差的

