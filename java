
使用jmap -dump:format=b,file=netty.bin PID 将堆内存dump出来，通过IBM的HeapAnalyzer工具进行分析，发现ByteBuf发生了泄露。

------------------------netty--------------------------
