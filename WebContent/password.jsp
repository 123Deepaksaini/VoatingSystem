<!DOCTYPE html>
<html>
    <head>
<title>Online Voting System</title>
<body style="background-image:url(images/flag.jpg)"/>

</head>




<%
    // OTP sent via SMS - not displayed for security
%>

        <br><br><br><br><br>

          <div style="padding-left: 150px;">

            <form action="OtpVerify" method="post">
            <h3><font color="white">Enter Your OTP</font></h3>

             <br>



             <input name="entered_otp"  placeholder="Enter your OTP" type="text" required>
                         <h3><font color="white">OTP has been sent to your registered phone number</font></h3>
             <br>
                       <input value="Verify OTP"   type="submit"  class="btn" style="padding-bottom: 10px; width: 120px;">


            </form>



    </div>




</body>

</html>
