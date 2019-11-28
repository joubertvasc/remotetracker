package jv.android.utils.validations;

import java.util.InputMismatchException;
import java.util.Random;

public class CNPJ {

	public static boolean isCNPJ(String CNPJ) {
		CNPJ = CNPJ.replaceAll("[.-]","");

		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
				CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
				CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
				CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
				CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
				(CNPJ.length() != 14))
			return(false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i=11; i>=0; i--) {
				// converte o enésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = (int)(CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else dig13 = (char)((11-r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i=12; i>=0; i--) {
				num = (int)(CNPJ.charAt(i)- 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else dig14 = (char)((11-r) + 48);

			// Verifica se os dígitos calculados conferem com os dígitos informados.
			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return(true);
			else return(false);
		} catch (InputMismatchException erro) {
			return(false);
		}
	}

	public static String formatCNPJ(String CNPJ) {
		// máscara do CNPJ: 99.999.999.9999-99
		return(CNPJ.substring(0, 2) + "." + CNPJ.substring(2, 5) + "." +
				CNPJ.substring(5, 8) + "." + CNPJ.substring(8, 12) + "-" +
				CNPJ.substring(12, 14));
	}

	public String geraCNPJ() throws Exception {
		int digito1 = 0, digito2 = 0, resto = 0;
		String  nDigResult;
		String numerosContatenados;
		String numeroGerado;
		Random numeroAleatorio = new Random();

		//numeros gerados
		int n1 = numeroAleatorio.nextInt(10);
		int n2 = numeroAleatorio.nextInt(10);
		int n3 = numeroAleatorio.nextInt(10);
		int n4 = numeroAleatorio.nextInt(10);
		int n5 = numeroAleatorio.nextInt(10);
		int n6 = numeroAleatorio.nextInt(10);
		int n7 = numeroAleatorio.nextInt(10);
		int n8 = numeroAleatorio.nextInt(10);
		int n9 = numeroAleatorio.nextInt(10);
		int n10 = numeroAleatorio.nextInt(10); 
		int n11 = numeroAleatorio.nextInt(10); 
		int n12 = numeroAleatorio.nextInt(10); 
		
		int soma = n12*2 + n11*3 + n10*4 + n9*5 + n8*6 + n7*7 + n6*8 + n5*9 + n4*2 + n3*3 + n2*4 + n1*5;
		int valor = (soma / 11)*11;
		
		digito1 = soma-valor;
		
		//Primeiro resto da divisão por 11.
		resto = (digito1 % 11);
		
		if(digito1 < 2) {			
			digito1 = 0;
		} else {
			digito1 = 11-resto;
		}
		
		int soma2 =  digito1*2 + n12*3 + n11*4 + n10*5 + n9*6 + n8*7 + n7*8 + n6*9 + n5*2 + n4*3 + n3*4 + n2*5 + n1*6 ;
		int valor2 = (soma2 / 11)*11;
		
		digito2 = soma2-valor2;
		
		//Primeiro resto da divisão por 11.
		resto = (digito2 % 11);
		
		if(digito2 < 2) {
			digito2 = 0;
		} else {
			digito2 = 11-resto;
		}
		
		//Conctenando os numeros
		numerosContatenados = String.valueOf(n1) + String.valueOf(n2) + String.valueOf(n3) + String.valueOf(n4) +
				String.valueOf(n5) + String.valueOf(n6) + String.valueOf(n7) +String.valueOf(n8)+
				String.valueOf(n9) + String.valueOf(n10) + String.valueOf(n11) +
				String.valueOf(n12);
		
		//Concatenando o primeiro resto com o segundo.
		nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
		numeroGerado = numerosContatenados+nDigResult;

		return numeroGerado;
	}
}
