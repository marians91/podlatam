package com.enel.platform.mepodlatam.batch.repository.mapper.edes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import com.enel.platform.mepodlatam.model.edes.EDESPod;

public class EDESPodMapper implements RowMapper<EDESPod> {

	@Override
	public EDESPod map(ResultSet rs, StatementContext ctx) throws SQLException {
		EDESPod pod = new EDESPod();

		pod.setPointCode(rs.getString("pointCode"));
		pod.setIdWorkOrderActivity(rs.getString("idWorkOrderActivity"));
		pod.setIdVisit(rs.getString("idVisit"));
		pod.setCodVisitType(rs.getString("codVisitType"));
		pod.setCodSelectionType(rs.getString("codSelectionType"));
		pod.setCodTerritorialDivision(rs.getString("codTerritorialDivision"));
		pod.setStatusResult(rs.getString("statusResult"));
		pod.setResultDescription(rs.getString("resultDescription"));
		pod.setEidActivityRequest(rs.getString("eidActivityRequest"));
		pod.setDtWoaCreation(convertToZonedDateTime("dtCreation", rs));
		pod.setFlgProcessed(rs.getString("flgProcessed"));
		pod.setTenant(rs.getString("tenant"));

		return pod;
	}

	private ZonedDateTime convertToZonedDateTime(String columnName, ResultSet rs) {
		OffsetDateTime odt = null;
		try {
			odt = rs.getObject(columnName, OffsetDateTime.class);
			ZoneId z = ZoneId.of("Europe/Rome");
			return odt.atZoneSameInstant(z);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

}
