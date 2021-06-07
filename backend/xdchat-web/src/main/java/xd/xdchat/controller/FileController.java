package xd.xdchat.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xd.xdchat.api.utils.FastDFSUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author LSK
 * @date 2021/5/30 - 16:57
 */
@RestController
public class FileController {

//  @Value("${fastdfs.nginx.host}")
//  String nginxHost;
//
//  @PostMapping("/file")
//  public String uploadFlie(MultipartFile file) throws IOException, MyException {
//    String fileId= FastDFSUtil.upload(file);
//    String url=nginxHost+fileId;
//    return url;
//  }

  @PostMapping("/file")
  public String uploadImage(@RequestParam MultipartFile file) throws IOException, MyException {

    String fileName = file.getOriginalFilename();
    String newFileName = DigestUtils.sha1Hex(file.getInputStream()) + "." + fileName.substring(fileName.lastIndexOf('.'));


    String path = "E://image//";

    File newFile = new File(path + newFileName);
    try {
      file.transferTo(newFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String url = "http://localhost:8080/image/" + newFileName;

    return url;
  }


}
