package at.tuwien.ase.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import at.tuwien.ase.dao.CompanyDaoInterface;
import at.tuwien.ase.model.Company;
import at.tuwien.ase.model.User;
import at.tuwien.ase.model.UserType;

import com.google.common.collect.Lists;


public class ParseUsers {

	private CompanyDaoInterface companyDao;
	
	private List<String> companies = new ArrayList<String>();
	private List<User> users = new ArrayList<User>();


	
	
	/**
	 * Method checks whether the uploaded file is of defined type, i.e., .xlsx.
	 * 
	 * @param filename - file name
	 * 
	 * @return - true if the file type corresponds to the above types, false otherwise.
	 */
	public boolean checkFileType(String filename) {
		
		if (filename.endsWith(".xlsx"))
			return true;
		else
			return false;
	}
	
	/**
	 * Method parses the input file with the extension .xlsx, .xls, or .csv. and creates a list
	 * of User objects.
	 * 
	 * @param filename - a file to parse.
	 * 
	 * @return - true if the file is parsed, false if an exception occurred during the parsing.
	 */
	public boolean processFile(InputStream filename) {
		
		try {
			
			users.clear();
			
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(filename);
            
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            List<Row> rowss = Lists.newArrayList(rowIterator);
            
            extractCompanies(rowss);
            extractUsers(sheet);
            
            filename.close();
        }
        catch (Exception e) {
        	//e.printStackTrace();
            return false;
        }
		
		return true;
	}
	
	private void extractUsers(XSSFSheet sheet) {
		
		XSSFRow row;
        XSSFCell cell;

        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 0; // No of columns
        int tmp = 0;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for(int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if(row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if(tmp > cols) cols = tmp;
            }
        }

        for(int c = 0; c < cols; c++) {
        	
        	String company = null;
        	
        	for(int r = 0; r < rows; r++) {
        		
                row = sheet.getRow(r);
                
                if(row != null) {
                	cell = row.getCell((short)c);

                    if(cell != null) {
                    	
                    	if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    	
	                    	if (companies.contains(cell.getStringCellValue())) {
	                    		
	                    		company = cell.getStringCellValue();
	                    		//System.out.println("Current company: " + company);
	                    	
	                    	} else if (company != null)
	                    		//System.out.println(cell.getStringCellValue() + " -- " + company);
	                    		addUsers(cell.getStringCellValue(), company);
                    	}
                    }
                }
        	}
        }
	}
	
	private void addUsers(String user, String company) {
		
		String name = null;
		String lastname = null;
		String userName = null;
		User u;
		
		if (!user.equals("-")) {
			
			if (user.indexOf(",") == -1) {
				
				lastname = user.substring(0, user.indexOf(" "));
				name = user.substring(user.indexOf(" ") + 1);
				
			} else {
				
				lastname = user.substring(0, user.indexOf(","));
				name = user.substring(user.indexOf(",") + 2);
			}
			
			userName = name.substring(0, 2) + lastname.replaceAll("\\s","");
			//System.out.println("User: " + name + " " + lastname + " -- " + userName + " -- "+ company);
			
			u = new User();
			u.setForname(name);
			u.setLastname(lastname);
			u.setUsername(userName);
			u.setPassword(userName);
			u.setUserType(UserType.WORKER);
			u.setCompany(findCompany(company));
			
			users.add(u);
		}
		
	}
	
	private Company findCompany(String name) {
		
		for (Company c : companyDao.listAll()) {
			
			if (c.getName().equals(name))
				return c;
		}
		
		return null;
	}
	
	private void extractCompanies(List<Row> rows) {

		for (Company c : companyDao.listAll()) {
			companies.add(c.getName());
		}
		/*
		Row r;
		List<Cell> cells;
		Cell c;
		
		r = rows.get(2);

        cells = Lists.newArrayList(r.cellIterator());
        
        for (int j = 0; j < cells.size(); j++) {
		
        	c = cells.get(j);
        	
            if (c.getCellType() == Cell.CELL_TYPE_STRING) {
            	
            	if (c.getStringCellValue().equals("keine"))
            		break;
            	else {
            		companies.add(c.getStringCellValue());
            		System.out.println(c.getStringCellValue());
            	}
            }
		}*/
	}

	
	public List<User> getUsers() {
		return users;
	}

	public void setCompanyDao(CompanyDaoInterface companyDao) {
		this.companyDao = companyDao;
	}
}