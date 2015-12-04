## chapter-02-control-structures-and-functions

函数 控制结构 异常

---

### 条件表达式

scala里的if/else是有值的，返回的值就是那个表达式的值。如果if语句的条件为false没有进入语句块，那么该if表达式的值为Unit(含义接近void，和void的区别是：void是空的钱包，Unit是钱包里一张写着“没钱”的纸币)，写做()。

scala中没有switch语句，代之以强大的模式匹配来解决问题。

### 块表达式和赋值

在scala中一切都是表达式，{}语句块是一组表达式的集合，这个集合的值取决于最后一个表达式的值。
在scala中，赋值表达式是值为Unit的。比如下面的语句块的值即为Unit因为：
```scala
{ r = r * n; n -=1 }
```
下面语句的想给x赋值和java中会完全不同，y = 1的值为Unit，x最终就会被赋值成Unit。所以不要这么用。
```scala
x = y = 1 
```

### 输入和输出

如果要打印一个值，可以用print和println函数。像这样：
```scala
print("Answer: ")
println(42)
```
另外还有一个C风格的printf函数：
```scala
printf("Hello, %s! you are %d years old.\n"," Fred", 42)
```
readLine函数可以从控制台读取一行输入。读取Int可以用readInt其他类型以此类推。
```scala
val name = readLine("请输入您的名字并回车：")
println("您的名称是："  + name)
```

### 循环
scala中的while/do语句和java、C++使用完全相同。
scala中的for循环比java更加直观方便。
```scala
for (i <- (1 to 10)) println(i)
for (i <- 1 to 10) println(i) //可以省略1 to 10外面的括号
```
    for循环的语法结构为： for (i <- 表达式)
    表达式一般可以为一个集合

在使用for循环的时候，也可以使用从0到n-1的区间，这时可以用until而不是to。如

```scala
for (i <- 1 until 10) println(i) //会自1输出至9
```

跳跃式循环
```scala
for(i <- 0 until (10,2)) println(i)
//输出
0
2
4
6
8
```
 
scala并没有提供break，continue。如果想使用类似功能可以：

    1. 使用Boolean和return
    2. 使用Breaks对象中的break方法实现，如下：
```scala
import util.control.Breaks._
for (...) {
    if(...) break //退出代码块，它是通过try/catch实现的，效率不高
}
```

### 高级for循环和for推导式

在java中实现双层嵌套for循环代码的可读性不如scala。在scala里，你只需要这样：
```scala
for (i <- i to 3; j <- 5 to 6) print((10 * i + j) + " ")
//将输出 15 16 25 26 35 36
```
从左到右就是双层for的从外到内。另外还可以在上面的基础上为每层循环加if条件判断：
```scala
for (i <- 1 to 3 if i < 3; j <- 5 to 6 if j > 5) print((10 * i + j) + " ")
//输出 16 26
```
可以定义不限个数的临时变量：
```scala
for (i <- 1 to 3; from = 6;j <- from to 6) print((10 * i + j) + " ")
//将输出 16 26 36
```
可以将for循环过程中的任意值收集到一个集合中，这类循环叫for推导式。
```scala
for (i <- 1 to 10) yield i % 3
//将返回 Vector(1, 2, 0, 1, 2, 0, 1, 2, 0, 1)
for (c <- "Hello") yield (c + 1).toChar
// 输出 Ifmmp
```

### 函数默认参数

```scala
def decorate(str:String, left:String = "[", right:String = "]") = left + str + right
//left和rigth带有默认值，如果不传会按顺序自动传递。如果手工传递了值，则依然按顺序对号入座。
decorate("Hello","<")
//返回 <Hello]

//也可以像下面这样指定参数名，如此一来便不用按顺序传递了
decorate("Hello",right = ">")
//返回 [Hello>
```

### 可变参数列表

```scala
def sum(args:Int*) = {
    //args实际上是一个Seq类型的参数
    for(arg <- args) println(arg)
}
sum(1,2,3,4)
//输出
1
2
3
4
```
既然可变参数列表可以传多个Int，那么可以这样吗？
```scala
sum(1 to 5) //这样写会报错，原因是1 to 5只是一个Range对象，并不是多个Int的参数，不过可以通过以下方式来把它变成一个参数序列。
sum(1 to 5:_*) //这转成了一组参数序列
```
当调用可变参数列表的函数且参数类型为Object的java方法，要手工对基本类型进行转换。

