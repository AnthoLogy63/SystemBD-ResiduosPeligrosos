package toxicidad.dao;

import toxicidad.modelo.ToxicidadModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import src.DBConnection;

public class ToxicidadDAO {

    public List<ToxicidadModel> findAll() throws SQLException {
        List<ToxicidadModel> lista = new ArrayList<>();
        String sql = "SELECT \"ToxCod\", \"ToxNom\", \"ToxEst\" FROM public.\"GZZ_TOXICIDAD\" ORDER BY \"ToxCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ToxicidadModel(
                    rs.getInt("ToxCod"),
                    rs.getString("ToxNom"),
                    rs.getString("ToxEst")
                ));
            }
        }
        return lista;
    }

    public void insert(ToxicidadModel tox) throws SQLException {
        String sql = "INSERT INTO public.\"GZZ_TOXICIDAD\" (\"ToxCod\", \"ToxNom\", \"ToxEst\") VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tox.getCodigo());
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
            ps.setInt(3, tox.getCodigo());
            ps.executeUpdate();
        }
    }

    public void softDelete(int codigo) throws SQLException {
        String sql = "UPDATE public.\"GZZ_TOXICIDAD\" SET \"ToxEst\" = '*' WHERE \"ToxCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
        }
    }

    public void inactivate(int codigo) throws SQLException {
        String sql = "UPDATE public.\"GZZ_TOXICIDAD\" SET \"ToxEst\" = 'I' WHERE \"ToxCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
        }
    }

    public void reactivate(int codigo) throws SQLException {
        String sql = "UPDATE public.\"GZZ_TOXICIDAD\" SET \"ToxEst\" = 'A' WHERE \"ToxCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
        }
    }

    public ToxicidadModel findById(int codigo) throws SQLException {
        String sql = "SELECT \"ToxCod\", \"ToxNom\", \"ToxEst\" FROM public.\"GZZ_TOXICIDAD\" WHERE \"ToxCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ToxicidadModel(
                        rs.getInt("ToxCod"),
                        rs.getString("ToxNom"),
                        rs.getString("ToxEst")
                    );
                }
            }
        }
        return null;
    }
}
