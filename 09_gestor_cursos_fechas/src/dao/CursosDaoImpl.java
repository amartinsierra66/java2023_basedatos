package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Curso;
import static helpers.ConnectionLocator.getConnection;
public class CursosDaoImpl implements CursosDao {
	
	
	@Override
	public boolean existeCurso(int idCurso) {
		try(Connection con=getConnection()){
			String sql="select * from cursos where idCurso=?";
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, idCurso);
			ResultSet rs=ps.executeQuery();
			return rs.next();
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean guardarCurso(Curso curso) {
		try(Connection con=getConnection()){
			String sql="insert into cursos (idCurso,curso,duracion,precio,fechaInicio) values (?,?,?,?,?)";
			//se crea el preparestatement a partir de la sql
			PreparedStatement ps=con.prepareStatement(sql);
			//sustituimos parámetros por valores
			ps.setInt(1, curso.getIdCurso());
			ps.setString(2, curso.getCurso());
			ps.setInt(3, curso.getDuracion());
			ps.setDouble(4, curso.getPrecio());
			//tranformamos el objeto LocalDate en un java.sql.Date
			ps.setDate(5, Date.valueOf(curso.getFechaInicio()));
			ps.execute();
			return true;
		}
		catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	@Override
	public List<Curso> cursos(){
		List<Curso> cursos=new ArrayList<>();
		try(Connection con=getConnection()){				
			String sql="select * from cursos";
			PreparedStatement ps=con.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				cursos.add(new Curso(rs.getInt("idCurso"),
							rs.getString("curso"),
							rs.getInt("duracion"),
							rs.getDouble("precio"),
							rs.getDate("fechaInicio").toLocalDate(),
							null  //lista de alumnos nula
						));
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();		
		}
		return cursos;
	}
	@Override
	public List<Curso> cursosFechas(LocalDate f1, LocalDate f2) {
		List<Curso> cursos=new ArrayList<>();
		try(Connection con=getConnection()){				
			String sql="select * from cursos where fechaInicio between ? and ?";
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setDate(1, Date.valueOf(f1));
			ps.setDate(2, Date.valueOf(f2));
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				cursos.add(new Curso(rs.getInt("idCurso"),
							rs.getString("curso"),
							rs.getInt("duracion"),
							rs.getDouble("precio"),
							rs.getDate("fechaInicio").toLocalDate(),
							null  //lista de alumnos nula
						));
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();		
		}
		return cursos;
	}
}
