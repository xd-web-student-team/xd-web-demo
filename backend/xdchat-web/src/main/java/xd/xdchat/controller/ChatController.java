package xd.xdchat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xd.xdchat.api.entity.Group;
import xd.xdchat.api.entity.RespBean;
import xd.xdchat.api.entity.User;
import xd.xdchat.service.GroupService;
import xd.xdchat.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
  @Autowired
  UserService userService;
  @Autowired
  GroupService groupService;

  static class GroupForm{
    public String groupName;
  }

  @GetMapping("/users")
  public List<User> getUsersWithoutCurrentUser(){
    return userService.getUsersWithoutCurrentUser();
  }

  @GetMapping("/groups")
  public List<Group> getGroups(){ return groupService.getGroups();  }

  @GetMapping("/checkGroupName")
  public Group checkGroupName(@RequestParam("groupName")String groupName){ return groupService.checkGroupName(groupName);  }

  @GetMapping("/checkJoined")
  public Integer checkJoined(@RequestParam("idGroup")Integer idGroup) { return groupService.checkJoined(idGroup); }

  @PostMapping("/buildGroup")
  public Group buildGroup(@RequestBody GroupForm groupForm){
       return groupService.buildGroup(groupForm.groupName);
  }

  @PostMapping("/joinGroup")
  public Group joinGroup(@RequestBody GroupForm groupForm){System.out.println(groupForm.groupName);
  //groupName = groupName.substring(0, groupName.length()-1);
  return groupService.joinGroup(groupForm.groupName);
  }
}
