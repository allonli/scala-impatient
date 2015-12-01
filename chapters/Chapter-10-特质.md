## Chapter-10-特质

特质 trait 接口 interface 

---

### 当做接口使用的特质
特质的部分功能和java中的接口十分类似。例如：
```scala
trait Logger {
    def log(msg: String) //这个是抽象方法，不要写abstract，特质中默认未实现的方法就是抽象的。
}

class ConsoleLogger extends Logger { // 实现Logger。用extends，不是implements
    def log(msg: String) { println(msg) } // 不需要写override 
}
```
如果要实现多个特质，可以用with关键字来串联：
```scala
class ConsoleLogger extends Logger with Cloneable with Serializable // 所有的java接口都可以做为scala的特质使用，scala中同样只能有一个父类，但是可以实现多个特质。
```
    上面这行代码要解读为Logger with Cloneable with Serializable是一个整体，然后再由ConsoleLogger类继承。

---

### 带有具体实现的特质
在scala中，特质是可以有实现的。
```scala
trait Logger {
    def log(msg: String) { println(msg) }
}

class SavingsAccount extends Logger {
    def withdraw(amount: Double) {
        log("Insufficeient funds") // 使用特质中的方法
    }
}
```
让特质有具体实现有一个弊端，就是特质修改，子类要重新编译。

---

### 使用特质的对象
```scala
val acct = new SavingsAccount with Logger
acct.withdraw(1.0)
// Insufficeient funds
```

---

### 特质继承顺序
```scala
//父特质
trait Logger {
  def log(msg: String) {
    println("Logger.log")
    println(msg)
  }
}

//给日志加时间戳
trait TimestampLogger extends Logger {
  override def log(msg: String) {
    println("TimestampLogger.log")
    super.log(new java.util.Date() + " " + msg)
  }
}

//截断日志
trait ShortLogger extends Logger {
  val maxLength = 15

  // 特质中的字段，下面会讲
  override def log(msg: String) {
    println("ShortLogger.log")
    super.log(if (msg.length <= maxLength) msg else msg.substring(0, maxLength - 3) + "...")
  }
}

class SavingsAccount

object MyTest10 extends App {
  val acct = new SavingsAccount with TimestampLogger with ShortLogger
  val acct2 = new SavingsAccount with ShortLogger with TimestampLogger
  acct.log("1234567890123456")
  println()
  acct2.log("1234567890123456")
}
// 输出：
// ShortLogger.log
// TimestampLogger.log
// Logger.log
// Tue Dec 01 10:38:35 CST 2015 123456789012...
//
// TimestampLogger.log
// ShortLogger.log
// Logger.log
// Tue Dec 01 1...
```
上面代码已经可以看出端倪，当对象使用一串特质时，调用顺序是从后到前（从右至左）的。逐层调用时，如果当前特质的左边没有特质了，那么它的super就会调用自己的父特质。

* spuer的调用关系，取决于特质的顺序。
* 如果要指定调用父类特质可以：super[TimestampLogger].log(...)。但是只能指定自己的直接父类特质。

以下代码是等价的：
```scala
val acct = new SavingsAccount with Logger with TimestampLogger with ShortLogger
//等价于
val acct = new SavingsAccount with TimestampLogger with ShortLogger
```

---

### 在特质中使用抽象方法
如果在子特质中使用super，而父特质中的方法为抽象方法时，子特质对应的方法要加上override和abstract
```scala
trait Logger {
    def log(msg: String) // 这是个抽象方法
}
abstract override def log() {
    super.log(...)
}
```

---

### 模板方法
```scala
trait Logger {
  def log(msg: String)
  def info(msg: String) { log("INFO: " + msg) }
  def warn(msg: String) { log("WARN: " + msg) }
  def error(msg: String) { log("ERROR: " + msg) }
}

class SavingsAccount extends Logger {
  override def log(msg: String) = { println(msg) }
}

val acct = new SavingsAccount
acct.error("oh my god!")
```

---

### 特质中的具体字段
scala中的特质，字段如果有初始化值就是具体的，反之则为抽象。
特质中的具体字段不会被子类继承，而只是被简单的添加进去。
```scala
trait ShortLogger extends Logger {
    val maxLength = 15 //具体字段
}

class Account {
    var balance = 0.0
}

class SavingsAccount extends Account with ShortLogger {
    var interest = 0.0
}
```


    | balance   |  -> 父类对象
    -------------
    | interest  |  -> 子类字段
    | maxLength |  -> 子类字段

具体的特质字段，可以视为是一个装配指令，表示任何混入该字段的子类，都自动拷贝该字段给自己。

---

### 特质中的抽象字段

特质中的未初始化的字段都是抽象的，子类必须重写，但是不用加override关键字。

---

