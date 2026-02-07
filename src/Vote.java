

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

@WebServlet("/Vote")
public class Vote extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String voter_card_number = request.getParameter("voter_card_number");
		String parties = request.getParameter("parties");

		
		if (voter_card_number == null || voter_card_number.trim().isEmpty() ||
		    parties == null || parties.trim().isEmpty()) {
		    out.println("Voter card number and party selection are required.");
		    return;
		}

		MyDb db = new MyDb();
		Connection con = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;

		try {
		    con = db.getCon();
		    if (con == null) {
		        out.println("Database connection failed.");
		        return;
		    }

		    
		    System.out.println("Voter Card: " + voter_card_number);
		    System.out.println("Party: " + parties);

		    
		    String checkSql = "SELECT voter_card_number FROM temp_voter_card_number WHERE voter_card_number = ?";
		    PreparedStatement checkStmt = con.prepareStatement(checkSql);
		    checkStmt.setString(1, voter_card_number);
		    ResultSet rs = checkStmt.executeQuery();

		    if (!rs.next()) {
		        
		        String sql1 = "INSERT INTO temp_voter_card_number(voter_card_number) VALUES (?)";
		        pstmt1 = con.prepareStatement(sql1);
		        pstmt1.setString(1, voter_card_number);
		        pstmt1.executeUpdate();
		    }

		    if (rs != null) rs.close();
		    if (checkStmt != null) checkStmt.close();

		   
		    String sql2 = "INSERT INTO vote(voter_card_number, partie) VALUES (?, ?)";
		    pstmt2 = con.prepareStatement(sql2);
		    pstmt2.setString(1, voter_card_number);
		    pstmt2.setString(2, parties);
		    pstmt2.executeUpdate();

		    response.sendRedirect("index.jsp");

		} catch (SQLException ex) {
		    Logger.getLogger(Vote.class.getName()).log(Level.SEVERE, null, ex);
		    out.println("An error occurred while voting: " + ex.getMessage());
		    ex.printStackTrace();
		} finally {
		    try {
		        if (pstmt1 != null) pstmt1.close();
		        if (pstmt2 != null) pstmt2.close();
		        if (con != null) con.close();
		    } catch (SQLException ex) {
		        Logger.getLogger(Vote.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
	}
}

