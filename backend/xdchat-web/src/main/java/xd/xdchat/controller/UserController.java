package xd.xdchat.controller;

import org.springframework.web.bind.annotation.*;
import xd.xdchat.api.entity.Group;
import xd.xdchat.api.entity.RespBean;
import xd.xdchat.api.entity.RespPageBean;
import xd.xdchat.api.entity.User;
import xd.xdchat.service.UserService;
import javax.annotation.Resource;
import java.util.List;

/**
 * (User)表控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 注册操作
     */
    @PostMapping("/register")
    public RespBean addUser(@RequestBody User user){
        if (userService.insert(user)==1){
            return RespBean.ok("注册成功！");
        }else{
            return RespBean.error("注册失败！");
        }
    }

    /**
     * 注册操作，检查用户名是否已被注册
     * @param username
     * @return
     */
    @GetMapping("/checkUsername")
    public Integer checkUsername(@RequestParam("username")String username){
        return userService.checkUsername(username);
    }

    /**
     * 注册操作，检查昵称是否已被注册
     * @param nickname
     * @return
     */
    @GetMapping("/checkNickname")
    public Integer checkNickname(@RequestParam("nickname") String nickname){
        //System.out.println(nickname);
        Integer result=userService.checkNickname(nickname);
        return result;
    }

    @PostMapping("/upload_profile")
    public String uploadFlie(@RequestParam MultipartFile file) throws IOException, MyException {

        String fileName = file.getOriginalFilename();
        String newFileName = DigestUtils.sha1Hex(file.getInputStream()) + "." + fileName.substring(fileName.lastIndexOf('.'));


        String path = "E://avatar//";

        File newFile = new File(path + newFileName);
        try {
            file.transferTo(newFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "http://localhost:8080/avatar/" + newFileName;

        return url;
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public User selectOne(@RequestParam("id")Integer id) {
        return this.userService.queryById(id);
    }

    /**
     * 获取群成员信息
     * @param Group
     * @return List<User>
     */
    @PostMapping("/getGroupMembers")
    public List<User> getGroupMembers(@RequestBody Group currentGroup){
        return userService.getGroupMembers(currentGroup);
    }
    /**
     * @author luo
     * @param page  页数，对应数据库查询的起始行数
     * @param size  数据量，对应数据库查询的偏移量
     * @param keyword 关键词，用于搜索
     * @param isLocked  是否锁定，用于搜索
     * @return
     */
    @GetMapping("/")
    public RespPageBean getAllUserByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                         @RequestParam(value = "size",defaultValue = "10") Integer size,
                                         String keyword,Integer isLocked){
        return userService.getAllUserByPage(page,size,keyword,isLocked);
    }

    /**
     * 更新用户的锁定状态
     * @author luo
     * @param id
     * @param isLocked
     * @return
     */
    @PutMapping("/")
    public RespBean changeLockedStatus(@RequestParam("id") Integer id, @RequestParam("isLocked") Boolean isLocked){
        if (userService.changeLockedStatus(id,isLocked)==1){
            return RespBean.ok("更新成功！");
        }else {
            return RespBean.error("更新失败！");
        }
    }

    /**
     * 删除单一用户
     * @author luo
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespBean deleteUser(@PathVariable Integer id){
        if (userService.deleteById(id)){
            return RespBean.ok("删除成功！");
        }
        else{
            return RespBean.error("删除失败！");
        }
    }

    /**
     * 批量删除用户
     * @author luo
     * @param ids
     * @return
     */
    @DeleteMapping("/")
    public RespBean deleteUserByIds(Integer[] ids){
        if (userService.deleteByIds(ids)==ids.length){
            return RespBean.ok("删除成功！");
        }else{
            return RespBean.error("删除失败！");
        }
    }
}
