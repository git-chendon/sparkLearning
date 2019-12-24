import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Author chen.don
 * @date 2019/11/26
 */


object SimpleApp {
  def main(args: Array[String]) {
    val logFile = "README.md" // 应该是你系统上的某些文件
    val conf = new SparkConf().setAppName("SimpleApp").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
  }
}
