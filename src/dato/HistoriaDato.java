/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dato;

import entidad.Historia;
import entidad.Mensaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author fabian
 */
public class HistoriaDato {
    
    public ArrayList<Mensaje> obtenerListHistoriasTodo() {
        return (ArrayList<Mensaje>) executeHQLQuery(query);
    }
    
    public Integer insertarHistoria(Historia historia) {
        return save(historia);
    }
    
    private String query = "from historia h";
    
    private List executeHQLQuery(String hql) {
        List resultList = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery(hql);
            resultList = q.list();
            session.getTransaction().commit();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return resultList;
    }
    
    private Integer save(Serializable serializable) {
        Integer newId = 0;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            newId = (int) session.save(serializable);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return newId;
    }
}
