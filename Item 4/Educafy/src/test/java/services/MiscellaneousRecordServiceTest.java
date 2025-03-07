
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Curriculum;
import domain.MiscellaneousRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private CurriculumService			curriculumService;


	@Test
	public void driverCreateSave() {
		final Collection<String> attachments = new ArrayList<String>();
		attachments.add("http://www.attachment1.com");
		attachments.add("http://www.attachment2.com");
		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Teacher crea MiscellaneousRecord 
				//			C: % Recorre 20 de las 20 (7+13) lineas posibles
				//			D: cobertura de datos=4/16
				"teacher1", "Disfruto ense�ando", attachments, null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Teacher crea MiscellaneousRecord 
				//			C: % Recorre 20 de las 20 (7+13) lineas posibles
				//			D: cobertura de datos=4/16
				"teacher1", "Escribo varios blogs", attachments, null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Un teacher intenta crear una MiscellaneousRecord con el texto vac�o
				//			C: % Recorre 19 de las 20 (7+13) lineas posibles
				//			D: cobertura de datos=4/16
				"teacher1", "", attachments, javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Teacher intenta crear MiscellaneousRecord sin archivos adjuntos
				//			C: % Recorre 19 de las 20 (7+13) lineas posibles
				//			D: cobertura de datos=4/16
				"teacher1", "freeText1", null, javax.validation.ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateCreateSave((String) testingRecord[i][0], (String) testingRecord[i][1], (Collection<String>) testingRecord[i][2], (Class<?>) testingRecord[i][3]);
	}

	protected void templateCreateSave(final String user, final String freeText, final Collection<String> attachments, final Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(user);
			final Curriculum curriculum = this.curriculumService.findOne(this.getEntityId("curriculum1"));

			final MiscellaneousRecord lRec = this.miscellaneousRecordService.create();
			lRec.setFreeText(freeText);
			if (attachments != null)
				lRec.setAttachments(attachments);
			final MiscellaneousRecord lRecSaved = this.miscellaneousRecordService.save(lRec, curriculum.getId());
			Assert.isTrue(lRecSaved.getId() != 0);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(oops.getMessage());
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Collection<String> attachments = new ArrayList<String>();
		attachments.add("http://www.attachment1.com");
		attachments.add("http://www.attachment2.com");
		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Teacher edita MiscellaneousRecord 
				//			C: % Recorre 10 de las 10 lineas posibles
				//			D: cobertura de datos=4/16
				"teacher1", "freeText1", attachments, "miscellaneousRecord1", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Teacher edita MiscellaneousRecord 
				//			C: % Recorre 10 de las 10 lineas posibles
				//			D: cobertura de datos=4/16
				"teacher3", "freeText1", attachments, "miscellaneousRecord4", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Un teacher intenta editar una MiscellaneousRecord con el texto vac�o
				//			C: % Recorre 9 de las 10 lineas posibles
				//			D: cobertura de datos=4/16
				"teacher1", "", attachments, "miscellaneousRecord1", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Un teacher intenta editar una MiscellaneousRecord que no est� ne modo draft
				//			C: % Recorre 5 de las 10 lineas posibles
				//			D: cobertura de datos=4/16
				"teacher1", "freeText1", attachments, "miscellaneousRecord2", javax.validation.ConstraintViolationException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Un teacher intenta editar una MiscellaneousRecord de otro teacher aunque est� en draft mode
				//			C: % Recorre 6 de las 10 lineas posibles
				//			D: cobertura de datos=4/16
				"teacher2", "freeText1", attachments, "miscellaneousRecord1", javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateEdit((String) testingRecord[i][0], (String) testingRecord[i][1], (Collection<String>) testingRecord[i][2], (String) testingRecord[i][3], (Class<?>) testingRecord[i][4]);
	}

	private void templateEdit(final String user, final String freeText, final Collection<String> attachments, final String miscellaneousRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(user);
			final MiscellaneousRecord lR = this.miscellaneousRecordService.findOne(this.getEntityId(miscellaneousRecord));
			final Curriculum curriculum = this.curriculumService.findCurriculumByMiscellaneousRecord(lR.getId());
			lR.setFreeText(freeText);
			if (attachments != null)
				lR.setAttachments(attachments);
			this.miscellaneousRecordService.save(lR, curriculum.getId());
			this.miscellaneousRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverDelete() {

		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Teacher borra MiscellaneousRecord 
				//			C: % Recorre 15 de las 15 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "miscellaneousRecord1", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Teacher borra MiscellaneousRecord 
				//			C: % Recorre 15 de las 15 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher3", "miscellaneousRecord4", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Teacher intenta borrar MiscellaneousRecord que no est� en draft mode
				//			C: % Recorre 7 de las 15 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "miscellaneousRecord2", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Teacher intena borrar miscellaneous Record que no es suyo, aun estando en modo draft
				//			C: % Recorre 3 de las 15 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher2", "miscellaneousRecord1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateDelete((String) testingRecord[i][0], (String) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}

	private void templateDelete(final String actor, final String miscellaneousRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final MiscellaneousRecord lRec = this.miscellaneousRecordService.findOne(this.getEntityId(miscellaneousRecord));
			this.miscellaneousRecordService.delete(lRec);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverToFinal() {

		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Teacher pasa a final MiscellaneousRecord en draft mode
				//			C: % Recorre 8 de las 8 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "miscellaneousRecord1", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Teacher pasa a final MiscellaneousRecord en draft mode
				//			C: % Recorre 8 de las 8 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher3", "miscellaneousRecord4", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Teacher intenar pasar a final MiscellaneousRecord que ya ets� en final mode
				//			C: % Recorre 5 de las 8 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "miscellaneousRecord2", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Teacher intenar pasar a final MiscellaneousRecord que ya ets� en final mode
				//			C: % Recorre 5 de las 8 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher13", "miscellaneousRecord5", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateToFinal((String) testingRecord[i][0], (String) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}

	private void templateToFinal(final String actor, final String miscellaneousRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final MiscellaneousRecord lRec = this.miscellaneousRecordService.findOne(this.getEntityId(miscellaneousRecord));
			this.miscellaneousRecordService.toFinal(lRec);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

	@Test
	public void driverCertify() {

		final Object testingRecord[][] = {
			{
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Un certificador certifica un registro en modo final
				//			C: 100% Recorre 7 de las 7 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "miscellaneousRecord2", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Positivo: Un certificador certifica un registro en modo final
				//			C: 100% Recorre 7 de las 7 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "miscellaneousRecord5", null
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Teacher no puede certificar un registro
				//			C: 100% Recorre 1 de las 7 lineas posibles
				//			D: cobertura de datos=1/3
				"teacher1", "miscellaneousRecord2", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Un certificador intenta certificar un registro en modo draft
				//			C: 100% Recorre 4 de las 7 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "miscellaneousRecord1", IllegalArgumentException.class
			}, {
				//			A: Educafy Req. 20.8 -> Los profesores pueden administrar su curr�cilum...
				//			B: Test Negativo: Un certificador intenta certificar un registro que ya est� certificado
				//			C: 100% Recorre 5 de las 7 lineas posibles
				//			D: cobertura de datos=1/3
				"certifier1", "miscellaneousRecord1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingRecord.length; i++)
			this.templateCertify((String) testingRecord[i][0], (String) testingRecord[i][1], (Class<?>) testingRecord[i][2]);
	}

	private void templateCertify(final String actor, final String miscellaneousRecord, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.authenticate(actor);
			final MiscellaneousRecord lRec = this.miscellaneousRecordService.findOne(this.getEntityId(miscellaneousRecord));
			this.miscellaneousRecordService.certify(lRec);
			this.miscellaneousRecordService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}
}
