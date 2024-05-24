package jpa_basic.demo12.answer;

import jakarta.validation.Valid;
import jpa_basic.demo12.question.Question;
import jpa_basic.demo12.question.QuestionService;
import jpa_basic.demo12.user.SiteUser;
import jpa_basic.demo12.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id,
							   @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal)
			/*@RequestParam(value = "content") String content)*/ {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if(bindingResult.hasErrors()){
			model.addAttribute("question", question);
			return "question_detail";
		}
		Answer answer = this.answerService.create(question,
				answerForm.getContent(), siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s",
				answer.getQuestion().getId(), answer.getId());
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		answerForm.setContent(answer.getContent());
		return "answer_form";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
							   @PathVariable("id") Integer id, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "answer_form";
		}
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.answerService.modify(answer, answerForm.getContent());
		return String.format("redirect:/question/detail/%s#answer_%s",
				answer.getQuestion().getId(), answer.getId());
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.answerService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerVote(Principal principal, @PathVariable("id") Integer id){
				Answer answer = this.answerService.getAnswer(id);
				SiteUser siteUser = this.userService.getUser(principal.getName());
				this.answerService.vote(answer,siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s",
				answer.getQuestion().getId(), answer.getId());
	}

}
