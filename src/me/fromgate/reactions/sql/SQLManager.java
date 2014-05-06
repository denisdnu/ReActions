/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.Variables;

public class SQLManager {
	private static boolean enabled = false;
	private static String serverAdress="127.0.0.1";
	private static String port="3306";
	private static String dataBase;
	private static String userName;
	private static String password;


	public static void init(){
		loadCfg();
		saveCfg();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			enabled = true;
		}
		catch (ClassNotFoundException e) {
			ReActions.util.logOnce("mysqlinitfail", "MySQL JDBC Driver not found!");
			enabled = false;
			return;
		}
	}

	public static void loadCfg(){
		serverAdress = ReActions.instance.getConfig().getString("MySQL.server","localhost");
		port = ReActions.instance.getConfig().getString("MySQL.port","3306");
		dataBase = ReActions.instance.getConfig().getString("MySQL.database","ReActions");
		userName = ReActions.instance.getConfig().getString("MySQL.username","root");
		password = ReActions.instance.getConfig().getString("MySQL.password","password");
	}

	public static void saveCfg(){
		ReActions.instance.getConfig().set("MySQL.server",serverAdress );
		ReActions.instance.getConfig().set("MySQL.port",port );
		ReActions.instance.getConfig().set("MySQL.database",dataBase);
		ReActions.instance.getConfig().set("MySQL.username",userName);
		ReActions.instance.getConfig().set("MySQL.password",password);
		ReActions.instance.saveConfig();
	}



	public static void setQueryToVar (String player, String var, String query, int column){
		if (!enabled) return;
		String result = SQLManager.executeSelect(query, column);
		Variables.setVar (player, var, result);
	}


	public static String executeSelect (String query){
		return executeSelect(query,-1);
	}

	public static boolean compareSelect (String value, String query, int column){
		String result = executeSelect(query,column);
		if (ReActions.util.isInteger(result,value)) return (Integer.parseInt(result)==Integer.parseInt(value));
		return result.equalsIgnoreCase(value);
	}


	public static String executeSelect (String query, int column){
		if (!enabled) return "";
		Connection connection =null;
		Statement selectStmt = null;
		ResultSet result = null;
		String resultStr = "";
		String connectionLine = "jdbc:mysql://"+serverAdress+(port.isEmpty() ? "":":"+port)+"/"+dataBase;
		try {
			connection = DriverManager.getConnection(connectionLine, userName, password);
		} catch (Exception e) {
			ReActions.util.logOnce("sqlconnect", "Failed to connect to database: "+connectionLine +" user: "+userName);
			return "";
		}
		try {
			selectStmt = connection.createStatement();
			result = selectStmt.executeQuery(query);
			if (result.first()){
				int columns = result.getMetaData().getColumnCount();
				if (column>0&&column<=columns) resultStr = result.getString(column);
			}
		} catch (Exception e) {
			ReActions.util.logOnce(query, "Failed to execute query: "+query);
		}
		try{
			if (result!=null) result.close();
			if (selectStmt!=null) selectStmt.close();
			if(connection != null) connection.close();
		} catch (Exception e) {
		}
		return resultStr;
	}


	public static boolean executeUpdate (String query) {
		if (!enabled) return false;
		Connection connection =null;
		Statement statement = null;
		boolean ok = false;
		String connectionLine = "jdbc:mysql://"+serverAdress+(port.isEmpty() ? "":":"+port)+"/"+dataBase;
		try {
			connection = DriverManager.getConnection(connectionLine, userName, password);
		} catch (Exception e) {
			ReActions.util.logOnce("sqlconnect", "Failed to connect to database: "+connectionLine +" user: "+userName);
			return false;
		}
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
			ok = true;
		} catch (Exception e) {
			ReActions.util.logOnce(query, "Failed to execute query: "+query);
			if (e.getMessage()!=null) ReActions.util.logOnce(query+e.getMessage(), e.getMessage());
			e.printStackTrace();
		}
		try{
			if (statement!=null) statement.close();
			if (connection != null) connection.close();
		} catch (Exception e) {
		}
		return ok;
	}



	public static boolean isEnabled(){
		return enabled;
	}

	public static boolean isSelectResultEmpty(String query) {
		if (!enabled) return false;
		Connection connection =null;
		Statement selectStmt = null;
		ResultSet result = null;
		boolean resultBool = false;
		String connectionLine = "jdbc:mysql://"+serverAdress+(port.isEmpty() ? "":":"+port)+"/"+dataBase;
		try {
			connection = DriverManager.getConnection(connectionLine, userName, password);
		} catch (Exception e) {
			ReActions.util.logOnce("sqlconnect", "Failed to connect to database: "+connectionLine +" user: "+userName);
			return false;
		}
		try {
			selectStmt = connection.createStatement();
			result = selectStmt.executeQuery(query);
			resultBool = result.next();
		} catch (Exception e) {
			ReActions.util.logOnce(query, "Failed to execute query: "+query);
			if (e.getMessage()!=null) ReActions.util.logOnce(query+e.getMessage(), e.getMessage());
		} 
		try{
			if (result!=null) result.close();
			if (selectStmt!=null) selectStmt.close();
			if(connection != null) connection.close();
		} catch (Exception e) {
		}
		return resultBool;
	}
	
	
	


}
