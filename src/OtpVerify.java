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
import javax.servlet.http.HttpSession;

@WebServlet("/OtpVerify")
public class OtpVerify extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String enteredOtp = request.getParameter("entered_otp");

        
        if (enteredOtp == null || enteredOtp.trim().isEmpty()) {
            out.println("Please enter the OTP.");
            return;
        }

        HttpSession session = request.getSession();
        String sessionOtp = (String) session.getAttribute("otp");
        String voterCardNumber = (String) session.getAttribute("voter_card_number");

        if (sessionOtp == null || voterCardNumber == null) {
            out.println("Session expired. Please try again.");
            return;
        }

        if (enteredOtp.equals(sessionOtp)) {
            
            MyDb db = new MyDb();
            Connection con = null;
            PreparedStatement pstmt = null;

            try {
                con = db.getCon();
                if (con == null) {
                    out.println("Database connection failed.");
                    return;
                }

                String sql = "INSERT INTO temp_voter_card_number(voter_card_number) VALUES (?)";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, voterCardNumber);
                pstmt.executeUpdate();

               
                session.removeAttribute("otp");
                session.removeAttribute("voter_card_number");

                
                response.sendRedirect("welcome.jsp");

            } catch (SQLException ex) {
                Logger.getLogger(OtpVerify.class.getName()).log(Level.SEVERE, null, ex);
                out.println("An error occurred while processing your request.");
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (con != null) con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(OtpVerify.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            out.println("Invalid OTP. Please try again.");
        }
    }
}