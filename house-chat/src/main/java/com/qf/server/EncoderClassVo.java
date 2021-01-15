package com.qf.server;

import com.alibaba.fastjson.JSON;
import com.qf.pojo.vo.ChatPeo;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class EncoderClassVo implements Encoder.Text<ChatPeo>{
    @Override
    public String encode(ChatPeo chatPeo) throws EncodeException {
      return JSON.toJSONString(chatPeo);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
