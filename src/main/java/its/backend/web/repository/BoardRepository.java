package its.backend.web.repository;

import its.backend.global.model.SearchCondition;
import its.backend.web.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByOrderBySeqDesc(Pageable pageable, SearchCondition searchCondition);
}
