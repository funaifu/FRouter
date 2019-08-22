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
注解在开发中我们经常见到，现在我们简单聊聊注解到底是个什么东西<br>

###### Java中常见的注解
  * @Override:用于标识该方法继承自超类, 当父类的方法被删除或修改了，编译器会提示错误信息
  * @Deprecated：表示该类或者该方法已经不推荐使用，已经过期了，如果用户还是要使用，会生成编译的警告
  * @SuppressWarnings：用于忽略的编译器警告信息
  * Java验证的注解：@NotNull、@Email
  
  
