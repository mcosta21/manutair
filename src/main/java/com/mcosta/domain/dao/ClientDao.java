package com.mcosta.domain.dao;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mcosta.domain.model.Client;
import com.mcosta.domain.model.Legal;
import com.mcosta.domain.model.Physical;
import com.mcosta.domain.validator.ClientValidator;
import com.thoughtworks.xstream.XStream;

public class ClientDao implements Dao {
    
    private static Set<Physical> physicals = new HashSet<Physical>();
    private static Set<Legal> legals = new HashSet<Legal>();

    private static String FILE_NAME_PHYSICAL = "database/clients-physical.xml";
    private static String FILE_NAME_LEGAL = "database/clients-legal.xml";
    private ClientValidator clientValidator = new ClientValidator();

    public ClientDao() {
        getLegals();
        getPhysicals();
    }

    @Override
    public void create(Object object) throws Exception {
        if(object instanceof Physical){
            Physical physical = (Physical) object;
            createPhysicalClient(physical);
        }
        if(object instanceof Legal) {
            Legal client = (Legal) object;
            createLegalClient(client);
        }

    }
    
    @Override
    public void update(Object object) throws Exception {
        if(object instanceof Physical){
            persist(FILE_NAME_PHYSICAL, physicals);
        }
        if(object instanceof Legal) {
            persist(FILE_NAME_LEGAL, legals);
        }
    }

    @Override
    public void delete(Object object) throws Exception{
        if(object instanceof Physical){
            physicals.remove(object);
            persist(FILE_NAME_PHYSICAL, physicals);
        }
        if(object instanceof Legal) {
            legals.remove(object);
            persist(FILE_NAME_LEGAL, legals);
        }
    }

    public List<Physical> getPhysicals() {
        File file = new File(FILE_NAME_PHYSICAL);
        if(!file.exists()) return new ArrayList<Physical>();
        XStream xs = new XStream();
        physicals = (Set<Physical>) xs.fromXML(file);
        List<Physical> items = new ArrayList<>();
        for(Physical p : physicals){
            items.add(p);
        }
        return items;
    }

    public List<Legal> getLegals() {
        File file = new File(FILE_NAME_LEGAL);
        if(!file.exists()) return new ArrayList<Legal>();
        XStream xs = new XStream();
        legals = (Set<Legal>) xs.fromXML(file);
        List<Legal> items = new ArrayList<>();
        for(Legal l : legals){
            items.add(l);
        }
        return items;
    }

    private void createPhysicalClient(Physical physical) throws Exception{
        if(!clientValidator.isValid(physical)) {
            throw new Exception(clientValidator.getMessage());
        }
        physicals.add(physical);
        persist(FILE_NAME_PHYSICAL, physicals);
    }

    private void createLegalClient(Legal legal) throws Exception{
        if(!clientValidator.isValid(legal)) {
            throw new Exception(clientValidator.getMessage());
        }
        legals.add(legal);
        persist(FILE_NAME_LEGAL, legals);

    }

    private void persist(String fileName, Set list) throws Exception {
        XStream xs = new XStream();
        String xml = xs.toXML(list);
        FileWriter fw = new FileWriter(fileName);
        fw.write(xml);
        fw.close();        
    }

    @Override
    public List<Client> index() {
        List<Client> clients = new ArrayList<>();
        clients.addAll(physicals);
        clients.addAll(legals);
        return clients;
    }
}
