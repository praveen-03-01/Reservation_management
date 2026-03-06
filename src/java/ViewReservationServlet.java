import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ViewReservationServlet")
public class ViewReservationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<title>Ocean View Resort - Reservations</title>");
        out.println("<style>");
        out.println("body{font-family:Arial Black;background:#f2f2f2;text-align:center;}");
        out.println("h2{color:#004080;}");
        out.println("table{margin:auto;border-collapse:collapse;width:95%;background:white;}");
        out.println("th,td{border:1px solid #ddd;padding:8px;}");
        out.println("th{background:#547751;color:white;}");
        out.println("a{text-decoration:none;margin:5px;color:blue;}");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h2>Ocean View Resort</h2>");
        out.println("<h2>Reservation List</h2>");

        out.println("<table>");
        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>Name</th>");
        out.println("<th>Email</th>");
        out.println("<th>Phone</th>");
        out.println("<th>Room Type</th>");
        out.println("<th>Check-In</th>");
        out.println("<th>Check-Out</th>");
        out.println("<th>Total</th>");
        out.println("<th>Action</th>");
        out.println("</tr>");

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reservations");

            while (rs.next()) {

                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("full_name") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("phone") + "</td>");
                out.println("<td>" + rs.getString("room_type") + "</td>");
                out.println("<td>" + rs.getDate("check_in") + "</td>");
                out.println("<td>" + rs.getDate("check_out") + "</td>");
                out.println("<td>Rs. " + rs.getDouble("total") + "</td>");
                out.println("<td>");
                out.println("<a href='ViewBillServlet?id=" + rs.getInt("id") + "'>View Bill</a>");
                out.println("<a href='UpdateReservationServlet?id=" + rs.getInt("id") + "'>Update</a>");
                out.println("<a href='DeleteReservationServlet?id=" + rs.getInt("id") + "' ");
                out.println("onclick='return confirm(\"Are you sure?\")'>Delete</a>");
                out.println("</td>");
                out.println("</tr>");
            }

            con.close();

        } catch (Exception e) {
            out.println("<tr><td colspan='9'>Error: " + e.getMessage() + "</td></tr>");
        }

        out.println("</table>");
        out.println("<br><br>");
        out.println("<a href='home_dash.html'>Back to Home</a>");
        out.println("</body></html>");
    }
}