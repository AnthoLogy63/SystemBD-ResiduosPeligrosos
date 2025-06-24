package envase.dao;

import envase.modelo.EnvaseModel;
import src.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnvaseDAO {

    public List<EnvaseModel> findAll() throws SQLException {
        List<EnvaseModel> lista = new ArrayList<>();
        String sql = "SELECT \"EnvCod\", \"EnvNom\", \"EnvEst\" FROM public.\"GZZ_ENVASE\" ORDER BY \"EnvCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new EnvaseModel(
                    rs.getString("EnvCod"),
                    rs.getString("EnvNom"),
                    rs.getString("EnvEst")
                ));
            }
        }
        return lista;
    }

    public void insert(EnvaseModel env) throws SQLException {
        String sql = "INSERT INTO public.\"GZZ_ENVASE\" (\"EnvCod\", \"EnvNom\", \"EnvEst\") VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, env.getCodigo());
            ps.setString(2, env.getNombre());
            ps.setString(3, env.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(EnvaseModel env) throws SQLException {
        String sql = "UPDATE public.\"GZZ_ENVASE\" SET \"EnvNom\" = ?, \"EnvEst\" = ? WHERE \"EnvCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, env.getNombre());
            ps.setString(2, env.getEstado());
            ps.setString(3, env.getCodigo());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String codigo) throws SQLException {
        String select = "SELECT \"EnvEst\" FROM public.\"GZZ_ENVASE\" WHERE \"EnvCod\" = ?";
        String update = "UPDATE public.\"GZZ_ENVASE\" SET \"EnvEst\" = '*' WHERE \"EnvCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(select)) {
            ps1.setString(1, codigo);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "*".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(update)) {
                ps2.setString(1, codigo);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean inactivate(String codigo) throws SQLException {
        String select = "SELECT \"EnvEst\" FROM public.\"GZZ_ENVASE\" WHERE \"EnvCod\" = ?";
        String update = "UPDATE public.\"GZZ_ENVASE\" SET \"EnvEst\" = 'I' WHERE \"EnvCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(select)) {
            ps1.setString(1, codigo);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "I".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(update)) {
                ps2.setString(1, codigo);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean reactivate(String codigo) throws SQLException {
        String select = "SELECT \"EnvEst\" FROM public.\"GZZ_ENVASE\" WHERE \"EnvCod\" = ?";
        String update = "UPDATE public.\"GZZ_ENVASE\" SET \"EnvEst\" = 'A' WHERE \"EnvCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(select)) {
            ps1.setString(1, codigo);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "A".equals(rs.getString(1))) {
                    return false;
                }
            }
            try (PreparedStatement ps2 = conn.prepareStatement(update)) {
                ps2.setString(1, codigo);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public EnvaseModel findById(String codigo) throws SQLException {
        String sql = "SELECT \"EnvCod\", \"EnvNom\", \"EnvEst\" FROM public.\"GZZ_ENVASE\" WHERE \"EnvCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EnvaseModel(
                        rs.getString("EnvCod"),
                        rs.getString("EnvNom"),
                        rs.getString("EnvEst")
                    );
                } else {
                    return null;
                }
            }
        }
    }
}
