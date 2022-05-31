package road.calcHillShade.repository;

import road.calcHillShade.domain.Road;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GetRoads {

    private final String sigCode;

    public GetRoads(String sigCode) {
        this.sigCode = sigCode;
    }

    public int get(Connection conn, List<Road> roads) throws SQLException {
        String selectSql = createSql();
        try(PreparedStatement pStmt = conn.prepareStatement(selectSql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            pStmt.setString(1, sigCode + "%");
            return parseResult(pStmt, roads);
        }
    }

    private String createSql() {
        return "SELECT id from road where sig_cd like ? order by id";
    }

    private int parseResult(PreparedStatement pStmt, List<Road> roads) throws SQLException {
        int baseIndex = 0, id;
        try (ResultSet rs = pStmt.executeQuery()) {
            while (rs.next()) {
                id = rs.getInt(1);
                if (rs.isFirst()) {
                    baseIndex = id;
                }
                roads.add(new Road(id));
            }
        }
        return baseIndex;
    }
}
