package xd.xdchat.controller;

import xd.xdchat.api.entity.UserState;
import xd.xdchat.service.UserStateService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * (UserState)表控制层
 */
@RestController
@RequestMapping("userState")
public class UserStateController {
    /**
     * 服务对象
     */
    @Resource
    private UserStateService userStateService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public UserState selectOne(Integer id) {
        return this.userStateService.queryById(id);
    }

}
