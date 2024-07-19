import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO
import java.io._

class ImagePHashScala(size: Int = 32, smallerSize: Int= 8){
    val c : Array[Double] = {
        val temp = Array.ofDim[Double](size)
        for (i <- 1 to size - 1) {
            temp(i) = 1
        }
        temp(0) = 1 / Math.sqrt(2.0)
        temp
    }

    val cosValue : Array[Array[Array[Array[Double]]]] = {
        val temp = Array.ofDim[Double](size, size, size, size)
        for (u <- 0 to size - 1) {
            for (v <- 0 to size - 1) {
                for (i <- 0 to size - 1) {
                    for (j <- 0 to size - 1) {
                        temp(u)(v)(i)(j) = 
                            Math.cos(((2 * i + 1) / (2.0 * size)) * u * Math.PI) * Math.cos(((2 * j + 1) / (2.0 * size)) * v* Math.PI)                    
                    }
                }
            }
        }
        temp
    }

    def getHash(is: InputStream) : String = {
        val img : BufferedImage = ImageIO.read(is)
        val img_resizedGrayed = resizeAndGrayScale(img, size, size)        
        var vals : Array[Array[Double]] = Array.ofDim[Double](size, size)

        for (x <- 0 to img_resizedGrayed.getWidth() - 1) {
            for (y <- 0 to img_resizedGrayed.getHeight() - 1) {
                vals(x)(y) = getBlue(img_resizedGrayed, x, y).toDouble
            }
        }

        val dctVals : Array[Array[Double]] = applyDCT(vals)
        var total : Double = 0

        for (x <- 0 to smallerSize - 1) {
            for (y <- 0 to smallerSize - 1) {
                total = total + dctVals(x)(y)
            }
        }

        total = total - dctVals(0)(0)

        val avg : Double = total / ((smallerSize * smallerSize) - 1)
        val hash : StringBuffer = new StringBuffer("")

        for (x <- 0 to smallerSize - 1) {
            for (y <- 0 to smallerSize - 1) {
                hash.append(
                    if(dctVals(x)(y) > avg){
                        1
                    }else{
                        0
                    }
                )
            }
        }

        return hash.toString();
    }

    private def resizeAndGrayScale(image : BufferedImage, width : Int, height : Int) : BufferedImage = {
        val resizedImage : BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY)
        val g : Graphics2D = resizedImage.createGraphics()
        g.drawImage(image, 0, 0, width, height, null)
        g.dispose()
        resizedImage
    }

    private def getBlue(img : BufferedImage, x : Int, y : Int): Int = {
        return (img.getRGB(x, y)) & 0xff
    }

    private def applyDCT(f : Array[Array[Double]]) : Array[Array[Double]] = {
        var F : Array[Array[Double]] = Array.ofDim[Double](size, size)
        for(u <- 0 to size - 1) {
            for(v <- 0 to size - 1) {
                var sum : Double = 0.0
                for(i <- 0 to size - 1) {
                    for(j <- 0 to size - 1) {                    
                        sum += cosValue(u)(v)(i)(j) * f(i)(j);
                    }
                }
                sum *= ((c(u) * c(v)) / 4.0)
                F(u)(v) = sum
            }
        }
        F
    }

    def hammingDistance(hash1: String, hash2: String): Int = {
        // Ensure both hashes have the same length
        require(hash1.length == hash2.length, "Hashes must be of equal length")

        // Initialize the distance counter
        var distance = 0

        // Iterate over corresponding characters in both hashes and count differences
        for (i <- 0 until hash1.length) {
            if (hash1.charAt(i) != hash2.charAt(i)) {
              distance += 1
            }
        }

        // Return the calculated Hamming distance
        distance
    }
}