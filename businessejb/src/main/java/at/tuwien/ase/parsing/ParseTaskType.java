package at.tuwien.ase.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import at.tuwien.ase.model.TaskType;
import at.tuwien.ase.model.WorkingObject;

import com.google.common.collect.Lists;


public class ParseTaskType {
	
	private boolean ttTypes = false;
	private boolean woObject = false;
	
	private HashMap<String, TaskType> taskTypes = new HashMap<String, TaskType>();
	private HashMap<String, WorkingObject> woS = new HashMap<String, WorkingObject>();
	
	private TaskType tt = null;
	private WorkingObject wo = null;
	
	private static ParseTaskType ptt;

	
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
	 * of corresponding objects, i.e., TaskType or WorkingObject.
	 * 
	 * @param filename - a file to parse.
	 * 
	 * @return - true if the file is parsed, false if an exception occurred during the parsing.
	 */
	public boolean processFile(InputStream filename) {
		
		try {
			
			if (taskTypes != null)
				taskTypes.clear();
			
			if (woS != null)
				woS.clear();
			
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(filename);
            
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            
            while (rowIterator.hasNext()) {
            	
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                
                int i = 0;
                String rowData = ""; 
                boolean invalidData = false;
                
                List<Cell> cells = Lists.newArrayList(cellIterator);
                
                for (i = 0; i < 2; i++) {
      
                	Cell cell;
                	
                	if (cells.size() > 1) 
                		cell = cells.get(i);
                	else
                		break;
                    
                    switch (cell.getCellType()) {
                    
                    	case Cell.CELL_TYPE_NUMERIC:
                    	
                    		int valueI = new Double(cell.getNumericCellValue()).intValue();
                    		rowData += valueI  + " ";
                    		
                    		break;
                        
                    	case Cell.CELL_TYPE_STRING:
                        	
                        	String value = cell.getStringCellValue();
                      
                        	if (i == 0) {
                        
                        		if (Character.isDigit(value.charAt(0)) || value.charAt(0) == '0')
                        			rowData += value  + " ";
                        		else if (value.equals("Zwischensumme")) {
                        			//System.out.println("Duration: " + cells.get(2).getNumericCellValue());
                        			rowData += "Duration: " + cells.get(2).getNumericCellValue()  + " ";
                        		} else
                        			invalidData = true;
                        	
                        	} else if (!invalidData) {
                        		rowData += cell.getStringCellValue() + " ";
                        	}
                        
                            break;
                            
                        default:
                        	
                        	invalidData = true;
                        	break;
                    }
                }
                
                if (!rowData.equals("")) {
                	//System.out.println(rowData);
                	processRow(rowData);
                }
            }
            
            filename.close();
        }
        catch (Exception e) {
        	//e.printStackTrace();
            return false;
        }
		
		return true;
	}
	
	/**
	 * Method processes each row in the file. The method creates either a TaskType or
	 * a WorkingObject out of the processed row.
	 * 
	 * @param row
	 */
	private void processRow(String row) {
		
		if (Character.isDigit(row.charAt(0)) && Character.isDigit(row.charAt(1)) && Character.isDigit(row.charAt(2))
				&& Character.isDigit(row.charAt(3)) && woObject) {
			
			wo = new WorkingObject();
			wo.setWoNumber(row.substring(0, row.indexOf(" ")));
			wo.setDescription(row.substring(row.indexOf(" ") + 1));
			
			//System.out.println("WO: " + wo.getWoNumber() + " -- " + wo.getDescription());
			woS.put(wo.getWoNumber(), wo);
			
		} else if (row.startsWith("Duration") && ttTypes) {
			
			tt.setExpectedWorkHours(new Double(row.substring(row.indexOf(" ") + 1)));
			//System.out.println("TT: " + tt.getTaskNumber() + " -- " + tt.getName() + " -- " + tt.getExpectedWorkHours());
			
			taskTypes.put(tt.getTaskNumber(), tt);
			
		} else if (Character.isDigit(row.charAt(0)) && Character.isDigit(row.charAt(1)) && ttTypes) {
			
			//System.out.println("START TT");
			tt = new TaskType();
			tt.setTaskNumber(row.substring(0, row.indexOf(" ")));
			tt.setDescription(row.substring(row.indexOf(" ") + 1));
			tt.setName(tt.getDescription().trim());
		} 
	}

	public HashMap<String, TaskType> getTaskTypes() {
		return taskTypes;
	}

	public HashMap<String, WorkingObject> getWoS() {
		return woS;
	}

	public boolean isTtTypes() {
		return ttTypes;
	}

	public void setTtTypes(boolean ttTypes) {
		this.ttTypes = ttTypes;
	}

	public boolean isWoObject() {
		return woObject;
	}

	public void setWoObject(boolean woObject) {
		this.woObject = woObject;
	}
}