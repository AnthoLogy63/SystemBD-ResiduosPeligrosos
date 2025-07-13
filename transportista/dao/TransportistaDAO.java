package transportista.dao;

import transportista.modelo.TransportistaModel;
import src.DBConnectionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransportistaDAO {

    public List<TransportistaModel> findAll() throws SQLException {
        List<TransportistaModel> lista = new ArrayList<>();
        String sql = "SELECT EmpTransNif, EmpTransNom, EmpTransCiu, EmpTransTip, EmpTransEst, EmpTransObs " +
                     "FROM R1M_TRANSPORTISTA ORDER BY EmpTransNom ASC";

        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new TransportistaModel(
                    rs.getString("EmpTransNif"),
                    rs.getString("EmpTransNom"),
                    rs.getString("EmpTransCiu"),
                    rs.getString("EmpTransTip"),
                    rs.getString("EmpTransEst"),
                    rs.getString("EmpTransObs")
                ));
            }
        }

        return lista;
    }

    public TransportistaModel findById(String nif) throws SQLException {
        String sql = "SELECT EmpTransNif, EmpTransNom, EmpTransCiu, EmpTransTip, EmpTransEst, EmpTransObs " +
                     "FROM R1M_TRANSPORTISTA WHERE EmpTransNif = ?";

        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nif);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TransportistaModel(
                        rs.getString("EmpTransNif"),
                        rs.getString("EmpTransNom"),
                        rs.getString("EmpTransCiu"),
                        rs.getString("EmpTransTip"),
                        rs.getString("EmpTransEst"),
                        rs.getString("EmpTransObs")
                    );
                }
            }
        }

        return null;
    }

    public void insert(TransportistaModel t) throws SQLException {
        String sql = "INSERT INTO R1M_TRANSPORTISTA " +
                     "(EmpTransNif, EmpTransNom, EmpTransCiu, EmpTransTip, EmpTransEst, EmpTransObs) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNif());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getCiudadCod());
            ps.setString(4, t.getTipoCod());
            ps.setString(5, t.getEstado());
            ps.setString(6, t.getObservacion());
            ps.executeUpdate();
        }
    }

    public void update(TransportistaModel t) throws SQLException {
        String sql = "UPDATE R1M_TRANSPORTISTA SET " +
                     "EmpTransNom = ?, EmpTransCiu = ?, EmpTransTip = ?, EmpTransEst = ?, EmpTransObs = ? " +
                     "WHERE EmpTransNif = ?";

        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getCiudadCod());
            ps.setString(3, t.getTipoCod());
            ps.setString(4, t.getEstado());
            ps.setString(5, t.getObservacion());
            ps.setString(6, t.getNif());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String nif) throws SQLException {
        String sqlCheck = "SELECT EmpTransEst FROM R1M_TRANSPORTISTA WHERE EmpTransNif = ?";
        String sqlDelete = "UPDATE R1M_TRANSPORTISTA SET EmpTransEst = '*' WHERE EmpTransNif = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(sqlCheck)) {
            ps1.setString(1, nif);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "*".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlDelete)) {
                ps2.setString(1, nif);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean inactivate(String nif) throws SQLException {
        String sqlCheck = "SELECT EmpTransEst FROM R1M_TRANSPORTISTA WHERE EmpTransNif = ?";
        String sqlUpdate = "UPDATE R1M_TRANSPORTISTA SET EmpTransEst = 'I' WHERE EmpTransNif = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(sqlCheck)) {
            ps1.setString(1, nif);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "I".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdate)) {
                ps2.setString(1, nif);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean reactivate(String nif) throws SQLException {
        String sqlCheck = "SELECT EmpTransEst FROM R1M_TRANSPORTISTA WHERE EmpTransNif = ?";
        String sqlUpdate = "UPDATE R1M_TRANSPORTISTA SET EmpTransEst = 'A' WHERE EmpTransNif = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(sqlCheck)) {
            ps1.setString(1, nif);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "A".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdate)) {
                ps2.setString(1, nif);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public List<String> getCiudadesActivas() throws SQLException {
        List<String> ciudades = new ArrayList<>();
        String sql = "SELECT CiudCod FROM GZZ_CIUDAD WHERE CiudEst = 'A' ORDER BY CiudCod ASC";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ciudades.add(rs.getString("CiudCod"));
            }
        }
        return ciudades;
    }

    public List<String> getTiposTransporteActivos() throws SQLException {
        List<String> tipos = new ArrayList<>();
        String sql = "SELECT TipTransCod FROM GZZ_TIPOTRANSPORTE WHERE TipTransEst = 'A' ORDER BY TipTransCod ASC";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tipos.add(rs.getString("TipTransCod"));
            }
        }
        return tipos;
    }
}
