package com.mcosta.domain.validator;

import com.mcosta.domain.model.ServiceOrder;
import com.mcosta.domain.model.User;

import java.time.LocalDate;

public class ServiceOrderValidator {

    private String message;

    public boolean isValid(ServiceOrder serviceOrder) {

        if (serviceOrder.getEquipment() == null) {
            message = "Equipamento não informado.";
            return false;
        }

        if (serviceOrder.getEquipmentLocationAddress() == null || serviceOrder.getEquipmentLocationAddress().isEmpty()) {
            message = "Endereço do equipmento não informado.";
            return false;
        }

        if (serviceOrder.getProblemDescription() == null || serviceOrder.getProblemDescription().isEmpty()) {
            message = "Descrição do problema não informado.";
            return false;
        }

        return true;
    }


    public String getMessage() {
        return message;
    }

    public boolean isValidToAllocateTechnician(User technician, LocalDate serviceDate, String hour) {

        if (technician== null) {
            message = "Técnico não informado.";
            return false;
        }

        if (serviceDate == null) {
            message = "Data do serviço não informada.";
            return false;
        }

        if (hour == null || hour.isEmpty()) {
            message = "Horário de atendimento não informada.";
            return false;
        }

        String[] hoursArray = hour.split(":");
        if (hoursArray.length > 2 || Integer.valueOf(hoursArray[0]) > 23 || Integer.valueOf(hoursArray[1]) > 60) {
            message = "Horário informa é inválido.";
            return false;
        }

        return true;
    }

    public boolean isValidToFinishServiceOrder(ServiceOrder serviceOrder) {

        if (serviceOrder.getSolutionDescription() == null || serviceOrder.getSolutionDescription().isEmpty()) {
            message = "Descrição da solução não informada.";
            return false;
        }

        if (serviceOrder.getFinishDate() == null) {
            message = "Data da conclusão não informada.";
            return false;
        }

        return true;
    }
}
