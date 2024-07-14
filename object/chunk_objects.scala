import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File, FileInputStream, FileOutputStream, PrintWriter}
import java.nio.file.{Files, Paths}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import scala.math.{ceil, min}
import scala.collection.mutable.ListBuffer
import java.io.{FileWriter, BufferedWriter, PrintWriter}

object PHashExample extends App {
  val pHash = new ImagePHashScala(size = 64, smallerSize = 8)

  def bufferedImageToInputStream(img: BufferedImage): ByteArrayInputStream = {
    val baos = new ByteArrayOutputStream()
    ImageIO.write(img, "jpg", baos)
    new ByteArrayInputStream(baos.toByteArray)
  }

  def processImagePair(fakeImg: BufferedImage, realImg: BufferedImage, p: Int): Int = {
    val width = fakeImg.getWidth
    val height = fakeImg.getHeight

    val subwidth = ceil(width.toDouble / p).toInt
    val subheight = ceil(height.toDouble / p).toInt

    var maxd = 0

    for (i <- 0 until p; j <- 0 until p) {
      val left = j * subwidth
      val upper = i * subheight
      val right = min((j + 1) * subwidth, width)
      val lower = min((i + 1) * subheight, height)

      val fSub = fakeImg.getSubimage(left, upper, right - left, lower - upper)
      val rSub = realImg.getSubimage(left, upper, right - left, lower - upper)

      val fSubInputStream = bufferedImageToInputStream(fSub)
      val rSubInputStream = bufferedImageToInputStream(rSub)

      val fHash = pHash.getHash(fSubInputStream)
      val rHash = pHash.getHash(rSubInputStream)

      val dist = pHash.hammingDistance(fHash, rHash)
      if(dist>maxd) maxd=dist

      fSubInputStream.close()
      rSubInputStream.close()
    }
    maxd
  }

  val basepath = "C:/Users/HP/Downloads/deepfake_images/object/"
  val subdivisions = List(1, 2, 3, 4, 5, 6)
  val names = List("object_1", "object_2", "object_3", "object_4",
                   "object_5", "object_6", "object_7", "object_8")

  val outputCsv = "scl_obj.csv"
  val writer = new PrintWriter(new FileOutputStream(outputCsv))
  writer.println("object_1,object_2,object_3,object_4,object_5,object_6,object_7,object_8")
  writer.close()

  for (p <- subdivisions) {
    val mutableList: ListBuffer[Int] = ListBuffer()
    for (name <- names) {
      val fakeImgPath = s"${basepath}${name}_fake.jpg"
      val realImgPath = s"${basepath}${name}.jpg"
      println(fakeImgPath)

      val fakeImg = ImageIO.read(new File(fakeImgPath))
      val realImg = ImageIO.read(new File(realImgPath))

      val dist = processImagePair(fakeImg, realImg, p)
      mutableList += dist
    }
    val writer = new PrintWriter(new BufferedWriter(new FileWriter(outputCsv, true)))
    try {
    // Join all elements with commas and write to CSV file
    val line = mutableList.mkString(",")
      writer.println(line)

      println(s"For $p, Elements written to $outputCsv successfully.")
    } finally {
      // Close the PrintWriter
      writer.close()
    }
  }
}