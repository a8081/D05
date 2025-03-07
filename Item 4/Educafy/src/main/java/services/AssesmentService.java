
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AssesmentRepository;
import domain.Assesment;
import domain.Comment;
import domain.Lesson;
import domain.Student;
import domain.Teacher;
import forms.AssesmentForm;

@Service
@Transactional
public class AssesmentService {

	@Autowired
	private AssesmentRepository	assesmentRepository;

	@Autowired
	private StudentService		studentService;

	@Autowired
	private TeacherService		teacherService;

	@Autowired
	private LessonService		lessonService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private Validator			validator;


	public Assesment create() {
		final Assesment assesment = new Assesment();
		return assesment;
	}

	public Assesment findOne(final int assesmentId) {
		Assert.isTrue(assesmentId != 0);
		final Assesment result = this.assesmentRepository.findOne(assesmentId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Assesment> findAll() {
		Collection<Assesment> res = new ArrayList<>();
		res = this.assesmentRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Assesment save(final Assesment assesment, final int lessonId) {
		Assert.notNull(assesment);
		Assert.isTrue(lessonId != 0);
		final Student principal = this.studentService.findByPrincipal();
		final Assesment result;
		final Collection<Lesson> lessons = this.lessonService.findAllByStudent();
		final Lesson lesson = this.lessonService.findOne(lessonId);
		Assert.isTrue(lessons.contains(lesson));

		if (assesment.getId() == 0) {
			assesment.setStudent(principal);
			assesment.setLesson(lesson);
		}
		result = this.assesmentRepository.save(assesment);
		return result;

	}
	public void delete(final Assesment assesment) {
		Assert.notNull(assesment);
		Assert.isTrue(assesment.getId() != 0);
		final Assesment retrieved = this.findOne(assesment.getId());
		final Student principal = this.studentService.findByPrincipal();
		Assert.isTrue(retrieved.getStudent().equals(principal));
		final List<Comment> comments = (List<Comment>) this.commentService.findAllCommentsByAssesment(assesment.getId());
		this.commentService.deleteInBatch(comments);
		this.assesmentRepository.delete(retrieved);
	}
	/* ========================= OTHER METHODS =========================== */

	public Collection<Assesment> findAllByStudentPrincipal() {
		Collection<Assesment> res = new ArrayList<>();
		final Student principal = this.studentService.findByPrincipal();
		res = this.assesmentRepository.findAllAssesmentByStudentId(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Assesment> findAllByTeacherPrincipal() {
		Collection<Assesment> res = new ArrayList<>();
		final Teacher principal = this.teacherService.findByPrincipal();
		res = this.assesmentRepository.findAllAssesmentByTeacher(principal.getUserAccount().getId());
		Assert.notNull(res);
		return res;
	}

	public Collection<Assesment> findAllAssesmentByTeacher(final int teacherId) {
		Collection<Assesment> res = new ArrayList<>();
		final Teacher teacher = this.teacherService.findOne(teacherId);
		res = this.assesmentRepository.findAllAssesmentByTeacher(teacher.getUserAccount().getId());
		return res;
	}

	public Collection<Assesment> findAllAssesmentByLesson(final int lessonId) {
		Collection<Assesment> res = new ArrayList<>();
		res = this.assesmentRepository.findAllAssesmentByLesson(lessonId);
		return res;
	}

	public void deleteInBatch(final Collection<Assesment> assesments) {
		this.assesmentRepository.deleteInBatch(assesments);
	}

	public Assesment reconstruct(final AssesmentForm assesmentForm, final BindingResult binding) {
		Assesment result;

		if (assesmentForm.getId() == 0)
			result = this.create();
		else
			result = this.findOne(assesmentForm.getId());

		result.setId(assesmentForm.getId());
		result.setVersion(assesmentForm.getVersion());
		result.setScore(assesmentForm.getScore());
		result.setComment(assesmentForm.getComment());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public void flush() {
		this.assesmentRepository.flush();
	}
}
