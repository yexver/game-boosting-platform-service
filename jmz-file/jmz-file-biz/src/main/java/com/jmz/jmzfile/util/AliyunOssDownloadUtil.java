package com.jmz.jmzfile.util;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class AliyunOssDownloadUtil {

    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;
    @Value("${oss.region}")
    private String region;

    /**
     * 单文件下载
     * @param objectKey OSS对象Key（如 2025-07-15/xxx.png）
     * @param localFilePath 本地保存路径
     */
    public void download(String objectKey, String localFilePath) {
        OSS ossClient = buildOssClient();
        try {
            ossClient.getObject(new GetObjectRequest(bucketName, objectKey), new File(localFilePath));
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 批量下载指定前缀下所有文件
     * @param folderPrefix OSS文件夹前缀（如 2025-07-15/，可传空字符串下载全部）
     * @param localDownloadPath 本地保存根目录
     */
    public void batchDownload(String folderPrefix, String localDownloadPath) {
        OSS ossClient = buildOssClient();
        try {
            String nextMarker = null;
            ObjectListing objectListing;
            do {
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName)
                        .withPrefix(folderPrefix)
                        .withMarker(nextMarker)
                        .withMaxKeys(1000);
                objectListing = ossClient.listObjects(listObjectsRequest);
                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                for (OSSObjectSummary s : sums) {
                    if (s.getKey().endsWith("/")) continue; // 跳过文件夹
                    String localFilePath = constructLocalFilePath(localDownloadPath, s.getKey(), folderPrefix);
                    File localFile = new File(localFilePath);
                    createDirectoryIfNotExists(localFile.getParent());
                    if (localFile.exists() && localFile.length() == s.getSize()) continue;
                    File tempFile = new File(localFilePath + ".tmp");
                    try {
                        ossClient.getObject(new GetObjectRequest(bucketName, s.getKey()), tempFile);
                        if (!tempFile.renameTo(localFile)) {
                            throw new IOException("无法重命名临时文件: " + tempFile.getPath() + " -> " + localFile.getPath());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (tempFile.exists()) tempFile.delete();
                    }
                }
                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
        } finally {
            ossClient.shutdown();
        }
    }

    private OSS buildOssClient() {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        return OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
    }

    private static String constructLocalFilePath(String localDownloadPath, String objectKey, String folderPrefix) {
        String relativePath = objectKey;
        if (folderPrefix != null && !folderPrefix.isEmpty() && objectKey.startsWith(folderPrefix)) {
            relativePath = objectKey.substring(folderPrefix.length());
        }
        String normalizedPath = localDownloadPath.endsWith(File.separator) ?
                localDownloadPath : localDownloadPath + File.separator;
        return normalizedPath + relativePath.replace("/", File.separator);
    }

    private static void createDirectoryIfNotExists(String dirPath) {
        if (dirPath == null || dirPath.isEmpty()) return;
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();
    }
} 