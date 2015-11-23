## Chapter-6-对象

对象 object

---

### 单例对象
scala中没有静态方法和字段。只要的功效可以用object语法来实现。(object定义其实就是具体某个类的单例)
```scala
object Accounts {
    private var lastNumber = 0
    def newUniqueNumber() = { lastNumber += 1; lastNumber }
}

Accounts.newUniqueNumber
// 1
Accounts.newUniqueNumber
// 2
```
对象也可以继承其他类和特质（类似接口）。但是不能定义自己的构造函数。
### 伴生对象
在java中会有既有成员方法又有静态方法的类。在scala中，可以用伴生对象来达到目的。
```scala
class Account {
    val id = Account.newUniqueNumber() //访问伴生对象的私有方法
}

object Accout { // 伴生对象
    private var lastNumber = 0
    private def newUniqueNumber() = { lastNumber += 1; lastNumber }
}
```
如果类和其伴生对象在同一个源文件中，它们可以互相访问对方的private特性。

在调用伴生对象方法时，要Account.newUniqueNumber()，不能直接写newUniqueNumber()。

### 继承类或特质的对象
```scala
abstract class UndoableAction(val description: String) {
    def undo(): Unit
    def redo(): Unit
}

object DoNothingAction extends UndoableAction("Do nothing"){
    override def undo() {}
    override def undo() {}
}

val actions = Map("open" -> DoNothingAction, "save" -> DoNothingAction) //value使用DoNothingAction对象
```
### apply方法
apply方法被定义在伴生对象中，定义之后会可以像如下代码一样使用它：
```scala
class Account private (val id: Int, initBalance: Double) {
    private var balance = initBalance
}

object Account { //伴生对象
    def apply(initBalance: Double) = new Account(1986, initBalance)
}

val acct = Account(1000.0) //实际调用的是apply方法，等价于Account.apply(1000.0)
```

伴生对象以下格式，apply方法会被调用，一般它会返回一个伴生对象所对应类的对象：

> *对象名(参数1,参数2...,参数N)*

```scala
Array("Mary", "had", "a", "little", "lamb")
//等价于
Array.apply("Mary", "had", "a", "little", "lamb")

Array(100)
//返回一个只有一个元素的Int数组
new Array(100)
//返回一个有100个null元素的Array[Nothing]
```

### 应用对象
scala中的main方法写到一个对象里。方法类型为Array[String] => Unit:
```scala
object Hello {
    def main(args: Array[String]) {
        println("Hello, World!")
    }
}
```
也可以继承App特质，
```scala
object Hello extends App{
    println("Hello, World!")
}

//在EPFL中运行 Hello.main(null)
```
如果需要命令行参数，可以通过args属性得到，
```scala
object Hello extends App{
    if (args.length > 0) {
        println("Hello, " + args(0))
    } else
        println("Hello, World!")
}

//创建Hello.scala把上面代码拷贝进去
//scalac Hello.scala
//scala  -Dscala.time Hello Fred
//输出以下内容（-Dscala.time是运行时间）：
//  Hello, World!
//  [total 49ms] 
```

### 枚举
scala中没有枚举类型。标准库里提供了一个Enumeration类，可以产出枚举。
```scala
object Color extends Enumeration {
    val Red, Yellow, Green = Value
    /*
    相当于以下代码
    val Red = Value
    val Yellow = Value
    val Green = Value
    */
}
```
Value是一对伴生的类和对象。每个Value对象有自己的ID和名称。可以手动设置。
```scala
object Color extends Enumeration {
    val Red = Value(0, "Stop")
    val Yellow = Value(10) // 名称为“Yellow”
    val Green = Value("Go") // ID为11
}

Color.Red
//Color.Value = Stop
Color.Green.id
//Int = 11
Color.values
//Color.ValueSet = Color.ValueSet(Stop, Yellow, Go)
//可用for(c <- Color.values)来遍历这个set
```

* 如果没有手动设置枚举名称默认为变量名。
* 如果没有手动设置枚举ID默认为前一个加1，从零开始。

如果不想使用对象名调用枚举，可以import一下。就可以只简写了。
```scala
import Color._
Red
//Color.Value = Stop
```
枚举的类型是Color.Value而不是Color。可以用type起个别名，给Color.Value起个别名Color.Color，再使用import Color._ 。这样看起来就好像枚举的类型是Color，而实际上在使用的是Color.Color。（真拗口）
```scala
import color._

object Color extends Enumeration {
    type Color = Value
    val Red, Yellow, Green = Value
}

def doWhat(color: Color) = {//实际上是Color.Color
    ...
}
```
可以用ID或者名称来进行定位，以下两行代码输出都是Color.Red对象：
```scala
Color(0) //将调用Color.apply
//返回 Color.Value = Stop
Color.withName("Yellow")
//返回 Color.Value = Yellow
```
