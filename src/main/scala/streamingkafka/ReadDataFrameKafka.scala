package streamingkafka

import org.apache.spark.sql.SparkSession

/**
 * @Author chen.don
 * @date 2019/12/11
 */
object ReadDataFrameKafka {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("https://SparkByExamples.com")
      .getOrCreate()

    //Subscribe to topic
    val df = spark
      .read
      .format("kafka")
      .option("kafka.bootstrap.servers", "192.168.100.49:9092")
      .option("subscribe", "heima") //可以添加多个topic  heima，heima1
      //      .option("topic","heima")
      .load()

        df.printSchema()

    val df2 = df.selectExpr("CAST(key AS STRING)",
      "CAST(value AS STRING)", "topic")
    df2.show(false)


  }


}
