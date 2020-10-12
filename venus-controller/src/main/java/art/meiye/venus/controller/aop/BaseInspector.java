package art.meiye.venus.controller.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 拦截器基类
 * 维护Pointcut、Order等常量
 * @author Garin
 * @date 2020-08-26
 */

public class BaseInspector {
    @Pointcut("execution(* art.meiye.venus.controller..*Controller.*(..))")
    protected void controller() { }
}
