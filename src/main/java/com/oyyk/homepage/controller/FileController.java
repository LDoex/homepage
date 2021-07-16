package com.oyyk.homepage.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.oyyk.homepage.resp.CommonResp;
import com.oyyk.homepage.util.SnowFlake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;


@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${server.port}")
    private String port;

    private static final String ip = "http://localhost";

    @Resource
    private SnowFlake snowFlake;

    /**
     * 上传接口
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public CommonResp upload(MultipartFile file) throws IOException{
        CommonResp resp = new CommonResp<>();
        //获取源文件名
        String originalFilename = file.getOriginalFilename();
        //获取上传路径
        Long flag = snowFlake.nextId();
        String rootFilePath = System.getProperty("user.dir") + "/src/main/resources/files/" + flag + '_' + originalFilename;
        //写入文件
        FileUtil.writeBytes(file.getBytes(), rootFilePath);
        //返回结果
        resp.setContent(ip+":"+port+"/file/"+flag); //返回url
        return resp;
    }

    /**
     * 下载接口
     * @param flag
     * @param response
     */
    @GetMapping("/{flag}")
    public CommonResp getFiles(@PathVariable String flag, HttpServletResponse response){
        CommonResp<Object> resp = new CommonResp<>();
        OutputStream os; //新建一个输出流对象
        String basePath = System.getProperty("user.dir")+"/src/main/resources/files/"; //获取根路径
        List<String> fileNames = FileUtil.listFileNames(basePath);//获取文件名
        String avatar = fileNames.stream().filter(name->name.contains(flag)).findAny().orElse("");//找到与参数一致的文件
        try {
            if(StrUtil.isNotEmpty(avatar)) {
                response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(avatar, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath+avatar); //读取字节流
                os = response.getOutputStream(); //通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setSuccess(false);
        }
        return resp;
    }
}
