package com.jmz.jmzfile.fileController;



import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzfile.util.AliyunOssDownloadUtil;
import com.jmz.jmzfile.util.AliyunOssUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/fileinfo")
public class FileController {

    @Autowired
    private AliyunOssUtil aliyunOssUtil;
    @Autowired
    private AliyunOssDownloadUtil aliyunOssDownloadUtil;

    @Value("${oss.bucketName}")
    private String bucketName;
    @Value("${oss.endpoint}")
    private String endpoint;

    /**
     * 单文件上传接口（上传到阿里云OSS）
     */
    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return R.error("文件为空");
        }
        String originalFilename = file.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(originalFilename);
        String objectName = LocalDate.now() + "/" + UUID.randomUUID().toString().replace("-", "");
        if (ext != null && !ext.isEmpty()) {
            objectName += "." + ext;
        }
        String etag = aliyunOssUtil.upload(file.getInputStream(), objectName);
        //String url = "https://" + bucketName + "." + endpoint.replaceFirst("^https?://", "") + "/" + objectName;
        String url = objectName;
        return R.success("上传文件成功", url);
    }

    /**
     * 多文件上传接口（上传到阿里云OSS）
     */
    @PostMapping("/multi-upload")
    public R multiUpload(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                urls.add("文件为空");
                continue;
            }
            String originalFilename = file.getOriginalFilename();
            String ext = StringUtils.getFilenameExtension(originalFilename);
            String objectName = LocalDate.now() + "/" + UUID.randomUUID().toString().replace("-", "");
            if (ext != null && !ext.isEmpty()) {
                objectName += "." + ext;
            }
            aliyunOssUtil.upload(file.getInputStream(), objectName);
            //String url = "https://" + bucketName + "." + endpoint.replaceFirst("^https?://", "") + "/" + objectName;
            String url = objectName;
            urls.add(url);
        }
        return R.success(urls);
    }

    /**
     * OSS单文件下载接口
     * @param objectKey OSS对象Key（如 2025-07-15/xxx.png）
     * @param localFilePath 本地保存路径
     */
    @GetMapping("/oss-download")
    public R ossDownload(@RequestParam("objectKey") String objectKey,
                         @RequestParam("localFilePath") String localFilePath) {
        aliyunOssDownloadUtil.download(objectKey, localFilePath);
        return R.success("下载完成: " + localFilePath);
    }

    /**
     * OSS批量下载接口
     * @param folderPrefix OSS文件夹前缀（如 2025-07-15/，可传空字符串下载全部）
     * @param localDownloadPath 本地保存根目录
     */
    @GetMapping("/oss-batch-download")
    public R ossBatchDownload(@RequestParam("folderPrefix") String folderPrefix,
                              @RequestParam("localDownloadPath") String localDownloadPath) {
        aliyunOssDownloadUtil.batchDownload(folderPrefix, localDownloadPath);
        return R.success("批量下载完成: " + localDownloadPath);
    }


    // 本地下载接口保留，如需OSS下载可扩展
    @GetMapping("/download")
    public void download(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
        Path filePath = Paths.get("uploads", filename);
        if (!Files.exists(filePath)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("文件不存在");
            return;
        }
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        try (InputStream in = Files.newInputStream(filePath); ServletOutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        }
    }

    @DeleteMapping("/oss-delete")
    public R ossDelete(@RequestParam("objectKey") String objectKey) {
        aliyunOssUtil.delete(objectKey);
        return R.success("删除成功: " + objectKey);
    }

    @DeleteMapping("/oss-batch-delete")
    public R ossBatchDelete(@RequestBody List<String> objectKeys) {
        aliyunOssUtil.deleteBatch(objectKeys);
        return R.success("批量删除成功");
    }

    @DeleteMapping("/oss-delete-by-prefix")
    public R ossDeleteByPrefix(@RequestParam("prefix") String prefix) {
        aliyunOssUtil.deleteByPrefix(prefix);
        return R.success("目录删除成功: " + prefix);
    }
} 