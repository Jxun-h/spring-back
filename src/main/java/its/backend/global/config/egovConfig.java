package its.backend.global.config;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.cmmn.trace.handler.DefaultTraceHandler;
import egovframework.rte.fdl.cmmn.trace.handler.TraceHandler;
import egovframework.rte.fdl.cmmn.trace.manager.DefaultTraceHandleManager;
import egovframework.rte.fdl.cmmn.trace.manager.TraceHandlerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
public class egovConfig {

    @Bean
    public DefaultTraceHandler defaultTraceHandler() {
        return new DefaultTraceHandler();
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public DefaultTraceHandleManager traceHandlerService(AntPathMatcher antPathMatcher, DefaultTraceHandler defaultTraceHandler) {
        DefaultTraceHandleManager traceHandlerService =  new DefaultTraceHandleManager();
        traceHandlerService.setReqExpMatcher(antPathMatcher);
        traceHandlerService.setPatterns(new String[]{"*"});
        traceHandlerService.setHandlers(new TraceHandler[]{defaultTraceHandler});

        return traceHandlerService;
    }

    @Bean
    public LeaveaTrace leaveaTrace(DefaultTraceHandleManager traceHandlerService) {
        LeaveaTrace leaveaTrace = new LeaveaTrace();
        leaveaTrace.setTraceHandlerServices(new TraceHandlerService[]{traceHandlerService});

        return leaveaTrace;
    }
}
