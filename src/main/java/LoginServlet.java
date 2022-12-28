
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import javax.naming.ldap.Rdn;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		RequestDispatcher rd = null;
		PrintWriter out = response.getWriter();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql:///atm_system", "root", "admin");
			String phone = request.getParameter("phone");
			String pin = request.getParameter("pin");
			HttpSession session = request.getSession();

			/*
			 * if(session.getAttribute("phone")==null) {
			 * response.sendRedirect("login.html"); }
			 */

			PreparedStatement ps = con.prepareStatement("select phone from customer where phone=? and pin=?");
			ps.setString(1, phone);
			ps.setString(2, pin);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
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
				rd = request.getRequestDispatcher("login.html");
				rd.forward(request, response);
			}

			/* rd.forward(request, response); */

		} 
		catch (ClassNotFoundException e) {

			e.printStackTrace();
		} 
		catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
