import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE username=? AND password=?";
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            rs = ps.executeQuery();

            if (rs.next()) {
                // Login success
                response.sendRedirect("home_dash.html");
            } else {
                // Login failed
               response.sendRedirect(request.getContextPath() + "/index.html?msg=invalid");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.html");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}