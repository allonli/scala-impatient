## Chapter-3-数组相关操作

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
### 遍历数组和数组缓冲
常规遍历
```scala
for(i <- 0 until (b.length, 2)) println(b(i))
//隔一个搞一下
//输出
1
2
```
如果不使用下标，可以更简单的遍历，有点像java中的增强for。
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
ArrayBuffer（不是Array）的toString方法会打印出如下效果，不会像java一样。
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
