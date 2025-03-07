
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import domain.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
public class AssesmentForm extends DomainEntity {

	private int		score;
	private String	comment;


	@Range(min = 0, max = 5)
	@Digits(integer = 1, fraction = 0)
	public Integer getScore() {
		return this.score;
	}

	public void setScore(final int score) {
		this.score = score;
	}

	@NotBlank
	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

}
