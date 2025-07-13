package tratamiento.dao;

import tratamiento.modelo.TratamientoModel;
import src.DBConnectionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TratamientoDAO {

    public List<TratamientoModel> findAll() throws SQLException {
        List<TratamientoModel> lista = new ArrayList<>();
        String sql = "SELECT TratCod, TratNom, TratEst FROM GZZ_TRATAMIENTO ORDER BY TratCod ASC";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new TratamientoModel(
                    rs.getString("TratCod"),
                    rs.getString("TratNom"),
                    rs.getString("TratEst")
                ));
            }
        }
        return lista;
    }

    public void insert(TratamientoModel t) throws SQLException {
        String sql = "INSERT INTO GZZ_TRATAMIENTO (TratCod, TratNom, TratEst) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getCodigo());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(TratamientoModel t) throws SQLException {
        String sql = "UPDATE GZZ_TRATAMIENTO SET TratNom = ?, TratEst = ? WHERE TratCod = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getEstado());
            ps.setString(3, t.getCodigo());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String codigo) throws SQLException {
        String check = "SELECT TratEst FROM GZZ_TRATAMIENTO WHERE TratCod = ?";
        String sql = "UPDATE GZZ_TRATAMIENTO SET TratEst = '*' WHERE TratCod = ?";
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
        String check = "SELECT TratEst FROM GZZ_TRATAMIENTO WHERE TratCod = ?";
        String sql = "UPDATE GZZ_TRATAMIENTO SET TratEst = 'I' WHERE TratCod = ?";
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
        String check = "SELECT TratEst FROM GZZ_TRATAMIENTO WHERE TratCod = ?";
        String sql = "UPDATE GZZ_TRATAMIENTO SET TratEst = 'A' WHERE TratCod = ?";
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

    public TratamientoModel findById(String codigo) throws SQLException {
        String sql = "SELECT TratCod, TratNom, TratEst FROM GZZ_TRATAMIENTO WHERE TratCod = ?";
        try (Connection conn = DBConnectionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TratamientoModel(
                        rs.getString("TratCod"),
                        rs.getString("TratNom"),
                        rs.getString("TratEst")
                    );
                } else {
                    return null;
                }
            }
        }
    }
}
