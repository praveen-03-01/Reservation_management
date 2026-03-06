import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/ReservationServlet")
public class ReservationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String checkIn = request.getParameter("check_in");
        String checkOut = request.getParameter("check_out");
        String roomType = request.getParameter("room_type");

        double price = 0;

        // Room price calculation
        if(roomType.equals("Single")) price = 5000;
        else if(roomType.equals("Double")) price = 8000;
        else if(roomType.equals("Suite")) price = 12000;

        LocalDate inDate = LocalDate.parse(checkIn);
        LocalDate outDate = LocalDate.parse(checkOut);
        long days = ChronoUnit.DAYS.between(inDate, outDate);
        if(days <= 0) days = 1;

        double total = days * price;

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO reservations(full_name,email,phone,check_in,check_out,room_type,price,total) VALUES(?,?,?,?,?,?,?,?)"
            );

            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setDate(4, java.sql.Date.valueOf(inDate));
            ps.setDate(5, java.sql.Date.valueOf(outDate));
            ps.setString(6, roomType);
            ps.setDouble(7, price);
            ps.setDouble(8, total);

            ps.executeUpdate();

            con.close();

            // Redirect to view all reservations
            response.sendRedirect("ViewReservationServlet");

        } catch (Exception e) {
            e.printStackTrace();
            // Optional: show error page
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}