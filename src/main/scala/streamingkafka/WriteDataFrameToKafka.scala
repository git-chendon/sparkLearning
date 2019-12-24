package streamingkafka

import org.apache.spark.sql.SparkSession

/**
 * @Author chen.don
 * @date 2019/12/11
 */
object WriteDataFrameToKafka {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExamples.com")
      .getOrCreate()

    val data = Seq(
      ("iphone","2007"),("iphone 3g","2008"),
      ("iphone 3gs","2009"),("iphone 4","2010"),
      ("iphone 4s","2011"),("iphone 5","2012"),
      ("iphone 8","20014"),("iphone 10","2017")
    )

    val df = spark.createDataFrame(data).toDF("key","value")
    /*
        since we are using dataframe which is already in text,
        selectExpr is optional.
        If the bytes of the Kafka records represent UTF8 strings,
        we can simply use a cast to convert the binary data
        into the correct type.

        df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      */

    df.write
      .format("kafka")
      .option("kafka.bootstrap.servers","192.168.100.49:9092")
      .option("topic","heima")
      .save()

  }



}
