package com.fnf.arouter_compiler;

import com.fnf.frouter_annotation.Router;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @ProjectName: FRouter
 * @Package: com.fnf.arouter_compiler
 * @ClassName: FAnnotationProcessor
 * @Description: java类作用描述
 * @Author: Fu_NaiFu
 * @CreateDate: 2019/8/10 14:34
 * @UpdateUser: 更新者：Fu_NaiFu
 * @UpdateDate: 2019/8/10 14:34
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@AutoService(Processor.class)
/**
 处理器接收的参数 替代 {@link AbstractProcessor#getSupportedOptions()} 函数
 */
@SupportedOptions(Constant.ARGUMENTS_NAME)
/**
 * 指定使用的Java版本 替代 {@link AbstractProcessor#getSupportedSourceVersion()} 函数
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
/**
 * 注册给哪些注解的  替代 {@link AbstractProcessor#getSupportedAnnotationTypes()} 函数
 */
@SupportedAnnotationTypes(Constant.ANNOTATION_TYPE_ROUTE)
public class FAnnotationProcessor extends AbstractProcessor {

    /**
     * key:组名 value:类名
     */
    private Map<String, String> rootMap = new TreeMap<>();
//    /**
//     * 分组 key:组名 value:对应组的路由信息
//     */
//    private Map<String, List<RouteMeta>> groupMap = new HashMap<>();

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementUtils;

    /**
     * type(类信息)工具类
     */
    private Types typeUtils;

    /**
     * 文件生成器 类/资源
     */
    private Filer filerUtils;

    private String moduleName;
    private Log log;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        log = Log.newLog(processingEnvironment.getMessager());
        log.i("init RouterProcessor " + moduleName + " success !");
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filerUtils = processingEnvironment.getFiler();
        //参数是模块名 为了防止多模块/组件化开发的时候 生成相同的 xx$$ROOT$$文件
        Map<String, String> options = processingEnvironment.getOptions();
        if (!Utils.isEmpty(options)) {
            moduleName = options.get(Constant.ARGUMENTS_NAME);
        }
        if (Utils.isEmpty(moduleName)) {
            throw new RuntimeException("Not set processor moudleName option !");
        }
        log.i("init RouterProcessor " + moduleName + " success !");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (!Utils.isEmpty(set)) {
            //被Route注解的节点集合
            Set<? extends Element> rootElements = roundEnvironment.getElementsAnnotatedWith(Router.class);
            if (!Utils.isEmpty(rootElements)) {
                TypeSpec typeSpec = processorRoute(rootElements);
                JavaFile javaFile = JavaFile.builder("com.process.router", typeSpec).build();
                try {
                    javaFile.writeTo(filerUtils);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    private TypeSpec processorRoute(Set<? extends Element> rootElements) {


        // 1. 构造参数，参数为activityMap
        // 参数类型为:HashMap<String,Class<?>>
        ParameterizedTypeName mapTypeName =
                ParameterizedTypeName.get(ClassName.get(HashMap.class), ClassName.get(String
                        .class), ClassName.get(Class.class));
        ParameterSpec mapSpec = ParameterSpec.builder(mapTypeName, "activityMap").build();
        MethodSpec.Builder initMethodBuilder =
                MethodSpec.methodBuilder("initActivityMap").addModifiers(Modifier.PUBLIC,
                        Modifier.STATIC).addParameter(mapSpec);

        for (Element element : rootElements) {
            Router route = element.getAnnotation(Router.class);
            String path = route.path();
            if (!"".equals(path)) {
                initMethodBuilder.addStatement("activityMap.put($S,$T.class)", path, ClassName.get
                        ((TypeElement) element));
            }
        }
        String className = "ARouter_" + moduleName;
        TypeSpec typeSpec = TypeSpec.classBuilder(className).addMethod(initMethodBuilder.build())
                .addModifiers(Modifier.PUBLIC).build();
        return typeSpec;
    }
}
