package servicos;

import entidades.Usuario;

public interface EmailService {
	public void notificarAtraso(Usuario usuario);
}
