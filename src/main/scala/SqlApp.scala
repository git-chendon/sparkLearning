import java.util.Properties

import org.apache.spark.SparkContext
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

/**
 * @Author chen.don
 * @date 2019/11/26
 */
object SqlApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[2]")
      .getOrCreate()
    val df = spark.read.json("src/main/resources/people.json")
    df.show()
    df.printSchema()

    spark.udf.register("myAverage",MyAverage)
    df.createOrReplaceTempView("people")
    df.show()

    val result = spark.sql("select myAverage(age) as average_age from people")
    result.show()


    val ds = spark.read.json("src/main/resources/Employee.json")
    ds.show()
    val averageSalary = MyAverage2.toColumn.name("average_salary")
    val result2 = ds.select(averageSalary)
    result2.show()
    //    val jdbcDF = spark.read.format("jdbc").
    //      option("url", "jdbc:mysql://192.168.100.49:3306/spark").
    //      option("driver","com.mysql.jdbc.Driver").
    //      option("dbtable", "student").
    //      option("user", "root").
    //      option("password", "123456").
    //      load()
    //
    //    jdbcDF.show()
    //下面我们设置两条数据表示两个学生信息
//    val studentRDD = spark.sparkContext.parallelize(Array("3 Rongcheng M 26", "4 Guanhua M 27")).map(_.split(" "))
//
//    //下面要设置模式信息
//    val schema = StructType(List(StructField("id", IntegerType, true), StructField("name", StringType, true), StructField("gender", StringType, true), StructField("age", IntegerType, true)))
//
//    //下面创建Row对象，每个Row对象都是rowRDD中的一行
//    val rowRDD = studentRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).trim, p(3).toInt))
//
//    //建立起Row对象和模式之间的对应关系，也就是把数据和模式对应起来
//    val studentDF = spark.createDataFrame(rowRDD, schema)
//
//    //下面创建一个prop变量用来保存JDBC连接参数
//    val prop = new Properties()
//    prop.put("user", "root") //表示用户名是root
//    prop.put("password", "123456") //表示密码是hadoop
//    prop.put("driver", "com.mysql.jdbc.Driver") //表示驱动程序是com.mysql.jdbc.Driver
//
//    //下面就可以连接数据库，采用append模式，表示追加记录到数据库spark的student表中
//    studentDF.write.mode("append").jdbc("jdbc:mysql://192.168.100.49:3306/spark", "spark.student", prop)

  }
}
