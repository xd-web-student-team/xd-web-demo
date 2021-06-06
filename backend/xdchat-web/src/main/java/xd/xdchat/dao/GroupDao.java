package xd.xdchat.dao;

import xd.xdchat.api.entity.Group;
import xd.xdchat.api.entity.RespBean;

import java.util.List;

public interface GroupDao {
    //获取该用户加入的群组信息
    List<Group> getGroups(Integer id);

    //获取该用户加入的群组id
    List<Integer> getGroupIds(Integer id);
    //检查重复
    Group checkGroupName(String groupName);

    //检查是否已加入群聊
    Integer checkJoined(Integer idGroup, Integer idUser);

    //创建群聊
    void buildGroup(Group newGroup);

    //加入群聊
    void joinGroup(Integer idGroup, Integer id);

}
