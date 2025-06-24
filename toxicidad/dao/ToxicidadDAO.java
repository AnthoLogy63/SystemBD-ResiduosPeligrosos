package toxicidad.dao;

import toxicidad.modelo.ToxicidadModel;
import tipo_transporte.conexion.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToxicidadDAO {

    public List<ToxicidadModel> findAll() throws SQLException {
        List<ToxicidadModel> lista = new ArrayList<>();
        String sql = "SELECT \"ToxCod\", \"ToxNom\", \"ToxEst\" FROM public.\"GZZ_TOXICIDAD\" ORDER BY \"ToxCod\" ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ToxicidadModel(
                    rs.getString("ToxCod"),
                    rs.getString("ToxNom"),
                    rs.getString("ToxEst")
                ));
            }
        }

        return lista;
    }

    public ToxicidadModel findById(String codigo) throws SQLException {
        String sql = "SELECT \"ToxCod\", \"ToxNom\", \"ToxEst\" FROM public.\"GZZ_TOXICIDAD\" WHERE \"ToxCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ToxicidadModel(
                        rs.getString("ToxCod"),
                        rs.getString("ToxNom"),
                        rs.getString("ToxEst")
                    );
                }
            }
        }
        return null;
    }

    public void insert(ToxicidadModel tox) throws SQLException {
        String sql = "INSERT INTO public.\"GZZ_TOXICIDAD\" (\"ToxCod\", \"ToxNom\", \"ToxEst\") VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tox.getCodigo());
            ps.setString(2, tox.getNombre());
            ps.setString(3, tox.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(ToxicidadModel tox) throws SQLException {
        String sql = "UPDATE public.\"GZZ_TOXICIDAD\" SET \"ToxNom\" = ?, \"ToxEst\" = ? WHERE \"ToxCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tox.getNombre());
            ps.setString(2, tox.getEstado());
            ps.setString(3, tox.getCodigo());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String codigo) throws SQLException {
        String checkSql = "SELECT \"ToxEst\" FROM public.\"GZZ_TOXICIDAD\" WHERE \"ToxCod\" = ?";
        String updateSql = "UPDATE public.\"GZZ_TOXICIDAD\" SET \"ToxEst\" = '*' WHERE \"ToxCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setString(1, codigo);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && "*".equals(rs.getString("ToxEst"))) {
                    return false; // ya está eliminado
                }
            }

            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setString(1, codigo);
                updatePs.executeUpdate();
                return true;
            }
        }
    }

    public boolean inactivate(String codigo) throws SQLException {
        String checkSql = "SELECT \"ToxEst\" FROM public.\"GZZ_TOXICIDAD\" WHERE \"ToxCod\" = ?";
        String updateSql = "UPDATE public.\"GZZ_TOXICIDAD\" SET \"ToxEst\" = 'I' WHERE \"ToxCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setString(1, codigo);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && "I".equals(rs.getString("ToxEst"))) {
                    return false; // ya está inactivo
                }
            }

            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setString(1, codigo);
                updatePs.executeUpdate();
                return true;
            }
        }
    }

    public boolean reactivate(String codigo) throws SQLException {
        String checkSql = "SELECT \"ToxEst\" FROM public.\"GZZ_TOXICIDAD\" WHERE \"ToxCod\" = ?";
        String updateSql = "UPDATE public.\"GZZ_TOXICIDAD\" SET \"ToxEst\" = 'A' WHERE \"ToxCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setString(1, codigo);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && "A".equals(rs.getString("ToxEst"))) {
                    return false; // ya está activo
                }
            }

            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setString(1, codigo);
                updatePs.executeUpdate();
                return true;
            }
        }
    }
}
