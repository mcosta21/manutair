package com.mcosta.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Lancamento {
    private LocalDate dataOcorrencia;
    private LocalTime horaOcorrencia;
    private TipoLancamento tipoLancamento;
    private Double valor;

    public Lancamento(TipoLancamento tipoLancamento, Double valor) {
        dataOcorrencia = LocalDate.now();
        horaOcorrencia = LocalTime.now();
        this.tipoLancamento = tipoLancamento;
        this.valor = valor;
    }

    /**
     * @return LocalDate return the dataOcorrencia
     */
    public LocalDate getDataOcorrencia() {
        return dataOcorrencia;
    }


    /**
     * @return LocalTime return the horaOcorrencia
     */
    public LocalTime getHoraOcorrencia() {
        return horaOcorrencia;
    }


    /**
     * @return TipoLancamento return the tipoLancamento
     */
    public TipoLancamento getTipoLancamento() {
        return tipoLancamento;
    }

    /**
     * @return Double return the valor
     */
    public Double getValor() {
        return valor;
    }

    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter hf = DateTimeFormatter.ofPattern("HH:mm");

        String resp = "Data: " + this.dataOcorrencia.format(df);
        resp+= "\tHora: " + this.getHoraOcorrencia().format(hf);
        resp+="\tTipo: " + this.getTipoLancamento();
        resp+="\tValor: " + this.getValor().toString();

        return resp;
    }


}
