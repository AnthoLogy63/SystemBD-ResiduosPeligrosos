package destino.dao;

import destino.modelo.DestinoModel;
import src.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinoDAO {

    public List<DestinoModel> findAll() throws SQLException {
        List<DestinoModel> lista = new ArrayList<>();
        String sql = """
            SELECT "DestCod", "DestNom", "DestCiu", "RegCod", "DestTipoResCod",
                   "DestCapacidad", "DestCapUsada", "DestFecCierreEstim",
                   "DestAviso", "DestEst", "DestObs"
            FROM public."R1M_DESTINO"
            ORDER BY "DestNom" ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new DestinoModel(
                    rs.getString("DestCod"),
                    rs.getString("DestNom"),
                    rs.getString("DestCiu"),
                    rs.getString("RegCod"),
                    rs.getString("DestTipoResCod"),
                    rs.getDouble("DestCapacidad"),
                    rs.getDouble("DestCapUsada"),
                    rs.getString("DestFecCierreEstim"),
                    rs.getString("DestAviso"),
                    rs.getString("DestEst"),
                    rs.getString("DestObs")
                ));
            }
        }
        return lista;
    }

    public DestinoModel findById(String cod) throws SQLException {
        String sql = "SELECT * FROM public.\"R1M_DESTINO\" WHERE \"DestCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cod);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DestinoModel(
                        rs.getString("DestCod"),
                        rs.getString("DestNom"),
                        rs.getString("DestCiu"),
                        rs.getString("RegCod"),
                        rs.getString("DestTipoResCod"),
                        rs.getDouble("DestCapacidad"),
                        rs.getDouble("DestCapUsada"),
                        rs.getString("DestFecCierreEstim"),
                        rs.getString("DestAviso"),
                        rs.getString("DestEst"),
                        rs.getString("DestObs")
                    );
                }
            }
        }
        return null;
    }

    public void insert(DestinoModel d) throws SQLException {
        String sql = """
            INSERT INTO public."R1M_DESTINO"
            ("DestCod", "DestNom", "DestCiu", "RegCod", "DestTipoResCod",
             "DestCapacidad", "DestCapUsada", "DestFecCierreEstim",
             "DestAviso", "DestEst", "DestObs")
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getCodigo());
            ps.setString(2, d.getNombre());
            ps.setString(3, d.getCiudadCod());
            ps.setString(4, d.getRegionCod());
            ps.setString(5, d.getTipoResiduo());
            ps.setDouble(6, d.getCapacidad());
            ps.setDouble(7, d.getCapacidadUsada());
            ps.setString(8, d.getFechaCierre());
            ps.setString(9, d.getAviso());
            ps.setString(10, d.getEstado());
            ps.setString(11, d.getObservacion());
            ps.executeUpdate();
        }
    }

    public void update(DestinoModel d) throws SQLException {
        String sql = """
            UPDATE public."R1M_DESTINO" SET
              "DestNom" = ?, "DestCiu" = ?, "RegCod" = ?, "DestTipoResCod" = ?,
              "DestCapacidad" = ?, "DestCapUsada" = ?, "DestFecCierreEstim" = ?,
              "DestAviso" = ?, "DestEst" = ?, "DestObs" = ?
            WHERE "DestCod" = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getCiudadCod());
            ps.setString(3, d.getRegionCod());
            ps.setString(4, d.getTipoResiduo());
            ps.setDouble(5, d.getCapacidad());
            ps.setDouble(6, d.getCapacidadUsada());
            ps.setString(7, d.getFechaCierre());
            ps.setString(8, d.getAviso());
            ps.setString(9, d.getEstado());
            ps.setString(10, d.getObservacion());
            ps.setString(11, d.getCodigo());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String codigo) throws SQLException {
        String sql = "UPDATE public.\"R1M_DESTINO\" SET \"DestEst\" = '*' WHERE \"DestCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean inactivate(String codigo) throws SQLException {
        String sql = "UPDATE public.\"R1M_DESTINO\" SET \"DestEst\" = 'I' WHERE \"DestCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean reactivate(String codigo) throws SQLException {
        String sql = "UPDATE public.\"R1M_DESTINO\" SET \"DestEst\" = 'A' WHERE \"DestCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    // Combos: ciudades, regiones, tipos de residuo
    public List<String> getCiudadesActivas() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT \"CiudCod\" FROM public.\"GZZ_CIUDAD\" WHERE \"CiudEst\" = 'A' ORDER BY \"CiudCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString(1));
        }
        return list;
    }

    public List<String> getRegionesActivas() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT \"RegCod\" FROM public.\"GZZ_REGION\" ORDER BY \"RegCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString(1));
        }
        return list;
    }

    public List<String> getTiposResiduosActivos() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT \"TipoResCod\" FROM public.\"GZZ_TIPORESIDUO\" ORDER BY \"TipoResCod\" ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString(1));
        }
        return list;
    }
    public String getRegionByCiudad(String ciudadCod) throws SQLException {
        String sql = "SELECT \"RegCod\" FROM public.\"GZZ_CIUDAD\" WHERE \"CiudCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ciudadCod);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("RegCod");
                }
            }
        }
        return null;
    }
    public double obtenerCapacidadDestino(String destCod) throws SQLException {
        String sql = "SELECT \"DestCapacidad\" FROM public.\"R1M_DESTINO\" WHERE \"DestCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, destCod);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        }
    }

}
