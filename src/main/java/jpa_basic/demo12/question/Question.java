package jpa_basic.demo12.question;

import jakarta.persistence.*;
import jpa_basic.demo12.answer.Answer;
import jpa_basic.demo12.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 200)
	private String subject;

	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDateTime createDate;

	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;

	@ManyToOne
	private SiteUser author;

	private LocalDateTime modifyDate;//수정날짜

	@ManyToMany
	Set<SiteUser> voter;
}
