package com.demo.appUser;

import com.demo.base.AjaxResult;
import com.demo.base.BaseController;
import com.demo.utils.DateTimeUtils;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import org.apache.log4j.Logger;

import java.io.File;

public class UploadController  extends BaseController {

    private static Logger logger = Logger.getLogger(UploadController.class);

    private static Prop p = PropKit.use("demo-config-dev.txt");
    /**
     * 文件上传
     */
    public void upload() {
        logger.debug("进入了调用方法");
        DateTimeUtils util = new DateTimeUtils();
        UploadFile upFile = getFile();
        String formatTime = util.getCurrentDateTimeStr();
        // 根据原文件名使用时间戳和随机数重命名，保存，原来的临时文件会自动删除
        String fileName = upFile.getFileName();
        logger.debug("调用获取到文件方法"+ fileName);
        String lastThreeLetter = fileName.substring(fileName.lastIndexOf("."));
        String sqlName = formatTime + (int) (Math.random() * 10000) + lastThreeLetter;
        upFile.getFile().renameTo(new File(PathKit.getWebRootPath() + "/upload/" + sqlName));
        logger.debug("获取到内容"+PathKit.getWebRootPath());
        logger.debug("获取到参数"+p.get("fileUpload_path"));
        String path = p.get("fileUpload_path") + sqlName;
        AjaxResult result = AjaxResult.successObj(path);
        renderJson(result);
    }
}
