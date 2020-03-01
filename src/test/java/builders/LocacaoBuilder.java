package builders;

import java.util.Arrays;
import java.util.Date;

import entidades.Locacao;
import entidades.Usuario;
import utils.DataUtils;

public class LocacaoBuilder {

	private Locacao locacao;

	private LocacaoBuilder() {

	}

	public static LocacaoBuilder umaLocacao() {
		LocacaoBuilder locacaoBuilder = new LocacaoBuilder();
		locacaoBuilder.locacao = new Locacao();
		locacaoBuilder.locacao.setUsuario(UsuarioBuilder.umUsuario().agora());
		locacaoBuilder.locacao.setFilmes(Arrays.asList(FilmeBuilder.umFilme().agora()));
		locacaoBuilder.locacao.setDataLocacao(new Date());
		locacaoBuilder.locacao.setDataRetorno(DataUtils.adicionarDias(new Date(), 1));
		locacaoBuilder.locacao.setValor(10d);
		return locacaoBuilder;
	}
	
	public LocacaoBuilder comUsuario(Usuario usuario) {
		locacao.setUsuario(usuario);
		return this;
	}
	
	public LocacaoBuilder comDataRetorno(Date data) {
		locacao.setDataRetorno(data);
		return this;
	}
	
	public LocacaoBuilder atrasado() {
		locacao.setDataLocacao(DataUtils.obeterDataComDiferenca(-4));
		locacao.setDataRetorno(DataUtils.obeterDataComDiferenca(-2));
		return this;
	}

	public Locacao agora() {
		return locacao;
	}
}
