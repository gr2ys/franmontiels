package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.SpanManager;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.common.TxContext;
import net.beeapm.agent.transmit.TransmitterFactory;

import java.util.Date;

public class TxHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(TxHandler.class.getSimpleName());
    //@Override
    public Span before(String className, String methodName, Object[] allArguments) {
        Span span = TxContext.getTxSpan();
        String gid = BeeTraceContext.getGId();
        //如果gid相等，那么调用过名称相同参数签名不一样的方法，属于二次调用,不需要再采集了
        if(span == null || !gid.equals(span.getGid())) {
            span = SpanManager.createLocalSpan(SpanType.TX);
            TxContext.setTxSpan(span);
            //span.addTag("sql", allArguments[0]);
        }else {
            if(methodName.startsWith("doBegin")){
                span.setTime(new Date());
            }
        }
        return span;
    }

    //@Override
    public Object after(String className, String methodName, Object[] allArguments, Object result, Throwable t) {
        Span span = TxContext.getTxSpan();
        if(t != null || !methodName.startsWith("doCleanupAfterCompletion")){
            TxContext.remove();
        }
        if(methodName.startsWith("doCleanupAfterCompletion") && span != null) {
            calculateSpend(span);
            TransmitterFactory.transmit(span);
        }
        return result;
    }
}
