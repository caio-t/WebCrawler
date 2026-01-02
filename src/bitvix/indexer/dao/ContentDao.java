package bitvix.indexer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


import bitvix.indexer.model.Customer;
import bitvix.indexer.model.Site;
import bitvix.indexer.util.Util;

public class ContentDao implements WebCrawlerDao {
	
	private Connection conn;
	private PreparedStatement command;
	private LinkedList<Customer> customers = new LinkedList<Customer>();
	
	
	public LinkedList<Site> getDomain(Customer customer) {
		// TODO Auto-generated method stub
		connect();
		
		LinkedList<Site> sites = new LinkedList<Site>();
		
		String sql = "SELECT * FROM entt_site "
				+ "WHERE sit_chp_site in (select sit_chp_site FROM "
				+ "	tbla_customersite WHERE cus_chp_customer = "+customer.getId()+")";
		ResultSet rs;
		
		try {
			Statement stm = conn.createStatement(); 
			rs = stm.executeQuery(sql);
		
			Site site;
			while(rs.next())
			{
				site = new Site();
				site.setName(rs.getString("sit_var_name"));
				site.setUrl(rs.getString("sit_var_url"));
				site.setDomain(rs.getString("sit_var_domain"));
				site.setMaxDepthOfCrawling(rs.getInt("sit_int_maxdepthofcrawling"));
				site.setMaxPagesToFetch(rs.getInt("sit_int_maxpagestofetch"));
				site.setPolitenessDelay(rs.getInt("sit_int_politenessdelay"));
				
				sites.add(site);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return sites;
		
		 
	}
	
	@Override
	public void insert(int customerId, String keyword, String anchor, String text, String url, String domain, String subDomain, String parentUrl) {
		
		connect();
		String sql = "";
		ResultSet rs;
		try {  
			
				sql = "SELECT con_chp_content, con_var_anchor, con_var_text FROM entt_content WHERE cus_chp_customer = "+customerId+ " AND con_var_keyword = '"+ keyword +"' AND con_var_url = '"+url+"'";
			
				Statement stm = conn.createStatement(); 
				rs = stm.executeQuery(sql);
				
				if (!rs.last())
				{
					sql = "INSERT INTO entt_content (cus_chp_customer,con_var_keyword,con_var_anchor,con_var_text,con_var_url,con_var_domain,con_var_subdomain,con_var_parenturl,con_var_dateregister) "
							+ "	VALUES (?,?,?,?,?,?,?,?,now())"; 
					
					this.command = conn.prepareStatement(sql);
					
					this.command.setInt(1, customerId); 
					this.command.setString(2, keyword); 
					this.command.setString(3, anchor); 
					this.command.setString(4, text); 
					this.command.setString(5, url);
					this.command.setString(6, domain);
					this.command.setString(7, subDomain);
					this.command.setString(8, parentUrl);
					
					this.command.setQueryTimeout(4000);
					this.command.execute();
					System.out.println("Inserida!");  
				}
				else
				{
					rs.beforeFirst();
					
					rs.next();
					
					sql = "UPDATE entt_content SET con_var_dateregister = now(), con_var_anchor = ?, con_var_text = ? WHERE con_chp_content = ?";
					this.command = conn.prepareStatement(sql);
					
					this.command.setString(1, anchor); 
					this.command.setString(2, text); 
					this.command.setInt(3, rs.getInt("con_chp_content")); 
					this.command.setQueryTimeout(2000);
					this.command.executeUpdate();
					System.out.println("Atualizado!");  
				}
				
	      } catch (SQLException e) {  
	         printError("Erro ao inserir conteúdo", e.getMessage() + " ==> SQL DEFEITUOSA: " + sql);  
	      } finally {  
	         close();  
	      }  
		
	}

	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
	private void close() {  
	      try {  
	         conn.close();  
	         command.close();  
	         System.out.println("Conexão Fechada");  
	      } catch (SQLException e) {  
	    	  printError("Erro ao fechar conexão", e.getMessage());  
	      }  
	   }  
	
	private void connect() {  
	      try {  
	         this.conn = ConnFactory.conexao(Util.URL, Util.USERNAME, Util.PASSWORD, ConnFactory.MYSQL);  
	         System.out.println("Conectado!");  
	      } catch (ClassNotFoundException e) {  
	    	  printError("Erro ao carregar o driver", e.getMessage());  
	      } catch (SQLException e) {  
	    	  printError("Erro ao conectar", e.getMessage());  
	      }  
	   }  
	
	private void printError(String msg, String msgErro) {  
	      System.err.println(msg);  
	      System.out.println(msgErro);  
	      System.exit(0);  
	   }

	public LinkedList<Site> getSitesCustomer(Customer customer) {
		// TODO Auto-generated method stub
		connect();
		
		LinkedList<Site> sites = new LinkedList<Site>();
		
		String sql = "SELECT * FROM entt_site "
				+ "WHERE sit_chp_site in (select sit_chp_site FROM "
				+ "	tbla_customersite WHERE cus_chp_customer = "+customer.getId()+")";
		ResultSet rs;
		
		try {
			Statement stm = conn.createStatement(); 
			rs = stm.executeQuery(sql);
		
			Site site;
			while(rs.next())
			{
				site = new Site();
				site.setName(rs.getString("sit_var_name"));
				site.setUrl(rs.getString("sit_var_url"));
				site.setDomain(rs.getString("sit_var_domain"));
				site.setMaxDepthOfCrawling(rs.getInt("sit_int_maxdepthofcrawling"));
				site.setMaxPagesToFetch(rs.getInt("sit_int_maxpagestofetch"));
				site.setPolitenessDelay(rs.getInt("sit_int_politenessdelay"));
				
				sites.add(site);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return sites;
		
		 
	}
	
	public ArrayList<String> getBlackList(Customer customer) {
		// TODO Auto-generated method stub
		connect();
		
		ArrayList<String> sites = new ArrayList<String>();
		
		String sql = "SELECT * FROM entt_blacklist "
				+ "WHERE cus_chp_customer = " + customer.getId();
		ResultSet rs;
		
		
		try {
			Statement stm = conn.createStatement(); 
			rs = stm.executeQuery(sql);
		
			while(rs.next())
			{
				sites.add(rs.getString("bll_var_url"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return sites;
		
		 
	}
	
	public ArrayList<String> getSiteSeed(Customer customer) {
		// TODO Auto-generated method stub
		connect();
		
		ArrayList<String> sites = new ArrayList<String>();
		
		String sql = "SELECT * FROM entt_siteseed";
				
		ResultSet rs;
		
		
		try {
			Statement stm = conn.createStatement(); 
			rs = stm.executeQuery(sql);
		
			while(rs.next())
			{
				sites.add(rs.getString("sid_var_url"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return sites;
		
		 
	}
	
	public ArrayList<String> getContentReaded(Customer customer) {
		// TODO Auto-generated method stub
		connect();
		
		ArrayList<String> sites = new ArrayList<String>();
		
		String sql = "SELECT * FROM entt_contentreaded "
				+ "WHERE cus_chp_customer = " + customer.getId();
		ResultSet rs;
		
		
		try {
			Statement stm = conn.createStatement(); 
			rs = stm.executeQuery(sql);
		
			while(rs.next())
			{
				sites.add(rs.getString("cor_var_url"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return sites;
		
		 
	}
	
	
	
	@Override
	public void fetchCustomer() {
		// TODO Auto-generated method stub
		connect();
		
	
		String sql = "SELECT * FROM entt_customer "
				+ "WHERE cus_int_situation = "+Customer.CUSTOMER_ACTIVE;
		ResultSet rs;
		
		try {
			Statement stm = conn.createStatement(); 
			rs = stm.executeQuery(sql);
			Customer customer = null;
			LinkedList<Customer> customerList = new LinkedList<Customer>();
			
			while(rs.next())
			{
				customer = new Customer();
				customer.setId(rs.getInt("cus_chp_customer"));
				customer.setName(rs.getString("cus_var_name"));
				customer.setKeyword(rs.getString("cus_var_keyword"));
				
				System.out.println(customer.getName());
				customerList.add(customer); 
			}
			
			 Iterator<Customer> iterator = customerList.iterator();
		     
			 while(iterator.hasNext())
			 {
				 customer = iterator.next();
				
				 customer.addSite(this.getSitesCustomer(customer));
				 
				 customer.addBlackList(this.getBlackList(customer));
				 
				 customer.addSiteSeed(this.getSiteSeed(customer));
				 
				 customer.addContentReaded(this.getContentReaded(customer));
		
				 
				 this.customers.add(customer);
				 
			 }
			 
			
			 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	public LinkedList<Customer> getCustomers()
	{
		return this.customers;
	}
	
	public void setCurrentCustomer(Customer customer)
	{
		connect();
		String sql = "";
		try {  
			
			sql = "UPDATE tbla_config SET cof_int_currentcustomer = ?";
			this.command = conn.prepareStatement(sql);
			
			this.command.setInt(1, customer.getId()); 
			this.command.setQueryTimeout(2000);
			this.command.executeUpdate();
		} catch (SQLException e) {  
	         printError("Erro ao inserir conteúdo", e.getMessage() + " ==> SQL DEFEITUOSA: " + sql);  
	    } finally {  
	         close();  
	    }  
	}

	public void updateCountResults(Customer customer) {
		connect();
		
		String sql = "";
		
		try {  
			
			sql = "UPDATE entt_customer SET cus_int_numberofresults = (SELECT COUNT(con_chp_content) FROM entt_content WHERE cus_chp_customer = ?) WHERE cus_chp_customer = ?";
			this.command = conn.prepareStatement(sql);
			
			this.command.setInt(1, customer.getId()); 
			this.command.setInt(2, customer.getId()); 
			
			this.command.setQueryTimeout(4000);
			this.command.executeUpdate();
		} catch (SQLException e) {  
	         printError("Erro ao inserir conteúdo", e.getMessage() + " ==> SQL DEFEITUOSA: " + sql);  
	    } finally {  
	         close();  
	    }  
		
		
	}
	

}
