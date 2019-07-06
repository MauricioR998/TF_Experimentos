package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import model.Producto;
import model.Categoria;
import model.Conexion;

public class ProductoDAO {
	private Conexion con;
	private Connection connection;
	private Client client;
	private WebTarget target;
 
	public ProductoDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) throws SQLException {
		con = new Conexion(jdbcURL, jdbcUsername, jdbcPassword);
		client = ClientBuilder.newClient();
	}
	
	public boolean ValidacionLetras(String cad) {
		boolean num = false;
		boolean str = false;
		boolean ex = false;
		for(int i = 0;i<cad.length();i++) {
			if(Character.isDigit(cad.charAt(i)) == true) {
				num = true;
			}else if (Character.isLetter(cad.charAt(i)) == true){
				str = true;
			}else { 
				ex = true;
			}
			
		}		
		if (num == false && str == true && ex == false) {
			return true;
		}else {
			return false;
		}
	}
 
	public boolean insertar(Producto producto) throws SQLException {		
		boolean registrar = false;
		if(producto.getNombre().equals("") || producto.getCategoria() == 0 || ValidacionLetras(producto.getNombre()) == false) {
			return registrar;
		}else {
		String sql = "INSERT INTO producto values (NULL,'"+producto.getNombre()+"','"+producto.getCategoria()+"','"+producto.getCodigo()+"')";
		try {
			con.conectar();
			connection = con.getJdbcConnection();
			Statement stm= connection.createStatement();
			stm.execute(sql);
			registrar=true;
			stm.close();
			connection.close();
			} catch (SQLException e) {
				System.out.println("Error: m�todo registrar");
				e.printStackTrace();
			}
		return registrar;
		}
	}
	public List<Categoria> listarCategorias() throws SQLException{
		List<Categoria> listaCategoria = new ArrayList<Categoria>();
		String sql = "SELECT * FROM categoria";
		con.conectar();
		connection = con.getJdbcConnection();
		Statement statement = connection.createStatement();
		ResultSet resulSet = statement.executeQuery(sql);
		while (resulSet.next()) {
			int id = resulSet.getInt("id");
			String nombre = resulSet.getString("nombre");
			Categoria categoria = new Categoria(id, nombre );

			listaCategoria.add(categoria);
		}
		con.desconectar();
		return listaCategoria;
	}
 
	public List<Producto> listarProductos() throws SQLException {
		target = client.target("http://ventas-crud-services.herokuapp.com/ListarProductos");
		String response = target.request(MediaType.APPLICATION_JSON).get(String.class);
		
		Gson gson = new Gson();
		List<Producto> productos = gson.fromJson(response, new GenericType<List<Producto>>(){}.getType());
		return productos;
	}
 
	public Producto obtenerPorId(int id) throws SQLException {
		Producto producto = null;
		target = client.target("http://ventas-crud-services.herokuapp.com/ObtenerProducto?id=" + id);
		String response = target.request(MediaType.APPLICATION_JSON).get(String.class);
		
		Gson gson = new Gson();
		producto = gson.fromJson(response, Producto.class); 
		return producto;
	}
	public Producto obtenerPorCodigo(String codigo) throws SQLException {
		Producto producto = null;
 
		String sql = "SELECT * FROM producto WHERE codigo= ? ";
		con.conectar();
		connection = con.getJdbcConnection();
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, codigo);
 
		ResultSet res = statement.executeQuery();
		if (res.next()) {
			producto = new Producto(res.getInt("id"),  res.getString("nombre"),
					res.getInt("idCategoria"),res.getString("codigo"));
		}
		res.close();
		con.desconectar();
 
		return producto;
	}
 
	public boolean actualizar(Producto producto) throws SQLException {
		boolean rowActualizar = false;
		if(producto.getNombre().equals("") || producto.getCategoria() == 0 || ValidacionLetras(producto.getNombre()) == false) {
			return rowActualizar;
		}else {
		String sql = "UPDATE producto SET nombre=?,idCategoria=? WHERE codigo=?";
		con.conectar();
		connection = con.getJdbcConnection();
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, producto.getNombre());
		statement.setInt(2, producto.getCategoria());
		statement.setString(3, producto.getCodigo());
 
		rowActualizar = statement.executeUpdate() > 0;
		statement.close();
		con.desconectar();
		return rowActualizar;
		}
	}
	
	public boolean eliminar(Producto producto) throws SQLException {
		boolean rowEliminar = false;
		String id = Integer.toString(producto.getId());
		System.out.println(id);
		target = client.target("http://ventas-crud-services.herokuapp.com/BorrarProducto?id=" + id);
		String response =target.request().delete(String.class);
		/*String sql = "DELETE FROM producto WHERE codigo=?";
		con.conectar();
		connection = con.getJdbcConnection();
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, producto.getCodigo());
 
		rowEliminar = statement.executeUpdate() > 0;
		statement.close();
		con.desconectar();*/
 
		return rowEliminar;
	}
}