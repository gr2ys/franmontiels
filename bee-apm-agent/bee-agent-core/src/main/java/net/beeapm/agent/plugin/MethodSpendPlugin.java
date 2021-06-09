package net.beeapm.agent.plugin;

import net.beeapm.agent.plugin.interceptor.MethodSpendAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Created by yuan on 2018/7/31.
 */
public class MethodSpendPlugin implements IPlugin {
    @Override
    public ElementMatcher<TypeDescription> buildTypesMatcher() {
        return ElementMatchers.nameStartsWith("net.beeapm.demo");
    }

    @Override
    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
        return ElementMatchers.isMethod();
    }

    @Override
    public Class adviceClass() {
        return MethodSpendAdvice.class;
    }

}
