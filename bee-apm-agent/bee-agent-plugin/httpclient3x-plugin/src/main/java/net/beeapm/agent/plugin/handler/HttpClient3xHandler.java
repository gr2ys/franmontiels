package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.HeaderKey;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import org.apache.commons.httpclient.HttpMethod;

/**
 * Created by yuan on 2018/8/16.
 */
public class HttpClient3xHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(HttpClient3xHandler.class.getSimpleName());
    @Override
    public Span before(String className,String methodName, Object[] allArguments) {
        try {
            for (int i = 0; i < allArguments.length; i++) {
                if (allArguments[i] instanceof HttpMethod) {
                    HttpMethod req = (HttpMethod) allArguments[i];
                    if(req.getRequestHeader(HeaderKey.GID) == null){
                        req.setRequestHeader(HeaderKey.GID, BeeTraceContext.getGId());
                        req.setRequestHeader(HeaderKey.PID,BeeTraceContext.getCurrentId());
                        req.setRequestHeader(HeaderKey.CTAG,BeeTraceContext.getCTag());
                    }
                }
            }
        }catch (Exception e){
            log.warn("",e);
        }
        return null;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments, Object result, Throwable t) {
        return result;
    }
}
