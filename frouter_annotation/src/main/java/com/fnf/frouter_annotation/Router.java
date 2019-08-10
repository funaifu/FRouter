package com.fnf.frouter_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: FRouter
 * @Package: com.fnf.frouter_annotation
 * @ClassName: Router
 * @Description: java类作用描述
 * @Author: Fu_NaiFu
 * @CreateDate: 2019/8/10 14:33
 * @UpdateUser: 更新者：Fu_NaiFu
 * @UpdateDate: 2019/8/10 14:33
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
@Target(ElementType.TYPE) // 注解作用在类上
@Retention(RetentionPolicy.CLASS)
public @interface Router {
    String path() default "";
}
