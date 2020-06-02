import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import ws.WebServiceWS_Service;

@WebServlet(urlPatterns = {"/ServletSaludar"})
public class ServletSaludar extends HttpServlet {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/TServicio/Web_Service_WS.wsdl")
    private WebServiceWS_Service service;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletSaludar</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletSaludar at " + request.getContextPath() + "</h1>");
            out.println(request.getParameter("saludo"));
            out.println(hello(request.getParameter("saludo")));
            out.println("</body>");
            out.println("</html>");
        }
    }

   

    private String hello(java.lang.String name) {
        
        ws.WebServiceWS port = service.getWebServiceWSPort();
        return port.hello(name);
    }

}
