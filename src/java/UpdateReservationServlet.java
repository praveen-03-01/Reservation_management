import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/UpdateReservationServlet")
public class UpdateReservationServlet extends HttpServlet {

    // SHOW FORM
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM reservations WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Update Reservation</title>");

                // SIMPLE CSS
                out.println("<style>");
                out.println("body{font-family:Arial Black; background:#f4f4f4;}");
                out.println(".box{width:400px; margin:60px auto; background:white; padding:20px; border:1px solid #ccc;}");
                out.println("h2{text-align:center;}");
                out.println("input[type=text], input[type=email]{width:100%; padding:8px; margin:8px 0;}");
                out.println("input[type=submit]{padding:8px 15px; background:#4CAF50; color:white; border:none;}");
                out.println("a{display:block; margin-top:10px; text-align:center;}");
                out.println("</style>");

                out.println("</head>");
                out.println("<body>");

                out.println("<div class='box'>");
                out.println("<h2>Update Reservation</h2>");
                out.println("<form method='post'>");

                out.println("<input type='hidden' name='id' value='" + id + "'>");

                out.println("Name:");
                out.println("<input type='text' name='full_name' value='" 
                        + rs.getString("full_name") + "' required>");

                out.println("Email:");
                out.println("<input type='email' name='email' value='" 
                        + rs.getString("email") + "' required>");

                out.println("Phone:");
                out.println("<input type='text' name='phone' value='" 
                        + rs.getString("phone") + "' required>");

                out.println("Room Type:");
                out.println("<input type='text' name='room_type' value='" 
                        + rs.getString("room_type") + "' required>");

                out.println("<input type='submit' value='Update'>");
                out.println("</form>");

                out.println("<a href='ViewReservationServlet'>Back</a>");
                out.println("</div>");

                out.println("</body>");
                out.println("</html>");
            }

            con.close();

        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }

    // UPDATE DATABASE
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("full_name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String roomType = request.getParameter("room_type");

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE reservations SET full_name=?, email=?, phone=?, room_type=? WHERE id=?");

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, roomType);
            ps.setInt(5, id);

            ps.executeUpdate();
            con.close();

            response.sendRedirect("ViewReservationServlet");

        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}