package matchers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import utils.DataUtils;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

	private Integer dias;
	
	public DataDiferencaDiasMatcher(Integer dias) {
		this.dias = dias;
	}
	
	public void describeTo(Description description) {
		Date dataEsperada = DataUtils.obeterDataComDiferenca(dias);
		DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		description.appendText(format.format(dataEsperada));
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data, DataUtils.obeterDataComDiferenca(dias));
	}

}
