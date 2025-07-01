package rescons.dao;

import rescons.modelo.ResConsModel;
import src.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResConsDAO {

    public List<ResConsModel> findAll() throws SQLException {
        List<ResConsModel> lista = new ArrayList<>();
        String sql = "SELECT \"EmpNif\", \"ResCod\", \"ConCod\", \"ResConCan\", \"ResConEst\" " +
                     "FROM public.\"R1T_RES_CONS\" ORDER BY \"EmpNif\", \"ResCod\", \"ConCod\"";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ResConsModel(
                        rs.getString("EmpNif"),
                        rs.getInt("ResCod"),
                        rs.getInt("ConCod"),
                        rs.getDouble("ResConCan"),
                        rs.getString("ResConEst")
                ));
            }
        }
        return lista;
    }

    public ResConsModel findById(String empNif, int resCod, int conCod) throws SQLException {
        String sql = "SELECT * FROM public.\"R1T_RES_CONS\" " +
                     "WHERE \"EmpNif\" = ? AND \"ResCod\" = ? AND \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empNif);
            ps.setInt(2, resCod);
            ps.setInt(3, conCod);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ResConsModel(
                            rs.getString("EmpNif"),
                            rs.getInt("ResCod"),
                            rs.getInt("ConCod"),
                            rs.getDouble("ResConCan"),
                            rs.getString("ResConEst")
                    );
                }
            }
        }
        return null;
    }

    public void insert(ResConsModel r) throws SQLException {
        String sql = "INSERT INTO public.\"R1T_RES_CONS\" " +
                     "(\"EmpNif\", \"ResCod\", \"ConCod\", \"ResConCan\", \"ResConEst\") " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getEmpNif());
            ps.setInt(2, r.getResCod());
            ps.setInt(3, r.getConCod());
            ps.setDouble(4, r.getCantidad());
            ps.setString(5, r.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(ResConsModel r) throws SQLException {
        String sql = "UPDATE public.\"R1T_RES_CONS\" SET \"ResConCan\" = ?, \"ResConEst\" = ? " +
                     "WHERE \"EmpNif\" = ? AND \"ResCod\" = ? AND \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, r.getCantidad());
            ps.setString(2, r.getEstado());
            ps.setString(3, r.getEmpNif());
            ps.setInt(4, r.getResCod());
            ps.setInt(5, r.getConCod());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String empNif, int resCod, int conCod) throws SQLException {
        String sql = "UPDATE public.\"R1T_RES_CONS\" SET \"ResConEst\" = '*' " +
                     "WHERE \"EmpNif\" = ? AND \"ResCod\" = ? AND \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empNif);
            ps.setInt(2, resCod);
            ps.setInt(3, conCod);
            int updated = ps.executeUpdate();
            return updated > 0;
        }
    }

    public boolean inactivate(String empNif, int resCod, int conCod) throws SQLException {
        String sql = "UPDATE public.\"R1T_RES_CONS\" SET \"ResConEst\" = 'I' " +
                     "WHERE \"EmpNif\" = ? AND \"ResCod\" = ? AND \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empNif);
            ps.setInt(2, resCod);
            ps.setInt(3, conCod);
            int updated = ps.executeUpdate();
            return updated > 0;
        }
    }

    public boolean reactivate(String empNif, int resCod, int conCod) throws SQLException {
        String sql = "UPDATE public.\"R1T_RES_CONS\" SET \"ResConEst\" = 'A' " +
                     "WHERE \"EmpNif\" = ? AND \"ResCod\" = ? AND \"ConCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empNif);
            ps.setInt(2, resCod);
            ps.setInt(3, conCod);
            int updated = ps.executeUpdate();
            return updated > 0;
        }
    }

    public List<String> getEmpresasActivas() throws SQLException {
        List<String> empresas = new ArrayList<>();
        String sql = "SELECT \"EmpNif\" FROM public.\"R1M_EMPRESA\" WHERE \"EmpEst\" = 'A' ORDER BY \"EmpNif\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                empresas.add(rs.getString("EmpNif"));
            }
        }
        return empresas;
    }

    public List<String> getResiduosActivos() throws SQLException {
        List<String> residuos = new ArrayList<>();
        String sql = "SELECT \"ResCod\" FROM public.\"R1T_RESIDUO\" WHERE \"ResEst\" = 'A' ORDER BY \"ResCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                residuos.add(String.valueOf(rs.getInt("ResCod")));
            }
        }
        return residuos;
    }

    public List<String> getConstituyentesActivos() throws SQLException {
        List<String> constituyentes = new ArrayList<>();
        String sql = "SELECT \"ConCod\" FROM public.\"R1Z_CONSTITUYENTE\" WHERE \"ConEst\" = 'A' ORDER BY \"ConCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                constituyentes.add(String.valueOf(rs.getInt("ConCod")));
            }
        }
        return constituyentes;
    }
}
