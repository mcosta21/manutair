package com.mcosta.domain.dao;

import com.mcosta.domain.enumeration.StatusServiceOrderEnum;
import com.mcosta.domain.model.ServiceOrder;
import com.mcosta.domain.validator.ServiceOrderValidator;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceOrderDao implements Dao {

    private static Set<ServiceOrder> serviceOrders = new HashSet<ServiceOrder>();
    private static String FILE_NAME = "database/service-orders.xml";
    private ServiceOrderValidator serviceOrderValidator = new ServiceOrderValidator();

    public ServiceOrderDao() {
        index();
    }

    @Override
    public void create(Object object) throws Exception {
        ServiceOrder serviceOrder = (ServiceOrder) object;
        if(!serviceOrderValidator.isValid(serviceOrder)) {
            throw new Exception(serviceOrderValidator.getMessage());
        }
        serviceOrder.setIdServiceOrder(Long.valueOf(index().size()+1));
        serviceOrders.add(serviceOrder);
        persist();

    }
    
    @Override
    public void update(Object object) throws Exception {
        ServiceOrder _serviceOrder = (ServiceOrder) object;
        serviceOrders.remove(_serviceOrder);
        serviceOrders.add(_serviceOrder);
        persist();
    }

    @Override
    public void delete(Object object) throws Exception{
    }

    @Override
    public List<ServiceOrder> index() {
        File file = new File(FILE_NAME);

        if(!file.exists()) return new ArrayList<ServiceOrder>();

        XStream xs = new XStream();
        serviceOrders = (Set<ServiceOrder>) xs.fromXML(file);

        List<ServiceOrder> items = new ArrayList<>();
        for(ServiceOrder c : serviceOrders){
            items.add(c);
        }

        return items;
    }

    public List<ServiceOrder> getServiceOrdersByTechnician(Long idUser) {
        File file = new File(FILE_NAME);

        if(!file.exists()) return new ArrayList<ServiceOrder>();

        XStream xs = new XStream();

        List<ServiceOrder> items = new ArrayList<>();
        for(ServiceOrder e : (Set<ServiceOrder>) xs.fromXML(file)){
            if(e.getUser() != null && e.getUser().getIdUser().equals(idUser) && e.getStatusServiceOrderEnum() == StatusServiceOrderEnum.IN_PROGRESS) items.add(e);
        }

        return items;
    }

    public List<ServiceOrder> getServiceOrdersByStatusOpened() {
        File file = new File(FILE_NAME);

        if(!file.exists()) return new ArrayList<ServiceOrder>();

        XStream xs = new XStream();

        List<ServiceOrder> items = new ArrayList<>();
        for(ServiceOrder e : (Set<ServiceOrder>) xs.fromXML(file)){
            if(e.getStatusServiceOrderEnum() == StatusServiceOrderEnum.OPENED) items.add(e);
        }

        return items;
    }

    private void persist() throws Exception {
        XStream xs = new XStream();
        String xml = xs.toXML(serviceOrders);
        FileWriter fw = new FileWriter(FILE_NAME);
        fw.write(xml);
        fw.close();        
    }

}
