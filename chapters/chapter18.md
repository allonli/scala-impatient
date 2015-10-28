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

### 18.6 复合类型
	val image = new ArrayBuffer[java.awt.Shape with java.io.Serializable]
	val rect = new Rectangle(5, 10, 20, 30)
	val rect2 = new Tmp
	image += rect // ok

  //image += new Area(rect) 这里会报错，因为Area只是一个Shape但不是Serializable的
  //以上代码进一步说明了extends A with B with C中是一个B with C整体
  
	val image2 = new ArrayBuffer[java.awt.Shape with java.io.Serializable {def contains(str: String): Boolean}]
  //在复合类型中应用鸭子类型
  
	image2 += rect2 
	for (img<-image2){
		img.contains("xxxx")
	}

### 18.7 中置类型
  //定义Person类，两个泛型参数，分别是S，T,因此
  //它是可以用中置表达式进行变量定义的
  
    case class Person[S, T](val name: S, val age: T)

    object InfixType extends App {
    //下面的代码是一种中置表达方法，相当于
    //val p:Person[String,Int]
    val p: String Person Int = Person("摇摆少年梦", 18)
    //中置表达式的模式匹配用法
    //模式匹配时可以直接用常量，也可以直接用变量
    p match {
      case "摇摆少年梦" Person 18 => println("matching is ok")
      case name Person age => println("name:" + name + "  age=" + age)
    }
    }

### 18.8 存在类型 Existential Types
  //forsome提供了常量n，以供泛型使用。那么M卽为内部类Member的子类，同一类的不同对象的内部类不相同，所以这里的m1和m2必须为同一个Network下的对象。
    
    def process[M <: n.Member forSome {val n : Network}](m1: M, m2: M) = (m1, m2)
    process(qq.join("xxx"),qq.join("xxxxxxx"))
  //下面这句就会报错，因为wx和qq不是同一个Network
  
    process(wx.join("xxx"),qq.join("xxxxxxx"))


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
