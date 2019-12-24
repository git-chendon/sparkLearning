package functionDemo

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Author chen.don
 * @date 2019/12/5
 */
object MapTest {

  def main(args: Array[String]): Unit = {
    printz
    println(minNim(2,3))
    val colors = Map("red" -> "#FF0000","azure" -> "#F0FFFF")
    println(colors)
    val conf = new SparkConf().setAppName("MapTest").setMaster("local[2]")
    val sc = new SparkContext(conf)
    var rdd1=sc.parallelize(List(1,2,3,4))//创建rdd1
    var rdd2=sc.parallelize(List(5,6,7,8))//创建rdd2
    var unionRes=rdd1 union rdd2
    println(unionRes)


  }

  def printz = print("scala hello")

  def minNim(x:Int,y:Int):Int = if(x>y) x else y

}
