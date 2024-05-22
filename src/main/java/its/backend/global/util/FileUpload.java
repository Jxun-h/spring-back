package its.backend.global.util;

import its.backend.global.config.StoragePropertiesConfig;
import its.backend.global.config.error.exception.StorageException;
import its.backend.web.dto.UploadFileDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FileUpload {
    private final String rootDir;
    private final List<String> extDisallowList;
    private final List<String> mimtypeDisallowList;
    private final AES256Ciper aes256Ciper;

    /**
     * 파일업로드 생성자
     *
     * @param storagePropertiesConfig
     */
    public FileUpload(StoragePropertiesConfig storagePropertiesConfig) {
        this.rootDir = storagePropertiesConfig.getDir();
        this.extDisallowList = storagePropertiesConfig.getExtDisallowList();
        this.mimtypeDisallowList = storagePropertiesConfig.getMimetypeDisallowList();
        this.aes256Ciper = AES256Ciper.getINSTANCE();
    }

    /**
     * 파일 경로 암호화
     *
     * @param filePath 파일 경로
     * @return String 암호화 된 파일 경로
     * @throws StorageException 오류 객체
     */
    public String encryptFilePath(String filePath) throws StorageException {
        try {
            return CommonUtil.encryptPath(aes256Ciper.encrypt(filePath));
        } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new StorageException("파일 URL 인코딩 중 에러가 발생했습니다.");
        }
    }

    /**
     * 파일 경로 복호화
     *
     * @param filePath 파일 경로
     * @return String 복호화 된 파일 경로
     * @throws StorageException 오류 객체
     */
    public String decryptFilePath(String filePath) throws StorageException {
        try {
            return aes256Ciper.decrypt(CommonUtil.decryptPath(filePath));
        } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new StorageException("파일 URL 디코딩 중 에러가 발생했습니다.");
        }
    }

    /**
     * 파일 저장
     * @param originFile 원본 파일
     * @param filePath 파일 저장 경로 (root dir X)
     * @param isChkImg 이미지 파일 여부 체크
     * @return UploadFileDto 파일 객체
     * @throws StorageException 오류 객체
     */
    public UploadFileDto storeFile(@NotNull MultipartFile originFile, @NotEmpty String filePath, boolean isChkImg) throws StorageException {
        return storeFile(originFile, filePath, isChkImg, null);
    }

    public UploadFileDto storeFile(@NotNull MultipartFile originFile, @NotEmpty String filePath, @NotEmpty boolean isChkImg, String[] extCustomWhiteArr) throws StorageException {
        String originFileName = Path.of(StringUtils.cleanPath(originFile.getOriginalFilename())).getFileName().toString();

        isValidFile(originFile, isChkImg, Optional.ofNullable(extCustomWhiteArr).map(m -> Arrays.asList(extCustomWhiteArr)).orElse(null));

        try {
            Files.createDirectories(Path.of(rootDir));

            try (InputStream is = originFile.getInputStream()) {
                String sysFileNm = CommonUtil.createUudi() + "." + StringUtils.getFilenameExtension(originFileName);
                Path path = Path.of(rootDir, filePath, sysFileNm);

                Files.createDirectories(path.getParent());
                Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);

                return new UploadFileDto(originFileName, sysFileNm, filePath, originFile.getSize(), originFile.getName(),
                        encryptFilePath(Path.of(filePath, sysFileNm).toString() + "|" + originFileName));
            }
        } catch (IOException e) {
            throw new StorageException("디렉터리 생성 혹은 파일 저장 중에 에러가 발생했습니다.");
        }
    }

    /**
     * 파일 검증
     * @param file 검증할 파일 
     * @param isChkImg 이미지 파일 체크 여부
     * @param extCustomWhiteList 커스텀 사용 가능 확장자 목록
     * @return boolean 검증 반환 값
     * @throws StorageException 오류 객체
     */
    private boolean isValidFile(@NotEmpty MultipartFile file, @NotEmpty boolean isChkImg, List<String> extCustomWhiteList) throws StorageException {
        try {
            if (file.isEmpty()) {
                throw new StorageException("비어있는 파일입니다.\n파일명 : " + file.getOriginalFilename());
            }

            String originFileName = Path.of(StringUtils.cleanPath(file.getOriginalFilename())).getFileName().toString();

            isValidCleanFileName(originFileName);
            isValidFileExtension(extCustomWhiteList, originFileName);

            String mimeType = new Tika().detect(file.getInputStream());

            isValidMimeType(originFileName, mimeType);
            isValidImageFile(isChkImg, originFileName, mimeType);

            return true;

        } catch (IOException e) {
            throw new StorageException("파일을 읽어오는 중 에러가 발생했습니다.\n 파일명 : " + file.getOriginalFilename());
        }
    }

    /**
     * MIME-TYPE 으로 Image 파일 여부 확인
     * @param isChkImg 이미지 여부 체크
     * @param originFileName 원본 파일명
     * @param mimeType MIME-TYPE
     * @throws StorageException 오류 객체
     */
    private void isValidImageFile(boolean isChkImg, String originFileName, String mimeType) throws StorageException {
        if (isChkImg && mimeType.indexOf("image") < 0) {
            throw new StorageException("이미지 파일만 업로드 가능합니다.\n파일명 : " + originFileName);
        }
    }

    private void isValidMimeType(String originFileName, String mimeType) throws StorageException {
        if (mimtypeDisallowList.contains(mimeType)) {
            throw new StorageException("허용되지 않은 파일 MIME-TYPE 의 파일입니다.");
        }
    }

    /**
     * 파일 확장자 검증
     *
     * @param extCustomWhiteList 커스텀 사용 가능 확장자 목록
     * @param originFileName     파일명
     * @throws StorageException 오류 객체
     */
    private void isValidFileExtension(List<String> extCustomWhiteList, String originFileName) throws StorageException {

        if (ObjectUtils.isEmpty(extCustomWhiteList)) {
            if (extDisallowList.contains(StringUtils.getFilenameExtension(originFileName).toLowerCase())) {
                throw new StorageException("허용되지 않은 확장자입니다.");
            } else {
                if (!extCustomWhiteList.contains(StringUtils.getFilenameExtension(originFileName).toLowerCase())) {
                    throw new StorageException("허용되지 않은 확장자입니다.");
                }
            }
        }
    }

    /**
     * 파일명 검증
     * @param originFileName 파일명
     * @throws StorageException 오류 객체
     */
    private void isValidCleanFileName(String originFileName) throws StorageException {
        if (originFileName.matches(".*(\\.\\.|\\/|\\\\\\\\).*")) {
            throw new StorageException("외부 디렉터리 경로로 저장이 불가합니다.\n파일명에는 [.. / \\] 문자가 포함될 수 없습니다.\n파일명 : " + originFileName);
        }
    }

}
