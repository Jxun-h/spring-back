package its.backend.web.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import its.backend.global.config.StoragePropertiesConfig;
import its.backend.global.config.error.exception.BusinessException;
import its.backend.global.util.FileUpload;
import its.backend.web.dto.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService extends EgovAbstractServiceImpl {
    private final StoragePropertiesConfig storagePropertiesConfig;
    private final FileUpload fileUpload;

    public List<FileDto> selFileList(FileDto fileDto) throws BusinessException {
        return null;
    }
}
