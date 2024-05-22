package its.backend.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UploadFileDto {
    private String sysFileNm;
    private String originFileNm;
    private String filePath;
    private String fileUrl;
    private String reqName;
    private long fileSize;

    public UploadFileDto(String originFileNm, String sysFileNm, String filePath, long fileSize, String reqName, String fileUrl) {
        this.sysFileNm = sysFileNm;
        this.originFileNm = originFileNm;
        this.filePath = filePath;
        this.fileUrl = fileUrl;
        this.reqName = reqName;
        this.fileSize = fileSize;
    }
}