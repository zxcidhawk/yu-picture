package com.yupi.yupicturebackend.controller;

import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.yupi.yupicturebackend.annotation.AuthCheck;
import com.yupi.yupicturebackend.common.BaseResponse;
import com.yupi.yupicturebackend.common.ResultUtils;
import com.yupi.yupicturebackend.constant.UserConstant;
import com.yupi.yupicturebackend.exception.BusinessException;
import com.yupi.yupicturebackend.exception.ErrorCode;
import com.yupi.yupicturebackend.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private CosManager cosManager;

    /**
     * 测试文件上传
     * @return
     */
    @PostMapping("/test/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> testUploadFile(@RequestPart("file")MultipartFile multipartFile) {
        // 文件目录
        String filename = multipartFile.getOriginalFilename();
        String filepath = String.format("/test/%s", filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问的地址
            return ResultUtils.success(filepath);
        } catch (IOException e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 测试文件下载
     * @return
     */
    @GetMapping("/test/download")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public void testDownloadFile(String filepath, HttpServletResponse response) {
        COSObjectInputStream cosObjectInput = null;
        try {
            COSObject cosObject = cosManager.getObject(filepath);
            cosObjectInput = cosObject.getObjectContent();
            byte[] butes = IOUtils.toByteArray(cosObjectInput);
            // 设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filepath);
            // 写入数据
            response.getOutputStream().write(butes);

        } catch (IOException e) {
            log.error("file download error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        } finally {
            // 释放流
            if (cosObjectInput != null) {
                IOUtils.release(cosObjectInput, log);
            }
        }

    }

}
