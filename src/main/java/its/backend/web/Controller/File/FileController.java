package its.backend.web.Controller.File;

import its.backend.global.config.PropertiesConfig;
import its.backend.global.config.StoragePropertiesConfig;
import its.backend.global.util.FileDownload;
import its.backend.global.util.FileUpload;
import its.backend.global.util.FileUploadUtil;
import its.backend.web.Service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileDownload fileDownload;
    private final FileUpload fileUpload;
    private final StoragePropertiesConfig storagePropertiesConfig;
    private final FileService fileService;
    private final FileUploadUtil fileUploadUtil;
    private final PropertiesConfig propertiesConfig;

    
}
