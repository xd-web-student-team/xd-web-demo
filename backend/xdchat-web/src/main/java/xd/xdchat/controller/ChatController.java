package xd.xdchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xd.xdchat.api.entity.User;
import xd.xdchat.service.UserService;

import java.util.List;

/**
 * @author LSK
 * @date 2021/5/30 - 16:57
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
  @Autowired
  UserService userService;

  @GetMapping("/users")
  public List<User> getUsersWithoutCurrentUser(){
    return userService.getUsersWithoutCurrentUser();
  }
}
