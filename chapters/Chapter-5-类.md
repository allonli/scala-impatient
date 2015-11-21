## Chapter-5-类

类 class

---

### 简单类和无参方法
scala中的类简单的和java相似，主要设计和习惯区别是：

* scala类中的方法默认为public的
* 字段必须初始化
* 类只有public的，所以不声明public关键字了（经常一个scala源文件会包含很多个类）
```scala
class Counter {
    private var value = 0 //你必须初始化字段
    def increment() { value += 1 } //默认是public的。
    def current() = value
}

val counter1 = new Counter
counter1.increment() // 当修改值时，最好加上“()”
counter1.current // 查询时不使用“()”，好一点
```

### getter和setter
在scala中，每个字段默认提供了getter和setter方法。在scala中的getter和setter是这样

>* age 等价于get方法；age_= 等价于set方法
    
```scala
class Person {
    var age = 0
}

 val person = new Person
 person age // 这么写和person.age是一样的
 person.age=(20) // 也可以很古怪的这么写 person age_= 10 ,因为方法名叫“.get_=” 这...！
```
>* jvm的内部实现会把上面的代码生成一个private的age和对应的getter、setter方法。


//建一个Person.scala文件把上面的Person代码放进去。在命令行中执行以下编译代码

    scalac Person.scala
//反编译

    javap -private Person
可以看到输出的结果，表明jvm的内部实现会把上面的代码生成一个private的age和对应的getter、setter方法。
```bash
Compiled from "Person.scala"
public class Person {
  private int age;
  public int age();//getter
  public void age_$eq(int);//setter ，java中不允许使用等号做为方法名，使用了“$eq”替代。
  public Person();
}
```
* 如果字段是私有的，getter和setter也是私有的。
* 如果字段是val的，只有getter被自动生成。(val已经不能修改，所以没必要提供setter)
* 如果将字段声明为private[this]，就不会再有getter和setter生成。

总结

* var的成员变量自动生成一个getter和setter
* val的成员变量自动生成一个getter
* scala中不能生成只有setter的成员变量

### 对象私有字段
和java一样，一个对象的方法内可以访问的其他任何该类对象的私有字段。（类私有字段）
```scala
class Counter {
    private var value = 0
    def increment() { value += 1 }
    def isLess(other : Counter) = value < other.value
    //可以访问另一个对象的私有字段
}
```
同时在scala中，权限可以控制的更细。如果字段加上private[this]修饰，则不能跨对象访问私有字段。（对象私有字段，SmallTalk等语言中也有对象级私有）
```scala
private[this] var value = 0 //只能自己用
```
private[this]中的this也可以换成自己的类名，或者外部类的类名（只有这两种选择，其他的会在编译期报错）。表示只能指定的类或者伴生对象（对象那章会讲）来调用。
### Bean属性
由于scala的默认生成的getter和setter和java中的规范不同（java bean是getXXX和setXXX），为了解决一些需要这种规范的场景，scala提供了兼容的注解来@BeanProperty。这样会同时生成两种风格的getter和setter.
```scala
import scala.beans.BeanProperty
class Person {
    @BeanProperty var name: String = _
}
// 将自动生成以下四个方法：
// name: String
// name_ = (newValue: String): Unit
// getName(): String
// setName(newValue: String): Unit
```

### 辅助构造器
scala中的构造函数分主辅。辅助的构造器和java基本相同，区别于：
>* 辅助构造器名为this。（修改类名变得更容易了）
>* 辅助构造器方法内的第一行，必须调用其他主、辅助构造器。
```scala
class Person{
  private var name = ""
  private var age = 0

  def this(name:String){
    this() //调用主构造器
    this.name = name
  }

  def this(name:String, age:Int){
    this(name) // 调用前一个辅助构造器
    this.age = age
  }
}
```
### 主构造器
主构造器在类名之后直接定义，如果类名后什么也不写，像java一样会默认提供一个无参的主构造器。
```scala
class Person(val name: String, val age: Int) 
```
以上语句换成java代码如下（始觉大道至简！）
```java
public class Person {
    private String name;
    private int age;
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName(){ return this.name; }
    public int getAge(){ return this.age }
    public void setName(String name){
        this.name = name;
    }
    public void setAge(int age){
        this.age = age;
    }
}
```
直接写在类体中的执行语句，会在主构造器中执行。
```scala
class Person(val name: String, val age: Int) {
    println("主构造器被调用！")
    def desc = name + " is " + age + " years old "
}
val person = new Person("allon", 29)
println(per desc)
//输出
// 主构造器被调用！
// allon is 29 years old 
```
常见的初始化配置文件等操作，就可以在类体中直接操作，因为它会做为主构造器中的一部分被执行。

主构造器中的参数实际是字段，它同样可以使用一些关键字来修饰。val\var\private\private[String]。也可以完全不加修饰关键字。
```scala
class Person(name: String, age:Int) {
    def desc = name + " is " + age + " years old "
}
```
以上主构造器的参数如果在该类内部方法中被使用了，它就相当于private[String]的效果，会自动升级为字段。如果没有被使用过，它就只是一个普通的主构造器中可以使用的参数而已。

也可以让主构造器变成private的。这样只能通过辅助构造器来创建Person对象了。
```scala
class Person private(val name: String, val age: Int) 
```

> 可以把类看作一个函数，主构造器参数就是这个函数的参数，那么类的内部任何位置当然可以使用该参数。只是当方法内部使用它时，它就会自动升级为字段而已。


### 内部类
在scala中，几乎可以随意嵌套语法结构。函数中可以定义函数，类中可以定义类。
```scala
import collection.mutable.ArrayBuffer

class Network {
    class Member
    
    pirvate val members = new ArrayBuffer[Member]
    
    def join(m:Member) = {
        members += m
    }
}

val chatter =  new Network
val myFace = new Network
```
> 在java中，内部类从属于外部类，scala中内部类从属于外部类对象。就像上面的代码中，myFace.Member和chatter.Member是两个不同的类。

```scala
  val chatter =  new Network
  val myFace = new Network
  val chatterMember = new chatter.Member
  val myFaceMember = new myFace.Member

  chatter.join(chatterMember)
  chatter.join(myFaceMember) //这里会报错，因为myFace.Member和chatter.Member是两个不同的类，members的泛型冲突了。
```

如果想解决以上问题，可以在到定义处把Member换成Netwok#Member。

```scala
  private val members = new ArrayBuffer[Network#Member]

  def join(m: Network#Member) = {
    members += m
  }
```

Network#Member的含义是，“任何Network的Member”。这种方式叫类型投影。后面也会讲到类型投影。
### 重命名外部类this引用
和java一样，在内部类中，可以通过“外部类.this”的方式来访问外部类的this引用。同时，在scala中你也可以为这个引用定义一个别名。

```scala
class Network(val name: String) {
  outer => //outer指向的是Network.this。可以用任何合法名来定义它，self在内部类中可能会引发岐义。

  class Member {
    def desc = outer.name
  }
}
```
