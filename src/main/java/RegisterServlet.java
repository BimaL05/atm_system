import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/register")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50)
public class RegisterServlet extends HttpServlet{
	

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();

        String title=req.getParameter("title");
		String firstname=req.getParameter("firstname");
		String middlename=req.getParameter("middlename");
		String lastname=req.getParameter("lastname");
		String phone=req.getParameter("phone");
		String pin=req.getParameter("pin");
		String accNum=req.getParameter("accNum");
		String accType=req.getParameter("accType");
		String email=req.getParameter("email");
		String gender=req.getParameter("gender");
		String DOB=req.getParameter("DOB");
		String address=req.getParameter("address");
		String address2=req.getParameter("address2");
		String city=req.getParameter("city");
		String state=req.getParameter("state");
		String country=req.getParameter("country");
		String postalCode=req.getParameter("postalCode");

		InputStream inputStream = null; // input stream of the upload file
		Connection con = null;
		
        Part filePart = req.getPart("finger");
        if (filePart != null) {
            // prints out some information for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());
             
            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
        }
         
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_system", "root", "admin");
            PreparedStatement ps = con.prepareStatement("insert into customer values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, title);
			ps.setString(2, firstname);
			ps.setString(3, middlename);
			ps.setString(4, lastname);
			ps.setString(5, phone);
			ps.setString(6, pin);
			ps.setString(7, accNum);
			ps.setString(8, accType);
			ps.setString(9, email);
			ps.setString(10, gender);
			ps.setString(11, DOB);
			ps.setString(12, address);
			ps.setString(13, address2);
			ps.setString(14, city);
			ps.setString(15, state);
			ps.setString(16, country);
			ps.setString(17, postalCode);
			
			if (inputStream != null) {
                // fetches input stream of the upload file for the blob column
                ps.setBlob(18, inputStream);
            }
			
            int count = ps.executeUpdate();
			
			if(count==0) {
				out.println("Record not stored into database");
			}else {
				out.println("Record Stored into Database");
			}
        } catch (Exception e) {
            out.println(e);
        }finally {
            if (con != null) {
                // closes the database connection
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }  
}