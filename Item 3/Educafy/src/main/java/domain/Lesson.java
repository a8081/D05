
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Lesson extends DomainEntity {

	private String	ticker;
	private String	title;
	private String	description;
	private double	price;
	private boolean	isDraft;

	private Teacher	teacher;
	private Subject	subject;


	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^[0-9]{6}[A-Z]{5}$")
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(final double price) {
		this.price = price;
	}

	public boolean isDraft() {
		return this.isDraft;
	}

	public void setDraft(final boolean isDraft) {
		this.isDraft = isDraft;
	}

	@Valid
	@ManyToOne(optional = false)
	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(final Teacher teacher) {
		this.teacher = teacher;
	}

	@Valid
	@ManyToOne(optional = false)
	public Subject getSubject() {
		return this.subject;
	}

	public void setSubject(final Subject subject) {
		this.subject = subject;
	}

}
