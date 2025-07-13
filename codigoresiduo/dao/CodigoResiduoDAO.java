package codigoresiduo.dao;

import codigoresiduo.modelo.CodigoResiduoModel;
import src.DBConnectionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CodigoResiduoDAO {

    public List<CodigoResiduoModel> findAll() throws SQLException {
        List<CodigoResiduoModel> lista = new ArrayList<>();
        String sql = "SELECT CodResNorm, CodResDesc, CodResEst FROM GZZ_CODIGORESIDUO ORDER BY CodResNorm ASC";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new CodigoResiduoModel(
                    rs.getString("CodResNorm"),
                    rs.getString("CodResDesc"),
                    rs.getString("CodResEst")
                ));
            }
        }
        return lista;
    }

    public void insert(CodigoResiduoModel c) throws SQLException {
        String sql = "INSERT INTO GZZ_CODIGORESIDUO (CodResNorm, CodResDesc, CodResEst) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCodigo());
            ps.setString(2, c.getDescripcion());
            ps.setString(3, c.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(CodigoResiduoModel c) throws SQLException {
        String sql = "UPDATE GZZ_CODIGORESIDUO SET CodResDesc = ?, CodResEst = ? WHERE CodResNorm = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDescripcion());
            ps.setString(2, c.getEstado());
            ps.setString(3, c.getCodigo());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String codigo) throws SQLException {
        String check = "SELECT CodResEst FROM GZZ_CODIGORESIDUO WHERE CodResNorm = ?";
        String sql = "UPDATE GZZ_CODIGORESIDUO SET CodResEst = '*' WHERE CodResNorm = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(check)) {
            ps1.setString(1, codigo);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "*".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
                ps2.setString(1, codigo);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean inactivate(String codigo) throws SQLException {
        String check = "SELECT CodResEst FROM GZZ_CODIGORESIDUO WHERE CodResNorm = ?";
        String sql = "UPDATE GZZ_CODIGORESIDUO SET CodResEst = 'I' WHERE CodResNorm = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(check)) {
            ps1.setString(1, codigo);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "I".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
                ps2.setString(1, codigo);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean reactivate(String codigo) throws SQLException {
        String check = "SELECT CodResEst FROM GZZ_CODIGORESIDUO WHERE CodResNorm = ?";
        String sql = "UPDATE GZZ_CODIGORESIDUO SET CodResEst = 'A' WHERE CodResNorm = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(check)) {
            ps1.setString(1, codigo);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "A".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
                ps2.setString(1, codigo);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public CodigoResiduoModel findById(String codigo) throws SQLException {
        String sql = "SELECT CodResNorm, CodResDesc, CodResEst FROM GZZ_CODIGORESIDUO WHERE CodResNorm = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CodigoResiduoModel(
                        rs.getString("CodResNorm"),
                        rs.getString("CodResDesc"),
                        rs.getString("CodResEst")
                    );
                } else {
                    return null;
                }
            }
        }
    }
}
