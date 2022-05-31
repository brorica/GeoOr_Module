package road.calcHillShade.repository;

import road.calcHillShade.domain.Road;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateRoadHillShade {

    private final int batchLimitValue = 1024;

    public void update(Connection conn, List<Road> roads) throws SQLException {
        String updateSql = createUpdateSql();
        try(PreparedStatement pStmt = conn.prepareStatement(updateSql)) {
            setPreParedArgument(pStmt, roads);
        }
    }

    public void setPreParedArgument(PreparedStatement pStmt, List<Road> roads) throws SQLException {
        int batchLimit = batchLimitValue,  recordCount = 0;
        for (Road road : roads) {
            pStmt.setInt(1, road.getAverage());
            pStmt.setInt(2, road.getId());
            pStmt.addBatch();

            if(--batchLimit == 0) {
                recordCount += pStmt.executeBatch().length;
                batchLimit = batchLimitValue;
            }
        }
        recordCount += pStmt.executeBatch().length;
        System.out.printf("%d update\n", recordCount);
    }

    private String createUpdateSql() {
        return "UPDATE road "
            + "SET hillshade = ? "
            + "where id = ?";
    }
}
