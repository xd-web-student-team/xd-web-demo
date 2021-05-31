package xd.xdchat.controller;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xd.xdchat.api.utils.FastDFSUtil;

import java.io.IOException;

/**
 * @author LSK
 * @date 2021/5/30 - 16:57
 */
@RestController
public class FileController {

  @Value("${fastdfs.nginx.host}")
  String nginxHost;

  @PostMapping("/file")
  public String uploadFlie(MultipartFile file) throws IOException, MyException {
    String fileId= FastDFSUtil.upload(file);
    String url=nginxHost+fileId;
    return url;
  }




}
