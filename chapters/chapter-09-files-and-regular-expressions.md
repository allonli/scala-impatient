## Chapter-9-文件和正则表达式

file 文件 正则表达式 Regular expression

---

### 读取行
```scala
import scala.io.Source
val source = Source.fromFile("myfile.txt", "UTF-8")
val iterator = source.getLines //获取Iterator[String]
for(l <- iterator) println(l) //直接打印
val lines = iterator.toArray //通过Iterator获取数组
val lines = iterator.toBuffer //通过Iterator获取数组
```
或者更简单粗暴的处理方式：把整个文件放到一个大字符串中
```scala
source.mkString
source.close //用完要关掉
```
### 读取字符
```scala
for(c <- source) println(c)
```
如果只是想查看下一个字符是什么，但是先不处理它。可以用source的buffered方法。然后再用head方法查看下一个字符。
```scala
val source = Source.fromFile("myfile.txt", "UTF-8")
val iterator = source.buffered

while (iterator.hasNext) {
    if (iterator.head 判断条件) 
        ...
    else
        ...
}

source.close()
```
### 读取词和数字
```scala
val tokens = source.mkString.split("\\S+")
val numbers = for (w <- tokens) yield w.toDouble
//或
val numbers = tokens.map(_.toDouble)
```
从控制台读取
```scala
print("How old are you? ")
val age = readInt()
```

### 从URL或其他源读取
```scala
val source1 = Source.fromURL("http://www.baidu.com", "UTF-8")
//需要提前知道对方页面的字符集，可以从http头里拿到
val source2 = Source.fromString("Hello, world!")
//这个东西有什么鸟用...
```
### 读取二进制文件
scala里没有字节流读取文件的方法，要使用java类库。
```scala
val file = new File(filename)
val in = new FileInputStream(file)
val bytes = new Array[Byte](file.length.toInt)
in.read(bytes)
in.close()
```
### 写入文件和访问目录
scala中没有写入和访问目录的api，需要借助java的写入文件api。

### 序列化
```scala
@SerialVersionUID(42L) class Person extends Serializable
//如果能接受默认UID，也可以省略掉@SerialVersionUID注解
val fred = new Person
import java.io._
val out = new ObjectOutputStream(new FileOutputStream("/tmp/test.obj"))
out.writeObject(fred)
out.close()
//序列化完成
val in = new ObjectInputStream(new FileInputStream("/tmp/test.obj"))
val savedFred = in.readObject().asInstanceOf[Person]
//反序列化
```
    scala的集合类默认都是可序列化的。可以放心使用它们做要序列化对象的成员变量。

### 进程控制-执行shell
scala中执行shell非常简单
```scala
import sys.process._
"ls" ! //执行ls命令，如果执行成功表达式的值为0，否则为非0。
```
如果使用!!而不是！的话，会以字符串的形式返回输出：
```scala
val result = "ls" !!
```
同时，还可以使用管道，用#|操作符实现：
```scala
"ls" #| "grep scala" ! 
```
**在执行shell的过程中，实际上scala把叹号前面的字符串做了隐式转换，偷偷转换成了ProcessBuilder对象，叹号再执行ProcessBuilder对象。**

#### 重定向
```scala
import java.io.File
"ls" #> new File("output.txt") ! //输出到文件
"ls" #>> new File("output.txt") ! //追加输出到文件
"grep scala" #< new File("output.txt") ! //文件做为输入
import java.net.URL 
"grep scala" #< new URL("http://www.scala-lang.org/api/current/#package") ! //url做为输入
```
进程控制支持很多熟悉的shell操作符，| > >> < && || ,使用时只要给它们的前面加个#即可。

如果需要要不同的目录运行，或者使用不同的环境变量，可以用Process对象的apply方法来构造ProcessBuilder。三个参数分别为命令、执行目录、一些元组用来设置环境变量。
```scala
val p = Process("ls" , new File("d:/"), ("LANG", "en_US"))
p !
```
### 正则表达式
从一个字符串到正则表达式对象，只需调用String类的r方法即可：
```scala 
val numPattern = "[0-9]+".r
```
如果正则表达式中有反斜杠、引号的话，可以使用"""..."""
```scala
val wsnumwsPattern = """\s+[0-9]+\s+""".r
```
findAllIn返回所有匹配的迭代器。
```scala
for (matchStr <- numPattern.findAllIn("99 bottles, 98 bottles"))
//或者转成数组再处理
val matches = numPattern.findAllIn("99 bottles, 98 bottles").toArray
//Array(99, 98)
```
要找到首个匹配项，可使用findFirstIn,得到一个Option[String]的结果。

也可以使用模式匹配：
```scala
val numitemPattern = "([0-9]+) ([a-z]+)".r
val numitemPattern(num, item) = "99 bottles"
//num: String = 99
//item: String = bottles
```
如果想从多个匹配项中提取分组内容，可以使用for:
```scala
for (numitemPattern(num, item) <- numitemPattern.findAllIn("99 bottles, 98 bottles"))
println("num:" + num + ", item:" + item)
//num:99, item:bottles
//num:98, item:bottles
```
