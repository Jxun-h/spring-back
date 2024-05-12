package its.backend.web.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "BOARD")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long fileSeq;
    private String title;
    private String contents;
    private String author;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
}
