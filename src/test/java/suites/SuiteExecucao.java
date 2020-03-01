package suites;

import org.junit.runners.Suite.SuiteClasses;

import servicos.CalculoValorLocacaoTest;
import servicos.LocacaoServiceTest;

//@RunWith(Suite.class)
@SuiteClasses({
	//CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class,
	})
public class SuiteExecucao {

}
