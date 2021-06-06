package xd.xdchat.api.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import xd.xdchat.api.converter.MyContentConverter;

import java.util.List;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

//(GroupMsgContent)实体类
@ColumnWidth(25)
@ContentRowHeight(40)
public class GroupMsgContent implements Serializable {
    private static final long serialVersionUID = 980328865610261046L;
    /**
     * 消息内容编号
     */
    @ExcelProperty("消息内容编号")
    private Integer id;
    /**
     * 发送者的编号
     */
    @ExcelProperty("发送者的编号")
    private Integer fromId;
    /**
     * 发送者的昵称
     */
    @ExcelProperty("昵称")
    private String fromName;
    /**
     * 发送者的头像
     */
    @ExcelIgnore
    private String fromProfile;
    /**
     * 消息发送时间
     */
    @ExcelProperty("发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 消息内容
     */
    @ExcelProperty(value = "内容",converter = MyContentConverter.class)
    @ColumnWidth(50)
    private String content;
    /**
     * 消息类型编号
     */
    @ExcelIgnore
    private Integer messageTypeId;

    @ExcelProperty("群号")
    private Integer idGroup;

    public Integer getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Integer idGroup) {
        this.idGroup = idGroup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromProfile() {
        return fromProfile;
    }

    public void setFromProfile(String fromProfile) {
        this.fromProfile = fromProfile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(Integer messageTypeId) {
        this.messageTypeId = messageTypeId;
    }

    @Override
    public String toString() {
        return "GroupMsgContent{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", fromName='" + fromName + '\'' +
                ", fromProfile='" + fromProfile + '\'' +
                ", createTime=" + createTime +
                ", content='" + content + '\'' +
                ", messageTypeId=" + messageTypeId +
                ", idGroup=" + idGroup +
                '}';
    }
}