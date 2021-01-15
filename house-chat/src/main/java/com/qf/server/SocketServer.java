package com.qf.server;

import com.qf.pojo.resp.BaseResp;
import com.qf.pojo.vo.ChatPeo;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ServerEndpoint(value ="/socketServer/{userid}",encoders = {EncoderClassVo.class})
@Component
public class SocketServer {
    private Session session;
    private static Map<String,Session> sessionPool=new HashMap<>();
    private static Map<String,String> sessionIds=new HashMap<>();

    //使用该注解，在初次连接时触发，这里会为客户端创建一个session，这个session不是我们所熟悉的httpsession
    //，同时我们会将客户端传来的唯一标示与该session绑定，并保存到我们定义的静态map里，便于后续的向指定客户端发送消息。
    @OnOpen
    public BaseResp open(Session session, @PathParam("userid")String userid){

        BaseResp baseResp = new BaseResp();
        if (userid!=null&&userid!=""){
            Set<String> strings = sessionPool.keySet();
                this.session=session;
                sessionPool.put(userid,session);
                sessionIds.put(session.getId(),userid);
                baseResp.setMessage(sessionIds.get(session.getId()));
                baseResp.setCode(200);
            return  baseResp;
        }
        baseResp.setMessage("用户名为空");
        baseResp.setCode(201);
        return baseResp;
    }
    //使用该注解，在客户端向服务端发送消息时触发，接收到来自客户端的消息。
    @OnMessage
    public void onMessage(String message){
        System.out.println("当前发送人sessionid为"+session.getId()+"发送内容为"+message);
    }
    //使用该注解，在客户端与服务端断开连接时触发，同时我们会将静态map里存储的关于此客户端的session移除掉
    @OnClose
    public void onClose(){
        sessionPool.remove(sessionIds.get(session.getId()));
        sessionIds.remove(session.getId());
    }
    @OnError//使用该注解，在发生错误时触发
    public void onError(Session session,Throwable error){
        error.printStackTrace();
    }
    /**
     * 发送消息的方法，根据客户端的唯一标示与要发送给客户端的消息进行对客户端的定向发送。
     */
    public static BaseResp sendMessage(String message, String sender,String addressee){
        Session s=sessionPool.get(addressee);
        BaseResp baseResp = new BaseResp();
        if (s!=null){
         try{
//             HashMap<String, String> stringStringHashMap = new HashMap<>();
//             stringStringHashMap.put("sender",sender);
//             stringStringHashMap.put("message",message);
             ChatPeo chatPeo = new ChatPeo(sender,message);

             s.getBasicRemote().sendObject(chatPeo);
             baseResp.setCode(200);
             baseResp.setMessage("发送成功");
             return baseResp;
         }catch (Exception e){
             e.printStackTrace();
         }
        }
        baseResp.setCode(201);
        baseResp.setMessage("发送失败,用户没有上线!!!");
        return baseResp;
    }
    //获取在线人数
    public static int getOnlineNum(){
        return sessionPool.size();
    }
    //获取所有在线用户
    public static String getOnlineUsers(){
        StringBuffer users=new StringBuffer();
        for (String key:sessionIds.keySet()){
            users.append(sessionIds.get(key)+",");
        }
        return users.toString();
    }
    //向所有客户端发送信息
    public static void sendAll(String msg,String name){
        for (String key: sessionIds.keySet()){
            sendMessage(msg,sessionIds.get(key),name);
        }
    }
}
