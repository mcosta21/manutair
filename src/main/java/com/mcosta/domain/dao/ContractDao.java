package com.mcosta.domain.dao;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mcosta.domain.model.Contract;
import com.mcosta.domain.validator.ContractValidator;
import com.thoughtworks.xstream.XStream;

public class ContractDao implements Dao {
    
    private static Set<Contract> contracts = new HashSet<Contract>();
    private static String FILE_NAME = "database/contracts.xml";
    private ContractValidator contractValidator = new ContractValidator();

    public ContractDao() {
        index();
    }

    @Override
    public void create(Object object) throws Exception {
        Contract contract = (Contract) object;
        if(!contractValidator.isValid(contract)) {
            throw new Exception(contractValidator.getMessage());
        }

        contracts.add(contract);
        persist();

    }
    
    @Override
    public void update(Object object) throws Exception {
        persist();
    }

    @Override
    public void delete(Object object) throws Exception{
        contracts.remove(object);
        persist();
    }

    @Override
    public List<Contract> index() {
        File file = new File(FILE_NAME);

        if(!file.exists()) return new ArrayList<Contract>();

        XStream xs = new XStream();
        contracts = (Set<Contract>) xs.fromXML(file);

        List<Contract> items = new ArrayList<>();
        for(Contract c : contracts){
            items.add(c);
        }

        return items;
    }

    private void persist() throws Exception {
        XStream xs = new XStream();
        String xml = xs.toXML(contracts);
        FileWriter fw = new FileWriter(FILE_NAME);
        fw.write(xml);
        fw.close();        
    }

}
