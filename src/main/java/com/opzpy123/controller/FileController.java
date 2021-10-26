package com.opzpy123.controller;

import com.opzpy123.mapper.UserNetdiscMapper;
import com.opzpy123.model.AuthUser;
import com.opzpy123.model.UserNetdisc;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.model.vo.OssUploadTask;
import com.opzpy123.service.AuthUserService;
import com.opzpy123.util.OssUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("file")
public class FileController {

    @Resource
    private UserNetdiscMapper userNetdiscMapper;


    @Resource
    private AuthUserService authUserService;

    //存放上传任务
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    @SneakyThrows
    @ResponseBody
    @PostMapping("upload")
    @Transactional(rollbackFor = Exception.class)
    ApiResponse<String> upload(MultipartFile file, Principal principal) {
        log.info("文件开始上传:{}", file.getOriginalFilename());
        //判空
        try {
            if (file.isEmpty()) {
                log.info("文件不能为空");
                return ApiResponse.ofSuccess("文件不能为空");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        AuthUser authUser = authUserService.getUserByUsername(principal.getName());
        //判重
        String fileKey = authUser.getUsername() + "/" + file.getOriginalFilename();
        OssUtils ossUtils = new OssUtils();
        if (ossUtils.isFileExist(fileKey)) {
            log.info("文件已存在");
            return ApiResponse.ofSuccess("文件已存在");
        }

        UserNetdisc userNetdisc = new UserNetdisc();
        userNetdisc.setFileName(file.getOriginalFilename());
        userNetdisc.setSize(file.getSize() + "");
        userNetdisc.setPath("/" + file.getOriginalFilename());
        userNetdisc.setUserId(authUser.getId());
        userNetdiscMapper.insert(userNetdisc);

        //上传任务放入线程池
        threadPoolExecutor.execute(new OssUploadTask(fileKey,file.getInputStream(),userNetdisc.getId()));
        log.info(authUser.getUsername() + "上传了:" + file.getOriginalFilename());
        return ApiResponse.ofSuccess("上传成功");
    }

    @ResponseBody
    @DeleteMapping("delete")
    @Transactional(rollbackFor = Exception.class)
    ApiResponse<String> delete(Long id) {
        OssUtils ossUtils = new OssUtils();
        UserNetdisc userNetdisc = userNetdiscMapper.selectById(id);
        AuthUser authUser = authUserService.getById(userNetdisc.getUserId());
        String key = authUser.getUsername() + userNetdisc.getPath();
        ossUtils.deleteFile(key);
        userNetdiscMapper.deleteById(id);
        log.info(authUser.getUsername() + "删除了:" + userNetdisc.getPath());
        return ApiResponse.ofSuccess();
    }
}
