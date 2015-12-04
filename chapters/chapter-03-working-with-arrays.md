## chapter-03-working-with-arrays

数组

---

### 定长数组
```scala
val nums = new Array[Int](10)
//初始化10整数的数组，所有元素初始化为0
val strs = new Array[String](10)
//10个值为null
val s = Array("Hello", "Allon")
//Array对象的apply方法
s(0) = "Goodbye"
//用圆括号
```

### 变长数组
```scala
import scala.collection.mutable.ArrayBuffer
val b = ArrayBuffer[Int]()
//一个空buffer
b += 1 
//相当于b.+=(ArrayBuffer(1))
//在结尾加一个元素
b += (1,2,3)
//在结尾加一坨元素，ArrayBuffer(1,2,3)
b ++= Array(4,5,6)
//在结尾加任何集合
b.trimEnd(3)
//移除最后3个元素，
```
在数组缓冲结尾追加和移除是高效的。也可以insert一些元素，后面的元素会顺移。
```scala
b.insert(2,13)
//输出(1, 1, 13, 2, 3)
b.insert(2,31,32,33)
//输出(1, 1, 31, 32, 33, 13, 2, 3)
b.remove(2)
//输出(1, 1, 32, 33, 13, 2, 3)
b.remove(2,3)
//输出(1, 1, 2, 3)，移除下标为2的起点共3个元素
```
一般来讲，可以先构建一个ArrayBuffer然后在buffer伸缩完成后再toArray。
```scala
b.toArray
//Array(1, 1, 2, 3)
```
### 遍历Array和ArrayBuffer
常规遍历
```scala
for(i <- 0 until (b.length, 2)) println(b(i))
//隔一个搞一下
//输出
1
2
```
如果不使用下标，可以更简单的遍历，有点像java中的增强for循环。
```scala
for(elem <- b){
    println(elem)
}
```
### 数组转换
```scala
for (elem <- b) yield elem
//b是一个ArrayBuffer，yield出来的结果也将会是一个ArrayBuffer
//如果b是一个Array，结果就会产生一个Array
for(i <- 0 until 10) yield i
//这样它会产生一个Vector(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
```
以上代码都会生成一个新的集合，原集合不变。其实在scala中，也可以这样来实现：
```scala
b.filter(_ % 2 == 0).map(2 * _)
//或
b filter {_ % 2 == 0} map {2 * _}
```
### 常用算法
```scala
Array(1, 2, 3, 4, 5, 6).sum
Array(1, 2, 3, 4, 5, 6).max
```
排序
```scala
val b = ArrayBuffer(1, 7, 2, 9)
b.sorted
//返回一个ArrayBuffer(1, 2, 7, 9)
```
也可以传入一个比较函数
```scala
b.sortWith(_ < _)
//返回一个ArrayBuffer(9, 7, 2, 1)
```
对一个数组自身排序，（虽然b为val，但是它内部的元素是可以修改的）
```scala
val b = Array(1, 7, 2, 9)
util.Sorting.quickSort(b)
//b变成了Array(1, 2, 7, 9)
```
打印数组可以像python中的join方法一样（用分隔符输出），同时可以加前后缀
```scala
b.mkString(",")
//加分隔符返回 String = 1,2,7,9

b.mkString("<",",",">")
//返回 String = <1,2,7,9>
```
ArrayBuffer（不是Array）的toString方法默认不会像java一样打些没用的东西，而是带有类似mkString的输出，打印出如下效果。
```scala
val c = ArrayBuffer(1, 7, 2, 9)
c.toString
//输出ArrayBuffer(1, 7, 2, 9)
```
### 多维数组
要构建一个Array[Array[Double]]，在scala中可以使用ofDim方法：
```scala
val b = Array.ofDim[Double](3,4)//三行，四列
//输出b: Array[Array[Double]] = Array(Array(0.0, 0.0, 0.0, 0.0),Array(0.0, 0.0, 0.0, 0.0), Array(0.0, 0.0, 0.0, 0.0))
b(2)(1) = 1.111//下标为横2纵1（第3个数组中的第2个值）。
```
* 其实多维数组就是：数组的数组。所以每行长度都是可以自由定义来决定。

### 和java相互转换
scala数组可以和java数组之间来回转换。请关注隐式转换那一章。

## 习题
* 编写一段代码，将a设置为一个n个随机整数的数组，要求随机数介于0（包含）和n(不包含)之间。
```scala
def randomArray(n: Int): Array[Int] = {
    val vector = for (i <- 0 until n) yield scala.util.Random.nextInt(n)
    vector.toArray
}
```

* 编写一个循环，将整数数组中的相信元素对换，
```scala
def switchNear(arr: Array[Int]): Array[Int] = {
  val t = arr.toBuffer
  for (i <- 1 until(t.length, 2); tmp = t(i)) {
    t(i) = t(i - 1)
    t(i - 1) = tmp
  }
  t.toArray
}
```
* 重复前一个练习，不过这一次生成一个新的值交换过的数组。用for/yield。
```scala
def switchNear(arr: Array[Int]): Array[Int] = {
  val vector =
    for (i <- 0 until arr.length; tmp = arr(i)) yield if (i % 2 == 0) arr(i + 1) else arr(i - 1)
  vector.toArray
}
```

* 给定一个整数数组，产生一个新的数组，包含元数组中的所有正值，以原有顺序排列，之后的元素是所有零或负值，以原有顺序排列。
```scala
def branchArray(arr: Array[Int]): Array[Int] = {
  val pre = ArrayBuffer[Int]()
  val last = ArrayBuffer[Int]()
  arr foreach (item => if (item >= 0) pre += item else last += item)
  pre ++= last.toArray
  pre.toArray
}
//另一个方法
def branchArray2(arr: Array[Int]): Array[Int] = {
  val all = ArrayBuffer[Int]()
  all ++= arr.filter(_ >= 0)
  all ++= arr.filter(_ < 0)
  all.toArray
}
```

* 如何计算Aarray[Double]的平均值？
```scala
def avgArr(arr: Array[Double]): Double = {
  arr.sum / arr.length
}
```

* 如何将Array[Int]反序排列？对于ArrayBuffer[Int]你又会怎么做？
```scala
arr reverse
arr.toBuffer reverse
```

* 写一段代码，打印数组中的所有的值，并去掉重复项。（提示：查看Scaladoc）
```scala
//arr: Array[Int] = Array(-2, -1, 0, 1, 2, 1, 2, 3, 4, -1)
arr distinct
//res116: Array[Int] = Array(-2, -1, 0, 1, 2, 3, 4)
```

* 收集一个数组的负值下标到一个数组，反序这个数组，再去掉它最后一个元素。然后对每个下标调用remove(i)。比较和之前做法的效率。
```scala
for(i <- 0 until arr.length if arr(i)<0) yield i
c.reverse.trimStart(1)
val d = arr.toBuffer
c.foreach(d.remove)
```