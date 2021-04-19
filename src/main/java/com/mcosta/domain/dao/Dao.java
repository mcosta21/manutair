package com.mcosta.domain.dao;

import java.util.List;

public interface Dao {
    public void create(Object object) throws Exception;
    public void update(Object object) throws Exception;
    public void delete(Object object) throws Exception;
    public List index();
    
}
