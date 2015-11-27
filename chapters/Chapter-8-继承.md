## Chapter-8-继承

extends 继承

---

### 继承类
```scala
class Employee extends Person {
    var salary = 0.0
}
```
和java不同的是，java中的final是不可变的。而scala中的final var是可以修改的，只是不能被继承。final val才相当于java的final。
```scala
class Person {
  val a = 1
  final val b = 2
}

class Employee extends Person{
    override val a = 11 //可以继承，同时在子类中可以修改
    override val b = 12 //错误，b是final不能继承
}
```

### 重写方法
在java中override注解的形式使用的，在scala中如果要复写，必须使用override修饰。

    java中没有强制使用override的原因是，如果父类总修改就会出现问题。假如有Person父类，Student子类。Student类有个id属性，父类没有。这时不需要写override修饰。而后来父类突然加了一个也叫id的属性，这时子类属性上没有override。如果强制子类override修饰，子类就会报错。从而影响到别人。这也是为什么java采用注解的方式。
    
在scala中，调用父类方法和java相同，使用super关键字：
```scala
class Employee extends Person {
    override def toString = super.toString
}
```

### 类型检查和转换
isInstanceOf用来检查对象是否属于某个类或子类，如果返回true可以再使用asInstanceOf把这个对象转成对应的类对象。
```scala
object Employee extends App{
  val e = Employee()
  val e2 = Employee

  if(e.isInstanceOf[Employee]){
    val s  = e.asInstanceOf[Employee]
    println(s)
  }

  if(e2.isInstanceOf[Employee]) { // object对象不属于它的伴生类
    println("e2")
  }else {
    println("e2 is not Employee class object ")
  }

  def apply() = new Employee

}

class Employee extends Person {
  var salary = 0.0
  var name  = "allon"
  override def toString: String = name + " " + salary
}

class Person

//运行 Employee 输出：
//allon 0.0
//e2 is not Employee class object 
```
由第二行输出也可以看出。单例对象不是用new关键字实例化的，所以没机会传递给它实例化参数。每个单例对象都被实现为虚拟类(synthetic class)的实例。

如果想测试一个引用指向的是一个Employee而不是它的子类，
```scala
if (p.getClass  == classOf[Employee])
```

### 调用父类构造器
在scala中不能用super的关键字来调用父类构造器
```scala
class Employee(name:String,age:Int,val salary:Double) extends Person(name,age)
```
在java中，上述代码如下：
```java
public class Employee extends Person { // Java
    private double salary;
    public Employee(String name, int age, double salary) {
        super(name, age);
        this.salary = salary;
    }
}
```
如果scala继承的是一个java类，它的主构造器必须调用java父类的一个构造方法。
```scala
class Square(x: Int, y: Int, width: Int) extends java.awt.Rectangle(x, y, width, width)
```
### 重写字段

>* def只能重写另一个def
>* val只能重写另一个val或者不带参数的def
>* var只能重写另一个抽象的var

    由于var只能重写抽象的var，那么父类如果定义了一个非抽象的，子类就没办法重写了。
    
### 匿名类
```scala 
val alien = new Person("Fred"){
    def greeting = "Greetings, Earthling! My name is Fred."
}
// alien: Person{def greeting: String}
```
可以做为参数来定义：
```scala
def meet(p: Person{def greeting: String}){
    println(p.name + "says: " + p.greeting)
}
```

### 抽象类和字段
子类重写父类的抽象方法时，不强制使用override。
```scala
abstract class Person {
    val id: Int
    //没有初始化 一个抽象的有getter的字段
    var name: String
    //抽象的，getter\setter俱全的字段
}

val fred = new Person {
    val id = 1729
    var name = "Fred"
}
```

### 构造顺序和提前定义
```scala 
class Creature {
  val range: Int = 10
  val env: Array[Int] = new Array[Int](range)
}

class Ant extends Creature {
  override val range = 2
}

val ant = new Ant
println(ant.env.length)
//打印 0
```
以上代码打印0的原因是因为如下过程：
1. 在new Ant的时候会先调父类构造方法，Creature的默认主构造器是Creature()。直接在默认的主构造方法中初始化range和env。
2. 当env被初始化时，它的参数range实际上是在调用子类的range()，而这时子类还没有完成初始化，子类的range字段没值。子类的range()只能返回Int的默认值0。
3. 等range有值了，一切都晚了，env已经初始化完成，长度为0。
4. 简而言之，先调父类构造器，父类构造器内部又调的是子类方法，而子类还没来得及初始化。

可以将子类的字段设为final或者将父类的字段设为lazy。可解决以上问题，或者将range字段设先于父类的初始化字段。
```scala
class Ant extends { override val range = 2 } with Creature
//这里要用with关键字
```
-Xcheckinit可以用来调试构造顺序问题，未初始化字段被访问的时候它会抛出异常。
```bash
scalac -Xcheckinit MyTest.scala
scala MyTest
#输出
scala.UninitializedFieldError: Uninitialized field: MyTest8.scala: 15
        at Ant.range(MyTest8.scala:15)
        at Creature.<init>(MyTest8.scala:11)
        at Ant.<init>(MyTest8.scala:14)
        at MyTest9$.delayedEndpoint$MyTest9$1(MyTest8.scala:5)
        ...  
```

### Scala继承层级

* 和java中基本类型对应的类、Unit类型。都是继承自AnyVal。AnyVal是空的，只是一个继承体系的合龙标记。
* 所有其他的类都是继承自AnyRef。相当于java中的Object类。有wait和notify/notifyAll等方法。同时还提供了一个synchronized方法，等同java中的synchronized代码块。
```scala
account.synchronized { account.balance += amount }
```
* 而AnyRef和AnyVal都是Any类的子类。isInstanceOf、asInstanceOf和一些判断相等和哈希的方法在Any类。

![Scala类的继承关系](https://github.com/allonli/scala-impatient/raw/master/chapters/20151127111823.png)

* 如图，scala的类都是实现了ScalaObject这个空接口。
* 同时，Null类型只有一个唯一的值null。根据图中关系，Null是不能赋值给一个AnyVal子类的变量的。比如val v:Int = null是不可以的。
* Nothing是没有实例的，主要应用在泛型中做为一个标记，表示任何类型。如List[Nothing]表示list中可以放任何Nothing子类型。

### 对象相等判断
AnyRef有两个判等方法，eq和equals。eq判断两引用是否指向同一个对象。默认equals会直接调用eq.当想自定义比较时，要复写equals和hashCode。

