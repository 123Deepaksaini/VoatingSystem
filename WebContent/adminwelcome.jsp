<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="MyDb"%>
<html>
<head>
<body style="background-image:url(images/flag.jpg)"/>
<style>
img {
  border-radius: 50%;
}
</style>
</head>
<title>Online Voting System</title>
    <%
  
   String s1 = (String)session.getAttribute("adminname");
   
    Map<String, Integer> counts = new HashMap<String, Integer>();
    MyDb db = new MyDb();
    Connection con = db.getCon();
    if (con == null) {
        out.println("<h3 style='color:white;'>Database connection failed. Check Render environment variables.</h3>");
        return;
    }
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("select partie,count(partie) as c from vote group by partie");
    while(rs.next()){
        counts.put(rs.getString("partie"), rs.getInt("c"));
    }
    rs.close();
    stmt.close();
    con.close();
    
    int cpiCount = counts.containsKey("cpi") ? counts.get("cpi") : 0;
    int congressCount = counts.containsKey("congrace") ? counts.get("congrace") : 0;
    int bspCount = counts.containsKey("bsp") ? counts.get("bsp") : 0;
    int bjpCount = counts.containsKey("bjp") ? counts.get("bjp") : 0;
    int aapCount = counts.containsKey("app") ? counts.get("app") : 0;
    
  
    
    
    %>

    <br>
    <br>
    <br>
   <div style="padding-left: 1200px;">
          
    <a href="adminlogout.jsp"><font size=5 color="red">Logout</font></a>
  </div>
    
        
            <div style="padding-left: 150px;">
             <p> <font size=5 color="white">Voting Results</font></p>
              <img src="images\cpi.jpg" height="70px" width="70px"/><%=cpiCount%>
           
             <br>
             <br>
             
        <img src="images\congrace.png" height="70px" width="70px"/><%=congressCount%>
       
             <br>
             <br>
             <img src="images\bsp.jpg" height="70px" width="70px"/><%=bspCount%>
             
              <br>
              <br>

             <img src="images\bjp.jpg" height="70px" width="70px"/><%=bjpCount%>
            
              <br>
              <br>
              
              <img src="images\Aap.jpg" height="70px" width="70px" /><%=aapCount%>
             <br>
             <br>
             
        
     
  </div>
   
    
   
  
</body>


</html> 
