package its.backend.global.util;

import its.backend.global.config.StoragePropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.util.Map;

@Component
@Slf4j
public class FileUploadUtil {

    private final FileUpload fileUpload;
    private final StoragePropertiesConfig storagePropertiesConfig;

    public FileUploadUtil(FileUpload fileUpload, StoragePropertiesConfig storagePropertiesConfig) {
        this.fileUpload = fileUpload;
        this.storagePropertiesConfig = storagePropertiesConfig;
    }

    /**
     * 디렉터리 생성
     *
     * @param dir 디렉터리 경로
     */
    public static void createDir(String dir) {
        File bf = new File(dir);
        if (!bf.exists()) {
            bf.mkdirs();
        }
    }

    public FileUploadResults uploadForDirectory(HttpServletRequest request, String uploadRelativePath1) throws IOException {
        MultipartHttpServletRequest multipartRequest;

        try {
            Assert.state(request instanceof MultipartHttpServletRequest, "request !instanceof MultipartHttpServletRequest");
            multipartRequest = (MultipartHttpServletRequest) request;
        } catch (Exception e) {
            return null;
        }

        Map fileMap = multipartRequest.getFileMap();

        if (MapUtils.isEmpty(fileMap)) {
            return null;
        }

        String uploadBasePath = StringUtils.defaultString(storagePropertiesConfig.getDir()).replaceAll("/+$", "") + File.separator;
        String uploadRelativePath = StringUtils.defaultString(uploadRelativePath1).replaceAll("/+$", "");
        uploadRelativePath = StringUtils.defaultString(uploadRelativePath).replaceAll("^/+", "");

        FileUploadResults results = new FileUploadResults();

        String uploadPath = uploadBasePath;
        results.setUploadBase(uploadPath);

        String uploadRelPath = StringUtils.defaultString(uploadRelativePath);
        String systemFilename;
        String originalFileName;

        results.setUploadRelPath(uploadRelPath);
        results.setUploadPath(results.getUploadBase() + uploadRelPath + File.separator);
        results.setEncodeUploadPath(
                fileUpload.encryptFilePath(
                        Path.of(
                                results.getUploadBase(),
                                uploadRelPath,
                                File.separator
                        ).toString()));

        boolean directoryPrepared = false;

        uploadPath = results.getUploadPath();

        int nos = 1;

        for (Object item : fileMap.entrySet()) {
            Map.Entry entry = (Map.Entry) item;
            MultipartFile multipartFile = (MultipartFile) entry.getValue();

            if (!StringUtils.equals(multipartFile.getOriginalFilename(), "")) {
                results.addFileSize(multipartFile.getSize() + "");
                results.addMulipartFile(multipartFile);
                results.addRequestName(multipartFile.getName());

                if (ObjectUtils.isArray(StringUtils.split(multipartFile.getOriginalFilename(), "\\"))) {
                    originalFileName =
                            (StringUtils.split(
                                    multipartFile.getOriginalFilename(),
                                    "\\")
                            )[StringUtils.split(multipartFile.getOriginalFilename(),
                                    "\\").length - 1];
                } else {
                    originalFileName = multipartFile.getOriginalFilename();
                }

                results.addOriginFilename(originalFileName);

                if (!directoryPrepared) {
                    createDir(uploadPath);
                    directoryPrepared = true;
                }

                systemFilename = CommonUtil.CreateSystemFileName(originalFileName);

                File file1 = new File(uploadPath, systemFilename);
                results.addNewFilename(systemFilename);
                File file2 = file1;

                results.addEncodeUploadFullPath(
                        fileUpload.encryptFilePath(
                                Path.of(
                                    uploadRelativePath,
                                    File.separator,
                                    systemFilename
                                )
                                + "|"
                                + originalFileName));

                results.addUploadFile(file2);

                try (
                    InputStream inputStream = multipartFile.getInputStream();
                    OutputStream outputStream = new FileOutputStream(file2);
                ) {
                    FileCopyUtils.copy(inputStream, outputStream);
                } finally {
                    file1 = null;
                    file2 = null;
                }
            }
            nos++;
        }
        return results;
    }
}
