package servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calculadoraMock;
	
	@Spy
	private Calculadora calculadoraSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void deveMostrarDiferencaEntreMockESpy() {
		Mockito.when(calculadoraMock.somar(1, 5)).thenReturn(8);
		Mockito.when(calculadoraSpy.somar(1, 5)).thenReturn(8);

	}
	

	@Test
	public void teste() {
		Calculadora calculadora = Mockito.mock(Calculadora.class);

		ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);

		Mockito.when(calculadora.somar(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(99);

		Assert.assertEquals(99, calculadora.somar(123, 5));

		// System.out.println(argumentCaptor.getAllValues());
	}
}
