
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Reservation extends DomainEntity {

	private String				status;
	private Date				moment;
	private String				explanation;
	private Double				cost;
	private Integer				hoursWeek;

	//Relaciones
	private Student				student;
	private Lesson				lesson;
	private CreditCard			creditCard;
	private Collection<Exam>	exams;


	@Pattern(regexp = "^(PENDING|APPROVED|REVIEWING|REJECTED|FINAL)$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	public String getExplanation() {
		return this.explanation;
	}

	public void setExplanation(final String explanation) {
		this.explanation = explanation;
	}

	@Min(0)
	public Double getCost() {
		return this.cost;
	}

	public void setCost(final Double cost) {
		this.cost = cost;
	}

	@NotNull
	@Range(min = 1, max = 10)
	public Integer getHoursWeek() {
		return this.hoursWeek;
	}

	public void setHoursWeek(final Integer hoursWeek) {
		this.hoursWeek = hoursWeek;
	}

	@ManyToOne(optional = false)
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(final Student student) {
		this.student = student;
	}

	@ManyToOne(optional = false)
	public Lesson getLesson() {
		return this.lesson;
	}

	public void setLesson(final Lesson lesson) {
		this.lesson = lesson;
	}

	@ManyToOne(optional = false)
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@OneToMany
	public Collection<Exam> getExams() {
		return this.exams;
	}

	public void setExams(final Collection<Exam> exams) {
		this.exams = exams;
	}

}
