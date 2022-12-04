package net.beeapm.agent.plugin;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.plugin.interceptor.RequestInputStreamAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author yuan
 * @date 2018/10/12
 */
@BeePlugin(type = BeePluginType.AGENT_PLUGIN, name = "requestInputStream")
public class RequestInputStreamPlugin extends AbstractPlugin {

    @Override
    public String getName() {
        return "requestInputStream";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        return ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named("javax.servlet.http.HttpServletRequest"))
                                .and(ElementMatchers.not(ElementMatchers.isInterface()))
                                .and(ElementMatchers.not(ElementMatchers.<TypeDescription>isAbstract()));

                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.<MethodDescription>named("getInputStream");

                    }
                }
        };
    }


    @Override
    public Class interceptorAdviceClass() {
        return RequestInputStreamAdvice.class;
    }


}
