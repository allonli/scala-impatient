## chapter-01-the-basics

scala 基础

---

### 命令行环境
在学习scala时可以用两种方式结合学习，一种是命令行模式，这适合执行一些简单的代码。以便快速得到结果。另一种是使用IDE开发，IDE这里使用的是IntelliJ IDEA。以下介绍了简单的几个步骤。
#### EPFL命令行
	1.安装scala
	2.将scala/bin加到PATH中
	3.执行scala命令
#### IntelliJ IDEA
    1.安装IEEA
    2.安装scala插件
    3.创建scala工程

### 声明常量
```scala
val anserwer = 8 * 5 + 2 //不可变常量,不用使用分号
anserwer = 1 //错误！
//以上代码等价于以下代码
val anserwer : Int = 8 * 5 + 2 //不用写‘Int’的原因是scala已经有了类型推断
var name,company : String = null //name和company都是初始值为null的字符串
```

### 声明变量
```scala
var anserwer = 8 * 5 + 2
```

### 常用类型
scala中也存在七种类型用来对应java的基本类型：Byte、Char、Short、Int、Long、Float、Double、Boolean。scala中不存在基本类型的概念，都视为引用类型。
下面的"Hello"和1分别被“偷偷”转换（隐式转换，如果想了解可以移步隐式转换那一章）成了StringOps和RichInt
```scala
"Hello".intersect("World") //输出"lo" String被转换成了StringOps
1.to(10) //Int被转换成RichInt
```

### 算术和操作符
scala中的操作符实际上是方法。1 + 1 实际上等价于1.+(1),加号就是方法名。（java/c++程序员会感觉有些不一样）。并且这些方法同样可以重载，这样一来，你就可以定义属于自己的符号（方法名）定义了。

### 调用函数和方法
方法被引用来调用，而scala也支持函数，这些函数不需要从某个类来调用。直接使用便是。
```scala
sqrt(2) //平方根
pow(2, 4) //4的平方
min(3, Pi) //最小值
/*
不过在使用上述函数之前,
你需要import scala.math._相当于java的import scala.math.*
使用以scala开头的包时，可以省略scala前缀。如：import math._
*/
```
scala没有静态方法，要使用类似的特性，可以使用companion object(伴生对象)。```BigInt.probablePrime(100, scala.util.Random)```这个生成随机数的方法，实际上BigInt是一个对象。

不带参数的方法，scala中通常不使用圆括号调用。如```"Hello".distinct```

scala中函数和方法的区别：
>* Scala中的方法跟Java的方法一样，方法是组成类的一部分。
>* Scala中的函数是一个完整的对象。

任何一个函数对象都是一个继承了Function开头的特质（trait，可以暂时当做java中的接口）。
```scala
Function1[+T1] extends AnyRef
//A function of 1 parameter.
Function2[+T1, +T2] extends AnyRef
//A function of 2 parameters.
Function3[+T1, +T2, +T3] extends AnyRef
//A function of 3 parameters.
//最多有Function22
```

java中有22个函数特质（trait）。任何一个函数都将是这22个trait的具体实现。
```scala
//以下两种方式结果相同
val adder = (x:Int,y:Int) => x+y
//返回 adder: (Int, Int) => Int = <function2>
val adder = new Function2[Int,Int,Int](){
    def apply(x:Int,y:Int):Int = x + y
}
//同样返回 adder: (Int, Int) => Int = <function2>
```

### apply方法
```scala
"Hello"(4) //等价于"Hello".apply(4),相当于java里的"Hello".charAt(i),这是scala为了便利提供的一种语法糖。
//"Hello"会被隐式转成StringOps，而在伴生对象StringOps中存在着apply方法。
```

### 使用scala doc
http://www.scala-lang.org/api
#### 使用技巧:
1. 左上解可以搜索类名，每条结果左侧的C和O是可以点的。分别进到该类和companion object(伴生对象)。点“display all entities”可以看到所有的包。
2. 数值类型，多看看RichInt、RichDouble等。字符串看StringOps。
3. 数学函数位于scala.math包中，不在某个类里。
4. 如果看到unary_-不要感到奇怪，这是前置的负操作符-x的方式。在操作符那章，你会豁然开朗。


## 习题

* 在Scala REPL中键入“3.” 然后按Tab。有哪些方法可以被应用？
    
        %   *   -   >    >>    ^  ...
* 在Scala REPL计算3的平方根，再求值平方。现在，这个结果与3相关多少？（提示：res变量是你的朋友。）
```scala
import math._
sqrt(3)
// res85: Double = 1.7320508075688772

res85*res85
// res86: Double = 2.9999999999999996

3 - res86
// res87: Double = 4.440892098500626E-16
```

* res变量是val还是var?

```scala
res89 = 3
//<console>:8: error: reassignment to val
//res9 = 3
//   ^
```
* Scala允许你用数字去乘字符串---去REPL中试一下"crazy"*3。这个操作做什么？在Scaladoc中如何找到这个操作?
```scala
"crazy" * 3
// res90: String = crazycrazycrazy
```
*    *是它的方法"crazy".*(3)。直接到StringOps类看*方法即可。
5. 10 max 2的含义是什么？max方法在哪个类中？
    
    此方法返回两个数字中较大的那个。在RichInt中。

6. 用BigInt计算2的1024次方。
```scala
BigInt(2).pow(1024)
```

* 为了在使用probablePrime(100,Random)获取随机素数时不在probablePrime和Random之前使用任何限定符，你需要引入什么？

    要引入对应的包，Random在util下。而probablePrime在对象BigInt里。
```scala
import util.Random
import import math.BigInt.probablePrime
probablePrime(3,Random)
//res1: scala.math.BigInt = 5
```

* 创建随机文件的方式之一是生成一个随机的BigInt，然后将它转换成三十六进制，输出类似"qsnvbevtomcj38o06kul"这样的字符串。查阅Scaladoc，找到在Scala中实现该逻辑的办法。

    在BigInt里找到toString方法
```scala
probablePrime(100,Random)
//res2: scala.math.BigInt = 680624836022523911868209171401

res2.toString(36)
//res3: String = 1tzk0mgequl6l6t2c24p
```

* 在Scala中如何获取字符串的首字符和尾字符？

    到StringOps中查找
```scala
scala> res3.head
//res4: Char = 1
scala> res3.last
//res6: Char = p

//另外两种方式：

res3(0)
res3.take(1)
//获取首字符
res3.reverse(0)
res3.takeRight(1)
//获取尾字符
```

* take,drop,takeRight和dropRight这些字符串函数是做什么用的？和substring相比，他们的优点和缺点都是哪些？

        查询API即可take是从字符串首开始获取字符串,drop是从字符串首开始去除字符串。takeRight和dropRight是从字符串尾开始操作。这四个方法都是单方向的。如果我想要字符串中间的子字符串，那么需要同时调用drop和dropRight，或者使用substring
