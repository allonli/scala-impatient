  ### 18.1 单例类型
  def main(args: Array[String]) {
    val t = new Test18_1
    val book = new Book
    book.setTitle("sdfsdf")
  }

  ### 18.2 类型投影
  val qq = new Network
  //qq.Member和chatter.Memer不是同一个类
  val chatter = new Network

  val fred = qq.join("Fred")
  //这里fred是一个Member对象
  val barney = chatter.join("Barney") //这里fred是一个Member对象,但是这里的meber和上面的不是同一个类了
  //fred.contacts += barney这里这么会报错，因为barney和fred不是同一个Member类的对象
  //要解决上述问题，需要使用类型投影。在泛型Network#Member表示为任何Network的Member就不会报错了
  fred.contacts += barney

  ### 18.4 类型别名
  type Index = mutable.HashMap[String, (Int, Int)]
  val idex = new Index
  idex += ("1" ->(1, 1))

  ### 18.5 结构类型(鸭子类型)
  val tar = new Target
  val ls = new ArrayBuffer[String]()
  ls +=("1vvvvv", "212312", "31asfdasf")
  appendLines(tar, ls.iterator)

  def appendLines(target: {def append(str: String): Any}, lines: Iterator[String]): Unit = {
    for (l <- lines) {
      target.append(l); target.append("\n")
    }
  }

  ### 18.6 复合类型
  val image = new ArrayBuffer[java.awt.Shape with java.io.Serializable]
  val rect = new Rectangle(5, 10, 20, 30)
  val rect2 = new Tmp
  image += rect // ok

  //image += new Area(rect) 这里会报错，因为Area只是一个Shape但不是Serializable的
  //以上代码进一步说明了extends A with B with C中是一个B with C整体
  val image2 = new ArrayBuffer[java.awt.Shape with java.io.Serializable {def contains(str: String): Boolean}]
  image2 += rect2 //在复合类型中应用鸭子类型
  for (img<-image2){
    img.contains("xxxx")
  }


}


class Test18_1 {
  def setTitle(title: String): this.type = {
    this
  }

  def setAuthor(title: String) = {
    this
  }
}

class Book extends Test18_1 {
  def addChapter(chapter: String) = {
    this
  }
}


/**
 * 18.2 类型投影
 */
class Network {

  class Member(val name: String) {
    val contacts = new ArrayBuffer[Network#Member]
  }

  object Member

  private val members = new ArrayBuffer[Member]

  def join(name: String) = {
    val m = new Member(name)
    members += m
    m
  }
}

/**
 * 18.5
 */
class Target {
  def append(str: String): Any = {
    println(str)
  }
}

/**
 * 18.6
 */
trait TmpContains {
  def contains(str: String): Boolean
}

class Tmp extends java.awt.Shape with java.io.Serializable with TmpContains {
  override def getBounds2D: Rectangle2D = null

  override def getPathIterator(affineTransform: AffineTransform): PathIterator = null

  override def getPathIterator(affineTransform: AffineTransform, v: Double): PathIterator = null

  override def contains(v: Double, v1: Double): Boolean = false

  override def contains(point2D: Point2D): Boolean = false

  override def contains(v: Double, v1: Double, v2: Double, v3: Double): Boolean = false

  override def contains(rectangle2D: Rectangle2D): Boolean = false

  override def intersects(v: Double, v1: Double, v2: Double, v3: Double): Boolean = false

  override def intersects(rectangle2D: Rectangle2D): Boolean = false

  override def getBounds: Rectangle = null

  override def contains(str: String): Boolean = false
}
