
import org.apache.spark.SparkContext
import org.bson.Document
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config._
import scala.collection.JavaConverters._

/**
 * @Author chen.don
 * @date 2019/11/26
 */
object MongoApp {
  def main(args: Array[String]): Unit = {
    /* Create the SparkSession.
     * If config arguments are passed from the command line using --conf,
     * parse args for the values to set.
     */
    import org.apache.spark.sql.SparkSession
    val spark = SparkSession.builder()
      .master("local")
      .appName("MongoSparkConnectorIntro")
      .config("spark.mongodb.input.uri", "mongodb://192.168.100.99/test.myCollection")
      .config("spark.mongodb.output.uri", "mongodb://192.168.100.99/test.myCollection")
      .getOrCreate()

    val sc = spark.sparkContext

    //写操作1
//    val documents = sc.parallelize((1 to 10).map(i => Document.parse(s"{test: $i}")))
//    MongoSpark.save(documents) // Uses the SparkConf for configuration
//
//    val writeConfig = WriteConfig(Map("collection" -> "spark", "writeConcern.w" -> "majority"), Some(WriteConfig(sc)))
//    val sparkDocuments = sc.parallelize((1 to 10).map(i => Document.parse(s"{spark: $i}")))
//    MongoSpark.save(sparkDocuments, writeConfig)

    //    val documents = sc.parallelize(
    //      Seq(new Document("fruits", List("apples", "oranges", "pears").asJava  ))  //这里list不支持
    //    )
    //    MongoSpark.save(documents)
    //读操作
    val rdd = MongoSpark.load(sc) //全部加载进来
    println(rdd.count)
    println(rdd.first.toJson)
//    val filteredRdd = rdd.filter(doc => doc.getInteger("test") > 5)
//    println(filteredRdd.count())
//    println(filteredRdd.first.toJson)
//
//    val aggregatedRdd = rdd.withPipeline(Seq(Document.parse("{$match: { test : {$gt :5 } } }")))
//    println(aggregatedRdd.count())
//    println(aggregatedRdd.first.toJson)
//
//    val readConfig = ReadConfig(Map("collection" -> "spark", "readPreference.name" -> "secondaryPreferred"), Some(ReadConfig(sc)))
//    val customRdd = MongoSpark.load(sc, readConfig)
//
//
//    println(customRdd.count)
//    println(customRdd.toDF())


  }
}
