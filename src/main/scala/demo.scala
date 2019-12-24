import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
 * @Author chen.don
 * @date 2019/11/27
 */


object demo {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local[2]")
      .appName("appName")
      .config("spark.mongodb.input.uri", "mongodb://192.168.100.55/jty_test.jty")
      .config("spark.mongodb.output.uri", "mongodb://192.168.100.55/jty_test.jty")
      .getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("ERROR")
    val readConfig = ReadConfig(Map(
      "collection" -> "jty",
      "readPreference.name" -> "primaryPreferred"),
      Some(ReadConfig(sc)
      ))
    val rdd = MongoSpark.load(sc, readConfig)
    //    val rdd = MongoSpark.load(sc)
    val df = rdd.toDF()
    val filteredDF = df.filter(df("date").leq("2019-08-28"))
    filteredDF.show()
    filteredDF.createTempView("mongo_data")
    val querySql = " SELECT school_id,GRADE,class_id,primaryLevel_id,secondaryLevel_id,thirdLevel_id,studentAnalysisDataList,'" + "2019-08-28" + "' date  FROM mongo_data"
    val str = querySql.toString
    println(str)
    val resultDF = spark.sql(querySql)
    resultDF.show()
    val value = resultDF.rdd.flatMap(
      row => {
        val school = row.getString(0)
        val grade = row.getInt(1)
        val `class` = row.getString(2)
        val primaryLevel = row.getString(3)
        val secondaryLevel = row.getString(4)
        val thirdLevel = row.getString(5)
        val students = row.getList[Row](6)
        val date = row.getAs[String]("date")
        import scala.collection.JavaConversions._
        val tuples = students
          .map(item => {
            val name = item.getAs[String]("name")
            val studentId = item.getAs[Any]("studentID").toString
            val gender = item.getAs[String]("gender")
            val aveHeartRate = item.getAs[Double]("aveHeartRate")
            val calorie = item.getAs[Int]("calorie")
            val steps = item.getAs[Int]("steps")
            val excerciseDensity = item.getAs[Double]("exerciseDensity")
            val excerciseIntensity = item.getAs[Double]("exerciseIntensity")
            var sex = 0
            if (gender.equals("女"))
              sex = 1
            ((school, grade, `class`, primaryLevel, secondaryLevel, thirdLevel, studentId, sex, date), (aveHeartRate, calorie, steps, excerciseDensity, excerciseIntensity))
          })
        tuples.toList
      })
    import spark.implicits._
    val vDF = value.toDF()
    vDF.show()
    //    value.foreach(println(_))
    val aaa = value.groupBy(_._1)
    println("xxx")
    aaa.foreach(println(_))
    val dataDF = aaa.map(row => {
      val aveHeartRate = row._2.map(_._2._1).reduce((a, b) => a + b) / row._2.toList.size.toDouble
      val aveSteps = row._2.map(_._2._3).reduce((a, b) => a + b) / row._2.toList.size.toDouble
      val aveCalorie = row._2.map(_._2._2).reduce((a, b) => a + b) / row._2.toList.size.toDouble
      val aveExcerciseDensity = row._2.map(_._2._4).reduce(_ + _) / row._2.toList.size.toDouble
      val aveExcerciseIntensity = row._2.map(_._2._5).reduce((a, b) => a + b) / row._2.toList.size.toDouble
      //      (row._1,avg1,avg2,avg3,avg4,avg5)
      val sportDataOfStudent = new JtySportDataReportMonthOfStudent(row._1._1, row._1._2, row._1._3, row._1._4, row._1._5, row._1._6, row._1._7, row._1._8, row._1._9, aveHeartRate, aveSteps, aveCalorie, aveExcerciseIntensity, aveExcerciseDensity)
      sportDataOfStudent
    }).toDF()

    dataDF.show()

    //
    //    dataDF.write
    //      .format("jdbc")
    //      .option("url", "jdbc:mysql://192.168.100.49:3306/spark")
    //      .option("driver", "com.mysql.jdbc.Driver")
    //      .option("dbtable", "jty")
    //      .option("user", "root")
    //      .option("password", "123456")
    //      .save()
    //    println("保存成功")


  }


}
