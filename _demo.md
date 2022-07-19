---
title: "第一篇文章"
layout: post
date: 2016-12-22
mathjax: true
diagram: true
ruby_notation: true
---

<details markdown="1"><summary>目录</summary>
* TOC
{:toc}
</details>

## Math Demo

$$ c = m^e \mod n $$

$$
 m^{ed} \equiv qq^{-1}m + pp^{-1}m  \\
   = (1 - k_1p)m + (1 - k_2q)m  \\
   = 2m - (k_1p+k_2q)m = m \mod pq 
$$


## Ruby notation Demo

[someword]{释义}


## 折叠演示

<details markdown="1"><summary>详细点此展开</summary>

```python
import sys
print "hello"
```
</details>

## Sequence图示例

<https://bramp.github.io/js-sequence-diagrams/>

```sequence
participant Device
participant Browser
participant Server
Browser->Server: username and password
Note over Server: verify password
Note over Server: generate challenge
Server->Browser:  challenge
Browser->Device: challenge
Note over Device: user touches button
Device-->Browser: response
Browser->Server: response
Note over Server: verify response
```

## Flowchart示例

<http://flowchart.js.org/>

```flowchart
st=>start: Start:>http://www.google.com[blank]
e=>end:>http://www.google.com
op1=>operation: My Operation
sub1=>subroutine: My Subroutine
cond=>condition: Yes
or No?:>http://www.google.com
io=>inputoutput: catch something...

st->op1->cond
cond(yes)->io->e
cond(no)->sub1(right)->op1
```
