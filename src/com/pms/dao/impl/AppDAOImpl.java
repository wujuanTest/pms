package com.pms.dao.impl;

import java.util.List;
import java.math.BigInteger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.AppDAO;
import com.pms.model.Application;
import com.pms.model.HibernateUtil;

public class AppDAOImpl implements AppDAO {

	@Override
	public Application AppAdd(Application app) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			app = (Application) session.merge(app);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名应用。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return app;
	}

	@Override
	public void AppMod(Application app) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(app);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return;	

	}

	@Override
	public void AppDel(Application app) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.delete(app);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Application> GetApplications(Application criteria, int page,
			int rows) throws Exception{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Application> rs = null;
		String sqlString = "select * from application where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				sqlString += " and name like :name ";
			}
			if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
				sqlString += " and flag like :flag ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Application.class);
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					q.setString( "name", "%" + criteria.getName() + "%" );
				}
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					q.setString( "flag", "%" + criteria.getFlag() + "%" );
				}
			}
			q.setFirstResult((page-1) * rows);   
			q.setMaxResults(rows);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public int GetApplicationsCount(Application criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from application where 1 = 1 ";
		if( criteria != null ) {
			if( criteria != null ) {
				if(criteria.getName() != null && criteria.getName().length() > 0) {
					sqlString += " and name like :name ";
				}
				if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
					sqlString += " and flag like :flag ";
				}
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			if( criteria != null ) {
				if( criteria != null ) {
					if(criteria.getName() != null && criteria.getName().length() > 0) {
						q.setString( "name", "%" + criteria.getName() + "%" );
					}
					if(criteria.getFlag() != null && criteria.getFlag().length() > 0) {
						q.setString( "flag", "%" + criteria.getFlag() + "%" );
					}
				}
			}
			rs = ((BigInteger)q.uniqueResult()).intValue();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	public List<Application> GetApplicationsWithNopage() throws Exception
	{
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<Application> rs = null;
		String sqlString = "select * from application ";
				
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(Application.class);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<User> GetUsersByParentId(int pid, int page, int rows)
//			throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		List<User> rs = null;
//		String sqlString = "select * from user where parent_id = :parent_id ";
//		
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
//			q.setInteger("parent_id", pid);
//			q.setFirstResult((page-1) * rows);   
//			q.setMaxResults(rows);   
//			rs = q.list();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		} finally {
//			HibernateUtil.closeSession();
//		}
//		return rs;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<User> GetUsersByParentIdWithNoPage(int pid, User criteria)
//			throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		List<User> rs = null;
//		String sqlString = "select * from user where parent_id = :parent_id ";
//		if( criteria != null ) {
//			if(criteria.getName() != null && criteria.getName().length() > 0) {
//				sqlString += " and name like :name ";
//			}
//			if(criteria.getStatus() != User.STATUSNONE) {
//				sqlString += " and status = :status ";
//			}
//			if(criteria.getUnit() != null && criteria.getUnit().length() > 0) {
//				sqlString += " and unit like :unit ";
//			}
//			if(criteria.getPolice_type() != null && criteria.getPolice_type().length() > 0) {
//				sqlString += " and police_type like :police_type ";
//			}
//			if(criteria.getSex() != null && criteria.getSex().length() > 0) {
//				sqlString += " and sex like :sex ";
//			}
//			if(criteria.getIdnum() != null && criteria.getIdnum().length() > 0) {
//				sqlString += " and idnum like :idnum ";
//			}
//			if(criteria.getMax_sensitive_level() != null && criteria.getMax_sensitive_level().length() > 0) {
//				sqlString += " and max_sensitive_level like :max_sensitive_level ";
//			}
//			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
//				sqlString += " and position like :position ";
//			}
//			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
//				sqlString += " and dept like :dept ";
//			}
//			if(criteria.getTitle() != null && criteria.getTitle().length() > 0) {
//				sqlString += " and title like :title ";
//			}
//			if(criteria.getPolice_num() != null && criteria.getPolice_num().length() > 0) {
//				sqlString += " and police_num like :police_num ";
//			}
//		}
//		
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
//			q.setInteger("parent_id", pid);
//			if( criteria != null ) {
//				if(criteria.getName() != null && criteria.getName().length() > 0) {
//					q.setString( "name", "%" + criteria.getName() + "%" );
//				}
//				if(criteria.getStatus() != User.STATUSNONE) {
//					q.setInteger( "status", criteria.getStatus() );
//				}
//	
//				if(criteria.getUnit() != null && criteria.getUnit().length() > 0) {
//					q.setString( "unit", "%" + criteria.getUnit() + "%" );
//				}
//				if(criteria.getPolice_type() != null && criteria.getPolice_type().length() > 0) {
//					q.setString( "police_type", "%" + criteria.getPolice_type() + "%" );
//				}
//				if(criteria.getSex() != null && criteria.getSex().length() > 0) {
//					q.setString( "sex", "%" + criteria.getSex() + "%" );
//				}
//				if(criteria.getIdnum() != null && criteria.getIdnum().length() > 0) {
//					q.setString( "idnum", "%" + criteria.getIdnum() + "%" );
//				}
//				if(criteria.getMax_sensitive_level() != null && criteria.getMax_sensitive_level().length() > 0) {
//					q.setString( "max_sensitive_level", "%" + criteria.getMax_sensitive_level() + "%" );
//				}
//				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
//					q.setString( "position", "%" + criteria.getPosition() + "%" );
//				}
//				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
//					q.setString( "dept", "%" + criteria.getDept() + "%" );
//				}
//				if(criteria.getTitle() != null && criteria.getTitle().length() > 0) {
//					q.setString( "title", "%" + criteria.getTitle() + "%" );
//				}
//				if(criteria.getPolice_num() != null && criteria.getPolice_num().length() > 0) {
//					q.setString( "police_num", "%" + criteria.getPolice_num() + "%" );
//				}
//			}
//			rs = q.list();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		} finally {
//			HibernateUtil.closeSession();
//		}
//		return rs;
//	}
//
//	@Override
//	public int GetUsersCountByParentId(int pid, User criteria) throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		int rs;
//		String sqlString = "select count(*) from user where parent_id = :parent_id ";
//		if( criteria != null ) {
//			if(criteria.getName() != null && criteria.getName().length() > 0) {
//				sqlString += " and name like :name ";
//			}
//			if(criteria.getStatus() != User.STATUSNONE) {
//				sqlString += " and status = :status ";
//			}
//			if(criteria.getUnit() != null && criteria.getUnit().length() > 0) {
//				sqlString += " and unit like :unit ";
//			}
//			if(criteria.getPolice_type() != null && criteria.getPolice_type().length() > 0) {
//				sqlString += " and police_type like :police_type ";
//			}
//			if(criteria.getSex() != null && criteria.getSex().length() > 0) {
//				sqlString += " and sex like :sex ";
//			}
//			if(criteria.getIdnum() != null && criteria.getIdnum().length() > 0) {
//				sqlString += " and idnum like :idnum ";
//			}
//			if(criteria.getMax_sensitive_level() != null && criteria.getMax_sensitive_level().length() > 0) {
//				sqlString += " and max_sensitive_level like :max_sensitive_level ";
//			}
//			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
//				sqlString += " and position like :position ";
//			}
//			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
//				sqlString += " and dept like :dept ";
//			}
//			if(criteria.getTitle() != null && criteria.getTitle().length() > 0) {
//				sqlString += " and title like :title ";
//			}
//			if(criteria.getPolice_num() != null && criteria.getPolice_num().length() > 0) {
//				sqlString += " and police_num like :police_num ";
//			}
//		}
//		
//		try {
//			Query q = session.createSQLQuery(sqlString);
//			q.setInteger("parent_id", pid);
//			if( criteria != null ) {
//				if(criteria.getName() != null && criteria.getName().length() > 0) {
//					q.setString( "name", "%" + criteria.getName() + "%" );
//				}
//				if(criteria.getStatus() != User.STATUSNONE) {
//					q.setInteger( "status", criteria.getStatus() );
//				}
//	
//				if(criteria.getUnit() != null && criteria.getUnit().length() > 0) {
//					q.setString( "unit", "%" + criteria.getUnit() + "%" );
//				}
//				if(criteria.getPolice_type() != null && criteria.getPolice_type().length() > 0) {
//					q.setString( "police_type", "%" + criteria.getPolice_type() + "%" );
//				}
//				if(criteria.getSex() != null && criteria.getSex().length() > 0) {
//					q.setString( "sex", "%" + criteria.getSex() + "%" );
//				}
//				if(criteria.getIdnum() != null && criteria.getIdnum().length() > 0) {
//					q.setString( "idnum", "%" + criteria.getIdnum() + "%" );
//				}
//				if(criteria.getMax_sensitive_level() != null && criteria.getMax_sensitive_level().length() > 0) {
//					q.setString( "max_sensitive_level", "%" + criteria.getMax_sensitive_level() + "%" );
//				}
//				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
//					q.setString( "position", "%" + criteria.getPosition() + "%" );
//				}
//				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
//					q.setString( "dept", "%" + criteria.getDept() + "%" );
//				}
//				if(criteria.getTitle() != null && criteria.getTitle().length() > 0) {
//					q.setString( "title", "%" + criteria.getTitle() + "%" );
//				}
//				if(criteria.getPolice_num() != null && criteria.getPolice_num().length() > 0) {
//					q.setString( "police_num", "%" + criteria.getPolice_num() + "%" );
//				}
//			}
//			rs = ((BigInteger)q.uniqueResult()).intValue();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			System.out.println(e.getMessage());
//			throw e;
//		} finally {
//			HibernateUtil.closeSession();
//		}
//		return rs;
//	}



}
