package jpa_basic.demo12.answer;

import jakarta.persistence.*;
import jpa_basic.demo12.question.Question;
import jpa_basic.demo12.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDateTime createDate;

	@ManyToOne
	private Question question;
	@ManyToOne
	private SiteUser author;

	private LocalDateTime modifyDate;//수정날짜


	@ManyToMany
	Set<SiteUser> voter;
}
