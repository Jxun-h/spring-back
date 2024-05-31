package its.backend.web.Controller.File;

import its.backend.global.config.PropertiesConfig;
import its.backend.global.config.StoragePropertiesConfig;
import its.backend.global.util.CommonUtil;
import its.backend.global.util.FileDownload;
import its.backend.global.util.FileUpload;
import its.backend.global.util.FileUploadUtil;
import its.backend.web.Service.FileService;
import its.backend.web.dto.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    /**
     * 이미지 다운로드
     *
     * @param filePath 파일 경로 정보
     * @return byte[] 반환 값
     * @throws IOException 오류 객체
     */
    @GetMapping(value = "/img", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/apng", "image/svg+xml", "image/webp"})
    @ResponseBody
    public byte[] downloadThumbnail(@RequestParam String filePath) throws IOException {
        String[] fileUrl = fileDownload.decryptFilePath(filePath).split("[|]");
        Path path = Path.of(storagePropertiesConfig.getDir(), fileUrl[0]);

        if (!Files.exists(path)) {
            log.error("FILE NOT FOUND : " + path.toUri());
            return IOUtils.toByteArray(Paths.get(ResourceUtils.getFile("classpath:static" + File.separator + "images" + File.separator + "common" + File.separator + "img_default.jpg").getPath()).toUri());
        }
        return IOUtils.toByteArray(Path.of(storagePropertiesConfig.getDir(), fileUrl[0]).toUri());
    }

    /**
     * 파일 다운로드
     *
     * @param filePath 파일 경로
     * @return ResponseEntity<Resource> 응답 값
     */
    @GetMapping(value = "/**/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) {
        String[] fileUrl = fileDownload.decryptFilePath(filePath).split("[|]");
        Resource resource = fileDownload.loadAsResource(fileUrl[0]);

        if (ObjectUtils.isEmpty(resource)) {
            return ResponseEntity.notFound().build();
        }

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment").filename(fileUrl[1], StandardCharsets.UTF_8).build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, String.valueOf(contentDisposition)).body(resource);
    }

    @GetMapping(value = "/**/zipDownload")
    @ResponseBody
    public void zipDownload(@RequestParam Map<String, Object> paramMap, HttpServletResponse response) throws IOException {
        FileDto paramDto = new FileDto();
        paramDto.setTargetDivsCd(CommonUtil.toStr(paramMap.get("targetDivsCd")));
        paramDto.setTargetSeq(CommonUtil.toStr(paramMap.get("targetSeq")));
        List<FileDto> fileList = fileService.selFileList(paramDto);

        List<String> files = fileList.stream()
                .map(f -> {
                    String fileUrl = fileDownload.decryptFilePath(f.getFilePathInfo());
                    String[] fileStr = fileUrl.split("[|]");
                    Path path = Path.of(storagePropertiesConfig.getDir(), fileStr[0]);
                    File file = path.toFile();

                    return file.getAbsolutePath();
                })
                .collect(Collectors.toList());

        zipDownload(files, response);
    }

    private void zipDownload(List<String> files, HttpServletResponse response) throws IOException {
        String downloadFileName = "result";
        try (ByteArrayOutputStream fout = new ByteArrayOutputStream()) {
            ZipOutputStream zout = new ZipOutputStream(fout);

            for (String f : files) {
                ZipEntry zipEntry = new ZipEntry(new File(f).getName());
                zout.putNextEntry(zipEntry);

                FileInputStream fin = new FileInputStream(f);
                byte[] buffer = new byte[1024];
                int length;

                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }

                zout.closeEntry();
                fin.close();
            }
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + downloadFileName + ".zip");

            response.getOutputStream().write(fout.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
