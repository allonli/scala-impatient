## chapter-11-operators

操作符

---

### 标识符和操作符
变量、函数、类等名称统称为标识符。在scala中的标识符，一般不跟关键字冲突，几乎所有的Unicode字符都可以。

在scala中，很多时候操作符实际上是函数名，所以标识符和操作符这两个概念差别不大。

常见的赋值操作符

    a 操作符= b
    等价于： a = a 操作符 b
    
---

### infix（中置）
    a 标识符 b
    如 1 to 10
这样的标识符在两个参数之间的表达式叫infix表达式（中置表达式）

---

### postfix和perfix (后置和前置)
后置和前置表达式都一元的，就是只有一个参数。中置由于有两个参数，所以叫二元表达式。（承自haskell同样的概念）

若标识符在后面，就叫后置。反之叫前置。
    a 标识符 // 后置
    如 1 toString
    -a // 前置
    
后置操作符的优先级低于中置
    a 中置操作符 b 后置操作符
    上述代码等同于：
    (a 中置操作符 b) 后置操作符
    
---

### 左结合和右结合
在优先级相同时，scala中的表达式默认是左结合，就是从左边向右开始计算。以下两种方式为右结合：

* 以冒号（:）结尾的操作符
* 赋值操作符

用于构造List的::的操作符就是右结合的，如：
```scala
1 :: 2 :: Nil
//意思是：
1 :: (2 :: Nil)
//这样先有2然后再把2挂到集合的尾部就成了List(1, 2)
```
右结合的二元操作符其实是第二个参数的方法：
```scala
2 :: Nil
//等价于
Nil.::(2)
```

---

### apply和update方法
```scala
f(arg1, arg2, ...)
f(arg1, arg2, ...) = value
```
如果f不是函数，那么上面的代码实际上调用的是apply和update方法：
```scala
f.apply(arg1, arg2, ...) //如果f不是函数，这行代码与f(arg1, arg2, ...)等价
f.update(arg1, arg2, ..., value) // 如果f不是函数，这行代码与f(arg1, arg2, ...) = value等价
```
这种机制被用在了数据和映射上，如：
```scala
val scores = new collection.mutable.HashMap[String, Int]
scores("Bob") = 100 //调用score.update("Bob", 100)
scores("Bob")       //调用score.apply("Bob")
```
同时，如之前章节提到的伴生对象，也是使用的这种方式：
```scala
class Fraction(n: Int, d: Int)

object Fraction {
  def apply(n: Int, d: Int) = new Fraction(n, d)
}
```
---

### 提取器
顾名思义，unapply是反向apply。它接受一个对象，反解成具体的值。这个叫提取器（Extractor）
scala有三种提取器：

* def unapply(object: S): Option[(T1, ..., Tn)]
* def unapply(object: S): Option[T]
* def unapply(object: S): Boolean

#### 第一种提取器：def unapply(object: S): Option[(T1, ..., Tn)]
```scala
class Fraction(n: Int, d: Int) {
  def *(f: Fraction): Fraction = Fraction(n * f.n, d * f.d)
}

object Fraction {
  def apply(n: Int, d: Int) = new Fraction(n, d)
  def unapply(input: Fraction) = if (input.d == 0) None else Some((input.n, input.d, input.hashcode))
}


var Fraction(a, b, c) = Fraction(3, 4) * Fraction(2, 5)
println("a:" + a + ", b:" + b + ", c:" + c)
//输出a:6, b:20, c:586617651
```
unapply反回一个Option，它包含一个元组。里面可以放一些值进去。
注意它和模式匹配（后面会讲）有点像，但是实际上不相同：
```scala
var Fraction(a, b, c) = ...
case Fraction(a, b, c) => ...
```
其实unapply能接受任何参数，并不局限于该类对象：
```scala
object Name {
  def unapply(input: String) = { // 接受一个String
    val x = input.split(" ")
    Some(x(0), x(1))
  }
}

val author = "Allon Li"
var Name(first, last) = author // Name.unapply(author)
println("first:" + first + ", last:" + last)
//输出 first:Allon, last:Li
```
每个case class（在后面会讲）默认会配备一对apply和unapply
```scala
case class Name(first: String, last: String) 

val author = Name("Allon","Li") //apply

author match {
  case Name(first, "Li") => println("the first name : " + first) // unapply ,这行代码表示：last必须为Li的人。
}
```


scala中没有一个元素的元组，如果要用unapply提取单个值，要指定它的返回值类型为Option的泛型。那就用到了第二种提取器：
#### def unapply(object: S): Option[T]
```scala
object Number {
    def unapply(input: String): Option[Int] =
    try {
        Some(Integer.parseInt(input.trim))
    } catch {
        case ex: NumberFormatException => None
    }
}

val Number(n) = "1729"
```
也可以用于测试输入，但不提取值。这时，unapply返回Boolean。这要就用到了第三种提取器：
#### def unapply(object: S): Boolean
```scala
object IsCompound {
    def unapply(input: String) = input.contains(" ")
}

author match {
  case IsCompound() => println("is compound") //IsCompound()里的()不能省
  case _ => println("...")
}
```
    在使用case时，对于返回Option的提取器，如果调用unapply方法成功返回Some，就算是成功。对于返回Boolean的提取器，如果调用unapply方法返回true，那就是匹配成功。

同样可以把第一种和第三种提取器结合，就像以下代码：
```scala
author match{
    case Name(first, last @ IsCompound()) => println("has a compound last name")
    case Name(first, last) => println("don't has a compound name")
    case _ => println("...")
  }
```
上面代码中第一个case是一种复合匹配，只有当Name这个提取器匹配成功，并且提取出来的第二个变量匹配成功IsCompound()时，整个模式才会匹配成功。在这里@定义了一个变量last，把它绑定到成功匹配了IsCompound的那个值上。

#### 提取序列
前面提到的提取器，都要固定返回值个数，也可以让提取器任意个数返回结果。这要使用unapplySeq方法。
```scala
object Name {
  def unapplySeq(input: String): Option[Seq[String]] = {
    Some(input.split(" "))
  }
}

author match { // author是unapplySeq的入参
  case Name(first, "Li") => println("the first name : " + first)
  case Name("Allon", middle, "Li") => println("the middle name : " + middle)
}
```
如果同时定义unapplySeq和冲突的unapply，会报错。
