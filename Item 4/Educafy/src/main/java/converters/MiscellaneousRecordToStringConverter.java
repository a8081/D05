
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.MiscellaneousRecord;

@Component
@Transactional
public class MiscellaneousRecordToStringConverter implements Converter<MiscellaneousRecord, String> {

	@Override
	public String convert(final MiscellaneousRecord miscellaneousRecord) {

		String result;

		if (miscellaneousRecord == null)
			result = null;
		else
			result = String.valueOf(miscellaneousRecord.getId());

		return result;

	}

}
