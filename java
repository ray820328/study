内存监测jconsole
-Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=10.10.84.16

Windows
tasklist | findstr eclipse

使用jmap -dump:format=b,file=netty.bin PID 将堆内存dump出来，通过IBM的HeapAnalyzer工具进行分析，发现ByteBuf发生了泄露。

Java 类的热替换 —— 概念、设计与实现
https://www.ibm.com/developerworks/cn/java/j-lo-hotswapcls/index.html

进程垃圾回收描述查看
jstat -gcutil 18071 1000

无锁高并发disruptor框架
https://github.com/trevorbernard/disruptor-examples

垃圾回收参数权威
http://www.fasterj.com/articles/oraclecollectors1.shtml
类加载器，网络加载安全加密
http://www.ibm.com/developerworks/cn/java/j-lo-classloader/index.html

Java线程与Linux内核线程的映射关系
http://blog.sina.com.cn/s/blog_605f5b4f010198b5.html

堆外内存使用，某一些情况是可以通过JVM的特定参数可以拿到 如jinfo， 还可以使用btrace 
查看api性能可以使用oprofile， 或者淘宝的tprofiler

虚拟内存太高可能原因
unix操作系统提供了一种非常高效的途径来实现页面缓存和socket之间的数据传递。在Linux操作系统中，
这种方式被称作：sendfile system call（Java提供了访问这个系统调用的方法：FileChannel.transferTo api）。

针对tomcat上的应用的. 其他的java程序, 只要你能触发他的thread dump并且拿到结果, 也是一样.
1. ps -ef | grep java找到你的java程序的进程id, 定位 pid
2. top -Hp $pidshift+t 查看耗cpu时间最多的几个线程, 记录下线程的id
3. 把上诉线程ID转换成16进制小写  比如  : 0x12ef
4. kill -3 $pid  触发tomcat的thread dump
5. 找到tomcat的catalin.out 日志, 把 上面几个线程对应的代码段拿出来.

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






Netty之有效规避内存泄漏
文章来自http://calvin1978.blogcn.com/articles/netty-leak.html 2016-01-15 546浏览
游戏开发GC培养
想免费获取内部独家PPT资料库？观看行业大牛直播？点击加入腾讯GAD游戏开发行业精英群711501594
　　有过痛苦的经历，特别能写出深刻的文章 —— 凯尔文. 肖
　　直接内存是IO框架的绝配，但直接内存的分配销毁不易，所以使用内存池能大幅提高性能，也告别了频繁的GC。但，要重新培养被Java的自动垃圾回收惯坏了的惰性。
　　Netty有一篇必读的文档 官方文档翻译：引用计数对象 ，在此基础上补充一些自己的理解和细节。

一、为什么要有引用计数器
　　Netty里四种主力的ByteBuf，其中UnpooledHeapByteBuf 底下的byte[]能够依赖JVM GC自然回收；而UnpooledDirectByteBuf底下是DirectByteBuffer，如Java堆外内存扫盲贴所述，除了等JVM GC，最好也能主动进行回收；而PooledHeapByteBuf 和 PooledDirectByteBuf，则必须要主动将用完的byte[]/ByteBuffer放回池里，否则内存就要爆掉。所以，Netty ByteBuf需要在JVM的GC机制之外，有自己的引用计数器和回收过程。
　　一下又回到了C的冰冷时代，自己malloc对象要自己free。 但和C时代又不完全一样，内有引用计数器，外有JVM的GC，情况更为复杂。

二、引用计数器常识
  •  计数器基于 AtomicIntegerFieldUpdater，为什么不直接用AtomicInteger？因为ByteBuf对象很多，如果都把int包一层AtomicInteger花销较大，而AtomicIntegerFieldUpdater只需要一个全局的静态变量。
  •  所有ByteBuf的引用计数器初始值为1。
  •  调用release()，将计数器减1，等于零时， deallocate()被调用，各种回收。
  •  调用retain()，将计数器加1，即使ByteBuf在别的地方被人release()了，在本Class没喊cut之前，不要把它释放掉。
  •  由duplicate(), slice()和order()所衍生的ByteBuf，与原对象共享底下的buffer，也共享引用计数器，所以它们经常需要调用retain()来显示自己的存在。
  •  当引用计数器为0，底下的buffer已被回收，即使ByteBuf对象还在，对它的各种访问操作都会抛出异常。

