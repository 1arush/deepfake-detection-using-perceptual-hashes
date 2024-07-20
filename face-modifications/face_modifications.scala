import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File, FileInputStream, FileOutputStream, PrintWriter}
import java.nio.file.{Files, Paths}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import scala.math.{ceil, min}
import scala.collection.mutable.ListBuffer
import java.io.{FileWriter, BufferedWriter, PrintWriter}

// The scala code write incorrectly to csv
// We must carry out a transpose operation on the data
// It writes a column as a row, however the provided csv files have been corrected


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

  val basePath = "C:/Users/HP/Downloads/images/"
  val pathNames = List("m1", "m2", "f1", "f2")
  val iterations = List("eyes", "nose", "nose_code", "lips", "lips_code")
  val outputCsvTemplate = "%s_face.csv"

  def processImagesForName(name: String): Unit = {
    val result = new ListBuffer[String]()

    for (iter <- iterations) {
      val distances = new ListBuffer[Int]()
      val baseImgPath = s"$basePath$name/base/base.jpg"
      val baseImg = ImageIO.read(new File(baseImgPath))

      if (iter.contains("eyes")) {
        for (p <- 1 to 5) {
          var maxDist = 0
          for (num <- 0 until 2) {
            val realImgPath = s"$basePath$name/base/eyebox_$num.jpg"
            val fakeImgPath = s"$basePath$name/$iter/eyebox_$num.jpg"
            val realImg = ImageIO.read(new File(realImgPath))
            val fakeImg = ImageIO.read(new File(fakeImgPath))
            val dist = processImagePair(fakeImg, realImg, p)
            if (dist > maxDist) maxDist = dist
          }
          distances += maxDist
        }
      } else {
        for (p <- 1 to 5) {
          val st = if (iter.contains("lips")) "lip" else "nose"
          val realImgPath = s"$basePath$name/base/${st}box.jpg"
          val fakeImgPath = s"$basePath$name/$iter/${st}box.jpg"
          val realImg = ImageIO.read(new File(realImgPath))
          val fakeImg = ImageIO.read(new File(fakeImgPath))
          val dist = processImagePair(fakeImg, realImg, p)
          distances += dist
        }
      }
      result += distances.mkString(",")
    }

    val outputCsv = outputCsvTemplate.format(name)
    val writer = new PrintWriter(new BufferedWriter(new FileWriter(outputCsv)))
    writer.println("eyes,nose,nose_code,lips,lips_code")
    for (line <- result) {
      writer.println(line)
    }
    writer.close()
  }

  for (name <- pathNames) {
    processImagesForName(name)
  }
}