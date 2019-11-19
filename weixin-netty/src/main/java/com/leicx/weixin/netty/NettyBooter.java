package com.leicx.weixin.netty;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

@Component
public class NettyBooter implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent){
            System.out.println("环境初始化！！！");
        } else if (event instanceof ApplicationPreparedEvent){
            System.out.println("初始化完成！！！");
        } else if (event instanceof ContextRefreshedEvent) {
            System.out.println("应用刷新！！");
            WSServer.getInstance().start();
        } else if (event instanceof ApplicationReadyEvent) {
            System.out.println("项目启动完成！！");
        } else if (event instanceof ContextStartedEvent) {
            System.out.println("应用启动！！");
        } else if (event instanceof ContextStoppedEvent) {
            System.out.println("项目停止！！");
        } else if (event instanceof ContextClosedEvent) {
            System.out.println("应用关闭！！");
        }
    }
}
