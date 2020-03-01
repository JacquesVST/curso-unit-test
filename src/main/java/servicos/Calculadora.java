package servicos;

import exception.DivisaoPorZeroException;

public class Calculadora {

	public int somar(int a, int b) {
		return a + b;
	}

	public int subtrair(int a, int b) {
		return a - b;
	}

	public int dividir(int a, int b) throws DivisaoPorZeroException {
		if(b == 0) {
			throw new DivisaoPorZeroException();
		}
		return a / b;
	}

	public int multiplicar(int a, int b) {
		return a * b;
	}

}
