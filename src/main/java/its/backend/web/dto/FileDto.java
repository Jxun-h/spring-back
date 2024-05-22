package its.backend.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FileDto {
    private Long fileSeq;
    private String targetDivsCd;
    private String targetSeq;
    private String sysFileNm;
    private String originFileNm;
    private String filePathInfo;
    private String fileUrlInfo;
    private String fileSize;
    private String regDtm;
    private String regId;
    private String updtDtm;
    private String updtId;

    private List<Long> fileSeqList;
}
