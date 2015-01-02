package hr.fer.ztel.hmo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class Utility {
	
	public static ProblemInstance readInstanceFromFile(String filename) {
		ProblemInstance instance = null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
					new BufferedInputStream(
					new FileInputStream(filename)),"UTF-8"));
			
			String line = null;
			int usersNum = Integer.parseInt(br.readLine());
			int warehousesNum = Integer.parseInt(br.readLine());
			instance = new ProblemInstance(usersNum, warehousesNum);
			
			br.readLine();  // blank line
			
			for (int i = 0; i < warehousesNum; ++i) {
				line = br.readLine().trim();
				String[] data = line.split("\\s+");
				int x = Integer.parseInt(data[0]);
				int y = Integer.parseInt(data[1]);
				instance.addWarehouse(new Warehouse(i, x, y));
			}
			
			br.readLine();  // blank line
			
			for (int i = 0; i < usersNum; ++i) {
				line = br.readLine().trim();
				String[] data = line.split("\\s+");
				int x = Integer.parseInt(data[0]);
				int y = Integer.parseInt(data[1]);
				instance.addUser(new User(i, x, y));
			}
			
			br.readLine();  // blank line

			int vehicleCapacity = Integer.parseInt(br.readLine());
			instance.setVehicleCapacity(vehicleCapacity);
			
			br.readLine();  // blank line
			
			for (int i = 0; i < warehousesNum; ++i) {
				int capacity = Integer.parseInt(br.readLine());
				instance.getWarehouses().get(i).setCapacity(capacity);
				instance.getWarehouses().get(i).setRemainingCapacity(capacity);
			}
			
			br.readLine();  // blank line
			
			for (int i = 0; i < usersNum; ++i) {
				int demand = Integer.parseInt(br.readLine());
				instance.getUsers().get(i).setDemand(demand);
			}
			
			br.readLine();  // blank line
			for (int i = 0; i < warehousesNum; ++i) {
				int openingCost = Integer.parseInt(br.readLine());
				instance.getWarehouses().get(i).setOpeningCost(openingCost);
			}
			
			br.readLine();
			int vehicleCost = Integer.parseInt(br.readLine());
			instance.setVehicleCost(vehicleCost);
			
			br.close();
			
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public static void writeToFile(String filename, Solution sol) {
		try {
			Writer bw = new BufferedWriter(
					new OutputStreamWriter(
					new BufferedOutputStream(
					new FileOutputStream(filename)),"UTF-8"));
			bw.write(sol.toString());
			bw.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
