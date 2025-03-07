
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Reservation;

@Component
@Transactional
public class ReservationToStringConverter implements Converter<Reservation, String> {

	@Override
	public String convert(final Reservation reservation) {

		String result;

		if (reservation == null)
			result = null;
		else
			result = String.valueOf(reservation.getId());

		return result;

	}

}
