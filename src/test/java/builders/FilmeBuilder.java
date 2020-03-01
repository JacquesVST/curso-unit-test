package builders;

import entidades.Filme;

public class FilmeBuilder {

	private Filme filme;

	private FilmeBuilder() {

	}

	public static FilmeBuilder umFilme() {
		FilmeBuilder filmeBuilder = new FilmeBuilder();
		filmeBuilder.filme = new Filme();
		filmeBuilder.filme.setNome("Filme 1");
		filmeBuilder.filme.setPrecoLocacao(10d);
		filmeBuilder.filme.setEstoque(1);
		return filmeBuilder;
	}

	public FilmeBuilder semEstoque() {
		filme.setEstoque(0);
		return this;
	}

	public FilmeBuilder comValor(Double valor) {
		filme.setPrecoLocacao(valor);
		return this;
	}

	public Filme agora() {
		return filme;
	}
}
