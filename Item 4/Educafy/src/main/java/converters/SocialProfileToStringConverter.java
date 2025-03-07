
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.SocialProfile;

@Component
@Transactional
public class SocialProfileToStringConverter implements Converter<SocialProfile, String> {

	@Override
	public String convert(final SocialProfile socialProfile) {

		String result;

		if (socialProfile == null)
			result = null;
		else
			result = String.valueOf(socialProfile.getId());

		return result;

	}

}
