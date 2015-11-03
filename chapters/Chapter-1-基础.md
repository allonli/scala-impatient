# Chapter-1-基础

scala 基础

---

### 命令行环境
1.安装scala

2.将scala/bin加到PATH中

3.执行scala命令


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
scala中和java一样也有七种类型：Byte、Char、Short、Int、Long、Float、Double、Boolean。scala中不存在基本类型的概念，都视为引用类型。
下面的"Hello"和1分别被“偷偷”转换（隐式转换，如果想了解可以移步隐式转换那一章，但是我还是建议你按顺序看。只当它是“偷偷”转换的即可）成了StringOps和RichInt
```scala
"Hello".intersect("World") //输出"lo" String被转换成了StringOps
1.to(10) //Int被转换成RichInt
```

### 算术和操作符
scala中的操作符实际上是方法。1 + 1 实际上等价于1.+(1),加号就是方法名。（java/c++程序员是不是感觉有点惊愕）。并且这些方法同样可以重载，这样一来，你就可以定义属于自己的符号（方法名）定义了。

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
scala没有静态方法，要使用类似的特性，可以使用companion object(伴生对象)。```BigInt.probablePrime(100, scala.util.Random)```这个生成随机数的方法，实际上BigInt就是一个对象。

不带参数的方法，scala中通常不使用圆括号调用。如```"Hello".distinct```

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
