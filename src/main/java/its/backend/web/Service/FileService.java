package its.backend.web.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import its.backend.global.config.StoragePropertiesConfig;
import its.backend.global.util.FileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService extends EgovAbstractServiceImpl {
    private final StoragePropertiesConfig storagePropertiesConfig;
    private final FileUpload fileUpload;


}
