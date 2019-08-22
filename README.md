# FRouter
android路由的实现<br>
这是一个简单的路由实现方式，主要用到apt技术通过注解的方式根据注册的路径实现activity的跳转。<br>
下面我会一步一步讲解其中使用到的知识点。<br>
1，Java注解<br>
2，auto-service<br>
3，javapoet<br>

*****************************

### java注解
<br>
注解在开发中我们经常见到，现在我们简单聊聊注解到底是个什么东东<br>

###### Java中常见的注解
* @Override:用于标识该方法继承自超类, 当父类的方法被删除或修改了，编译器会提示错误信息
* @Deprecated：表示该类或者该方法已经不推荐使用，已经过期了，如果用户还是要使用，会生成编译的警告
* @SuppressWarnings：用于忽略的编译器警告信息
* Java验证的注解：@NotNull、@Email
  
 下面是Override.java的源码：
  
  
  ![baidu](https://github.com/funaifu/FRouter/blob/master/imge/override.jpg)
 
###### 自定义注解
  
  注解是写在.java文件中，使用@interface作为关键字, 所以注解也是Java的一种数据类型。<br>
  在创建自定义注解的时候，需要使用一些注解来描述自己创建的注解，就是写在@interface上面的那些注解，<br>
  这些注解被称为元注解Annotation，如在Override中看到的@Target、@Retention等。<br>
  Annotation(注解)是插入代码中的元数据，在JDK5.0及以后版本引入。它主要的作用有以下四方面：<br>
  1,生成文档，通过代码里标识的元数据生成javadoc文档。<br>
  2,编译检查，通过代码里标识的元数据让编译器在编译期间进行检查验证。<br>
  3,编译时动态处理，编译时通过代码里标识的元数据动态处理，例如动态生成代码。<br>
  4,运行时动态处理，运行时通过代码里标识的元数据动态处理，例如使用反射注入实<br>
 
###### java.lang.annotation提供了四种元注解，专门注解其他的注解：
* @Documented –注解是否将包含在JavaDoc中
* @Retention –什么时候使用该注解
* @Target –注解用于什么地方
* @Inherited – 是否允许子类继承该注解

###### @Target：表示该注解可以用于什么地方，可能的ElementType参数有：
* CONSTRUCTOR：构造器的声明
* FIELD：域声明（包括enum实例）
* LOCAL_VARIABLE：局部变量声明
* METHOD：方法声明
* PACKAGE：包声明
* PARAMETER：参数声明
* TYPE：类、接口（包括注解类型）或enum声明
   
###### @Retention表示需要在什么级别保存该注解信息。可选的RetentionPolicy参数包括：
* SOURCE：注解将被编译器丢弃
* CLASS：注解在class文件中可用，但会被VM丢弃
* RUNTIME：VM将在运行期间保留注解，因此可以通过反射机制读取注解的信息

自定义注解如下：

 ![baidu](https://github.com/funaifu/FRouter/blob/master/imge/router.jpg)


 
