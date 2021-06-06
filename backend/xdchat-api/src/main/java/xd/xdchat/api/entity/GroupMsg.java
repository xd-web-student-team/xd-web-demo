package xd.xdchat.api.entity;

import java.util.List;

public class GroupMsg {
    private int idGroup;

    private List<GroupMsgContent> groupMsgContentList;
    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public List<GroupMsgContent> getGroupMsgContentList() {
        return groupMsgContentList;
    }

    public void setGroupMsgContentList(List<GroupMsgContent> groupMsgContentList) {
        this.groupMsgContentList = groupMsgContentList;
    }

}
