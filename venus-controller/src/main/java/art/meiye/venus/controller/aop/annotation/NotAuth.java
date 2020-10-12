package art.meiye.venus.controller.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解 - 禁用鉴权
 * 修饰方法 - 该方法不会检查userId、token等权限信息，直接放行
 * 修饰类 - 该类下所有方法不会检查userId、token等权限信息，直接放行
 *
 * @see art.meiye.venus.controller.aop.AopAuthInspector
 *
 * @author Garin
 * @date 2020-08-26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAuth {

}