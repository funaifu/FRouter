# FRouter
android路由的实现<br>
这是一个简单的路由实现方式，主要用到apt技术通过注解的方式根据注册的路径实现activity的跳转。<br>
下面我会一步一步讲解其中使用到的知识点。<br>
1，Java注解<br>
2，APT编译时注解<br>
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
 
 ![baidu](https://github.com/funaifu/FRouter/blob/master/imge/router_execute.jpg)
 
 *******************************************
 
 ### APT编译时注解
 
 ###### Element
 
 要了解APT我们首先得了解Element，element指的是一系列与之相关的接口集合，它们位于javax.lang.model.element包下面<br>
 表示一个程序元素，比如包、类或者方法。每个元素都表示一个静态的语言级构造（不表示虚拟机的运行时构造）。<br>
 它的子类接口有：ExecutableElement, PackageElement, TypeElement, TypeParameterElement, VariableElement<br>
 Element一些重要的方法：<br>
 1,TypeMirror asType() <br>
  返回此元素定义的类型。通用元素定义了一系列类型，而不仅仅是一个类型。 如果这是一个通用元素，则返回原型类型。 <br>
  这是元素对与其自己的形式类型参数相对应的类型变量的调用。 例如，对于通用类元素C<N extends Number> ，返回参数化类型C<N> 。 <br>
  Types实用程序界面具有更多的一般方法来获取由元素定义的全部类型。<br>
 2,ElementKind getKind() <br>
  返回此元素的 kind 。这种元素的种类<br>
 3,Set<Modifier> getModifiers()<br>
  返回此元素的修饰符，不包括注释。 包含隐式修饰符，如接口成员的public和static修饰符。该元素的修饰符，如果没有，则为空集<br>
 4,Name getSimpleName()<br>
  返回此元素的简单（不合格）名称。 通用类型的名称不包括对其正式类型参数的任何引用。 例如，类型元素java.util.Set<E>的简单名称是"Set" 。<br>
  如果此元素表示未命名的package ，则返回空名称。 如果代表constructor ，则返回名称“ <init> ”。<br>
  如果代表static initializer ，则返回名称“ <clinit> ”。 如果它代表一个anonymous class或instance initializer ，则返回一个空的名称。<br>
 5,List<? extends AnnotationMirror> getAnnotationMirrors()<br>
  返回直接存在于此构造上的注释。<br>
 6,<A extends Annotation> A getAnnotation(类<A> annotationType)<br>
  返回指定类型的这种构造的注解，如果这样的注释存在 ，否则null 。<br>

###### AbstractProcessor

一个抽象的注释处理器，被设计为大多数具体注解处理器的一个方便的超类。<br>
1,public Set<String> getSupportedOptions()<br>
  如果处理器类是带有加注解的SupportedOptions ，则返回一个不可修改的组具有相同的一组字符串作为注释的。<br>
2,public Set<String> getSupportedAnnotationTypes()<br>
  如果处理器类是带有加注解的SupportedAnnotationTypes ，则返回一个不可修改的组具有相同的一组字符串作为注释的。<br>
3,public SourceVersion getSupportedSourceVersion()<br>
  如果处理器类注释为SupportedSourceVersion ，则返回注释中的源版本。 如果类没有这样注释， 那么返回SourceVersion.RELEASE_6 。<br>
4,public void init(ProcessingEnvironment processingEnv)<br>
  通过将processingEnv字段设置为processingEnv参数的值，如果在同一对象上多次调用此方法，则将抛出IllegalStateException 。<br>
  processingEnv - 工具框架向处理器提供访问环境的环境<br>
5,public abstract boolean process(Set<? extends TypeElement> annotations,RoundEnvironment roundEnv)<br>
  对来自前一轮的类型元素处理一组注释类型，并返回此处理器是否声明这些注释类型。 如果返回true ，则会声明注释类型，不会要求后续处理器进行处理; <br>
  如果返回false ，则注释类型是无人认领的，并且后处理器可能被要求处理它们。 处理器可以总是返回相同的布尔值，或者可以根据所选择的标准来改变结果。<br>

###### ProcessingEnvironment

注释处理工具框架将提供一个具有实现此接口的对象的注释 processor，因此 processor <br>
可以使用该框架提供的设施来编写新文件、报告错误消息并查找其他实用工具。<br>
1,Map<String,String> getOptions()<br>
  返回传递给注解处理工具的处理器特定选项。 选项以选项名称的形式返回到选项值。 对于没有值的选项，地图中的相应值为null 。<br>
2,Messager getMessager()<br>
  返回用于报告错误，警告和其他通知的信息。<br>
3，Filer getFiler()<br>
  返回用于创建新的源，类或辅助文件的文件管理器。<br>
4，Elements getElementUtils()<br>
  返回一些用于操作元素的实用方法的实现<br>
5，Types getTypeUtils()<br>
  返回一些用于对类型进行操作的实用方法的实现。<br>
6，SourceVersion getSourceVersion()<br>
  返回任何生成的 source和 class文件应符合的源版本。<br>
 
###### RoundEnvironment

注释处理工具框架将提供一个注释处理器和一个实现此接口的对象，这样处理器可以查询有关注释处理的 round 的信息。<br>
1，Set<? extends Element> getRootElements()<br>
返回上一轮生成的注释处理的根元素。<br>
2，Set<? extends Element> getElementsAnnotatedWith(TypeElement a)<br>
返回使用给定注释类型注释的元素。 注释可以直接显示或继承。 仅返回本轮注释处理中包含的包元素和类型元素，或返回在其中声明的成员，<br>
构造函数，参数或类型参数的声明。 包含的类型元素是root types和嵌套在其中的任何成员类型。 包中的元素不被认为包含在内，<br>
只因为该包的一个package-info文件被创建。<br>
3，Set<? extends Element> getElementsAnnotatedWith(类<? extends Annotation> a)<br>
返回使用给定注释类型注释的元素。 注释可以直接显示或继承。 仅返回本轮注释处理中包含的包元素和类型元素，或返回在其中声明的成员，<br>
构造函数，参数或类型参数的声明。 包含的类型元素是root types ，嵌套在其中的任何成员类型。 包中的元素不被认为包含在内，<br>
因为该包的package-info文件已创建。<br>

************************************************************************************


  
  


  















 
 


 
