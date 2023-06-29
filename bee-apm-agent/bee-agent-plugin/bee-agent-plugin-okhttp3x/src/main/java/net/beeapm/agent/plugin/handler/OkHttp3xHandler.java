package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.HeaderKey;
import net.beeapm.agent.config.BeeConfig;
import net.beeapm.agent.log.Log;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;

/**
 * Created by yuan on 2018/8/14.
 */
public class OkHttp3xHandler extends AbstractHandler {
    private static final Log log = LogFactory.getLog(OkHttp3xHandler.class.getSimpleName());
    @Override
    public Span before(String className,String methodName, Object[] allArguments,Object[] extVal) {
        try {
            if(allArguments[0] != null && allArguments[0] instanceof okhttp3.Request.Builder){
                okhttp3.Request.Builder builder = (okhttp3.Request.Builder)allArguments[0];
                builder.header(HeaderKey.GID, BeeTraceContext.getGId());
                builder.header(HeaderKey.PID,BeeTraceContext.getCurrentId());
                builder.header(HeaderKey.SRC_APP, BeeConfig.me().getApp());
                builder.header(HeaderKey.SRC_INST, BeeConfig.me().getInst());
                if(BeeTraceContext.getCTag() != null) {
                    builder.header(HeaderKey.CTAG, BeeTraceContext.getCTag());
                }
            }
        }catch (Exception e){
            log.warn("",e);
        }
        return null;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments, Object result, Throwable t,Object[] extVal) {
        return result;
    }
}
