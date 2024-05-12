package its.backend.web.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import its.backend.global.model.SearchCondition;
import its.backend.web.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static its.backend.web.entity.QBoard.board;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<Board> findAllBySearchCondition(Pageable pageable, SearchCondition searchCondition) {
        JPAQuery<Board> query = queryFactory.selectFrom(board)
                .where(searchKeywords(searchCondition.getSearchKey(), searchCondition.getSearchVal()));

        long total = query.stream().count();

        List<Board> results = query
                .where(searchKeywords(searchCondition.getSearchKey(), searchCondition.getSearchVal()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.seq.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression searchKeywords(String searchKey, String searchVal) {
        if ("author".equals(searchKey)) {
            if (StringUtils.hasLength(searchVal)) {
                return board.author.contains(searchVal);
            }
        } else if ("title".equals(searchKey)) {
            if (StringUtils.hasLength(searchVal)) {
                return board.title.contains(searchVal);
            }
        } else if ("contents".equals(searchKey)) {
            if (StringUtils.hasLength(searchVal)) {
                return board.contents.contains(searchVal);
            }
        }
        return null;
    }
}
