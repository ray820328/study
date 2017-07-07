内存监测jconsole
-Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=10.10.84.16

Windows
tasklist | findstr eclipse

使用jmap -dump:format=b,file=netty.bin PID 将堆内存dump出来，通过IBM的HeapAnalyzer工具进行分析，发现ByteBuf发生了泄露。

进程垃圾回收描述查看
jstat -gcutil 18071 1000

垃圾回收参数权威
http://www.fasterj.com/articles/oraclecollectors1.shtml
类加载器，网络加载安全加密
http://www.ibm.com/developerworks/cn/java/j-lo-classloader/index.html

------------------------netty--------------------------
PooledByteBufAllocator内存泄漏
http://www.cnblogs.com/zoucaitou/p/4280618.html

jar 指定任意main方法
nohup java -Xms128m -Xmx512m -server -classpath ".:commons-codec-1.10.jar:jackson-annotations-2.5.0.jar:jackson-core-2.5.4.jar:jackson-databind-2.5.4.jar:log4j-1.2.17.jar:mongo-java-driver-3.0.3.jar:netty-all-4.0.25.Final.jar:Notice.jar" com.cmge.Main > notice.out 2>&1 &

------------------------启动参数样例--------------------------
内部服务参数配置：
JAVA_OPTS="-server -XX:+UseParNewGC -Xms1024m -Xmx2048m -XX:MaxNewSize=128m -XX:NewSize=128m -XX:PermSize=96m -XX:MaxPermSize=128m -XX:+UseConcMarkSweepGC -XX:+CMSPermGenSweepingEnabled -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:CMSInitiatingOccupancyFraction=1 -XX:+CMSIncrementalMode -XX:MaxTenuringThreshold=0 -XX:SurvivorRatio=20000 -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0  -XX:CMSIncrementalDutyCycleMin=10 -XX:CMSIncrementalDutyCycle=30 -XX:CMSMarkStackSize=8M -XX:CMSMarkStackSizeMax=32M"
前端应用参数配置：
 JAVA_OPTS="-server  -Xmx4096m -Xms4096m -Xmn480m -Xss256k -XX:PermSize=128m -XX:MaxPermSize=256m -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=8 -XX:CMSFullGCsBeforeCompaction=0 
-XX:+UseCMSCompactAtFullCollection -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=7 -XX:GCTimeRatio=19 
-Xnoclassgc -XX:+DisableExplicitGC -XX:+UseParNewGC -XX:-CMSParallelRemarkEnabled -XX:CMSInitiatingOccupancyFraction=70 -XX:SoftRefLRUPolicyMSPerMB=0" 
参数说明：
-Xmx1280m：设置JVM最大可用内存为1280m。最大可设为3550m。具体应用可适当调整。
-Xms1280m：设置JVM初始内存为1280m。此值可以设置与-Xmx相同，以避免每次垃圾回收完成后JVM重新分配内存。
-Xmn480m：设置年轻代大小为480m。整个堆大小=年轻代大小 + 年老代大小 + 持久代大小。持久代一般固定大小为64m，所以增大年轻代后，将会减小年老代大小。此值对系统性能影响较大，Sun官方推荐配置为整个堆的3/8。
-Xss256k：设置每个线程的堆栈大小。JDK5.0以后每个线程堆栈大小为1M，以前每个线程堆栈大小为256K。更具应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。
-XX:PermSize=64m：指定 jvm 中 Perm Generation 的最小值。 这个参数需要看你的实际情况。可以通过jmap 命令看看到底需要多少。
-XX:MaxPermSize=128m：指定 Perm Generation 的最大值
-XX:+UseConcMarkSweepGC：设置并发收集器
-XX:ParallelGCThreads=8：配置并行收集器的线程数，即：同时多少个线程一起进行垃圾回收。此值最好配置与处理器数目相等。
-XX:CMSFullGCsBeforeCompaction=0：由于并发收集器不对内存空间进行压缩、整理，所以运行一段时间以后会产生“碎片”，使得运行效率降低。此值设置运行多少次GC以后对内存空间进行压缩、整理。
-XX:+UseCMSCompactAtFullCollection：打开对年老代的压缩。可能会影响性能，但是可以消除碎片。
-XX:SurvivorRatio=8：每个survivor space 和 eden之间的比例。
-XX:MaxTenuringThreshold=7：设置垃圾最大年龄。如果设置为0的话，则年轻代对象不经过Survivor区，直接进入年老代。对于年老代比较多的应用，可以提高效率。如果将此值设置为一个较大值，则年轻代对象会在Survivor区进行多次复制，这样可以增加对象再年轻代的存活时间，增加在年轻代即被回收的概率。
-XX:GCTimeRatio=19：设置垃圾回收时间占程序运行时间的百分比，公式为1/(1+n)。
-Xnoclassgc：禁用类垃圾回收，性能会有一定提高。
-XX:+DisableExplicitGC：当此参数打开时，在程序中调用System.gc()将会不起作用。默认是off。
-XX:+UseParNewGC：设置年轻代为并行收集。可与CMS收集同时使用。
-XX:-CMSParallelRemarkEnabled：在使用 UseParNewGC 的情况下 , 尽量减少 mark 的时间。
-XX:CMSInitiatingOccupancyFraction=70：指示在 old generation 在使用了 70% 的比例后 , 启动 concurrent collector。
-XX:SoftRefLRUPolicyMSPerMB=0：每兆堆空闲空间中SoftReference的存活时间

-----------------------------------centos top显示占用虚拟内存太大的问题-----------------------------------
There is a known problem with Java and glibc >= 2.10 (includes Ubuntu >= 10.04, RHEL >= 6).
The cure is to set this env. variable: 
export MALLOC_ARENA_MAX=4
If you are running Tomcat, you can add this to TOMCAT_HOME/bin/setenv.sh file.

There is an IBM article about setting MALLOC_ARENA_MAX https://www.ibm.com/developerworks/community/blogs/kevgrig/entry/linux_glibc_2_10_rhel_6_malloc_may_show_excessive_virtual_memory_usage?lang=en

This blog post says
resident memory has been known to creep in a manner similar to a memory leak or memory fragmentation.
search for MALLOC_ARENA_MAX on Google or SO for more references.

You might want to tune also other malloc options to optimize for low fragmentation of allocated memory:
# tune glibc memory allocation, optimize for low fragmentation
# limit the number of arenas
export MALLOC_ARENA_MAX=2
# disable dynamic mmap threshold, see M_MMAP_THRESHOLD in "man mallopt"
export MALLOC_MMAP_THRESHOLD_=131072
export MALLOC_TRIM_THRESHOLD_=131072
export MALLOC_TOP_PAD_=131072
export MALLOC_MMAP_MAX_=65536
