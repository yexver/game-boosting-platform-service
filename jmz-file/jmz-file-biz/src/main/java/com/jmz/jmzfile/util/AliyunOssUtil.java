package com.jmz.jmzfile.util;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * 阿里云OSS上传工具类
 */
@Component
public class AliyunOssUtil {

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

    /**
     * 上传文件到OSS
     * @param inputStream 文件流
     * @param objectName OSS对象名（路径/文件名.后缀）
     * @return 上传结果的ETag或URL
     * @throws Exception 上传失败抛出异常
     */
    public String upload(InputStream inputStream, String objectName) throws Exception {
        OSS ossClient = buildOssClient();
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 返回ETag或拼接URL
            return result.getETag();
        } catch (OSSException oe) {
            throw new RuntimeException("OSS服务端异常: " + oe.getErrorMessage(), oe);
        } catch (ClientException ce) {
            throw new RuntimeException("OSS客户端异常: " + ce.getMessage(), ce);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    
    /**
     * 删除指定OSS对象
     * @param objectKey 要删除的objectKey
     */
    public void delete(String objectKey) {
        OSS ossClient = buildOssClient();
        try {
            ossClient.deleteObject(bucketName, objectKey);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 批量删除OSS对象
     * @param objectKeys 要删除的objectKey列表
     */
    public void deleteBatch(List<String> objectKeys) {
        OSS ossClient = buildOssClient();
        try {
            for (String objectKey : objectKeys) {
                ossClient.deleteObject(bucketName, objectKey);
            }
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 删除指定前缀（目录）下所有OSS对象
     * @param prefix 目录前缀，如 2024-07-15/
     */
    public void deleteByPrefix(String prefix) {
        OSS ossClient = buildOssClient();
        try {
            String nextMarker = null;
    ObjectListing objectListing;
    do {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName)
                .withPrefix(prefix)
                .withMarker(nextMarker)
                .withMaxKeys(1000);
        objectListing = ossClient.listObjects(listObjectsRequest);
        // ...
        nextMarker = objectListing.getNextMarker();
    } while (objectListing.isTruncated());
        } finally {
            ossClient.shutdown();
        }
    }
} 