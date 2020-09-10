package com.monitor.argus.mis.controller.webSockets.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by huxiaolei on 2016/10/21.
 */
@ServerEndpoint("/webSockets/alarm/alarmNotifySocket")
public class AlarmNotifySocket {

    private static Logger logger = LoggerFactory.getLogger(AlarmNotifySocket.class);

    // 线程安全set，用来存放每个客户端对应的AlarmNotifySocket对象，群发信息即可
    public static volatile CopyOnWriteArraySet<AlarmNotifySocket> webSocketSet = new CopyOnWriteArraySet<AlarmNotifySocket>();

    // 静态变量，用来记录当前连接数
    private static volatile int onlineCount = 0;

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        logger.info("有新连接加入！当前在线客户端为:" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        logger.info("有连接关闭！当前在线客户端为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("接收到客户端信息:" + message);
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        logger.error("socket异常:" + error.getMessage());
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return AlarmNotifySocket.onlineCount;
    }

    public static synchronized void addOnlineCount() {
        AlarmNotifySocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        AlarmNotifySocket.onlineCount--;
    }
}
