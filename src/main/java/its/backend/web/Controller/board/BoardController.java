package its.backend.web.Controller.board;

import its.backend.global.model.Header;
import its.backend.global.model.SearchCondition;
import its.backend.web.Service.BoardService;
import its.backend.web.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board/list")
    public Header<List<BoardDto>> boardList(@PageableDefault(sort = {"idx"}) Pageable pageable, SearchCondition searchCondition) {
        return boardService.getBoardList(pageable, searchCondition);
    }

}
