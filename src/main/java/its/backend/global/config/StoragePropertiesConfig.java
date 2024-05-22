package its.backend.global.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "demo.file")
@Setter
@Getter
@ToString
public class StoragePropertiesConfig {
    private String dir;
    private String dirUser;

    /**
     * 파일업로드 기본 체크 사항
     * extDisallowList 확장자 비허용 리스느
     * mimetypeDisallowList mime-type 비허용 리스트
     */
    private List<String> extDisallowList;
    private List<String> mimetypeDisallowList;
    
    /** Default Upload File Path */
    private String dirDefault;

    /** Excel Upload File Path */
    private String dirExcel;

}
