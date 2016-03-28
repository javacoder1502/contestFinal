package quiz.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;



public class HibernateOperations {
	
	Session session;
	
public List<Object[]> getResultList(String query){
		
		session = HibernateUtil.getSessionFactory().getCurrentSession();
	    List<Object[]> ls_ob = new ArrayList<Object[]>();
		
		Transaction tx = null;
	
		try {
			tx = session.beginTransaction();

		Query q = session.createQuery(query);
     
		ls_ob  = (List<Object[]>)q.list();

	}catch (HibernateException ex) {
		if (tx != null) {
			System.out.println("Exception in getList method " + ex);
			
			  tx.rollback(); 
			  ex.printStackTrace();
			 
		}
		System.out.println("Exception getList tx open" + ex);
	} finally {
		session.close();
	}
		
		return ls_ob;
	}
	
	
	
	
	
	
public List<Object> getListForSingleColumn(String query){
		
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	/*	session = HibernateUtil.getSessionFactory().openSession();*/
		
		List<Object> ls_ob = new ArrayList<Object>();
		
		Transaction tx = null;
	
		try {
			tx = session.beginTransaction();

		Query q = session.createQuery(query);
         
		ls_ob  = q.list();

	}catch (HibernateException ex) {
		if (tx != null) {
			System.out.println("Exception in getList method " + ex);
			
			  tx.rollback(); 
			  ex.printStackTrace();
			 
		}
		System.out.println("Exception getList tx open" + ex);
	} finally {
		session.close();
	}
		
		return ls_ob;
	}

public void update(String query){
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tx = null;
	
	try {
		tx = session.beginTransaction();
	
	/*SQLQuery sqlQuery = session.createSQLQuery(query);
	sqlQuery.executeUpdate();*/
	
	Query query_s = session.createQuery(query);
        query_s.executeUpdate();
    tx.commit();
	//tx.commit();
	}catch (HibernateException ex) {
		if (tx != null) {
			System.out.println("Exception in getList method " + ex);
			
			  tx.rollback(); 
			  ex.printStackTrace();
			 
		}
		System.out.println("Exception getList tx open" + ex);
	} finally {
		//session.close();
	}
	
}



public void insert(String query){
	session = HibernateUtil.getSessionFactory().getCurrentSession();
	Transaction tx = session.beginTransaction();
	//get Connction from Session
	
	session.doWork(new Work() {
		
		@Override
		public void execute(Connection connection) throws SQLException {
			 PreparedStatement ps = connection
		                .prepareStatement(query);
		      
		            ps.executeUpdate();
		       
			
		}
	});
	
	tx.commit();
	//session.close();

	
}


}
