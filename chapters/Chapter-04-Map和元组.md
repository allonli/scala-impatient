## Chapter-4-Map和元组

Map 映射 元组

---

### 创建Map并取值
scala中的Map可以非常直观的创建，下面是创建一个不可变的Map
```scala
val map = Map("Allon" -> 29, "Jerry" -> 18, "Karen" -> 54 )
map("Allon")
//返回29
map("Allon") = 30
//会报错，因为这个Map默认为collection.immutable.Map的不可变Map。不能修改，也不能添加元素。
```
上面的map是不可以被修改的，如果想要一个可变的Map可以用
```scala
val map = collection.mutable.Map("Allon" -> 29, "Jerry" -> 18, "Karen" -> 54 )
map("Allon") = 99
//可以修改成功
```
操作符 "->" 也可以换成逗号。

map取值比较简单，就像上面一样。不过如果map中不存在对应的key，会抛错。这时我们可以用contains方法检查是否包含key。
```scala
if(map.contains("XXX")) map("XXX") else 0
```
上面的写法有些麻烦，更常用的是：
```scala
val value1 = map.getOrElse("Allon",0)
```
像java中一样，map是有get方法的。它的返回是Option类的子类，要么是Some，要么是None。在模式匹配那章会具体讲Option类。
```scala
map.get("Allon")
//返回 Option[Int] = Some(99)
```

### 更新Map中的值

```scala
val map = collection.mutable.Map("Allon" -> 29, "Jerry" -> 18, "Karen" -> 54 )
map +=  ("Bob" -> 63) 
//Map(Karen -> 54, Bob -> 63, Allon -> 29, Jerry -> 18)
```
如果更新过程中，不存在key，那么它会自动添加
```scala
map("Koran") = 77 
//Map(Karen -> 54, Bob -> 63, Allon -> 29, Jerry -> 18, Koran -> 77)
```
删除，如果不存在该key，就当没发生。
```scala
map -= "Koran"
//Map(Karen -> 54, Bob -> 63, Allon -> 29, Jerry -> 18)
```
不能更新一个不可变Map，但是可以把它追加到其他Map上。

*   累加过程中遇到冲突元素会update。
*   不管是不是可变Map都支持这种操作。
*   写法只能如下这么写，不可以直接两个Map直接相加。
```scala
map + ("Iresb" -> 43,"Karen" -> 12)
//Map(Karen -> 12, Allon -> 29, Bob -> 63, Iresb -> 43, Jerry -> 18)
```
也可以
```scala
var map = collection.immutable.Map("Iresb" -> 43,"Karen" -> 12)
map = map + ("Abc" -> 43,"Kkk" -> 12)
```

### 遍历Map和有序Map
```scala
for((k, v) <- map) println("key:" + k +", "+ "value:"+ v)
map.keyset //Set(Iresb, Karen, Abc, Kkk)
map.values //MapLike(43, 12, 43, 12)
for((k, v) <- map) yield (v, k)//对调k和v
```
scala中没有可变的tree map，下面两种是scala中的有序map。
```scala
val map = collection.immutable.SortedMap("Allon" -> 29, "Jerry" -> 18, "Karen" -> 54 ) //基于平衡树实现的有序map。它是不可变的！

val map = collection.immutable.LinkedHashMap("Allon" -> 29, "Jerry" -> 18, "Karen" -> 54 ) //基于链表实现的有序map，顺序卽为插入顺序 
```

### 与java的互操作
scala中没有可变tree map，我们可以使用java的API，从而把java的转成scala的map。
```scala
import collection.JavaConversions.mapAsScalaMap

val map = new java.util.TreeMap[String, Int]
map.put("Jerry",18)
map += ("Jerry"->18)// 相当于"map.+=(k -> v)"，返回一个scala的map。从java的TreeMap直接返回scala的map，经历了一次“偷偷”（隐式）转换。

val map: collection.mutable.Map[String, Int] = new java.util.TreeMap[String, Int]
//经过修改后，再转换scala的普通map
```
除此之外，scala还可以从java.util.Properties转成scala的Map
```scala
import collection.JavaConversions.propertiesAsScalaMap
val props : collection.Map[String, String] = System.getProperties()
```
也可以相反的转换，把scala的Map给java的API用。
```scala
import collection.JavaConversions.mapAsJavaMap
import java.awt.font.TextAttribute._ //引入java相关的枚举key

val attrs = Map(FAMILY -> "Serif", SIZE -> 12) //scala的Map
val font = new java.awt.Font(attrs) //该方法入参是一个Java的Map，这里其实是“偷偷”把attrs转成了java的Map。
```
### Tuple (元组)
元组是不同类型的值的聚集；K->V是最简单的元组。它和数组的区别是：
>* 元组内的元素不能修改。
>* 元组为不同类型的数据聚集。
>* 访问从1开始，而不是0。

元组的定义和使用非常简单
```scala
val tuple1 = (1, 3.14, "Fred")
// 本质上就是一个：val tuple1 = Tuple3[Int, Double, java.lang.String](1, 3.14, "Fred")
tuple1._2 //第二个元素，首个元素为1不是0。
tuple1 _2 //两种写都可以，只要别写成tuple1_2就可以了
```

一般，元组的元素用模式匹配来获取（后面一章会讲模式匹配）
```scala
val (first, second, third) = t
/*
first: Int = 1
second: Double = 3.14
third: String = Fred
*/

//如果只需要部分元素可以用“_”省略
val (first, second, _) = t
first: Int = 1
second: Double = 3.14
/*
first: Int = 1
second: Double = 3.14
*/
```

元组可以用在有多个返回结果的函数上。如：
```scala
"New York".partition(_.isUpper)
// 返回("NY","ew ork"),分别是满足和不满足条件的字符。
```

### 拉链操作
```scala
val symbols = Array("<", "-", ">")
val counts = Array(2, 10, 2)
val pairs = symbols.zip(counts)
//输出Array((<,2), (-,10), (>,2))
val map = pairs.toMap //变为一个Map
//输出Map("<" -> 2, "-" -> 10, ">" -> 2)
```
