package xd.xdchat.api.entity;

//Group实体类
public class Group {
    private Integer id;
    private String groupName;
    private Integer idGroupHolder;

    public Group(Integer id, String groupName, Integer idGroupHolder) {
        this.id = id;
        this.groupName = groupName;
        this.idGroupHolder = idGroupHolder;
    }

    public Integer getIdGroupHolder() {
        return idGroupHolder;
    }

    public void setIdGroupHolder(Integer idGroupHolder) {
        this.idGroupHolder = idGroupHolder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
