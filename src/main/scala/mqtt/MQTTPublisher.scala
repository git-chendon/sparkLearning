package mqtt


import org.apache.log4j.{Level, Logger}
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttException, MqttMessage}
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
/**
 * @Author chen.don
 * @date 2019/11/29
 */


/**
 * A simple Mqtt publisher for demonstration purposes, repeatedly publishes
 * Space separated String Message "hello mqtt demo for spark streaming"
 */
object MQTTPublisher {
  def main(args: Array[String]) :Unit = {
    if (args.length < 2) {
      System.err.println("Usage: MQTTPublisher <MqttBrokerUrl> <topic>")
      System.exit(1)
    }

    // Set logging level if log4j not configured (override by adding log4j.properties to classpath)
    if (!Logger.getRootLogger.getAllAppenders.hasMoreElements) {
      Logger.getRootLogger.setLevel(Level.WARN)
    }

    val Seq(brokerUrl, topic) = args.toSeq

    var client: MqttClient = null

    try {
      val persistence = new MemoryPersistence()
      client = new MqttClient(brokerUrl, MqttClient.generateClientId(), persistence)

      client.connect()

      val msgtopic = client.getTopic(topic)
      val msgContent = "hello mqtt demo for spark streaming"
      val message = new MqttMessage(msgContent.getBytes("utf-8"))

      while (true) {
        try {
          msgtopic.publish(message)
          println(s"Published data. topic: ${msgtopic.getName()}; Message: $message")
        } catch {
          case e: MqttException if e.getReasonCode == MqttException.REASON_CODE_MAX_INFLIGHT =>
            Thread.sleep(10)
            println("Queue is full, wait for to consume data from the message queue")
        }
      }
    } catch {
      case e: MqttException => println("Exception Caught: " + e)
    } finally {
      if (client != null) {
        client.disconnect()
      }
    }
  }
}
