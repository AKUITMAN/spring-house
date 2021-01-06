package com.qf.listen;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.mail.internet.MimeMessage;
import java.util.Map;

@Component
public class ListenSender {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    TemplateEngine templateEngine;

    @RabbitListener(queues = "send-mail")
    public void sendMail(Map map, Channel channel, Message message){
        Integer id = (Integer) map.get("id");
        String email = (String) map.get("email");
        //开始邮件的发送
        Context context = new Context();
        context.setVariable("id",id);
        //与前端页面相结合生成字符串
        String emailTemplate = templateEngine.process("emailTemplate", context);

        MimeMessage mimeMailMessage = null;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("用户激活邮件");
           /* StringBuilder sb = new StringBuilder();
            sb.append(emailContent);*/

            mimeMessageHelper.setText(emailTemplate,true);
            javaMailSender.send(mimeMailMessage);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