三、谁来负责Release
　　在C时代，我们喜欢让malloc和free成对出现，而在Netty里，因为Handler链的存在，ByteBuf经常要传递到下一个Hanlder去而不复还，所以规则变成了谁是最后使用者，谁负责释放。
另外，更要注意的是各种异常情况，ByteBuf没有成功传递到下一个Hanlder，还在自己地界里的话，一定要进行释放。
1、InBound Message
　　在AbstractNioByteChannel.NioByteUnsafe.read() 处创建了ByteBuf并调用pipeline.fireChannelRead(byteBuf) 送入Handler链。
　　根据上面的谁最后谁负责原则，每个Handler对消息可能有三种处理方式：
  •  对原消息不做处理，调用 ctx.fireChannelRead(msg)把原消息往下传，那不用做什么释放。
  •  将原消息转化为新的消息并调用 ctx.fireChannelRead(newMsg)往下传，那必须把原消息release掉。
  •  如果已经不再调用ctx.fireChannelRead(msg)传递任何消息，那更要把原消息release掉。
　　假设每一个Handler都把消息往下传，Handler并也不知道谁是启动Netty时所设定的Handler链的最后一员，所以Netty在Handler链的最末补了一个TailHandler，如果此时消息仍然是ReferenceCounted类型就会被release掉。
2、OutBound Message
　　要发送的消息由应用所创建，并调用 ctx.writeAndFlush(msg) 进入Handler链。在每个Handler中的处理类似InBound Message，最后消息会来到HeadHandler，再经过一轮复杂的调用，在flush完成后终将被release掉。
3、异常发生时的释放
　　多层的异常处理机制，有些异常处理的地方不一定准确知道ByteBuf之前释放了没有，可以在释放前加上引用计数大于0的判断避免释放失败；有时候不清楚ByteBuf被引用了多少次，但又必须在此进行彻底的释放，可以循环调用reelase()直到返回true。

四、内存泄漏检测
　　所谓内存泄漏，主要是针对池化的ByteBuf。ByteBuf对象被JVM GC掉之前，没有调用release()把底下的DirectByteBuffer或byte[]归还到池里，会导致池越来越大。而非池化的ByteBuf，即使像DirectByteBuf那样可能会用到System.gc()，但终归会被release掉的，不会出大事。
　　Netty担心大家不小心就搞出个大新闻来，因此提供了内存泄漏的监测机制。
　　Netty默认会从分配的ByteBuf里抽样出大约1%的来进行跟踪。如果泄漏，会有如下语句打印：

　　这句话报告有泄漏的发生，提示你用-D参数，把防漏等级从默认的simple升到advanced，就能具体看到被泄漏的ByteBuf被创建和访问的地方。
  •  禁用（DISABLED） - 完全禁止泄露检测，省点消耗。
  •  简单（SIMPLE） - 默认等级，告诉我们取样的1%的ByteBuf是否发生了泄露，但总共一次只打印一次，看不到就没有了。
  •  高级（ADVANCED） - 告诉我们取样的1%的ByteBuf发生泄露的地方。每种类型的泄漏（创建的地方与访问路径一致）只打印一次。对性能有影响。
  •  偏执（PARANOID） - 跟高级选项类似，但此选项检测所有ByteBuf，而不仅仅是取样的那1%。对性能有绝大的影响。

实现细节
　　每当各种ByteBufAllocator 创建ByteBuf时，都会问问是否需要采样，Simple和Advanced级别下，就是以113这个素数来取模（害我看文档的时候还在瞎担心，1％，万一泄漏的地方有所规律，刚好躲过了100这个数字呢，比如都是3倍数的），命中了就创建一个Java堆外内存扫盲贴里说的PhantomReference。然后创建一个Wrapper，包住ByteBuf和Reference。
　　simple级别下，wrapper只在执行release()时调用Reference.clear()，Advanced级别下则会记录每一个创建和访问的动作。
　　当GC发生，还没有被clear()的Reference就会被JVM放入到之前设定的ReferenceQueue里。
　　在每次创建PhantomReference时，都会顺便看看有没有因为忘记执行release()把Reference给clear掉，在GC时被放进了ReferenceQueue的对象，有则以 "io.netty.util.ResourceLeakDetector”为logger name，写出前面例子里的Error级别的日日志。顺便说一句，Netty能自动匹配日志框架，先找Slf4j，再找Log4j，最后找JDK logger。

值得说三遍的事
　　一定要盯紧log里有没有出现 "LEAK: "字样，因为simple级别下它只会出现一次，所以不要依赖自己的眼睛，要依赖grep。如果出现了，而且你用的是PooledBuf，那一定是问题，不要有任何的侥幸，立刻用"-Dio.netty.leakDetectionLevel=advanced" 再跑一次，看清楚它创建和访问的地方。
　　功能测试时，最好开着"-Dio.netty.leakDetectionLevel=paranoid"。
　　但是，怎么测试都可能存在没有覆盖到的分支。如果内存尚够，可以适当把-XX:MaxDirectMemorySize 调大，反正只是max，平时也不会真用了你的。然后监控其使用量，及时报警。
