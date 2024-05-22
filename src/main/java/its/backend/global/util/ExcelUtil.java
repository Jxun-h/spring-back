package its.backend.global.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class ExcelUtil {
    private static MessageSource messageSource;

    public static void makeExcel(Object data, String templateName, @RequestParam Map<String, Object> paramMap,
                                 HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {

        String templatesPath = getTemplatesPath();
        String templateFile = templatesPath + File.separator + templateName;
        String tempFile = makeTempFilePath(templatesPath, templateName);

        Map<String, Object> beans = null;

        if (data instanceof List) {
            beans = new HashMap<>();
            beans.put("dataList", data);
        } else if (data instanceof Map) {
            beans = (Map) data;
        }

        // Get Excel Data Load - XSSF (With Excel Template)

        // Download Excel Data - SXSSF (Streaming - Out Of Memory Problem Solution)
        
    }

    /**
     * 임시 파일 디렉터리 생성
     * @param templatesPath
     * @param fileName
     * @return String 임시 파일 디렉터리 경로
     */
    private static String makeTempFilePath(String templatesPath, String fileName) {
        return tempPathValidate(templatesPath)
                + File.separator
                + UUID.randomUUID()
                + fileName.replaceAll(".+(\\.[^.]+)", "$1");
    }

    /**
     * 임시 파일 디렉터리 경로 validation
     * @param templatesPath
     * @return String 임시 디렉터리 경로
     */
    private static String tempPathValidate(String templatesPath) {
        String tempPath = templatesPath + File.separator + "temp";
        File dir = new File(tempPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return tempPath;
    }

    /**
     * 템플릿 파일 경로 리턴
     * @return String 템플릿 파일 경로
     * @throws FileNotFoundException
     */
    private static String getTemplatesPath() throws FileNotFoundException {
        return ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "fileTemplates/excel").getPath();
    }

}
