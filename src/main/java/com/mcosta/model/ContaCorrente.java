package com.mcosta.model;

import java.util.ArrayList;
import java.util.List;

public class ContaCorrente {
    private Integer numero;
    private Double saldo;
    private Double limite;

    private List<Lancamento> lancamentos;

    private static Integer proximoNumero = 1;

    public ContaCorrente() {
        this.numero = proximoNumero;
        proximoNumero++;
        this.saldo = 0.0;
        this.limite = 0.0;
        lancamentos = new ArrayList<Lancamento>();
    }

    public ContaCorrente(Double limite) {
        this.numero = proximoNumero;
        proximoNumero++;
        this.saldo = 0.0;
        this.limite = limite;
        lancamentos = new ArrayList<Lancamento>();
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Double getSaldo() {
        return this.saldo;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public Double getLimite() {
        return this.limite;
    }

    public Double getDisponivel() {
        return this.saldo + this.limite;
    }

    public void depositar(Double valor) {
        this.saldo+=valor;
        Lancamento lancamento = new Lancamento(TipoLancamento.deposito, valor);
        this.lancamentos.add(lancamento);
    }

    public void sacar(Double valor) {
        if (valor > getDisponivel()) {
            throw new IllegalArgumentException("Saldo Insuficiente");
        }
        this.saldo-=valor;
        Lancamento lancamento = new Lancamento(TipoLancamento.saque, valor);
        this.lancamentos.add(lancamento);
    }

    public List<Lancamento> getLancamentos() {
        return this.lancamentos;
    }

    public String toString() {
        return numero.toString();
    }
}
