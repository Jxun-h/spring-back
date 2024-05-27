package its.backend.global.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileUploadResult {

    @Getter
    private final MultipartFile multipartFile;
    @Getter
    private final File uploadFile;
    @Setter
    @Getter
    private String uploadRelPath;
    @Getter
    @Setter
    private String requestName;
    @Getter
    @Setter
    private String fileSize;


    public FileUploadResult(MultipartFile mf, File uf, String uploadRelPath, String requestName, String fileSize) {
        this.multipartFile = mf;
        this.uploadFile = uf;
        this.uploadRelPath = uploadRelPath;
        this.requestName = requestName;
        this.fileSize = fileSize;
    }

    /**
     * Origin File name
     *
     * @return String Origin File name
     */
    public String getOriginalFileName() {
        return multipartFile.getOriginalFilename();
    }

    /**
     * 파일 이름 새로 가져오기
     * 중복 파일명 존재 여부 판단 후 파일명 반환
     * 
     * @return String 새로운 파일명
     */
    public String getNewFileName() {
        return uploadFile.getName();
    }

    /**
     * Content Type 반환
     * @return String Content Type
     */
    public String getContentType() {
        return this.multipartFile.getContentType();
    }

    /**
     * String 으로 데이터 반환
     *
     * @return Stirng 파일 정보
     */
    public String toString() {
        return "originFileName: " + multipartFile.getOriginalFilename()
                + ", newFileName: " + uploadFile.getName()
                + ", fileSize: " + multipartFile.getSize()
                + ", contentType: " + multipartFile.getContentType()
                + ", uploadRelPath: " + uploadRelPath
                + ", requestName: " + multipartFile.getName();
    }
}
