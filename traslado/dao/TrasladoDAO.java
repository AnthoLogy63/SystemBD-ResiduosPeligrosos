package traslado.dao;

import traslado.modelo.TrasladoModel;
import src.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrasladoDAO {

    public List<TrasladoModel> findAll() throws SQLException {
        List<TrasladoModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM public.\"R1T_TRASLADO\" " +
                 "ORDER BY TO_DATE(\"TraFecEnv\", 'YYYY-MM-DD') ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToTraslado(rs));
            }
        }
        return lista;
    }

    public TrasladoModel findById(String empNif, int resCod, String traFecEnv, String destCod) throws SQLException {
        String sql = "SELECT * FROM public.\"R1T_TRASLADO\" WHERE \"EmpNif\" = ? AND \"ResCod\" = ? AND \"TraFecEnv\" = ? AND \"DestCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, empNif);
            ps.setInt(2, resCod);
            ps.setString(3, traFecEnv);
            ps.setString(4, destCod);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTraslado(rs);
                }
            }
        }
        return null;
    }

    public void insert(TrasladoModel t) throws SQLException {
        String sql = "INSERT INTO public.\"R1T_TRASLADO\" (\"EmpNif\", \"ResCod\", \"TraFecEnv\", \"DestCod\", \"TraEnv\", \"TraFecLlega\", \"TraTrat\", \"TraCant\", \"TraObs\", \"TransCod\", \"EstCod\") " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getEmpNif());
            ps.setInt(2, t.getResCod());
            ps.setString(3, t.getFechaEnvio());
            ps.setString(4, t.getDestCod());
            ps.setString(5, t.getTipoEnvase());
            ps.setString(6, t.getFechaLlegada());
            ps.setString(7, t.getTratamiento());
            ps.setDouble(8, t.getCantidad());
            ps.setString(9, t.getObservacion());
            ps.setString(10, t.getTransportista());
            ps.setString(11, t.getEstado());
            ps.executeUpdate();
        }
    }

    public void update(TrasladoModel t) throws SQLException {
        String sql = "UPDATE public.\"R1T_TRASLADO\" SET \"TraEnv\" = ?, \"TraFecLlega\" = ?, \"TraTrat\" = ?, \"TraCant\" = ?, \"TraObs\" = ?, \"TransCod\" = ?, \"EstCod\" = ? " +
                     "WHERE \"EmpNif\" = ? AND \"ResCod\" = ? AND \"TraFecEnv\" = ? AND \"DestCod\" = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getTipoEnvase());
            ps.setString(2, t.getFechaLlegada());
            ps.setString(3, t.getTratamiento());
            ps.setDouble(4, t.getCantidad());
            ps.setString(5, t.getObservacion());
            ps.setString(6, t.getTransportista());
            ps.setString(7, t.getEstado());
            ps.setString(8, t.getEmpNif());
            ps.setInt(9, t.getResCod());
            ps.setString(10, t.getFechaEnvio());
            ps.setString(11, t.getDestCod());
            ps.executeUpdate();
        }
    }

    public boolean softDelete(String empNif, int resCod, String traFecEnv, String destCod) throws SQLException {
        return updateEstado(empNif, resCod, traFecEnv, destCod, '*');
    }

    public boolean inactivate(String empNif, int resCod, String traFecEnv, String destCod) throws SQLException {
        return updateEstado(empNif, resCod, traFecEnv, destCod, 'I');
    }

    public boolean reactivate(String empNif, int resCod, String traFecEnv, String destCod) throws SQLException {
        return updateEstado(empNif, resCod, traFecEnv, destCod, 'A');
    }

    private boolean updateEstado(String empNif, int resCod, String traFecEnv, String destCod, char nuevoEstado) throws SQLException {
        String sql = "UPDATE public.\"R1T_TRASLADO\" SET \"EstCod\" = ? WHERE \"EmpNif\" = ? AND \"ResCod\" = ? AND \"TraFecEnv\" = ? AND \"DestCod\" = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, String.valueOf(nuevoEstado));
            ps.setString(2, empNif);
            ps.setInt(3, resCod);
            ps.setString(4, traFecEnv);
            ps.setString(5, destCod);
            return ps.executeUpdate() > 0;
        }
    }

    private TrasladoModel mapResultSetToTraslado(ResultSet rs) throws SQLException {
        return new TrasladoModel(
            rs.getString("EmpNif"),
            rs.getInt("ResCod"),
            rs.getString("TraFecEnv"),
            rs.getString("DestCod"),
            rs.getString("TraEnv"),
            rs.getString("TraFecLlega"),
            rs.getString("TraTrat"),
            rs.getDouble("TraCant"),
            rs.getString("TraObs"),
            rs.getString("TransCod"),
            rs.getString("EstCod")
        );
    }
    // Empresas activas (NIF)
    public List<String> getEmpresasActivas() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT \"EmpNif\" FROM public.\"R1M_EMPRESA\" WHERE \"EmpEst\" = 'A' ORDER BY \"EmpNif\"";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("EmpNif"));
            }
        }
        return lista;
    }

    // Códigos de residuos activos
    public List<Integer> getResiduosActivos() throws SQLException {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT \"ResCod\" FROM public.\"R1T_RESIDUO\" WHERE \"ResEst\" = 'A' ORDER BY \"ResCod\"";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getInt("ResCod"));
            }
        }
        return lista;
    }

    // Destinos activos
    public List<String> getDestinosActivos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT \"DestCod\" FROM public.\"R1M_DESTINO\" WHERE \"DestEst\" = 'A' ORDER BY \"DestCod\"";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("DestCod"));
            }
        }
        return lista;
    }

    // Tipos de envase activos
    public List<String> getEnvasesActivos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT \"EnvCod\" FROM public.\"GZZ_ENVASE\" WHERE \"EnvEst\" = 'A' ORDER BY \"EnvCod\"";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("EnvCod"));
            }
        }
        return lista;
    }

    // Tratamientos activos
    public List<String> getTratamientosActivos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT \"TratCod\" FROM public.\"GZZ_TRATAMIENTO\" WHERE \"TratEst\" = 'A' ORDER BY \"TratCod\"";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("TratCod"));
            }
        }
        return lista;
    }

    // Transportistas activos
    public List<String> getTransportistasActivos() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT \"EmpTransNif\" FROM public.\"R1M_TRANSPORTISTA\" WHERE \"EmpTransEst\" = 'A' ORDER BY \"EmpTransNif\"";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("EmpTransNif"));
            }
        }
        return lista;
    }
    public double obtenerCantidadAcumuladaPorDestino(String destCod) throws SQLException {
        String sql = "SELECT SUM(\"TraCant\") FROM public.\"R1T_TRASLADO\" WHERE \"DestCod\" = ? AND \"EstCod\" = 'A'";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, destCod);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        }
    }

}
