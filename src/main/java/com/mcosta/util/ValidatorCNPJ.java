package com.mcosta.util;


public class ValidatorCNPJ {
	
	  public boolean isValido(String cnpj) {
		  return (validacaoCnpj(cnpj) != null);
	  }
	  
	  public boolean isNotValido(String cnpj) {
		  return (!isValido(cnpj));
	  }
	
	  public String validacaoCnpj(String cnpj) {
		  
		  cnpj = cnpj.replaceAll("-","").replaceAll("/","").replaceAll("\\.","").replaceAll(" ", "");
		  
		  if ((cnpj.length() != 14))
			  return null;
		  
		  if (verificarCnpjNaoValido(cnpj))			  
			  return null;
		  
	      char digito13 = calculaPrimeiroValidadorDoCnpj(cnpj);
	      char digito14 = calculaSegundoValidadorDoCnpj(cnpj);
	      
	      if ((digito13 == cnpj.charAt(12)) && (digito14 == cnpj.charAt(13))) 
	    	  return aplicaMascaraNoCnpj(cnpj);
	       else 
	    	  return null;
	  }

	private String aplicaMascaraNoCnpj(String cnpj) {
		return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." +
			      cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" +
			      cnpj.substring(12, 14);
	}

	private char calculaSegundoValidadorDoCnpj(String cnpj) {
		char digito14;
		int num;
		int resto;
		int soma = 0;
		int peso = 2;
		for (int i = 12; i >= 0; i--) {
	        num = (int)(cnpj.charAt(i) - 48);
	        soma += (num * peso);
	        peso += 1;
	        if (peso == 10)
	           peso = 2;
		  }
	      
	      resto = soma % 11;
	      if ((resto == 0) || (resto == 1))
	         digito14 = '0';
	      else 
	    	 digito14 = (char) ((11 - resto) + 48);
		return digito14;
	}

	private char calculaPrimeiroValidadorDoCnpj(String cnpj) {
		char digito13;
		int num;
		int resto;
		int soma = 0;
		int peso = 2;
		for (int i = 11; i >= 0; i--) {
	        num = (int)(cnpj.charAt(i) - 48);
	        soma += (num * peso);
	        peso += 1;
	        if (peso == 10)
	           peso = 2;
	      }
	      
	      resto = soma % 11;
	      if ((resto == 0) || (resto == 1))
	    	  digito13 = '0';
	      else 
	    	  digito13 = (char) ((11 - resto) + 48);
		return digito13;
	}

	private boolean verificarCnpjNaoValido(String cnpj) {
		return cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
			  cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
			  cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
			  cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
			  cnpj.equals("88888888888888") || cnpj.equals("99999999999999");
	}
}
