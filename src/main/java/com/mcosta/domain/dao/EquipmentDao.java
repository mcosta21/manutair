package com.mcosta.domain.dao;

import com.mcosta.domain.model.Equipment;
import com.mcosta.domain.validator.EquipmentValidator;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EquipmentDao implements Dao {

    private static Set<Equipment> equipments = new HashSet<Equipment>();
    private static String FILE_NAME = "database/equipments.xml";
    private EquipmentValidator equipmentValidator = new EquipmentValidator();

    public EquipmentDao() {
        index();
    }

    @Override
    public void create(Object object) throws Exception {
        Equipment equipment = (Equipment) object;
        if(!equipmentValidator.isValid(equipment)) {
            throw new Exception(equipmentValidator.getMessage());
        }
        equipment.setIdEquipment(Long.valueOf(index().size()+1));
        equipments.add(equipment);
        persist();

    }
    
    @Override
    public void update(Object object) throws Exception {
        persist();
    }

    @Override
    public void delete(Object object) throws Exception{
        Equipment equipment = (Equipment) object;
        equipment.setIsDeleted(true);
        persist();
    }

    @Override
    public List<Equipment> index() {
        File file = new File(FILE_NAME);

        if(!file.exists()) return new ArrayList<Equipment>();

        XStream xs = new XStream();
        equipments = (Set<Equipment>) xs.fromXML(file);

        List<Equipment> items = new ArrayList<>();
        for(Equipment e : equipments){
            if(!e.getIsDeleted()) items.add(e);
        }

        return items;
    }

    public List<Equipment> index(Long idContract) {
        File file = new File(FILE_NAME);

        if(!file.exists()) return new ArrayList<Equipment>();

        XStream xs = new XStream();

        List<Equipment> items = new ArrayList<>();
        for(Equipment e : (Set<Equipment>) xs.fromXML(file)){
            if(!e.getIsDeleted() && e.getContract().getIdContract().equals(idContract)) items.add(e);
        }

        return items;
    }

    private void persist() throws Exception {
        XStream xs = new XStream();
        String xml = xs.toXML(equipments);
        FileWriter fw = new FileWriter(FILE_NAME);
        fw.write(xml);
        fw.close();        
    }

}