```scala
val str = MessageFormat.format("The answer to {0} is [1]", "everything", 42.asInstanceOf[AnyRef])
```

    42.asInstanceOf[AnyRef])

### 过程
如果函数体为花括号，但是没有“=”的函数，返回类型为Unit。也叫过程(procedure)
```scala
def box(s: String) {//没有=号
...
}

//以下三种情况效果相同
def box(s:String){}
//box: (s: String)Unit

def box2(s:String)={}
//box2: (s: String)Unit

def box3(s:String):Unit={}
//box3: (s: String)Unit
```

如果使用过程，有可能产生java程序员意想不到的后果，因为它的值为Unit。实际使用时和期望有可能不一致。导致：Unit在那里不能接受 的异常。

### lazy
以下代码它会不会立刻初始化，只有当words被使用的时候才会去打开文件。
```scala
lazy val words = scala.io.Source.fromFile("/usr/share/dict/words").mkString
//第一次被使用时执行
val words = scala.io.Source.fromFile("/usr/share/dict/words").mkString
//一被定义就执行
def words = scala.io.Source.fromFile("/usr/share/dict/words").mkString
//每次调用执行都会执行
```
### 异常
scala没有非运行时异常。IOException不需要声明和强制catch。throw一个异常的值为Nothing。
```scala
throw new IllegalArgumentException("this e exception")//它的值为Nothing
```
scala中也同样使用try/catch来捕获异常，语法形式是scala中的模式匹配方式。
```scala
try {
    ...
} catch {
    case _: SomeException... //不需要使用异常变量，直接用“_”表示即可
} finally {
    in.close() // 和java/c++中的含义相同，不管怎样，in都将被close
}
```

---

## 习题
* 一个数字如果为正数，则它的signum为1;如果是负数,则signum为-1;如果为0,则signum为0.编写一个函数来计算这个值。

```scala
def signum(num: Int):Int = {
   if(num > 0) 1 else if(num == 0) 0 else -1
}
```

*  一个{}的值是什么？类型是什么？
```scala
val t = {}
// t: Unit = ()
```
   可以看出，它的值是()类型是Unit     
    
*  指出在Scala中何种情况下赋值语句x=y=1是合法的。(提示：给x找个合适的类型定义)
    
    所以只要x为Unit类型就可以了。

*  针对下列Java循环编写一个Scala版本:for(int i=10;i>=0;i--)System.out.println(i);
```scala
for(i <- 0 to 10 reverse) println(i)
```

*  编写一个过程countdown(n:Int)，打印从n到0的数字。
```scala
def countdown(n:Int){
    0 to n reverse foreach print
}
```

*  编写一个for循环,计算字符串中所有字母的Unicode代码的乘积。举例来说，"Hello"中所有字符串的乘积为9415087488L。
```scala
var t:Long = 1
for(i <- "Hello") t = t * i.toLong
t
//res57: Long = 9415087488
```

*  同样是解决前一个练习的问题，但这次不使用循环。（提示：在Scaladoc中查看StringOps）
```scala
"Hello".foldLeft(1L)((a, b) => a * b)
```

*  编写一个函数product(s:String)，计算前面练习中提到的乘积。
```scala
def product(s:String):BigInt = {
    s.foldLeft(1L)((a, b) => a * b)
}
```

*  把前一个练习中的函数改成递归函数。
```scala
def productRec(s: String):Long = {
	if(s.length == 0) 1
	else s(0) * productRec(s drop 1)
}                                         
```

*  编写函数计算x^n^,其中n是整数，使用如下的递归定义：
x^n^=y^2^,如果n是正偶数的话，这里的y=x^(n/2)^
x^n^ = x*x^(n-1)^,如果n是正奇数的话
x^0^ = 1
x^n^ = 1/x^(-n)^,如果n是负数的话
```scala
def xpown(x: BigInt, n: Int): BigInt = {
	if(n > 0 && n % 2 == 0) xpown(x, n / 2) * xpown(x, n / 2)
	else if(n > 0) x * xpown(x, n - 1)
	else if(n == 0) 1
	else 1 / xpown(x, -n)
} 
```
