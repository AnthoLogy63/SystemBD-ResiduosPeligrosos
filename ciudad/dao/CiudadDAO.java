package ciudad.dao;

import ciudad.modelo.CiudadModel;
import tipo_transporte.conexion.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CiudadDAO {

    public List<CiudadModel> findAll() throws SQLException {
        List<CiudadModel> lista = new ArrayList<>();
        String sql = "SELECT \"CiudCod\", \"CiudNom\", \"CiudEst\", \"RegCod\" " +
                     "FROM public.\"GZZ_CIUDAD\" ORDER BY \"CiudCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new CiudadModel(
                    rs.getString("CiudCod"),
                    rs.getString("CiudNom"),
                    rs.getString("CiudEst"),
                    rs.getString("RegCod")
                ));
            }
        }
        return lista;
    }

    public CiudadModel findById(String codigo) throws SQLException {
        String sql = "SELECT \"CiudCod\", \"CiudNom\", \"CiudEst\", \"RegCod\" " +
                     "FROM public.\"GZZ_CIUDAD\" WHERE \"CiudCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CiudadModel(
                        rs.getString("CiudCod"),
                        rs.getString("CiudNom"),
                        rs.getString("CiudEst"),
                        rs.getString("RegCod")
                    );
                }
            }
        }
        return null;
    }

    public void insert(CiudadModel ciudad) throws SQLException {
        String sql = "INSERT INTO public.\"GZZ_CIUDAD\" (\"CiudCod\", \"CiudNom\", \"CiudEst\", \"RegCod\") VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ciudad.getCodigo());
            ps.setString(2, ciudad.getNombre());
            ps.setString(3, ciudad.getEstado());
            ps.setString(4, ciudad.getRegionCodigo());
            ps.executeUpdate();
        }
    }

    public void update(CiudadModel ciudad) throws SQLException {
        String sql = "UPDATE public.\"GZZ_CIUDAD\" SET \"CiudNom\" = ?, \"CiudEst\" = ?, \"RegCod\" = ? WHERE \"CiudCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ciudad.getNombre());
            ps.setString(2, ciudad.getEstado());
            ps.setString(3, ciudad.getRegionCodigo());
            ps.setString(4, ciudad.getCodigo());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String codigo) throws SQLException {
        String sql = "UPDATE public.\"GZZ_CIUDAD\" SET \"CiudEst\" = '*' WHERE \"CiudCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean inactivate(String codigo) throws SQLException {
        String sql = "UPDATE public.\"GZZ_CIUDAD\" SET \"CiudEst\" = 'I' WHERE \"CiudCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean reactivate(String codigo) throws SQLException {
        String sql = "UPDATE public.\"GZZ_CIUDAD\" SET \"CiudEst\" = 'A' WHERE \"CiudCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    public List<String> getRegionesActivas() throws SQLException {
        List<String> regiones = new ArrayList<>();
        String sql = "SELECT \"RegCod\" FROM public.\"GZZ_REGION\" WHERE \"RegEst\" = 'A' ORDER BY \"RegCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                regiones.add(rs.getString("RegCod"));
            }
        }
        return regiones;
    }
}
