---
layout: post
title:  "2017 XCTF&NJCTF - LittleRotatorGame-200"
date:   2017-03-16 18:00:00 +520
categories: Android_CTF
---

2017XCTF南京站线上赛同时也是NJCTF的Mobile题，一度做的我怀疑人生，题目可以在XCTF OJ上面找到
- http://oj.xctf.org.cn/practice/defensetrain/5584ce8300114ceabd14f1fdea1669cd/

这是一个纯native的APP，拿到手后看到没有Java层的Activity，当时就把我吓蒙了，上一次看到没有Activity的APP还是Android 4.x的时候，当时允许无Activity跑应用，于是就有好多病毒利用这点搞啊搞......

题目要继续做啊，临时到谷歌爸爸那里学习
- https://developer.android.com/reference/android/app/NativeActivity.html

给的是一个根据手机重力传感器的三个值然后显示相应颜色的APP，看的头都晕了

看不下去了，于是决定先看so，IDA打开so

![IMAGE](/assets/resources/D3518F55CC05C88D98FFE0D6F32E83F1.png)

我去！！！！！！

这尼玛什么鬼！！！！！！

200分的题目至于上O-LLVM混淆嘛？？？？？？

至于如何搞O-LLVM，以后专门整理一篇文章分享一下个人经验

总结出流程

首先通过传感器获取`(x, y, z)`三个值，之后传入`a_process()`

![IMAGE](/assets/resources/D21970D567385699FA24586A31F922C8.png)

当然，`a_process()`函数也是使用O-LLVM混淆了

![IMAGE](/assets/resources/EB37C882150FD785FDE938FC4F5B9187.png)

调用`a_process()`后会有一个判断

![IMAGE](/assets/resources/857462AEB0A00E22CAB7930A751EE54D.png)

如果`a_process()`的返回值不是6，直接和数组对比

![IMAGE](/assets/resources/E2D3A10E323E1164FD956A95B384F2DE.png)

如果返回值为6，则调用`b_process()`，并将两个返回值相加再跟数组对比

![IMAGE](/assets/resources/556127679658742606A73C4954F771A9.png)

`b_process()`函数同样使用了O-LLVM混淆

![IMAGE](/assets/resources/5F5B5BCA1E704E49470E9E84CCDDCEC9.png)

对比的数组
```
.rodata:0000785C dword_785C      DCD 2, 7, 5, 3, 2, 9, 4, 1, 3, 6
```

一共十个数字，也就是说要对比十次

那么我们可以通过动态调试伪造输入，当对比的数据为7和9时，将`a_process()`的返回值设为6，并将b的返回值设为1和3

使用IDA动态调试，先计算出两个调用点的地址然后下断点
```
>>> hex(0x38A6 + 0x7389F000)
'0x738a28a6'
>>> hex(0x3BE2 + 0x7389F000)
'0x738a2be2'
```

断点下好后，一直F9，当R0的值保持为2不变，即可进行十次对比

![IMAGE](/assets/resources/BA03E37D2F27E8CE4CC9FCD0E3AB4E97.png)

数组第二个数字是7，所以需要将R0修改为6

![IMAGE](/assets/resources/0AA9881DEBF05FA6AC40164A3480C6CF.png)

此时会在`b_process()`断下，将返回值修改为1，如果已经为1则不需要修改

![IMAGE](/assets/resources/250FE842FAF1C9CB3424927C88BDF68E.png)

按照这种方法对比完数组的十个数据，在Android Device Monitor输出Flag

![IMAGE](/assets/resources/7C1814CB95F60485B393C0E797DF5498.png)

动态调试的过程并不是很难，唯一需要注意的是，注意手机要立着:)

Flag：njctf{PvrNa7iv3Al1}

O-LLVM混淆这种大杀器想必是难倒了一大波人吧，各种跳转......

其实一开始运行APP就可以发现，这就是谷歌官网用于演示NativeActivity的Demo，出题人你出来我们聊聊人生