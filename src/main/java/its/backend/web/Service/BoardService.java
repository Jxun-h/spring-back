package its.backend.web.Service;

import its.backend.global.model.Header;
import its.backend.global.model.Pagination;
import its.backend.global.model.SearchCondition;
import its.backend.web.dto.BoardDto;
import its.backend.web.entity.Board;
import its.backend.web.repository.BoardRepository;
import its.backend.web.repository.BoardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static its.backend.global.config.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardRepositoryCustom boardRepositoryCustom;


    /**
     * 게시글 목록 호출
     */
    public Header<List<BoardDto>> getBoardList(Pageable pageable, SearchCondition searchCondition) {
        List<BoardDto> dtos = new ArrayList<>();

        Page<Board> boards = boardRepositoryCustom.findAllBySearchCondition(pageable, searchCondition);

        for (Board entity : boards) {
            BoardDto dto = BoardDto.builder()
                    .seq(entity.getSeq())
                    .author(entity.getAuthor())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .createAt(entity.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                    .build();

            dtos.add(dto);
        }

        Pagination pagination = new Pagination(
                (int) boards.getTotalElements() + 0,
                pageable.getPageNumber() + 1,
                pageable.getPageSize() + 0,
                10
        );

        return Header.OK(dtos, pagination);
    }
}
