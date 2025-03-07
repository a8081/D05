
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.PersonalRecordRepository;
import domain.PersonalRecord;

@Component
@Transactional
public class StringToPersonalRecordConverter implements Converter<String, PersonalRecord> {

	@Autowired
	private PersonalRecordRepository	personalRecordRepository;


	@Override
	public PersonalRecord convert(final String text) {

		final PersonalRecord result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.personalRecordRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
