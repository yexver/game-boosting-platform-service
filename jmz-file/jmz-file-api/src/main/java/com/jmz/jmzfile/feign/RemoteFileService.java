package com.jmz.jmzfile.feign;

import com.jmz.jmzcommoncore.responseResult.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "jmz-file", contextId = "remoteFileService", path = "/fileinfo")
public interface RemoteFileService {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R upload(@RequestPart("file") MultipartFile file) throws Exception;

    @PostMapping(value = "/multi-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R multiUpload(@RequestPart("files") MultipartFile[] files) throws Exception;

    @GetMapping("/oss-download")
    R ossDownload(@RequestParam("objectKey") String objectKey,
                  @RequestParam("localFilePath") String localFilePath);

    @GetMapping("/oss-batch-download")
    R ossBatchDownload(@RequestParam("folderPrefix") String folderPrefix,
                      @RequestParam("localDownloadPath") String localDownloadPath);

    @DeleteMapping("/oss-delete")
    R ossDelete(@RequestParam("objectKey") String objectKey);

    @DeleteMapping("/oss-batch-delete")
    R ossBatchDelete(@RequestBody List<String> objectKeys);

    @DeleteMapping("/oss-delete-by-prefix")
    R ossDeleteByPrefix(@RequestParam("prefix") String prefix);
}
