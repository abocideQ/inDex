#### 奇怪的知识

```
一. JAVA 双亲委派 双亲委派的父子并不是继承关系

1.CustomClassLoader(自定义de类加载器)

2.AppClassLoader (应用程序类加载器，加载*Java环境变量CLASSPATH所指定的路径下的类库 ，
  而CLASSPATH所指定的路径可以通过 System.getProperty("java.class.path") 获取，该变量可被覆盖)

3.ExtClassLoader(扩展类加载器，会加载 $JAVA_HOME/jre/lib/ext 的类库)

4.BootStrapClassLoader(启动类加载器，JVM启动时创建，加载$JAVA_HOME/JRE/LIB下面的类库,只加载一次，防篡改)

如果类已经加载了，就不用再加载 Class<?> c = findLoadedClass(name);
if (c==null)
CustomClassLoader 委派父加载器->AppClassLoader 委派父加载器->ExtClassLoader 委派父加载器->BootStrapClassLoader
未找到-> ExtClassLoader 未找到-> AppClassLoader 未找到->CustomClassLoader 未找到 -> Exception

-----------------------------------------------------------------------

二. Android 双亲委派

Android屏蔽了ClassLoader的findClass加载方法
Android有两个类加载器PathClassLoader,DexclassLoader都继承于BaseDexClassLoader

PathClassLoader 和 DexClassLoader 都能加载外部的 dex／apk
PathClassLoader：只能使用系统默认位置
DexClassLoader：可以指定 optimizedDirectory

BaseDexClassLoader 构造函数：
dexPath: 需要加载的文件列表，文件可以是包含了 classes.dex 的 JAR/APK/ZIP，也可以直接使用 classes.dex 文件，多个文件用 “:” 分割
optimizedDirectory: 存放优化后的 dex，可以为空
libraryPath: 存放需要加载的 native 库的目录
parent: 父ClassLoader
BaseDexClassLoader 的运行方式: 传入 dex 文件->优化->保存优化后的 dex 文件到 optimizedDirectory 目录。

1.热修复 (class 会有缓存问题？)
使用DexClassLoader：能够加载未安装的jar/apk/dex
DexClassLoader ->DexPathlist->dexElements[]
将新的dex包与dexElement[] 合并放在数组最前面成一个新的dex,旧的dex就不会生效
通过反射将 原来的dexElements[] 替换掉，这样就可不会加载旧的带有bug的class文件。
Dex仅在 对象被调用前 写入PathList生效

2.插件化 ?????

-----------------------------------------------------------------------

三. AMS PMS WMS

1. 两种Activity启动方式
val intent = Intent(this,xxx::class.java)
activity.startActivity(intent)
-> Activity.ActivityThread.mInstrumention.excStartActivity()
-> newActivity()

val intent = Intent(this,xxx::class.java)
intent.flag = Intent.FLAG_ACTIVITY_NEW_TASK 
context.startActivity()
-> ContextImpl.ActivityThread.mInstrumention.excStartActivity()
-> newActivity()

Context 的几种实现
Context -> ContextWrapper -> Application
                            -> Service
                            -> ContextThemeWrapper -> Activity
Context -> ContextImpl

2.AMS: ActivityManagerService
Zygote 进程 (Socket 通信) 
System_server 进程 (Binder 通信)
RemoteService 进程

Process ？？ -> (Binder 启动System_server服务) 
-> System_server -> (Socket 请求fork进程) -> Zygote (fork进程 成功) -> (Socket) 
-> RemoteService -> (Binder 请求关联进程) 
-> System_server -> (Binder 关联成功) 
-> RemoteService (开始运作)
-> ApplicationThread -> ActivityThread -> xxx

????????????????????

3.WMS :WindowManagerService
** WindowManagerService：分配/管理Surface,Window的本质就是Surfac
** SurfaceFlinger: 一个独立的Service， 它接收所有Window的Surface作为输入进行图形渲染 + 显示
** OpengGl：配置EGL(操作GPU的API) 可绕开 WMS 进行图形渲染 + 显示

Activity Window View: Activity(PhoneWindow(DecorView))
Activity.attch() -> new PhoneWindow() -> PhoneWindow.addView(DecorView) -> DecorView.addView(xml)

WindowManager WindowManagerImpl WindowManagerGlobal ViewRootImpl:
WindowManager：进程单例
WindowManagerImpl：WindowManager的实现类
WindowManagerGlobal：进程单例 View的具体操作类
ViewRoot：由WindowManagerGlobal初始化
ViewRootImpl：ViewRoot的实现类，View树的管理者，核心任务是和WindowManagerService进行通信

4.PMS :PackageManagerService 
System_server -> ?????????????

-----------------------------------------------------------------------
```
#### 没啥用的面试知识
```
一.嘛四 Java字节码?

1. 解释  : 给虚拟机用的玩意儿 -> XXX.Class
2. 编译  : XXX.Java 编译器 -> XXX.Class 二进制字节码文件, 可反向操作
3. 组成  : N行2进制数据 -> 每行：地址 + 内容
4. 魔数  : 字节码中第一行内容 首4位 ，指定文件类型 
5. 更多其他的有必要了解吗？

二. 嘛四 JVM?

1. JRE : Java API + JVM
2. JVM : java 虚拟机
3. 不同的JVM : 谷歌Android 虚拟机 Dalvik VM
4. 基本构成 ： 类加载器 + 运行时数据区(内存操作) + 执行引擎(class->机器码) + 本地库接口(C/C++)
5. 运行时数据区 : 程序计数器(字节码的行号指示器) + 虚拟机栈(存储方法相关信息) + 本地方法栈(存储Native方法信息) + 堆(存储对象) + 方法区(类信息、常量、静态变量等)
6. 垃圾算法：标记(如名字，不好用) + 复制(内存满了,复制活的,清除死的) + 分代(新生代频繁GC,老年代少GC) 等
```