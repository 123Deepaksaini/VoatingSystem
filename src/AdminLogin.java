import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AdminLogin")
public class AdminLogin extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		String admin_name = request.getParameter("admin_name");
		String admin_pass = request.getParameter("admin_pass");

		
		if (admin_name == null || admin_name.trim().isEmpty() ||
		    admin_pass == null || admin_pass.trim().isEmpty()) {
		    out.println("Username and password are required.");
		    return;
		}

		MyDb db = new MyDb();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
		    con = db.getCon();
		    if (con == null) {
		        out.println("Database connection failed.");
		        return;
		    }

		    String sql = "SELECT * FROM admin_login WHERE user_name = ? AND password = ?";
		    pstmt = con.prepareStatement(sql);
		    pstmt.setString(1, admin_name);
		    pstmt.setString(2, admin_pass);
		    rs = pstmt.executeQuery();

		    if (rs.next()) {
		        HttpSession session = request.getSession();
		        session.setAttribute("adminname", admin_name);
		        response.sendRedirect("adminwelcome.jsp");
		    } else {
		        response.sendRedirect("wrong.jsp");
		    }

		} catch (SQLException ex) {
		    Logger.getLogger(AdminLogin.class.getName()).log(Level.SEVERE, null, ex);
		    out.println("An error occurred during login.");
		} finally {
		    try {
		        if (rs != null) rs.close();
		        if (pstmt != null) pstmt.close();
		        if (con != null) con.close();
		    } catch (SQLException ex) {
		        Logger.getLogger(AdminLogin.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
	}
}
