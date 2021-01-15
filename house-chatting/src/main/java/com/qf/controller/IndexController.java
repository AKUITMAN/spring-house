package com.qf.controller;

import com.qf.pojo.resp.BaseResp;
import com.qf.server.SocketServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {


    @RequestMapping("/sendMessage")
    public BaseResp sendMessage(@RequestParam("message")String message, @RequestParam("sender")String sender, @RequestParam("addressee")String addressee) {
        BaseResp baseResp = SocketServer.sendMessage(message,sender,addressee);
        return baseResp;
    }

    @RequestMapping(value = "/getUsers",method = RequestMethod.GET)
    public BaseResp getChatPeople(){
        BaseResp onlineUsers = SocketServer.getOnlineUsers();
        return onlineUsers;
    }
}
