package tipo_residuo.dao;

import tipo_residuo.modelo.TipoResiduoModel;
import tipo_transporte.conexion.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoResiduoDAO {

    public List<TipoResiduoModel> findAll() throws SQLException {
        List<TipoResiduoModel> lista = new ArrayList<>();
        String sql = "SELECT \"TipoResCod\", \"TipoResNom\", \"TipoResEst\" FROM public.\"GZZ_TIPORESIDUO\" ORDER BY \"TipoResCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoResiduoModel(
                    rs.getString("TipoResCod"),
                    rs.getString("TipoResNom"),
                    rs.getString("TipoResEst")
                ));
            }
        }
        return lista;
    }

    public void insert(TipoResiduoModel t) throws SQLException {
        String sql = "INSERT INTO public.\"GZZ_TIPORESIDUO\" (\"TipoResCod\", \"TipoResNom\", \"TipoResEst\") VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getCodigo());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(TipoResiduoModel t) throws SQLException {
        String sql = "UPDATE public.\"GZZ_TIPORESIDUO\" SET \"TipoResNom\" = ?, \"TipoResEst\" = ? WHERE \"TipoResCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getEstado());
            ps.setString(3, t.getCodigo());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String codigo) throws SQLException {
        String check = "SELECT \"TipoResEst\" FROM public.\"GZZ_TIPORESIDUO\" WHERE \"TipoResCod\" = ?";
        String sql = "UPDATE public.\"GZZ_TIPORESIDUO\" SET \"TipoResEst\" = '*' WHERE \"TipoResCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
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
        String check = "SELECT \"TipoResEst\" FROM public.\"GZZ_TIPORESIDUO\" WHERE \"TipoResCod\" = ?";
        String sql = "UPDATE public.\"GZZ_TIPORESIDUO\" SET \"TipoResEst\" = 'I' WHERE \"TipoResCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
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
        String check = "SELECT \"TipoResEst\" FROM public.\"GZZ_TIPORESIDUO\" WHERE \"TipoResCod\" = ?";
        String sql = "UPDATE public.\"GZZ_TIPORESIDUO\" SET \"TipoResEst\" = 'A' WHERE \"TipoResCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
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

    public TipoResiduoModel findById(String codigo) throws SQLException {
        String sql = "SELECT \"TipoResCod\", \"TipoResNom\", \"TipoResEst\" FROM public.\"GZZ_TIPORESIDUO\" WHERE \"TipoResCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoResiduoModel(
                        rs.getString("TipoResCod"),
                        rs.getString("TipoResNom"),
                        rs.getString("TipoResEst")
                    );
                } else {
                    return null;
                }
            }
        }
    }
}
