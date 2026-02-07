
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/Password")

public class Password extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String voter_card_number = request.getParameter("voter_card_number");

        
        if (voter_card_number == null || voter_card_number.trim().isEmpty()) {
            out.println("Please enter a valid voter card number.");
            return;
        }

        MyDb db = new MyDb();
        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            con = db.getCon();
            if (con == null) {
                out.println("Database connection failed.");
                return;
            }

            
            String sql1 = "SELECT voter_card_number FROM temp_voter_card_number WHERE voter_card_number = ?";
            pstmt1 = con.prepareStatement(sql1);
            pstmt1.setString(1, voter_card_number);
            rs1 = pstmt1.executeQuery();

            if (rs1.next()) {
                response.sendRedirect("votercheck.jsp?name=" + voter_card_number);
                return;
            }

            
            String sql2 = "SELECT uid, contact FROM voter_register WHERE voter_card_number = ?";
            pstmt2 = con.prepareStatement(sql2);
            pstmt2.setString(1, voter_card_number);
            rs2 = pstmt2.executeQuery();

            if (rs2.next()) {
                String contact = rs2.getString("contact");

                
                Random random = new Random();
                int otp = 100000 + random.nextInt(900000); 

                
                HttpSession session = request.getSession();
                session.setAttribute("otp", String.valueOf(otp));
                session.setAttribute("voter_card_number", voter_card_number);

                
                try {
                    sendSMS(contact, "Your OTP for voting is: " + otp);
                } catch (Exception e) {
                    Logger.getLogger(Password.class.getName()).log(Level.SEVERE, "SMS sending failed", e);
                    
                }

                
                request.setAttribute("contact", contact);
                request.getRequestDispatcher("password.jsp").forward(request, response);
            } else {
                out.println("Please enter a valid voter card number. You may need to register first.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
            out.println("An error occurred while processing your request.");
        } finally {
            try {
                if (rs1 != null) rs1.close();
                if (rs2 != null) rs2.close();
                if (pstmt1 != null) pstmt1.close();
                if (pstmt2 != null) pstmt2.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Password.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    private void sendSMS(String phoneNumber, String message) throws Exception {

        String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
        String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
        String FROM_NUMBER = System.getenv("TWILIO_FROM_NUMBER");

        
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+91" + phoneNumber;
        }

        
        String url = "https://api.twilio.com/2010-04-01/Accounts/" + ACCOUNT_SID + "/Messages.json";

        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        
        String auth = ACCOUNT_SID + ":" + AUTH_TOKEN;
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        con.setRequestProperty("Authorization", "Basic " + encodedAuth);

        
        String urlParameters = "From=" + URLEncoder.encode(FROM_NUMBER, "UTF-8") +
                             "&To=" + URLEncoder.encode(phoneNumber, "UTF-8") +
                             "&Body=" + URLEncoder.encode(message, "UTF-8");

        
        con.setDoOutput(true);
        con.getOutputStream().write(urlParameters.getBytes("UTF-8"));

        
        Scanner scanner = new Scanner(con.getInputStream());
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();

        System.out.println("SMS sent successfully to " + phoneNumber);
    }
}

    

