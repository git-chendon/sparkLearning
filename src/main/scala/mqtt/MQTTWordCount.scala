package mqtt

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.mqtt.MQTTUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @Author chen.don
 * @date 2019/11/29
 */

/**
 * A sample wordcount with MQTTInputDStream
 *
 * Usage: MQTTWordCount <MqttbrokerUrl> <topic>
 *
 * To run this example on your local machine, you first need to setup a MQTT broker and publisher,
 * like Mosquitto (http://mosquitto.org/) an easy to use and install open source MQTT Broker.
 * On Mac OS, Mosquitto can be installed with Homebrew `$ brew install mosquitto`.
 * On Ubuntu, Mosquitto can be installed with the command `$ sudo apt-get install mosquitto`.
 *
 * Alternatively, checkout the Eclipse paho project which provides a number of clients and utilities
 * for working with MQTT (http://www.eclipse.org/paho/#getting-started).
 *
 * How to run this example locally:
 *
 * (1) Start a MQTT message broker/server, i.e. Mosquitto:
 *
 *    `$ mosquitto -p 1883`
 *
 * (2) Run the publisher:
 *
 *    `$ bin/run-example \
 *      org.apache.spark.examples.streaming.mqtt.MQTTPublisher tcp://localhost:1883 foo`
 *
 * (3) Run the example:
 *
 *    `$ bin/run-example \
 *      org.apache.spark.examples.streaming.mqtt.MQTTWordCount tcp://localhost:1883 foo`
 */
object MQTTWordCount {
  def main(args: Array[String]) {
    if (args.length < 2) {
      // scalastyle:off println
      System.err.println(
        "Usage: MQTTWordCount <MqttbrokerUrl> <topic>")
      // scalastyle:on println
      System.exit(1)
    }

    val Seq(brokerUrl, topic) = args.toSeq
    val sparkConf = new SparkConf().setAppName("MQTTWordCount")

    // check Spark configuration for master URL, set it to local if not configured
    if (!sparkConf.contains("spark.master")) {
      sparkConf.setMaster("local[2]")
    }

    val ssc = new StreamingContext(sparkConf, Seconds(2))
    val lines = MQTTUtils.createStream(ssc, brokerUrl, topic, StorageLevel.MEMORY_ONLY_SER_2)
    val words = lines.flatMap(x => x.split(" "))
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)

    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
