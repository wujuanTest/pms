package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.UserDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.User;

public class UserDAOImpl implements UserDAO {

	@Override
	public User UserAdd(User user) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			user = (User) session.merge(user);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名机构。");
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
		return user;
	}

	@Override
	public void UserMod(User user) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(user);
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
	public void UserDel(User user) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.delete(user);
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
	public List<User> GetUsersByParentId(String pid, int page, int rows)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE where GA_DEPARTMENT = :GA_DEPARTMENT ";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setString("GA_DEPARTMENT", pid);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> GetUsersByParentIdWithNoPage(String pid, User criteria)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<User> rs = null;
		String sqlString = "select * from WA_AUTHORITY_POLICE where GA_DEPARTMENT = :GA_DEPARTMENT ";
		if( criteria != null ) {
			if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
				sqlString += " and NAME like :NAME ";
			}
//			if(criteria.getUSER_STATUS() != User.STATUSNONE) {
//				sqlString += " and status = :status ";
//			}
			if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
				sqlString += " and BUSINESS_TYPE like :BUSINESS_TYPE ";
			}
			if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
				sqlString += " and POLICE_SORT like :POLICE_SORT ";
			}
			if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
				sqlString += " and SEXCODE like :SEXCODE ";
			}
			if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
				sqlString += " and CERTIFICATE_CODE_SUFFIX like :CERTIFICATE_CODE_SUFFIX ";
			}
			if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
				sqlString += " and SENSITIVE_LEVEL like :SENSITIVE_LEVEL ";
			}
			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
				sqlString += " and position like :position ";
			}
			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
				sqlString += " and dept like :dept ";
			}
			if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
				sqlString += " and TAKE_OFFICE like :TAKE_OFFICE ";
			}
			if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
				sqlString += " and POLICE_NO like :POLICE_NO ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(User.class);
			q.setString("GA_DEPARTMENT", pid);
			if( criteria != null ) {
				if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
					q.setString( "NAME", "%" + criteria.getNAME() + "%" );
				}
//				if(criteria.getStatus() != User.STATUSNONE) {
//					q.setInteger( "status", criteria.getStatus() );
//				}
	
				if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
					q.setString( "BUSINESS_TYPE", "%" + criteria.getBUSINESS_TYPE() + "%" );
				}
				if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
					q.setString( "POLICE_SORT", "%" + criteria.getPOLICE_SORT() + "%" );
				}
				if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
					q.setString( "SEXCODE", "%" + criteria.getSEXCODE() + "%" );
				}
				if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
					q.setString( "CERTIFICATE_CODE_SUFFIX", "%" + criteria.getCERTIFICATE_CODE_SUFFIX() + "%" );
				}
				if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
					q.setString( "SENSITIVE_LEVEL", "%" + criteria.getSENSITIVE_LEVEL() + "%" );
				}
				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
					q.setString( "position", "%" + criteria.getPosition() + "%" );
				}
				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
					q.setString( "dept", "%" + criteria.getDept() + "%" );
				}
				if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
					q.setString( "TAKE_OFFICE", "%" + criteria.getTAKE_OFFICE() + "%" );
				}
				if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
					q.setString( "POLICE_NO", "%" + criteria.getPOLICE_NO() + "%" );
				}
			}
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
	public int GetUsersCountByParentId(String pid, User criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from WA_AUTHORITY_POLICE where GA_DEPARTMENT = :GA_DEPARTMENT ";
		if( criteria != null ) {
			if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
				sqlString += " and NAME like :NAME ";
			}
//			if(criteria.getStatus() != User.STATUSNONE) {
//				sqlString += " and status = :status ";
//			}
			if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
				sqlString += " and BUSINESS_TYPE like :BUSINESS_TYPE ";
			}
			if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
				sqlString += " and POLICE_SORT like :POLICE_SORT ";
			}
			if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
				sqlString += " and SEXCODE like :SEXCODE ";
			}
			if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
				sqlString += " and CERTIFICATE_CODE_SUFFIX like :CERTIFICATE_CODE_SUFFIX ";
			}
			if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
				sqlString += " and SENSITIVE_LEVEL like :SENSITIVE_LEVEL ";
			}
			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
				sqlString += " and position like :position ";
			}
			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
				sqlString += " and dept like :dept ";
			}
			if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
				sqlString += " and TAKE_OFFICE like :TAKE_OFFICE ";
			}
			if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
				sqlString += " and POLICE_NO like :POLICE_NO ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
			q.setString("GA_DEPARTMENT", pid);
			if( criteria != null ) {
				if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
					q.setString( "NAME", "%" + criteria.getNAME() + "%" );
				}
