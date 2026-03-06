import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/ViewBillServlet")
public class ViewBillServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Reservation Bill</title>");
        out.println("<style>");
        out.println("body{font-family:Arial Black; background:#f4f4f4; text-align:center;}");
        out.println(".bill-container{background:white; width:50%; margin:auto; padding:20px; border-radius:8px; box-shadow:0px 0px 10px #ccc;}");
        out.println("h2{color:#007bff;}");
        out.println("table{width:100%; border-collapse:collapse; margin-top:10px;}");
        out.println("td,th{padding:10px; border:1px solid #ddd;}");
        out.println(".print-btn{margin-top:20px; padding:10px 20px; background:#007bff; color:white; border:none; border-radius:5px; cursor:pointer;}");
        out.println(".print-btn:hover{background:#0056b3;}");
        out.println("</style>");
        out.println("<script>");
        out.println("function printBill(){ window.print(); }");
        out.println("</script>");
        out.println("</head>");
        out.println("<body>");

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reservations WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                String roomType = rs.getString("room_type");
                LocalDate checkIn = rs.getDate("check_in").toLocalDate();
                LocalDate checkOut = rs.getDate("check_out").toLocalDate();

                long days = ChronoUnit.DAYS.between(checkIn, checkOut);
                if(days <= 0) days = 1; // minimum 1 day

                // Calculate price per night based on room type
                double price = 0;
                if(roomType.equalsIgnoreCase("Single")) price = 5000;
                else if(roomType.equalsIgnoreCase("Double")) price = 8000;
                else if(roomType.equalsIgnoreCase("Suite")) price = 12000;

                double total = days * price;
                double tax = total * 0.10; // 10% tax
                double finalTotal = total + tax;

                out.println("<div class='bill-container'>");
                out.println("<h2>Reservation Bill</h2>");
                out.println("<table>");
                out.println("<tr><th>Field</th><th>Details</th></tr>");
                out.println("<tr><td>Reservation ID</td><td>" + rs.getInt("id") + "</td></tr>");
                out.println("<tr><td>Name</td><td>" + rs.getString("full_name") + "</td></tr>");
                out.println("<tr><td>Email</td><td>" + rs.getString("email") + "</td></tr>");
                out.println("<tr><td>Phone</td><td>" + rs.getString("phone") + "</td></tr>");
                out.println("<tr><td>Room Type</td><td>" + roomType + "</td></tr>");
                out.println("<tr><td>Check In</td><td>" + checkIn + "</td></tr>");
                out.println("<tr><td>Check Out</td><td>" + checkOut + "</td></tr>");
                out.println("<tr><td>Days</td><td>" + days + "</td></tr>");
                out.println("<tr><td>Price Per Night</td><td>Rs. " + price + "</td></tr>");
                out.println("<tr><td>Total</td><td>Rs. " + total + "</td></tr>");
                out.println("<tr><td>Tax (10%)</td><td>Rs. " + tax + "</td></tr>");
                out.println("<tr><td><b>Final Total</b></td><td><b>Rs. " + finalTotal + "</b></td></tr>");
                out.println("</table>");
                out.println("<button class='print-btn' onclick='printBill()'>Print Bill</button>");
                out.println("<br><br><a href='ViewReservationServlet'>Back to Reservations</a>");
                out.println("</div>");

            } else {
                out.println("<h3>Reservation not found!</h3>");
                out.println("<a href='ViewReservationServlet'>Back to Reservations</a>");
            }

            con.close();
        } catch(Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}