内存监测jconsole
-Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=10.10.84.16

Windows
tasklist | findstr eclipse

使用jmap -dump:format=b,file=netty.bin PID 将堆内存dump出来，通过IBM的HeapAnalyzer工具进行分析，发现ByteBuf发生了泄露。

进程垃圾回收描述查看
jstat -gcutil 18071 1000

垃圾回收参数权威
http://www.fasterj.com/articles/oraclecollectors1.shtml

------------------------netty--------------------------
PooledByteBufAllocator内存泄漏
http://www.cnblogs.com/zoucaitou/p/4280618.html

jar 指定任意main方法
nohup java -Xms128m -Xmx512m -server -classpath ".:commons-codec-1.10.jar:jackson-annotations-2.5.0.jar:jackson-core-2.5.4.jar:jackson-databind-2.5.4.jar:log4j-1.2.17.jar:mongo-java-driver-3.0.3.jar:netty-all-4.0.25.Final.jar:Notice.jar" com.cmge.Main > notice.out 2>&1 &

