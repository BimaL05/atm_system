
import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


@WebServlet("login_fingerprint")
public class Login_Fingerprint_Servlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	//@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		RequestDispatcher rd = null;
//		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
		// Initially assigning null
		BufferedImage imgA = null;
		BufferedImage imgB = null;

		// Try block to check for exception
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql:///atm_system", "root", "admin");
			String phone = request.getParameter("phone");
			HttpSession session = request.getSession();
			
			PreparedStatement ps = con.prepareStatement("select phone from customer where phone=?");
			ps.setString(1, phone);
			ResultSet rs = ps.executeQuery();
			
			// Reading file from local directory by
			// creating object of File class
			File fileA
				= new File("C:/Users/bimal/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/atm_system/images/fingerprint.jpg");
			File fileB
				= new File("E:/Fingerprint based ATM System/fingerprint.jpg");
			
			// Reading files
			imgA = ImageIO.read(fileA);
			imgB = ImageIO.read(fileB);
			
			// Assigning dimensions to image
			int width1 = imgA.getWidth();
			int width2 = imgB.getWidth();
			int height1 = imgA.getHeight();
			int height2 = imgB.getHeight();
			
			long difference = 0;
			
			// treating images likely 2D matrix
			
			// Outer loop for rows(height)
			for (int y = 0; y < height1; y++) {
			
				// Inner loop for columns(width)
				for (int x = 0; x < width1; x++) {
			
					int rgbA = imgA.getRGB(x, y);
					int rgbB = imgB.getRGB(x, y);
					int redA = (rgbA >> 16) & 0xff;
					int greenA = (rgbA >> 8) & 0xff;
					int blueA = (rgbA)&0xff;
					int redB = (rgbB >> 16) & 0xff;
					int greenB = (rgbB >> 8) & 0xff;
					int blueB = (rgbB)&0xff;
			
					difference += Math.abs(redA - redB);
					difference += Math.abs(greenA - greenB);
					difference += Math.abs(blueA - blueB);
				}
			}
			
			// Total number of red pixels = width * height
			// Total number of blue pixels = width * height
			// Total number of green pixels = width * height
			// So total number of pixels = width * height *
			// 3
			double total_pixels = width1 * height1 * 3;
			
			// Normalizing the value of different pixels
			// for accuracy
			
			// Note: Average pixels per color component
			double avg_different_pixels = difference / total_pixels;
			
			// There are 255 values of pixels in total
			double percentage = (avg_different_pixels / 255) * 100;
			
			// Lastly print the difference percentage
			//System.out.println("Difference Percentage-->" + percentage);
			
			
			if (rs.next())
			{
				if(percentage == 0.0)
				{
					out.print("Welcome");
				}
					session.setAttribute("phone", rs.getString("phone"));
					rd = request.getRequestDispatcher("menu.html");
					rd.forward(request, response);
			}
				
				else {
					/*
					 * //out.print("Wrong!!"); rd=request.getRequestDispatcher("menu.html");
					 * //rd.include(request, response);
					 * 
					 * System.out.println("Login Failed");
					 * System.out.println("<a href=login.html>Try Again!!!</a>");
					 */
					request.setAttribute("status", "failed");
					rd = request.getRequestDispatcher("login_fingerprint.html");
					rd.forward(request, response);
				}
		}

		// Catch block to check for exceptions
		catch (IOException e) {
			System.out.println(e);
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
