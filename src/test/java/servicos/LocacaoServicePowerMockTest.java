package servicos;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import builders.FilmeBuilder;
import builders.UsuarioBuilder;
import daos.LocacaoDAO;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import matchers.MatchersProprios;
import utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocacaoService.class, DataUtils.class })
public class LocacaoServicePowerMockTest {

	@InjectMocks
	private LocacaoService locacaoService;

	@Mock
	private LocacaoDAO locacaoDAO;

	@Mock
	private SPCService spcService;

	@Mock
	private EmailService emailService;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		locacaoService = PowerMockito.spy(locacaoService);
	}

	//@Test
	public void deveAlugarFilme() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5d).agora());

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obeterData(28, 4, 2017));

		Locacao locacao = locacaoService.alugarFilmes(usuario, filmes);

		error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5d)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obeterData(28, 4, 2017)),
				CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obeterData(29, 4, 2017)),
				CoreMatchers.is(true));
	}

	//@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {

		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 23);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2017);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

		Locacao resultado = locacaoService.alugarFilmes(usuario, filmes);

		Assert.assertThat(resultado.getDataRetorno(), MatchersProprios.caiNumaSegunda());

		PowerMockito.verifyStatic(Mockito.atLeastOnce());
		Calendar.getInstance();

	}

	@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		PowerMockito.doReturn(1d).when(locacaoService, "calcularValorLocacao", filmes);

		locacaoService.alugarFilmes(usuario, filmes);

		PowerMockito.verifyPrivate(locacaoService).invoke("calcularValorLocacao", filmes);
	}

	@Test
	public void deveCalcularValorLocacao() throws Exception {
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Double valor = (Double) Whitebox.invokeMethod(locacaoService, "calcularValorLocacao", filmes);

		Assert.assertThat(valor, CoreMatchers.is(10d));
	}
}
