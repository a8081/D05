
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Student;

@Component
@Transactional
public class StudentToStringConverter implements Converter<Student, String> {

	@Override
	public String convert(final Student student) {

		String result;

		if (student == null)
			result = null;
		else
			result = String.valueOf(student.getId());

		return result;

	}

}
