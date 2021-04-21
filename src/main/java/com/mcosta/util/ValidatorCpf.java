package com.mcosta.util;

public class ValidatorCpf {
	
	 public boolean isValido(String cpf) {
		 
		 cpf = limparCpf(cpf);
		 
		 if (verificarCpfNaoValido(cpf)) {
			 return false;
		 }
          
        char digito10 = calculaPrimeiroDigidoValidador(cpf);
        char digito11 = calculaSegundoDigitoValidador(cpf);
          
	    if ((digito10 == cpf.charAt(9)) && (digito11 == cpf.charAt(10))) {
	    	return true;
	    }
	    else {
	    	return false;
	    }
    }

	private String limparCpf(String cpf) {
		return cpf.replaceAll("-","").replaceAll("/","").replaceAll("\\.","").replaceAll(" ", "");
	}
	 
	 public boolean isNotValido(String cpf) {
		 return (!isValido(cpf));
	 }

	private char calculaSegundoDigitoValidador(String cpf) {
		char digito11;
		int soma = 0;
		int numero;
		int peso = 11;
        for(int i = 0; i < 10; i++) {
	        numero = (int)(cpf.charAt(i) - 48);
	        soma = soma + (numero * peso);
	        peso = peso - 1;
        }
      
        int resto = 11 - (soma % 11);
        if ((resto == 10) || (resto == 11)) {
        	digito11 = '0';
        }
        else {
        	digito11 = (char)(resto + 48);
        }
        
		return digito11;
	}

	private char calculaPrimeiroDigidoValidador(String cpf) {
		char digito10;
		int soma = 0;
		int numero;
        int peso = 10;
        
        for (int i = 0; i < 9; i++) {              
            numero = (int)(cpf.charAt(i) - 48); 
            soma = soma + (numero * peso);
            peso = peso - 1;
        }
      
        int resto = 11 - (soma % 11);
        
        if ((resto == 10) || (resto == 11)) {
        	 digito10 = '0';
        }           
        else {
        	digito10 = (char)(resto + 48); // converte no respectivo caractere numerico
        }
        
		return digito10;
	}

	private boolean verificarCpfNaoValido(String cpf) {
		return cpf.equals("00000000000") ||
            cpf.equals("11111111111") ||
            cpf.equals("22222222222") || cpf.equals("33333333333") ||
            cpf.equals("44444444444") || cpf.equals("55555555555") ||
            cpf.equals("66666666666") || cpf.equals("77777777777") ||
            cpf.equals("88888888888") || cpf.equals("99999999999") ||
            (cpf.length() != 11);
	}
	          
    public String imprimeCPF(String cpf) {
        return(cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
        cpf.substring(6, 9) + "-" + cpf.substring(9, 11));
    }
	        

}
