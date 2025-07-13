package region.dao;

import region.modelo.RegionModel;
import src.DBConnectionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegionDAO {

    public List<RegionModel> findAll() throws SQLException {
        List<RegionModel> lista = new ArrayList<>();
        String sql = "SELECT RegCod, RegNom, RegEst FROM GZZ_REGION ORDER BY RegCod ASC";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new RegionModel(
                        rs.getString("RegCod"),
                        rs.getString("RegNom"),
                        rs.getString("RegEst")
                ));
            }
        }
        return lista;
    }

    public RegionModel findById(String codigo) throws SQLException {
        String sql = "SELECT RegCod, RegNom, RegEst FROM GZZ_REGION WHERE RegCod = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RegionModel(
                            rs.getString("RegCod"),
                            rs.getString("RegNom"),
                            rs.getString("RegEst")
                    );
                }
            }
        }
        return null;
    }

    public void insert(RegionModel region) throws SQLException {
        String sql = "INSERT INTO GZZ_REGION (RegCod, RegNom, RegEst) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, region.getCodigo());
            ps.setString(2, region.getNombre());
            ps.setString(3, region.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(RegionModel region) throws SQLException {
        String sql = "UPDATE GZZ_REGION SET RegNom = ?, RegEst = ? WHERE RegCod = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, region.getNombre());
            ps.setString(2, region.getEstado());
            ps.setString(3, region.getCodigo());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String codigo) throws SQLException {
        String sql = "UPDATE GZZ_REGION SET RegEst = '*' WHERE RegCod = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean inactivate(String codigo) throws SQLException {
        String sql = "UPDATE GZZ_REGION SET RegEst = 'I' WHERE RegCod = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean reactivate(String codigo) throws SQLException {
        String sql = "UPDATE GZZ_REGION SET RegEst = 'A' WHERE RegCod = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }
}
