import org.apache.spark._ 
import org.apache.spark.SparkContext._ 
import org.apache.spark.streaming._ 
import org.apache.spark.streaming.twitter._ 
import org.apache.spark.streaming.StreamingContext._
import TutorialHelper._ 
import org.apache.spark.sql._
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import java.sql.Connection
import java.sql.Statement
import org.apache.commons.dbcp2._
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.util.Logging
import com.datastax.spark.connector.embedded._
import com.datastax.spark.connector._
import com.datastax.spark.connector.streaming._

object Tutorial {
  def main(args: Array[String]) {

    //The sparkconf stores configurationg paramaters that the spark driver passes to spark context
    val conf = new SparkConf().setMaster("local[2]").setAppName("Tutorial").set("spark.cassandra.connection.host", "127.0.0.1")

    //creating a spark context. A spark context allows your spark driver to access the cluster through a resource manager
    val sc = new SparkContext(conf)

    //creating a sqlcontext by passing it to an existing spark context
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

      /** Creates the keyspace and table in Cassandra. */
    CassandraConnector(conf).withSessionDo { session =>
    session.execute(s"DROP KEYSPACE IF EXISTS demo")
    session.execute(s"CREATE KEYSPACE IF NOT EXISTS demo WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}")
    session.execute(s"CREATE TABLE IF NOT EXISTS demo.wordcount (word TEXT PRIMARY KEY, count COUNTER)")
    session.execute(s"TRUNCATE demo.wordcount")
    }


    // Configure Twitter credentials
    val apiKey = "sxFS1fDGzLnHiJRFBj1aVoeIk"
    val apiSecret = "XXNxbEWCYFuAGmsHkSM30wdTs8ndMLIgo7vKT2iYk5eyCWJzh2"
    val accessToken = "4352160262-VX2emCu8MjEWFEhKoIJ9toJ5eB2n2Be6BKwchPW"
    val accessTokenSecret = "2BKwdQptKGMwB3tngBH7GkIq8WVdPZYM0NcHAKSdEK1M8"

    TutorialHelper.configureTwitterCredentials(apiKey, apiSecret, accessToken, accessTokenSecret)
    // Your code goes here

    val ssc = new StreamingContext(sc, Seconds(4))

    val tweets = TwitterUtils.createStream(ssc, None, Array("Boko", "Haram", "BokoHaram", "Boko Haram"))

    val words = tweets.map(status => status.getText())

    val pairs =  words.map(word => (word,1))
        .saveToCassandra( "demo",  "wordcount", SomeColumns("word", "count"))

    words.print()
    ssc.start()
    ssc.awaitTermination()
  }
}