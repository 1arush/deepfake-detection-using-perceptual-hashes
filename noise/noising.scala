import java.io.{FileInputStream, FileOutputStream, PrintWriter}
import java.nio.file.{Files, Paths}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import scala.collection.mutable


object ImagePHashScalaExample extends App {
  val SIZE = 8
  val names = mutable.ArrayBuffer[String]()
  val order = List("f1", "f2", "m1", "m2", "o1", "o2", "o3", "o4")

  for (c <- List("f", "m", "o")) {
    val li = c match {
      case "m" | "f" => List(1, 2)
      case "o" => List(1, 2, 3, 4)
    }
    for (i <- li) {
      val ns = c + i.toString
      names += ns
    }
  }
  println(names)

  val unif = mutable.Map[String, List[Int]]()
  val gaus = mutable.Map[String, List[Int]]()

  for (name <- names) {
    unif(name) = List()
    gaus(name) = List()
  }

  val pHash = new ImagePHashScala(size = 64, smallerSize = 8)

  for (noise <- 20 to 240 by 20) {
    for (name <- names) {
      val basei = s"C:/Users/HP/Downloads/work/$name/${name}.jpg"
      val unifi = s"C:/Users/HP/Downloads/work/$name/${name}u_$noise.jpg"
      val gausi = s"C:/Users/HP/Downloads/work/$name/${name}g_$noise.jpg"

      val timg = new FileInputStream(basei)
      val tuni = new FileInputStream(unifi)
      val tgas = new FileInputStream(gausi)

      val img = pHash.getHash(timg)
      val uni = pHash.getHash(tuni)
      val gas = pHash.getHash(tgas)

      val udst = pHash.hammingDistance(img, uni)
      val gdst = pHash.hammingDistance(img, gas)

      unif(name) = unif(name) :+ udst
      gaus(name) = gaus(name) :+ gdst
    }
    println(s"Noise level $noise processed!")
  }

  saveToCSV("C:/users/hp/downloads/scl_u.csv", unif)
  saveToCSV("C:/users/hp/downloads/scl_g.csv", gaus)

  def saveToCSV(filePath: String, data: mutable.Map[String, List[Int]]): Unit = {
    val writer = new PrintWriter(new FileOutputStream(filePath))
    val headers = names.mkString(",") // Headers in the desired order
    writer.println(headers)

    // Write rows, each representing a noise level
    for (rowIdx <- data(names.head).indices) {
      val row = names.map(name => data(name)(rowIdx))
      writer.println(row.mkString(","))
    }
    writer.close()
  }
}