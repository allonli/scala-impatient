import java.awt.Rectangle
import java.awt.geom._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by liyonghui on 2015/10/23.
 */
/**
 * 18.1 ��������
 */
object Test18 {

  def main(args: Array[String]) {
    val t = new Test18_1
    val book = new Book
    book.setTitle("sdfsdf")
  }

  //18.2 ����ͶӰ
  val qq = new Network
  //qq.Member��chatter.Memer����ͬһ����
  val chatter = new Network

  val fred = qq.join("Fred")
  //����fred��һ��Member����
  val barney = chatter.join("Barney") //����fred��һ��Member����,���������meber������Ĳ���ͬһ������
  //fred.contacts += barney������ô�ᱨ����Ϊbarney��fred����ͬһ��Member��Ķ���
  //Ҫ����������⣬��Ҫʹ������ͶӰ���ڷ���Network#Member��ʾΪ�κ�Network��Member�Ͳ��ᱨ����
  fred.contacts += barney

  //18.4 ���ͱ���
  type Index = mutable.HashMap[String, (Int, Int)]
  val idex = new Index
  idex += ("1" ->(1, 1))

  //18.5 �ṹ����(Ѽ������)
  val tar = new Target
  val ls = new ArrayBuffer[String]()
  ls +=("1vvvvv", "212312", "31asfdasf")
  appendLines(tar, ls.iterator)

  def appendLines(target: {def append(str: String): Any}, lines: Iterator[String]): Unit = {
    for (l <- lines) {
      target.append(l); target.append("\n")
    }
  }

  //18.6 ��������
  val image = new ArrayBuffer[java.awt.Shape with java.io.Serializable]
  val rect = new Rectangle(5, 10, 20, 30)
  val rect2 = new Tmp
  image += rect // ok

  //image += new Area(rect) ����ᱨ����ΪAreaֻ��һ��Shape������Serializable��
  //���ϴ����һ��˵����extends A with B with C����һ��B with C����
  val image2 = new ArrayBuffer[java.awt.Shape with java.io.Serializable {def contains(str: String): Boolean}]
  image2 += rect2 //�ڸ���������Ӧ��Ѽ������
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
 * 18.2 ����ͶӰ
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