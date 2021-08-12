import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RainfallReport {

	// Write the required business logic as expected in the question description
	public List<AnnualRainfall> generateRainfallReport(String filePath) throws FileNotFoundException {
		List<AnnualRainfall> arfList = new ArrayList<AnnualRainfall>();
		File f = new File(filePath);
		Scanner dataReader = new Scanner(f);
		while (dataReader.hasNextLine()) {
			String fileData = dataReader.nextLine();
			String[] stringsplitter = fileData.split(",");
			try {
				AnnualRainfall arf = new AnnualRainfall();
				validate(stringsplitter[0]);
				double[] monthlyRainfall = new double[12];
				for (int i = 0; i < 12; i++) {
					monthlyRainfall[i] = Double.parseDouble(stringsplitter[i + 2]);
				}
				arf.setCityPincode(Integer.parseInt(stringsplitter[0]));
				arf.setCityName(stringsplitter[1]);
				arf.calculateAverageAnnualRainfall(monthlyRainfall);

				arfList.add(arf);

			} catch (InvalidCityPincodeException e) {
				System.out.println(e.getMessage());
			} finally {
				dataReader.close();
			}
		}
		return arfList;
	}

	public List<AnnualRainfall> findMaximumRainfallCities() {

		List<AnnualRainfall> alist = new ArrayList<AnnualRainfall>();
		try {
			Connection con = DBHandler.establishConnection();
			String sql = "select * from Annualrainfall where average_annual_rainfall in(Select max(average_annual_rainfall) from Annual Rainfall)";

			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int pincode = rs.getInt(1);
				String name = rs.getString(2);
				double avgRainfall = rs.getDouble(3);

				AnnualRainfall arf = new AnnualRainfall();
				arf.setCityPincode(pincode);
				arf.setCityName(name);
				arf.setAverageAnnualRainfall(avgRainfall);

				alist.add(arf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alist;
	}

	public boolean validate(String cityPincode) throws InvalidCityPincodeException {
		if (cityPincode.matches("[1-9]{1}[0-9]{4})"))
			return true;
		else
			throw new InvalidCityPincodeException("Invalid City Pin Code");
	}

}
