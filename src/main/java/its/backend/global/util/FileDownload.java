package its.backend.global.util;

import its.backend.global.config.StoragePropertiesConfig;
import its.backend.global.config.error.exception.StorageException;
import its.backend.web.dto.FileDto;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Slf4j
public class FileDownload {
    /**
     * 파일 업로드 루트 디렉터리 경로
     */
    private String rootDir;
    /**
     * 암호화 유틸
     */
    private AES256Ciper aes256Ciper;

    /**
     * 파일 다운로드 생성자
     *
     * @param storagePropertiesConfig
     */
    public FileDownload(StoragePropertiesConfig storagePropertiesConfig) {
        this.rootDir = storagePropertiesConfig.getDir();
        this.aes256Ciper = AES256Ciper.getINSTANCE();
    }

    /**
     * 파일 경로 Decryption
     *
     * @param encryptFilePath
     * @return
     * @throws StorageException
     */
    public String decryptFilePath(String encryptFilePath) throws StorageException {
        try {
            return aes256Ciper.decrypt(CommonUtil.decryptPath(encryptFilePath));
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new StorageException("파일 URL 디코딩 중 에러가 발생했습니다.");
        }
    }

    /**
     * 파일 리소스 불러오기
     *
     * @param filePath 파일 경로
     * @return 파일 리소스
     */
    public Resource loadAsResource(String filePath) {
        try {
            Path file = Path.of(rootDir, filePath);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                String msg = "파일이 존재 하지 않습니다.";
                log.error(msg + "파일 : " + file);
                throw new StorageException(msg);
            }
        } catch (MalformedURLException e) {
            log.error("경로에서 URL을 확인할 수 없습니다. 파일 : " + filePath, e);
        }

        return null;
    }

    public void compressZipFile(List<FileDto> fileList, String compressFile) throws StorageException {

        if (!fileList.isEmpty()) {
            try (
                    FileOutputStream fos = new FileOutputStream(compressFile);
                    ZipOutputStream zos = new ZipOutputStream(fos)
            ) {

                byte[] buffer = new byte[1024];

                for (FileDto dto : fileList) {
                    ZipEntry zipEntry = new ZipEntry(dto.getOriginFileNm());
                    zos.putNextEntry(zipEntry);

                    @Cleanup FileInputStream fis = new FileInputStream(Path.of(this.rootDir, dto.getFilePathInfo(), dto.getSysFileNm()).toFile());

                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                }

            } catch (FileNotFoundException e) {
                throw new StorageException("파일이 존재하지 않습니다.", e);
            } catch (IOException e) {
                throw new StorageException("파일을 읽어올 수 없습니다.", e);
            }
        } else {
            throw new StorageException("파일 리스트가 비어있거나 잘못됐습니다.");
        }
    }

    /**
     * 리소스 디렉터리 안의 파일 다운로드
     *
     * @param path     파일 경로
     * @param filename 파일명
     * @return ResponseEntity<Resource> 리소스 응답값
     */
    public ResponseEntity<Resource> downloadResourceFile(String path, String filename) {
        try {
            String filePath = ResourceUtils.getFile("classpath:" + path + File.separator + filename).getPath();
            Resource resource = loadAsResourceForResourcePath(filePath);
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment").filename(filename, StandardCharsets.UTF_8).build();

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, String.valueOf(contentDisposition)).body(resource);
        } catch (FileNotFoundException e) {
            log.error("경로에서 파일을 확인할 수 없습니다. \n" + path + File.separator + filename, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 파일 리소스 Load
     *
     * @param filePath 파일 경로
     * @return 파일 리소스
     */
    private Resource loadAsResourceForResourcePath(String filePath) {
        try {
            Resource resource = new UrlResource(Paths.get(filePath).toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.error("파일을 읽어올 수 없습니다.\nfilePath : " + filePath);
            }
        } catch (MalformedURLException e) {
            log.error("경로에서 URL을 읽어올 수 없습니다.\nfilePath : " + filePath, e);
        }
        return null;
    }

}
