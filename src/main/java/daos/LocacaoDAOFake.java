package daos;

import java.util.List;

import entidades.Locacao;

public class LocacaoDAOFake implements LocacaoDAO {

	public void salvar(Locacao locacao) {

	}

	@Override
	public List<Locacao> obterLocacoesPendentes() {
		return null;
	}
}
