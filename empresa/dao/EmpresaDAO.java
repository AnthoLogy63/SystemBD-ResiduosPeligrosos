package empresa.dao;

import empresa.modelo.EmpresaModel;
import src.DBConnectionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    public List<EmpresaModel> findAll() throws SQLException {
        List<EmpresaModel> lista = new ArrayList<>();
        String sql = "SELECT EmpNif, EmpNom, EmpCiu, EmpAct, EmpObs, EmpEst FROM R1M_EMPRESA ORDER BY EmpNom ASC";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new EmpresaModel(
                        rs.getString("EmpNif"),
                        rs.getString("EmpNom"),
                        rs.getString("EmpCiu"),
                        rs.getString("EmpAct"),
                        rs.getString("EmpObs"),
                        rs.getString("EmpEst")
                ));
            }
        }
        return lista;
    }

    public EmpresaModel findById(String nif) throws SQLException {
        String sql = "SELECT EmpNif, EmpNom, EmpCiu, EmpAct, EmpObs, EmpEst FROM R1M_EMPRESA WHERE EmpNif = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nif);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EmpresaModel(
                            rs.getString("EmpNif"),
                            rs.getString("EmpNom"),
                            rs.getString("EmpCiu"),
                            rs.getString("EmpAct"),
                            rs.getString("EmpObs"),
                            rs.getString("EmpEst")
                    );
                }
            }
        }
        return null;
    }

    public void insert(EmpresaModel e) throws SQLException {
        String sql = "INSERT INTO R1M_EMPRESA (EmpNif, EmpNom, EmpCiu, EmpAct, EmpObs, EmpEst) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNif());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getCiudadCod());
            ps.setString(4, e.getActividad());
            ps.setString(5, e.getObservacion());
            ps.setString(6, e.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(EmpresaModel e) throws SQLException {
        String sql = "UPDATE R1M_EMPRESA SET EmpNom = ?, EmpCiu = ?, EmpAct = ?, EmpObs = ?, EmpEst = ? WHERE EmpNif = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCiudadCod());
            ps.setString(3, e.getActividad());
            ps.setString(4, e.getObservacion());
            ps.setString(5, e.getEstado());
            ps.setString(6, e.getNif());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String nif) throws SQLException {
        String check = "SELECT EmpEst FROM R1M_EMPRESA WHERE EmpNif = ?";
        String sql = "UPDATE R1M_EMPRESA SET EmpEst = '*' WHERE EmpNif = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(check)) {
            ps1.setString(1, nif);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "*".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
                ps2.setString(1, nif);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean inactivate(String nif) throws SQLException {
        String check = "SELECT EmpEst FROM R1M_EMPRESA WHERE EmpNif = ?";
        String sql = "UPDATE R1M_EMPRESA SET EmpEst = 'I' WHERE EmpNif = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(check)) {
            ps1.setString(1, nif);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "I".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
                ps2.setString(1, nif);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean reactivate(String nif) throws SQLException {
        String check = "SELECT EmpEst FROM R1M_EMPRESA WHERE EmpNif = ?";
        String sql = "UPDATE R1M_EMPRESA SET EmpEst = 'A' WHERE EmpNif = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(check)) {
            ps1.setString(1, nif);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "A".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
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

}
