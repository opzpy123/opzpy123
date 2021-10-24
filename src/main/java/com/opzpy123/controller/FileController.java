package com.opzpy123.controller;

import com.opzpy123.mapper.AuthUserMapper;
import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.UserNetdisc;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.service.AuthUserService;
import com.opzpy123.util.OssUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@Controller
@RequestMapping("file")
public class FileController {

    @Resource
    private UserNetdiscMapper userNetdiscMapper;

    @Resource
    private OssUtils ossUtils;

    @Resource
    private AuthUserService authUserService;

    @ResponseBody
    @PostMapping("upload")
    @Transactional(rollbackFor = Exception.class)
    ApiResponse<String> upload(MultipartFile file, Principal principal) throws IOException {
        //判空
        if (file == null) return ApiResponse.ofSuccess("文件不能为空");
        AuthUser authUser = authUserService.getUserByUsername(principal.getName());
        //判重
        String fileKey = authUser.getUsername() + "/" + file.getOriginalFilename();
        if (ossUtils.isFileExist(fileKey)) {
            return ApiResponse.ofSuccess("文件已存在");
        }

        UserNetdisc userNetdisc = new UserNetdisc();
        userNetdisc.setFileName(file.getOriginalFilename());
        userNetdisc.setSize(file.getSize() + "");
        userNetdisc.setPath("/" + file.getOriginalFilename());
        userNetdisc.setUserId(authUser.getId());

        String url = ossUtils.upload(file.getInputStream(), fileKey);
        userNetdisc.setUrl(url);
        userNetdiscMapper.insert(userNetdisc);
        return ApiResponse.ofSuccess("上传成功");
    }

    @ResponseBody
    @DeleteMapping("delete")
    @Transactional(rollbackFor = Exception.class)
    ApiResponse<String> delete(Long id){
        UserNetdisc userNetdisc = userNetdiscMapper.selectById(id);
        AuthUser authUser = authUserService.getById(userNetdisc.getUserId());
        String key = authUser.getUsername()+userNetdisc.getPath();
        ossUtils.deleteFile(key);
        userNetdiscMapper.deleteById(id);
        return ApiResponse.ofSuccess();
    }
}
