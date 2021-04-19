package com.mcosta.util;

import java.io.FileWriter;
import java.util.Set;

import com.thoughtworks.xstream.XStream;

public class PersistenceFile<T> {

    private Set<T> list;
    private String fileName;

    public PersistenceFile(String fileName, Set<T> list) {
        this.fileName = fileName;
        this.list = list;
    }
    
    public void persist() throws Exception {
        XStream xs = new XStream();
        String xml = xs.toXML(list);
        System.out.println(fileName);
        FileWriter fw = new FileWriter(fileName);
        fw.write(xml);
        fw.close();        
    }

}
