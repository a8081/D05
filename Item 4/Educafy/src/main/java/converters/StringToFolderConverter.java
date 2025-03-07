
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.FolderRepository;
import domain.Folder;

@Component
@Transactional
public class StringToFolderConverter implements Converter<String, Folder> {

	@Autowired
	private FolderRepository	folderRepository;


	@Override
	public Folder convert(final String text) {

		final Folder result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.folderRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
