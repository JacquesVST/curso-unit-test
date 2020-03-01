package servicos;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import daos.LocacaoDAO;
import entidades.Filme;
import entidades.Locacao;
import entidades.Usuario;
import exception.FilmeSemEstoqueException;
import exception.LocadoraException;
import utils.DataUtils;

public class LocacaoService {

	private LocacaoDAO locacaoDAO;
	private SPCService spcService;
	private EmailService emailService;

	public Locacao alugarFilmes(Usuario usuario, List<Filme> filmes)
			throws LocadoraException, FilmeSemEstoqueException {

		if (usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}

		if (filmes == null || filmes.size() == 0) {
			throw new LocadoraException("Filme vazio");
		}

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Filme sem estoque");
			}
		}
		boolean negativado;

		try {
			negativado = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new LocadoraException("Problemas com SPC, tente novamente");
		}

		if (negativado) {
			throw new LocadoraException("Usuario negativado");
		}
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(obterData());
		locacao.setValor(calcularValorLocacao(filmes));

		Date dataEntrega = obterData();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		if (DataUtils.validarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);

		locacaoDAO.salvar(locacao);

		return locacao;
	}

	protected Date obterData() {
		return new Date();
	}

	private Double calcularValorLocacao(List<Filme> filmes) {
		Double precoLocacao = 0d;
		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);

			Double precoFilme = filme.getPrecoLocacao();
			if (i == 2) {
				precoFilme *= 0.75;
			} else if (i == 3) {
				precoFilme *= 0.5;
			} else if (i == 4) {
				precoFilme *= 0.25;
			} else if (i > 4) {
				precoFilme = 0d;
			}
			precoLocacao += precoFilme;
		}
		return precoLocacao;
	}

	public void notificarAtrasos() {
		List<Locacao> locacoes = locacaoDAO.obterLocacoesPendentes();
		for (Locacao locacao : locacoes) {
			if (locacao.getDataRetorno().before(obterData())) {

				emailService.notificarAtraso(locacao.getUsuario());
			}
		}
	}

	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(obterData());
		novaLocacao.setDataRetorno(DataUtils.obeterDataComDiferenca(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		locacaoDAO.salvar(novaLocacao);
	}

}