### 特质构造顺序
特质也有构造器，由字段的初始化和其他语句组成。
```scala
trait FileLogger extends Logger {
    val out = new PrintWriter("app.log") //构造器的一部分
    out.println("# " + new Date().toString) //同样是物质构造器的一部分
    def log(msg: String) {...} //不属于构造器的一部分
}
```
构造顺序如下：

* 对象首先调用父类构造器。
* 特质构造器在父类构造器之后、类构造器之前执行。
* 特质构造顺序和调用顺序相反，由左至右。
* 每个特质中，父特质先构造。
* 如果多个特质共有一个父特质，那么这个父特质在已经构造的情况下，不会再次构造。
* 所有特质构造器完成，子类再构造。

```scala
class SavingsAccount extends Account with FileLogger with ShortLogger
```
构造顺序
1. Account // 对象首先调用父类构造器。
2. Logger // 每个特质中，父特质先构造。
3. FileLogger // 对象首先调用父类构造器。
4. ShortLogger // 特质构造顺序和调用顺序相反，由左至右。
5. SavingsAccount // 所有特质构造器完成，子类再构造。

同样是之前的例子，调用顺序和初始化顺序放到一起打印：
```scala
//父特质
trait Logger {
  println("Logger init")

  def log(msg: String) {
    println("Logger.log")
    println(msg)
  }
}

//给日志加时间戳
trait TimestampLogger extends Logger {
  println("TimestampLogger init")

  override def log(msg: String) {
    println("TimestampLogger.log")
    super.log(new java.util.Date() + " " + msg)
  }
}

//截断日志
trait ShortLogger extends Logger {
  println("ShortLogger init")

  val maxLength = 15

  // 特质中的字段，下面会讲
  override def log(msg: String) {
    println("ShortLogger.log")
    super.log(if (msg.length <= maxLength) msg else msg.substring(0, maxLength - 3) + "...")
  }
}


class Account {
  println("Account init")
}

class SavingsAccount extends Account with TimestampLogger with ShortLogger

object MyTest10 extends App {
  val acct = new SavingsAccount
  println("初始化顺序打印完成！下面开始打印调用顺序：")
  acct.log("1234567890123456")
}

// 输出
// Account init
// Logger init
// TimestampLogger init
// ShortLogger init
// 初始化顺序打印完成！下面开始打印调用顺序：
// ShortLogger.log
// TimestampLogger.log
// Logger.log
```
可见第一轮全部先初始化，第二轮开始调用。顺序正好相反。

线性化是描述某个类型的所有父类型的规范，定义：
如果$C$ extends $C_1$ with $C_2$ with $...$ $C_n$，则 $lin(C)$ = $C$ >> $lin(C_n)$ >> $...$ >> $lin(C_2)$ >> $lin(C_1)$   
“>>”表示串联并去掉重复项，右侧胜出。
线性化顺序就是之前所说的super调用顺序。

---

### 初始化特质中的字段
特质不能有构造参数，每个特质都有一个无参的构造函数。
    
    缺少构造参数是特质和类的唯一差别

由于特质是无参构造，而且先于子类构造，那么如果想在常规构造之前初始化一些字段时，可以：
```scala
val acct = new { val filename = "myapp.log" } with FileLogger with ShortLogger
//如果在类中实现
class SavingsAccount extends { val filename = "myapp.log" } with FileLogger with ShortLogger
```
也可以使用lazy值，只是效率不是很高。

---

继承类的特质
类也可以被特质继承（变态啊）。若如此，该类也将自动成为该特质子类的父类。
```scala
trait LoggedException extends Exception with Logger {
  def log() { log(getMessage()) }
}

class UnhappyException extends LoggedException {
  override def getMessage() = "arggh"
}
```


    | Exception |   | LoggedException |
            ↖           ↗ 
          | UnhappyException |

如果子类已经有一个父类，那么该父类必须是那个特质父类的子类。

---

### 自身类型
当特质继承类时，编译器会把所有该特质的子类都认这个类为父类。scala还有另一套机制可以保证这一点：自身类型（self type）。
定义如下：
```scala
this: 类型 =>
```
例：
```scala
trait LoggedException extends Logger {
  this: Exception => def log() { log(getMessage()) }
}

class UnhappyException extends Exception with LoggedException{
  override def getMessage() = "arggh"
}
```
LoggedException类这样就只能被Exception的子类继承。
后面会更详细介绍这里自身类型。

* 自身类型就是强制要求一些类型必须是某些类的子类。
* 还有一种结构类型是强制要求一些类具有种个方法，属于一种轻量的接口。

### 特质的背后
特质会被翻译成jvm的类和接口。

* 没有具体实现的特质，会直接翻译成java中的接口。
* 有具体实现的方法scala会帮我们创建一个伴生类，该类用静态方法来存方法。
* 这些伴生类不会有任何字段，对应的字段用getter和setter方法来表示。
* 如果特质继承了某个父类，则伴生类不会继承这个父类。这个父类会被所有实现该特质的子类继承。