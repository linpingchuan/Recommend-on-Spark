## Log about how to build Spark Streaming & Kafka
***
### Issues about Spark Kafka Streaming
** Way to Solve : Add the code below to pom.xml **--[StackOverFlow](http://stackoverflow.com/questions/27710887/kafkautils-class-not-found-in-spark-streaming)

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

