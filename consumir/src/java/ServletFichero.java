import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import ws.WebServiceWS_Service;

@WebServlet(urlPatterns = {"/ServletFichero"})
public class ServletFichero extends HttpServlet {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/TServicio/Web_Service_WS.wsdl")
    private WebServiceWS_Service service;
    private static String JDBC_URL = "jdbc:mysql://localhost:3306/prueba";
    private static String JDBC_USER = "root";
    private static String JDBC_PASS = "";
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

        String Archivo="D:\\2.txt";
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletFichero</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletFichero at " + request.getContextPath() + "</h1>");
            out.println(request.getParameter("valor"));
            out.println(getFicheroAdjunto());
            
            String s = new String(getFicheroAdjunto(), StandardCharsets.US_ASCII);
            try (FileOutputStream stream = new FileOutputStream(Archivo)) {
                
                stream.write(getFicheroAdjunto());
            }
            
            out.println("Terminando de crear el archivo");
            PreparedStatement ps;
            ResultSet res;
            String cadena;
            FileReader f = new FileReader(Archivo);
            BufferedReader b = new BufferedReader(f);
            
            out.println("Comenzando la subida");
            
            while((cadena = b.readLine())!=null) {
                
                String[] parts = cadena.split(",");
                String name = parts[0]; // NOMBRES
                String lastP = parts[1]; // APELLIDO PATERNO
                String lastM = parts[2]; // APELLIDO MATERNO
                String dni = parts[3]; // DNI
                

                Connection con = null;

                try {
                    con = getConection();
                    ps = con.prepareStatement("INSERT INTO persona (nombre, apellido_paterno, apellido_materno, dni) VALUES(?,?,?,?)");
                    ps.setString(1, name);
                    ps.setString(2, lastP);
                    ps.setString(3, lastM);
                    ps.setString(4, dni);
                    ps.executeUpdate();
                    
                    int re2 = ps.executeUpdate();
                
                    if(re2 > 0){
                        out.println("EXITO al guardar persona");
                    }else{
                        out.println("ERROR al guardar persona");
                    }
                    
                }catch (Exception e) {  

                }  
            
            }
            b.close();
            out.println("Fin de la subida");
            out.println("</body>");
            out.println("</html>");
        }
       
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private byte[] getFicheroAdjunto() {
        ws.WebServiceWS port = service.getWebServiceWSPort();
        return port.getFicheroAdjunto();
    }

    private String getDirec() {
        ws.WebServiceWS port = service.getWebServiceWSPort();
        return port.getDirec();
    }

    
    public static Connection getConection(){
        Connection con = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            con = (Connection) DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASS);
            
            
            System.out.println("conexion exitosa");
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }   
 
}