//				if(criteria.getStatus() != User.STATUSNONE) {
//					q.setInteger( "status", criteria.getStatus() );
//				}
	
				if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
					q.setString( "BUSINESS_TYPE", "%" + criteria.getBUSINESS_TYPE() + "%" );
				}
				if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
					q.setString( "POLICE_SORT", "%" + criteria.getPOLICE_SORT() + "%" );
				}
				if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
					q.setString( "SEXCODE", "%" + criteria.getSEXCODE() + "%" );
				}
				if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
					q.setString( "CERTIFICATE_CODE_SUFFIX", "%" + criteria.getCERTIFICATE_CODE_SUFFIX() + "%" );
				}
				if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
					q.setString( "SENSITIVE_LEVEL", "%" + criteria.getSENSITIVE_LEVEL() + "%" );
				}
				if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
					q.setString( "position", "%" + criteria.getPosition() + "%" );
				}
				if(criteria.getDept() != null && criteria.getDept().length() > 0) {
					q.setString( "dept", "%" + criteria.getDept() + "%" );
				}
				if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
					q.setString( "TAKE_OFFICE", "%" + criteria.getTAKE_OFFICE() + "%" );
				}
				if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
					q.setString( "POLICE_NO", "%" + criteria.getPOLICE_NO() + "%" );
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

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Organization> GetOrgNodeByParentId(int pid) throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		List<Organization> rs = null;
//		String sqlString = "select * from organization where parent_id = :parent_id ";
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
//			q.setInteger("parent_id", pid);
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
//	public boolean OrgHasChild(int pid) throws Exception {
//		int rs = GetOrgNodeCountByParentId(pid);
//		return rs > 0;
//	}
//

//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Organization> GetOrgNodeByParentId(int pid, Organization condition)
//			throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		List<Organization> rs = null;
//		String sqlString = "select * from organization where parent_id = :parent_id ";
//		if(condition.getName() != null && condition.getName().length() > 0) {
//			sqlString += " and name like :name ";
//		}
//		if(condition.getUid() != null && condition.getUid().length() > 0) {
//			sqlString += " and uid = :uid ";
//		}
//		if(condition.getOrg_level() != null && condition.getOrg_level().length() > 0) {
//			sqlString += " and org_level = :org_level ";
//		}
//			
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
//			q.setInteger("parent_id", pid);
//			if(condition.getName() != null && condition.getName().length() > 0) {
//				q.setString( "name", "%" + condition.getName() + "%" );
//			}
//			if(condition.getUid() != null && condition.getUid().length() > 0) {
//				q.setString( "uid", condition.getUid() );
//			}
//			if(condition.getOrg_level() != null && condition.getOrg_level().length() > 0) {
//				q.setString( "org_level", condition.getOrg_level() );
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

//
//	@SuppressWarnings("unchecked")
//	@Override
//	public Organization GetOrgNodeById(int id) throws Exception {
//		Session session = HibernateUtil.currentSession();
//		Transaction tx = session.beginTransaction();
//		Organization rs = null;
//		String sqlString = "select * from organization where id = :id ";
//		try {
//			Query q = session.createSQLQuery(sqlString).addEntity(Organization.class);
//			q.setInteger("id", id);
//			rs = (Organization) q.uniqueResult();
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
