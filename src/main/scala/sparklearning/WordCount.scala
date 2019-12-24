package sparklearning

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Author chen.don
 * @date 2019/12/2
 */
object WordCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("WordCount")

    val sc = new SparkContext(conf)
    println(sc.textFile("GoneWithTheWind").first())

    val text = sc.textFile("GoneWithTheWind")
    //将文本按行处理，每行按空格拆成一个数组；flatMap会将各个数组中元素合成一个大集合
    val textSplit = text.flatMap(line =>line.split(" "))
    val textSplitFlag = textSplit.map(word =>(word,1))
    val countWord = textSplitFlag.reduceByKey((x,y)=>x+y)
    countWord.saveAsTextFile("testWorkCount")

  }




}
