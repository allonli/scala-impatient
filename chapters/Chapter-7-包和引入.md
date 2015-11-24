## Chapter-7-包和引入

包 引入 import package

---

### 包
scala中的包和C++的命名空间和java中的包概念是相同的，只不过它和目录没有直接关系，是一个虚拟的关系，在代码中定义。
```scala
package com {
  package horstmann {
    package  impatient {
      class Employee
    }
  }
}
```

* scala的包和目录不是强一致的，Employee完全可以不放在com/horstmann/impatient/这个目录下。
* 包可以定义在任何源文件中。
* 如果在相同或不同的源文件中定义了完全相同的冲突内容，会在编译时报“重复定义”的错误。

### 包的作用域
与java不同的是，scala的子包在以下代码方式时，不用import就有使用父包作用域的权力。
```scala
package com {
  package horstmann {

    object Util{
      def printObj(obj:Any) = println(obj.toString)
    }

    package  impatient {
      class Employee(name:String){
        override def toString = name
      }
      object Employee {
        Util.printObj(new Employee("allon")) // 注意Util是父包中的对象。不需要写成com.horstmann.Util。
      }
    }
  }
}
```
scala中的包路径是相对的
```scala
package com {
  package horstmann {
    package  util{
      object Properties {
        def printObj(obj:Any) = println(obj.toString)
      }
    }

    package  impatient {
      class Employee(name:String){
        override def toString = name
      }
      object Employee {
        def apply(): Unit ={
          util.Properties.printObj(new Employee("allon")) //不能直接使用Properties而要使用util.Properties。
        }
      }
    }
  }
}
```
以上代码访问Properties可以使用
1. util.Peroperties
2. com.horstmann.util.Peroperties
3. 定义绝对路径_root_.com.horstmann.util.Peroperties
4. 如果想使用的是scala标准库中的util.Peroperties会出现冲突，最好写全路径scala.util.Peroperties

### 文件顶部定义包
如果把包语句写成像java一样的串，作用域就会和java类似了。
```scala
package com.horstmann {
    object UtilA
}

package com.horstmann.impatient {
    object UtilB
    class Employee {
        //在这里com和com.horstmann作用域的成员是不可见的。
        UtilA //错误，不可见
        UtilB //可见
    }
}
```
scala也可以在文件顶部定义包
```scala
package com.horstmann.impatient
package people
class Person
// 这等价于
package com.horstmann.impatient {
    package people {
        class Person
    }
}
```
### 包对象
包里可以定义类、对象和特质，但是因为jvm的局限不能定义函数。为了解决这个问题，引入了包对象的概念。

每个包都可以定义一个和它同名的包对象。（有些像伴生对象）
```scala
package com.horstmann.impatient

package object people {
    val defaultName = "allon"
}

package people {
    package people {
        class Person(val name:String = defaultName) //使用的是包对象中的默认值。这里的defaultName不用写全路径，因为它们在同一个包中，外部使用时可以com.horstmann.impatient.people.defaultName来使用。
    }
}
```
在jvm中，包对象被编译成package.class，并置于相应的包下。一般可以把包对象放到com/horstmann/impatient/people/package.scala。

### 包的可见性及import
可以指定字段在哪些父包中可见
```scala
class Person(private[horstmann] val name:String = defaultName)
```

#### import
```scala
import java.awt._
//等价于java中的java.awt.*
```
在scala中完全可以定义com.horstmann.impatient.*.people，但是这么干等于在坑人。

import的语句在scala中可以放在任何地方。并不一定要文件顶部。
```scala
class Manager {
    import scala.collection.mutable._
    val subordinates = new ArrayBuffer[Employee]
}
```
#### import时的重命名和部分引入
```scala
import java.util.{HashMap => JavaHashMap} //重命名为JavaHashMap，这样可以避免一些冲突。
import java.awt.{Color, Font} 
//只引入Color和Font
import java.util.{HashMap => _, _} 
//HashMap => _ 表示隐藏掉HashMap，第二个下划线表示通配符全部。这个import整个含义是：除了HashMap都引入。
```

#### 隐式import
就是scala“偷偷”的帮我们已经引入了scala开头的包。因而我们在使用scala开头的包时，我们才可以省略掉“scala”。
```scala
import scala.collection.mutable._
//因为偷偷引入了scala包，所以也可以省略"scala"，从而和下面的import等价
import collection.mutable._
```