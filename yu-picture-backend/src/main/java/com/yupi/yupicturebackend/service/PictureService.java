package com.yupi.yupicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.yupicturebackend.model.dto.picture.PictureQueryRequest;
import com.yupi.yupicturebackend.model.dto.picture.PictureUploadRequest;
import com.yupi.yupicturebackend.model.entity.Picture;
import com.yupi.yupicturebackend.model.entity.User;
import com.yupi.yupicturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author 86182
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-05-01 10:35:16
*/
public interface PictureService extends IService<Picture> {

    /**
     * 校验图片
     *
     * @param picture 图片
     */
    void validPicture(Picture picture);

    /**
     * 上传图片
     *
     * @param multipartFile 文件
     * @param pictureUploadRequest 上传图片请求
     * @param loginUser 登录用户
     * @return 图片信息
     */
    PictureVO uploadPicture(MultipartFile multipartFile,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);

    /**
     * 获取图片封装类（单条）
     * @param picture 图片
     * @param request 请求
     * @return 图片封装类
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 获取图片封装类（分页）
     * @param picturePage 图片分页
     * @param request 请求
     * @return 图片封装类
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param pictureQueryRequest 查询条件
     * @return 查询条件
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);
}
