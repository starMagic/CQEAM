package com.sino.rds.check.dao;

import com.sino.base.dto.DTO;
import com.sino.framework.dto.BaseUserDTO;
import com.sino.rds.appbase.dao.RDSBaseDAO;

import java.sql.Connection;

public class ReportViewCheckDAO extends RDSBaseDAO {

    public ReportViewCheckDAO(BaseUserDTO userAccount, DTO dtoParameter, Connection conn) {
        super(userAccount, dtoParameter, conn);
    }

}
