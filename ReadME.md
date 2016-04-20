## Log about how to build Spark Streaming & Kafka
***
### Issues about Spark Kafka Streaming
** Way to Solve : Add the code below to pom.xml ** --[StackOverFlow](http://stackoverflow.com/questions/27710887/kafkautils-class-not-found-in-spark-streaming)

``` xml
<plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
          </descriptorRefs>
          <archive>
            <manifest>
              <mainClass></mainClass>
            </manifest>
          </archive>
        </configuration>
        <executions>
          <execution>
            <id>make-assembly</id>
            <phase>package</phase>
            <goals>
              <goal>single</goal>
            </goals>
          </execution>
        </executions>
</plugin>
```
### The Way Solve Spark-Submit
> Example Spark-Submit
spark-submit \
--master spark://ubuntu:7077 \
--deploy-mode client \
--jars /home/lin/IdeaProjects/moon/target/recommend-1.0-SNAPSHOT-jar-with-dependencies.jar \
--class com.moon.app.KafkaApp  /home/lin/IdeaProjects/moon/target/recommend-1.0-SNAPSHOT.jar \

### Example of Using Kafka

** Step 1: start Kafka Server **
``` shell
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```
** Step 2: create topic **
``` shell
bin/kafka-topics.sh --create --zookeeper localhost:2181 \
--replication-factor 1 --partitions 1 --topic test
```
** Step3: send message as Producer **
``` shell
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
```

** KafkaCount **
``` Scala
object KafkaWordCount {

}
```
