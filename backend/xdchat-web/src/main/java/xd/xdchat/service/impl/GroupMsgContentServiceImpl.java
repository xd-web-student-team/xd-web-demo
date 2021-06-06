package xd.xdchat.service.impl;

import com.alibaba.excel.EasyExcel;
import org.springframework.stereotype.Service;
import xd.xdchat.api.entity.Group;
import xd.xdchat.api.entity.GroupMsg;
import xd.xdchat.api.entity.GroupMsgContent;
import xd.xdchat.api.entity.RespPageBean;
import xd.xdchat.api.utils.UserUtil;
import xd.xdchat.dao.GroupDao;
import xd.xdchat.dao.GroupMsgContentDao;
import xd.xdchat.service.GroupMsgContentService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * (GroupMsgContent)表服务实现类
 *
 * @author makejava
 * @since 2020-06-17 10:51:13
 */
@Service("groupMsgContentService")
public class GroupMsgContentServiceImpl implements GroupMsgContentService {
    @Resource
    private GroupMsgContentDao groupMsgContentDao;

    @Resource
    private GroupDao groupDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public GroupMsgContent queryById(Integer id) {
        return this.groupMsgContentDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public Map<Integer,GroupMsg> queryAllByLimit(Integer offset, Integer limit) {
        List<Integer> groupIds = groupDao.getGroupIds(UserUtil.getCurrentUser().getId());
        Map<Integer,GroupMsg> res = new HashMap<>();
        for(int i = 0; i< groupIds.size(); ++i){
            res.put(groupIds.get(i),this.groupMsgContentDao.queryAllByLimit(offset, limit, groupIds.get(i)));
        }
        return res;
    }

    //通过Group Id查询群聊数据
    @Override
    public GroupMsg getGroupMsgContentByIdGroup(Integer idGroup) {
        return groupMsgContentDao.queryAllByLimit(null, null, idGroup);
    }
    /**
     * 新增数据
     *
     * @param groupMsgContent 实例对象
     * @return 实例对象
     */
    @Override
    public GroupMsgContent insert(GroupMsgContent groupMsgContent) {
        this.groupMsgContentDao.insert(groupMsgContent);
        return groupMsgContent;
    }

    /**
     * 修改数据
     *
     * @param groupMsgContent 实例对象
     * @return 实例对象
     */
    @Override
    public GroupMsgContent update(GroupMsgContent groupMsgContent) {
        this.groupMsgContentDao.update(groupMsgContent);
        return this.queryById(groupMsgContent.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.groupMsgContentDao.deleteById(id) > 0;
    }

    @Override
    public RespPageBean getAllGroupMsgContentByPage(Integer page, Integer size, String nickname, Integer type, Date[] dateScope) {
        if (page!=null&&size!=null){
            page=(page-1)*size;
        }
        List<GroupMsgContent> allGroupMsgContentByPage = groupMsgContentDao.getAllGroupMsgContentByPage(page, size, nickname, type, dateScope);
        Long total=groupMsgContentDao.getTotal(nickname, type, dateScope);
        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(allGroupMsgContentByPage);
        respPageBean.setTotal(total);
        return respPageBean;
    }

    @Override
    public Integer deleteGroupMsgContentByIds(Integer[] ids) {
        return groupMsgContentDao.deleteGroupMsgContentByIds(ids);
    }


}
