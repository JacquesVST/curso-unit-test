package utils;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.util.Calendar;
import java.util.Date;

public class DataUtils {

	public static Date adicionarDias(Date data, int dias) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(DAY_OF_MONTH, dias);
		return calendar.getTime();
	}

	public static Date obeterDataComDiferenca(int dias) {
		return adicionarDias(new Date(), dias);
	}

	public static Date obeterData(int dia, int mes, int ano) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(DAY_OF_MONTH, dia);
		calendar.set(MONTH, mes);
		calendar.set(YEAR, ano);
		return calendar.getTime();
	}

	public static boolean isMesmaData(Date data1, Date data2) {
		Calendar calendario1 = Calendar.getInstance();
		Calendar calendario2 = Calendar.getInstance();
		calendario1.setTime(data1);
		calendario2.setTime(data2);
		boolean isMesmaData = calendario1.get(DAY_OF_YEAR) == calendario2.get(DAY_OF_YEAR)
				&& calendario1.get(YEAR) == calendario2.get(YEAR);
		return isMesmaData;
	}
	
	public static boolean validarDiaSemana(Date data, Integer dia) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		boolean isDiaSemana = dia == calendario.get(Calendar.DAY_OF_WEEK);
		return isDiaSemana;
	}
	

}
