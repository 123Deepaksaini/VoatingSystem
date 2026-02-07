

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Register")
public class Register extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String voter_card_number = request.getParameter("voter_card_number");
		String address = request.getParameter("address");
		String dob = request.getParameter("dob");
		String contact = request.getParameter("contact");
		String email = request.getParameter("email");

		
		if (name == null || name.trim().isEmpty() ||
		    surname == null || surname.trim().isEmpty() ||
		    voter_card_number == null || voter_card_number.trim().isEmpty() ||
		    address == null || address.trim().isEmpty() ||
		    dob == null || dob.trim().isEmpty() ||
		    contact == null || contact.trim().isEmpty() ||
		    email == null || email.trim().isEmpty()) {
		    out.println("All fields are required.");
		    return;
		}

		MyDb db = new MyDb();
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
		    con = db.getCon();
		    if (con == null) {
		        out.println("Database connection failed.");
		        return;
		    }

		    String sql = "INSERT INTO voter_register(name, surname, voter_card_number, contact, address, dob, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
		    pstmt = con.prepareStatement(sql);
		    pstmt.setString(1, name);
		    pstmt.setString(2, surname);
		    pstmt.setString(3, voter_card_number);
		    pstmt.setString(4, contact);
		    pstmt.setString(5, address);
		    pstmt.setString(6, dob);
		    pstmt.setString(7, email);

		    int rowsAffected = pstmt.executeUpdate();
		    if (rowsAffected > 0) {
		        response.sendRedirect("index.jsp");
		    } else {
		        out.println("Registration failed.");
		    }

		} catch (SQLException ex) {
		    Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
		    out.println("An error occurred during registration.");
		} finally {
		    try {
		        if (pstmt != null) pstmt.close();
		        if (con != null) con.close();
		    } catch (SQLException ex) {
		        Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
	}
}


