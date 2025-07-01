package residuo.dao;

import residuo.modelo.ResiduoModel;
import src.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResiduoDAO {

    public List<ResiduoModel> findAll() throws SQLException {
        List<ResiduoModel> lista = new ArrayList<>();
        String sql = "SELECT \"ResCod\", \"EmpNif\", \"ResTox\", \"TipoResCod\", \"CodResNorm\", \"ResCant\", \"ResObs\", \"ResEst\" FROM public.\"R1T_RESIDUO\" ORDER BY \"ResCod\" ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ResiduoModel(
                rs.getInt("ResCod"), rs.getString("EmpNif"), rs.getInt("ResTox"),
                rs.getString("TipoResCod"), rs.getString("CodResNorm"),
                rs.getDouble("ResCant"), rs.getString("ResObs"),
                rs.getString("ResEst").charAt(0)
            ));
            }
        }
        return lista;
    }

    public ResiduoModel findById(String codigo) throws SQLException {
        String sql = "SELECT \"ResCod\", \"EmpNif\", \"ResTox\", \"TipoResCod\", \"CodResNorm\", \"ResCant\", \"ResObs\", \"ResEst\" " +
                     "FROM public.\"R1T_RESIDUO\" WHERE \"ResCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ResiduoModel(
                        rs.getInt("ResCod"),
                        rs.getString("EmpNif"),
                        rs.getInt("ResTox"),
                        rs.getString("TipoResCod"),
                        rs.getString("CodResNorm"),
                        rs.getDouble("ResCant"),
                        rs.getString("ResObs"),
                        rs.getString("ResEst").charAt(0)
                    );
                }
            }
        }
        return null;
    }

    public void insert(ResiduoModel r) throws SQLException {
        String sql = "INSERT INTO public.\"R1T_RESIDUO\" (\"ResCod\", \"EmpNif\", \"ResTox\", \"TipoResCod\", \"CodResNorm\", \"ResCant\", \"ResObs\", \"ResEst\") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getResCod());
            ps.setString(2, r.getEmpNif());
            ps.setInt(3, r.getResTox());
            ps.setString(4, r.getTipoResCod());
            ps.setString(5, r.getCodResNorm());
            ps.setDouble(6, r.getResCant());
            ps.setString(7, r.getResObs());
            ps.setString(8, String.valueOf(r.getResEst()));
            ps.executeUpdate();
        }
    }

    public void update(ResiduoModel r) throws SQLException {
        String sql = "UPDATE public.\"R1T_RESIDUO\" SET \"EmpNif\" = ?, \"ResTox\" = ?, \"TipoResCod\" = ?, \"CodResNorm\" = ?, \"ResCant\" = ?, \"ResObs\" = ?, \"ResEst\" = ? WHERE \"ResCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getEmpNif());
            ps.setInt(2, r.getResTox());
            ps.setString(3, r.getTipoResCod());
            ps.setString(4, r.getCodResNorm());
            ps.setDouble(5, r.getResCant());
            ps.setString(6, r.getResObs());
            ps.setString(7, String.valueOf(r.getResEst())); 
            ps.setInt(8, r.getResCod());
            ps.executeUpdate();
        }
    }


    public boolean delete(int codigo) throws SQLException {
        String sql = "UPDATE public.\"R1T_RESIDUO\" SET \"ResEst\" = '*' WHERE \"ResCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    // ========== MÉTODOS PARA LLENAR COMBOS ==========

    public List<String> getEmpresasActivas() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT \"EmpNif\" FROM public.\"R1M_EMPRESA\" WHERE \"EmpEst\" = 'A' ORDER BY \"EmpNif\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("EmpNif"));
            }
        }
        return lista;
    }

    public List<Integer> getToxicidadesActivas() throws SQLException {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT \"ToxCod\" FROM public.\"GZZ_TOXICIDAD\" WHERE \"ToxEst\" = 'A' ORDER BY \"ToxCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getInt("ToxCod"));
            }
        }
        return lista;
    }

    public List<String> getTiposResiduoActivos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT \"TipoResCod\" FROM public.\"GZZ_TIPORESIDUO\" WHERE \"TipoResEst\" = 'A' ORDER BY \"TipoResCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("TipoResCod"));
            }
        }
        return lista;
    }

    public List<String> getCodigosResiduoActivos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT \"CodResNorm\" FROM public.\"GZZ_CODIGORESIDUO\" WHERE \"CodResEst\" = 'A' ORDER BY \"CodResNorm\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("CodResNorm"));
            }
        }
        return lista;
    }
}
