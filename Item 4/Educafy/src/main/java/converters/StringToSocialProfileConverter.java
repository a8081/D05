
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.SocialProfileRepository;
import domain.SocialProfile;

@Component
@Transactional
public class StringToSocialProfileConverter implements Converter<String, SocialProfile> {

	@Autowired
	private SocialProfileRepository	socialProfileRepository;


	@Override
	public SocialProfile convert(final String text) {

		final SocialProfile result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.socialProfileRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
