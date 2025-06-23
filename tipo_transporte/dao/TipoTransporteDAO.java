package tipo_transporte.dao;

import tipo_transporte.conexion.DBConnection;
import tipo_transporte.modelo.TipoTransporteModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoTransporteDAO {

    public List<TipoTransporteModel> findAll() throws SQLException {
        List<TipoTransporteModel> lista = new ArrayList<>();
        String sql = "SELECT \"TipTransCod\", \"TipTransNom\", \"TipTransEst\" "
                + "FROM public.\"GZZ_TIPOTRANSPORTE\" "
                + "ORDER BY \"TipTransCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoTransporteModel(
                    rs.getString("TipTransCod"),
                    rs.getString("TipTransNom"),
                    rs.getString("TipTransEst")
                ));
            }
        }
        return lista;
    }


    public void insert(TipoTransporteModel tt) throws SQLException {
        String sql = "INSERT INTO public.\"GZZ_TIPOTRANSPORTE\" (\"TipTransCod\",\"TipTransNom\",\"TipTransEst\") VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tt.getCodigo());
            ps.setString(2, tt.getNombre());
            ps.setString(3, tt.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(TipoTransporteModel tt) throws SQLException {
        String sql = "UPDATE public.\"GZZ_TIPOTRANSPORTE\" SET \"TipTransNom\" = ?, \"TipTransEst\" = ? WHERE \"TipTransCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tt.getNombre());
            ps.setString(2, tt.getEstado());
            ps.setString(3, tt.getCodigo());
            ps.executeUpdate();
        }
    }

    //Marcar como “eliminado”
    public boolean softDelete(String codigo) throws SQLException {
        String select = "SELECT \"TipTransEst\" FROM public.\"GZZ_TIPOTRANSPORTE\" WHERE \"TipTransCod\" = ?";
        String update = "UPDATE public.\"GZZ_TIPOTRANSPORTE\" SET \"TipTransEst\" = '*' WHERE \"TipTransCod\" = ?";

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

    //Inactivar: estado 'I'
    public boolean inactivate(String codigo) throws SQLException {
        String select = "SELECT \"TipTransEst\" FROM public.\"GZZ_TIPOTRANSPORTE\" WHERE \"TipTransCod\" = ?";
        String update = "UPDATE public.\"GZZ_TIPOTRANSPORTE\" SET \"TipTransEst\" = 'I' WHERE \"TipTransCod\" = ?";

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


    //Reactivar: estado 'A'
    public boolean reactivate(String codigo) throws SQLException {
        String select = "SELECT \"TipTransEst\" FROM public.\"GZZ_TIPOTRANSPORTE\" WHERE \"TipTransCod\" = ?";
        String update = "UPDATE public.\"GZZ_TIPOTRANSPORTE\" SET \"TipTransEst\" = 'A' WHERE \"TipTransCod\" = ?";

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

    public TipoTransporteModel findById(String codigo) throws SQLException {
        String sql = "SELECT \"TipTransCod\",\"TipTransNom\",\"TipTransEst\" FROM public.\"GZZ_TIPOTRANSPORTE\" WHERE \"TipTransCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoTransporteModel(
                        rs.getString("TipTransCod"),
                        rs.getString("TipTransNom"),
                        rs.getString("TipTransEst")
                    );
                } else {
                    return null;
                }
            }
        }
    }
}