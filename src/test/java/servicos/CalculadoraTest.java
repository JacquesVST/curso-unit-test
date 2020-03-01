package servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import exception.DivisaoPorZeroException;

//@RunWith(ParallelRunner.class)
public class CalculadoraTest {

	private Calculadora calculadora;

	@Before
	public void setup() {
		calculadora = new Calculadora();
		// System.out.println("Iniciando...");
	}

	@Test
	public void deveSomarDoisValores() {
		int a = 5;
		int b = 3;

		int resulttado = calculadora.somar(a, b);

		Assert.assertEquals(8, resulttado);
	}

	@Test
	public void deveSubtrairDoisValores() {
		int a = 8;
		int b = 5;

		int resulttado = calculadora.subtrair(a, b);

		Assert.assertEquals(3, resulttado);
	}

	@Test
	public void deveDividirDoisValores() throws DivisaoPorZeroException {
		int a = 6;
		int b = 3;

		int resulttado = calculadora.dividir(a, b);

		Assert.assertEquals(2, resulttado);
	}

	@Test(expected = DivisaoPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws DivisaoPorZeroException {
		int a = 10;
		int b = 0;

		int resulttado = calculadora.dividir(a, b);

		Assert.assertEquals(2, resulttado);
	}

	@Test
	public void deveMultiplicarDoisValores() {
		int a = 4;
		int b = 2;

		int resulttado = calculadora.multiplicar(a, b);

		Assert.assertEquals(8, resulttado);
	}
}
