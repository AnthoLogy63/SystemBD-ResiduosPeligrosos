package constituyente.dao;

import constituyente.modelo.ConstituyenteModel;
import src.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConstituyenteDAO {

    public List<ConstituyenteModel> findAll() throws SQLException {
        List<ConstituyenteModel> lista = new ArrayList<>();
        String sql = "SELECT \"ConCod\", \"ConNom\", \"ConObs\", \"ConEst\" FROM public.\"R1Z_CONSTITUYENTE\" ORDER BY \"ConCod\" ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ConstituyenteModel(
                        rs.getInt("ConCod"),
                        rs.getString("ConNom"),
                        rs.getString("ConObs"),
                        rs.getString("ConEst").charAt(0)
                ));
            }
        }
        return lista;
    }

    public ConstituyenteModel findById(int cod) throws SQLException {
        String sql = "SELECT \"ConCod\", \"ConNom\", \"ConObs\", \"ConEst\" FROM public.\"R1Z_CONSTITUYENTE\" WHERE \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cod);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ConstituyenteModel(
                            rs.getInt("ConCod"),
                            rs.getString("ConNom"),
                            rs.getString("ConObs"),
                            rs.getString("ConEst").charAt(0)
                    );
                }
            }
        }
        return null;
    }

    public void insert(ConstituyenteModel c) throws SQLException {
        String sql = "INSERT INTO public.\"R1Z_CONSTITUYENTE\" (\"ConCod\", \"ConNom\", \"ConObs\", \"ConEst\") VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getCodigo());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getObservacion());
            ps.setString(4, String.valueOf(c.getEstado()));
            ps.executeUpdate();
        }
    }

    public void update(ConstituyenteModel c) throws SQLException {
        String sql = "UPDATE public.\"R1Z_CONSTITUYENTE\" SET \"ConNom\" = ?, \"ConObs\" = ?, \"ConEst\" = ? WHERE \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getObservacion());
            ps.setString(3, String.valueOf(c.getEstado()));
            ps.setInt(4, c.getCodigo());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(int cod) throws SQLException {
        String sqlCheck = "SELECT \"ConEst\" FROM public.\"R1Z_CONSTITUYENTE\" WHERE \"ConCod\" = ?";
        String sqlUpdate = "UPDATE public.\"R1Z_CONSTITUYENTE\" SET \"ConEst\" = '*' WHERE \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(sqlCheck)) {

            ps1.setInt(1, cod);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "*".equals(rs.getString("ConEst"))) {
                    return false;
                }
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdate)) {
                ps2.setInt(1, cod);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean inactivate(int cod) throws SQLException {
        String sqlCheck = "SELECT \"ConEst\" FROM public.\"R1Z_CONSTITUYENTE\" WHERE \"ConCod\" = ?";
        String sqlUpdate = "UPDATE public.\"R1Z_CONSTITUYENTE\" SET \"ConEst\" = 'I' WHERE \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(sqlCheck)) {

            ps1.setInt(1, cod);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "I".equals(rs.getString("ConEst"))) {
                    return false;
                }
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdate)) {
                ps2.setInt(1, cod);
                ps2.executeUpdate();
                return true;
            }
        }
    }

    public boolean reactivate(int cod) throws SQLException {
        String sqlCheck = "SELECT \"ConEst\" FROM public.\"R1Z_CONSTITUYENTE\" WHERE \"ConCod\" = ?";
        String sqlUpdate = "UPDATE public.\"R1Z_CONSTITUYENTE\" SET \"ConEst\" = 'A' WHERE \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(sqlCheck)) {

            ps1.setInt(1, cod);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next() && "A".equals(rs.getString("ConEst"))) {
                    return false;
                }
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdate)) {
                ps2.setInt(1, cod);
                ps2.executeUpdate();
                return true;
            }
        }
    }
}
