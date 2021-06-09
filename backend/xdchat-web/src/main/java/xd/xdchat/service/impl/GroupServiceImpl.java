package xd.xdchat.service.impl;

import org.springframework.stereotype.Service;
import xd.xdchat.api.entity.Group;
import xd.xdchat.api.entity.RespBean;
import xd.xdchat.api.utils.UserUtil;
import xd.xdchat.dao.GroupDao;
import xd.xdchat.service.GroupService;

import javax.annotation.Resource;
import java.util.List;

@Service("groupService")
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupDao groupDao;
    @Override
    public List<Group> getGroups() {
        return groupDao.getGroups(UserUtil.getCurrentUser().getId());
    }
    @Override
    public Group checkGroupName(String groupName) {
        return groupDao.checkGroupName(groupName);
    }

    @Override
    public Integer checkJoined(Integer idGroup) { return groupDao.checkJoined(idGroup, UserUtil.getCurrentUser().getId()); }

    @Override
    public Group buildGroup(String groupName) {
        Group newGroup = new Group(0, groupName, UserUtil.getCurrentUser().getId());
        groupDao.buildGroup(newGroup);
        groupDao.joinGroup(newGroup.getId(), UserUtil.getCurrentUser().getId());
        return newGroup;
    }

    @Override
    public Group joinGroup(String groupName) {
        Group theGroup = groupDao.checkGroupName(groupName);
        groupDao.joinGroup(theGroup.getId(), UserUtil.getCurrentUser().getId());
        return theGroup;
    }

    @Override
    public void quitGroup(Group groupToQuit) {
        groupDao.quitGroup(groupToQuit.getId(), UserUtil.getCurrentUser().getId());
    }

    @Override
    public void kickGroup(Integer idGroup, Integer idUser) {
        groupDao.quitGroup(idGroup, idUser);
    }
}
