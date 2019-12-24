import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig

/**
 * @Author chen.don
 * @date 2019/11/26
 */
object MongoSql {
  def main(args: Array[String]): Unit = {

    import org.apache.spark.sql.SparkSession



    /* For Self-Contained Scala Apps: Create the SparkSession
     * CREATED AUTOMATICALLY IN spark-shell */
    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("MongoSparkConnectorIntro")
      .config("spark.mongodb.input.uri", "mongodb://192.168.100.99/test.characters")
      .config("spark.mongodb.output.uri", "mongodb://192.168.100.99/test.characters")
      .getOrCreate()

//    val df = MongoSpark.load(sparkSession)
//    df.printSchema()
//    df.show()

//    df.filter(df("age") < 100).show()
//
//    val explicitDF = MongoSpark.load[Character](sparkSession)
//    explicitDF.printSchema()
//    explicitDF.show()
//
//    val rdd = MongoSpark.load(sparkSession.sparkContext)
//    val dfInferredSchema = rdd.toDF()
//    val dfExplicitSchema = rdd.toDF[Character]()
//    val ds = rdd.toDS[Character]()
//    println("xxx")
//    dfInferredSchema.show()
//    dfExplicitSchema.show()
//    ds.show()


    val characters = MongoSpark.load[Character](sparkSession)

    characters.createOrReplaceTempView("characters")  //没有这个view不能执行sql

    val centenarians = sparkSession.sql("SELECT name, age FROM characters WHERE age >= 100")
    centenarians.show()


    MongoSpark.save(centenarians.write.option("collection", "hundredClub").mode("overwrite"))
//    centenarians.write.option("collection", "hundredClub").mode("overwrite").mongo()
    centenarians.write.option("collection", "hundredClub").mode("overwrite").format("mongo").save()

    println("Reading from the 'hundredClub' collection:")
    MongoSpark.load[Character](sparkSession, ReadConfig(Map("collection" -> "hundredClub"), Some(ReadConfig(sparkSession)))).show()

    import com.mongodb.spark._
    import com.mongodb.spark.config._
    import org.bson.Document


    //
    //    val docs = """
    //      {"name": "Bilbo Baggins", "age": 50}
    //      {"name": "Gandalf", "age": 1000}
    //      {"name": "Thorin", "age": 195}
    //      {"name": "Balin", "age": 178}
    //      {"name": "Kíli", "age": 77}
    //      {"name": "Dwalin", "age": 169}
    //      {"name": "Óin", "age": 167}
    //      {"name": "Glóin", "age": 158}
    //      {"name": "Fíli", "age": 82}
    //      {"name": "Bombur"}""".trim.stripMargin.split("[\\r\\n]+").toSeq
    //    sparkSession.sparkContext.parallelize(docs.map(Document.parse)).saveToMongoDB()

    // Additional operations go here...

  }
}
