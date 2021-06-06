package xd.xdchat.service;

import xd.xdchat.api.entity.Group;
import xd.xdchat.api.entity.RespBean;

import java.util.List;

public interface GroupService {
    //获取加入的群组
    List<Group> getGroups();
    //检查是否重复
    Group checkGroupName(String groupName);

    //检查是否已加入该群
    Integer checkJoined(Integer idGroup);

    //新建群聊
    Group buildGroup(String groupName);

    //加入群聊
    Group joinGroup(String groupName);

    //退出群聊
    void quitGroup(Group groupToQuit);
}
