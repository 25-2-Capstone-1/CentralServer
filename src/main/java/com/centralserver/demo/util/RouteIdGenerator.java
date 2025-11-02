package com.centralserver.demo.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class RouteIdGenerator implements IdentifierGenerator {
    private static final String PREFIX = "ROUTE_";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        try {
            // 현재 최대 route_id 값 조회
            String sql = "SELECT route_id FROM recommended_route ORDER BY route_id DESC LIMIT 1";
            String lastId = (String) session.createNativeQuery(sql).uniqueResult();

            long nextNumber = 1L;
            if (lastId != null && lastId.startsWith(PREFIX)) {
                // ROUTE_ 숫자 부분만 추출
                String numPart = lastId.substring(PREFIX.length());
                nextNumber = Long.parseLong(numPart) + 1;
            }

            return PREFIX + nextNumber;

        } catch (Exception e) {
            e.printStackTrace();
            return PREFIX + System.currentTimeMillis(); // fallback
        }
    }
}
