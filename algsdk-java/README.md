部署编译好的jar包时的注意事项：

>解压jar包可以看到，在jnademo-1.0.0.jar\BOOT-INF\classes\win32-x86-64\ 目录下有AlgSDK.dll，但是jar包拷贝要一台没有安装VisualStudio的机器上一运行就会报如下错；
>
>PS D:\webdeploy> java -jar .\jnademo-1.0.0.jar
>SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
>SLF4J: Defaulting to no-operation (NOP) logger implementation
>SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
>Exception in thread "main" java.lang.UnsatisfiedLinkError: 找不到指定的模块。
>
>```shell
>    at com.sun.jna.Native.open(Native Method)
>    at com.sun.jna.NativeLibrary.loadLibrary(NativeLibrary.java:277)
>    at com.sun.jna.NativeLibrary.getInstance(NativeLibrary.java:461)
>    at com.sun.jna.Library$Handler.<init>(Library.java:192)
>    at com.sun.jna.Native.load(Native.java:596)
>    at com.sun.jna.Native.load(Native.java:570)
>    at com.icetc.jnademo.AlgSDK.<clinit>(AlgSDK.java:12)
>    at com.icetc.jnademo.Application.<clinit>(Application.java:20)
>    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
>    at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
>    at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
>    at java.lang.reflect.Method.invoke(Unknown Source)
>    at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:49)
>    at org.springframework.boot.loader.Launcher.launch(Launcher.java:108)
>    at org.springframework.boot.loader.Launcher.launch(Launcher.java:58)
>    at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:88)
>```

这是应为AlgSDK.dll是使用VisualStudio2019开发，依赖VS2019C++的一些dll库，具体可以使用"depends.exe"查找依赖哪些dll，然后将这些dll拷贝到和jnademo-1.0.0.jar在同一级目录下，重新运行jar包即可成功。

