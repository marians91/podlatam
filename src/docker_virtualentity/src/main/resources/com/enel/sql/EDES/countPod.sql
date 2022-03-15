SELECT count(*) FROM(
       SELECT
           tmwr.woa_point_code AS "pointCode" ,
           tmwr.id_work_order_activity::TEXT AS "idWorkOrderActivity" ,
           tmwr.id_work_order_activity::TEXT AS "idVisit" ,
           tmwr.ckr_cod_checking_type AS "codVisitType" ,
           tmwr.ckr_cod_checking_type AS "codSelectionType",
           tmwr.woa_eid_operational_unit AS "codTerritorialDivision" ,
           'OK' AS "statusResult",
           'OK' AS "resultDescription",
           tmwr.woa_eid_request_activity AS "eidActivityRequest",
           tmwr.woa_dt_creation AS "dtCreation",
           CASE
               WHEN tmwr.woa_cod_business_state_vcode IN ('CON', 'ANN') THEN TRUE
               ELSE FALSE
           END AS "flgProcessed" ,
           'DD01' AS "tenant"
       FROM flat.flat.t_monitoring_woa_rver tmwr
       -- WHERE tmwr.woa_request_system = 'II4ER'
       UNION
       SELECT tmworr.point_code AS "pointCode",
           NULL AS "idWorkOrderActivity" ,
           NULL AS "idVisit",
           NULL AS "codVisitType",
           NULL AS "codSelectionType",
           NULL AS "codTerritorialDivision",
           'KO' AS "statusResult",
           tmworr.description_result AS "resultDescription",
           tmworr.eid_request_activity AS "eidActivityRequest",
           tmworr.dt_creation AS "dtCreation",
           NULL AS "flgProcessed",
           'DD01' AS "tenant"
       FROM flat.flat.t_monitoring_work_order_request_rejected tmworr
       WHERE tmworr.activity_type = 'RVER' -- AND tmworr.request_system = 'II4ER'
       ORDER BY "idWorkOrderActivity"
) AS visit_request;