package servicos;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import builders.FilmeBuilder;
import builders.LocacaoBuilder;
import builders.UsuarioBuilder;
import daos.LocacaoDAO;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exception.FilmeSemEstoqueException;
import exception.LocadoraException;
import matchers.MatchersProprios;
import utils.DataUtils;

public class LocacaoServiceTest {

	@InjectMocks
	@Spy
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
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().comValor(5d).agora());

		Mockito.doReturn(DataUtils.obeterData(28, 4, 2017)).when(locacaoService).obterData();

		Locacao locacao = locacaoService.alugarFilmes(usuario, filmes);

		error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5d)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obeterData(28, 4, 2017)),
				CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obeterData(29, 4, 2017)),
				CoreMatchers.is(true));
	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora());

		locacaoService.alugarFilmes(usuario, filmes);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		try {
			locacaoService.alugarFilmes(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuario vazio"));
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		locacaoService.alugarFilmes(usuario, null);
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Mockito.doReturn(DataUtils.obeterData(29, 3, 2017)).when(locacaoService).obterData();

		Locacao resultado = locacaoService.alugarFilmes(usuario, filmes);

		Assert.assertThat(resultado.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}

	@Test
	public void naoDeveAlugarFilmeParaUsuarioNegativado() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		// Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("User 2").agora();

		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Mockito.when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		try {
			locacaoService.alugarFilmes(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuario negativado"));
		}

		Mockito.verify(spcService).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("User 2").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("User 3").agora();
		List<Locacao> locacoes = Arrays.asList(LocacaoBuilder.umaLocacao().comUsuario(usuario).atrasado().agora(),
				LocacaoBuilder.umaLocacao().comUsuario(usuario2).agora(),
				LocacaoBuilder.umaLocacao().comUsuario(usuario3).atrasado().agora(),
				LocacaoBuilder.umaLocacao().comUsuario(usuario3).atrasado().agora());
		Mockito.when(locacaoDAO.obterLocacoesPendentes()).thenReturn(locacoes);

		locacaoService.notificarAtrasos();

		Mockito.verify(emailService, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
		Mockito.verify(emailService).notificarAtraso(usuario);
		Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuario2);
		Mockito.verifyNoMoreInteractions(emailService);
	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Mockito.when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrofica"));

		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");

		locacaoService.alugarFilmes(usuario, filmes);
	}

	@Test
	public void deveProrrogarUmaLocacao() {
		Locacao locacao = LocacaoBuilder.umaLocacao().agora();

		locacaoService.prorrogarLocacao(locacao, 3);

		ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(locacaoDAO).salvar(argumentCaptor.capture());
		Locacao novaLocacao = argumentCaptor.getValue();

		error.checkThat(novaLocacao.getValor(), CoreMatchers.is(30d));
		error.checkThat(novaLocacao.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(novaLocacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(3));
	}

	@Test
	public void deveCalcularValorLocacao() throws Exception {
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Class<LocacaoService> clazz= LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valor = (Double) metodo.invoke(locacaoService, filmes);		
		
		Assert.assertThat(valor, CoreMatchers.is(10d));
	}
}
