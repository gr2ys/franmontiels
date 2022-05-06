package net.beeapm.agent.plugin.handler;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.common.*;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanType;
import net.beeapm.agent.plugin.common.BeeHttpResponseWrapper;
import net.beeapm.agent.plugin.ServletConfig;
import net.beeapm.agent.plugin.common.RequestBodyHolder;
import net.beeapm.agent.transmit.TransmitterFactory;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuan on 2018/8/5.
 */
public class ServletHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(ServletHandler.class.getSimpleName());
    @Override
    public Span before(String className,String methodName, Object[] allArguments,Object[] extVal) {
        if(!ServletConfig.me().isEnable()){
            return null;
        }
        Span currSpan = SpanManager.getCurrentSpan();
        if(currSpan == null || !currSpan.getType().equals(SpanType.REQUEST)){
            HttpServletRequest request = (HttpServletRequest)allArguments[0];
            BeeTraceContext.setGId(request.getHeader(HeaderKey.GID));
            BeeTraceContext.setPId(request.getHeader(HeaderKey.PID));
            BeeTraceContext.setCTag(request.getHeader(HeaderKey.CTAG));
            Span span = SpanManager.createEntrySpan(SpanType.REQUEST);
            span.addTag("sc",request.getHeader(HeaderKey.SRC_CLUSTER));
            span.addTag("ss",request.getHeader(HeaderKey.SRC_SERVER));
            HttpServletResponse resp = (HttpServletResponse) allArguments[1];
            if(ServletConfig.me().isEnableRespBody() && !resp.getClass().getSimpleName().equals("BeeHttpResponseWrapper")){
                BeeHttpResponseWrapper wrapper = new BeeHttpResponseWrapper(resp);
                span.addTag("_respWrapper",wrapper);
            }
            return span;
        }
        return null;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments,Object result, Throwable t,Object[] extVal) {
        Span currSpan = SpanManager.getCurrentSpan();
        if(!ServletConfig.me().isEnable() || CollectRatio.NO()){
            return null;
        }
        if(currSpan!=null && currSpan.getType().equals(SpanType.REQUEST)) {
            Span span = SpanManager.getExitSpan();
            HttpServletRequest request = (HttpServletRequest) allArguments[0];
            HttpServletResponse response = (HttpServletResponse) allArguments[1];
            span.addTag("url", request.getRequestURL());
            span.addTag("remote", request.getRemoteAddr());
            span.addTag("method", request.getMethod());
            //span.addTag("clazz", className);
            calculateSpend(span);
            if(span.getSpend() > ServletConfig.me().getSpend() && CollectRatio.YES()) {
                response.setHeader(HeaderKey.GID, span.getGid());   //返回gid，用于跟踪
                response.setHeader(HeaderKey.ID, span.getId());     //返回id，用于跟踪
                TransmitterFactory.transmit(span);
                collectRequestParameter(span,request);//采集参数
                collectRequestBody(span,request);//采集body
                collectRequestHeader(span,request);//采集header
                collectResponseBody(span,response);
            }
            return result;
        }
        return null;
    }

    private void collectRequestParameter(Span span,HttpServletRequest request){
        if(ServletConfig.me().isEnableReqParam()){
            Map<String, String[]> params = request.getParameterMap();
            if(params != null && !params.isEmpty()) {
                Span paramSpan = new Span(SpanType.REQUEST_PARAM);
                paramSpan.setId(span.getId());
                paramSpan.setIp(null);
                paramSpan.setPort(null);
                paramSpan.setServer(null);
                paramSpan.setCluster(null);
                paramSpan.addTag("param", JSON.toJSONString(params));
                TransmitterFactory.transmit(paramSpan);
            }
        }
    }
    private void collectRequestBody(Span span,HttpServletRequest request){
        if(ServletConfig.me().isEnableReqBody()){
            try {
                //触发获取body的代码植入
                request.getInputStream();
            }catch (Exception e){
            }
            if(StringUtils.isNotBlank(RequestBodyHolder.getRequestBody())) {
                Span bodySpan = new Span(SpanType.REQUEST_BODY);
                bodySpan.setId(span.getId());
                bodySpan.setIp(null);
                bodySpan.setPort(null);
                bodySpan.setServer(null);
                bodySpan.setCluster(null);
                bodySpan.addTag("body", RequestBodyHolder.getRequestBody());
                TransmitterFactory.transmit(bodySpan);
            }
        }
    }
    private void collectRequestHeader(Span span,HttpServletRequest request){
        if(ServletConfig.me().isEnableReqHeaders()){
            Map<String, String> headers = new HashMap<String, String>();
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                headers.put(key, value);
            }
            if(!headers.isEmpty()) {
                Span headersSpan = new Span(SpanType.REQUEST_HEADERS);
                headersSpan.setId(span.getId());
                headersSpan.setIp(null);
                headersSpan.setPort(null);
                headersSpan.setServer(null);
                headersSpan.setCluster(null);
                headersSpan.addTag("headers", JSON.toJSONString(headers));
                TransmitterFactory.transmit(headersSpan);
            }
        }
    }
    private void collectResponseBody(Span span,HttpServletResponse resp){
        if(ServletConfig.me().isEnableRespBody()){
            BeeHttpResponseWrapper beeResp = (BeeHttpResponseWrapper)resp;
            beeResp.out();//触发原有的输出
            Span respSpan = new Span(SpanType.RESPONSE_BODY);
            respSpan.setId(span.getId());
            respSpan.setIp(null);
            respSpan.setPort(null);
            respSpan.setServer(null);
            respSpan.setCluster(null);
            respSpan.addTag("body", new String(beeResp.getBytes()));
            TransmitterFactory.transmit(respSpan);
        }
    }
}
