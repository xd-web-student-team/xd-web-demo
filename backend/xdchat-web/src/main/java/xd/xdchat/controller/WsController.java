package xd.xdchat.controller;

import com.github.binarywang.java.emoji.EmojiConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import xd.xdchat.api.entity.GroupMsgContent;
import xd.xdchat.api.entity.Message;
import xd.xdchat.api.entity.User;
import xd.xdchat.service.GroupMsgContentService;
import xd.xdchat.service.GroupService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WsController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 单聊的消息的接受与转发
     * @param authentication
     * @param message
     */
    @MessageMapping("/ws/chat")
    public void handleMessage(Authentication authentication, Message message){
        User user= ((User) authentication.getPrincipal());
        message.setFromNickname(user.getNickname());
        message.setFrom(user.getUsername());
        message.setCreateTime(new Date());
        simpMessagingTemplate.convertAndSendToUser(message.getTo(),"/queue/chat",message);
    }

    @Autowired
    GroupMsgContentService groupMsgContentService;
    EmojiConverter emojiConverter = EmojiConverter.getInstance();

    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 群聊的消息接受与转发
     * @param authentication
     * @param groupMsgContent
     */
    @MessageMapping("/ws/groupChat")
    public void handleGroupMessage(Authentication authentication, GroupMsgContent groupMsgContent){
        User currentUser= (User) authentication.getPrincipal();
        //处理emoji内容,转换成unicode编码
        groupMsgContent.setContent(emojiConverter.toHtml(groupMsgContent.getContent()));
        //保证来源正确性，从Security中获取用户信息
        groupMsgContent.setFromId(currentUser.getId());
        groupMsgContent.setFromName(currentUser.getNickname());
        groupMsgContent.setFromProfile(currentUser.getUserProfile());
        groupMsgContent.setCreateTime(new Date());
        //保存该条群聊消息记录到数据库中
        groupMsgContentService.insert(groupMsgContent);
        //转发该条数据
        simpMessagingTemplate.convertAndSend("/topic/greetings",groupMsgContent);
    }

    @Autowired
    GroupService groupService;
    /**
     * 踢人消息接受与转发
     * @param authentication
     * @param message
     */
    @MessageMapping("/ws/kickGroup")
    public void kickGroup(Authentication authentication, Message message){
        groupService.kickGroup(Integer.parseInt(message.getContent()), Integer.parseInt(message.getFrom()));
        User user= ((User) authentication.getPrincipal());
        message.setFrom(user.getUsername());
        message.setCreateTime(new Date());
        simpMessagingTemplate.convertAndSendToUser(message.getTo(),"/queue/kick",message);
    }
}
