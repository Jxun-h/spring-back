package its.backend.global.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileUploadResults {
    private int size;
    /**
     * -- GETTER --
     * 멀티 파트 파일 리스트 반환
     *
     * @return mulipartFiles 멀티 파트 파일 리스트
     */
    @Getter
    private final List mulipartFiles = new ArrayList();
    private final List<File> uploadFiles = new ArrayList<>();
    @Getter
    private final List<String> originFilenames = new ArrayList<>();
    @Getter
    private final List<String> newFilenames = new ArrayList<>();
    @Getter
    private final List<String> requestName = new ArrayList<>();
    @Getter
    private final List<String> fileSize = new ArrayList<>();
    @Getter
    private final List<String> encodeUploadFullPath = new ArrayList<>();

    @Getter @Setter
    private String uploadBase, uploadRelPath, uploadPath, encodeUploadPath;

    /**
     * 이터레이터 프록시
     *
     * @return Iterator
     * @deprecated
     */
    public Iterator iterator() {
        return new FileUploadResultIterator();
    }

    /**
     * multipart 파일 추가
     *
     * @param multipartFile 멀티 파트 파일
     */
    public void addMulipartFile(MultipartFile multipartFile) {
        mulipartFiles.add(multipartFile);
    }

    /**
     * 파일 추가
     *
     * @param file 파일
     */
    public void addUploadFile(File file) {
        size++;
        uploadFiles.add(file);
    }

    /**
     * Origin File 이름 추가
     *
     * @param filename 파일 이름
     */
    public void addOriginFilename(String filename) {
        originFilenames.add(filename);
    }

    /**
     * 업로드 파일 Full Path
     *
     * @param uploadFullPath 업로드 파일 Full Path
     */
    public void addEncodeUploadFullPath(String uploadFullPath) {
        encodeUploadFullPath.add(uploadFullPath);
    }

    /**
     * 신규 파일명 추가
     *
     * @param filename 신규 파일명
     */
    public void addNewFilename(String filename) {
        newFilenames.add(filename);
    }

    /**
     * 업로드 파일 리스트 반환
     *
     * @return uploadFiles
     */
    public List<File> getMulipartFiles() {
        return uploadFiles;
    }

    /**
     * Request Name 지정
     *
     * @param requestName Request Name
     */
    public void addRequestName(String requestName) {
        this.requestName.add(requestName);
    }

    /**
     * 파일 사이즈 추가
     *
     * @param fileSize 파일 사이즈
     */
    public void addFileSize(String fileSize) {
        this.fileSize.add(fileSize);
    }

    /**
     * 업로드 결과
     * @return List<FileUploadResult> 결과
     */
    public List<FileUploadResult> getResultList() {
        if (CollectionUtils.isEmpty(uploadFiles)) {
            return null;
        }

        List<FileUploadResult> results = new ArrayList<>();

        int len = uploadFiles.size();

        for (int i = 0; i < len; i++) {
            results.add(
                new FileUploadResult(
                        (MultipartFile) mulipartFiles.get(i),
                        uploadFiles.get(i),
                        uploadRelPath,
                        requestName.get(i),
                        fileSize.get(i)
                )
            );
        }
        return results;
    }

    /**
     * Inner Iterator class
     * java.util.Iterator Interface
     */
    private class FileUploadResultIterator implements Iterator {
        private int pos;

        public boolean hasNext() {
            if (size <= 0) {
                return false;
            }
            if (size > pos) {
                return true;
            } else {
                pos = 0;
                return false;
            }
        }

        public Object next() {
            FileUploadResult fur = new FileUploadResult((MultipartFile) mulipartFiles.get(pos), uploadFiles.get(pos), uploadRelPath, requestName.get(pos), fileSize.get(pos));
            pos++;
            return fur;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
